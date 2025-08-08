import { useEffect, useState } from "react";
import type { CSSProperties } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { FiEdit, FiTrash2 } from "react-icons/fi";
import { FaUserEdit } from "react-icons/fa";

const PRIMARY_COLOR = "#174bd1ff";

const MandantenListe = () => {
  const [mandanten, setMandanten] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchMandanten();
  }, []);

  const fetchMandanten = async () => {
    try {
      const response = await axios.get("/api/mandanten");
      setMandanten(response.data);
    } catch (err) {
      console.error("Fehler beim Laden der Mandanten:", err);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Soll dieser Mandant gelöscht werden?")) return;
    try {
      await axios.delete(`/api/mandanten/${id}`);
      setMandanten((prev) => prev.filter((m: any) => m.id !== id));
    } catch (e) {
      alert("Löschen fehlgeschlagen!");
    }
  };

  return (
    <div style={page}>
      <h2 style={titleStyle}>Mandanten</h2>
      <table style={tableStyle}>
        <thead>
          <tr>
            <th style={thStyle}>ID</th>
            <th style={thStyle}>Beschreibung</th>
            <th style={thStyle}>Name 1</th>
            <th style={thStyle}>Name 2</th>
            <th style={thStyle}>Local</th>
            <th style={thStyle}>Action</th>
          </tr>
        </thead>
        <tbody>
          {mandanten.map((item: any) => (
            <tr key={item.id}>
              <td style={tdStyle}>{item.id}</td>
              <td style={tdStyle}>{item.beschreibung}</td>
              <td style={tdStyle}>{item.name1}</td>
              <td style={tdStyle}>{item.name2}</td>
              <td style={tdStyle}>{item.local}</td>
              <td style={tdStyle}>
                <button
                  type="button"
                  style={iconButton}
                  onClick={() => navigate(`/mandantenneuanlage/${item.id}`)}
                  aria-label="Mandant bearbeiten"
                  title="Mandant bearbeiten"
                >
                  <FiEdit size={20} />
                </button>

                <button
                  type="button"
                  style={{ ...iconButton, marginLeft: "0.5rem" }}
                  onClick={() => handleDelete(item.id)}
                  aria-label="Löschen"
                  title="Löschen"
                >
                  <FiTrash2 size={20} />
                </button>

                <button
                  type="button"
                  style={{ ...iconButton, marginLeft: "0.5rem" }}
                  onClick={() =>
                    navigate(`/mandantenbenutzer/${item.id}`, {
                      state: { mandantName: item.name1 },
                    })
                  }
                  aria-label="Benutzer bearbeiten"
                  title="Benutzer bearbeiten"
                >
                  <FaUserEdit size={20} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div style={buttonContainer}>
        <button style={buttonPrimary} onClick={() => navigate(-1)}>
          Zurück
        </button>
        <button style={buttonPrimary} onClick={() => navigate("/mandantenneuanlage")}>
          Neuanlage
        </button>
        <button style={buttonPrimary} onClick={() => navigate("/mandanten")}>
          Alle Mandanten
        </button>
      </div>
    </div>
  );
};

// Stil tanımları - CSSProperties tipiyle tiplenmiş
const page: CSSProperties = {
  fontFamily: "Arial, sans-serif",
  padding: "2rem",
  minHeight: "100vh",
  position: "relative",
};

const titleStyle: CSSProperties = {
  fontSize: "1.5rem",
  fontWeight: "bold",
  marginBottom: "1rem",
  color: PRIMARY_COLOR,
};

const buttonContainer: CSSProperties = {
  marginTop: "1rem",
  display: "flex",
  justifyContent: "flex-end",
  gap: "1rem",
};

const buttonBase: CSSProperties = {
  backgroundColor: PRIMARY_COLOR,
  color: "#fff",
  padding: "0.5rem 1.5rem",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
  fontSize: "1rem",
};

const buttonPrimary: CSSProperties = { ...buttonBase };

const tableStyle: CSSProperties = {
  width: "100%",
  borderCollapse: "collapse",
};

const thStyle: CSSProperties = {
  borderBottom: "2px solid #ccc",
  padding: "0.5rem",
  textAlign: "left",
  backgroundColor: "#f5f5f5",
  color: PRIMARY_COLOR,
};

const tdStyle: CSSProperties = {
  borderBottom: "1px solid #ddd",
  padding: "0.5rem",
};

const iconButton: CSSProperties = {
  background: "none",
  border: "none",
  color: PRIMARY_COLOR,
  cursor: "pointer",
  fontSize: "1.2rem",
  padding: 0,
};

export default MandantenListe;
