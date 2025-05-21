import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AlertStack from "../../../components/alert/AlertStack";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
import { getUsers, deleteUserById } from "../../../api/userApi";
import { User, UserFilterParams } from "../../../types/User";
import "./AdminUserPanelPage.css";
import ConfirmModalPanel from "../../../components/ConfirmModalPanel";
import { AxiosError } from "axios";

function AdminUserPanelPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [userToDelete, setUserToDelete] = useState<number | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [filters, setFilters] = useState<UserFilterParams>({});
  const [filterInputs, setFilterInputs] = useState<UserFilterParams>({});
  const [page, setPage] = useState<number>(0);
  const [sizePerPage, setSizePerPage] = useState<number>(10);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [showConfirm, setShowConfirm] = useState(false);
  const navigate = useNavigate();
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

  const loadUsers = async () => {
    try {
      setLoading(true);
      const data = await getUsers({ ...filters, page, sizePerPage });
      setUsers(data.content);
      setTotalPages(data.totalPages);
    } catch {
      showAlert("Błąd podczas pobierania użytkowników", "error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, [filters, page, sizePerPage]);

  const confirmDelete = async () => {
    if (!userToDelete) return;
    try {
      const message = await deleteUserById(userToDelete);
      showAlert(message || "Użytkownik został usunięty", "success");
      setShowConfirm(false);
      setUserToDelete(null);
      loadUsers();
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Nie udało się usunąć użytkownika";
      showAlert(message, "error");
      setShowConfirm(false);
      setUserToDelete(null);
    }
  };

  const handleDeleteClick = (id: number) => {
    setUserToDelete(id);
    setShowConfirm(true);
  };

  return (
    <>
      <AdminNavbar />
      <div
        className="admin-panel-container admin-user-panel"
        style={{ marginTop: "80px" }}
      >
        <h2 className="admin-panel-title" style={{ marginBottom: 30 }}></h2>

        <div
          className="panel-grid"
          style={{ maxWidth: 800, margin: "0 auto 40px" }}
        >
          <div className="panel-card">
            <div className="admin-user-header">
              <h1 className="admin-panel-title">Panel użytkowników</h1>
              <p className="admin-panel-subtitle">
                Zarządzaj użytkownikami, przypisuj role i przeglądaj dane
                kontaktowe.
              </p>
            </div>
            <div className="admin-user-filter-form">
              <input
                className="admin-user-filter-input"
                placeholder="Email"
                value={filterInputs.email || ""}
                onChange={(e) =>
                  setFilterInputs({ ...filterInputs, email: e.target.value })
                }
              />
              <input
                className="admin-user-filter-input"
                placeholder="Nazwa użytkownika"
                value={filterInputs.username || ""}
                onChange={(e) =>
                  setFilterInputs({ ...filterInputs, username: e.target.value })
                }
              />
              <input
                className="admin-user-filter-input"
                placeholder="Imię"
                value={filterInputs.firstName || ""}
                onChange={(e) =>
                  setFilterInputs({
                    ...filterInputs,
                    firstName: e.target.value,
                  })
                }
              />
              <input
                className="admin-user-filter-input"
                placeholder="Nazwisko"
                value={filterInputs.lastName || ""}
                onChange={(e) =>
                  setFilterInputs({ ...filterInputs, lastName: e.target.value })
                }
              />
              <input
                className="admin-user-filter-input"
                placeholder="Telefon"
                value={filterInputs.phoneNumber || ""}
                onChange={(e) =>
                  setFilterInputs({
                    ...filterInputs,
                    phoneNumber: e.target.value,
                  })
                }
              />
              <select
                className="admin-user-filter-input"
                value={filterInputs.role || ""}
                onChange={(e) =>
                  setFilterInputs({ ...filterInputs, role: e.target.value })
                }
              >
                <option value="">Wszystkie role</option>
                <option value="ROLE_USER">USER</option>
                <option value="ROLE_ADMIN">ADMIN</option>
                <option value="ROLE_MANAGER">MANAGER</option>
              </select>

              <select
                className="admin-user-filter-input"
                value={filterInputs.sort?.[0] || "id,asc"}
                onChange={(e) =>
                  setFilterInputs({ ...filterInputs, sort: e.target.value })
                }
              >
                <option value="id,asc">ID rosnąco</option>
                <option value="id,desc">ID malejąco</option>
                <option value="firstName,asc">Imię A-Z</option>
                <option value="firstName,desc">Imię Z-A</option>
                <option value="lastName,asc">Nazwisko A-Z</option>
                <option value="lastName,desc">Nazwisko Z-A</option>
                <option value="email,asc">Email Z-A</option>
                <option value="email,desc">Email Z-A</option>
              </select>

              <select
                className="admin-user-filter-input"
                value={sizePerPage}
                onChange={(e) => setSizePerPage(parseInt(e.target.value))}
              >
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="25">25</option>
                <option value="50">50</option>
              </select>

              <button
                className="admin-user-filter-btn"
                onClick={() => {
                  setFilters(filterInputs);
                  setPage(0);
                }}
              >
                Szukaj
              </button>
            </div>
          </div>
        </div>

        {loading ? (
          <p className="admin-user-loading">Ładowanie...</p>
        ) : (
          <table
            className="admin-user-table panel-card"
            style={{ maxWidth: 800, margin: "0 auto 40px" }}
          >
            <thead>
              <tr>
                <th>ID</th>
                <th>Nazwa</th>
                <th>Email</th>
                <th>Imię i nazwisko</th>
                <th>Telefon</th>
                <th>Akcje</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr
                  key={u.id}
                  onClick={() => navigate(`/admin/users/${u.id}`)}
                  style={{ cursor: "pointer" }}
                >
                  <td>{u.id}</td>
                  <td>{u.username}</td>
                  <td>{u.email}</td>
                  <td>
                    {u.firstName} {u.lastName}
                  </td>
                  <td>{u.phoneNumber}</td>
                  <td className="admin-user-actions">
                    <button
                      className="admin-user-delete-btn"
                      onClick={(e) => {
                        e.stopPropagation();
                        handleDeleteClick(u.id);
                      }}
                    >
                      Usuń
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        <div
          className="admin-user-pagination"
          style={{ maxWidth: 800, margin: "0 auto" }}
        >
          <button
            onClick={() => setPage((p) => Math.max(p - 1, 0))}
            disabled={page === 0}
          >
            Poprzednia
          </button>
          <span>
            Strona {page + 1} z {totalPages}
          </span>
          <button
            onClick={() => setPage((p) => p + 1)}
            disabled={page + 1 >= totalPages}
          >
            Następna
          </button>
        </div>
      </div>
      {showConfirm && (
        <ConfirmModalPanel
          message="Czy na pewno chcesz usunąć tego użytkownika?"
          onConfirm={confirmDelete}
          onCancel={() => {
            setShowConfirm(false);
            setUserToDelete(null);
          }}
        />
      )}

      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </>
  );
}

export default AdminUserPanelPage;
