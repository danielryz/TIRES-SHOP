import { Link, useNavigate } from "react-router-dom";
import "./ProfilePage.css";
import { deleteUser } from "../../api/userApi";
import { useState } from "react";
import ConfirmModal from "../../components/ConfirmModal";
import AlertStack from "../../components/alert/AlertStack";
import { AxiosError } from "axios";

function ProfilePage() {
  const navigate = useNavigate();
  const [showConfirm, setShowConfirm] = useState(false);

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

  const confirmDelete = async () => {
    try {
      const message = await deleteUser();
      localStorage.removeItem("token");
      showAlert(message, "success");
      setShowConfirm(false);
      setTimeout(() => navigate("/"), 2000);
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Nie udało się usunąć konta.";
      showAlert(message, "error");
    }
  };
  return (
    <div className="profile-page">
      <h1>Witaj, użytkowniku!</h1>
      <div className="profile-options">
        <Link to="/profile/settings" className="profile-btn">
          Ustawienia konta
        </Link>
        <Link to="/profile/addresses" className="profile-btn">
          Książka adresowa
        </Link>
        <Link to="/profile/orders" className="profile-btn">
          Twoje zamówienia
        </Link>
        <button
          className="profile-btn delete"
          onClick={() => setShowConfirm(true)}
        >
          Usuń konto
        </button>
      </div>
      {showConfirm && (
        <ConfirmModal
          message="Czy na pewno chcesz usunąć swoje konto? Tego nie da się cofnąć."
          onConfirm={confirmDelete}
          onCancel={() => setShowConfirm(false)}
        />
      )}
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default ProfilePage;
