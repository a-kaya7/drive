import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../ApiConfig/authService"; 
import { api } from "../ApiConfig/api";
import "./Login.css";

interface Message {
  text: string;
  type: "success" | "error" | "";
}

function Login() {
  const navigate = useNavigate();
  const [benutzerkennung, setBenutzerkennung] = useState("");
  const [passwort, setPasswort] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<Message>({ text: "", type: "" });

  // MFA state
  const [showMfaPopup, setShowMfaPopup] = useState(false);
  const [otpAuthUrl, setOtpAuthUrl] = useState("");
  const [mfaCode, setMfaCode] = useState("");

  // Popup state
  const [showPopup, setShowPopup] = useState(false);
  const [resetEmail, setResetEmail] = useState("");
  const [resetMessage, setResetMessage] = useState("");

  // Passwortwechsel
  const [showPasswortPopup, setShowPasswortPopup] = useState(false);
  const [altesPasswort, setAltesPasswort] = useState("");
  const [neuesPasswort, setNeuesPasswort] = useState("");
  const [passwortWiederholung, setPasswortWiederholung] = useState("");
  const [error, setError] = useState<string | null>(null);

  const handleLogin = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ text: "", type: "" });
    setError(null);

    try {
      const data = await login(benutzerkennung, passwort);
      if (data.passwortAenderung) {
        setShowPasswortPopup(true);
      } else if (data.mfaRequired) {
        setOtpAuthUrl(data.qrCodeBase64 || "");
        setShowMfaPopup(true);
      } else if (data.token) {
        localStorage.setItem("token", data.token);
        navigate("/home");
      }
      setMessage({
        text: "",
        type: "success",
      });
    } catch (err: any) {
      setMessage({
        text: err.response?.data?.message || "Login fehlgeschlagen. Bitte überprüfen Sie Ihre Eingaben.",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleMfaSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);

    try {
      const response = await api.post("/api/login/mfa", {
        benutzerkennung,
        mfaCode: Number(mfaCode),
      });

      if (response.data.token) {
        localStorage.setItem("token", response.data.token);
        setShowMfaPopup(false);

        if (response.data.passwortAenderung) {
          setShowPasswortPopup(true);
        } else {
          navigate("/home");
        }
      }
      setMessage({
        text: "MFA erfolgreich bestätigt!",
        type: "success",
      });
    } catch (err: any) {
      setError(err.response?.data || "Ungültiger MFA-Code");
    }
  };

  const handleResetSubmit = (e: FormEvent) => {
    e.preventDefault();
    console.log("E-Mail:", resetEmail);
    console.log("Nachricht:", resetMessage);
    setShowPopup(false);
  };

  const handlePasswortWechselSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    if (neuesPasswort !== passwortWiederholung) {
      setError("Die neuen Passwörter stimmen nicht überein.");
      return;
    }

    try {
     await api.post("/api/benutzer/passwortWechsel", {
        benutzerkennung,
        altesPasswort,
        neuesPasswort,
        passwortWiederholung,
      });

      setShowPasswortPopup(false);
      setMessage({ text: "Passwort erfolgreich geändert. Bitte erneut anmelden.", type: "success" });
      setBenutzerkennung("");
      setPasswort("");
    } catch (err: any) {
      setError(err.response?.data || "Fehler beim Ändern des Passworts.");
    }
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleLogin}>
        <h1>Fahrschule Portal</h1>
        <h3>Anmeldung</h3>

        <label>Benutzername</label>
        <input
          type="text"
          value={benutzerkennung}
          onChange={(e) => setBenutzerkennung(e.target.value)}
          required
        />

        <label>Passwort</label>
        <input
          type="password"
          value={passwort}
          onChange={(e) => setPasswort(e.target.value)}
          required
        />

        <button type="submit" disabled={loading}>
          {loading ? "Wird geprüft..." : "Login"}
        </button>

        <p className="forgot-password" onClick={() => setShowPopup(true)}>
          Passwort vergessen?
        </p>

        {message.text && <p className={`message ${message.type}`}>{message.text}</p>}
      </form>

      {/* MFA Popup */}
      {showMfaPopup && (
        <div className="popup-overlay">
          <div className="popup">
            <h3>MFA Verifizierung</h3>
            <p>Scannen Sie den QR-Code mit Google Authenticator und geben Sie den Code ein:</p>
            {otpAuthUrl && <img src={otpAuthUrl} alt="MFA QR Code" style={{ marginBottom: "10px", width: "200px", height: "200px" }} />}
            <form onSubmit={handleMfaSubmit}>
              <input
                type="text"
                placeholder="MFA Code"
                value={mfaCode}
                onChange={(e) => setMfaCode(e.target.value)}
                required
                maxLength={6}
                pattern="\d{6}"
              />
              {error && <p className="message error">{error}</p>}
              <div className="popup-actions">
                <button type="button" onClick={() => setShowMfaPopup(false)}>Abbrechen</button>
                <button type="submit">Bestätigen</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Passwort reset popup */}
      {showPopup && (
        <div className="popup-overlay">
          <div className="popup">
            <h3>Passwort zurücksetzen</h3>
            <form onSubmit={handleResetSubmit}>
              <input
                type="email"
                placeholder="E-Mail"
                value={resetEmail}
                onChange={(e) => setResetEmail(e.target.value)}
                required
              />
              <textarea
                placeholder="Ihre Nachricht an den Admin"
                value={resetMessage}
                onChange={(e) => setResetMessage(e.target.value)}
                rows={4}
                required
              />
              <div className="popup-actions">
                <button type="button" onClick={() => setShowPopup(false)}>Abbrechen</button>
                <button type="submit">Senden</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Passwortwechsel Popup */}
      {showPasswortPopup && (
        <div className="popup-overlay">
          <div className="popup">
            <h3>Passwort ändern</h3>
            <form onSubmit={handlePasswortWechselSubmit}>
              <label>Altes Passwort</label>
              <input
                type="password"
                value={altesPasswort}
                onChange={(e) => setAltesPasswort(e.target.value)}
                required
              />

              <label>Neues Passwort</label>
              <input
                type="password"
                value={neuesPasswort}
                onChange={(e) => setNeuesPasswort(e.target.value)}
                required
              />

              <label>Neues Passwort wiederholen</label>
              <input
                type="password"
                value={passwortWiederholung}
                onChange={(e) => setPasswortWiederholung(e.target.value)}
                required
              />

              {error && <p className="message error">{error}</p>}

              <div className="popup-actions">
                <button type="button" onClick={() => setShowPasswortPopup(false)}>Abbrechen</button>
                <button type="submit">Speichern</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default Login;
