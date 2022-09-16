import { api } from "./api";

export class CitiesService {
    getCities(citiesRequest) {
        return api.get("/cities", {
            headers: {
                Authorization: "Bearer " + citiesRequest.jwt,
            }, params: {...citiesRequest.searchParams}
        });
    }

}

export class CitiesRequest {
    constructor(jwt, searchParams) {
        this.jwt = jwt;
        this.searchParams = searchParams;
    }
}