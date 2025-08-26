import React, { useState, useEffect, FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../ApiConfig/api";

const PRIMARY_COLOR = "#174bd1ff";

interface Fuehrerschein {
  fuehrerscheinId: string;
  fuehrerscheinKlasse: string;
}

interface Fahrschueler {
  vorname: string;
  nachname: string;
  geburtsdatum: Date | null;
  strasse: string;
  plz: string;
  ort: string;
  land: string;
  telefonnummer: string;
  email: string;
  fuehrerscheinList: Fuehrerschein[];
  anmeldedatum: Date | null;
  pruefungsstatus: string;
  bezahlt: boolean;
  dokumente: string | null;
  hinweis: string | null;
  notfallkontakt: string | null;
}

const Fahrschuelerneuanlage = () => {
  const [fahrschueler, setFahrschueler] = useState<Fahrschueler>({
    vorname: "",
    nachname: "",
    geburtsdatum: null,
    strasse: "",
    plz: "",
    ort: "",
    land: "",
    telefonnummer: "",
    email: "",
    fuehrerscheinList: [],
    anmeldedatum: null,
    pruefungsstatus: "NOCH_OFFEN",
    bezahlt: false,
    dokumente: null,
    hinweis: null,
    notfallkontakt: null,
  });

  const [availableFuehrerscheinList, setAvailableFuehrerscheinList] = useState<Fuehrerschein[]>([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchFuehrerschein = async () => {
      try {
        const response = await api.get<Fuehrerschein[]>("/api/fuehrerscheinlist");
        setAvailableFuehrerscheinList(response.data);
      } catch (err) {
        console.error("Fehler beim Laden der Führerscheinklasse", err);
      }
    };
    fetchFuehrerschein();
  }, []);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setErrorMessage(null);

    try {
      await api.post("/api/fahrschuelerneuanlage", {
        withCredentials: true,
        vorname: fahrschueler.vorname,
        nachname: fahrschueler.nachname,
        geburtsdatum: fahrschueler.geburtsdatum,
        adresse: {
          strasse: fahrschueler.strasse,
          plz: fahrschueler.plz,
          ort: fahrschueler.ort,
          land: fahrschueler.land,
        },
        telefonnummer: fahrschueler.telefonnummer,
        email: fahrschueler.email,
        fuehrerscheine: fahrschueler.fuehrerscheinList,
        anmeldedatum: fahrschueler.anmeldedatum,
        pruefungsstatus: fahrschueler.pruefungsstatus,
        bezahlt: fahrschueler.bezahlt,
        dokumente: fahrschueler.dokumente,
        hinweis: fahrschueler.hinweis,
        notfallkontakt: fahrschueler.notfallkontakt,
      });

      navigate("/fahrschueler");
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
        <h2 style={title}>Fahrschüler anlegen</h2>

        {errorMessage && <div style={{ color: "red", marginBottom: "1rem" }}>{errorMessage}</div>}

        <form onSubmit={handleSubmit} style={formGrid}>
          {/* Sol taraf */}
          <div>
            <h3 style={sectionTitle}>Allgemeine Angaben</h3>

            <div style={field}>
              <label style={label}>Vorname<span style={{ color: "red" }}>*</span></label>
              <input
                type="text"
                value={fahrschueler.vorname}
                onChange={(e) => setFahrschueler(prev => ({ ...prev, vorname: e.target.value }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>Nachname <span style={{ color: "red" }}>*</span></label>
              <input
                type="text"
                value={fahrschueler.nachname}
                onChange={(e) => setFahrschueler(prev => ({ ...prev, nachname: e.target.value }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>Geburtsdatum <span style={{ color: "red" }}>*</span></label>
              <input
                type="date"
                value={fahrschueler.geburtsdatum ? fahrschueler.geburtsdatum.toISOString().split("T")[0] : ""}
                onChange={(e) => setFahrschueler(prev => ({ ...prev, geburtsdatum: e.target.value ? new Date(e.target.value) : null }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>Straße</label>
              <input type="text" value={fahrschueler.strasse} onChange={e => setFahrschueler(prev => ({ ...prev, strasse: e.target.value }))} style={input} />
            </div>

            <div style={field}>
              <label style={label}>PLZ</label>
              <input type="text" value={fahrschueler.plz} onChange={e => setFahrschueler(prev => ({ ...prev, plz: e.target.value }))} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Ort</label>
              <input type="text" value={fahrschueler.ort} onChange={e => setFahrschueler(prev => ({ ...prev, ort: e.target.value }))} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Land</label>
              <input type="text" value={fahrschueler.land} onChange={e => setFahrschueler(prev => ({ ...prev, land: e.target.value }))} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Telefonnummer</label>
              <input type="text" value={fahrschueler.telefonnummer} onChange={e => setFahrschueler(prev => ({ ...prev, telefonnummer: e.target.value }))} style={input} />
            </div>

            <div style={field}>
              <label style={label}>E-Mail</label>
              <input type="text" value={fahrschueler.email} onChange={e => setFahrschueler(prev => ({ ...prev, email: e.target.value }))} style={input} />
            </div>
          </div>

          {/* Sağ taraf */}
          <div>
            <h3 style={sectionTitle}>Führerschein</h3>

            <div style={field}>
              <label style={label}>Führerschein auswählen <span style={{ color: "red" }}>*</span></label>
              <select
                multiple
                size={8}
                value={fahrschueler.fuehrerscheinList.map(f => f.fuehrerscheinId)}
                onChange={(e) => {
                  const selectedOptions = Array.from(e.target.selectedOptions).map(o => ({
                    fuehrerscheinId: o.value,
                    fuehrerscheinKlasse: o.text,
                  }));
                  setFahrschueler(prev => ({ ...prev, fuehrerscheinList: selectedOptions }));
                }}
                style={input}
              >
                {availableFuehrerscheinList.map(f => (
                  <option key={f.fuehrerscheinId.toString()} value={f.fuehrerscheinId.toString()}>
                    {f.fuehrerscheinKlasse}
                  </option>
                ))}
              </select>

              {fahrschueler.fuehrerscheinList.length > 0 && (
                <div style={{ marginTop: "0.5rem", display: "flex", gap: "0.5rem", flexWrap: "wrap" }}>
                  {fahrschueler.fuehrerscheinList.map(f => (
                    <div
                      key={f.fuehrerscheinId}
                      style={{
                        background: "#eee",
                        padding: "0.3rem 0.6rem",
                        borderRadius: "4px",
                        fontSize: "0.9rem",
                        display: "flex",
                        alignItems: "center"
                      }}
                    >
                      {f.fuehrerscheinKlasse}
                      <button
                        type="button"
                        onClick={() => setFahrschueler(prev => ({
                          ...prev,
                          fuehrerscheinList: prev.fuehrerscheinList.filter(x => x.fuehrerscheinId !== f.fuehrerscheinId)
                        }))}
                        style={{ marginLeft: "0.3rem", border: "none", background: "transparent", cursor: "pointer", fontWeight: "bold" }}
                      >
                        ×
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>

            <div style={field}>
              <label style={label}>Anmeldedatum <span style={{ color: "red" }}>*</span></label>
              <input type="date" value={fahrschueler.anmeldedatum ? fahrschueler.anmeldedatum.toISOString().split("T")[0] : ""} onChange={e => setFahrschueler(prev => ({ ...prev, anmeldedatum: e.target.value ? new Date(e.target.value) : null }))} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Prüfungsstatus</label>
                <select
                  value={fahrschueler.pruefungsstatus}
                  onChange={e =>setFahrschueler(prev => ({ ...prev, pruefungsstatus: e.target.value })) }
                 style={input}>
                  <option value="NOCH_OFFEN">Noch offen</option>
                  <option value="THEORIE_BESTANDEN">Theorie bestanden</option>
                  <option value="PRAXIS_BESTANDEN">Praxis bestanden</option>
               </select>
            </div>

            <div style={field}>
              <label style={label}>Anmeldegebühr bezahlt?<br />             
                <input type="checkbox" checked={fahrschueler.bezahlt} 
                onChange={e => setFahrschueler(prev => ({ ...prev, bezahlt: e.target.checked }))} /> Ja
              </label>
           </div>
           
           <div style={field}>
              <label style={label}>Dokumente</label>
              <textarea value={fahrschueler.dokumente || ""} 
              onChange={e => setFahrschueler(prev => ({ ...prev, dokumente: e.target.value }))} 
              style={{...input, height: '50px', fontSize: '16px', resize: 'vertical' }} />
            </div>

            <div style={field}>
              <label style={label}>Hinweis</label>
              <input type="text" value={fahrschueler.hinweis || ""} onChange={e => setFahrschueler(prev => ({ ...prev, hinweis: e.target.value }))} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Notfallkontakt</label>
              <input type="text" value={fahrschueler.notfallkontakt || ""} onChange={e => setFahrschueler(prev => ({ ...prev, notfallkontakt: e.target.value }))} style={input} />
            </div>

            <div style={buttonsRow}>
              <button type="button" style={buttonSecondary} onClick={() => navigate("/fahrschueler")}>
                Abbrechen
              </button>
              <button type="submit" style={buttonPrimary} disabled={loading}>
                {loading ? "Speichern..." : "Speichern"}
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

// --- Styles ---
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

export default Fahrschuelerneuanlage;
