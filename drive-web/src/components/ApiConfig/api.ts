import axios from "axios";

const API_BASE = "http://localhost:8080"; // backend URL

export const api = axios.create({
  baseURL: API_BASE,
    withCredentials: true, 
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem("token"); 
  console.log("Interceptor token:", token);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
