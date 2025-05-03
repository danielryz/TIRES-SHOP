import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerUser } from '../../api/authApi';
// @ts-ignore
import logo from '../../assets/logo.png';
import './RegisterPage.css';
import Alert from "../../components/alert/Alert";

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const [alert, setAlert] = useState<{ message: string; type: 'success' | 'error' } | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await registerUser({ username, email, password });
            localStorage.setItem('token', response.token);
            navigate('/');
        } catch (error: any) {
            setAlert({message: error.response?.data?.error || 'Registration failed.', type: 'error'});
        }
    };

    return (
        <div className="register-container">
            <div className="register-card">
                <Link to={'/'} className="register-logo">
                    <img src={logo} alt="Tire Shop Logo" />
                </Link>
                <h1 className="register-title">REJESTRACJA</h1>
                <form onSubmit={handleSubmit} className="register-form">
                    <input
                        type="text"
                        placeholder="Nazwa użytkownika"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        className="register-input"
                    />
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        className="register-input"
                    />
                    <input
                        type="password"
                        placeholder="Hasło"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className="register-input"
                    />
                    <button type="submit" className="register-button">Zarejestruj się</button>
                </form>

                <div className="register-bottom-link">
                    Masz już konto?{' '}
                    <Link to="/login" className="login-link">Zaloguj się</Link>
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

export default RegisterPage;
