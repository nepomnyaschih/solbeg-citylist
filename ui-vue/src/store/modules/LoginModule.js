import { isValueEmpty } from "../../utils/HelperFunctions";
import {
  getJwtFromStorage,
  getRolesFromStorage,
  setJwtTokenInStorage,
  setRolesInStorage,
  removeJwtTokenFromStorage,
  removeRolesFromStorage
} from "../../utils/LocalStorageUtils";
import { LoginService } from "../../services/login";

function getInitialState() {
  const token = getJwtFromStorage();
  const roles = getRolesFromStorage();
  if (isValueEmpty(token)) {
    return { loggedIn: false, jwt: "", roles:[] };
  } else {
    return { loggedIn: true, jwt: token,  userRoles: roles};
  }
}

const loginService = new LoginService();

export const loginModule = {
  namespaced: true,
  state: getInitialState(),
  getters: {
    getLoginStatus(state) {
      return state;
    },
    getJwt(state) {
      return state.jwt;
    },
    getRoles(state) {
      return state.userRoles;
    },
  },
  mutations: {
    loginSuccess(state, props) {
      state.loggedIn = true;
      state.jwt = props.jwt;
      state.userRoles = props.roles;
      setJwtTokenInStorage(props.jwt);
      setRolesInStorage(props.roles);
    },
    loginFailure(state) {
      state.loggedIn = false;
      state.jwt = null;
      state.userRoles = null;
      removeJwtTokenFromStorage();
      removeRolesFromStorage()
    },
    logout(state) {
      state.loggedIn = false;
      state.jwt = null;
      state.userRoles = null;
      removeRolesFromStorage();
      removeJwtTokenFromStorage();
    },
  },
  actions: {
    async doLogin({ commit }, loginRequest) {
      try {
        const response = await loginService.doLogin(loginRequest);
        const jwt = response.data.jwtToken;
        const roles = response.data.roles;
        commit("loginSuccess", {jwt, roles});
        return Promise.resolve();
      } catch (error) {
        console.log(error);
        commit("loginFailure");
        return Promise.reject(error);
      }
    },
    doLogout({ commit }) {
      commit("logout");
    },
  },
};
