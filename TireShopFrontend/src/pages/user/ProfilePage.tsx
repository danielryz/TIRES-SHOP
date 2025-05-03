import { Link, useNavigate } from 'react-router-dom';
import './ProfilePage.css';
import {deleteUser} from '../../api/userApi';
import {useState} from 'react';
import Alert from '../../components/alert/Alert'
import ConfirmModal from "../../components/ConfirmModal";



function ProfilePage() {
    const [alert, setAlert] = useState<{ message: string; type: 'success' | 'error' } | null>(null);
    const navigate = useNavigate();
    const [showConfirm, setShowConfirm] = useState(false);

    const confirmDelete = async () => {
        try {
            const message = await deleteUser();
            localStorage.removeItem('token');
            setAlert({ message, type: 'success' });
            setShowConfirm(false);
            setTimeout(() => navigate('/'), 2000);
        } catch (err: any) {
            setAlert({
                message: err?.response?.data?.error || 'Nie udało się usunąć konta.',
                type: 'error'
            });
        }
    };
    return (
        <div className="profile-page">
            <h1>Witaj, użytkowniku!</h1>
            <div className="profile-options">
                <Link to="/profile/settings" className="profile-btn">Ustawienia konta</Link>
                <Link to="/profile/addresses" className="profile-btn">Książka adresowa</Link>
                <Link to="/profile/orders" className="profile-btn">Twoje zamówienia</Link>
                <button className="profile-btn delete" onClick={() => setShowConfirm(true)}>
                    Usuń konto
                </button>
            </div>
            {showConfirm && (
                <ConfirmModal
                    message="Czy na pewno chcesz usunąć swoje konto? Tego nie da się cofnąć."
                    onConfirm={confirmDelete}
                    onCancel={() => setShowConfirm(false)}
                />
            )}
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

export default ProfilePage;
