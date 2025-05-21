import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getOrderByIdAdmin, updateOrderStatus } from "../../../api/ordersApi";
import { OrderResponse, OrderStatus } from "../../../types/Order";
import AlertStack from "../../../components/alert/AlertStack";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
import "./AdminOrderDetailPage.css";

function AdminOrderDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [order, setOrder] = useState<OrderResponse | null>(null);
  const [status, setStatus] = useState<OrderStatus | null>(null);
  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const OrderStatusLabels: Record<OrderStatus, string> = {
    [OrderStatus.CANCELLED]: "Anulowane",
    [OrderStatus.COMPLETED]: "Zakończone",
    [OrderStatus.CONFIRMED]: "Potwierdzone",
    [OrderStatus.CREATED]: "Nowe",
    [OrderStatus.IN_PROGRESS]: "W realizacji",
  };

  useEffect(() => {
    if (!id) return;
    setLoading(true);
    getOrderByIdAdmin(Number(id))
      .then((data) => {
        setOrder(data);
        setStatus(data.status as OrderStatus);
      })
      .catch(() => {
        showAlert("Błąd przy pobieraniu szczegółów zamówienia", "error");
      })
      .finally(() => setLoading(false));
  }, [id]);

  const showAlert = (message: string, type: "success" | "error") => {
    const alertId = Date.now() + Math.random();
    setAlerts((prev) => [...prev, { id: alertId, message, type }]);
  };

  const removeAlert = (id: number) => {
    setAlerts((prev) => prev.filter((a) => a.id !== id));
  };

  const handleSave = async () => {
    if (!order) return;
    setSaving(true);
    try {
      await updateOrderStatus(order.id, status as OrderStatus);
      showAlert("Status zamówienia został zaktualizowany", "success");
      const updatedOrder = await getOrderByIdAdmin(order.id);
      setOrder(updatedOrder);
    } catch {
      showAlert("Błąd przy aktualizacji statusu zamówienia", "error");
    } finally {
      setSaving(false);
    }
  };

  if (loading)
    return <p className="loading-message">Ładowanie danych zamówienia...</p>;
  if (!order) return <p className="error-message">Nie znaleziono zamówienia</p>;

  return (
    <>
      <AdminNavbar />
      <main className="order-detail-page">
        <h2 className="order-detail-title">Szczegóły zamówienia #{order.id}</h2>

        <div className="order-detail-content">
          {/* Lewa kolumna - 3 tabele */}
          <div className="left-column">
            <section className="order-section">
              <h3>Dane użytkownika</h3>
              <table className="order-table">
                <tbody>
                  <tr>
                    <th>Użytkownik</th>
                    <td>
                      {order.user
                        ? `Użytkownik #${order.user.id}`
                        : `Gość #${order.clientId}`}
                    </td>
                  </tr>
                  <tr>
                    <th>Imię i nazwisko</th>
                    <td>
                      {order.user
                        ? `${order.user.firstName} ${order.user.lastName}`
                        : `${order.guestFirstName} ${order.guestLastName}`}
                    </td>
                  </tr>
                  <tr>
                    <th>Email</th>
                    <td>{order.user ? order.user.email : order.guestEmail}</td>
                  </tr>
                  <tr>
                    <th>Telefon</th>
                    <td>
                      {order.user
                        ? order.user.phoneNumber
                        : order.guestPhoneNumber}
                    </td>
                  </tr>
                </tbody>
              </table>
            </section>

            <section className="order-section">
              <h3>Dane zamówienia</h3>
              <table className="order-table">
                <tbody>
                  <tr>
                    <th>Kwota zamówienia</th>
                    <td>{order.totalAmount} zł</td>
                  </tr>
                  <tr>
                    <th>Opłacone</th>
                    <td>{order.isPaid ? "Tak" : "Nie"}</td>
                  </tr>
                  <tr>
                    <th>Data płatności</th>
                    <td>
                      {order.paidAt
                        ? new Date(order.paidAt).toLocaleString()
                        : "-"}
                    </td>
                  </tr>
                  <tr>
                    <th>Utworzone</th>
                    <td>{new Date(order.createdAt).toLocaleString()}</td>
                  </tr>
                  <tr>
                    <th>Status zamówienia:</th>
                    <td>
                      <select
                        id="order-status-select"
                        className="order-status-select"
                        value={status ?? ""}
                        onChange={(e) => {
                          const val = e.target.value;
                          if (
                            val === "" ||
                            Object.values(OrderStatus).includes(
                              val as OrderStatus,
                            )
                          ) {
                            setStatus(val as OrderStatus | null);
                          }
                        }}
                      >
                        {Object.entries(OrderStatusLabels).map(
                          ([key, label]) => (
                            <option key={key} value={key}>
                              {label}
                            </option>
                          ),
                        )}
                      </select>
                      <button
                        className="save-status-button"
                        onClick={handleSave}
                        disabled={saving}
                        aria-busy={saving}
                        style={{ marginLeft: "10px" }}
                      >
                        {saving ? "Zapisywanie..." : "Zapisz zmiany"}
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </section>

            <section className="order-section">
              <h3>Adres dostawy</h3>
              <table className="order-table">
                <tbody>
                  <tr>
                    <th>Ulica</th>
                    <td>{order.shippingAddress.street}</td>
                  </tr>
                  <tr>
                    <th>Numer domu</th>
                    <td>{order.shippingAddress.houseNumber}</td>
                  </tr>
                  <tr>
                    <th>Numer lokalu</th>
                    <td>{order.shippingAddress.apartmentNumber ?? "-"}</td>
                  </tr>
                  <tr>
                    <th>Kod pocztowy</th>
                    <td>{order.shippingAddress.postalCode}</td>
                  </tr>
                  <tr>
                    <th>Miasto</th>
                    <td>{order.shippingAddress.city}</td>
                  </tr>
                </tbody>
              </table>
            </section>
          </div>

          {/* Prawa kolumna - tabela z produktami */}
          <div className="right-column">
            <section className="order-section">
              <h3>Lista produktów zamówienia</h3>
              <table className="order-table">
                <thead>
                  <tr>
                    <th>ID produktu</th>
                    <th>Nazwa produktu</th>
                    <th>Ilość</th>
                    <th>Cena za sztukę</th>
                    <th>Pełna cena</th>
                  </tr>
                </thead>
                <tbody>
                  {order.items.map((item) => (
                    <tr key={item.id}>
                      <td>{item.id}</td>
                      <td>{item.productName}</td>
                      <td>{item.quantity}</td>
                      <td>{item.priceAtPurchase} zł</td>
                      <td>{item.totalPrice} zł</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </section>
          </div>
        </div>

        {/* Sekcja statusu + przycisk */}
        <section className="order-status-section"></section>

        <AlertStack alerts={alerts} onRemove={removeAlert} />
      </main>
    </>
  );
}

export default AdminOrderDetailPage;
