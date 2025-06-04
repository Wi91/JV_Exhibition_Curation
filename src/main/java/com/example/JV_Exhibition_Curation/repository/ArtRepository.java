package com.example.JV_Exhibition_Curation.repository;

import com.example.JV_Exhibition_Curation.model.Artwork;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtRepository extends CrudRepository<Artwork, Long> {
    Optional<Artwork> findByApiIdAndApiOrigin(Long apiId, String apiOrigin);
    boolean existsByApiIdAndApiOrigin(Long apiId, String apiOrigin);
}
