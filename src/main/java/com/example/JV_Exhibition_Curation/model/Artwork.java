package com.example.JV_Exhibition_Curation.model;

import jakarta.persistence.*;
import lombok.*;

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
    Long apiId;
    String title;
    String description;
    String altText;
    String artistName;
    String imageUrl;
    String apiOrigin;

}
