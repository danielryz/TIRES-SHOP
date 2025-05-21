import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Product, ProductType } from "../../../types/Product";
import { Tire } from "../../../types/Tire";
import { Rim } from "../../../types/Rim";
import { Accessory, AccessoryType } from "../../../types/Accessory";

import { getTireById, updateTire } from "../../../api/tiresApi";
import { getRimById, updateRim } from "../../../api/rimApi";
import { getAccessoryById, updateAccessory } from "../../../api/accessoryApi";
import { getProductById, updateProduct } from "../../../api/productApi";

import AlertStack from "../../../components/alert/AlertStack";
import AdminNavbar from "../../../components/navbars/AdminNavbar";
import "./AdminProductDetailPage.css";
import { Image } from "../../../types/Image";
import { deleteImage, getImagesByProductId } from "../../../api/imageApi";
import ProductImageUpload from "./ProductImageUpload";

function AdminProductDetailPage() {
  const params = useParams<{ id: string; type: string }>();
  const id = Number(params.id);
  const rawType = params.type?.toUpperCase();
  const navigate = useNavigate();
  const type = Object.values(ProductType).includes(rawType as ProductType)
    ? (rawType as ProductType)
    : null;

  const [product, setProduct] = useState<
    Tire | Rim | Accessory | Product | null
  >(null);
  const [images, setImages] = useState<Image[]>([]);
  const [formData, setFormData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [alerts, setAlerts] = useState<
    { id: number; message: string; type: "success" | "error" }[]
  >([]);

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
  const accessoryTypeLabels: Record<AccessoryType, string> = {
    [AccessoryType.TOOLS]: "Narzędzia",
    [AccessoryType.BOLT]: "Śruby",
    [AccessoryType.CHAINS]: "Łańcuchy",
    [AccessoryType.JACK]: "Podnośniki",
    [AccessoryType.SENSOR]: "Czujniki",
  };

  const productTypeLabels: Record<ProductType, string> = {
    [ProductType.ALL]: "Wszystko",
    [ProductType.RIM]: "Felgi",
    [ProductType.TIRE]: "Opony",
    [ProductType.ACCESSORY]: "Akcesoria",
  };

  useEffect(() => {
    const loadProduct = async () => {
      if (!id || !type) {
        showAlert("Brak ID lub nieprawidłowy typ produktu", "error");
        return;
      }

      try {
        setLoading(true);
        let data;

        switch (type) {
          case ProductType.TIRE:
            data = await getTireById(id);
            break;
          case ProductType.RIM:
            data = await getRimById(id);
            break;
          case ProductType.ACCESSORY:
            data = await getAccessoryById(id);
            break;
          case ProductType.ALL:
            data = await getProductById(id);
            break;
          default:
            throw new Error("Nieznany typ produktu");
        }

        setProduct(data);
        setFormData(data);
      } catch (err) {
        handleAxiosError(err, "Błąd podczas pobierania produktu");
      } finally {
        setLoading(false);
      }
    };

    loadProduct();
  }, [id, type]);

  useEffect(() => {
    const fetchImages = async () => {
      try {
        const imgs = await getImagesByProductId(id);
        setImages(imgs);
      } catch (err) {
        handleAxiosError(err, "Błąd przy pobieraniu zdjęć:");
      }
    };

    fetchImages();
  }, [id]);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFormData((prev: any) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleUpdate = async () => {
    try {
      switch (type) {
        case ProductType.TIRE:
          await updateTire(id, formData);
          break;
        case ProductType.RIM:
          await updateRim(id, formData);
          break;
        case ProductType.ACCESSORY:
          await updateAccessory(id, formData);
          break;
        case ProductType.ALL:
          await updateProduct(id, formData);
          break;
        default:
          throw new Error("Nieznany typ produktu");
      }
      showAlert("Produkt zaktualizowany pomyślnie", "success");
    } catch (err) {
      handleAxiosError(err, "Błąd podczas aktualizacji produktu");
    }
  };

  const handleDeleteImage = async (imageId: number) => {
    try {
      const response = await deleteImage(imageId);
      const updated = await getImagesByProductId(id);
      setImages(updated);
      showAlert(response || "Zdjęcie zsostało usunięte", "success");
    } catch (error) {
      console.error("Błąd przy usuwaniu zdjęcia:", error);
    }
  };

  if (loading) return <p>Ładowanie...</p>;
  if (!product || !formData) return <p>Nie znaleziono produktu</p>;

  return (
    <>
      <AdminNavbar />
      <div className="admin-product-detail-page" style={{ marginTop: 80 }}>
        <div className="admin-product-links">
          {type === ProductType.TIRE && (
            <>
              <button
                className="admin-product-link-btn"
                onClick={() => navigate("/admin/products/tires")}
              >
                Powrót
              </button>
            </>
          )}
          {type === ProductType.RIM && (
            <>
              <button
                className="admin-product-link-btn"
                onClick={() => navigate("/admin/products/rims")}
              >
                Powrót
              </button>
            </>
          )}
          {type === ProductType.ACCESSORY && (
            <>
              <button
                className="admin-product-link-btn"
                onClick={() => navigate("/admin/products/accessories")}
              >
                Powrót
              </button>
            </>
          )}
          {type === ProductType.ALL && (
            <>
              <button
                className="admin-product-link-btn"
                onClick={() => navigate("/admin/products")}
              >
                Powrót
              </button>
            </>
          )}
        </div>
        <div className="admin-product-images-section">
          <h3>Zdjęcia produktu</h3>

          <div className="admin-product-images-grid">
            {images.map((img) => (
              <div key={img.id} className="admin-product-image-wrapper">
                <img src={img.url} alt="Product" />
                <button
                  className="admin-product-delete-img-btn"
                  onClick={() => handleDeleteImage(img.id)}
                >
                  Usuń
                </button>
              </div>
            ))}
          </div>

          <div className="admin-product-form-group">
            <label>Dodaj zdjęcie</label>
            <ProductImageUpload
              productId={id}
              onUploadSuccess={async () => {
                const updated = await getImagesByProductId(id);
                setImages(updated);
              }}
            />
          </div>
        </div>
        <div className="admin-product-detail-section">
          <h2>Edycja produktu</h2>

          <div className="admin-product-form-group">
            <label>Nazwa</label>
            <input
              className="admin-product-form-input"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
            />
          </div>

          <div className="admin-product-form-group">
            <label>Cena</label>
            <input
              className="admin-product-form-input"
              name="price"
              type="number"
              value={formData.price}
              onChange={handleInputChange}
            />
          </div>

          <div className="admin-product-form-group">
            <label>Opis</label>
            <input
              className="admin-product-form-input"
              name="description"
              value={formData.description}
              onChange={handleInputChange}
            />
          </div>

          <div className="admin-product-form-group">
            <label>Stan magazynowy</label>
            <input
              className="admin-product-form-input"
              name="stock"
              type="number"
              value={formData.stock}
              onChange={handleInputChange}
            />
          </div>

          <div className="admin-product-form-group">
            <label>Typ produktu</label>
            <input
              className="admin-product-form-input"
              name="accessoryType"
              value={productTypeLabels[formData.type as ProductType]}
              readOnly
            ></input>
          </div>

          {/* Pola specyficzne dla typu */}
          {type === ProductType.TIRE && (
            <>
              <div className="admin-product-form-group">
                <label>Sezon</label>
                <input
                  className="admin-product-form-input"
                  name="season"
                  value={formData.season}
                  onChange={handleInputChange}
                />
              </div>
              <div className="admin-product-form-group">
                <label>Rozmiar</label>
                <input
                  className="admin-product-form-input"
                  name="size"
                  value={formData.size}
                  onChange={handleInputChange}
                />
              </div>
            </>
          )}

          {type === ProductType.RIM && (
            <>
              <div className="admin-product-form-group">
                <label>Materiał</label>
                <input
                  className="admin-product-form-input"
                  name="material"
                  value={formData.material}
                  onChange={handleInputChange}
                />
              </div>
              <div className="admin-product-form-group">
                <label>Rozstaw śrub</label>
                <input
                  className="admin-product-form-input"
                  name="boltPattern"
                  value={formData.boltPattern}
                  onChange={handleInputChange}
                />
              </div>
              <div className="admin-product-form-group">
                <label>Rozmiar</label>
                <input
                  className="admin-product-form-input"
                  name="size"
                  value={formData.size}
                  onChange={handleInputChange}
                />
              </div>
            </>
          )}
          {type === ProductType.ACCESSORY && (
            <div className="admin-product-form-group">
              <label>Typ akcesorium</label>
              <select
                className="admin-product-form-input"
                name="accessoryType"
                value={formData.accessoryType}
                onChange={handleInputChange}
              >
                {Object.entries(accessoryTypeLabels).map(([key, label]) => (
                  <option key={key} value={key}>
                    {label}
                  </option>
                ))}
              </select>
            </div>
          )}

          <button
            className="admin-product-update-button"
            onClick={handleUpdate}
          >
            Zapisz zmiany
          </button>
        </div>
        <AlertStack alerts={alerts} onRemove={removeAlert} />
      </div>
    </>
  );
}

export default AdminProductDetailPage;
