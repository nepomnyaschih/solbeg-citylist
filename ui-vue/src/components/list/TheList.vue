<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { CitiesService, CitiesRequest } from '../../services/cities';

const citiesServise = new CitiesService();

const cities = ref([]);
const searchText = ref("");
const pagination = ref({
    page: 1,
    rowsPerPage: 3,
    rowsNumber: 0
});
const columns = [
    { name: 'name', label: 'Name', field: 'name' },
    { name: 'pic', label: 'Photo', field: 'photo' }
];

const $store = useStore();

var canEdit = false;

if ($store.getters["loginModule/getRoles"].includes("ROLE_ALLOW_EDIT")) canEdit = true;

async function getCities() {

    const searchParams = {
        searchText: searchText.value,
        page: pagination.value.page - 1,
        size: pagination.value.rowsPerPage
    };

    const req = new CitiesRequest($store.getters["loginModule/getJwt"], searchParams);
    const response = await citiesServise.getCities(req);

    cities.value = response.data.cities;
    pagination.value.rowsNumber = response.data.totalCount;
}

function onSearch() {
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

onMounted(() => {
    getCities();
})

async function update(id, name, photo) {
    try {
        return await citiesServise.updateCity($store.getters["loginModule/getJwt"], id, { name, photo });
    } catch (err) {
        return false;
    }
}

</script>
        
<template>
    <q-page class="row justify-center">
        <div class="column full-width">
            <div class="row q-pa-md">
                <q-table class="full-width" grid title="Cities" :rows="cities" :columns="columns" row-key="name"
                    @request="onPagination" v-model:pagination="pagination" :rows-per-page-options="[3,6,12,24,48]"
                    hide-header>
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
                                    <q-icon name="edit" v-if="canEdit" />
                                    <q-popup-edit v-model.number="props.row.name" v-slot="scope"
                                        @save="(value, initialValue)=>{update(props.row.id, value, props.row.photo)}"
                                        :validate="(val)=>{return val.length > 0}" :disable="!canEdit">
                                        <q-input type="name" v-model.number="scope.value" dense autofocus
                                            @keyup.enter="scope.set"
                                            :rules="[ val => val && val.length > 0 || 'fill it']" />
                                    </q-popup-edit>
                                </q-card-section>
                                <q-separator />
                                <q-card-section class="flex flex-center">
                                    <q-popup-edit v-model.number="props.row.photo" auto-save
                                        @save="(value, initialValue)=>{update(props.row.id, props.row.name, value)}"
                                        :validate="(val)=>{return val.length > 0}" v-slot="scope" :disable="!canEdit">
                                        <q-input type="name" v-model.number="scope.value" dense autofocus
                                            @keyup.enter="scope.set"
                                            :rules="[ val => val && val.length > 0 || 'fill it']" />
                                    </q-popup-edit>
                                    <q-img v-bind:src=props.row.photo loading="lazy" spinner-color="gray"
                                        style="max-width: 300px; height: 300px;">
                                        <template v-slot:error>
                                            <div class="absolute-full flex flex-center text-white">
                                                Cannot load image
                                            </div>
                                        </template>
                                    </q-img>
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
        