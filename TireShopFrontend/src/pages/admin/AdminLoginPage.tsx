import { useState } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import { loginUser } from '../../api/authApi';
import { isAdmin } from '../../utils/authUtils';
// @ts-ignore
import logo from '../../assets/logo.png';
import './AdminLoginPage.css';

function AdminLoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await loginUser({ email, password });
            localStorage.setItem('token', response.token);

            if (isAdmin()) {
                navigate('/admin');
            } else {
                alert('Nie jesteś administratorem.');
                localStorage.removeItem('token');
            }
        } catch (error: any) {
            alert(error.response?.data?.error || 'Błąd logowania admina.');
        }
    };

    return (
        <div className="admin-container">
            <div className="admin-card">
                <Link to={'/'} className="admin-logo">
                    <img src={logo} alt="Tire Shop Admin Logo" />
                </Link>
                <h1 className="admin-title">TIRE SHOP PANEL</h1>
                <form onSubmit={handleSubmit} className="admin-form">
                    <input
                        type="email"
                        placeholder="Admin Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        className="admin-input"
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className="admin-input"
                    />
                    <button type="submit" className="admin-button">Zaloguj się</button>
                </form>
            </div>
        </div>
    );
}

export default AdminLoginPage;
