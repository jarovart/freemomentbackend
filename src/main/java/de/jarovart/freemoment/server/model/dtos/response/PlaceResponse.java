package de.jarovart.freemoment.server.model.dtos.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PlaceResponse {
    private Long id;
    private String name;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private boolean existedPlace;

    public PlaceResponse(Long id, String name, Double latitude, Double longitude, boolean existedPlace) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.existedPlace = existedPlace;
    }
}

