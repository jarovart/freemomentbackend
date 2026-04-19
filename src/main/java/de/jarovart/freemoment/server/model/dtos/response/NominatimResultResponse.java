package de.jarovart.freemoment.server.model.dtos.response;

public record NominatimResultResponse(
        String display_name,
        Double lat,
        Double lon
) {
}
