package com.example.JV_Exhibition_Curation.controller;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.model.Artwork;
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
    public ResponseEntity<List<Artwork>> getAllHomeArtworks(@RequestParam Integer page){
        return new ResponseEntity<>(artExhibitService.getAllHomeArtworks(page), HttpStatus.OK);
    }

    //Search artworks
    @GetMapping("/artwork/home/search")
    public ResponseEntity<List<Artwork>> searchArtworks(@RequestParam String query, @RequestParam Integer page){
        return new ResponseEntity<>(artExhibitService.getSearchResults(query, page), HttpStatus.OK);
    }

    //Create exhibition
    @PostMapping("/exhibitions/{exhibitionId}")
    public ResponseEntity<List<Artwork>> addArtworkToExhibition (@RequestBody @Valid SavedArtworksDTO savedArtworksDTO, @PathVariable Long exhibitionId){
        return new ResponseEntity<>(artExhibitService.addArtworkToExhibition(savedArtworksDTO, exhibitionId));
    }

    @DeleteMapping("/exhibitions/{exhibitionId}/{artworkId}")
    public ResponseEntity<Artwork> removeArtworkFromExhibition (@PathVariable Long exhibitionId, @RequestBody @Valid SavedArtworksDTO savedArtworksDTO){
        artExhibitService.removeArtworkFromExhibition(exhibitionId, savedArtworksDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
