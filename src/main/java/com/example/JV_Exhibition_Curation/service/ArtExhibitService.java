package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.model.Artwork;
import com.example.JV_Exhibition_Curation.model.Exhibition;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;

import java.util.List;

public interface ArtExhibitService {
    List<Artwork> getAllHomeArtworks(Integer page);


    List<Artwork> getSearchResults(String query, Integer page);

    Exhibition addArtworkToExhibition(@Valid SavedArtworksDTO savedArtworksDTO, Long exhibitionID);

    Exhibition removeArtworkFromExhibition(Long exhibitionId, @Valid SavedArtworksDTO savedArtworksDTO);

    Exhibition createNewExhibition();

    void deleteExhibition(Long exhibitionId);

    List<Exhibition> getAllExhibitions();

    Exhibition getExhibition(Long exhibitionId);
}
