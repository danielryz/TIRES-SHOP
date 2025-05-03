import {useEffect, useRef, useState} from 'react';
import { getRim } from '../../api/rimApi';
import { getImagesByProductId } from "../../api/imageApi";
import { Rim } from '../../types/Rim';
import './RimPage.css';
import {addToCart} from "../../api/cartApi";
import Alert from "../../components/alert/Alert";
import AlertStack from "../../components/alert/AlertStack";

function RimPage() {
    const [rim, setRim] = useState<Rim[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [minPrice, setMinPrice] = useState(0);
    const [maxPrice, setMaxPrice] = useState(1000);
    const [selectedSort, setSelectedSort] = useState('default');

    const [isWinter, setIsWinter] = useState(false);
    const [isSummer, setIsSummer] = useState(false);
    const [isAllSeason, setIsAllSeason] = useState(false);

    const [alerts, setAlerts] = useState<{ id: number; message: string; type: 'success' | 'error' }[]>([]);
    const nextId = useRef(0);

    const showAlert = (message: string, type: 'success' | 'error') => {
        const id = nextId.current++;
        setAlerts((prev) => [...prev, { id, message, type }]);
    };

    const removeAlert = (id: number) => {
        setAlerts((prev) => prev.filter((a) => a.id !== id));
    };

    const handleAddToCart = async (productId: number) => {
        try {
            await addToCart(productId, 1);
            showAlert('Dodano produkt do koszyka',  'success' );
        } catch (err: any) {
            showAlert( err?.response?.data?.error || 'Nie udało się dodać produktu do koszyka','error');
        }
    };

    const toggleWinter = () => setIsWinter((prev) => !prev);
    const toggleSummer = () => setIsSummer((prev) => !prev);
    const toggleAllSeason = () => setIsAllSeason((prev) => !prev);

    const getSortLabel = (sortValue: string) => {
        switch (sortValue) {
            case 'asc':
                return 'Cena rosnąco';
            case 'desc':
                return 'Cena malejąco';
            case 'default':
            default:
                return 'Domyślnie';
        }
    };



    useEffect(() => {
        const fetchRim = async () => {
            try {
                const rimData = await getRim();

                const rimWithImages = await Promise.all(
                    rimData.map(async (rim) => {
                        try {
                            const images = await getImagesByProductId(rim.id);

                            return {
                                ...rim,
                                imageUrls: images.map((img) => img.url),
                            };
                        } catch (error) {
                            console.error('Failed to load image for rim:', rim.id);
                            return { ...rim, imageUrls: [] };
                        }
                    })
                );

                setRim(rimWithImages);
            } catch (err) {
                setError('Failed to load rim.');
            } finally {
                setLoading(false);
            }
        };

        fetchRim();
    }, []);

    if (loading) return <p>Loading rim...</p>;
    if (error) return <p>{error}</p>;
    const filteredAndSortedRim = rim
        .filter((r) => {

            return r.price >= minPrice && r.price <= maxPrice;
        })
        .sort((a, b) => {
            if (selectedSort === 'asc') return a.price - b.price;
            if (selectedSort === 'desc') return b.price - a.price;
            return 0;
        });
    return (
        <div className="rim-page">

            <aside className="filters">
                <h3>Filtry</h3>
                <label className="custom-checkbox">
                    <input type="checkbox" checked={isWinter} onChange={toggleWinter} />
                    <span className="checkmark"></span>
                    <span className="checkbox-label">Opony Zimowe</span>
                </label>
                <label className="custom-checkbox">
                    <input type="checkbox" checked={isSummer} onChange={toggleSummer} />
                    <span className="checkmark"></span>
                    <span className="checkbox-label">Opony Letnie</span>
                </label>
                <label className="custom-checkbox">
                    <input type="checkbox" checked={isAllSeason} onChange={toggleAllSeason} />
                    <span className="checkmark"></span>
                    <span className="checkbox-label">Opony Wielosezonowe</span>
                </label>
            </aside>
            <main className="rim-list">
                <div className="rim-controls">
                    <div className="price-range">
                        <label>Cena (zł):</label>
                        <input
                            type="range"
                            min="0"
                            max={maxPrice}
                            value={minPrice}
                            onChange={(e) => {
                                const value = Number(e.target.value);
                                if (value <= maxPrice) setMinPrice(value);
                            }}
                        />

                        <input
                            type="range"
                            min={minPrice}
                            max="1000"
                            value={maxPrice}
                            onChange={(e) => {
                                const value = Number(e.target.value);
                                if (value >= minPrice) setMaxPrice(value);
                            }}
                        />
                        <span>{minPrice} zł - {maxPrice} zł</span>
                    </div>

                    <div className="custom-select">
                        <div className="custom-select-button">
                            Sortuj: {getSortLabel(selectedSort)}
                        </div>
                        <ul className="custom-select-dropdown">
                            <li onClick={() => setSelectedSort("default")}>Domyślnie</li>
                            <li onClick={() => setSelectedSort("asc")}>Cena rosnąco</li>
                            <li onClick={() => setSelectedSort("desc")}>Cena malejąco</li>
                        </ul>
                    </div>

                </div>
                {filteredAndSortedRim.map((rim) => (

                    <div key={rim.id} className="rim-card">
                        <div className="rim-image">
                            {rim.imageUrls && rim.imageUrls.length > 0 ? (
                                <img src={rim.imageUrls[0]} alt={rim.name} className="rim-img" />
                            ) : (
                                <div className="image-placeholder">Brak zdjęcia</div>
                            )}
                        </div>
                        <div className="rim-info">
                            <h2>{rim.name}</h2>
                            <p><strong>Rozmiar:</strong> {rim.size}</p>
                            <p><strong>Materiał:</strong> {rim.material}</p>
                            <p><strong>Rozstaw śrub:</strong> {rim.boltPattern}</p>
                            <p><strong>Stan:</strong> {rim.stock} szt.</p>
                            <p className="rim-desc">{rim.description}</p>
                        </div>
                        <div className="rim-buy">
                            <div className="buy-box">
                                <p className="price">{rim.price.toFixed(2)} zł</p>
                                <p className="tax-info">Zawiera VAT • wysyłka 1–2 dni</p>
                                {rim.stock > 0 ? (
                                    <button className="add-to-cart-btn" onClick={() => handleAddToCart(rim.id)}>
                                        Dodaj do koszyka
                                    </button>
                                ) : (
                                    <span className="out-of-stock">Brak w magazynie</span>
                                )}

                            </div>
                        </div>
                    </div>
                ))}
            </main>
            <AlertStack alerts={alerts} onRemove={removeAlert} />
        </div>
    );
}

export default RimPage;
