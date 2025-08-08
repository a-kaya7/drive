
import { useState, type ChangeEvent, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Login.css";

interface Message {
  text: string;
  type: "success" | "error" | "";
}

function Login() {
  const navigate = useNavigate();
  const [userName, setUserName] = useState<string>("");
  const [passwort, setPasswort] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  const [message, setMessage] = useState<Message>({ text: "", type: "" });

  const handleLogin = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ text: "", type: "" });

    try {
      const response = await axios.post("/api/login", {
        userName: userName,
        passwort: passwort,
      });

      navigate("/home");

      setMessage({
        text: "Login erfolgreich! Token: " + response.data.token,
        type: "success",
      });
    } catch (err) {
      setMessage({
        text: "Login fehlgeschlagen. Bitte überprüfen Sie Ihre Eingaben.",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleUserNameChange = (e: ChangeEvent<HTMLInputElement>) => {
    setUserName(e.target.value);
  };

  const handlePasswortChange = (e: ChangeEvent<HTMLInputElement>) => {
    setPasswort(e.target.value);
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleLogin}>
        <h2>Anmeldung</h2>

        <label>Benutzername</label>
        <input
          type="text"
          value={userName}
          onChange={handleUserNameChange}
          required
        />

        <label>Passwort</label>
        <input
          type="password"
          value={passwort}
          onChange={handlePasswortChange}
          required
        />

        <button type="submit" disabled={loading}>
          {loading ? "Wird geprüft..." : "Login"}
        </button>

        {message.text && (
          <p className={`message ${message.type}`}>{message.text}</p>
        )}
      </form>
    </div>
  );
}

export default Login;
