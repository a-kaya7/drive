import { api } from "./api";

export const login = async (username: string, password: string) => {
  const response = await api.post("/api/login", { benutzerkennung: username, passwort: password });
  if (!response.data.mfaRequired) {
    localStorage.setItem("token", response.data.token);
  }
  return response.data;
};

export const logout = () => {
  localStorage.removeItem("token");
};
