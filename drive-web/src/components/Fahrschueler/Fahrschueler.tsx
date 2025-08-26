import React, { useEffect, useMemo, useState } from "react";
import type { ChangeEvent, FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { FaUserEdit } from "react-icons/fa";
import { FiTrash2 } from "react-icons/fi";
import { TiExport } from "react-icons/ti";
import { api } from "../ApiConfig/api";

const PRIMARY_COLOR = "#174bd1ff";

interface Fuehrerschein {
  fuehrerscheinKlasse: string;
}

interface Fahrschueler {
  fahrschuelerId: string; 
  nachname: string;
  geburtsdatum: string | null;
  telefonnummer: string;
   fuehrerscheinList: Fuehrerschein[];
  anmeldedatum: string | null;
  bezahlt: boolean;
}

const FahrschuelerListen = () => {
  const [fahrschuelerList, setFahrschuelerList] = useState<Fahrschueler[]>([]);
  const [eingabe, setEingabe] = useState("");
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState<{ text: string; type: "error" | "success" | "" }>({ text: "", type: "" });

  const navigate = useNavigate();

  useEffect(() => {
    const fetchFahrschueler = async () => {
      setLoading(true);
      try {
        const response = await api.get<any[]>("/api/fahrschuelerlist");
        const formattedData: Fahrschueler[] = response.data.map(f => ({
          fahrschuelerId: f.fahrschuelerId,
          nachname: f.nachname,
          geburtsdatum: f.geburtsdatum,
          telefonnummer: f.telefonnummer,
          bezahlt: f.bezahlt,
          fuehrerscheinList: f.fuehrerscheine.map((k: string) => ({ fuehrerscheinKlasse: k })),
        }));

        setFahrschuelerList(formattedData);
      } catch (err) {
        console.error("Fehler beim Laden der Fahrschülerliste", err);
        setMsg({ text: "Fehler beim Laden der Daten", type: "error" });
      } finally {
        setLoading(false);
      }
    };
    fetchFahrschueler();
  }, []);

  const filteredFahrschueler = useMemo(() => {
    const q = eingabe.trim().toLowerCase();
    if (!q) return fahrschuelerList;
    return fahrschuelerList.filter((u) => u.nachname.toLowerCase().includes(q));
  }, [fahrschuelerList, eingabe]);

  const handleEditFahrschueler = (fahrschuelerId: string) => {
    navigate(`/fahrschuelerbearbeiten/${fahrschuelerId}`);
  };

  const handleDeleteFahrschueler = async (fahrschuelerId: string) => {
    if (!window.confirm("Soll dieser Fahrschueler gelöscht werden?")) return;
    try {
      await api.delete(`/api/fahrschuelerloeschen/${fahrschuelerId}`);
      setFahrschuelerList((prev) => prev.filter((u) => u.fahrschuelerId !== fahrschuelerId));
      setMsg({ text: "Fahrschüler gelöscht", type: "success" });
    } catch (e) {
      alert("Löschen fehlgeschlagen!");
    }
  };

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
  };

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={{ ...title, textAlign: "left" }}>Fahrschüler</h2>

        <section style={section}>
          <form onSubmit={handleSubmit}>
            <div style={field}>
              <label style={label}>Fahrschüler suchen</label>
              <input
                type="text"
                value={eingabe}
                onChange={(e: ChangeEvent<HTMLInputElement>) => setEingabe(e.target.value)}
                style={input}
                placeholder="Suchen..."
              />
            </div>

            {msg.text && (
              <div style={{ color: msg.type === "error" ? "red" : "green", marginBottom: "1.5rem" }}>
                {msg.text}
              </div>
            )}

            <div style={buttonsRow}>
              <button type="button" style={buttonPrimary} onClick={() => navigate("/fahrschuelerneuanlage")}>
                Neuanlage
              </button>
              <button type="button" style={buttonPrimary} onClick={() => navigate(-1)}>
                Zurück
              </button>
            </div>
          </form>
        </section>

        <section style={section}>
          {loading ? (
            <div>Lädt...</div>
          ) : (
            <table style={tableStyle}>
              <thead>
                <tr>
                  <th style={thStyle}>Nachname</th>
                  <th style={thStyle}>Geburtsdatum</th>
                  <th style={thStyle}>Telefonnummer</th>
                  <th style={thStyle}>Führerscheinklasse</th>
                  <th style={thStyle}>Anmeldengebühr</th>
                  <th style={thStyle}>Aktion</th>
                </tr>
              </thead>
              <tbody>
                {filteredFahrschueler.length === 0 ? (
                  <tr>
                    <td colSpan={6} style={{ textAlign: "center", padding: "1rem" }}>
                      Keine Daten vorhanden.
                    </td>
                  </tr>
                ) : (
                  filteredFahrschueler.map((u) => (
                    <tr key={u.fahrschuelerId}>
                      <td style={tdStyle}>{u.nachname}</td>
                      <td style={tdStyle}>{u.geburtsdatum || "-"}</td>
                      <td style={tdStyle}>{u.telefonnummer || "-"}</td>
                      <td style={tdStyle}>
                        {u.fuehrerscheinList.map((f) => f.fuehrerscheinKlasse).join(", ") || "-"}
                      </td>
                      <td style={tdStyle}>{u.bezahlt ? "Ja" : "Nein"}</td>
                      <td style={tdStyle}>
                        <button
                          type="button"
                          style={iconButton}
                          onClick={() => handleEditFahrschueler(u.fahrschuelerId)}
                          aria-label="Bearbeiten"
                          title="Fahrschüler bearbeiten"
                        >
                          <FaUserEdit size={18} />
                        </button>
                        <button
                          type="button"
                          style={{ ...iconButton, marginLeft: "0.5rem" }}
                          onClick={() => handleDeleteFahrschueler(u.fahrschuelerId)}
                          aria-label="Löschen"
                          title="Löschen"
                        >
                          <FiTrash2 size={18} />
                        </button>
                        <button
                          type="button"
                          style={{ ...iconButton, marginLeft: "0.5rem" }}
                          onClick={() => alert("Exportieren noch nicht implementiert")}
                          aria-label="exportieren"
                          title="Exportieren"
                        >
                          <TiExport size={18} />
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
const page: React.CSSProperties = { fontFamily: "Arial, sans-serif", padding: "2rem" };
const container: React.CSSProperties = { maxWidth: 1000, margin: "0 auto" };
const title: React.CSSProperties = { textAlign: "center", color: PRIMARY_COLOR, marginBottom: "2rem" };
const section: React.CSSProperties = { marginBottom: "2rem" };
const field: React.CSSProperties = { marginBottom: "2rem" };
const label: React.CSSProperties = { display: "block", marginBottom: "0.6rem" };
const input: React.CSSProperties = { width: "100%", padding: "0.7rem 0", border: "none", borderBottom: "2px solid #ccc", fontSize: "1rem", outline: "none" };
const buttonsRow: React.CSSProperties = { display: "flex", gap: "1.5rem", justifyContent: "flex-end" };
const buttonBase: React.CSSProperties = { backgroundColor: PRIMARY_COLOR, color: "#fff", padding: "0.7rem 2rem", border: "none", borderRadius: "4px", cursor: "pointer", fontSize: "1rem" };
const buttonPrimary: React.CSSProperties = { ...buttonBase };
const tableStyle: React.CSSProperties = { width: "100%", borderCollapse: "collapse" };
const thStyle: React.CSSProperties = { borderBottom: "2px solid #ccc", padding: "0.75rem", textAlign: "left", backgroundColor: "#f5f5f5", color: PRIMARY_COLOR };
const tdStyle: React.CSSProperties = { borderBottom: "1px solid #ddd", padding: "0.75rem" };
const iconButton: React.CSSProperties = { background: "none", border: "none", color: PRIMARY_COLOR, cursor: "pointer", fontSize: "1rem", padding: 0 };

export default FahrschuelerListen;
