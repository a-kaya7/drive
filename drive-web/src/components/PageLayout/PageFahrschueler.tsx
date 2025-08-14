import { NavLink, Outlet, useLocation } from "react-router-dom";

const PRIMARY_COLOR = "#174bd1ff";

const actions = [
  { name: "Fahrschülerprofile", path: "/fahrschulerr" },
  { name: "Unterrichtsverwaltung", path: "/mandanten" },
  { name: "Prüfungs und-Testverwaltung", path: "/benutzergruppe" },
  { name: "Zahlungsverwaltung", path: "/organisation" },
  { name: "Benachrichtigung", path: "/systemfunktion" },
];

export default function PageLayout() {
  const location = useLocation();

  return (
    <div style={{ display: "flex", height: "calc(100vh - 96px)" }}>
      <aside
        style={{
          width: 250,
          backgroundColor: "#f4f4f4",
          padding: 20,
          borderRight: "1px solid #ddd",
        }}
      >
        <h3 style={{ color: PRIMARY_COLOR }}>Fahrschüler-Verwaltung</h3>
        <ul style={{ listStyle: "none", padding: 0 }}>
          {actions.map(({ name, path }) => {
            const isActive =
              location.pathname === path || location.pathname.startsWith(path + "/");

            return (
              <li key={name} style={{ margin: "10px 0" }}>
                <NavLink
                  to={path}
                  style={{
                    textDecoration: "none",
                    color: isActive ? PRIMARY_COLOR : "#333",
                    fontWeight: isActive ? "bold" : "normal",
                    transition: "color 0.2s",
                  }}
                >
                  {name}
                </NavLink>
              </li>
            );
          })}
        </ul>
      </aside>

      <main style={{ flex: 1, padding: 20, overflowY: "auto" }}>
        <Outlet />
      </main>
    </div>
  );
}
