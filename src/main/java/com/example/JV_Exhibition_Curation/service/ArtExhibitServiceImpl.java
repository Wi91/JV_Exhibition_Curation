package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.model.Artwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtExhibitServiceImpl implements ArtExhibitService{

    @Autowired
    ApiService apiService;

    @Override
    public List<Artwork> getAllHomeArtworks(Integer page) {
        return apiService.getAllHomeArtworks(page);
    }
}
