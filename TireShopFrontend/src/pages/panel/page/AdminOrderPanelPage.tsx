import { useState } from "react";
import AlertStack from "../../../components/alert/AlertStack";
import "../admin/AdminPanelPage.css";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
function AdminProductPanelPage() {
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
        <AlertStack alerts={alerts} onRemove={removeAlert} />
      </div>
    </>
  );
}

export default AdminProductPanelPage;
