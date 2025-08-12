import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
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

  // Popup  state
  const [showPopup, setShowPopup] = useState(false);
  const [resetEmail, setResetEmail] = useState("");
  const [resetMessage, setResetMessage] = useState("");

  const handleLogin = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ text: "", type: "" });

    try {
      const response = await axios.post("http://localhost:8080/api/login", {
        benutzerkennung,
        passwort,
      });

      navigate("/home");

      setMessage({
        text: "Login erfolgreich! Token: " + response.data.token,
        type: "success",
      });
    } catch {
      setMessage({
        text: "Login fehlgeschlagen. Bitte überprüfen Sie Ihre Eingaben.",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleResetSubmit = (e: FormEvent) => {
    e.preventDefault();
    console.log("E-Mail:", resetEmail);
    console.log("Nachricht:", resetMessage);
    setShowPopup(false);
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

        {/* Passwort vergessen linki */}
        <p
          className="forgot-password"
          onClick={() => setShowPopup(true)}
        >
          Passwort vergessen?
        </p>

        {message.text && (
          <p className={`message ${message.type}`}>{message.text}</p>
        )}
      </form>

      {/* Popup */}
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
                <button type="button" onClick={() => setShowPopup(false)}>
                  Abbrechen
                </button>
                <button type="submit">Senden</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default Login;
