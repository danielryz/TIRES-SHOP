import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../../api/authApi";
import "./LoginPage.css";
import logo from "../../assets/logo.png";
import { Link } from "react-router-dom";
import { AxiosError } from "axios";
import AlertStack from "../../components/alert/AlertStack";

function LoginPage() {
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
      navigate("/");
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message = error.response?.data?.message || "Login failed.";
      showAlert(message, "error");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <Link to={"/"} className="login-logo">
          <img src={logo} alt="Tire Shop Logo" />
        </Link>
        <h1 className="login-title">LOGOWANIE</h1>
        <form onSubmit={handleSubmit} className="login-form">
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="login-input"
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="login-input"
          />
          <button type="submit" className="login-button">
            Zaloguj się
          </button>
        </form>
        <div className="login-top-link">
          Nie masz konta?{" "}
          <Link to="/register" className="register-link">
            Zarejestruj się
          </Link>
        </div>
      </div>
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default LoginPage;
