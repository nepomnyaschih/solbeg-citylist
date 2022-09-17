<script setup>
import { ref, } from 'vue'
import router from '../../router/index'
import { useStore } from 'vuex'
import { LoginRequest } from '../../services/login';

const $store = useStore();

const userName = ref('');
const userPassword = ref('');

async function loign() {

    const name = userName.value;
    const pass = userPassword.value;

    const loginRequest = new LoginRequest(name, pass);
    await $store.dispatch("loginModule/doLogin", loginRequest);
    router.push("/");
}

</script>
    
<template>
    <q-page class="row justify-center">
        <div class="column">
            <div class="row">
                <h5 class="text-h5 q-my-md">Sign in</h5>
            </div>
            <div class="row">
                <q-card square bordered class="q-pa-lg shadow-1">
                    <q-card-section>
                        <q-form class="" @submit="loign">
                            <q-input filled v-model="userName" label="Login" lazy-rules
                                :rules="[ val => val && val.length > 0 || 'required field']" />
                            <q-input filled type="password" v-model="userPassword" label="Password" lazy-rules
                                :rules="[ val => val && val.length > 0 || 'required field']" />
                            <q-btn label="Login" type="submit" color="primary" class="full-width" />
                        </q-form>
                    </q-card-section>
                    <q-card-section class="q-pt-none">
                        Users for test:
                        <br/>
                        view - view
                        <br/>
                        edit - edit
                    </q-card-section>
                </q-card>
            </div>
        </div>
    </q-page>
</template>
    
<style scoped>

</style>
    