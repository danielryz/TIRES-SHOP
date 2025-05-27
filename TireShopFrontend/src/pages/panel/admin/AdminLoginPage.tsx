import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../../../api/authApi";
import { isAdmin } from "../../../utils/authUtils";
import logo from "../../../assets/logo.png";
import "./AdminLoginPage.css";
import AlertStack from "../../../components/alert/AlertStack";
import { AxiosError } from "axios";

function AdminLoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await loginUser({ email, password });
      localStorage.setItem("token", response.token);

      if (isAdmin()) {
        navigate("/admin");
      } else {
        showAlert("Nie jesteś administratorem.", "error");
        localStorage.removeItem("token");
      }
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message = error.response?.data?.message || "Błąd logowania admina.";
      showAlert(message, "error");
    }
  };

  return (
    <div className="admin-container">
      <div className="admin-card">
        <Link to={"/"} className="admin-logo">
          <img src={logo} alt="Tire Shop Admin Logo" />
        </Link>
        <h1 className="admin-title">TIRE SHOP PANEL</h1>
        <form onSubmit={handleSubmit} className="admin-form">
          <input
            type="email"
            placeholder="Admin Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="admin-input"
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="admin-input"
          />
          <button type="submit" className="admin-button">
            Zaloguj się
          </button>
        </form>
      </div>
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default AdminLoginPage;
