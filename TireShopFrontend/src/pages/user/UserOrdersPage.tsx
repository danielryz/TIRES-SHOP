import { useEffect, useRef, useState } from "react";
import { cancelOrder, getUserOrders } from "../../api/ordersApi";
import { OrderResponse } from "../../types/Order";
import AlertStack from "../../components/alert/AlertStack";
import "./UserOrdersPage.css";
import ConfirmModal from "../../components/ConfirmModal";

function UserOrdersPage() {
  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [loading, setLoading] = useState(true);

  const [selectedOrderId, setSelectedOrderId] = useState<number | null>(null);
  const [showConfirm, setShowConfirm] = useState(false);

  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);
  const nextId = useRef(0);
  const showAlert = (message: string, type: "success" | "error") => {
    const id = nextId.current++;
    setAlerts((prev) => [...prev, { id, message, type }]);
  };

  const removeAlert = (id: number) => {
    setAlerts((prev) => prev.filter((a) => a.id !== id));
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const data = await getUserOrders();
      setOrders(data);
    } catch {
      showAlert("Błąd podczas ładowania zamówień.", "error");
    } finally {
      setLoading(false);
    }
  };

  const handleCancelClick = (orderId: number) => {
    setSelectedOrderId(orderId);
    setShowConfirm(true);
  };

  const confirmCancel = async () => {
    if (!selectedOrderId) return;
    try {
      await cancelOrder(selectedOrderId);
      setOrders((prev) =>
        prev.map((o) =>
          o.id === selectedOrderId ? { ...o, status: "CANCELLED" } : o,
        ),
      );
    } catch (err) {
      console.error("Błąd anulowania zamówienia:", err);
    } finally {
      setShowConfirm(false);
      setSelectedOrderId(null);
    }
  };

  const cancelModal = () => {
    setShowConfirm(false);
    setSelectedOrderId(null);
  };

  if (loading) return <p>Ładowanie zamówień...</p>;

  return (
    <div className="user-orders-page">
      <h2>Twoje zamówienia</h2>
      {orders.length === 0 ? (
        <p>Nie masz jeszcze żadnych zamówień.</p>
      ) : (
        orders.map((order) => (
          <div key={order.id} className="order-card">
            <div className="order-header">
              <div>
                Nr zamówienia: <strong>#{order.id}</strong>
              </div>
              <div>
                Status:{" "}
                <span className={`status ${order.status.toLowerCase()}`}>
                  {order.status}
                </span>
              </div>
              <div>Data: {new Date(order.createdAt).toLocaleString()}</div>
            </div>
            <div className="order-items">
              {order.items.map((item) => (
                <div key={item.id} className="order-item">
                  <div>{item.productName}</div>
                  <div>{item.quantity} szt.</div>
                  <div>{item.totalPrice.toFixed(2)} zł</div>
                </div>
              ))}
            </div>
            <div className="order-footer">
              <div className="order-total">
                Łącznie: <strong>{order.totalAmount.toFixed(2)} zł</strong>
              </div>
              {order.status === "CREATED" && (
                <button
                  onClick={() => handleCancelClick(order.id)}
                  className="cancel-order-btn"
                >
                  Anuluj
                </button>
              )}
            </div>
          </div>
        ))
      )}
      {showConfirm && (
        <ConfirmModal
          message="Czy na pewno chcesz anulować to zamówienie?"
          onConfirm={confirmCancel}
          onCancel={cancelModal}
        />
      )}
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default UserOrdersPage;
