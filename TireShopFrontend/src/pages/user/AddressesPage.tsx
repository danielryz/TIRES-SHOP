import { useEffect, useState } from "react";
import { Address } from "../../types/Address";
import {
  getAddresses,
  deleteAddress,
  getAddressById,
} from "../../api/addressesApi";
import AddressForm from "./AddressForm";
import "./AddressesPage.css";
import Modal from "../../components/Modal";
import ConfirmModal from "../../components/ConfirmModal";
import AlertStack from "../../components/alert/AlertStack";
import { AxiosError } from "axios";

function AddressesPage() {
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editAddress, setEditAddress] = useState<Address | undefined>(
    undefined,
  );

  const [showConfirm, setShowConfirm] = useState(false);
  const [deleteId, setDeleteId] = useState<number | null>(null);
  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);

  const showAlert = (message: string, type: "success" | "error") => {
    const id = Date.now() + Math.random();
    setAlerts((prev) => [...prev, { id, message, type }]);
  };

  const removeAlert = (id: number) => {
    setAlerts((prev) => prev.filter((a) => a.id !== id));
  };

  const fetchAddresses = async () => {
    setLoading(true);
    try {
      const data = await getAddresses();
      setAddresses(data);
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Nie udało się pobrać adresów";
      showAlert(message, "error");
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
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Nie udało się pobrać adresu";
      showAlert(message, "error");
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
      showAlert(msg, "success");
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message || "Nie udało się usunąć adresu";
      showAlert(message, "error");
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
              <p>
                <strong>
                  {address.street} {address.houseNumber}
                  {address.apartmentNumber && `/${address.apartmentNumber}`}
                </strong>
              </p>
              <p>
                {address.postalCode} {address.city}
              </p>
              <p>Typ: {address.type}</p>
              <div className="address-actions">
                <button onClick={() => handleEdit(address.id)}>Edytuj</button>
                <button onClick={() => handleAskDelete(address.id)}>
                  Usuń
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      <button
        className="add-address-btn"
        onClick={() => {
          setEditAddress(undefined);
          setShowForm(true);
        }}
      >
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
            showAlert={showAlert}
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
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default AddressesPage;
