// src/pages/CheckoutPage.tsx
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCartWithImages, CartItem } from '../../api/cartApi';
import { createOrder, addShippingAddress } from '../../api/ordersApi';
import { getUserProfile } from '../../api/userApi';
import { getAddressByType } from '../../api/addressesApi';
import AlertStack from '../../components/alert/AlertStack';
import './CheckoutPage.css';

interface Address {
    id: number;
    street: string;
    houseNumber: string;
    apartmentNumber?: string;
    postalCode: string;
    city: string;
    type: string;
}

function CheckoutPage() {
    const navigate = useNavigate();
    const [step, setStep] = useState(1);
    const [cartItems, setCartItems] = useState<CartItem[]>([]);
    const [loading, setLoading] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userInfo, setUserInfo] = useState<any>(null);
    const [addresses, setAddresses] = useState<Address[]>([]);
    const [selectedAddressId, setSelectedAddressId] = useState<number | 'new'>('new');

    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        street: '',
        houseNumber: '',
        apartmentNumber: '',
        postalCode: '',
        city: ''
    });

    const [alerts, setAlerts] = useState<{ id: number; message: string; type: 'success' | 'error' }[]>([]);
    const [nextId, setNextId] = useState(0);

    const showAlert = (message: string, type: 'success' | 'error') => {
        const id = nextId;
        setNextId(prev => prev + 1);
        setAlerts(prev => [...prev, { id, message, type }]);
    };

    const removeAlert = (id: number) => {
        setAlerts(prev => prev.filter(a => a.id !== id));
    };

    const handleInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    useEffect(() => {
        const checkUser = async () => {
            try {
                const user = await getUserProfile();
                setUserInfo(user);
                setIsLoggedIn(true);
                setFormData((prev) => ({
                    ...prev,
                    firstName: user.firstName,
                    lastName: user.lastName,
                    email: user.email,
                    phone: user.phoneNumber
                }));
                const savedAddresses = await getAddressByType('SHIPPING');
                setAddresses(savedAddresses);
                if (savedAddresses.length > 0) setSelectedAddressId(savedAddresses[0].id);
            } catch {
                setIsLoggedIn(false);
            }
        };
        checkUser();
    }, []);

    const handleNext = async () => {
        if (!formData.firstName || !formData.lastName || !formData.email || !formData.phone) {
            showAlert('Uzupełnij dane kontaktowe.', 'error');
            return;
        }
        try {
            const items = await getCartWithImages();
            setCartItems(items);
            setStep(2);
        } catch {
            showAlert('Nie udało się pobrać koszyka.', 'error');
        }
    };

    const handleOrder = async () => {
        if (selectedAddressId === 'new') {
            if (!formData.street || !formData.houseNumber || !formData.postalCode || !formData.city) {
                showAlert('Uzupełnij adres dostawy.', 'error');
                return;
            }
        }
        try {
            setLoading(true);
            const orderResponse = await createOrder({
                guestFirstName: isLoggedIn ? '' : formData.firstName,
                guestLastName: isLoggedIn ? '' : formData.lastName,
                guestEmail: isLoggedIn ? '' : formData.email,
                guestPhoneNumber: isLoggedIn ? '' : formData.phone,
                items: cartItems.map((item) => ({ productId: item.productId, quantity: item.quantity }))
            });

            if (selectedAddressId === 'new') {
                await addShippingAddress({
                    street: formData.street,
                    houseNumber: formData.houseNumber,
                    apartmentNumber: formData.apartmentNumber,
                    postalCode: formData.postalCode,
                    city: formData.city
                });
            } // else: address is already linked in backend

            showAlert('Zamówienie złożone pomyślnie!', 'success');
            setTimeout(() => navigate('/'), 2000);
        } catch {
            showAlert('Nie udało się złożyć zamówienia.', 'error');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="checkout-page">
            <h1>Składanie zamówienia</h1>

            {step === 1 && (
                <div className="step-box">
                    <h2>Dane kontaktowe</h2>
                    <input name="firstName" placeholder="Imię" onChange={handleInput} value={formData.firstName} />
                    <input name="lastName" placeholder="Nazwisko" onChange={handleInput} value={formData.lastName} />
                    <input name="email" placeholder="Email" onChange={handleInput} value={formData.email} />
                    <input name="phone" placeholder="Telefon" onChange={handleInput} value={formData.phone} />
                    <button className="step-btn" onClick={handleNext}>Dalej</button>
                </div>
            )}

            {step === 2 && (
                <div className="step-box">
                    <h2>Adres dostawy</h2>
                    {isLoggedIn && addresses.length > 0 && (
                        <div className="existing-addresses">
                            {addresses.map(addr => (
                                <div
                                    key={addr.id}
                                    className={`address-card ${selectedAddressId === addr.id ? 'selected' : ''}`}
                                    onClick={() => setSelectedAddressId(addr.id)}
                                >
                                    <p><strong>{addr.street} {addr.houseNumber}</strong></p>
                                    <p>{addr.apartmentNumber && `m. ${addr.apartmentNumber}`}</p>
                                    <p>{addr.postalCode} {addr.city}</p>
                                </div>
                            ))}
                            <div
                                className={`address-card ${selectedAddressId === 'new' ? 'selected' : ''}`}
                                onClick={() => setSelectedAddressId('new')}
                            >
                                <p>Użyj nowego adresu</p>
                            </div>
                        </div>
                    )}


                    {(selectedAddressId === 'new' || !isLoggedIn) && (
                        <>
                            <input name="street" placeholder="Ulica" onChange={handleInput} />
                            <input name="houseNumber" placeholder="Nr domu" onChange={handleInput} />
                            <input name="apartmentNumber" placeholder="Nr mieszkania" onChange={handleInput} />
                            <input name="postalCode" placeholder="Kod pocztowy" onChange={handleInput} />
                            <input name="city" placeholder="Miasto" onChange={handleInput} />
                        </>
                    )}

                    <h3>Podsumowanie</h3>
                    <ul className="summary-list">
                        {cartItems.map(item => (
                            <li key={item.id}>{item.productName} x{item.quantity} – {item.totalPrice.toFixed(2)} zł</li>
                        ))}
                    </ul>

                    <p className="summary-total"><strong>Suma:</strong> {cartItems.reduce((a, b) => a + b.totalPrice, 0).toFixed(2)} zł</p>

                    <button className="order-btn" disabled={loading} onClick={handleOrder}>
                        {loading ? 'Przetwarzanie...' : 'Zamawiam'}
                    </button>
                </div>
            )}

            <AlertStack alerts={alerts} onRemove={removeAlert} />
        </div>
    );
}

export default CheckoutPage;
