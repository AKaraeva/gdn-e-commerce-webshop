package at.gdn.backend.valueobjects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
public class ProductImage {
    @Column(name = "image_name", nullable = false)
    private String imageName;
}
