import { useEffect, useRef, useState } from "react";
import { getAccessory } from "../../api/accessoryApi";
import { getImagesByProductId } from "../../api/imageApi";
import { Accessory } from "../../types/Accessory";
import "./AccessoryPage.css";
import { addToCart } from "../../api/cartApi";
import AlertStack from "../../components/alert/AlertStack";
import { useCart } from "../../context/CartContext";
import { AxiosError } from "axios";

function AccessoryPage() {
  const [accessory, setAccessory] = useState<Accessory[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [minPrice, setMinPrice] = useState(0);
  const [maxPrice, setMaxPrice] = useState(1000);
  const [selectedSort, setSelectedSort] = useState("default");

  const [isWinter, setIsWinter] = useState(false);
  const [isSummer, setIsSummer] = useState(false);
  const [isAllSeason, setIsAllSeason] = useState(false);

  const { refreshCart } = useCart();
  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);
  const nextId = useRef(0);

  const showAlert = (message: string, type: "success" | "error") => {
    const id = nextId.current++;
    setAlerts((prev) => [...prev, { id, message, type }]);
  };

  const removeAlert = (id: number) => {
    setAlerts((prev) => prev.filter((a) => a.id !== id));
  };

  const handleAddToCart = async (productId: number) => {
    try {
      await addToCart(productId, 1);
      refreshCart();
      showAlert("Dodano produkt do koszyka", "success");
    } catch (err: unknown) {
      const error = err as AxiosError<{ message: string }>;
      const message =
        error.response?.data?.message ||
        "Nie udało się dodać produktu do koszyka";
      showAlert(message, "error");
    }
  };

  const toggleWinter = () => setIsWinter((prev) => !prev);
  const toggleSummer = () => setIsSummer((prev) => !prev);
  const toggleAllSeason = () => setIsAllSeason((prev) => !prev);

  const getSortLabel = (sortValue: string) => {
    switch (sortValue) {
      case "asc":
        return "Cena rosnąco";
      case "desc":
        return "Cena malejąco";
      case "default":
      default:
        return "Domyślnie";
    }
  };

  useEffect(() => {
    const fetchAccessory = async () => {
      try {
        const accessoryData = await getAccessory();

        const accessoryWithImages = await Promise.all(
          accessoryData.map(async (accessory) => {
            try {
              const images = await getImagesByProductId(accessory.id);

              return {
                ...accessory,
                imageUrls: images.map((img) => img.url),
              };
            } catch {
              console.error(
                "Failed to load image for accessory:",
                accessory.id,
              );
              return { ...accessory, imageUrls: [] };
            }
          }),
        );

        setAccessory(accessoryWithImages);
      } catch {
        setError("Failed to load accessory.");
      } finally {
        setLoading(false);
      }
    };

    fetchAccessory();
  }, []);

  if (loading) return <p>Loading accessory...</p>;
  if (error) return <p>{error}</p>;
  const filteredAndSortedAccessory = accessory
    .filter((a) => {
      return a.price >= minPrice && a.price <= maxPrice;
    })
    .sort((a, b) => {
      if (selectedSort === "asc") return a.price - b.price;
      if (selectedSort === "desc") return b.price - a.price;
      return 0;
    });
  return (
    <div className="accessory-page">
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
          <input
            type="checkbox"
            checked={isAllSeason}
            onChange={toggleAllSeason}
          />
          <span className="checkmark"></span>
          <span className="checkbox-label">Opony Wielosezonowe</span>
        </label>
      </aside>
      <main className="accessory-list">
        <div className="accessory-controls">
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
            <span>
              {minPrice} zł - {maxPrice} zł
            </span>
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
        {filteredAndSortedAccessory.map((accessory) => (
          <div key={accessory.id} className="accessory-card">
            <div className="accessory-image">
              {accessory.imageUrls && accessory.imageUrls.length > 0 ? (
                <img
                  src={accessory.imageUrls[0]}
                  alt={accessory.name}
                  className="accessory-img"
                />
              ) : (
                <div className="image-placeholder">Brak zdjęcia</div>
              )}
            </div>
            <div className="accessory-info">
              <h2>{accessory.name}</h2>
              <p>
                <strong>Rodzaj:</strong> {accessory.accessoryType}
              </p>
              <p>
                <strong>Stan:</strong> {accessory.stock} szt.
              </p>
              <p className="accessory-desc">{accessory.description}</p>
            </div>
            <div className="accessory-buy">
              <div className="buy-box">
                <p className="price">{accessory.price.toFixed(2)} zł</p>
                <p className="tax-info">Zawiera VAT • wysyłka 1–2 dni</p>
                {accessory.stock > 0 ? (
                  <button
                    className="add-to-cart-btn"
                    onClick={() => handleAddToCart(accessory.id)}
                  >
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

export default AccessoryPage;
