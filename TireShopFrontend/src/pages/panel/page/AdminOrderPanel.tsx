import { Link } from "react-router-dom";
import { useState } from "react";
import AlertStack from "../../../components/alert/AlertStack";
import "../admin/AdminPanelPage.css";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
function AdminPanelPage() {
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

  return (
    <>
      <AdminNavbar />
      <div className="admin-panel-container">
        <div className="admin-panel-container">
          <h1 className="admin-panel-title">Admin Management Panel</h1>
          <p className="admin-panel-subtitle">
            Manage users, products and orders from one place.
          </p>

          <div className="panel-grid">
            <Link to="/admin/users">
              <div className="panel-card">
                <div className="panel-icon">👥</div>
                <h3 className="panel-title">User Panel</h3>
                <p className="panel-desc">
                  Manage users, addresses, roles & orders
                </p>
              </div>
            </Link>

            <Link to="/admin/products">
              <div className="panel-card">
                <div className="panel-icon">📦</div>
                <h3 className="panel-title">Product Panel</h3>
                <p className="panel-desc">
                  Manage tires, rims, accessories and photos
                </p>
              </div>
            </Link>

            <Link to="/admin/orders">
              <div className="panel-card">
                <div className="panel-icon">🛒</div>
                <h3 className="panel-title">Order Management</h3>
                <p className="panel-desc">View and update order statuses</p>
              </div>
            </Link>
          </div>

          <AlertStack alerts={alerts} onRemove={removeAlert} />
        </div>
      </div>
    </>
  );
}

export default AdminPanelPage;
