package com.example.JV_Exhibition_Curation.controller;

import com.example.JV_Exhibition_Curation.service.ArtExhibitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("https://collectionapi.metmuseum.org/public/collection/v1")
public class ArtExhibitsController {

    @Autowired
    ArtExhibitService artExhibitService;

@GetMapping
    //Get all artworks

    @GetMapping
    //Get artwork by id


}
