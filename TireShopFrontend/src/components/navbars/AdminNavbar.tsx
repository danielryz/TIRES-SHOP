// components/navbar/AdminNavbar.tsx
import { useEffect, useState } from "react";
import { getUserProfile } from "../../api/userApi";
import LogoutButton from "../button/LogoutButton";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
import logo from "../../assets/logo.png";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
import avatar from "../../assets/admin.png";
import "./AdminNavbar.css";
import { Link } from "react-router-dom";

interface User {
  firstName: string;
  lastName: string;
  email: string;
}

function AdminNavbar() {
  const [admin, setAdmin] = useState<User | null>(null);

  useEffect(() => {
    const fetchAdmin = async () => {
      try {
        const data = await getUserProfile();
        setAdmin(data);
      } catch {
        setAdmin(null);
      }
    };
    fetchAdmin();
  }, []);

  return (
    <nav className="admin-navbar">
      <div className="admin-navbar-content">
        <div className="admin-nav-left">
          <Link to="/" className="navbarAdmin-logo">
            <img src={logo} alt="TireShop Logo" />
          </Link>
        </div>

        <div className="admin-nav-center">
          <Link to="/admin/users">
            <div className="nav-panel-card">
              <div className="nav-panel-icon">👥</div>
              <h3 className="nav-panel-title">User Panel</h3>
            </div>
          </Link>

          <Link to="/admin/products">
            <div className="nav-panel-card">
              <div className="nav-panel-icon">📦</div>
              <h3 className="nav-panel-title">Product Panel</h3>
            </div>
          </Link>

          <Link to="/admin/orders">
            <div className="nav-panel-card">
              <div className="nav-panel-icon">🛒</div>
              <h3 className="nav-panel-title">Order Management</h3>
            </div>
          </Link>
        </div>

        <div className="admin-nav-right">
          {admin && (
            <div className="admin-info">
              <div className="admin-avatar">
                <img src={avatar} alt="Admin Avatar" />
              </div>
              <div className="admin-details">
                <span className="admin-name">
                  {admin.firstName} {admin.lastName}
                </span>
                <span className="admin-email">
                  <i className="fas fa-envelope"></i> {admin.email}
                </span>
              </div>
            </div>
          )}
          <LogoutButton />
        </div>
      </div>
    </nav>
  );
}

export default AdminNavbar;
