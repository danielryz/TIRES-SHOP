import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginUser } from '../../api/authApi';
import './LoginPage.css';
// @ts-ignore
import logo from '../../assets/logo.png';
import { Link } from 'react-router-dom';
import Alert from "../../components/alert/Alert";


function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const [alert, setAlert] = useState<{ message: string; type: 'success' | 'error' } | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await loginUser({ email, password });
            localStorage.setItem('token', response.token);
            navigate('/');
        } catch (error: any) {
            setAlert({message: error.response?.data?.error || 'Login failed.', type: 'error' });
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
                    <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required className="login-input" />
                    <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required className="login-input" />
                    <button type="submit" className="login-button">Zaloguj się</button>
                </form>
                <div className="login-top-link">
                    Nie masz konta?{' '}
                    <Link to="/register" className="register-link">Zarejestruj się</Link>
                </div>
            </div>
            {alert && (
                <Alert
                    message={alert.message}
                    type={alert.type}
                    onClose={() => setAlert(null)}
                />
            )}
        </div>
    );
}


export default LoginPage;
