package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.model.Artwork;

import java.util.List;

public interface ApiService {

    List<Artwork> getAllHomeArtworks(Integer page, String origin);

    List<Artwork> getArtworkSearchResult(String query, Integer page);

    Artwork getArtworkDetailsByApi(SavedArtworksDTO savedArtworksDTO);
}
