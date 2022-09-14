import { createStore } from "vuex";
import { loginModule } from "./modules/LoginModule";

export default createStore({
  modules: {
    loginModule
  }
});
