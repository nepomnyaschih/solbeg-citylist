<script setup>
import { ref } from 'vue'
import { RouterLink, RouterView } from 'vue-router'
import { useStore } from 'vuex'
import router from '../src/router/index'

const $store = useStore();

function isLoggedIn() {
  return $store.getters["loginModule/getLoginStatus"].loggedIn;
}

function logout() {
  $store.dispatch("loginModule/doLogout");
  router.push({ name: "Login" });
}

</script>

<template>

  <nav class="navbar navbar-dark bg-primary vh-5">
    <div class="container-fluid">
      <span class="navbar-brand mb-0 h1">Solbeg cities list application</span>
      <form class="d-flex" role="search">
        <button v-if="isLoggedIn()" v-on:click="logout" class="btn btn-danger" type="submit">Logout</button>
      </form>
    </div>
  </nav>
  <!-- 
          <RouterLink to="/">Cities List</RouterLink>
          <RouterLink to="/login">Login</RouterLink> -->

  <RouterView />
</template>

<style scoped>
.vh-5 {
  height: 5vh !important;
}
</style>
