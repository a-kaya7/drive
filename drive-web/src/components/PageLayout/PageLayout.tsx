import { NavLink, Outlet, useLocation } from "react-router-dom";

const PRIMARY_COLOR = "#174bd1ff";

type Action = {
  name: string;
  path: string;
};

const Layout = () => {
  const location = useLocation();

  const actions: Action[] = [
    { name: "Institut", path: "/institute" },
    { name: "Mandanten", path: "/mandanten" },
    { name: "Benutzer/Rollen", path: "/benutzergruppe" },
    { name: "Organisationseinheiten", path: "/organisation" },
    { name: "Systemfunktion", path: "/systemfunktion" },
    { name: "Protokolierung", path: "/protokolierung" },
    { name: "Scorecard", path: "/scorecard" },
    { name: "System", path: "/system" },
  ];

  return (
    <div style={{ display: "flex", height: "100vh" }}>
      <div
        style={{
          width: 250,
          backgroundColor: "#f4f4f4",
          padding: 20,
          borderRight: "1px solid #ddd",
        }}
      >
        <h3 style={{ color: PRIMARY_COLOR }}>Administration</h3>
        <ul style={{ listStyle: "none", padding: 0 }}>
          {actions.map((action) => {
            const isActive =
              location.pathname === action.path ||
              location.pathname.startsWith(action.path + "/");

            return (
              <li key={action.name} style={{ margin: "10px 0" }}>
                <NavLink
                  to={action.path}
                  style={{
                    textDecoration: "none",
                    color: isActive ? PRIMARY_COLOR : "#333",
                    fontWeight: isActive ? "bold" : "normal",
                    transition: "color 0.2s",
                  }}
                >
                  {action.name}
                </NavLink>
              </li>
            );
          })}
        </ul>
      </div>

      <div style={{ flex: 1, padding: 0, margin: 0 }}>
        <Outlet />
      </div>
    </div>
  );
};

export default Layout;
