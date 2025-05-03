import { useEffect, useState } from 'react';
import { Address } from '../../types/Address';
import { getAddresses, deleteAddress, getAddressById } from '../../api/addressesApi';
import AddressForm from './AddressForm';
import './AddressesPage.css';
import Modal from '../../components/Modal';
import Alert from "../../components/alert/Alert";
import ConfirmModal from '../../components/ConfirmModal';

function AddressesPage() {
    const [addresses, setAddresses] = useState<Address[]>([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [editAddress, setEditAddress] = useState<Address | undefined>(undefined);

    const [alert, setAlert] = useState<{ message: string; type: 'success' | 'error' } | null>(null);

    const [showConfirm, setShowConfirm] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);

    const fetchAddresses = async () => {
        setLoading(true);
        try {
            const data = await getAddresses();
            setAddresses(data);
        } catch (err: any) {
            const msg = err.response?.data?.data?.error || 'Nie udało się pobrać adresów';
            setAlert({message: msg, type: 'error'});
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAddresses();
    }, []);

    const handleEdit = async (id: number) => {
        try {
            const address = await getAddressById(id);
            setEditAddress(address);
            setShowForm(true);
        } catch (err: any){
            const msg = err?.response?.data?.error || 'Nie udało się pobrać adresu';
            setAlert({message: msg, type: 'error' });
        }
    };

    const handleAskDelete = (id: number) => {
        setDeleteId(id);
        setShowConfirm(true);
    };

    const confirmDelete = async () => {
        if (deleteId === null) return;
        try {
            const msg = await deleteAddress(deleteId);
            setAddresses((prev) => prev.filter((a) => a.id !== deleteId));
            setAlert({ message: msg, type: 'success' });
        } catch (err: any) {
            const msg = err?.response?.data?.error || 'Nie udało się usunąć adresu';
            setAlert({ message: msg, type: 'error' });
        } finally {
            setShowConfirm(false);
            setDeleteId(null);
        }
    };


    return (
        <div className="address-book">
            <h2>Książka adresowa</h2>

            {loading ? (
                <p>Ładowanie...</p>
            ) : (
                <div className="address-list">
                    {addresses.map((address) => (
                        <div key={address.id} className="address-card">
                            <p><strong>{address.street} {address.houseNumber}{address.apartmentNumber && `/${address.apartmentNumber}`}</strong></p>
                            <p>{address.postalCode} {address.city}</p>
                            <p>Typ: {address.type}</p>
                            <div className="address-actions">
                                <button onClick={() => handleEdit(address.id)}>Edytuj</button>
                                <button onClick={() => handleAskDelete(address.id)}>Usuń</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            <button className="add-address-btn" onClick={() => {
                setEditAddress(undefined);
                setShowForm(true);
            }}>
                Dodaj nowy adres
            </button>

            {showForm && (
                <Modal onClose={() => setShowForm(false)}>
                <AddressForm
                    existingAddress={editAddress}
                    onSave={() => {
                        setShowForm(false);
                        setEditAddress(undefined);
                        fetchAddresses();
                    }}
                    onCancel={() => {
                        setShowForm(false);
                        setEditAddress(undefined);
                    }}
                    setAlert={setAlert}
                />
                </Modal>
            )}
            {showConfirm && (
                <ConfirmModal
                    title="Potwierdzenie usunięcia"
                    message="Czy na pewno chcesz usunąć ten adres?"
                    onConfirm={confirmDelete}
                    onCancel={() => {
                        setShowConfirm(false);
                        setDeleteId(null);
                    }}
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

export default AddressesPage;
