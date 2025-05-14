import { useNavigate, useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";
import { getUserOrderById, payForYourOrder } from "../../api/ordersApi";
import AlertStack from "../../components/alert/AlertStack";
import { AxiosError } from "axios";
import { OrderResponse } from "../../types/Order";
import "./PaymentPage.css";
function PaymentPage() {
  const { orderId } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState<OrderResponse | null>(null);
  const [loading, setLoading] = useState(true);
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

  const fetchOrder = async (orderId: number) => {
    try {
      const data = await getUserOrderById(orderId);
      setOrder(data);
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Nie znaleziono zamówienia.";
      showAlert(message, "error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!orderId) return;
    const parsedId = parseInt(orderId);
    if (!isNaN(parsedId)) {
      fetchOrder(parsedId);
    } else {
      showAlert("Nieprawidłowe ID zamówienia.", "error");
      setLoading(false);
    }
  }, [orderId]);

  const handlePayNow = async () => {
    if (!order?.id) return;
    try {
      const message = await payForYourOrder(order.id);
      showAlert(message || "Płatność zakończona sukcesem", "success");
      setTimeout(() => navigate("/"), 2000);
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Wystąpił problem z płatnością.";
      showAlert(message, "error");
    }
  };

  const handlePayLater = () => {
    navigate("/");
  };

  if (loading) return <p>Ładowanie...</p>;
  if (!order) return <p>Nie znaleziono zamówienia.</p>;

  return (
    <div className="payment-page">
      <h1>Podsumowanie płatności</h1>
      <p>Zamówienie: #{order.id}</p>
      <p>Kwota: {order.totalAmount} zł</p>
      <p>Status: {order.isPaid ? "OPŁACONE" : "NIEOPŁACONE"}</p>
      <div className="payment-buttons">
        {!order.isPaid ? (
          <>
            <button className="pay-btn" onClick={handlePayNow}>
              Zapłać teraz
            </button>
            <button className="later-btn" onClick={handlePayLater}>
              Zapłać później
            </button>
          </>
        ) : (
          <p>To zamówienie jest już opłacone.</p>
        )}
      </div>
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default PaymentPage;
