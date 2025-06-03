package com.example.JV_Exhibition_Curation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "artwork", uniqueConstraints = {@UniqueConstraint(columnNames = {"apiId", "apiOrigin"})})

public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long id;
    @NotNull(message = "Api Id cannot be null")
    @Range(min = 0, message = "Api Id must be 0 or above")
    Long apiId;
    @NotNull(message = "Title cannot be null")
    String title;
    @NotNull(message = "Description cannot be null")
    String description;
    @NotNull(message = "Alt Text cannot be null")
    String altText;
    @NotNull(message = "Artist name cannot be null")
    String artistName;
    @NotNull(message = "Image Url cannot be null")
    String imageUrl;
    @NotNull(message = "Api origin cannot be null")
    String apiOrigin;

}
