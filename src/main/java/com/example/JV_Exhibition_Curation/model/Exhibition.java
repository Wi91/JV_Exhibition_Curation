package com.example.JV_Exhibition_Curation.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Exhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

   private Long id;
   private String title;

    @ManyToMany
    @JoinTable(name = "exhibition_artwork", joinColumns = {@JoinColumn(name = "exhibition_id")}, inverseJoinColumns = {@JoinColumn(name = "artwork_id")})
   private ArrayList<Artwork> artList;


}
