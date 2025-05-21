import React, { useState } from "react";
import { uploadImageForProduct } from "../../../api/imageApi";
import { Image } from "../../../types/Image";
import AlertStack from "../../../components/alert/AlertStack";
import "./AdminProductDetailPage.css";
function ProductImageUpload({
  productId,
  onUploadSuccess,
}: {
  productId: number;
  onUploadSuccess: (img: Image) => void;
}) {
  const [file, setFile] = useState<File | null>(null);
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
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files?.length) {
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!file) return;
    try {
      const uploadedImage = await uploadImageForProduct(productId, file);
      onUploadSuccess(uploadedImage);
      showAlert("Upload successful", "success");
      setFile(null);
    } catch (err) {
      console.error(err);
      handleAxiosError(err, "Upload failed");
    }
  };

  return (
    <>
      <input type="file" onChange={handleChange} />
      <button
        className="admin-product-update-button"
        onClick={handleUpload}
        disabled={!file}
      >
        Dodaj zdjęcie
      </button>
      <AlertStack alerts={alerts} onRemove={removeAlert} />
    </>
  );
}

export default ProductImageUpload;
