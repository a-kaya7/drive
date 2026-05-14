import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Login from "./components/Login/Login";
import Logout from "./components/Login/Logout";
import HomePage from "./components/Homepage/Homepage";
import Administration from "./components/Administration/Administration";
import AdministrationAllgemein from "./components/Administration/AdministrationAllgemein";
import Benutzergruppe from "./components/Administration/Benutzergruppe";
import Benutzergruppeneuanlage from "./components/Administration/Benutzergruppeneuanlage";
import Benutzerbearbeiten from "./components/Administration/Benutzerbearbeiten";
import Benutzerneuanlage from "./components/Administration/Benutzerneuanlage";
import Benutzergruppebearbeiten from "./components/Administration/Benutzergruppebearbeiten";
import Benutzerbearbeiten2 from "./components/Administration/Beutzerbearbeiten2";
import Institut from "./components/Administration/Institut";
import Institutneuanlage from "./components/Administration/Institutneuanlage";
import Institutbearbeiten from "./components/Administration/Institutbearbeiten";
import Mandant from "./components/Administration/Mandant";
import Mandantenneuanlage from "./components/Administration/Mandantneuanlage";
import Mandantbearbeiten from "./components/Administration/Mandantbearbeiten";
import Organisationseinheiten from "./components/Administration/Organisationseinheiten";
import Fuehrerscheinverwalten from "./components/Führerschein/Fuehrerscheinverwalten";
import Fahrschuelerverwaltung from "./components/Fahrschueler/Fahrschuelerverwaltung";
import Fahrschuelerneuanlage from "./components/Fahrschueler/Fahrschuelerneuanlage";
import Fahrschueler from "./components/Fahrschueler/Fahrschueler";
import Fahrschuelerbearbeiten from "./components/Fahrschueler/Fahrschuelerbearbeiten";
import FahrschuelerVertrag from "./components/Administration/Drucken/FahrschuelerVertrag";


import PortalLayout from "./components/PageLayout/PortalLayout";
import PageLayout from "./components/PageLayout/PageLayout";
import PageFahrschueler from "./components/PageLayout/PageFahrschueler";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/portal/*" element={<Logout><PortalLayout/></Logout>}/>

        <Route element={<PortalLayout />}>
          <Route path="/home" element={<HomePage />} />
          <Route path="/administration" element={<Administration />} />
          <Route path="/administrationallgemein" element={<AdministrationAllgemein />} />
          <Route path="/fahrschuelerverwaltung" element={<Fahrschuelerverwaltung/>} />

          <Route element={<PageLayout />}>
            <Route path="/institute" element={<Institut />} />
            <Route path="/institutneuanlage" element={<Institutneuanlage />} />
            <Route path="/institutbearbeiten/:institutsname" element={<Institutbearbeiten />} />
            <Route path="/benutzergruppe" element={<Benutzergruppe />} />
            <Route path="/benutzergruppeneuanlage" element={<Benutzergruppeneuanlage />} />
            <Route path="/benutzergruppebearbeiten/:benutzergruppe" element={<Benutzergruppebearbeiten />} />
            <Route path="/benutzerbearbeiten/:benutzergruppe" element={<Benutzerbearbeiten />} />
            <Route path="/benutzerneuanlage/:benutzergruppe" element={<Benutzerneuanlage />} />
            <Route path="/benutzerbearbeiten2/:benutzerkennung" element={<Benutzerbearbeiten2/>} />
            <Route path="/mandanten" element={<Mandant />} />
            <Route path="/mandantenneuanlage" element={<Mandantenneuanlage />} />
            <Route path="/mandantbearbeiten/:idname" element={<Mandantbearbeiten/>} />
            <Route path="/organisationseinheiten" element={<Organisationseinheiten/>} />
            <Route path="/führerscheinverwalten" element={<Fuehrerscheinverwalten/>} />
            <Route path="/fahrschuelervertrag/:fahrschuelerId" element={<FahrschuelerVertrag />} />
            
          </Route>
          <Route element={<PageFahrschueler/>}>
           <Route path="/fahrschuelerneuanlage" element={<Fahrschuelerneuanlage/>} />
           <Route path="/fahrschueler" element={<Fahrschueler/>} />
           <Route path="/fahrschuelerbearbeiten/:fahrschuelerId" element={<Fahrschuelerbearbeiten/>} />
          </Route>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
