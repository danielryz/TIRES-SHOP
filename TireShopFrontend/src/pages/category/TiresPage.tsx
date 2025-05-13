import { useEffect, useRef, useState } from "react";
import { getTires } from "../../api/tiresApi";
import { getImagesByProductId } from "../../api/imageApi";
import { Tire } from "../../types/Tire";
import "./TiresPage.css";
import { addToCart } from "../../api/cartApi";
import AlertStack from "../../components/alert/AlertStack";
import { useCart } from "../../context/CartContext";
import { AxiosError } from "axios";

function TiresPage() {
  const [tires, setTires] = useState<Tire[]>([]);
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
    const fetchTires = async () => {
      try {
        const tiresData = await getTires();

        const tiresWithImages = await Promise.all(
          tiresData.map(async (tire) => {
            try {
              const images = await getImagesByProductId(tire.id);

              return {
                ...tire,
                imageUrls: images.map((img) => img.url),
              };
            } catch {
              console.error("Failed to load image for tire:", tire.id);
              return { ...tire, imageUrls: [] };
            }
          }),
        );

        setTires(tiresWithImages);
      } catch {
        setError("Failed to load tires.");
      } finally {
        setLoading(false);
      }
    };

    fetchTires();
  }, []);

  if (loading) return <p>Loading tires...</p>;
  if (error) return <p>{error}</p>;
  const filteredAndSortedTires = tires
    .filter((t) => {
      const season = t.season.toLowerCase();
      const matchesSeason =
        (!isWinter && !isSummer && !isAllSeason) ||
        (isWinter && season === "winter") ||
        (isSummer && season === "summer") ||
        (isAllSeason && season === "all_season");

      return matchesSeason && t.price >= minPrice && t.price <= maxPrice;
    })
    .sort((a, b) => {
      if (selectedSort === "asc") return a.price - b.price;
      if (selectedSort === "desc") return b.price - a.price;
      return 0;
    });

  return (
    <div className="tires-page">
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
      <main className="tire-list">
        <div className="tire-controls">
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
        {filteredAndSortedTires.map((tire) => (
          <div key={tire.id} className="tire-card">
            <div className="tire-image">
              {tire.imageUrls && tire.imageUrls.length > 0 ? (
                <img
                  src={tire.imageUrls[0]}
                  alt={tire.name}
                  className="tire-img"
                />
              ) : (
                <div className="image-placeholder">Brak zdjęcia</div>
              )}
            </div>
            <div className="tire-info">
              <h2>{tire.name}</h2>
              <p>
                <strong>Sezon:</strong> {tire.season}
              </p>
              <p>
                <strong>Rozmiar:</strong> {tire.size}
              </p>
              <p>
                <strong>Stan:</strong> {tire.stock} szt.
              </p>
              <p className="tire-desc">{tire.description}</p>
            </div>
            <div className="tire-buy">
              <div className="buy-box">
                <p className="price">{tire.price.toFixed(2)} zł</p>
                <p className="tax-info">Zawiera VAT • wysyłka 1–2 dni</p>
                {tire.stock > 0 ? (
                  <button
                    className="add-to-cart-btn"
                    onClick={() => handleAddToCart(tire.id)}
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

export default TiresPage;
