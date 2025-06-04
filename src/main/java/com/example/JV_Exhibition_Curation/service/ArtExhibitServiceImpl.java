package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.model.Artwork;
import com.example.JV_Exhibition_Curation.model.Exhibition;
import com.example.JV_Exhibition_Curation.repository.ArtExhibitRepository;
import com.example.JV_Exhibition_Curation.repository.ExhibitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArtExhibitServiceImpl implements ArtExhibitService{

    @Autowired
    ApiService apiService;
    @Autowired
    ExhibitionRepository exhibitionRepository;

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
        return apiService.getArtworkSearchResult(query, page);
    }

    @Override
    public HttpStatusCode addArtworkToExhibition(SavedArtworksDTO savedArtworksDTO, Long exhibitionID) {
        return null;
    }

    @Override
    public void removeArtworkFromExhibition(Long exhibitionId, SavedArtworksDTO savedArtworksDTO) {
    }

    @Override
    public Exhibition createNewExhibition() {
        Exhibition exhibition = new Exhibition();
        exhibition = exhibitionRepository.save(exhibition);
        exhibition.setTitle("Exhibition " + exhibition.getId());
        return exhibitionRepository.save(exhibition);
    }

    @Override
    public void deleteExhibition(Long exhibitionId) {
        if(exhibitionRepository.existsById(exhibitionId)){
            exhibitionRepository.deleteById(exhibitionId);
        } else {
            //throw exception
        }

    }

    @Override
    public List<Exhibition> getAllExhibitions() {
        ArrayList<Exhibition> exhibitionList = new ArrayList<>();
        exhibitionRepository.findAll().forEach(exhibitionList::add);
        return exhibitionList;
    }

    @Override
    public Exhibition getExhibition(Long exhibitionId) {
        Optional<Exhibition> optionalExhibition = exhibitionRepository.findById(exhibitionId);
        if(optionalExhibition.isPresent()){
            return optionalExhibition.get();
        } else {
            //throw exception
        }
        return null; //change this to exception throw
    }
}
