import { Link, useNavigate, useLocation } from "react-router-dom";
import { getTokenPayload } from "../../utils/authUtils";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
import logo from "../../assets/logo.png";
import "./Navbar.css";
import {
  FaShoppingCart,
  FaUserPlus,
  FaSignInAlt,
  FaSearch,
  FaPhoneAlt,
  FaUserCircle,
} from "react-icons/fa";
import { useCart } from "../../context/CartContext";
import { useState } from "react";

function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();
  const tokenPayload = getTokenPayload();
  const [searchTerm, setSearchTerm] = useState("");
  const { count } = useCart();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchTerm.trim().length === 0) return;
    navigate(`/products?query=${encodeURIComponent(searchTerm.trim())}`);
  };

  if (
    ["/login", "/register", "/admin-login", "/admin"].includes(
      location.pathname,
    )
  ) {
    return null;
  }

  return (
    <nav className="navbar">
      <div className="navbar-content">
        <div className="navbar-left">
          <Link to="/" className="navbar-logo">
            <img src={logo} alt="TireShop Logo" />
          </Link>
        </div>

        <form className="navbar-search" onSubmit={handleSubmit}>
          <FaSearch className="search-icon-left" />
          <input
            type="text"
            placeholder="Szukaj produktów..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <button type="submit" className="search-submit-btn">
            Szukaj
          </button>
        </form>
        <div className="navbar-right">
          <Link to="/contact" className="navbar-icon-link">
            <FaPhoneAlt size={25} style={{ marginRight: 6 }} />
            Kontakt
          </Link>

          {tokenPayload ? (
            <>
              <Link to="/profile" className="navbar-icon-link">
                <FaUserCircle size={25} style={{ marginRight: 6 }} />
                Twoje konto
              </Link>
              <button onClick={handleLogout} className="navbar-button">
                Wyloguj
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="navbar-icon-link">
                <FaSignInAlt size={25} style={{ marginRight: 6 }} />
                Logowanie
              </Link>
              <Link to="/register" className="navbar-icon-link">
                <FaUserPlus size={25} style={{ marginRight: 6 }} />
                Rejestracja
              </Link>
            </>
          )}
          <Link to="/cart" className="navbar-icon-link" title="Koszyk">
            <FaShoppingCart size={25} style={{ marginRight: 6 }} />
            Koszyk {count > 0 && <span className="cart-count">{count}</span>}
          </Link>

          <div style={{ height: "100px" }} />
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
