package com.example.JV_Exhibition_Curation.repository;

import com.example.JV_Exhibition_Curation.model.Artwork;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtExhibitRepository extends CrudRepository<Artwork, Long> {
}
