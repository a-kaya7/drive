import React from "react";
import { Link } from "react-router-dom";

/*  */
interface Module {
  id: number;
  title: string;
  description: string;
  link: string;
}

/* Modül liste */
const modules: Module[] = [
  { id: 1, title: "Fahrschülerprofile", description: "Fahrschüler verwalten", link: "/fahrschueler" },
  { id: 2, title: "Unterrichtsverwaltung", description: "Admin-Bereich", link: "/mandanten" },
  { id: 3, title: "Prüfungs- und Testverwaltung", description: "Admin-Bereich", link: "/benutzergruppe" },
  { id: 4, title: "Zahlungsverwaltung", description: "Admin-Bereich", link: "/schueler-management" },
  { id: 5, title: "Benachrichtigung", description: "Admin-Bereich", link: "/schueler-management" },
];

/*  */
const pageStyle: React.CSSProperties = {
  margin: 0,
  fontFamily: '"Segoe UI", Tahoma, Geneva, Verdana, sans-serif',
  backgroundColor: "#f3f4f6",
  color: "#333",
  minHeight: "100vh",
};

const mainStyle: React.CSSProperties = {
  maxWidth: "1200px",
  margin: "2rem auto",
  padding: "0 1rem",
};

const gridStyle: React.CSSProperties = {
  display: "grid",
  gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
  gap: "1.5rem",
  marginTop: "2rem",
};

const cardStyle: React.CSSProperties = {
  backgroundColor: "white",
  borderRadius: "12px",
  padding: "1.5rem",
  boxShadow: "0 4px 12px rgba(0, 0, 0, 0.07)",
  textDecoration: "none",
  color: "#174bd1ff",
  transition: "transform 0.2s ease-in-out, boxShadow 0.2s ease-in-out",
  display: "flex",
  flexDirection: "column",
  justifyContent: "center",
};

const titleStyle: React.CSSProperties = {
  marginTop: 0,
  marginBottom: "0.7rem",
  fontSize: "1.3rem",
  fontWeight: 700,
};

const descriptionStyle: React.CSSProperties = {
  fontSize: "0.95rem",
  color: "#4b5563",
  margin: 0,
  flexGrow: 1,
};

function Fahrschueler() {
  return (
    <div style={pageStyle}>
      <main style={mainStyle}>

        <div style={gridStyle}>
          {modules.map((mod) => (
            <ModuleCard key={mod.id} module={mod} />
          ))}
        </div>
      </main>
    </div>
  );
}

interface ModuleCardProps {
  module: Module;
}

function ModuleCard({ module }: ModuleCardProps) {
  const handleMouseEnter = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.currentTarget.style.transform = "translateY(-6px)";
    e.currentTarget.style.boxShadow = "0 8px 20px rgba(45, 106, 237, 0.3)";
  };
  const handleMouseLeave = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.currentTarget.style.transform = "none";
    e.currentTarget.style.boxShadow = "0 4px 12px rgba(0, 0, 0, 0.07)";
  };

  return (
    <Link
      to={module.link}
      style={cardStyle}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      <h2 style={titleStyle}>{module.title}</h2>
      <p style={descriptionStyle}>{module.description}</p>
    </Link>
  );
}

export default Fahrschueler;
