import React, { useEffect, useState } from "react";
import type { FormEvent } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

const PRIMARY_COLOR = "#174bd1ff";

const Mandantbearbeiten: React.FC = () => {
  const { idname: routeIdname } = useParams<{ idname: string }>();
  const navigate = useNavigate();

  const [idname, setIdname] = useState("");
  const [beschreibung, setBeschreibung] = useState("");
  const [institutsname, setInstitutsname] = useState("");
  const [locale, setLocale] = useState("");
  const [telefon, setTelefon] = useState("");
  const [strasse, setStrasse] = useState("");
  const [plz, setPlz] = useState("");
  const [ort, setOrt] = useState("");
  const [land, setLand] = useState("");
  const [email, setEmail] = useState("");

  const [error, setError] = useState("");
  const [message, setMessage] = useState({ text: "", type: "" });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchInstitut = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/api/mandant/${routeIdname}`);
        const data = res.data;

        setIdname(data.idname || "");
        setBeschreibung(data.beschreibung || "");
        setInstitutsname(data.institut?.institutsname || "");
        setLocale(data.locale || "");
        setTelefon(data.telefon || "");
        setStrasse(data.adresse?.strasse  ??  "");
        setPlz(data.adresse?.plz ?? "");
        setOrt(data.adresse?.ort ?? "");
        setLand(data.adresse?.land ?? "");
        setEmail(data.email || "");
      } catch (err) {
        setError("Fehler beim Laden des Instituts.");
      }
    };

    if (routeIdname) fetchInstitut();
  }, [routeIdname]);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setMessage({ text: "", type: "" });

    if (!idname.trim()) return setError("Institutsname ist erforderlich!");

    setError("");
    setLoading(true);

    try {
      await axios.put(`http://localhost:8080/api/institutbearbeiten/${routeIdname}`, {
        idname,
        beschreibung,
        institutsname,
        locale,
        telefon,
        adresse: {
         strasse,
         plz,
         ort,
         land,
      },
        email,
      });

      setMessage({ text: "Institut erfolgreich aktualisiert!", type: "success" });
      setTimeout(() => navigate("/institute"), 1000);
    } catch (err: any) {
      setMessage({
        text: err.response?.data?.message || "Fehler beim Speichern",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={{ ...title, textAlign: "left"}}>Mandant bearbeiten</h2>

        <form onSubmit={handleSubmit}>
          <div style={twoColumn}>
            {/* Grunddaten */}
            <div style={column}>
              <h3 style={sectionTitle}>Grunddaten</h3>

              <div style={field}>
                <label style={label}>
                  ID Name <span style={{ color: "red" }}>*</span>
                </label>
                <input
                  type="text"
                  value={idname}
                  onChange={(e) => setIdname(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>
                  Beschreibung
                </label>
                <input
                  type="text"
                  value={beschreibung}
                  onChange={(e) => setBeschreibung(e.target.value)}
                  style={input}
                />
              </div>

              <div style={field}>
                <label style={label}>Institut</label>
                <input
                  type="text"
                  value={institutsname}
                  readOnly
                  style={{ ...input, backgroundColor: "#f2f2f2", color: "#666" }}
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
              <div style={field}>
                <label style={label}>Telefon</label>
                <input
                  type="text"
                  value={telefon}
                  onChange={(e) => setTelefon(e.target.value)}
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



const page: React.CSSProperties = {
  fontFamily: "Arial, sans-serif",
  padding: "2rem",
};

const container: React.CSSProperties = {
  maxWidth: 1000,
  margin: "0 auto",
  padding: "2rem",
  border: "none",
  boxShadow: "none",
  background: "transparent",
};

const title: React.CSSProperties = {
  textAlign: "center",
  color: PRIMARY_COLOR,
  marginBottom: "1.5rem",
};

const sectionTitle: React.CSSProperties = {
  color: PRIMARY_COLOR,
  margin: "1.5rem 0 0.8rem 0",
  fontSize: "1.05rem",
};

const twoColumn: React.CSSProperties = {
  display: "flex",
  gap: "2rem",
  alignItems: "flex-start",
  flexWrap: "wrap",
};

const column: React.CSSProperties = {
  flex: 1,
  minWidth: "280px",
};

const field: React.CSSProperties = {
  marginBottom: "1.5rem",
};

const label: React.CSSProperties = {
  display: "block",
  marginBottom: "0.3rem",
};

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

const buttonsRow: React.CSSProperties = {
  display: "flex",
  gap: "1rem",
  justifyContent: "flex-end",
};

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

export default Mandantbearbeiten;
