package com.solbeg.citylist.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityUpdateResponse {

    private boolean isSuccess;
    private String message;

}
