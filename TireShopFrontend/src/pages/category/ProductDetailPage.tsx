import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Product, ProductType } from "../../types/Product";
import { Tire } from "../../types/Tire";
import { Rim } from "../../types/Rim";
import { Accessory, AccessoryType } from "../../types/Accessory";

import { getTireById } from "../../api/tiresApi";
import { getRimById } from "../../api/rimApi";
import { getAccessoryById } from "../../api/accessoryApi";
import { getProductById } from "../../api/productApi";
import AlertStack from "../../components/alert/AlertStack";
import "./ProductDetailPage.css";
import { Image } from "../../types/Image";
import { getImagesByProductId } from "../../api/imageApi";
import { addToCart } from "../../api/cartApi";
import { AxiosError } from "axios";
import { useCart } from "../../context/CartContext";

function ProductDetailPage() {
  const params = useParams<{ type: string; id: string }>();
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
  const [activeImage, setActiveImage] = useState<Image | null>(null);

  const [quantity, setQuantity] = useState(1);
  const { refreshCart } = useCart();
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

  const renderDescription = (description: string) => {
    const imageRegex = /https?:\/\/[^\s]+?\.(?:jpg|jpeg|png|webp|gif)/gi;

    const parts = description.split(imageRegex);

    const matches = description.match(imageRegex) || [];

    const combined = [];

    for (let i = 0; i < parts.length; i++) {
      combined.push(parts[i]);
      if (i < matches.length) {
        combined.push(matches[i]);
      }
    }

    return combined.map((part, i) => {
      if (imageRegex.test(part)) {
        return (
          <div
            key={i}
            style={{
              display: "flex",
              justifyContent: "center",
              margin: "12px 0",
            }}
          >
            <img
              src={part}
              alt="Opisowy obrazek"
              style={{
                width: "500px",
                height: "auto",
                borderRadius: "8px",
              }}
            />
          </div>
        );
      }

      return <p key={i}>{part}</p>;
    });
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
        if (imgs.length > 0) {
          setActiveImage(imgs[0]);
        }
      } catch (err) {
        handleAxiosError(err, "Błąd przy pobieraniu zdjęć:");
      }
    };

    fetchImages();
  }, [id]);

  const handleAddToCart = async () => {
    if (!product || !product.id) {
      showAlert("Nieprawidłowy produkt", "error");
      return;
    }

    try {
      await addToCart(product.id, quantity);
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

  const handleQuantityChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = parseInt(e.target.value, 10);

    if (isNaN(value)) value = 1;
    if (value < 1) value = 1;
    if (value > formData.stock) value = formData.stock;

    setQuantity(value);
  };

  if (loading) return <p>Ładowanie...</p>;
  if (!product || !formData) return <p>Nie znaleziono produktu</p>;

  return (
    <div className="product-detail-page">
      <div className="product-links">
        {type === ProductType.TIRE && (
          <button
            className="product-link-btn"
            onClick={() => navigate("/tires")}
          >
            Powrót
          </button>
        )}
        {type === ProductType.RIM && (
          <button
            className="product-link-btn"
            onClick={() => navigate("/rims")}
          >
            Powrót
          </button>
        )}
        {type === ProductType.ACCESSORY && (
          <button
            className="product-link-btn"
            onClick={() => navigate("/accessories")}
          >
            Powrót
          </button>
        )}
        {type === ProductType.ALL && (
          <button
            className="product-link-btn"
            onClick={() => navigate("/others")}
          >
            Powrót
          </button>
        )}
      </div>

      <div className="product-card">
        {/* Sekcja zdjęć */}
        <div className="product-images-section">
          <h3>Zdjęcia produktu</h3>

          {activeImage ? (
            <div className="main-image-wrapper">
              <img
                src={activeImage.url}
                alt="Active"
                className="main-product-image"
              />
            </div>
          ) : (
            <p>Brak zdjęcia głównego</p>
          )}

          <div className="thumbnail-grid">
            {images.map((img) => (
              <img
                key={img.id}
                src={img.url}
                alt="Miniatura"
                className={`thumbnail-image ${img.id === activeImage?.id ? "active" : ""}`}
                onClick={() => setActiveImage(img)}
              />
            ))}
          </div>
        </div>

        {/* Sekcja szczegółów */}
        <div className="product-info-table">
          <h2>Szczegóły produktu</h2>
          <table>
            <tbody>
              <tr>
                <td>Nazwa</td>
                <td>{formData.name}</td>
              </tr>
              <tr>
                <td>Cena</td>
                <td>{formData.price.toFixed(2)} zł</td>
              </tr>
              <tr>
                <td>Stan magazynowy</td>
                <td>{formData.stock}</td>
              </tr>
              <tr>
                <td>Typ produktu</td>
                <td>{productTypeLabels[formData.type as ProductType]}</td>
              </tr>

              {type === ProductType.TIRE && (
                <>
                  <tr>
                    <td>Sezon</td>
                    <td>{formData.season}</td>
                  </tr>
                  <tr>
                    <td>Rozmiar</td>
                    <td>{formData.size}</td>
                  </tr>
                </>
              )}

              {type === ProductType.RIM && (
                <>
                  <tr>
                    <td>Materiał</td>
                    <td>{formData.material}</td>
                  </tr>
                  <tr>
                    <td>Rozstaw śrub</td>
                    <td>{formData.boltPattern}</td>
                  </tr>
                  <tr>
                    <td>Rozmiar</td>
                    <td>{formData.size}</td>
                  </tr>
                </>
              )}

              {type === ProductType.ACCESSORY && (
                <tr>
                  <td>Typ akcesorium</td>
                  <td>
                    {
                      accessoryTypeLabels[
                        formData.accessoryType as AccessoryType
                      ]
                    }
                  </td>
                </tr>
              )}
            </tbody>
          </table>

          {/* Buybox */}
          <div className="buybox">
            <div className="buybox-price">
              {(formData.price * quantity).toFixed(2)} zł
            </div>
            <input
              type="number"
              min={1}
              max={formData.stock}
              value={quantity}
              onChange={(e) => handleQuantityChange(e)}
              className="buybox-quantity"
            />
            {formData.stock > 0 ? (
              <button
                className="buybox-button"
                onClick={(e) => {
                  e.stopPropagation();
                  {
                    handleAddToCart();
                  }
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
      <div className="product-description-section">
        <h3>Opis produktu</h3>
        {renderDescription(formData.description)}
      </div>
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </div>
  );
}

export default ProductDetailPage;
