import { api } from "./api";

export class CitiesService {
    getCities(citiesRequest) {
        return api.get("/cities", {
            headers: {
                Authorization: "Bearer " + citiesRequest.jwt,
            }, params: { ...citiesRequest.searchParams }
        });
    };

    updateCity(jwt, id, cityData) {
        return api.post("/cities/" + id, cityData, {
            headers: {
                Authorization: "Bearer " + jwt,
            }
        });
    }

}

export class CitiesRequest {
    constructor(jwt, searchParams) {
        this.jwt = jwt;
        this.searchParams = searchParams;
    }
}