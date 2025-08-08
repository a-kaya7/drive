import React from "react";
import { Link } from "react-router-dom";
import styles from "./Homepage.module.css";

interface Module {
  id: number;
  title: string;
  description: string;
  link: string;
}

const modules: Module[] = [
  { id: 1, title: "Administration", description: "Allgemein, Recht, Import/Export, Drucken, Wartungsmodus", link: "/administration" },
  { id: 2, title: "Fahrschülerverwaltung", description: "Verwaltung der Fahrschülerdaten und -profile", link: "/schueler-management" },
  { id: 3, title: "Stundenplanung und Kalender", description: "Theorie- & Praxisunterricht, Kalenderverwaltung", link: "/schedule" },
  { id: 4, title: "Reservierungssystem", description: "Unterrichtsreservierung, Fahrlehrer- & Fahrzeugausswahl", link: "/reservation" },
  { id: 5, title: "Prüfungsverfolgung", description: "Prüfungstermine, Ergebnisse und Simulationstests", link: "/exam-tracking" },
  { id: 6, title: "Zahlungssystem", description: "Kursgebühren, Online-Zahlungen und Verlauf", link: "/payments" },
  { id: 7, title: "Benachrichtigungen und Nachrichten", description: "Unterrichtserinnerungen und Fahrlehrernachrichten", link: "/notifications" },
  { id: 8, title: "Fahrzeug-& Geräteverwaltung", description: "Fahrzeugverfolgung und Wartungsstatus", link: "/vehicle-management" },
  { id: 9, title: "Berichte & Statistiken", description: "Fortschrittsberichte und Leistungskontrolle", link: "/reports" },
  { id: 10, title: "Aufgabenübersicht", description: "Die Aufgaben", link: "/aufgabenübersicht" },
  { id: 11, title: "Mehrsprachige Unterstützung", description: "Sprachoptionen: Deutsch, und Englisch", link: "/language" },
];

const HomePage: React.FC = () => {
  return (
    <div className={styles.container}>
      <main className={styles.main}>
        <div className={styles.moduleGrid}>
          {modules.map((mod) => (
            <ModuleCard key={mod.id} module={mod} />
          ))}
        </div>
      </main>
    </div>
  );
};

interface ModuleCardProps {
  module: Module;
}

const ModuleCard: React.FC<ModuleCardProps> = ({ module }) => {
  const lines = module.description.split(",");

  return (
    <Link
      to={module.link}
      className={styles.moduleCard}
      onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.05)")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
    >
      <h2 className={styles.moduleTitle}>{module.title}</h2>
      <div className={styles.moduleDescription}>
        {lines.map((line, idx) => (
          <div key={idx}>{line.trim()}</div>
        ))}
      </div>
    </Link>
  );
};

export default HomePage;
