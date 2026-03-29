package de.jarovart.freemoment.server.model.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ImageTransform {
    private Double scale;
    private Double offsetX; // offsetX = -0.2 Prozent / relative Einheit is richtig -> offsetX = 120 // px ist falsch
    private Double offsetY; // offsetX = currentOffset.dx / imageWidth
}
