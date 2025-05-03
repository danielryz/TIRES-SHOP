import {changeUserPassword, deleteUserData, getUserProfile, updateUserData} from "../../api/userApi";
import {useEffect, useState} from "react";
import {User} from "../../types/User";
import './SettingsPage.css';
import Alert from "../../components/alert/Alert";

function SettingsPage() {
    const [form, setForm] = useState({
        username: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        email: ''
    });
    const [newPassword, setNewPassword] = useState('');
    const [currentPassword, setCurrentPassword] = useState('');

    const [alert, setAlert] = useState<{ message: string; type: 'success' | 'error' } | null>(null);

    useEffect(() => {
        getUserProfile().then((user: User) => {
            setForm({
                username: user.username,
                firstName: user.firstName,
                lastName: user.lastName,
                phoneNumber: user.phoneNumber,
                email: user.email
            });
        });
    }, []);

    const handleDataUpdate = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await updateUserData(form);
            setAlert({message: response, type: 'success'});
        } catch (err: any) {
            const msg = err?.response?.data?.error || 'Błąd przy aktualizacji danych.';
            setAlert({message: msg, type: 'error'});
        }
    };

    const handlePasswordChange = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await changeUserPassword({ password: currentPassword, newPassword });
            setAlert({message: response, type: 'success' });

            setCurrentPassword('');
            setNewPassword('');

        } catch (err: any) {
            const msg = err?.response?.data?.error || 'Wystąpił błąd przy zmianie hasła.';
            setAlert({message: msg, type: 'error' });
        }
    };

    const handleClearData = async () => {
        if (!window.confirm('Czy na pewno chcesz usunąć swoje dane osobowe?')) return;
        try {
            const response = await deleteUserData();
            setAlert({message: response, type: 'success'});
            setForm((prev) => ({
                ...prev,
                firstName: '',
                lastName: '',
                phoneNumber: '',
            }));
        }catch(err: any) {
            const msg = err?.response?.data?.error || 'Nie udało się usunąć danych.';
            setAlert({message: msg, type: 'error'});
        }
    }

    return (
        <div className="profile-settings">
            <h2>Ustawienia konta</h2>

            <form onSubmit={handleDataUpdate}>
                <h3>Dane użytkownika</h3>
                <label>Nazwa użytkownika:
                    <input name="username" value={form.username || ''} onChange={(e) => setForm({...form, username: e.target.value})} />
                </label>
                <label>E-mail:
                    <input name="email" value={form.email || ''} readOnly />
                </label>
                <label>Imię:
                    <input name="firstName" value={form.firstName || ''} onChange={(e) => setForm({ ...form, firstName: e.target.value })} />
                </label>
                <label>Nazwisko:
                    <input name="lastName" value={form.lastName || ''} onChange={(e) => setForm({ ...form, lastName: e.target.value })} />
                </label>
                <label>Numer telefonu:
                    <input name="phoneNumber" value={form.phoneNumber || ''} onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })} />
                </label>
                <button type="submit">Zapisz dane</button>
                <button
                    type="button"
                    className="clear-data-btn"
                    onClick={handleClearData}
                >
                    Usuń dane osobowe
                </button>
            </form>

            <form onSubmit={handlePasswordChange}>
                <h3>Zmiana hasła</h3>
                <label>Obecne hasło:
                    <input type="password" value={currentPassword || ''} onChange={(e) => setCurrentPassword(e.target.value)} />
                </label>
                <label>Nowe hasło:
                    <input type="password" value={newPassword || ''} onChange={(e) => setNewPassword(e.target.value)} />
                </label>
                <button type="submit">Zmień hasło</button>
            </form>
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

export default SettingsPage;