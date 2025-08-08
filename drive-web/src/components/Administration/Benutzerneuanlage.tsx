import React, { useState } from "react";
import type { FormEvent } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";


const PRIMARY_COLOR = "#174bd1ff";

const BenutzerNeuanlage: React.FC = () => {
  // Allgemeine Angaben
  const [benutzerkennung, setBenutzerkennung] = useState("");
  const [anrede, setAnrede] = useState(""); // <-- artık text input
  const [vorname, setVorname] = useState("");
  const [nachname, setNachname] = useState("");
  const [email, setEmail] = useState("");
  const [benutzerVon, setBenutzerVon] = useState("");
  const [benutzerBis, setBenutzerBis] = useState("");

  // Passwort
  const [passwort, setPasswort] = useState("");
  const [passwortWiederholung, setPasswortWiederholung] = useState("");
  const [passwortAb, setPasswortAb] = useState("");
  const [zeitraumPasswort, setZeitraumPasswort] = useState("");
  const [passwortAenderung, setPasswortAenderung] = useState(false);
  const [mfa, setMfa] = useState(false);

  // Mandant
  const [mandant, setMandant] = useState("");

  // UI
  const [error, setError] = useState("");
  const [message, setMessage] = useState<{ text: string; type: "error" | "success" | "" }>({
    text: "",
    type: "",
  });
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMessage({ text: "", type: "" });

    if (!benutzerkennung.trim()) return setError("Benutzerkennung ist erforderlich!");
    if (!vorname.trim()) return setError("Vorname ist erforderlich!");
    if (!nachname.trim()) return setError("Nachname ist erforderlich!");
    if (!mandant) return setError("Mandant ist erforderlich!");
    if (!passwort) return setError("Passwort ist erforderlich!");
    if (!passwortWiederholung) return setError("Passwort (Wiederholung) ist erforderlich!");
    if (passwort !== passwortWiederholung) return setError("Passwörter stimmen nicht überein!");

    if (benutzerVon && benutzerBis && new Date(benutzerVon) > new Date(benutzerBis)) {
      return setError("Benutzer bis darf nicht vor Benutzer von liegen.");
    }

    setError("");
    setLoading(true);

    try {
      await axios.post("/api/benutzerneuanlage", {
        benutzerkennung,
        anrede,
        vorname,
        nachname,
        email,
        benutzerVon,
        benutzerBis,
        passwort,
        passwortAb,
        zeitraumPasswort,
        passwortAenderung,
        mfa,
        mandant,
      });

      setMessage({ text: "Benutzer erfolgreich gespeichert!", type: "success" });
      setTimeout(() => navigate("/benutzer"), 1000);
    } catch (err) {
      const errorMsg =
        axios.isAxiosError(err) && err.response?.data
          ? String(err.response.data)
          : "Fehler aufgetreten";
      setMessage({
        text: errorMsg,
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={title}>Benutzer anlegen</h2>

        <form onSubmit={handleSubmit}>
          {/* Allgemeine Angaben */}
          <h3 style={sectionTitle}>Allgemeine Angaben</h3>

          <div style={field}>
            <label style={label}>
              Benutzerkennung <span style={{ color: "red" }}>*</span>
            </label>
            <input
              type="text"
              value={benutzerkennung}
              onChange={(e) => setBenutzerkennung(e.target.value)}
              style={input}
            />
          </div>

          <div style={field}>
            <label style={label}>Anrede</label>
            <input
              type="text"
              value={anrede}
              onChange={(e) => setAnrede(e.target.value)}
              style={input}
            />
          </div>

          <div style={field}>
            <label style={label}>
              Vorname <span style={{ color: "red" }}>*</span>
            </label>
            <input
              type="text"
              value={vorname}
              onChange={(e) => setVorname(e.target.value)}
              style={input}
            />
          </div>

          <div style={field}>
            <label style={label}>
              Nachname <span style={{ color: "red" }}>*</span>
            </label>
            <input
              type="text"
              value={nachname}
              onChange={(e) => setNachname(e.target.value)}
              style={input}
            />
          </div>

          <div style={field}>
            <label style={label}>E-Mail</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              style={input}
            />
          </div>

          <div style={field}>
            <label style={label}>Benutzer von</label>
            <input
              type="date"
              value={benutzerVon}
              onChange={(e) => setBenutzerVon(e.target.value)}
              style={input}
            />
          </div>

          <div style={field}>
            <label style={label}>Benutzer bis</label>
            <input
              type="date"
              value={benutzerBis}
              onChange={(e) => setBenutzerBis(e.target.value)}
              style={input}
            />
          </div>

          {/* Passwort */}
          <h3 style={sectionTitle}>Passwort</h3>

          <div style={field}>
            <label style={label}>
              Passwort <span style={{ color: "red" }}>*</span>
            </label>
            <input
              type="password"
              value={passwort}
              onChange={(e) => setPasswort(e.target.value)}
              style={input}
            />
          </div>

          <div style={field}>
            <label style={label}>
              Passwort (Wiederholung) <span style={{ color: "red" }}>*</span>
            </label>
            <input
              type="password"
              value={passwortWiederholung}
              onChange={(e) => setPasswortWiederholung(e.target.value)}
              style={input}
            />
          </div>

          <div style={field}>
            <label style={label}>Passwort ab</label>
            <input
              type="date"
              value={passwortAb}
              onChange={(e) => setPasswortAb(e.target.value)}
              style={input}
            />
          </div>

          <div style={field}>
            <label style={label}>Zeitraum für Passwort (Tage)</label>
            <input
              type="number"
              min={0}
              value={zeitraumPasswort}
              onChange={(e) => setZeitraumPasswort(e.target.value)}
              style={input}
            />
          </div>

          <div style={{ marginBottom: "1rem" }}>
            <label>
              <input
                type="checkbox"
                checked={passwortAenderung}
                onChange={(e) => setPasswortAenderung(e.target.checked)}
                style={{ marginRight: "0.5rem" }}
              />
              Passwortänderung erforderlich
            </label>
          </div>

          <div style={{ marginBottom: "2rem" }}>
            <label>
              <input
                type="checkbox"
                checked={mfa}
                onChange={(e) => setMfa(e.target.checked)}
                style={{ marginRight: "0.5rem" }}
              />
              MFA-Authentifizierung
            </label>
          </div>

          {/* Mandantenzuordnung */}
          <h3 style={sectionTitle}>Mandantenzuordnung</h3>

          <div style={field}>
            <label style={label}>
              Mandant <span style={{ color: "red" }}>*</span>
            </label>
            <select
              value={mandant}
              onChange={(e) => setMandant(e.target.value)}
              style={{ ...input, border: "none", borderBottom: "2px solid #ccc" }}
            >
              <option value="">- Bitte wählen -</option>
              {/* Dinamik doldurulacaksa burayı değiştir */}
              <option value="1">Mandant 1</option>
              <option value="2">Mandant 2</option>
            </select>
          </div>

          {/* Mesaj & Hata */}
          {error && <div style={errorStyle}>{error}</div>}

          {message.text && (
            <div
              style={{
                color: message.type === "error" ? "red" : "green",
                marginBottom: "1rem",
              }}
            >
              {message.text}
            </div>
          )}

          <div style={buttonsRow}>
            <button type="submit" style={buttonPrimary} disabled={loading}>
              {loading ? "Speichere..." : "Speichern"}
            </button>
            <button type="button" style={buttonSecondary} onClick={() => navigate(-1)}>
              Zurück
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

const page: React.CSSProperties = { fontFamily: "Arial, sans-serif", padding: "2rem" };
const container: React.CSSProperties = {
  maxWidth: 600,
  margin: "0 auto",
  padding: "2rem",
  border: "none",
  boxShadow: "none",
  background: "transparent",
};
const title: React.CSSProperties = { textAlign: "center", color: PRIMARY_COLOR, marginBottom: "1.5rem" };

const sectionTitle: React.CSSProperties = {
  color: PRIMARY_COLOR,
  margin: "1.5rem 0 0.8rem 0",
  fontSize: "1.05rem",
};

const field: React.CSSProperties = { marginBottom: "1.5rem" };
const label: React.CSSProperties = { display: "block", marginBottom: "0.3rem" };

const input: React.CSSProperties = {
  width: "100%",
  padding: "0.5rem 0",
  border: "none",
  borderBottom: "2px solid #ccc",
  fontSize: "1rem",
  outline: "none",
};

const errorStyle: React.CSSProperties = {
  color: "red",
  fontSize: "0.9rem",
  marginBottom: "0.8rem",
};

const buttonsRow: React.CSSProperties = { display: "flex", gap: "1rem", justifyContent: "flex-start" };

const buttonBase: React.CSSProperties = {
  backgroundColor: PRIMARY_COLOR,
  color: "#fff",
  padding: "0.5rem 1.5rem",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
  fontSize: "1rem",
};

const buttonPrimary: React.CSSProperties = { ...buttonBase };
const buttonSecondary: React.CSSProperties = { ...buttonBase };

export default BenutzerNeuanlage;
