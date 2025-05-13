import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useLocation,
} from "react-router-dom";
import HomePage from "./pages/home/HomePage";
import LoginPage from "./pages/login/LoginPage";
import RegisterPage from "./pages/login/RegisterPage";
import TiresPage from "./pages/category/TiresPage";
import AdminLoginPage from "./pages/panel/admin/AdminLoginPage";
import AdminRoute from "./routes/AdminRoute";
import AdminPanelPage from "./pages/panel/admin/AdminPanelPage";
import Navbar from "./components/navbars/Navbar";
import SecondaryNavbar from "./components/navbars/SecondaryNavbar";
import AccessoryPage from "./pages/category/AccessoryPage";
import RimPage from "./pages/category/RimPage";
import ProfilePage from "./pages/user/ProfilePage";
import CartPage from "./pages/cart/CartPage";
import ContactPage from "./pages/home/ContactPage";
import TireRankingPage from "./pages/home/TireRankingPage";
import InstallationPage from "./pages/home/InstallationPage";
import TireSelectionPage from "./pages/home/TireSelectionPage";
import RimSelectionPage from "./pages/home/RimSelectionPage";
import SettingsPage from "./pages/user/SettingsPage";
import AddressesPage from "./pages/user/AddressesPage";
import CheckoutPage from "./pages/order/CheckoutPage";
import UserOrdersPage from "./pages/user/UserOrdersPage";
import AdminProductPanel from "./pages/panel/page/AdminProductPanel";
import AdminUserPanel from "./pages/panel/page/AdminUserPanel";
import AdminOrderPanel from "./pages/panel/page/AdminOrderPanel";

function AppWrapper() {
  const location = useLocation();
  const isAdminRoute = location.pathname.startsWith("/admin");

  return (
    <div className={isAdminRoute ? "admin-layout" : "default-layout"}>
      {!isAdminRoute && <Navbar />}
      {!isAdminRoute && <SecondaryNavbar />}

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route path="/tires" element={<TiresPage />} />
        <Route path="/accessories" element={<AccessoryPage />} />
        <Route path="/rims" element={<RimPage />} />

        <Route path="/contact" element={<ContactPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/profile/settings" element={<SettingsPage />} />
        <Route path="/profile/addresses" element={<AddressesPage />} />
        <Route path="/profile/orders" element={<UserOrdersPage />} />

        <Route path="/cart" element={<CartPage />} />
        <Route path="/checkout" element={<CheckoutPage />} />

        <Route path="/ranking-opon" element={<TireRankingPage />} />
        <Route path="/montaz" element={<InstallationPage />} />
        <Route path="/dobor-opon" element={<TireSelectionPage />} />
        <Route path="/dobor-felg" element={<RimSelectionPage />} />

        <Route element={<AdminRoute />}>
          <Route path="/admin" element={<AdminPanelPage />} />
          <Route path="/admin-login" element={<AdminLoginPage />} />
          <Route path="/admin/users" element={<AdminUserPanel />} />
          <Route path="/admin/products" element={<AdminProductPanel />} />
          <Route path="/admin/orders" element={<AdminOrderPanel />} />
        </Route>

        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <Router>
      <AppWrapper />
    </Router>
  );
}

export default App;
