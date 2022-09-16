<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { CitiesService, CitiesRequest } from '../../services/cities';

const citiesServise = new CitiesService();

const cities = ref([]);
const searchText = ref("");
const pagination = ref({
    page: 1,
    rowsPerPage: 6,
    rowsNumber: 0
});
const columns = [
    { name: 'name', label: 'Name', field: 'name' },
    { name: 'pic', label: 'Photo', field: 'photo' }
];

const $store = useStore();

async function getCities() {

    const searchParams = {
        searchText: searchText.value,
        page: pagination.value.page-1,
        size: pagination.value.rowsPerPage
    };

    const req = new CitiesRequest($store.getters["loginModule/getJwt"], searchParams);
    const response = await citiesServise.getCities(req);
    cities.value = response.data.cities;


    pagination.value.rowsNumber = response.data.totalCount;
}

function onSearch(){
    pagination.value.page = 1;
    getCities();
}

function onPagination(props) {
    const { page, rowsPerPage } = props.pagination
    if (props) {
        pagination.value.rowsPerPage = rowsPerPage;
        pagination.value.page = page;
        getCities();
    }
}

getCities();
</script>
        
<template>

    <q-page class="row justify-center">

        <div class="column full-width">
            <div class="row q-pa-md">
                <q-table class="full-width" grid title="Cities" :rows="cities" :columns="columns" row-key="name"
                    @request="onPagination" v-model:pagination="pagination" :rows-per-page-options="[3,6,12,24,48]" hide-header>
                    <template v-slot:top-right>
                        <q-input borderless dense debounce="500" v-model="searchText" placeholder="Search"
                            @update:modelValue="onSearch">
                            <template v-slot:append>
                                <q-icon name="search" />
                            </template>
                        </q-input>
                    </template>
                    <template v-slot:item="props">
                        <div class="q-pa-xs col-xs-12 col-sm-6 col-md-4">
                            <q-card>
                                <q-card-section class="text-center">
                                    <strong>{{ props.row.name }}</strong>
                                </q-card-section>
                                <q-separator />
                                <q-card-section class="flex flex-center">
                                    <q-img v-bind:src=props.row.photo loading="lazy" spinner-color="white"
                                        style="max-width: 300px; height: 300px;" :fit="contain" />
                                </q-card-section>
                            </q-card>
                        </div>
                    </template>
                </q-table>
            </div>
        </div>

    </q-page>
</template>
        
<style scoped>

</style>
        