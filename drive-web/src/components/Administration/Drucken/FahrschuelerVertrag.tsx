import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { api } from "../../ApiConfig/api";


const PRIMARY_COLOR = "#174bd1ff";

const FahrschuelerVertrag = () => {
  const { fahrschuelerId } = useParams<{ fahrschuelerId: string }>();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [msg, setMsg] = useState("");

  useEffect(() => {
    const downloadVertrag = async () => {
      if (!fahrschuelerId) {
        setMsg("Fahrschüler-ID fehlt.");
        setLoading(false);
        return;
      }

      try {
        const response = await api.get(
          `/api/fahrschuelervertrag/${fahrschuelerId}`,
          {
            responseType: "blob",
          }
        );

        const blob = new Blob([response.data], {
          type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        });

        const url = window.URL.createObjectURL(blob);

        const link = document.createElement("a");
        link.href = url;
        link.download = `ausbildungsvertrag-${fahrschuelerId}.docx`;
        document.body.appendChild(link);
        link.click();

        link.remove();
        window.URL.revokeObjectURL(url);

        alert("Vertrag wurde erfolgreich erstellt und heruntergeladen.");
        setMsg("Vertrag wurde erfolgreich erstellt.");
      } catch (error) {
        console.error("Fehler beim Exportieren des Vertrags", error);
        setMsg("Vertrag konnte nicht erstellt werden.");
      } finally {
        setLoading(false);
      }
    };

    downloadVertrag();
  }, [fahrschuelerId]);

  return (
    <div style={page}>
      <div style={container}>
        <h2 style={title}>Fahrschüler Vertrag</h2>

        {loading ? (
          <p>Vertrag wird erstellt...</p>
        ) : (
          <>
            <p>{msg}</p>

            <button style={buttonPrimary} onClick={() => navigate(-1)}>
              Zurück
            </button>
          </>
        )}
      </div>
    </div>
  );
};

const page: React.CSSProperties = {
  fontFamily: "Arial, sans-serif",
  padding: "2rem",
};

const container: React.CSSProperties = {
  maxWidth: 800,
  margin: "0 auto",
};

const title: React.CSSProperties = {
  color: PRIMARY_COLOR,
  marginBottom: "2rem",
};

const buttonPrimary: React.CSSProperties = {
  backgroundColor: PRIMARY_COLOR,
  color: "#fff",
  padding: "0.7rem 2rem",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
  fontSize: "1rem",
};

export default FahrschuelerVertrag;