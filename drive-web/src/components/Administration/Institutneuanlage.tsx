import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const PRIMARY_COLOR = "#174bd1ff";

const InstitutNeuanlage = () => {
  const [institutsname, setInstitutsname] = useState("");
  const [bezeichnung, setBezeichnung] = useState("");
  const [iban, setIban] = useState("");
  const [bic, setBic] = useState("");
  const [waehrung, setWaehrung] = useState("");
  const [locale, setLocale] = useState("");
  const [strasse, setStrasse] = useState("");
  const [plz, setPlz] = useState("");
  const [ort, setOrt] = useState("");
  const [land, setLand] = useState("");
  const [telefon, setTelefon] = useState("");
  const [email, setEmail] = useState("");

  // UI state
  const [error, setError] = useState("");
  const [message, setMessage] = useState({ text: "", type: "" });
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMessage({ text: "", type: "" });

    if (!institutsname.trim()) return setError("Institutsname ist erforderlich!");
    if (!bezeichnung.trim()) return setError("Bezeichnung ist erforderlich!");

    setError("");
    setLoading(true);

    try {
      await axios.post("http://localhost:8080/api/institutneuanlage", {
        institutsname,
        bezeichnung,
        iban,
        bic,
        waehrung,
        locale,
        adresse: {
          strasse,
          plz,
          ort,
          land,
        },
        telefon,
        email,
      });

      setMessage({ text: "Institut erfolgreich gespeichert!", type: "success" });
      setTimeout(() => navigate("/institute"), 1000);
    } catch (err: any) {
      setMessage({
        text: err.response?.data?.message || "Fehler aufgetreten",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={{ ...title, textAlign: "left"}}>Neuanlage eines Instituts</h2>

        <form onSubmit={handleSubmit}>
          <div style={twoColumn}>
            {/* Grunddaten */}
            <div style={column}>
              <h3 style={sectionTitle}>Grunddaten</h3>

              <div style={field}>
                <label style={label}>
                  Institutsname <span style={{ color: "red" }}>*</span>
                </label>
                <input
                  type="text"
                  value={institutsname}
                  onChange={(e) => setInstitutsname(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>
                  Bezeichnung <span style={{ color: "red" }}>*</span>
                </label>
                <input
                  type="text"
                  value={bezeichnung}
                  onChange={(e) => setBezeichnung(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>IBAN</label>
                <input
                  type="text"
                  value={iban}
                  onChange={(e) => setIban(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>BIC</label>
                <input
                  type="text"
                  value={bic}
                  onChange={(e) => setBic(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>Währung</label>
                <input
                  type="text"
                  value={waehrung}
                  onChange={(e) => setWaehrung(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>Locale</label>
                <input
                  type="text"
                  value={locale}
                  onChange={(e) => setLocale(e.target.value)}
                  style={input}
                />
              </div>
            </div>

            {/* Adresse-Daten */}
            <div style={column}>
              <h3 style={sectionTitle}>Adresse-Daten</h3>

              <div style={field}>
                <label style={label}>Straße</label>
                <input
                  type="text"
                  value={strasse}
                  onChange={(e) => setStrasse(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>PLZ</label>
                <input
                  type="text"
                  value={plz}
                  onChange={(e) => setPlz(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>Ort</label>
                <input
                  type="text"
                  value={ort}
                  onChange={(e) => setOrt(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>Land</label>
                <input
                  type="text"
                  value={land}
                  onChange={(e) => setLand(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>Telefon</label>
                <input
                  type="text"
                  value={telefon}
                  onChange={(e) => setTelefon(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>Email</label>
                <input
                  type="text"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  style={input}
                />
              </div>
            </div>
          </div>

          {/* Error */}
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
            <button
              type="button"
              style={buttonSecondary}
              onClick={() => navigate(-1)}
            >
              Zurück
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

const page = {
  fontFamily: "Arial, sans-serif",
  padding: "2rem",
} as React.CSSProperties;

const container = {
  maxWidth: 1000,
  margin: "0 auto",
  padding: "2rem",
  border: "none",
  boxShadow: "none",
  background: "transparent",
} as React.CSSProperties;

const title = {
  textAlign: "center",
  color: PRIMARY_COLOR,
  marginBottom: "1.5rem",
} as React.CSSProperties;

const sectionTitle = {
  color: PRIMARY_COLOR,
  margin: "1.5rem 0 0.8rem 0",
  fontSize: "1.05rem",
} as React.CSSProperties;

const twoColumn = {
  display: "flex",
  gap: "2rem",
  alignItems: "flex-start",
  flexWrap: "wrap",
} as React.CSSProperties;

const column = {
  flex: 1,
  minWidth: "280px",
} as React.CSSProperties;

const field = {
  marginBottom: "1.5rem",
} as React.CSSProperties;

const label = {
  display: "block",
  marginBottom: "0.3rem",
} as React.CSSProperties;

const input = {
  width: "100%",
  padding: "0.5rem 0",
  border: "none",
  borderBottom: "2px solid #ccc",
  fontSize: "1rem",
  outline: "none",
} as React.CSSProperties;

const errorStyle = {
  color: "red",
  fontSize: "0.9rem",
  marginBottom: "0.8rem",
} as React.CSSProperties;

const buttonsRow = {
  display: "flex",
  gap: "1rem",
  justifyContent: "flex-end",
} as React.CSSProperties;

const buttonBase = {
  backgroundColor: PRIMARY_COLOR,
  color: "#fff",
  padding: "0.5rem 1.5rem",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
  fontSize: "1rem",
} as React.CSSProperties;

const buttonPrimary = { ...buttonBase };
const buttonSecondary = { ...buttonBase };

export default InstitutNeuanlage;
