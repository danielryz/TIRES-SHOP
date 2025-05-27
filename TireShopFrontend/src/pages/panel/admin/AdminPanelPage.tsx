import { Link } from "react-router-dom";
import "./AdminPanelPage.css";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
function AdminPanelPage() {
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
        </div>
      </div>
    </>
  );
}

export default AdminPanelPage;
