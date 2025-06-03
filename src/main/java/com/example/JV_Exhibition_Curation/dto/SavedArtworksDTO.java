package com.example.JV_Exhibition_Curation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class SavedArtworksDTO {
    @NotNull(message = "ArtworkId cannot be null")
    @Range(min = 0, message = "ArtworkId cannot be less than 0")
    Long artworkId;

    @NotNull(message = "Api Origin cannot be null")
    @NotBlank(message = "Api Origin cannot be blank")
    String apiOrigin;
}
