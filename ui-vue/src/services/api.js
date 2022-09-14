import axios from "axios";

const api = axios.create({ baseURL: "http://localhost:8080/api" });
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
