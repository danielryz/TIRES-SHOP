import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import {
  getUserById,
  addRoleToUser,
  removeUserRole,
  getRoles,
  deleteUserById,
} from "../../../api/userApi";
import { Role, User } from "../../../types/User";
import "./AdminUserDetailPage.css";
import ConfirmModalPanel from "../../../components/ConfirmModalPanel";
import { AxiosError } from "axios";
import AlertStack from "../../../components/alert/AlertStack";
function AdminUserDetailPage() {
  const { id } = useParams();
  const [user, setUser] = useState<User | null>(null);
  const [userToDelete, setUserToDelete] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [userRoles, setUserRoles] = useState<string[]>([]);
  const [roles, setRoles] = useState<Role[]>([]);
  const [selectedRoleId, setSelectedRoleId] = useState<number | null>(null);
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

  useEffect(() => {
    const fetchData = async () => {
      if (!id) return;
      setLoading(true);
      try {
        const [userData, allRoles] = await Promise.all([
          getUserById(parseInt(id)),
          getRoles(),
        ]);
        setUser(userData);
        setUserRoles(userData.roles || []);
        setRoles(allRoles);
      } catch {
        setUser(null);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const handleRoleToggle = async () => {
    if (!user || selectedRoleId === null) return;

    const selectedRole = roles.find((r) => r.id === selectedRoleId);
    if (!selectedRole) return;

    const roleName = selectedRole.name;

    try {
      if (userRoles.includes(roleName)) {
        await removeUserRole(user.id, selectedRoleId);
        setUserRoles((prev) => prev.filter((r) => r !== roleName));
      } else {
        await addRoleToUser(user.id, selectedRoleId);
        setUserRoles((prev) => [...prev, roleName]);
      }
    } catch (error) {
      console.error("Błąd przy aktualizacji roli:", error);
    }
  };

  const confirmDelete = async () => {
    if (!userToDelete) return;
    try {
      const message = await deleteUserById(userToDelete);
      showAlert(message || "Użytkownik został usunięty", "success");
      setShowConfirm(false);
      setUserToDelete(null);
      setTimeout(() => navigate("/admin/users"), 2000);
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
    <div className="user-detail-container">
      {loading ? (
        <p>Ładowanie...</p>
      ) : user ? (
        <>
          <h2>{user.username}</h2>
          <p>Email: {user.email}</p>
          <p>Imię: {user.firstName}</p>
          <p>Nazwisko: {user.lastName}</p>
          <p>Telefon: {user.phoneNumber}</p>
          <p>
            Role:{" "}
            {userRoles.length > 0
              ? userRoles.join(", ")
              : "Brak przypisanych ról"}
          </p>

          <div className="user-role-form">
            <select
              value={selectedRoleId ?? ""}
              onChange={(e) => setSelectedRoleId(parseInt(e.target.value))}
            >
              {roles.map((role) => (
                <option key={role.id} value={role.id}>
                  {role.name.replace("ROLE_", "")}
                </option>
              ))}
            </select>

            <button
              className="admin-user-update-role-btn"
              onClick={handleRoleToggle}
            >
              {userRoles.includes(
                roles.find((r) => r.id === selectedRoleId)?.name || "",
              )
                ? "Usuń rolę"
                : "Dodaj rolę"}
            </button>

            <button
              className="admin-user-delete-btn"
              onClick={(e) => {
                e.stopPropagation();
                handleDeleteClick(user.id); // poprawione
              }}
            >
              Usuń użytkownika
            </button>
          </div>
        </>
      ) : (
        <p>Nie znaleziono użytkownika</p>
      )}
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
    </div>
  );
}

export default AdminUserDetailPage;
