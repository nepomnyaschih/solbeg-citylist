import { api } from "./api";
import { removeJwtTokenFromStorage, removeRolesFromStorage } from "../utils/LocalStorageUtils";

export class LoginService {
  doLogin(loginRequest) {
    return api.post("/login", loginRequest);
  }

  doLogout() {
    removeJwtTokenFromStorage();
    removeRolesFromStorage();
  }
}

export class LoginRequest {
  constructor(username, password) {
    this.username = username;
    this.password = password;
  }
}
