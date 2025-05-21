import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Rim, RimFilterParams, CreateRimRequest } from "../../../types/Rim";
import { getRims, createRims } from "../../../api/rimApi";
import AlertStack from "../../../components/alert/AlertStack";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
import "./AdminProductPanelPage.css";
import { ProductType } from "../../../types/Product";

function AdminRimPanelPage() {
  const [rims, setRims] = useState<Rim[]>([]);
  const [filters, setFilters] = useState<RimFilterParams>();
  const [filterInputs, setFilterInputs] = useState<RimFilterParams>({});
  const [page, setPage] = useState(0);
  const [sizePerPage, setSizePerPage] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);

  const [newRim, setNewRim] = useState<CreateRimRequest>({
    name: "",
    price: 0,
    description: "",
    stock: 0,
    type: ProductType.RIM,
    material: "",
    size: "",
    boltPattern: "",
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

  const loadRims = async () => {
    try {
      setLoading(true);
      const data = await getRims({
        ...filters,
        page,
        sizePerPage,
      });
      setRims(data.content);
      setTotalPages(data.totalPages);
    } catch (err) {
      handleAxiosError(err, "Błąd podczas ładowania produktów");
    } finally {
      setLoading(false);
    }
  };

  const handleCreateProduct = async () => {
    try {
      const created = await createRims([{ ...newRim, type: ProductType.RIM }]);
      showAlert(`Dodano Felgę: ${created[0].name}`, "success");
      setNewRim({
        name: "",
        price: 0,
        description: "",
        stock: 0,
        type: ProductType.RIM,
        material: "",
        size: "",
        boltPattern: "",
      });
      loadRims();
    } catch (err) {
      handleAxiosError(err, "Nie udało się dodać produktu");
    }
  };

  useEffect(() => {
    loadRims();
  }, [filters, page, sizePerPage]);

  return (
    <>
      <AdminNavbar />
      <div className="admin-panel-container" style={{ marginTop: 80 }}>
        <h1 className="admin-product-panel">Panel Felg</h1>

        {/* LINKI DO INNYCH PANELI */}
        <div className="admin-product-links">
          <button
            className="admin-product-link-btn"
            onClick={() => navigate("/admin/products/tires")}
          >
            Opony
          </button>
          <button
            className="admin-product-link-btn"
            onClick={() => navigate("/admin/products")}
          >
            ALL
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
            placeholder="Materiał"
            value={filterInputs.material?.join(",") || ""}
            onChange={(e) =>
              setFilterInputs({
                ...filterInputs,
                material: e.target.value
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
            placeholder="Rozstaw śrub,wzorzec"
            value={filterInputs.boltPattern?.join(",") || ""}
            onChange={(e) =>
              setFilterInputs({
                ...filterInputs,
                boltPattern: e.target.value
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
            <h2>Dodaj nowy produkt (Felga)</h2>

            <div className="form-group">
              <label htmlFor="productName">Nazwa produktu</label>
              <input
                id="productName"
                className="admin-product-input"
                placeholder="Nazwa"
                value={newRim.name}
                onChange={(e) => setNewRim({ ...newRim, name: e.target.value })}
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
                value={newRim.price}
                onChange={(e) =>
                  setNewRim({
                    ...newRim,
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
                value={newRim.description}
                onChange={(e) =>
                  setNewRim({
                    ...newRim,
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
                value={newRim.stock}
                onChange={(e) =>
                  setNewRim({
                    ...newRim,
                    stock: parseInt(e.target.value) || 0,
                  })
                }
              />
              <small className="input-description">
                Ilość dostępna w magazynie
              </small>
            </div>

            <div className="form-group">
              <label htmlFor="productMaterial">Material</label>
              <input
                id="productMaterial"
                className="admin-product-input"
                placeholder="Material"
                value={newRim.material}
                onChange={(e) =>
                  setNewRim({ ...newRim, material: e.target.value })
                }
              />
            </div>

            <div className="form-group">
              <label htmlFor="productSize">Size</label>
              <input
                id="productSize"
                className="admin-product-input"
                placeholder="Size"
                value={newRim.size}
                onChange={(e) => setNewRim({ ...newRim, size: e.target.value })}
              />
            </div>

            <div className="form-group">
              <label htmlFor="productBoltPattern">Bolt Pattern</label>
              <input
                id="productBoltPattern"
                className="admin-product-input"
                placeholder="Bolt Pattern"
                value={newRim.boltPattern}
                onChange={(e) =>
                  setNewRim({ ...newRim, boltPattern: e.target.value })
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
              {rims.map((p) => (
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

export default AdminRimPanelPage;
