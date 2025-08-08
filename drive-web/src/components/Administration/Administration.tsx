import React from "react";
import { Link } from "react-router-dom";

interface Module {
  id: number;
  title: string;
  description: string;
  link: string;
}

const modules: Module[] = [
  {
    id: 1,
    title: "Allgemein",
    description:
      "Institut, Mandanten/Gruppe, Mandant, Benutzer/Rollen, Organisationseinheiten, Systemfunktion, Protokolierung, Scorecard, System ",
    link: "/administrationallgemein",
  },
  { id: 2, title: "Recht", description: "Benutzergruppe verwalten, test", link: "/benutzergruppe" },
  { id: 3, title: "Import/Export", description: "Admin-Bereich", link: "/schueler-management" },
  { id: 4, title: "Drucken", description: "Admin-Bereich", link: "/schueler-management" },
  { id: 5, title: "Wartungsmodus", description: "Admin-Bereich", link: "/schueler-management" },
];

const containerStyle: React.CSSProperties = {
  fontFamily: "Arial, sans-serif",
};

const mainStyle: React.CSSProperties = {
  padding: 0,    // Burada padding sıfırlandı
  margin: 0,     // Margin sıfırlandı
};

const gridStyle: React.CSSProperties = {
  marginTop: "2rem",
  display: "grid",
  gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
  gap: "1.5rem",
};

const linkStyle: React.CSSProperties = {
  display: "block",
  padding: "1.5rem",
  borderRadius: "10px",
  border: "1px solid #ccc",
  textDecoration: "none",
  color: "#004080",
  boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
  transition: "transform 0.2s",
};

const titleStyle: React.CSSProperties = {
  marginBottom: "0.5rem",
};

const descriptionStyle: React.CSSProperties = {
  fontSize: "0.9rem",
  color: "#555",
};

function Administration() {
  return (
    <div style={containerStyle}>
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
  const lines = module.description.split(",");

  const handleMouseEnter = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.currentTarget.style.transform = "scale(1.05)";
  };
  const handleMouseLeave = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.currentTarget.style.transform = "scale(1)";
  };

  return (
    <Link
      to={module.link}
      style={linkStyle}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      <h2 style={titleStyle}>{module.title}</h2>
      <div style={descriptionStyle}>
        {lines.map((line, index) => (
          <div key={index}>{line.trim()}</div>
        ))}
      </div>
    </Link>
  );
}

export default Administration;
