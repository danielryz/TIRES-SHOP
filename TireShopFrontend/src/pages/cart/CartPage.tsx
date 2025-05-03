import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  CartItem,
  clearCart,
  deleteCartItem,
  getCartWithImages,
  updateCartItem,
} from "../../api/cartApi";
import "./CartPage.css";
import AlertStack from "../../components/alert/AlertStack";
import { useCart } from "../../context/CartContext";

function CartPage() {
  const [items, setItems] = useState<CartItem[]>([]);
  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);
  const nextId = useRef(0);

  const navigate = useNavigate();
  const { refreshCart } = useCart();
  const showAlert = (message: string, type: "success" | "error") => {
    const id = nextId.current++;
    setAlerts((prev) => [...prev, { id, message, type }]);
  };

  const removeAlert = (id: number) => {
    setAlerts((prev) => prev.filter((a) => a.id !== id));
  };

  const fetchCart = async () => {
    try {
      const data = await getCartWithImages();
      data.sort((a, b) => a.productId - b.productId);
      setItems(data);
    } catch {
      showAlert("Nie udało się pobrać koszyka", "error");
    }
  };

  useEffect(() => {
    fetchCart();
  }, []);

  const handleUpdate = async (id: number, quantity: number) => {
    try {
      await updateCartItem(id, quantity);
      await fetchCart();
      refreshCart();
    } catch {
      showAlert("Błąd przy aktualizacji ilości", "error");
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteCartItem(id);
      await fetchCart();
      refreshCart();
    } catch {
      showAlert("Nie udało się usunąć pozycji", "error");
    }
  };

  const handleClear = async () => {
    try {
      await clearCart();
      refreshCart();
      await fetchCart();
    } catch {
      showAlert("Nie udało się wyczyścić koszyka", "error");
    }
  };

  const total = items.reduce((acc, item) => acc + item.totalPrice, 0);

  return (
    <div className="cart-page">
      <h1>Twój koszyk</h1>

      {items.length === 0 ? (
        <p>Koszyk jest pusty.</p>
      ) : (
        <>
          <div className="cart-list">
            {items.map((item) => (
              <div key={item.id} className="cart-item">
                <img
                  src={item.imageUrl}
                  alt={item.productName}
                  className="cart-image"
                />
                <div className="cart-info">
                  <h3>{item.productName}</h3>
                  <p>{item.pricePerItem.toFixed(2)} zł / szt.</p>
                  <div className="cart-quantity">
                    <button
                      onClick={() => handleUpdate(item.id, item.quantity - 1)}
                      disabled={item.quantity <= 1}
                    >
                      -
                    </button>
                    <span>{item.quantity}</span>
                    <button
                      onClick={() => handleUpdate(item.id, item.quantity + 1)}
                    >
                      +
                    </button>
                  </div>
                  <p>
                    <strong>Suma:</strong> {item.totalPrice.toFixed(2)} zł
                  </p>
                  <button
                    className="remove-btn"
                    onClick={() => handleDelete(item.id)}
                  >
                    Usuń
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="cart-summary">
            <p>
              <strong>Łącznie:</strong> {total.toFixed(2)} zł
            </p>
            <button className="clear-btn" onClick={handleClear}>
              Wyczyść koszyk
            </button>
            <button
              className="checkout-btn"
              onClick={() => navigate("/checkout")}
              disabled={items.length === 0}
            >
              Przejdź do zamówienia
            </button>
          </div>
        </>
      )}
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default CartPage;
