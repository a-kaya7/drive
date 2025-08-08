import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { FiEdit, FiTrash2 } from "react-icons/fi";

const PRIMARY_COLOR = "#174bd1ff";

interface Institute {
  bezeichnung: string;
  institutsname: string;
  id: number;
  idname: string;
}

const InstitutListe: React.FC = () => {
  const [institute, setInstitute] = useState<Institute[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchInstitute();
  }, []);

  const fetchInstitute = async () => {
    try {
      const response = await axios.get<Institute[]>("/api/institutanzeigen");
      setInstitute(response.data);
    } catch (err) {
      console.error("Fehler beim Laden der Institute:", err);
    }
  };

  const handleDelete = async (idname: string) => {
    if (!window.confirm("Soll dieses Institut gelöscht werden?")) return;
    try {
      await axios.delete(`/api/institute/${idname}`);
      setInstitute((prev) => prev.filter((inst) => inst.idname !== idname));
    } catch (e: any) {
      const errorMessage =
        e.response?.data?.error || e.response?.data?.message || "Löschen fehlgeschlagen!";
      alert(errorMessage);
    }
  };

  return (
    <div style={page}>
      {/* Sayfa Başlığı */}
      <h2 style={titleStyle}>Institute</h2>

      <table style={tableStyle}>
        <thead>
          <tr>
            <th style={thStyle}>ID</th>
            <th style={thStyle}>Bezeichnung</th>
            <th style={thStyle}>InstitutName</th>
            <th style={thStyle}>Aktion</th>
          </tr>
        </thead>
        <tbody>
          {institute.map((item) => (
            <tr key={item.idname}>
              <td style={tdStyle}>{item.idname}</td>
              <td style={tdStyle}>{item.bezeichnung}</td>
              <td style={tdStyle}>{item.institutsname}</td>
              <td style={tdStyle}>
                {/* Bearbeiten */}
                <button
                  type="button"
                  style={iconButton}
                  onClick={() => navigate(`/institutbearbeiten/${item.idname}`)}
                  aria-label="Institut bearbeiten"
                  title="Institut bearbeiten"
                >
                  <FiEdit size={20} />
                </button>

                {/* Löschen */}
                <button
                  type="button"
                  style={{ ...iconButton, marginLeft: "0.5rem" }}
                  onClick={() => handleDelete(item.idname)}
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

      {/* Butonu tablonun altına sağa hizala */}
      <div style={buttonContainer}>
        <button style={buttonPrimary} onClick={() => navigate("/institutneuanlage")}>
          Neuanlage
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
  color: PRIMARY_COLOR,
  fontSize: "1.4rem",
  fontWeight: "bold",
  marginBottom: "1rem",
};

const buttonContainer: React.CSSProperties = {
  marginTop: "1rem",
  display: "flex",
  justifyContent: "flex-end",
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

export default InstitutListe;
