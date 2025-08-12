import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate} from "react-router-dom";
import { FiEdit, FiTrash2 } from "react-icons/fi";
import { FaUserEdit } from "react-icons/fa";

const PRIMARY_COLOR = "#174bd1ff";

interface Benutzergruppe {
  benutzergruppe: string;
  beschreibung: string;
  freigabe: boolean;
  mandant?: string;
}

const BenutzergruppenListe: React.FC = () => {
  const [benutzergruppen, setBenutzergruppen] = useState<Benutzergruppe[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchBenutzergruppen();
  }, []);

  const fetchBenutzergruppen = async () => {
    try {
      const response = await axios.get<Benutzergruppe[]>("http://localhost:8080/api/benutzergruppen");
      setBenutzergruppen(response.data);
    } catch (err) {
      console.error("Fehler beim Laden der Benutzergruppen:", err);
    }
  };

  const handleDelete = async (benutzergruppe: string) => {
    if (!window.confirm("Soll diese Benutzergruppe gelöscht werden?")) return;
    try {
      await axios.delete(`http://localhost:8080/api/benutzergruppe/${benutzergruppe}`);
      setBenutzergruppen((prev) => prev.filter((g) => g.benutzergruppe !== benutzergruppe));
    } catch (e) {
      alert("Löschen fehlgeschlagen!");
    }
  };

  return (
    <div style={page}>
      <h2 style={titleStyle}>Benutzergruppen</h2>
      <table style={tableStyle}>
        <thead>
          <tr>
            <th style={thStyle}>Benutzergruppe</th>
            <th style={thStyle}>Beschreibung</th>
            <th style={thStyle}>Freigaberecht</th>
            <th style={thStyle}>Mandant</th>
            <th style={thStyle}>Aktion</th>
          </tr>
        </thead>
        <tbody>
          {benutzergruppen.map((item) => (
            <tr key={item.benutzergruppe}>
              <td style={tdStyle}>{item.benutzergruppe}</td>
              <td style={tdStyle}>{item.beschreibung}</td>
              <td style={tdStyle}>{item.freigabe ? "Ja" : "Nein"}</td>
              <td style={tdStyle}>{item.mandant}</td>
              <td style={tdStyle}>
                <button
                  type="button"
                  style={iconButton}
                  onClick={() => navigate(`/benutzergruppebearbeiten/${item.benutzergruppe}`)}
                  aria-label="Benutzergruppe bearbeiten"
                  title="Benutzergruppe bearbeiten"
                >
                  <FiEdit size={20} />
                </button>

                <button
                  type="button"
                  style={{ ...iconButton, marginLeft: "0.5rem" }}
                  onClick={() => handleDelete(item.benutzergruppe)}
                  aria-label="Löschen"
                  title="Löschen"
                >
                  <FiTrash2 size={20} />
                </button>

                <button
                  type="button"
                  style={{ ...iconButton, marginLeft: "0.5rem" }}
                  onClick={() =>
                    navigate(`/benutzerbearbeiten/${item.benutzergruppe}`, {
                      state: { gruppeName: item.benutzergruppe },
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
        <button style={buttonPrimary} onClick={() => navigate("/benutzergruppeneuanlage")}>
          Neuanlage
        </button>
        <button style={buttonPrimary} onClick={() => navigate("/benutzer")}>
          Alle Benutzer
        </button>
      </div>
    </div>
  );
};

const page: React.CSSProperties = {
  fontFamily: "Arial, sans-serif",
  padding: "2rem",
  minHeight: "100vh",
  position: "relative",
};

const titleStyle: React.CSSProperties = {
  fontSize: "1.5rem",
  fontWeight: "bold",
  marginBottom: "1rem",
  color: PRIMARY_COLOR,
};

const buttonContainer: React.CSSProperties = {
  marginTop: "1rem",
  display: "flex",
  justifyContent: "flex-end",
  gap: "1rem",
};

const buttonBase: React.CSSProperties = {
  backgroundColor: PRIMARY_COLOR,
  color: "#fff",
  padding: "0.5rem 1.5rem",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
  fontSize: "1rem",
};

const buttonPrimary: React.CSSProperties = { ...buttonBase };

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
  fontWeight: 600,
};

const tdStyle: React.CSSProperties = {
  borderBottom: "1px solid #ddd",
  padding: "0.5rem",
};

const iconButton: React.CSSProperties = {
  background: "none",
  border: "none",
  color: PRIMARY_COLOR,
  cursor: "pointer",
  fontSize: "1.2rem",
  padding: 0,
};

export default BenutzergruppenListe;
