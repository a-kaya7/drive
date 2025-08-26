import { Outlet, useNavigate } from "react-router-dom";
import MainModulesNav from "../MainModulesNav/MainModulesNav";
import { useEffect, useState } from "react";
import axios from "axios";

interface Mandant {
  mandantId: string;
  idname: string;
  beschreibung: string;
  locale: string;
  telefon: string;
}

export default function PortalLayout() {
  const lang = "de";
  const navigate = useNavigate();
  const [mandants, setMandants] = useState<Mandant[]>([]);
  const [selectedMandant, setSelectedMandant] = useState<string>("Mandant");
  const [loading, setLoading] = useState(true);

  // Logout
  const handleLogout = async () => {
    try {
      await axios.post("http://localhost:8080/api/logout", {}, { withCredentials: true });
    } catch (error) {
      console.error("Logout error:", error);
    } finally {
      // Token temizle
      localStorage.removeItem("authToken");
      sessionStorage.removeItem("authToken");

      // Login sayfasına yönlendir
      navigate("/login");
    }
  };

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/mandantenlist", { withCredentials: true })
      .then((response) => {
        const data: Mandant[] = response.data;
        setMandants(data);
        if (data.length > 0) setSelectedMandant(data[0].mandantId);
      })
      .catch((error) => console.error("Mandant çekme hatası:", error))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return null;

  return (
    <div>
      <nav style={topNav}>
        <div style={left}>
          <span style={brand}>Fahrschule Portal</span>

          <select
            value={selectedMandant}
            onChange={(e) => setSelectedMandant(e.target.value)}
            style={comboBoxStyle}
          >
            <option disabled>Mandant</option>
            {mandants.map((m) => (
              <option key={m.mandantId} value={m.mandantId}>
                {m.idname}
              </option>
            ))}
          </select>
        </div>

        <div style={right}>
          <button onClick={handleLogout} style={logoutBtn}>
            Ausloggen
          </button>
        </div>
      </nav>

      <MainModulesNav lang={lang} />
      <Outlet />
    </div>
  );
}

// Stil
const topNav: React.CSSProperties = {
  background: "#174bd1ff",
  color: "white",
  padding: "0.8rem 1.2rem",
  display: "flex",
  alignItems: "center",
  justifyContent: "space-between",
};

const left: React.CSSProperties = {
  display: "flex",
  alignItems: "center",
  gap: 12,
};

const brand: React.CSSProperties = {
  fontWeight: "bold",
  fontSize: "1.4rem",
};

const right: React.CSSProperties = {
  display: "flex",
  alignItems: "center",
};

const logoutBtn: React.CSSProperties = {
  background: "transparent",
  border: "1px solid rgba(255,255,255,.6)",
  color: "#fff",
  padding: "0.25rem 0.6rem",
  borderRadius: 4,
  cursor: "pointer",
  fontSize: "1.1rem",
};

const comboBoxStyle: React.CSSProperties = {
  marginLeft: 150,
  padding: "0.4rem 0.8rem",
  borderRadius: 6,
  border: "1px solid #ccc",
  fontSize: "1rem",
  backgroundColor: "#f9f9f9",
  color: "#000",
  cursor: "pointer",
};
