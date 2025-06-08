package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.exception.APIPageOutOfBoundsException;
import com.example.JV_Exhibition_Curation.exception.DuplicatedArtworkException;
import com.example.JV_Exhibition_Curation.exception.InvalidExhibitionException;
import com.example.JV_Exhibition_Curation.exception.InvalidRequestException;
import com.example.JV_Exhibition_Curation.model.Artwork;
import com.example.JV_Exhibition_Curation.model.Exhibition;
import com.example.JV_Exhibition_Curation.repository.ArtRepository;
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
    @Autowired
    ArtRepository artRepository;

    @Override
    public List<Artwork> getAllHomeArtworks(Integer page) {
        if(page == null || page < 1){
          throw new APIPageOutOfBoundsException("Page must be greater or equal to 1");
        }
        return apiService.getAllHomeArtworks(page);
    }

    @Override
    public List<Artwork> getSearchResults(String query, Integer page) {
        if(page == null || page < 1){
            throw new APIPageOutOfBoundsException("Page must be greater or equal to 1");
        }
        return apiService.getArtworkSearchResult(query, page);
    }

    @Override
    public Exhibition addArtworkToExhibition(SavedArtworksDTO savedArtworksDTO, Long exhibitionId) {
        Optional<Exhibition> exhibitionOptional = exhibitionRepository.findById(exhibitionId);
        if(exhibitionOptional.isEmpty()){
            throw new InvalidExhibitionException("Exhibition does not exist");
            //throw exception
        }
        Exhibition exhibition = exhibitionOptional.get();
        Artwork artwork = apiService.getArtworkDetailsByApi(savedArtworksDTO);
        if(!artRepository.existsByApiIdAndApiOrigin(artwork.getApiId(), artwork.getApiOrigin())){
            artwork = artRepository.save(artwork);
        } else {
            artwork = artRepository.findByApiIdAndApiOrigin(artwork.getApiId(), artwork.getApiOrigin()).get();
        }
        if(exhibition.getArtList().contains(artwork)){
            throw new DuplicatedArtworkException("Exhibition already contains artwork");
            //throw exception
        } else {
            ArrayList<Artwork> artworkArrayList = new ArrayList<>(exhibition.getArtList());
           artworkArrayList.add(artwork);
           exhibition.setArtList(artworkArrayList);
        }
        return exhibitionRepository.save(exhibition);
    }

    @Override
    public Exhibition removeArtworkFromExhibition(Long exhibitionId, SavedArtworksDTO savedArtworksDTO) {
        Optional<Exhibition> exhibitionOptional = exhibitionRepository.findById(exhibitionId);
        if(exhibitionOptional.isEmpty()){
            throw new InvalidExhibitionException("No exhibitions");
            //throw exception
        }
        Exhibition exhibition = exhibitionOptional.get();
        Optional<Artwork> optionalArtwork = artRepository.findByApiIdAndApiOrigin(savedArtworksDTO.getArtworkId(), savedArtworksDTO.getApiOrigin());
        if(optionalArtwork.isEmpty()){
            throw new InvalidExhibitionException("Artwork is empty");
            //throw exception
        }
        Artwork artwork = optionalArtwork.get();
        if(!exhibition.getArtList().contains(artwork)){
            throw new DuplicatedArtworkException("Artwork not in exhibition");
            //Name is misleading, intent to throw conflict
        }
        exhibition.getArtList().remove(artwork);
        return exhibitionRepository.save(exhibition);
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
            throw new InvalidExhibitionException("Exhibition does not exist");
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
            throw new InvalidExhibitionException("Exhibition does not exist");
        }
    }
}
