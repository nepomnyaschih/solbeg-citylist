<script setup>
import { ref, computed } from 'vue'
import router from '../../router/index'
import { useStore } from 'vuex'
import { LoginRequest } from '../../services/login';

const $store = useStore();

const formError = ref(false);
const userName = ref('');
const userPassword = ref('');

async function loign() {

    const name = userName.value;
    const pass = userPassword.value;


    if (name == "" || pass == "") {
        formError.value = true;
    } else {
        formError.value = false;
        const loginRequest = new LoginRequest(name, pass);
        await $store.dispatch("loginModule/doLogin", loginRequest);
        router.push("/");
    }
}

</script>
    
<template>
    <section class="vh-95" style="background-color: #508bfc;">
        <div class="container py-5 h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                    <div class="card shadow-2-strong" style="border-radius: 1rem;">
                        <div class="card-body p-5 text-center">
                            <h3 class="mb-5">Sign in</h3>
                            <div class="form-outline mb-4">
                                <input type="text" id="user" v-model="userName" class="form-control form-control-lg" />
                                <label class="form-label" for="user">User</label>
                            </div>
                            <div class="form-outline mb-4">
                                <input type="password" id="password" v-model="userPassword"
                                    class="form-control form-control-lg" />
                                <label class="form-label" for="password">Password</label>
                            </div>
                            <button class="btn btn-primary btn-lg btn-block" type="submit" @click="loign">Login</button>
                            <div v-if="formError" class="cred-err">Name and Password are required!</div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </section>
</template>
    
<style scoped>
.cred-err {
    color: red;
    padding: 1rem;
}

.vh-95 {
    height: 95vh !important;
}
</style>
    