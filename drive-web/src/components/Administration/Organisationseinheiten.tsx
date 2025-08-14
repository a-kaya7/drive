import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const PRIMARY_COLOR = "#174bd1ff";

const Organisationseinheiten: React.FC = () => {
  const navigate = useNavigate();

  const [openSections, setOpenSections] = useState<{
    [key: string]: boolean;
  }>({
    raeumlich: false,
    personell: false,
    kurse: false,
    hierarchie: false,
  });

  const toggleSection = (key: string) => {
    setOpenSections((prev) => ({
      ...prev,
      [key]: !prev[key],
    }));
  };

  const sections = [
    { key: "raeumlich", title: "Räumliche Organisation" },
    { key: "personell", title: "Personelle Organisation" },
    { key: "kurse", title: "Kurse & Ausbildungseinheiten" },
    { key: "hierarchie", title: "Hierarchische Struktur / Verknüpfungen" },
  ];

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={{ ...title, textAlign: "left" }}>Organisationseinheiten</h2>

        {sections.map((sec) => (
          <div key={sec.key} style={sectionBlock}>
            <div style={sectionHeader}>
              <span>{sec.title}</span>
              <button
                onClick={() => toggleSection(sec.key)}
                style={iconButton}
              >
                {openSections[sec.key] ? "▲" : "▼"}
              </button>
            </div>

            {openSections[sec.key] && (
              <div style={sectionContent}>
                {sec.key === "kurse" ? (
                  <>
                    <label style={labelStyle}>Führerschein verwalten</label>
                    <button
                      style={buttonPrimary}
                      onClick={() => navigate("/führerscheinverwalten")}
                    >
                      Öffnen
                    </button>
                  </>
                ) : (
                  <>
                    <p>
                      Hier können Sie Inhalte oder Formulare für{" "}
                      <b>{sec.title}</b> einfügen.
                    </p>
                    <button style={buttonPrimary}>Speichern</button>
                    <button
                      style={buttonSecondary}
                      onClick={() => navigate(-1)}
                    >
                      Zurück
                    </button>
                  </>
                )}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

// Styles
const page: React.CSSProperties = { fontFamily: "Arial, sans-serif", padding: "2rem" };
const container: React.CSSProperties = {
  maxWidth: 800,
  margin: "0 auto",
  padding: "2rem",
  border: "none",
  boxShadow: "none",
  background: "transparent",
};
const title: React.CSSProperties = { color: PRIMARY_COLOR, marginBottom: "1.5rem" };

const sectionBlock: React.CSSProperties = {
  borderBottom: "1px solid #ccc",
  marginBottom: "1rem",
  paddingBottom: "1rem",
};
const sectionHeader: React.CSSProperties = {
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  fontSize: "1.1rem",
  fontWeight: "bold",
};
const iconButton: React.CSSProperties = {
  background: "none",
  border: "none",
  fontSize: "1.2rem",
  cursor: "pointer",
};
const sectionContent: React.CSSProperties = {
  marginTop: "1rem",
  padding: "1rem",
  backgroundColor: "#f9f9f9",
  borderRadius: 4,
};

const labelStyle: React.CSSProperties = {
  display: "block",
  marginBottom: "0.5rem",
  fontWeight: "bold",
};

const buttonBase: React.CSSProperties = {
  backgroundColor: PRIMARY_COLOR,
  color: "#fff",
  padding: "0.5rem 1.5rem",
  border: "none",
  borderRadius: 4,
  cursor: "pointer",
  fontSize: "1rem",
  marginRight: "1rem",
};
const buttonPrimary: React.CSSProperties = { ...buttonBase };
const buttonSecondary: React.CSSProperties = {
  ...buttonBase,
  backgroundColor: "#777",
};

export default Organisationseinheiten;
