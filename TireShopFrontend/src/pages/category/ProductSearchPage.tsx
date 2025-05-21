import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { searchProduct } from "../../api/productApi";
import { getImagesByProductId } from "../../api/imageApi";
import "./CategoryPage.css";
import AlertStack from "../../components/alert/AlertStack";
import {
  AccessoryResponse,
  Product,
  ProductApiResponse,
  ProductType,
  RimResponse,
  TireResponse,
} from "../../types/Product";
import { addToCart } from "../../api/cartApi";
import { AxiosError } from "axios";
import { useCart } from "../../context/CartContext";

function ProductSearchPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(0);
  const [searchParams] = useSearchParams();
  const searchTerm = searchParams.get("query")?.trim() || "";
  const [page, setPage] = useState(0);
  const [sizePerPage, setSizePerPage] = useState(20);
  const [selectedSort, setSelectedSort] = useState("default");

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
    const fetchProducts = async () => {
      setLoading(true);
      setError(null);

      try {
        const sortMap: Record<string, string> = {
          default: "id,asc",
          asc: "price,asc",
          desc: "price,desc",
        };

        const response = await searchProduct({
          query: searchTerm.trim() || undefined,
          page,
          sizePerPage,
          sort: sortMap[selectedSort],
        });
        setTotalPages(response.totalPages);

        const productsWithImages = await Promise.all(
          response.content.map(async (product) => {
            try {
              const images = await getImagesByProductId(product.id);
              return { ...product, imageUrls: images.map((img) => img.url) };
            } catch {
              return { ...product, imageUrls: [] };
            }
          }),
        );

        setProducts(productsWithImages);
      } catch {
        setError("Błąd podczas ładowania produktów");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [searchTerm, page, sizePerPage, selectedSort]);

  console.log("searchTerm:", searchTerm.trim());
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

  function renderProduct(product: ProductApiResponse) {
    switch (product.type) {
      case ProductType.TIRE: {
        const tire = product as TireResponse;
        return (
          <div className="category-info">
            <h2>{tire.name}</h2>
            <p>
              <strong>Sezon:</strong> {tire.season} szt.
            </p>
            <p>
              <strong>Rozmiar:</strong> {tire.size} szt.
            </p>
            <p>
              <strong>Stan magazynowy:</strong> {product.stock} szt.
            </p>
          </div>
        );
      }
      case ProductType.RIM: {
        const rim = product as RimResponse;
        return (
          <div className="category-info">
            <h2>{rim.name}</h2>
            <p>
              <strong>Materiał:</strong> {rim.material} szt.
            </p>
            <p>
              <strong>Rozmiar:</strong> {rim.size} szt.
            </p>
            <p>
              <strong>Rozstaw śrub:</strong> {rim.boltPattern} szt.
            </p>
            <p>
              <strong>Stan magazynowy:</strong> {rim.stock} szt.
            </p>
          </div>
        );
      }
      case ProductType.ACCESSORY: {
        const accessory = product as AccessoryResponse;
        return (
          <div className="category-info">
            <h2>{accessory.name}</h2>
            <p>
              <strong>Typ Akcesorium:</strong> {accessory.accessoryType} szt.
            </p>
            <p>
              <strong>Stan magazynowy:</strong> {accessory.stock} szt.
            </p>
          </div>
        );
      }
      case ProductType.ALL:
      default:
        return (
          <div>
            <h2>{product.name}</h2>
            <p>
              <strong>Stan magazynowy:</strong> {product.stock} szt.
            </p>
          </div>
        );
    }
  }

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
      </aside>

      <main className="category-list">
        <div className="category-controls">
          <div className="price-range"></div>

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
          products.map((p) => (
            <div
              key={p.id}
              className="category-card"
              onClick={(e) => handleCardClick(e, p.type, p.id)}
            >
              <div className="category-image">
                {p.imageUrls && p.imageUrls.length > 0 ? (
                  <img
                    src={p.imageUrls[0]}
                    alt={p.name}
                    className="category-img"
                  />
                ) : (
                  <div className="image-placeholder">Brak zdjęcia</div>
                )}
              </div>

              {renderProduct(p)}

              <div className="category-buy">
                <div className="buy-box">
                  <p className="price">{p.price.toFixed(2)} zł</p>
                  <p className="tax-info">Zawiera VAT • wysyłka 1–2 dni</p>
                  {p.stock > 0 ? (
                    <button
                      className="add-to-cart-btn"
                      onClick={(e) => {
                        e.stopPropagation();
                        handleAddToCart(p.id);
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

export default ProductSearchPage;
