package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.model.Artwork;

import java.util.List;

public interface ApiService {

    List<Artwork> getAllHomeArtworks(Integer page);

    List<Artwork> getArtworkSearchResult(String query, Integer page);
}
