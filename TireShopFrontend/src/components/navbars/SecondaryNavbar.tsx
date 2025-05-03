import { Link, useLocation } from 'react-router-dom';
import './SecondaryNavbar.css';

export default function SecondaryNavbar() {
    const location = useLocation();
    const isHome = location.pathname === '/';

    if (['/login', '/register', '/admin-login', '/admin'].includes(location.pathname)) {
        return null;
    }

    return (
    <div className="secondary-navbar">
        <div className="secondary-navbar-content">
            <div className="category-dropdown">
                <div className="category-button">KATEGORIE</div>
                <ul className={`category-dropdown-list ${isHome ? 'always-open' : ''}`}>
                    <li><Link className="navbar-pill" to="/tires">Opony</Link></li>
                    <li><Link className="navbar-pill" to="/rims">Felgi</Link></li>
                    <li><Link className="navbar-pill" to="/accessories">Akcesoria</Link></li>
                    <li><Link className="navbar-pill" to="/others">Inne</Link></li>
                </ul>
            </div>
        <div className="secondary-links">
            <Link to="/ranking-opon" className="navbar-pill">Rankingi Opon</Link>
            <Link to="/montaz" className="navbar-pill">Montaż</Link>
            <Link to="/dobor-opon" className="navbar-pill">Dobór Opon</Link>
            <Link to="/dobor-felg" className="navbar-pill">Dobór Felg</Link>
        </div>
        </div>

    </div>

    );
}
