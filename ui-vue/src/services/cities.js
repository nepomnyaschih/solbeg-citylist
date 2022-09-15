import { api } from "./api";
import { useStore } from 'vuex'

const $store = useStore();

export class CitiesService {
    getCities(citiesRequest) {
        return api.get("/cities", {
            headers: {
                Authorization: "Bearer " + citiesRequest.jwt,
            }, params: {...citiesRequest.searchText,...citiesRequest.page,...citiesRequest.size}
        });
    }

}

export class CitiesRequest {
    constructor(jwt, searchText) {
        this.jwt = jwt;
        this.searchText = searchText;
        this.page = 0;
        this.size = 10;
    }
}