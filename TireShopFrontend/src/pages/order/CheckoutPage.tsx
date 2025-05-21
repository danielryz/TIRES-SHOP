import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getCartWithImages, CartItem } from "../../api/cartApi";
import { createOrder } from "../../api/ordersApi";
import { getUserProfile } from "../../api/userApi";
import { getAddressByType } from "../../api/addressesApi";
import AlertStack from "../../components/alert/AlertStack";
import { useCart } from "../../context/CartContext";
import "./CheckoutPage.css";

interface Address {
  id: number;
  street: string;
  houseNumber: string;
  apartmentNumber?: string;
  postalCode: string;
  city: string;
  type: string;
}

function CheckoutPage() {
  const navigate = useNavigate();
  const [step, setStep] = useState(1);
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [selectedAddressId, setSelectedAddressId] = useState<number | "new">(
    "new",
  );
  const { refreshCart } = useCart();

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    street: "",
    houseNumber: "",
    apartmentNumber: "",
    postalCode: "",
    city: "",
  });

  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);

  const showAlert = (message: string, type: "success" | "error") => {
    const id = Date.now() + Math.random();
    setAlerts((prev) => [...prev, { id, message, type }]);
  };

  const removeAlert = (id: number) => {
    setAlerts((prev) => prev.filter((a) => a.id !== id));
  };

  const handleInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    const checkUser = async () => {
      try {
        const user = await getUserProfile();
        setIsLoggedIn(true);
        setFormData((prev) => ({
          ...prev,
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email,
          phone: user.phoneNumber,
        }));
        const savedAddresses = await getAddressByType("SHIPPING");
        setAddresses(savedAddresses);
        if (savedAddresses.length > 0)
          setSelectedAddressId(savedAddresses[0].id);
      } catch {
        setIsLoggedIn(false);
      }
    };
    checkUser();
  }, []);

  const handleNext = async () => {
    if (
      !formData.firstName ||
      !formData.lastName ||
      !formData.email ||
      !formData.phone
    ) {
      showAlert("Uzupełnij dane kontaktowe.", "error");
      return;
    }
    try {
      const items = await getCartWithImages();
      setCartItems(items);
      setStep(2);
    } catch {
      showAlert("Nie udało się pobrać koszyka.", "error");
    }
  };

  const handleOrder = async () => {
    if (
      !formData.street ||
      !formData.houseNumber ||
      !formData.postalCode ||
      !formData.city
    ) {
      showAlert("Uzupełnij adres dostawy.", "error");
      return;
    }

    try {
      setLoading(true);
      const items = cartItems.map((item) => ({
        productId: item.productId,
        quantity: item.quantity,
      }));

      const orderRequest = {
        guestFirstName: formData.firstName,
        guestLastName: formData.lastName,
        guestEmail: formData.email,
        guestPhoneNumber: formData.phone,
        street: formData.street,
        houseNumber: formData.houseNumber,
        apartmentNumber: formData.apartmentNumber || undefined,
        postalCode: formData.postalCode,
        city: formData.city,
        items,
      };

      const createdOrder = await createOrder(orderRequest);

      showAlert("Zamówienie złożone pomyślnie!", "success");
      refreshCart();

      setTimeout(() => navigate(`/payment/${createdOrder.id}`), 10000);
    } catch {
      showAlert("Nie udało się złożyć zamówienia.", "error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (selectedAddressId !== "new") {
      const selected = addresses.find((addr) => addr.id === selectedAddressId);
      if (selected) {
        setFormData((prev) => ({
          ...prev,
          street: selected.street,
          houseNumber: selected.houseNumber,
          apartmentNumber: selected.apartmentNumber || "",
          postalCode: selected.postalCode,
          city: selected.city,
        }));
      }
    } else {
      setFormData((prev) => ({
        ...prev,
        street: "",
        houseNumber: "",
        apartmentNumber: "",
        postalCode: "",
        city: "",
      }));
    }
  }, [selectedAddressId, addresses]);

  return (
    <div className="checkout-page">
      <h1>Składanie zamówienia</h1>

      {step === 1 && (
        <div className="step-box">
          <h2>Dane kontaktowe</h2>
          <input
            name="firstName"
            placeholder="Imię"
            onChange={handleInput}
            value={formData.firstName}
          />
          <input
            name="lastName"
            placeholder="Nazwisko"
            onChange={handleInput}
            value={formData.lastName}
          />
          <input
            name="email"
            placeholder="Email"
            onChange={handleInput}
            value={formData.email}
          />
          <input
            name="phone"
            placeholder="Telefon"
            onChange={handleInput}
            value={formData.phone}
          />
          <button className="step-btn" onClick={handleNext}>
            Dalej
          </button>
        </div>
      )}

      {step === 2 && (
        <div className="step-box">
          <h2>Adres dostawy</h2>
          {isLoggedIn && addresses.length > 0 && (
            <div className="existing-addresses">
              {addresses.map((addr) => (
                <div
                  key={addr.id}
                  className={`address-card ${selectedAddressId === addr.id ? "selected" : ""}`}
                  onClick={() => setSelectedAddressId(addr.id)}
                >
                  <p>
                    <strong>
                      {addr.street} {addr.houseNumber}
                    </strong>
                  </p>
                  <p>{addr.apartmentNumber && `m. ${addr.apartmentNumber}`}</p>
                  <p>
                    {addr.postalCode} {addr.city}
                  </p>
                </div>
              ))}
              <div
                className={`address-card ${selectedAddressId === "new" ? "selected" : ""}`}
                onClick={() => setSelectedAddressId("new")}
              >
                <p>Użyj nowego adresu</p>
              </div>
            </div>
          )}

          {selectedAddressId === "new" && (
            <>
              <input
                name="street"
                placeholder="Ulica"
                onChange={handleInput}
                value={formData.street}
              />
              <input
                name="houseNumber"
                placeholder="Nr domu"
                onChange={handleInput}
                value={formData.houseNumber}
              />
              <input
                name="apartmentNumber"
                placeholder="Nr mieszkania"
                onChange={handleInput}
                value={formData.apartmentNumber}
              />
              <input
                name="postalCode"
                placeholder="Kod pocztowy"
                onChange={handleInput}
                value={formData.postalCode}
              />
              <input
                name="city"
                placeholder="Miasto"
                onChange={handleInput}
                value={formData.city}
              />
            </>
          )}

          <h3>Podsumowanie</h3>
          <ul className="summary-list">
            {cartItems.map((item) => (
              <li key={item.id}>
                {item.productName} x{item.quantity} –{" "}
                {item.totalPrice.toFixed(2)} zł
              </li>
            ))}
          </ul>

          <p className="summary-total">
            <strong>Suma:</strong>{" "}
            {cartItems.reduce((a, b) => a + b.totalPrice, 0).toFixed(2)} zł
          </p>

          <button
            className="order-btn"
            disabled={loading}
            onClick={handleOrder}
          >
            {loading ? "Przetwarzanie..." : "Zamawiam"}
          </button>
        </div>
      )}

      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default CheckoutPage;
