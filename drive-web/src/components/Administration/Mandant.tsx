import { useEffect, useState } from "react";
import type { CSSProperties } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { FiEdit, FiTrash2 } from "react-icons/fi";

const PRIMARY_COLOR = "#174bd1ff";

interface Mandanten {
  idname: string;
  beschreibung: string;
  locale: string;
  telefon: string;
}

const MandantenListe: React.FC = () => {
  const [mandanten, setMandanten] = useState<Mandanten[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchMandanten();
  }, []);

  const fetchMandanten = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/mandantenlist");
      setMandanten(response.data);
    } catch (err) {
      console.error("Fehler beim Laden der Mandanten:", err);
    }
  };

  const handleDelete = async (idname: String) => {
    if (!window.confirm("Soll dieser Mandant gelöscht werden?")) return;
    try {
      await axios.delete(`http://localhost:8080/api/mandant/${idname}`);
      setMandanten((prev) => prev.filter((m: any) => m.idname !== idname));
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
            <th style={thStyle}>ID Name</th>
            <th style={thStyle}>Beschreibung</th>
            <th style={thStyle}>Local</th>
            <th style={thStyle}>Telefon</th>
            <th style={thStyle}>Action</th>
          </tr>
        </thead>
        <tbody>
          {mandanten.map((item: any) => (
            <tr key={item.idname}>
              <td style={tdStyle}>{item.idname}</td>
              <td style={tdStyle}>{item.beschreibung}</td>
              <td style={tdStyle}>{item.local}</td>
              <td style={tdStyle}>{item.telefon}</td>
              <td style={tdStyle}>
                <button
                  type="button"
                  style={iconButton}
                  onClick={() => navigate(`/mandantenneuanlage/${item.idname}`)}
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

              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div style={buttonContainer}>
        <button style={buttonPrimary} onClick={() => navigate("/mandantenneuanlage")}>
          Neuanlage
        </button>
      </div>
    </div>
  );
};

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
