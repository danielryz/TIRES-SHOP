import {
  changeUserPassword,
  deleteUserData,
  getUserProfile,
  updateUserData,
} from "../../api/userApi";
import { useEffect, useState } from "react";
import { User } from "../../types/User";
import "./SettingsPage.css";
import ConfirmModal from "../../components/ConfirmModal";
import AlertStack from "../../components/alert/AlertStack";
import { AxiosError } from "axios";

function SettingsPage() {
  const [form, setForm] = useState({
    username: "",
    firstName: "",
    lastName: "",
    phoneNumber: "",
    email: "",
  });
  const [newPassword, setNewPassword] = useState("");
  const [currentPassword, setCurrentPassword] = useState("");
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

  useEffect(() => {
    getUserProfile().then((user: User) => {
      setForm({
        username: user.username,
        firstName: user.firstName,
        lastName: user.lastName,
        phoneNumber: user.phoneNumber,
        email: user.email,
      });
    });
  }, []);

  const handleDataUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await updateUserData(form);
      showAlert(response, "success");
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Błąd przy aktualizacji danych.";
      showAlert(message, "error");
    }
  };

  const handlePasswordChange = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await changeUserPassword({
        password: currentPassword,
        newPassword,
      });
      showAlert(response, "success");

      setCurrentPassword("");
      setNewPassword("");
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Wystąpił błąd przy zmianie hasła.";
      showAlert(message, "error");
    }
  };

  const handleClearData = () => {
    setShowConfirm(true);
  };

  const confirmClearData = async () => {
    setShowConfirm(false);
    try {
      const response = await deleteUserData();
      showAlert(response, "success");
      setForm((prev) => ({
        ...prev,
        firstName: "",
        lastName: "",
        phoneNumber: "",
      }));
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Nie udało się usunąć danych.";
      showAlert(message, "error");
    }
  };

  return (
    <div className="profile-settings">
      <h2>Ustawienia konta</h2>

      <form onSubmit={handleDataUpdate}>
        <h3>Dane użytkownika</h3>
        <label>
          Nazwa użytkownika:
          <input
            name="username"
            value={form.username || ""}
            onChange={(e) => setForm({ ...form, username: e.target.value })}
          />
        </label>
        <label>
          E-mail:
          <input name="email" value={form.email || ""} readOnly />
        </label>
        <label>
          Imię:
          <input
            name="firstName"
            value={form.firstName || ""}
            onChange={(e) => setForm({ ...form, firstName: e.target.value })}
          />
        </label>
        <label>
          Nazwisko:
          <input
            name="lastName"
            value={form.lastName || ""}
            onChange={(e) => setForm({ ...form, lastName: e.target.value })}
          />
        </label>
        <label>
          Numer telefonu:
          <input
            name="phoneNumber"
            value={form.phoneNumber || ""}
            onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })}
          />
        </label>
        <button type="submit">Zapisz dane</button>
        <button
          type="button"
          className="clear-data-btn"
          onClick={handleClearData}
        >
          Usuń dane osobowe
        </button>
      </form>

      <form onSubmit={handlePasswordChange}>
        <h3>Zmiana hasła</h3>
        <label>
          Obecne hasło:
          <input
            type="password"
            value={currentPassword || ""}
            onChange={(e) => setCurrentPassword(e.target.value)}
          />
        </label>
        <label>
          Nowe hasło:
          <input
            type="password"
            value={newPassword || ""}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </label>
        <button type="submit">Zmień hasło</button>
      </form>
      {showConfirm && (
        <ConfirmModal
          message="Czy na pewno chcesz usunąć swoje dane osobowe?"
          onConfirm={confirmClearData}
          onCancel={() => setShowConfirm(false)}
        />
      )}
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default SettingsPage;
