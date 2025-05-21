import React, { useEffect, useRef, useState } from "react";
import { getAvailableTireFilters, getTire } from "../../api/tiresApi";
import { getImagesByProductId } from "../../api/imageApi";
import { FilterCount, Tire } from "../../types/Tire";
import "./CategoryPage.css";
import { addToCart } from "../../api/cartApi";
import AlertStack from "../../components/alert/AlertStack";
import { useCart } from "../../context/CartContext";
import { AxiosError } from "axios";
import { useNavigate } from "react-router-dom";
import { ProductType } from "../../types/Product";

function TiresPage() {
  const [tires, setTires] = useState<Tire[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(0);
  const [minPrice, setMinPrice] = useState(0);
  const [maxPrice, setMaxPrice] = useState(1000);

  const originalMinPrice = useRef<number | null>(null);
  const originalMaxPrice = useRef<number | null>(null);
  const [selectedSort, setSelectedSort] = useState("default");

  const [selectedSeasons, setSelectedSeasons] = useState<string[]>([]);
  const [selectedSizes, setSelectedSizes] = useState<string[]>([]);

  const [tempMinPrice, setTempMinPrice] = useState(minPrice);
  const [tempMaxPrice, setTempMaxPrice] = useState(maxPrice);

  const [page, setPage] = useState(0);
  const [sizePerPage, setSizePerPage] = useState(20);

  const [availableSeasons, setAvailableSeasons] = useState<FilterCount[]>([]);
  const [availableSizes, setAvailableSizes] = useState<FilterCount[]>([]);
  const navigate = useNavigate();
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

  useEffect(() => {
    const handler = setTimeout(() => {
      setMinPrice(tempMinPrice);
      setMaxPrice(tempMaxPrice);
    }, 500);

    return () => clearTimeout(handler);
  }, [tempMinPrice, tempMaxPrice]);

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
    const fetchFilters = async () => {
      try {
        const {
          seasons,
          sizes,
          minPrice: backendMinPrice,
          maxPrice: backendMaxPrice,
        } = await getAvailableTireFilters();

        setAvailableSeasons(seasons);
        setAvailableSizes(sizes);

        if (originalMinPrice.current === null) {
          originalMinPrice.current = backendMinPrice;
          originalMaxPrice.current = backendMaxPrice;

          setTempMinPrice(backendMinPrice);
          setTempMaxPrice(backendMaxPrice);

          setMinPrice(backendMinPrice);
          setMaxPrice(backendMaxPrice);
        }
      } catch (err) {
        console.error("Nie udało się pobrać filtrów", err);
      }
    };

    fetchFilters();
  }, []);

  useEffect(() => {
    const fetchTires = async () => {
      try {
        setLoading(true);

        const sortMap: Record<string, string> = {
          default: "id,asc",
          asc: "price,asc",
          desc: "price,desc",
        };

        const response = await getTire({
          season: selectedSeasons,
          size: selectedSizes,
          minPrice,
          maxPrice,
          page,
          sizePerPage,
          sort: sortMap[selectedSort],
        });
        setTotalPages(response.totalPages);

        const tiresWithImages = await Promise.all(
          response.content.map(async (tire) => {
            try {
              const images = await getImagesByProductId(tire.id);
              return { ...tire, imageUrls: images.map((img) => img.url) };
            } catch {
              return { ...tire, imageUrls: [] };
            }
          }),
        );

        setTires(tiresWithImages);
      } catch {
        setError("Błąd podczas ładowania opon...");
      } finally {
        setLoading(false);
      }
    };

    fetchTires();
  }, [
    selectedSeasons,
    selectedSizes,
    minPrice,
    maxPrice,
    selectedSort,
    page,
    sizePerPage,
  ]);

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

  const handleCardClick = (
    e: React.MouseEvent,
    type: ProductType,
    id: number,
  ) => {
    const target = e.target as HTMLElement;
    if (target.closest("button")) return;
    navigate(`/product/${type.toLowerCase()}/${id}`);
  };

  if (loading)
    return (
      <div className="load-product">
        <p>Loading products...</p>
      </div>
    );
  if (error)
    return (
      <div className="failed-load-product">
        <p>{error}</p>
      </div>
    );

  return (
    <div className="category-page">
      <aside className="filters">
        <h3>Filtry</h3>

        <div className="filter-group">
          <h4>Sezon</h4>
          {availableSeasons.map(({ value, count }) => (
            <label key={value} className="custom-checkbox">
              <input
                type="checkbox"
                checked={selectedSeasons.includes(value)}
                onChange={() =>
                  setSelectedSeasons((prev) =>
                    prev.includes(value)
                      ? prev.filter((s) => s !== value)
                      : [...prev, value],
                  )
                }
              />
              <span className="checkmark"></span>
              <span className="checkbox-label">
                Opony {value} ({count})
              </span>
            </label>
          ))}
        </div>

        <div className="filter-group">
          <h4>Rozmiar</h4>
          {availableSizes.map(({ value, count }) => (
            <label key={value} className="custom-checkbox">
              <input
                type="checkbox"
                checked={selectedSizes.includes(value)}
                onChange={() =>
                  setSelectedSizes((prev) =>
                    prev.includes(value)
                      ? prev.filter((s) => s !== value)
                      : [...prev, value],
                  )
                }
              />
              <span className="checkmark"></span>
              <span className="checkbox-label">
                {value} ({count})
              </span>
            </label>
          ))}
        </div>
      </aside>

      <main className="category-list">
        <div className="category-controls">
          <div className="price-range">
            <label>Cena (zł):</label>
            <input
              type="range"
              min={originalMinPrice.current ?? 0}
              max={tempMaxPrice}
              value={tempMinPrice}
              onChange={(e) => {
                const newMin = Number(e.target.value);
                if (newMin <= tempMaxPrice) {
                  setTempMinPrice(newMin);
                } else {
                  setTempMinPrice(tempMaxPrice);
                }
              }}
            />

            <input
              type="range"
              min={tempMinPrice}
              max={originalMaxPrice.current ?? 1000}
              value={tempMaxPrice}
              onChange={(e) => {
                const newMax = Number(e.target.value);
                if (newMax >= tempMinPrice) {
                  setTempMaxPrice(newMax);
                } else {
                  setTempMaxPrice(tempMinPrice);
                }
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

        {!loading &&
          !error &&
          tires.map((tire) => (
            <div
              key={tire.id}
              className="category-card"
              onClick={(e) => handleCardClick(e, tire.type, tire.id)}
            >
              <div className="category-image">
                {tire.imageUrls && tire.imageUrls.length > 0 ? (
                  <img
                    src={tire.imageUrls[0]}
                    alt={tire.name}
                    className="category-img"
                  />
                ) : (
                  <div className="image-placeholder">Brak zdjęcia</div>
                )}
              </div>

              <div className="category-info">
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
              </div>

              <div className="category-buy">
                <div className="buy-box">
                  <p className="price">{tire.price.toFixed(2)} zł</p>
                  <p className="tax-info">Zawiera VAT • wysyłka 1–2 dni</p>
                  {tire.stock > 0 ? (
                    <button
                      className="add-to-cart-btn"
                      onClick={(e) => {
                        e.stopPropagation();
                        handleAddToCart(tire.id);
                      }}
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

        <div className="pagination">
          <button
            className="pagination-btn"
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
          >
            Poprzednia
          </button>

          <span className="pagination-info">Strona {page + 1}</span>

          <button
            className="pagination-btn"
            onClick={() => setPage((p) => p + 1)}
            disabled={page + 1 >= totalPages}
          >
            Następna
          </button>

          <label htmlFor="itemsPerPage" className="pagination-select-label">
            Ilość na stronę:
          </label>
          <select
            id="itemsPerPage"
            className="pagination-select"
            value={sizePerPage}
            onChange={(e) => {
              setSizePerPage(Number(e.target.value));
              setPage(0);
            }}
          >
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
            <option value={100}>100</option>
          </select>
        </div>
      </main>

      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default TiresPage;
