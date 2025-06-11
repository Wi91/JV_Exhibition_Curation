package com.example.JV_Exhibition_Curation.controller;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.model.Artwork;
import com.example.JV_Exhibition_Curation.model.Exhibition;
import com.example.JV_Exhibition_Curation.service.ArtExhibitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/museo")
public class ArtExhibitsController {

    @Autowired
    ArtExhibitService artExhibitService;

    @GetMapping("/artwork/home")
    public ResponseEntity<List<Artwork>> getAllHomeArtworks(@RequestParam Integer page, @RequestParam String origin){
        return new ResponseEntity<>(artExhibitService.getAllHomeArtworks(page, origin), HttpStatus.OK);
    }

    //Search All Artworks
    @GetMapping("/artwork/home/search")
    public ResponseEntity<List<Artwork>> searchArtworks(@RequestParam String query, @RequestParam Integer page){
        return new ResponseEntity<>(artExhibitService.getSearchResults(query, page), HttpStatus.OK);
    }

    //Get artwork details from API

    //Add Artwork To Exhibition
    @PostMapping("/exhibitions/{exhibitionId}/artworks")
    public ResponseEntity<Exhibition> addArtworkToExhibition (@RequestBody @Valid SavedArtworksDTO savedArtworksDTO, @PathVariable Long exhibitionId){
        return new ResponseEntity<>(artExhibitService.addArtworkToExhibition(savedArtworksDTO, exhibitionId), HttpStatus.OK);
    }

    //Delete Artwork From Exhibition
    @DeleteMapping("/exhibitions/{exhibitionId}/artworks")
    public ResponseEntity<Exhibition> removeArtworkFromExhibition (@PathVariable Long exhibitionId, @RequestBody @Valid SavedArtworksDTO savedArtworksDTO){
        return new ResponseEntity<>(artExhibitService.removeArtworkFromExhibition(exhibitionId, savedArtworksDTO), HttpStatus.OK);
    }

    //Create Exhibition
    @PostMapping("/exhibitions")
    public ResponseEntity<Exhibition> createNewExhibition (){
        return new ResponseEntity<>(artExhibitService.createNewExhibition(), HttpStatus.CREATED);
    }

    //Delete Exhibition
    @DeleteMapping("/exhibitions/{exhibitionId}")
    public ResponseEntity<Exhibition> deleteExhibition (@PathVariable Long exhibitionId){
        artExhibitService.deleteExhibition(exhibitionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Retrieve all Exhibitions
    @GetMapping("/exhibitions")
    public ResponseEntity<List<Exhibition>> getAllExhibitions () {
        return new ResponseEntity<>(artExhibitService.getAllExhibitions(), HttpStatus.OK);
    }

    @GetMapping("/exhibitions/{exhibitionId}")
    public ResponseEntity<Exhibition> getExhibition (@PathVariable Long exhibitionId) {
        return new ResponseEntity<>(artExhibitService.getExhibition(exhibitionId), HttpStatus.OK);
    }

}
