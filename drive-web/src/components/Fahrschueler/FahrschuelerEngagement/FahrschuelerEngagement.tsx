import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../../ApiConfig/api";
import { FaUserGroup } from "react-icons/fa6";

const PRIMARY_COLOR = "#174bd1ff";

interface Fahrschueler {
  fahrschuelerId: string;
  vorname: String;
  nachname: string;
}

const FahrschuelerEngagement = () => {
  const { fahrschuelerId } = useParams<{ fahrschuelerId: string }>();
 

  const [fahrschueler, setFahrschueler] = useState<Fahrschueler | null>(null);

  useEffect(() => {
    if (!fahrschuelerId) return;

    const loadFahrschueler = async () => {
      try {
        const response = await api.get(`/api/engagement/${fahrschuelerId}`);
        setFahrschueler(response.data);
      } catch (error) {
        
      }
    };

    loadFahrschueler();
  }, [fahrschuelerId]);

  return (
    <div
      style={{
        padding: "40px",
      }}
    >
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          gap: "12px",
        }}
      >
        <FaUserGroup
          size={35}
          color={PRIMARY_COLOR}
          style={{
    transform: "translate(-570px, -40px)",
  }}
        />

        <div
    style={{
      fontSize: "11px",
      fontWeight: 400,
      color: "#333",
      width: "110%",
      marginTop: "-45px",
      textAlign: "left",
     }}
    >
      {fahrschueler?.vorname} 
       {fahrschueler?.nachname}
      </div>
      </div>
    </div>
  );
};

export default FahrschuelerEngagement;