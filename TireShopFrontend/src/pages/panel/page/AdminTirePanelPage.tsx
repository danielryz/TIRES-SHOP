import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Tire, TireFilterParams, CreateTireRequest } from "../../../types/Tire";
import { getTire, createTires } from "../../../api/tiresApi";
import AlertStack from "../../../components/alert/AlertStack";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
import "./AdminProductPanelPage.css";
import { ProductType } from "../../../types/Product";

function AdminTirePanelPage() {
  const [tires, setTires] = useState<Tire[]>([]);
  const [filters, setFilters] = useState<TireFilterParams>();
  const [filterInputs, setFilterInputs] = useState<TireFilterParams>({});
  const [page, setPage] = useState(0);
  const [sizePerPage, setSizePerPage] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);

  const [newTire, setNewTire] = useState<CreateTireRequest>({
    name: "",
    price: 0,
    description: "",
    stock: 0,
    type: ProductType.TIRE,
    season: "",
    size: "",
  });

  const navigate = useNavigate();

  const showAlert = (message: string, type: "success" | "error") => {
    const id = Date.now() + Math.random();
    setAlerts((prev) => [...prev, { id, message, type }]);
  };

  const removeAlert = (id: number) => {
    setAlerts((prev) => prev.filter((a) => a.id !== id));
  };

  const handleAxiosError = (err: unknown, fallback: string) => {
    const message = (err as any)?.response?.data?.message || fallback;
    showAlert(message, "error");
  };

  const loadTires = async () => {
    try {
      setLoading(true);
      const data = await getTire({
        ...filters,
        page,
        sizePerPage,
      });
      setTires(data.content);
      setTotalPages(data.totalPages);
    } catch (err) {
      handleAxiosError(err, "Błąd podczas ładowania produktów");
    } finally {
      setLoading(false);
    }
  };

  const handleCreateProduct = async () => {
    try {
      const created = await createTires([
        { ...newTire, type: ProductType.TIRE },
      ]);
      showAlert(`Dodano Oponę: ${created[0].name}`, "success");
      setNewTire({
        name: "",
        price: 0,
        description: "",
        stock: 0,
        type: ProductType.TIRE,
        season: "",
        size: "",
      });
      loadTires();
    } catch (err) {
      handleAxiosError(err, "Nie udało się dodać produktu");
    }
  };

  useEffect(() => {
    loadTires();
  }, [filters, page, sizePerPage]);

  return (
    <>
      <AdminNavbar />
      <div className="admin-panel-container" style={{ marginTop: 80 }}>
        <h1 className="admin-product-panel">Panel Opon</h1>

        {/* LINKI DO INNYCH PANELI */}
        <div className="admin-product-links">
          <button
            className="admin-product-link-btn"
            onClick={() => navigate("/admin/products")}
          >
            ALL
          </button>
          <button
            className="admin-product-link-btn"
            onClick={() => navigate("/admin/products/rims")}
          >
            Felgi
          </button>
          <button
            className="admin-product-link-btn"
            onClick={() => navigate("/admin/products/accessories")}
          >
            Akcesoria
          </button>
        </div>

        {/* FILTRY */}
        <div className="admin-product-form">
          <input
            className="admin-product-input"
            placeholder="Nazwa"
            value={filterInputs.name || ""}
            onChange={(e) =>
              setFilterInputs({ ...filterInputs, name: e.target.value })
            }
          />
          <input
            className="admin-product-input"
            placeholder="Sezon"
            value={filterInputs.season?.join(",") || ""}
            onChange={(e) =>
              setFilterInputs({
                ...filterInputs,
                season: e.target.value
                  .split(",")
                  .map((s) => s.trim())
                  .filter((s) => s !== ""),
              })
            }
          />
          <input
            className="admin-product-input"
            placeholder="Rozmiar"
            value={filterInputs.size?.join(",") || ""}
            onChange={(e) =>
              setFilterInputs({
                ...filterInputs,
                size: e.target.value
                  .split(",")
                  .map((s) => s.trim())
                  .filter((s) => s !== ""),
              })
            }
          />
          <input
            className="admin-product-input"
            type="number"
            placeholder="Cena min"
            value={filterInputs.minPrice || ""}
            onChange={(e) =>
              setFilterInputs({
                ...filterInputs,
                minPrice: parseFloat(e.target.value) || undefined,
              })
            }
          />
          <input
            className="admin-product-input"
            type="number"
            placeholder="Cena max"
            value={filterInputs.maxPrice || ""}
            onChange={(e) =>
              setFilterInputs({
                ...filterInputs,
                maxPrice: parseFloat(e.target.value) || undefined,
              })
            }
          />
          <select
            value={sizePerPage}
            onChange={(e) => {
              setSizePerPage(parseInt(e.target.value));
              setPage(0);
            }}
          >
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={25}>25</option>
            <option value={50}>50</option>
          </select>
          <button
            className="admin-product-submit-btn"
            onClick={() => {
              setPage(0);
              setFilters(filterInputs);
            }}
          >
            Szukaj
          </button>
        </div>

        <button
          className="admin-product-toggle-btn"
          onClick={() => setIsFormVisible((v) => !v)}
        >
          {isFormVisible ? "Ukryj formularz" : "Dodaj produkt"}
        </button>

        {isFormVisible && (
          <div className="admin-product-add-form">
            <h2>Dodaj nowy produkt (Opona)</h2>

            <div className="form-group">
              <label htmlFor="productName">Nazwa produktu</label>
              <input
                id="productName"
                className="admin-product-input"
                placeholder="Nazwa"
                value={newTire.name}
                onChange={(e) =>
                  setNewTire({ ...newTire, name: e.target.value })
                }
              />
            </div>

            <div className="form-group">
              <label htmlFor="productPrice">Cena (zł)</label>
              <input
                id="productPrice"
                className="admin-product-input"
                type="number"
                min="0"
                step="0.01"
                placeholder="Cena"
                value={newTire.price}
                onChange={(e) =>
                  setNewTire({
                    ...newTire,
                    price: parseFloat(e.target.value) || 0,
                  })
                }
              />
            </div>

            <div className="form-group">
              <label htmlFor="productDescription">Opis</label>
              <input
                id="productDescription"
                className="admin-product-input"
                placeholder="Opis"
                value={newTire.description}
                onChange={(e) =>
                  setNewTire({
                    ...newTire,
                    description: e.target.value,
                  })
                }
              />
            </div>

            <div className="form-group">
              <label htmlFor="productStock">Stan magazynowy</label>
              <input
                id="productStock"
                className="admin-product-input"
                type="number"
                min="0"
                step="1"
                placeholder="Stan magazynowy"
                value={newTire.stock}
                onChange={(e) =>
                  setNewTire({
                    ...newTire,
                    stock: parseInt(e.target.value) || 0,
                  })
                }
              />
              <small className="input-description">
                Ilość dostępna w magazynie
              </small>
            </div>

            <div className="form-group">
              <label htmlFor="productSeason">Sezon</label>
              <input
                id="productSeason"
                className="admin-product-input"
                placeholder="Sezon"
                value={newTire.season}
                onChange={(e) =>
                  setNewTire({ ...newTire, season: e.target.value })
                }
              />
            </div>

            <div className="form-group">
              <label htmlFor="productSize">Rozmiar</label>
              <input
                id="productSize"
                className="admin-product-input"
                placeholder="Rozmiar"
                value={newTire.size}
                onChange={(e) =>
                  setNewTire({ ...newTire, size: e.target.value })
                }
              />
            </div>

            <button
              className="admin-product-submit-btn"
              onClick={handleCreateProduct}
            >
              Dodaj produkt
            </button>
          </div>
        )}

        {/* LISTA PRODUKTÓW */}
        {loading ? (
          <p>Ładowanie produktów...</p>
        ) : (
          <table className="admin-product-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nazwa</th>
                <th>Cena</th>
                <th>Typ</th>
              </tr>
            </thead>
            <tbody>
              {tires.map((p) => (
                <tr
                  key={p.id}
                  onClick={() =>
                    navigate(`/admin/products/${p.type.toLowerCase()}/${p.id}`)
                  }
                  style={{ cursor: "pointer" }}
                >
                  <td>{p.id}</td>
                  <td>{p.name}</td>
                  <td>{p.price} zł</td>
                  <td>{p.type}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {/* PAGINACJA */}
        <div className="admin-product-pagination" style={{ marginTop: 20 }}>
          <button
            className="admin-product-link-btn"
            onClick={() => setPage((p) => Math.max(p - 1, 0))}
            disabled={page === 0}
          >
            Poprzednia
          </button>
          <span style={{ alignSelf: "center", margin: "0 10px" }}>
            Strona {page + 1} z {totalPages}
          </span>
          <button
            className="admin-product-link-btn"
            onClick={() => setPage((p) => p + 1)}
            disabled={page + 1 >= totalPages}
          >
            Następna
          </button>
        </div>

        <AlertStack alerts={alerts} onRemove={removeAlert} />
      </div>
    </>
  );
}

export default AdminTirePanelPage;
