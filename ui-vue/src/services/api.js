import axios from "axios";
const baseURl = import.meta.env.VITE_BACK_BASE_URL;

const api = axios.create({ baseURL: baseURl+"/api" });
api.defaults.timeout = 500;

api.interceptors.request.use(
  function (config) {
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);


export { api };
