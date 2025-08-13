import React, { useState, useEffect } from "react";
import type { FormEvent } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

const PRIMARY_COLOR = "#174bd1ff";

const BenutzerBearbeiten: React.FC = () => {
  const { benutzerkennung: routeBenutzerkennung , benutzergruppe } = useParams<{ benutzerkennung: string, benutzergruppe:string }>();
  const navigate = useNavigate();

  const [benutzerkennung, setBenutzerkennung] = useState("");
  const [anrede, setAnrede] = useState("");
  const [vorname, setVorname] = useState("");
  const [nachname, setNachname] = useState("");
  const [email, setEmail] = useState("");
  const [benutzerVon, setBenutzerVon] = useState("");
  const [benutzerBis, setBenutzerBis] = useState("");

  const [passwort, setPasswort] = useState("");
  const [passwortWiederholung, setPasswortWiederholung] = useState("");
  const [passwortAb, setPasswortAb] = useState("");
  const [zeitraumPasswort, setZeitraumPasswort] = useState<number>(0);
  const [passwortAenderung, setPasswortAenderung] = useState(false);
  const [mfa, setMfa] = useState(false);
  const [mandant, setMandant] = useState("");

  const [mandantListe, setMandantListe] = useState<Array<{ mandantId: string; idname: string }>>([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    const fetchBenutzer = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/benutzer/${routeBenutzerkennung}`);
        const data = response.data;

        setBenutzerkennung(data.benutzerkennung || "");
        setAnrede(data.anrede || "");
        setVorname(data.vorname || "");
        setNachname(data.nachname || "");
        setEmail(data.email || "");
        setBenutzerVon(data.benutzerVon || "");
        setBenutzerBis(data.benutzerBis || "");
        setPasswortAb(data.passwortAb || "");
        setZeitraumPasswort(data.zeitraumPasswort || 0);
        setPasswortAenderung(data.passwortAenderung || false);
        setMfa(data.mfa || false);
        setMandant(data.mandant?.idname || "");
      } catch (error: any) {
        console.error("Fehler beim Laden des Benutzers:", error.response?.data || error.message);
        if(error.response && error.response.data){
            const data = error.response.data;
            const msg = typeof data === 'string' ? data :(data.message || JSON.stringify(data));
            setErrorMessage(msg);
        }
        }
        finally{
            setLoading(false);
        }
    };

    const fetchMandanten = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/mandantenlist");
        setMandantListe(response.data);
      } catch (error) {
        console.error("Fehler beim Laden der Mandanten:", error);
      }
    };

    if (routeBenutzerkennung) {
      fetchBenutzer();
      fetchMandanten();
    }
  }, [routeBenutzerkennung]);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setErrorMessage(null);

    try {
      await axios.put(`http://localhost:8080/api/benutzerbearbeiten/${benutzerkennung}`, {
        benutzerkennung,
        anrede,
        vorname,
        nachname,
        email,
        benutzerVon,
        benutzerBis,
        passwort,
        passwortWiederholung,
        passwortAb,
        zeitraumPasswort,
        passwortAenderung,
        mfa,
        benutzergruppe,
        mandant:{idname: mandant}
      });
      const targetBenutzergruppe = benutzergruppe || "defaultGruppe";
      setTimeout(() => navigate(`/benutzerbearbeiten/${targetBenutzergruppe}`), 1000);
    } catch (error: any) {
      console.error("Fehler beim Speichern:", error.response?.data || error.message);
      if (error.response && error.response.data) {
        const data = error.response.data;
        const msg = typeof data === "string" ? data : data.message || JSON.stringify(data);
        setErrorMessage(msg);
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={{ ...title, textAlign: "left" }}>Benutzer bearbeiten</h2>

        {errorMessage && (
          <div style={{ color: "red", marginBottom: "1rem" }}>
            {errorMessage}
          </div>
        )}

        <form onSubmit={handleSubmit} style={formGrid}>
          <div>
            <h3 style={sectionTitle}>Allgemeine Angaben</h3>

            <div style={field}>
              <label style={label}>Benutzerkennung <span style={{ color: "red" }}>*</span></label>
              <input
                type="text"
                value={benutzerkennung}
                onChange={(e) => setBenutzerkennung(e.target.value)}
                style={input}
                disabled
              />
            </div>

            <div style={field}>
              <label style={label}>Anrede</label>
              <input type="text" value={anrede} onChange={(e) => setAnrede(e.target.value)} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Vorname <span style={{ color: "red" }}>*</span></label>
              <input type="text" value={vorname} onChange={(e) => setVorname(e.target.value)} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Nachname</label>
              <input type="text" value={nachname} onChange={(e) => setNachname(e.target.value)} style={input} />
            </div>

            <div style={field}>
              <label style={label}>E-Mail</label>
              <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Benutzer von</label>
              <input type="date" value={benutzerVon} onChange={(e) => setBenutzerVon(e.target.value)} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Benutzer bis</label>
              <input type="date" value={benutzerBis} onChange={(e) => setBenutzerBis(e.target.value)} style={input} />
            </div>
          </div>

          <div>
            <h3 style={sectionTitle}>Passwort</h3>

            <div style={field}>
              <label style={label}>Passwort <span style={{ color: "red" }}>*</span></label>
              <input type="password" value={passwort} onChange={(e) => setPasswort(e.target.value)} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Passwort (Wiederholung) <span style={{ color: "red" }}>*</span></label>
              <input type="password" value={passwortWiederholung} onChange={(e) => setPasswortWiederholung(e.target.value)} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Passwort ab</label>
              <input type="date" value={passwortAb} onChange={(e) => setPasswortAb(e.target.value)} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Zeitraum für Passwort (Tage)</label>
              <input
                type="number"
                min={0}
                value={zeitraumPasswort}
                onChange={(e) => setZeitraumPasswort(Number(e.target.value))}
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

            <div>
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

            <div style={field}>
                <h1></h1>
              <label htmlFor="mandant-select" style={label}>
                Mandant auswählen <span style={{ color: "red" }}>*</span>
              </label>
              <select
                id="mandant-select"
                value={mandant}
                onChange={(e) => setMandant(e.target.value)}
                style={{ ...input, padding: "0.5rem" }}
                required
              >
                <option value="">Bitte wählen</option>
                {mandantListe.map((inst) => (
                  <option key={inst.mandantId.toString()} value={inst.idname.toString()}>
                    {inst.idname}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div style={{ gridColumn: "1 / span 2", ...buttonsRow }}>
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

// Stil tanımları
const page: React.CSSProperties = { fontFamily: "Arial, sans-serif", padding: "2rem" };
const container: React.CSSProperties = { maxWidth: 900, margin: "0 auto" };
const title: React.CSSProperties = { textAlign: "center", color: PRIMARY_COLOR, marginBottom: "1.5rem" };
const sectionTitle: React.CSSProperties = { color: PRIMARY_COLOR, margin: "1rem 0" };
const field: React.CSSProperties = { marginBottom: "1.5rem" };
const label: React.CSSProperties = { display: "block", marginBottom: "0.3rem" };
const input: React.CSSProperties = { width: "100%", padding: "0.5rem 0", border: "none", borderBottom: "2px solid #ccc", fontSize: "1rem", outline: "none" };
const buttonsRow: React.CSSProperties = { display: "flex", gap: "1rem", justifyContent: "flex-end", marginTop: "1rem" };
const buttonBase: React.CSSProperties = { backgroundColor: PRIMARY_COLOR, color: "#fff", padding: "0.5rem 1.5rem", border: "none", borderRadius: "4px", cursor: "pointer", fontSize: "1rem" };
const buttonPrimary: React.CSSProperties = { ...buttonBase };
const buttonSecondary: React.CSSProperties = { ...buttonBase };
const formGrid: React.CSSProperties = { display: "grid", gridTemplateColumns: "1fr 1fr", gap: "2rem" };

export default BenutzerBearbeiten;
