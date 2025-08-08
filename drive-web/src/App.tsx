import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Login from "./components/Login/Login";
import HomePage from "./components/Homepage/Homepage";
import Administration from "./components/Administration/Administration";
import AdministrationAllgemein from "./components/Administration/AdministrationAllgemein";
import Benutzergruppe from "./components/Administration/Benutzergruppe";
import Benutzergruppeneuanlage from "./components/Administration/Benutzergruppeneuanlage";
import Benutzerbearbeiten from "./components/Administration/Benutzerbearbeiten";
import Benutzerneuanlage from "./components/Administration/Benutzerneuanlage";
import Institut from "./components/Administration/Institut";
import Institutneuanlage from "./components/Administration/Institutneuanlage";
import Institutbearbeiten from "./components/Administration/Institutbearbeiten";
import Mandant from "./components/Administration/Mandant";
import Mandantenneuanlage from "./components/Administration/Mandantneuanlage";

import PortalLayout from "./components/PageLayout/PortalLayout";
import PageLayout from "./components/PageLayout/PageLayout";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />

        <Route element={<PortalLayout />}>
          <Route path="/home" element={<HomePage />} />
          <Route path="/administration" element={<Administration />} />
          <Route path="/administrationallgemein" element={<AdministrationAllgemein />} />

          <Route element={<PageLayout />}>
            <Route path="/institute" element={<Institut />} />
            <Route path="/institutneuanlage" element={<Institutneuanlage />} />
            <Route path="/institutbearbeiten/:institutsname" element={<Institutbearbeiten />} />
            <Route path="/benutzergruppe" element={<Benutzergruppe />} />
            <Route path="/benutzergruppeneuanlage" element={<Benutzergruppeneuanlage />} />
            <Route path="/benutzerbearbeiten/:id" element={<Benutzerbearbeiten />} />
            <Route path="/benutzerneuanlage" element={<Benutzerneuanlage />} />
            <Route path="/mandanten" element={<Mandant />} />
            <Route path="/mandantenneuanlage" element={<Mandantenneuanlage />} />
          </Route>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
