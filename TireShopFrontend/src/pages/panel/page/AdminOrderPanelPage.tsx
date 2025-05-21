import { useEffect, useState } from "react";
import { getOrders } from "../../../api/ordersApi";
import {
  OrderFilterParams,
  OrderResponse,
  OrderStatus,
} from "../../../types/Order";
import AlertStack from "../../../components/alert/AlertStack";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
import "./AdminOrderPanelPage.css";
import { useNavigate } from "react-router-dom";

function AdminOrderPanelPage() {
  const OrderStatusLabels: Record<OrderStatus, string> = {
    [OrderStatus.CANCELLED]: "Anulowane",
    [OrderStatus.COMPLETED]: "Zakończone",
    [OrderStatus.CONFIRMED]: "Potwierdzone",
    [OrderStatus.CREATED]: "Nowe",
    [OrderStatus.IN_PROGRESS]: "W realizacji",
  };

  const navigate = useNavigate();
  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [paginationFilters, setPaginationFilters] = useState<OrderFilterParams>(
    {
      page: 0,
      sizePerPage: 10,
      sort: "createdAt,desc",
    },
  );
  const [filters, setFilters] = useState<OrderFilterParams>({
    page: 0,
    sizePerPage: 10,
    sort: "createdAt,desc",
  });
  const [totalPages, setTotalPages] = useState(0);
  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);
  const [loading, setLoading] = useState(false);

  const showAlert = (message: string, type: "success" | "error") => {
    const id = Date.now() + Math.random();
    setAlerts((prev) => [...prev, { id, message, type }]);
  };

  const removeAlert = (id: number) => {
    setAlerts((prev) => prev.filter((a) => a.id !== id));
  };

  useEffect(() => {
    fetchOrders();
  }, [paginationFilters]);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const response = await getOrders({
        ...filters,
        ...paginationFilters,
      });
      setOrders(response.content);
      setTotalPages(response.totalPages);
    } catch {
      showAlert("Błąd przy pobieraniu zamówień", "error");
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFilters((prev) => ({
      ...prev,
      [name]:
        value === "" ? undefined : name === "userId" ? parseInt(value) : value,
    }));
  };

  return (
    <>
      <AdminNavbar />
      <div className="admin-order-panel-container" style={{ marginTop: 80 }}>
        <h2>Zamówienia</h2>

        <div className="admin-order-filters">
          <label>
            ID użytkownika:
            <input
              type="number"
              name="userId"
              className="admin-order-input"
              value={filters.userId ?? ""}
              onChange={handleFilterChange}
            />
          </label>

          <label>
            Status:
            <select
              name="status"
              className="admin-order-select"
              value={filters.status ?? ""}
              onChange={handleFilterChange}
            >
              <option value="">Wszystkie</option>
              {Object.entries(OrderStatusLabels).map(([key, label]) => (
                <option key={key} value={key}>
                  {label}
                </option>
              ))}
            </select>
          </label>

          <label>
            Opłacone:
            <select
              name="isPaid"
              className="admin-order-select"
              value={
                filters.isPaid === undefined
                  ? ""
                  : filters.isPaid
                    ? "true"
                    : "false"
              }
              onChange={(e) =>
                setFilters((prev) => ({
                  ...prev,
                  isPaid:
                    e.target.value === ""
                      ? undefined
                      : e.target.value === "true",
                }))
              }
            >
              <option value="">Wszystkie</option>
              <option value="true">Tak</option>
              <option value="false">Nie</option>
            </select>
          </label>

          <label>
            Od daty utworzenia:
            <input
              type="date"
              name="createdAtFrom"
              className="admin-order-input"
              value={filters.createdAtFrom ?? ""}
              onChange={handleFilterChange}
            />
          </label>

          <label>
            Do daty utworzenia:
            <input
              type="date"
              name="createdAtTo"
              className="admin-order-input"
              value={filters.createdAtTo ?? ""}
              onChange={handleFilterChange}
            />
          </label>

          <label>
            Od daty opłacenia:
            <input
              type="date"
              name="paidAtFrom"
              className="admin-order-input"
              value={filters.paidAtFrom ?? ""}
              onChange={handleFilterChange}
            />
          </label>

          <label>
            Do daty opłacenia:
            <input
              type="date"
              name="paidAtTo"
              className="admin-order-input"
              value={filters.paidAtTo ?? ""}
              onChange={handleFilterChange}
            />
          </label>

          <label>
            Sortowanie:
            <select
              name="sort"
              className="admin-order-select"
              value={filters.sort}
              onChange={handleFilterChange}
            >
              <option value="">Domyślnie</option>
              <option value="createdAt,desc">Data utworzenia ↓</option>
              <option value="createdAt,asc">Data utworzenia ↑</option>
              <option value="status,desc">Status ↓</option>
              <option value="status,asc">Status ↑</option>
              <option value="id,desc">Id ↓</option>
              <option value="id,asc">Id ↑</option>
            </select>
          </label>

          <button
            className="admin-order-submit-btn"
            onClick={() =>
              setPaginationFilters({
                ...filters,
                page: 0,
                sizePerPage: paginationFilters.sizePerPage,
              })
            }
            disabled={loading}
          >
            {loading ? "Szukam..." : "Szukaj"}
          </button>
        </div>

        {loading ? (
          <p>Ładowanie zamówień...</p>
        ) : orders.length === 0 ? (
          <p>Brak zamówień</p>
        ) : (
          <table className="admin-order-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Użytkownik</th>
                <th>Status</th>
                <th>Kwota</th>
                <th>Opłacone</th>
                <th>Utworzone</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr
                  key={order.id}
                  onClick={() => navigate(`/admin/order/${order.id}`)}
                  style={{ cursor: "pointer" }}
                >
                  <td>{order.id}</td>
                  <td>
                    {order.user != null
                      ? `Użytkownik #${order.user.id}`
                      : `Gość #${order.clientId}`}
                  </td>
                  <td>{OrderStatusLabels[order.status as OrderStatus]}</td>
                  <td>{order.totalAmount} zł</td>
                  <td>{order.isPaid ? "Tak" : "Nie"}</td>
                  <td>{new Date(order.createdAt).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        <div className="admin-order-pagination">
          <button
            onClick={() =>
              setPaginationFilters((prev) => ({
                ...prev,
                page: Math.max((prev.page ?? 0) - 1, 0),
              }))
            }
            disabled={paginationFilters.page === 0}
            className="admin-order-page-btn"
          >
            Poprzednia
          </button>

          <span className="admin-order-page-info">
            Strona {(paginationFilters.page ?? 0) + 1} z {totalPages}
          </span>

          <button
            onClick={() =>
              setPaginationFilters((prev) => ({
                ...prev,
                page: Math.min((prev.page ?? 0) + 1, totalPages - 1),
              }))
            }
            disabled={(paginationFilters.page ?? 0) >= totalPages - 1}
            className="admin-order-page-btn"
          >
            Następna
          </button>

          <select
            className="admin-order-page-select"
            value={paginationFilters.sizePerPage}
            onChange={(e) => {
              const newSize = parseInt(e.target.value);
              setPaginationFilters((prev) => ({
                ...prev,
                page: 0,
                sizePerPage: newSize,
              }));
            }}
          >
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={25}>25</option>
            <option value={50}>50</option>
          </select>
        </div>

        <AlertStack alerts={alerts} onRemove={removeAlert} />
      </div>
    </>
  );
}

export default AdminOrderPanelPage;
