import React, { useEffect, useMemo, useState } from "react";
import type { FormEvent, ChangeEvent } from "react";
import axios from "axios";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { FaUserEdit } from "react-icons/fa";  // <-- buraya ekledim
import { FiTrash2 } from "react-icons/fi";

const PRIMARY_COLOR = "#174bd1ff";

interface User {
  id: number;
  benutzerkennung: string;
  vorname?: string;
  nachname?: string;
  benutzerBis?: string;
  mandant?: string;
  benutzergruppe?: string;
}

interface Msg {
  text: string;
  type: "error" | "success" | "";
}

const BenutzerBearbeiten: React.FC = () => {
  const { benutzergruppe } = useParams<{ benutzergruppe: string }>();
  const location = useLocation();
  const navigate = useNavigate();

  const initialGruppeName = location.state?.gruppeName ?? "";

  const [gruppeName] = useState(initialGruppeName);
  const [eingabe, setEingabe] = useState<string>("");
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [msg, setMsg] = useState<Msg>({ text: "", type: "" });

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const { data } = await axios.get<User[]>(
          "http://localhost:8080/api/benutzerlist"
        );
        setUsers(data);
      } catch (e) {
        setMsg({ text: "Fehler beim Laden der Benutzerdaten.", type: "error" });
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [benutzergruppe]);

  const filteredUsers = useMemo(() => {
    const q = eingabe.trim().toLowerCase();
    if (!q) return users;
    return users.filter(
      (u) =>
        u.benutzerkennung.toLowerCase().includes(q) ||
        `${u.vorname ?? ""} ${u.nachname ?? ""}`
          .toLowerCase()
          .includes(q) ||
        (u.mandant ?? "").toLowerCase().includes(q)
    );
  }, [users, eingabe]);

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMsg({ text: "", type: "" });

    if (!eingabe.trim()) {
      setMsg({ text: "Eingabe ist erforderlich!", type: "error" });
      return;
    }

    setMsg({ text: "Neuanlage erfolgreich ausgeführt (Dummy).", type: "success" });
  };

  const handleEditUser = (benutzerkennung: string) => {
    navigate(`/benutzerbearbeiten2/${benutzerkennung}`);
  };

  const handleDeleteUser = async (userId: number) => {
    if (!window.confirm("Soll dieser Benutzer gelöscht werden?")) return;
    try {
      await axios.delete(`/api/benutzer/${userId}`);
      setUsers((prev) => prev.filter((u) => u.id !== userId));
    } catch (e) {
      alert("Löschen fehlgeschlagen!");
    }
  };

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={{ ...title, textAlign: "left" }}>Benutzer bearbeiten</h2>

        <section style={section}>
          <div style={{ marginBottom: "1.2rem" }}>
            <strong>Benutzergruppe:</strong>{" "}
            <span style={{ color: PRIMARY_COLOR }}>{gruppeName || "(unbekannt)"}</span>
          </div>
        </section>

        <section style={section}>
          <form onSubmit={handleSubmit}>
            <div style={field}>
              <label style={label}>Benutzer suchen</label>
              <input
                type="text"
                value={eingabe}
                onChange={(e: ChangeEvent<HTMLInputElement>) =>
                  setEingabe(e.target.value)
                }
                style={input}
                placeholder="Suchen..."
              />
            </div>

            {msg.text && (
              <div
                style={{
                  color: msg.type === "error" ? "red" : "green",
                  marginBottom: "1.5rem",
                }}
              >
                {msg.text}
              </div>
            )}

            <div style={buttonsRow}>
              <button
                type="button"
                style={buttonPrimary}
                onClick={() => navigate(`/benutzerneuanlage/${benutzergruppe}`)}
              >
                Neuanlage
              </button>
              <button type="button" style={buttonPrimary} onClick={() => navigate(-1)}>
                Zurück
              </button>
            </div>
          </form>
        </section>

        <section style={section}>
          <h3 style={sectionHeader}>Benutzer der Gruppe</h3>

          {loading ? (
            <div>Lädt...</div>
          ) : (
            <table style={tableStyle}>
              <thead>
                <tr>
                  <th style={thStyle}>Benutzerkennung</th>
                  <th style={thStyle}>Name</th>
                  <th style={thStyle}>Benutzer bis</th>
                  <th style={thStyle}>Mandant</th>
                  <th style={thStyle}>Benutzergruppe</th>
                  <th style={thStyle}>Aktion</th>
                </tr>
              </thead>
              <tbody>
                {filteredUsers.length === 0 ? (
                  <tr>
                    <td colSpan={6} style={{ textAlign: "center", padding: "1rem" }}>
                      Keine Daten vorhanden.
                    </td>
                  </tr>
                ) : (
                  filteredUsers.map((u) => (
                    <tr key={u.id}>
                      <td style={tdStyle}>{u.benutzerkennung}</td>
                      <td style={tdStyle}>
                        {(u.vorname || "") + " " + (u.nachname || "")}
                      </td>
                      <td style={tdStyle}>{u.benutzerBis || "-"}</td>
                      <td style={tdStyle}>{u.mandant || "-"}</td>
                      <td style={tdStyle}>{u.benutzergruppe || "-"}</td>
                      <td style={tdStyle}>
                        <button
                          type="button"
                          style={iconButton}
                          onClick={() => handleEditUser(u.benutzerkennung)}
                          aria-label="Bearbeiten"
                          title="Benutzer bearbeiten"
                        >
                          <FaUserEdit size={18} />
                        </button>
                        <button
                          type="button"
                          style={{ ...iconButton, marginLeft: "0.5rem" }}
                          onClick={() => handleDeleteUser(u.id)}
                          aria-label="Löschen"
                          title="Löschen"
                        >
                          <FiTrash2 size={18} />
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          )}
        </section>
      </div>
    </div>
  );
};

/* ---------------- styles ---------------- */
const page: React.CSSProperties = { fontFamily: "Arial, sans-serif", padding: "2rem" };

const container: React.CSSProperties = {
  maxWidth: 1000,
  margin: "0 auto",
};

const title: React.CSSProperties = { textAlign: "center", color: PRIMARY_COLOR, marginBottom: "2rem" };

const section: React.CSSProperties = { marginBottom: "2rem" };

const sectionHeader: React.CSSProperties = { color: PRIMARY_COLOR, marginBottom: "1.5rem" };

const field: React.CSSProperties = { marginBottom: "2rem" };

const label: React.CSSProperties = { display: "block", marginBottom: "0.6rem" };

const input: React.CSSProperties = {
  width: "100%",
  padding: "0.7rem 0",
  border: "none",
  borderBottom: "2px solid #ccc",
  fontSize: "1rem",
  outline: "none",
};

const buttonsRow: React.CSSProperties = {
  display: "flex",
  gap: "1.5rem",
  justifyContent: "flex-end",
};

const buttonBase: React.CSSProperties = {
  backgroundColor: PRIMARY_COLOR,
  color: "#fff",
  padding: "0.7rem 2rem",
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
  padding: "0.75rem",
  textAlign: "left",
  backgroundColor: "#f5f5f5",
  color: PRIMARY_COLOR,
};

const tdStyle: React.CSSProperties = {
  borderBottom: "1px solid #ddd",
  padding: "0.75rem",
};

const iconButton: React.CSSProperties = {
  background: "none",
  border: "none",
  color: PRIMARY_COLOR,
  cursor: "pointer",
  fontSize: "1rem",
  padding: 0,
};

export default BenutzerBearbeiten;
