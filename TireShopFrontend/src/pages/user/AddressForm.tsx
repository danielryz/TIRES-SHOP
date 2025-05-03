import { useState, useEffect } from 'react';
import { Address } from '../../types/Address';
import { addAddress, updateAddress } from '../../api/addressesApi';
import './AddressForm.css'


interface Props {
    existingAddress?: Address;
    onSave: () => void;
    onCancel: () => void;
    setAlert: (alert: { message: string; type: 'success' | 'error' }) => void;
}

function AddressForm({ existingAddress, onSave, onCancel, setAlert }: Props) {

    const [form, setForm] = useState<Address>({
        id: 0,
        street: '',
        houseNumber: '',
        apartmentNumber: '',
        postalCode: '',
        city: '',
        type: 'SHIPPING',
    });

    useEffect(() => {
        if (existingAddress) {
            setForm({...existingAddress, type: existingAddress.type || 'SHIPPING' });
        }
    }, [existingAddress]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            let msg = '';
            if (existingAddress) {
                msg = await updateAddress(form.id, form);
            } else {
                msg = await addAddress(form);
            }

            setAlert({ message: msg, type: 'success' });
            onSave();
        } catch (err: any) {
            setAlert({
                message: err?.response?.data?.error || 'Nie udało się zapisać adresu',
                type: 'error'
            });
        }
    };

    return (
        <form className="address-form" onSubmit={handleSubmit}>
            <label>Ulica:
                <input name="street" value={form.street} onChange={handleChange} required />
            </label>
            <label>Nr domu:
                <input name="houseNumber" value={form.houseNumber} onChange={handleChange} required />
            </label>
            <label>Nr mieszkania:
                <input name="apartmentNumber" value={form.apartmentNumber} onChange={handleChange} />
            </label>
            <label>Kod pocztowy:
                <input name="postalCode" value={form.postalCode} onChange={handleChange} required />
            </label>
            <label>Miasto:
                <input name="city" value={form.city} onChange={handleChange} required />
            </label>
            <label>Typ adresu:
                <select name="type" value={form.type || 'SHIPPING'} onChange={handleChange}>
                    <option value="SHIPPING">Dostawa</option>
                    <option value="BILLING">Faktura</option>
                    <option value="RESIDENTIAL">Zamieszkania</option>
                </select>
            </label>
            <div className="form-actions">
                <button type="submit">{existingAddress ? 'Zapisz zmiany' : 'Dodaj adres'}</button>
                <button type="button" onClick={onCancel}>Anuluj</button>
            </div>
        </form>
    );
}

export default AddressForm;
