import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Accessory,
  AccessoryFilterParams,
  CreateAccessoryRequest,
  AccessoryType,
} from "../../../types/Accessory";
import AlertStack from "../../../components/alert/AlertStack";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
import "./AdminProductPanelPage.css";
import { ProductType } from "../../../types/Product";
import { createAccessories, getAccessories } from "../../../api/accessoryApi";

function AdminAccessoryPanelPage() {
  const [accessories, setAccessories] = useState<Accessory[]>([]);
  const [filters, setFilters] = useState<AccessoryFilterParams>();
  const [filterInputs, setFilterInputs] = useState<AccessoryFilterParams>({});
  const [page, setPage] = useState(0);
  const [sizePerPage, setSizePerPage] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);

  const [newAccessory, setNewAccessory] = useState<CreateAccessoryRequest>({
    name: "",
    price: 0,
    stock: 0,
    description: "",
    type: ProductType.TIRE,
    accessoryType: "" as AccessoryType,
  });
  const accessoryTypeLabels: Record<AccessoryType, string> = {
    [AccessoryType.TOOLS]: "Narzędzia",
    [AccessoryType.BOLT]: "Śruby",
    [AccessoryType.CHAINS]: "Łańcuchy",
    [AccessoryType.JACK]: "Podnośniki",
    [AccessoryType.SENSOR]: "Czujniki",
  };

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

  const loadAccessories = async () => {
    try {
      setLoading(true);
      const data = await getAccessories({
        ...filters,
        page,
        sizePerPage,
      });
      setAccessories(data.content);
      setTotalPages(data.totalPages);
    } catch (err) {
      handleAxiosError(err, "Błąd podczas ładowania produktów");
    } finally {
      setLoading(false);
    }
  };

  const handleCreateProduct = async () => {
    try {
      const created = await createAccessories([
        { ...newAccessory, type: ProductType.ACCESSORY },
      ]);
      showAlert(`Dodano produkt: ${created[0].name}`, "success");
      setNewAccessory({
        name: "",
        price: 0,
        stock: 0,
        description: "",
        type: ProductType.TIRE,
        accessoryType: "" as AccessoryType,
      });
      loadAccessories();
    } catch (err) {
      handleAxiosError(err, "Nie udało się dodać produktu");
    }
  };

  useEffect(() => {
    loadAccessories();
  }, [filters, page, sizePerPage]);

  return (
    <>
      <AdminNavbar />
      <div className="admin-panel-container" style={{ marginTop: 80 }}>
        <h1 className="admin-product-panel">Panel Akcesoriów</h1>

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
            onClick={() => navigate("/admin/products/rims")}
          >
            Felgi
          </button>
          <button
            className="admin-product-link-btn"
            onClick={() => navigate("/admin/products")}
          >
            ALL
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
          <select
            className="admin-product-input"
            multiple
            value={filterInputs.accessoryType || []}
            onChange={(e) =>
              setFilterInputs({
                ...filterInputs,
                accessoryType: Array.from(e.target.selectedOptions).map(
                  (option) => option.value as AccessoryType,
                ),
              })
            }
          >
            {Object.values(AccessoryType).map((type) => (
              <option key={type} value={type}>
                {accessoryTypeLabels[type]}
              </option>
            ))}
          </select>
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
            <h2>Dodaj nowy produkt (Akcesorium)</h2>

            <div className="form-group">
              <label htmlFor="productName">Nazwa produktu</label>
              <input
                id="productName"
                className="admin-product-input"
                placeholder="Nazwa"
                value={newAccessory.name}
                onChange={(e) =>
                  setNewAccessory({ ...newAccessory, name: e.target.value })
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
                value={newAccessory.price}
                onChange={(e) =>
                  setNewAccessory({
                    ...newAccessory,
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
                value={newAccessory.description}
                onChange={(e) =>
                  setNewAccessory({
                    ...newAccessory,
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
                value={newAccessory.stock}
                onChange={(e) =>
                  setNewAccessory({
                    ...newAccessory,
                    stock: parseInt(e.target.value) || 0,
                  })
                }
              />
              <small className="input-description">
                Ilość dostępna w magazynie
              </small>
            </div>

            <div className="form-group">
              <label htmlFor="productAccessoryType">Typ Akcesorium</label>
              <select
                id="productAccessoryType"
                className="admin-product-input"
                value={newAccessory.accessoryType}
                onChange={(e) =>
                  setNewAccessory({
                    ...newAccessory,
                    accessoryType: e.target.value as AccessoryType,
                  })
                }
              >
                <option value="">-- Wybierz typ --</option>
                {Object.values(AccessoryType).map((type) => (
                  <option key={type} value={type}>
                    {accessoryTypeLabels[type]}
                  </option>
                ))}
              </select>
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
                <th>Stan magazynowy</th>
                <th>Typ</th>
                <th>Typ Akcesorium</th>
              </tr>
            </thead>
            <tbody>
              {accessories.map((p) => (
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
                  <td>{p.stock}</td>
                  <td>{p.type}</td>
                  <td>{accessoryTypeLabels[p.accessoryType]}</td>
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

export default AdminAccessoryPanelPage;
