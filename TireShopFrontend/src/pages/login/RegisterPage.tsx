import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../../api/authApi";
import logo from "../../assets/logo.png";
import "./RegisterPage.css";
import AlertStack from "../../components/alert/AlertStack";
import { AxiosError } from "axios";

function RegisterPage() {
  const [username, setUsername] = useState("");
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
      const response = await registerUser({ username, email, password });
      localStorage.setItem("token", response.token);
      navigate("/");
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message = error.response?.data?.message || "Registration failed.";
      showAlert(message, "error");
    }
  };

  return (
    <div className="register-container">
      <div className="register-card">
        <Link to={"/"} className="register-logo">
          <img src={logo} alt="Tire Shop Logo" />
        </Link>
        <h1 className="register-title">REJESTRACJA</h1>
        <form onSubmit={handleSubmit} className="register-form">
          <input
            type="text"
            placeholder="Nazwa użytkownika"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            className="register-input"
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="register-input"
          />
          <input
            type="password"
            placeholder="Hasło"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="register-input"
          />
          <button type="submit" className="register-button">
            Zarejestruj się
          </button>
        </form>

        <div className="register-bottom-link">
          Masz już konto?{" "}
          <Link to="/login" className="login-link">
            Zaloguj się
          </Link>
        </div>
      </div>
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default RegisterPage;
