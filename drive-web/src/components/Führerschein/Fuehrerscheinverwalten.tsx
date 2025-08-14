import React, { useEffect, useState } from "react";
import { FiEdit, FiTrash2 } from "react-icons/fi";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const PRIMARY_COLOR = "#174bd1ff";

interface Fuehrerschein {
  fuehrerscheinKlasse: string;
  fahrzeuge_Ekl: string;
  mindestalter: number;
  voraussetzung: string;
}

const Fuehrerscheinverwalten: React.FC = () => {
  const [fuehrerscheinKlasse, setFuehrerscheinKlasse] = useState("");
  const [fahrzeuge_Ekl, setFahrzeuge_Ekl] = useState("");
  const [mindestalter, setMindestalter] = useState<number | "">("");
  const [voraussetzung, setVoraussetzung] = useState("");

  // List
  const [liste, setListe] = useState<Fuehrerschein[]>([]);
  const [errorMessage, setErrorMessage] = useState("");

  const navigate = useNavigate();

  // List beim seite-Öffnen
  useEffect(() => {
    fetchFuehrerschein();
  }, []);

  const fetchFuehrerschein = async () => {
    try {
      const response = await axios.get<Fuehrerschein[]>(
        "http://localhost:8080/api/fuehrerscheinlist"
      );
      setListe(response.data);
    } catch (err) {
      console.error("Fehler beim Laden der Führerscheine:", err);
      setErrorMessage("Führerscheinliste konnte nicht geladen werden.");
    }
  };
  // List wird geladen wenn man seite öffnt 
   useEffect(() => {
    fetchFuehrerschein();
  }, []);

  const handleAdd = async () => {
    if (!fuehrerscheinKlasse || !fahrzeuge_Ekl || mindestalter === "" || !voraussetzung) {
      setErrorMessage("Bitte alle Pflichtfelder ausfüllen.");
      return;
    }

    const newItem: Fuehrerschein = {
      fuehrerscheinKlasse,
      fahrzeuge_Ekl,
      mindestalter: Number(mindestalter),
      voraussetzung,
    };

    try {
      await axios.post("http://localhost:8080/api/fuehrerscheinneuanlage", newItem);
      // Formu_Clean
      setFuehrerscheinKlasse("");
      setFahrzeuge_Ekl("");
      setMindestalter("");
      setVoraussetzung("");

      setErrorMessage("Erfolgreich gespeichert!");
    } catch (error: any) {
      console.error("Fehler beim Speichern:", error?.response?.data || error?.message);
      if (error?.response?.data) {
        const data = error.response.data;
        const msg = typeof data === "string" ? data : data.message || JSON.stringify(data);
        setErrorMessage(msg);
      } else {
        setErrorMessage("Fehler beim Speichern!");
      }
    }
  };

  const handleDelete = async (fuehrerscheinKlasse: string) => {
    if (!window.confirm("Soll dieser Führerschein gelöscht werden?")) return;
    try {
      await axios.delete(`http://localhost:8080/api/fuehrerschein/${fuehrerscheinKlasse}`);
      await fetchFuehrerschein();
    } catch (e) {
      console.error(e);
      alert("Löschen fehlgeschlagen!");
    }
  };

  return (
    <div style={page}>
      <h2 style={title}>Führerschein Verwalten</h2>

      {errorMessage && (
        <div
          style={{
            color: errorMessage.includes("Erfolgreich") ? "green" : "red",
            marginBottom: "1rem",
          }}
        >
          {errorMessage}
        </div>
      )}

      {/* Form */}
      <div style={formBlock}>
        <label style={label}>
          Führerscheinklasse <span style={{ color: "red" }}>*</span>
        </label>
        <input
          type="text"
          value={fuehrerscheinKlasse}
          onChange={(e) => setFuehrerscheinKlasse(e.target.value)}
          style={input}
        />

        <label style={label}>Fahrzeuge</label>
        <input
          type="text"
          value={fahrzeuge_Ekl}
          onChange={(e) => setFahrzeuge_Ekl(e.target.value)}
          style={input}
        />

        <label style={label}>Mindestalter</label>
        <input
          type="number"
          value={mindestalter}
          onChange={(e) =>
            setMindestalter(e.target.value === "" ? "" : Number(e.target.value))
          }
          style={input}
        />

        <label style={label}>Voraussetzung</label>
        <textarea
          value={voraussetzung}
          onChange={(e) => setVoraussetzung(e.target.value)}
          style={{ ...input, height: "3rem", resize: "vertical" }}
        />

        <div style={{ display: "flex", justifyContent: "flex-end" }}>
          <button onClick={handleAdd} style={buttonPrimary}>
            Hinzufügen
          </button>
        </div>
      </div>

      {/* Liste */}
      <h2 style={titleStyle}>Führerschein</h2>
      <table style={tableStyle}>
        <thead>
          <tr>
            <th style={thStyle}>Führerscheinklasse</th>
            <th style={thStyle}>Fahrzeuge</th>
            <th style={thStyle}>Mindestalter</th>
            <th style={thStyle}>Voraussetzung</th>
            <th style={thStyle}>Action</th>
          </tr>
        </thead>
        <tbody>
          {liste.map((item) => (
            <tr key={item.fuehrerscheinKlasse}>
              <td style={tdStyle}>{item.fuehrerscheinKlasse}</td>
              <td style={tdStyle}>{item.fahrzeuge_Ekl}</td>
              <td style={tdStyle}>{item.mindestalter}</td>
              <td style={tdStyle}>{item.voraussetzung}</td>
              <td style={tdStyle}>
                <button
                  type="button"
                  style={iconButton}
                  onClick={() =>
                    navigate(`/fuehrerscheinbearbeiten/${item.fuehrerscheinKlasse}`)
                  }
                  aria-label="Führerschein bearbeiten"
                  title="Führerschein bearbeiten"
                >
                  <FiEdit size={20} />
                </button>

                <button
                  type="button"
                  style={{ ...iconButton, marginLeft: "0.5rem" }}
                  onClick={() => handleDelete(item.fuehrerscheinKlasse)}
                  aria-label="Löschen"
                  title="Löschen"
                >
                  <FiTrash2 size={20} />
                </button>
              </td>
            </tr>
          ))}
          {liste.length === 0 && (
            <tr>
              <td style={tdStyle} colSpan={5}>
                Keine Daten vorhanden.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

// Styles
const page: React.CSSProperties = {
  fontFamily: "Arial, sans-serif",
  padding: "2rem",
  minHeight: "100vh",
  position: "relative",
};

const title: React.CSSProperties = {
  color: PRIMARY_COLOR,
  fontSize: "1.4rem",
  fontWeight: "bold",
  marginBottom: "1rem",
};

const titleStyle: React.CSSProperties = {
  color: PRIMARY_COLOR,
  fontSize: "1.4rem",
  fontWeight: "bold",
  margin: "1.5rem 0 0.5rem",
};

const formBlock: React.CSSProperties = {
  marginBottom: "2rem",
  display: "flex",
  flexDirection: "column",
  gap: "0.8rem",
  maxWidth: 400,
};

const label: React.CSSProperties = {
};

const input: React.CSSProperties = {
  width: "100%",
  padding: "0.5rem 0",
  border: "none",
  borderBottom: "1px solid #ccc",
  backgroundColor: "#fff",
  fontSize: "1rem",
  outline: "none",
};

const buttonPrimary: React.CSSProperties = {
  backgroundColor: PRIMARY_COLOR,
  color: "#fff",
  padding: "0.5rem 1.2rem",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
};

const tableStyle: React.CSSProperties = {
  width: "100%",
  borderCollapse: "collapse",
};

const thStyle: React.CSSProperties = {
  borderBottom: "2px solid #ccc",
  padding: "0.5rem",
  textAlign: "left",
  backgroundColor: "#f5f5f5",
  color: PRIMARY_COLOR,
  fontWeight: "bold",
};

const tdStyle: React.CSSProperties = {
  borderBottom: "1px solid #ddd",
  padding: "0.5rem",
  verticalAlign: "top",
};

const iconButton: React.CSSProperties = {
  background: "none",
  border: "none",
  color: PRIMARY_COLOR,
  cursor: "pointer",
  fontSize: "1.2rem",
  padding: 0,
};

export default Fuehrerscheinverwalten;
