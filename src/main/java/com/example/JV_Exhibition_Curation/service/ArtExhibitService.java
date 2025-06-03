package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.model.Artwork;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;

import java.util.List;

public interface ArtExhibitService {
    List<Artwork> getAllHomeArtworks(Integer page);


    List<Artwork> getSearchResults(String query, Integer page);

    HttpStatusCode addArtworkToExhibition(@Valid SavedArtworksDTO savedArtworksDTO, Long exhibitionID);

    void removeArtworkFromExhibition(Long exhibitionId, @Valid SavedArtworksDTO savedArtworksDTO);
}
