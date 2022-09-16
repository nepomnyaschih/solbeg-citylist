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
  <q-layout view="hHh LpR fFf">
    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-toolbar-title>
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
          </q-avatar>
          Solbeg cities list application
        </q-toolbar-title>
        <q-btn v-if="isLoggedIn()" @click="logout" color="deep-orange" glossy label="Logout" />
      </q-toolbar>
    </q-header>
    <q-page-container>
      <RouterView />
    </q-page-container>
  </q-layout>
</template>

<style scoped>
</style>
