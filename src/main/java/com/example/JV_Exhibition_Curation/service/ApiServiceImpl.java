package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.model.Artwork;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService{


    private static final String CHICAGO_ARTWORK_ALL_ARTWORKS_URL ="https://api.artic.edu/api/v1/artworks?limit=10&page=";


    @Override
    public List<Artwork> getAllHomeArtworks(Integer page) {

        ArrayList<Artwork> artworks = new ArrayList<>();
        return List.of();
    }
}
