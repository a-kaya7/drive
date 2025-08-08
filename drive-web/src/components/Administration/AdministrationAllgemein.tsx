
import { Link } from "react-router-dom";

interface Module {
  id: number;
  title: string;
  description: string;
  link: string;
}

const modules: Module[] = [
  { id: 1, title: "Institut", description: "Institute verwalten", link: "/institute" },
  { id: 2, title: "Mandanten", description: "Admin-Bereich", link: "/mandanten" },
  { id: 3, title: "Benutzer/Rollen", description: "Admin-Bereich", link: "/benutzergruppe" },
  { id: 4, title: "Organisationseinheiten", description: "Admin-Bereich", link: "/schueler-management" },
  { id: 5, title: "Systemfunktion", description: "Admin-Bereich", link: "/schueler-management" },
  { id: 6, title: "Protokolierung", description: "Admin-Bereich", link: "/schueler-management" },
  { id: 7, title: "Scorecard", description: "Admin-Bereich", link: "/schueler-management" },
  { id: 8, title: "System", description: "Admin-Bereich", link: "/schueler-management" },
];

function Administration() {
  return (
    <div style={{ fontFamily: "Arial, sans-serif" }}>
      {/* inhalt */}
      <main style={{ padding: "2rem" }}>
        <div
          style={{
            marginTop: "2rem",
            display: "grid",
            gridTemplateColumns: "repeat(auto-fit,minmax(250px,1fr))",
            gap: "1.5rem",
          }}
        >
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
  return (
    <Link
      to={module.link}
      style={{
        display: "block",
        padding: "1.5rem",
        borderRadius: "10px",
        border: "1px solid #ccc",
        textDecoration: "none",
        color: "#004080",
        boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
        transition: "transform 0.2s",
      }}
      onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.05)")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
    >
      <h2 style={{ marginBottom: "0.5rem" }}>{module.title}</h2>
      <p style={{ fontSize: "0.9rem", color: "#555" }}>{module.description}</p>
    </Link>
  );
}

export default Administration;
