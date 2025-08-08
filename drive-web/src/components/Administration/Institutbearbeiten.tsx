import React, { useEffect, useState } from "react";
import type { FormEvent } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";;

const PRIMARY_COLOR = "#174bd1ff";

const Institutbearbeiten: React.FC = () => {
  const { idname: routeIdname } = useParams<{ idname: string }>();
  const navigate = useNavigate();

  const [idname, setIdname] = useState("");
  const [institutsname, setInstitutsname] = useState("");
  const [bezeichnung, setBezeichnung] = useState("");
  const [bankleitzahl, setBankleitzahl] = useState("");
  const [bic, setBic] = useState("");
  const [bwl, setBwl] = useState("");
  const [locale, setLocale] = useState("");

  const [error, setError] = useState("");
  const [message, setMessage] = useState<{ text: string; type: string }>({ text: "", type: "" });
  const [loading, setLoading] = useState(false);

  // Veriyi çek
  useEffect(() => {
    const fetchInstitut = async () => {
      try {
        const res = await axios.get(`/api/institute/${routeIdname}`);
        const data = res.data;

        setIdname(data.idname || "");
        setInstitutsname(data.institutsname || "");
        setBezeichnung(data.bezeichnung || "");
        setBankleitzahl(data.bankleitzahl || "");
        setBic(data.bic || "");
        setBwl(data.bwl || "");
        setLocale(data.locale || "");
      } catch (err) {
        setError("Fehler beim Laden des Instituts.");
      }
    };

    if (routeIdname) fetchInstitut();
  }, [routeIdname]);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setMessage({ text: "", type: "" });

    if (!idname.trim()) return setError("ID ist erforderlich!");
    if (!institutsname.trim()) return setError("Institutsname ist erforderlich!");
    if (!bezeichnung.trim()) return setError("Bezeichnung ist erforderlich!");

    setError("");
    setLoading(true);

    try {
      await axios.put(`/api/institute/${routeIdname}`, {
        idname,
        institutsname,
        bezeichnung,
        bankleitzahl,
        bic,
        bwl,
        locale,
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
        <h2 style={title}>Institut bearbeiten</h2>

        <form onSubmit={handleSubmit}>
          <h3 style={sectionTitle}>Grunddaten</h3>

          <div style={field}>
            <label style={label}>
              ID <span style={{ color: "red" }}>*</span>
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
            <label style={label}>Bankleitzahl</label>
            <input
              type="text"
              value={bankleitzahl}
              onChange={(e) => setBankleitzahl(e.target.value)}
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
            <label style={label}>BWL</label>
            <input
              type="text"
              value={bwl}
              onChange={(e) => setBwl(e.target.value)}
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

// Stil ayarları (aynı kalabilir)
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

export default Institutbearbeiten;
