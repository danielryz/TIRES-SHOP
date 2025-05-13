import { useNavigate } from "react-router-dom";

function LogoutButton() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <button
      onClick={handleLogout}
      style={{
        padding: "10px",
        fontSize: "16px",
        borderRadius: "5px",
        border: "none",
        backgroundColor: "#4e75b9",
        color: "white",
        cursor: "pointer",
        marginTop: "10px",
      }}
    >
      Logout
    </button>
  );
}

export default LogoutButton;
