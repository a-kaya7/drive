import React, { useEffect, useState } from "react";
import type { FormEvent } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

const PRIMARY_COLOR = "#174bd1ff";

const BenutzergruppeBearbeiten: React.FC = () =>{
    const { benutzergruppe: routeBenutzergruppe} = useParams<{benutzergruppe: string}>();
    const navigate = useNavigate();

    const[benutzergruppe, setBenutzergruppe] = useState("");
    const[beschreibung, setBeschreibung] = useState("");
    const [freigabe, setFreigabe] = useState<boolean>(false);

    const [error, setError] = useState("");
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
    const fetchBenutzergruppe = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/api/benutzergruppe/${routeBenutzergruppe}`);
        const data = res.data;

        setBenutzergruppe(data.benutzergruppe || "");
        setBeschreibung(data.beschreibung || "");
        setFreigabe(data.freigabe || "");
      } catch (err){
        setError("Fehler beim Laden");
      }
    }
      if(routeBenutzergruppe) fetchBenutzergruppe();
    }, [routeBenutzergruppe]);


    const handleSubmit = async (e:FormEvent) => {
        e.preventDefault();
        try{
            await axios.put(`http://localhost:8080/api/benutzergruppebearbeiten/${routeBenutzergruppe}`, {
                benutzergruppe,
                beschreibung,
                freigabe,
                });
         setTimeout(() => navigate("/benutzergruppe"), 1000);
        } catch (err: any){
    } finally {
      setLoading(false);
    }
         };


         return(    <div style={page}>
      <div style={container}>
        <h2 style={{ ...title, textAlign: "left"}}>Benutzergruppe bearbeiten</h2>

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
const buttonsRow: React.CSSProperties = { display: "flex", gap: "1rem", justifyContent: "flex-end" };
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

export default BenutzergruppeBearbeiten;