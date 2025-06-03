package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.model.Artwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtExhibitServiceImpl implements ArtExhibitService{

    @Autowired
    ApiService apiService;

    @Override
    public List<Artwork> getAllHomeArtworks(Integer page) {
        if(page == null || page < 1){
            //Add exception
        }
        return apiService.getAllHomeArtworks(page);
    }

    @Override
    public List<Artwork> getSearchResults(String query, Integer page) {
        if(page == null || page < 1){
            //Add exception
        }
        return List.of();
    }

    @Override
    public HttpStatusCode addArtworkToExhibition(SavedArtworksDTO savedArtworksDTO, Long exhibitionID) {
        return null;
    }

    @Override
    public void removeArtworkFromExhibition(Long exhibitionId, SavedArtworksDTO savedArtworksDTO) {

    }
}
