<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { CitiesService, CitiesRequest } from '../../services/cities';

const citiesServise = new CitiesService();
const cities = ref([]);
const searchText = ref("");

const $store = useStore();

async function getCities() {
    const searchParam = {
        searchText: searchText.value,
    };


    const req = new CitiesRequest($store.getters["loginModule/getJwt"], searchParam);
    const response = await citiesServise.getCities(req);
    cities.value = response.data.cities;
}

</script>
        
<template>
    <div class="container" style="margin-top: 50px;">
        <div class="input-group mb-3">
            <input v-model="searchText" type="text" class="form-control" placeholder="Search cities..."
                aria-label="City name" aria-describedby="button-addon2">
            <button @click="getCities" class="btn btn-outline-primary" id="button-addon2">Search</button>
        </div>
    </div>
    <div class="container" style="margin-top: 50px;">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <li class="page-item"><a class="page-link" href="#">1</a></li>
                <li class="page-item"><a class="page-link" href="#">2</a></li>
                <li class="page-item"><a class="page-link" href="#">3</a></li>
            </ul>
        </nav>
    </div>

    <div class="container">
        <table class="table">
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Photo</th>
                    <th scope="col">Name</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="c in cities">
                    <td>{{ c.id }}</td>
                    <td> <img v-bind:src=c.photo> </td>
                    <td>{{ c.name }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
        
<style scoped>

</style>
        