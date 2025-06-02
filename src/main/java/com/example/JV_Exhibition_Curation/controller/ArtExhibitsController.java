package com.example.JV_Exhibition_Curation.controller;

import com.example.JV_Exhibition_Curation.model.Artwork;
import com.example.JV_Exhibition_Curation.service.ArtExhibitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/museo")
public class ArtExhibitsController {

    @Autowired
    ArtExhibitService artExhibitService;

    @GetMapping("/artwork/home")
    public ResponseEntity<List<Artwork>> getAllHomeArtworks(@RequestParam Integer page){
        return new ResponseEntity<>(artExhibitService.getAllHomeArtworks(page), HttpStatus.OK);
    }


}
