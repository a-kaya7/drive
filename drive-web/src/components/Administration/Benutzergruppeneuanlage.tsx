import React, { useState } from "react";
import type { FormEvent } from "react";
import axios, { AxiosError } from "axios";
import { useNavigate } from "react-router-dom";

const PRIMARY_COLOR = "#174bd1ff";

const BenutzergruppeNeuanlage: React.FC = () => {
  const [benutzergruppe, setBenutzergruppe] = useState<string>("");
  const [beschreibung, setBeschreibung] = useState<string>("");
  const [freigabe, setFreigabe] = useState<boolean>(false);
  const [error, setError] = useState<string>("");
  const [message, setMessage] = useState<{ text: string; type: string }>({ text: "", type: "" });
  const [loading, setLoading] = useState<boolean>(false);

  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMessage({ text: "", type: "" });

    if (!benutzergruppe.trim()) {
      setError("Benutzergruppe ist erforderlich!");
      return;
    }
    setError("");
    setLoading(true);

    try {
      await axios.post("/api/benutzergruppeneuanlage", {
        benutzergruppe: benutzergruppe,
        beschreibung: beschreibung,
        freigabe: freigabe,
      });

      setMessage({ text: "Benutzergruppe erfolgreich gespeichert!", type: "success" });
      setTimeout(() => navigate("/benutzergruppe"), 1000);
    } catch (err) {
      const axiosError = err as AxiosError<{ message?: string }>;
      setMessage({
        text: axiosError.response?.data.message || "Fehler aufgetreten",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={title}>Benutzergruppe anlegen</h2>

        <form onSubmit={handleSubmit}>
          <div style={field}>
            <label style={label}>
              Benutzergruppe <span style={{ color: "red" }}>*</span>
            </label>
            <input
              type="text"
              value={benutzergruppe}
              onChange={(e) => setBenutzergruppe(e.target.value)}
              style={input}
            />
            {error && <div style={errorStyle}>{error}</div>}
          </div>

          <div style={field}>
            <label style={label}>Beschreibung</label>
            <input
              type="text"
              value={beschreibung}
              onChange={(e) => setBeschreibung(e.target.value)}
              style={input}
            />
          </div>

          <div style={{ marginBottom: "2rem" }}>
            <label>
              <input
                type="checkbox"
                checked={freigabe}
                onChange={(e) => setFreigabe(e.target.checked)}
                style={{ marginRight: "0.5rem" }}
              />
              Freigaberecht
            </label>
          </div>

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
const errorStyle: React.CSSProperties = { color: "red", fontSize: "0.9rem", marginTop: "0.3rem" };
const buttonsRow: React.CSSProperties = { display: "flex", gap: "1rem", justifyContent: "flex-start" };
const buttonBase: React.CSSProperties = {
  backgroundColor: PRIMARY_COLOR,
  color: "#fff",
  padding: "0.5rem 1.5rem",
  border: "none",
  borderRadius: 4,
  cursor: "pointer",
  fontSize: "1rem",
};
const buttonPrimary: React.CSSProperties = { ...buttonBase };
const buttonSecondary: React.CSSProperties = { ...buttonBase };

export default BenutzergruppeNeuanlage;
