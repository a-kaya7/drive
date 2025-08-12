import { Outlet, useNavigate } from "react-router-dom";
import MainModulesNav from "../MainModulesNav/MainModulesNav";

export default function PortalLayout() {
  const lang = "de";
  const navigate = useNavigate();

  const handleLogout = () => {
    
    localStorage.removeItem("authToken");
    sessionStorage.removeItem("authToken");
    navigate("/login", {replace: true});
  };

  return (
    <div>
      {/* Navbar */}
      <nav style={topNav}>
        <div style={left}>
          <span style={brand}>Fahrschule Portal</span>
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
  gap: 0.6,
};

const brand: React.CSSProperties = {
  fontWeight: "bold",
  fontSize: "1.4rem",
};

const right: React.CSSProperties = {
  display: "flex",
  alignItems: "center",
  gap: 0.6,
};

const logoutBtn: React.CSSProperties = {
  background: "transparent",
  border: "1px solid rgba(255,255,255,.6)",
  color: "#fff",
  padding: "0.25rem 0.6rem",
  borderRadius: 4,
  cursor: "pointer",
  fontSize: "0.9rem",
};
