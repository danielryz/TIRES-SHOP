import React, { useState } from "react";
import { createTires } from "../../../api/tiresApi";
import { ProductType } from "../../../types/Tire";

const AddTiresForm: React.FC = () => {
  const [tires, setTires] = useState([
    {
      name: "",
      price: 0,
      description: "",
      stock: 0,
      type: ProductType.TIRE,
      season: "",
      size: "",
    },
  ]);

  const handleSubmit = async () => {
    try {
      const created = await createTires(tires);
      console.log("Utworzone opony:", created);
    } catch (error) {
      console.error("Błąd przy tworzeniu:", error);
    }
  };

  function updateField(index: number, field: string, value: any) {
    const updated = [...tires];
    updated[index] = { ...updated[index], [field]: value };
    setTires(updated);
  }

  return (
    <form className="add-tires-form">
      <div>
        <h2>Dodaj nowe opony</h2>
        {tires.map((tire, index) => (
          <div key={index}>
            <input
              value={tire.name}
              onChange={(e) => updateField(index, "name", e.target.value)}
              placeholder="Nazwa"
            />
            <input
              type="number"
              value={tire.price}
              onChange={(e) =>
                updateField(index, "price", parseFloat(e.target.value))
              }
              placeholder="Cena"
            />
            <input
              type="number"
              value={tire.stock}
              onChange={(e) =>
                updateField(index, "stock", parseFloat(e.target.value))
              }
              placeholder="Ilość"
            />
            <input
              value={tire.season}
              onChange={(e) => updateField(index, "season", e.target.value)}
              placeholder="Sezon"
            />
            <input
              value={tire.size}
              onChange={(e) => updateField(index, "size", e.target.value)}
              placeholder="Rozmiar"
            />
            <input
              className="description"
              type="text"
              value={tire.description}
              onChange={(e) =>
                updateField(index, "description", e.target.value)
              }
              placeholder="description"
            />
          </div>
        ))}
        <button
          onClick={() =>
            setTires([
              ...tires,
              {
                name: "",
                price: 0,
                description: "",
                stock: 0,
                type: ProductType.TIRE,
                season: "",
                size: "",
              },
            ])
          }
        >
          Dodaj kolejny
        </button>
        <button onClick={handleSubmit}>Wyślij</button>
      </div>
    </form>
  );
};

export default AddTiresForm;
