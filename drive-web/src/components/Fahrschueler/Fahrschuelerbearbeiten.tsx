import React, { useState, useEffect, FormEvent } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { api } from "../ApiConfig/api";

const PRIMARY_COLOR = "#174bd1ff";

interface Adresse {
  strasse: string;
  plz: string;
  ort: string;
  land: string;
}

interface Fuehrerschein {
  fuehrerscheinId: string;
  fuehrerscheinKlasse: string;
  fahrzeuge_Ekl?: string;
  mindestalter?: number;
  voraussetzung?: string;
}

interface Fahrschueler {
  vorname: string;
  nachname: string;
  geburtsdatum: string | null;
  adresse: Adresse;
  telefonnummer: string;
  email: string;
  fuehrerscheine: Fuehrerschein[];
  anmeldedatum: string | null;
  pruefungsstatus: string;
  bezahlt: boolean;
  dokumente: string | null;
  hinweis: string | null;
  notfallkontakt: string | null;
}

const FahrschuelerBearbeiten = () => {
  const { fahrschuelerId } = useParams<{ fahrschuelerId: string }>();
  const [fahrschueler, setFahrschueler] = useState<Fahrschueler>({
    vorname: "",
    nachname: "",
    geburtsdatum: null,
    adresse:{
    strasse: "",
    plz: "",
    ort: "",
    land: "",
    },
    telefonnummer: "",
    email: "",
    fuehrerscheine: [],
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

    const fetchFahrschueler = async () => {
      try {
        const response = await api.get<Fahrschueler>(`/api/fahrschueler/${fahrschuelerId}`);
        const data = response.data as any;
        setFahrschueler({
          vorname: data.vorname,
    nachname: data.nachname,
    geburtsdatum: data.geburtsdatum?.split("T")[0] || null,
    anmeldedatum: data.anmeldedatum?.split("T")[0] || null,
    adresse: data.adresse || { strasse: "", plz: "", ort: "", land: "" },
    telefonnummer: data.telefonnummer,
    email: data.email,
    pruefungsstatus: data.pruefungsstatus,
    bezahlt: data.bezahlt,
    dokumente: data.dokumente,
    hinweis: data.hinweis,
    notfallkontakt: data.notfallkontakt,
    fuehrerscheine: (data.fuehrerscheine || []).map((f: any) => ({
    fuehrerscheinId: f.fuehrerscheinId,
    fuehrerscheinKlasse: f.fuehrerscheinKlasse,
    fahrzeuge_Ekl: f.fahrzeuge_Ekl,
    mindestalter: f.mindestalter,
    voraussetzung: f.voraussetzung
  })),

        });
      } catch (err) {
        console.error("Fehler beim Laden des Fahrschülers", err);
      }
    };

    fetchFuehrerschein();
    fetchFahrschueler();
  }, [fahrschuelerId]);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setErrorMessage(null);

    try {
      await api.put(`/api/fahrschuelerbearbeiten/${fahrschuelerId}`, {
        ...fahrschueler,
      });

      navigate("/fahrschueler");
    } catch (error: any) {
      console.error("Fehler beim Speichern:", error.response?.data || error.message);
      const msg = error.response?.data?.message || error.response?.data || error.message;
      setErrorMessage(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={title}>Fahrschüler bearbeiten</h2>

        {errorMessage && <div style={{ color: "red", marginBottom: "1rem" }}>{errorMessage}</div>}

        <form onSubmit={handleSubmit} style={formGrid}>
          <div>
            <h3 style={sectionTitle}>Allgemeine Angaben</h3>

            <div style={field}>
              <label style={label}>Vorname <span style={{ color: "red" }}>*</span></label>
              <input
                type="text"
                value={fahrschueler.vorname || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, vorname: e.target.value, }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>Nachname <span style={{ color: "red" }}>*</span></label>
              <input
                type="text"
                value={fahrschueler.nachname || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, nachname: e.target.value, }))}
                style={input}
              />
            </div>
            
            <div style={field}>
              <label style={label}>Geburtsdatum <span style={{ color: "red" }}>*</span></label>
              <input
                type="date"
                value={fahrschueler.geburtsdatum || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, geburtsdatum: e.target.value || null }))}
                style={input}
              />
            </div>
            
            <div style={field}>
              <label style={label}>Straße</label>
              <input
                type="text"
                value={fahrschueler.adresse.strasse || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, strasse: e.target.value, }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>Plz</label>
              <input
                type="text"
                value={fahrschueler.adresse.plz || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, plz: e.target.value, }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>Ort</label>
              <input
                type="text"
                value={fahrschueler.adresse.ort || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, ort: e.target.value, }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>Land</label>
              <input
                type="text"
                value={fahrschueler.adresse.land || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, land: e.target.value, }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>Telefon</label>
              <input
                type="text"
                value={fahrschueler.telefonnummer || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, telefonnummer: e.target.value, }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>E-mail<span style={{ color: "red" }}>*</span></label>
              <input
                type="text"
                value={fahrschueler.email || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, email: e.target.value, }))}
                style={input}
              />
            </div>
            
          </div>

          <div>
            <h3 style={sectionTitle}>Führerschein</h3>

            <div style={field}>
              <label style={label}>Führerschein auswählen <span style={{ color: "red" }}>*</span></label>
              <select
                multiple
                size={8}
                value={fahrschueler.fuehrerscheine.map(f => f.fuehrerscheinId.toString())}
                onChange={(e) => {
                  const selectedOptions = Array.from(e.target.selectedOptions).map(o => ({
                    fuehrerscheinId: o.value,
                    fuehrerscheinKlasse: o.text,
                  }));
                  setFahrschueler(prev => ({ ...prev, fuehrerscheine: selectedOptions }));
                }}
                style={input}
              >
                {availableFuehrerscheinList.map(f => (
                  <option key={f.fuehrerscheinId.toString()} value={f.fuehrerscheinId.toString()}>
                    {f.fuehrerscheinKlasse}
                  </option>
                ))}
              </select>

              {fahrschueler.fuehrerscheine.length > 0 && (
                <div style={{ marginTop: "0.5rem", display: "flex", gap: "0.5rem", flexWrap: "wrap" }}>
                  {fahrschueler.fuehrerscheine.map(f => (
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
                          fuehrerscheinList: prev.fuehrerscheine.filter(x => x.fuehrerscheinId !== f.fuehrerscheinId)
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
              <input
                type="date"
                disabled
                value={fahrschueler.anmeldedatum || ""}
                onChange={e => setFahrschueler(prev => ({ ...prev, anmeldedatum: e.target.value || null }))}
                style={input}
              />
            </div>

            <div style={field}>
              <label style={label}>Prüfungsstatus</label>
              <select
                value={fahrschueler.pruefungsstatus}
                onChange={e => setFahrschueler(prev => ({ ...prev, pruefungsstatus: e.target.value }))}
                style={input}
              >
                <option value="NOCH_OFFEN">Noch offen</option>
                <option value="THEORIE_BESTANDEN">Theorie bestanden</option>
                <option value="PRAXIS_BESTANDEN">Praxis bestanden</option>
              </select>
            </div>

            <div style={field}>
              <label style={label}>Bezahlt</label>
              <input
                type="checkbox"
                checked={fahrschueler.bezahlt}
                onChange={e => setFahrschueler(prev => ({ ...prev, bezahlt: e.target.checked }))}
                style={{ marginLeft: "0.5rem" }}
              />
            </div>

            <div style={field}>
              <label style={label}>Dokumente</label>
               <textarea value={fahrschueler.dokumente || ""} 
              onChange={e => setFahrschueler(prev => ({ ...prev, dokumente: e.target.value }))} 
              style={{...input, height: '50px', fontSize: '16px', resize: 'vertical' }} />
            </div>

            <div style={field}>
              <label style={label}>Hinweis</label>
              <input type="text" value={fahrschueler.hinweis || ""} 
              onChange={e => setFahrschueler(prev => ({ ...prev, hinweis: e.target.value }))} style={input} />
            </div>

            <div style={field}>
              <label style={label}>Notfallkontakt</label>
              <input type="text" value={fahrschueler.notfallkontakt || ""} 
              onChange={e => setFahrschueler(prev => ({ ...prev, notfallkontakt: e.target.value }))} style={input} />
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

const page: React.CSSProperties = { fontFamily: "Arial, sans-serif", padding: "2rem" };
const container: React.CSSProperties = { maxWidth: 900, margin: "0 auto" };
const title: React.CSSProperties = { textAlign: "center", color: PRIMARY_COLOR, marginBottom: "1.5rem" };
const sectionTitle: React.CSSProperties = { color: PRIMARY_COLOR, margin: "1rem 0" };
const field: React.CSSProperties = { marginBottom: "1.5rem" };
const label: React.CSSProperties = { display: "block", marginBottom: "0.3rem" };
const input: React.CSSProperties = { width: "100%", padding: "0.5rem 0", border: "none", borderBottom: "2px solid #ccc", fontSize: "1rem", outline: "none" };
const formGrid: React.CSSProperties = { display: "grid", gridTemplateColumns: "1fr 1fr", gap: "2rem" };
const buttonsRow: React.CSSProperties = { display: "flex", gap: "1rem", justifyContent: "flex-end", marginTop: "1rem" };
const buttonBase: React.CSSProperties = { backgroundColor: PRIMARY_COLOR, color: "#fff", padding: "0.5rem 1.5rem", border: "none", borderRadius: "4px", cursor: "pointer", fontSize: "1rem" };
const buttonPrimary: React.CSSProperties = { ...buttonBase };
const buttonSecondary: React.CSSProperties = { ...buttonBase };
export default FahrschuelerBearbeiten;
