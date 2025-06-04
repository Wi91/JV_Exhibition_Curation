package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.model.Artwork;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {


    private static final String CHICAGO_ARTWORK_ALL_ARTWORKS_URL = "https://api.artic.edu/api/v1/artworks?limit=10&page=";
    private static final String CHICAGO_IMAGE_BASE_URL = "https://www.artic.edu/iiif/2";


    @Override
    public List<Artwork> getAllHomeArtworks(Integer page) {

        ArrayList<Artwork> artworks = new ArrayList<>();
        ArrayList<Artwork> artworksChicago = getAllHomeChicagoArtworks(page);
        List<Artwork> list = new ArrayList<>(artworksChicago);

        return list;
    }

    @Override
    public List<Artwork> getArtworkSearchResult(String query, Integer page) {
        ArrayList<Artwork> chicagoArtworks = getChicagoSearchArtwork(query, page);
        return chicagoArtworks;
    }

    @Override
    public Artwork getArtworkDetailsByApi(SavedArtworksDTO savedArtworksDTO) {
        Artwork artwork = new Artwork();
        switch (savedArtworksDTO.getApiOrigin()) {
            case "Chicago Institute":
                artwork = getChicagoApiArtwork(savedArtworksDTO.getArtworkId());
        }
        return artwork;
    }

    private Artwork getChicagoApiArtwork(Long artworkId) {
        String url = "https://api.artic.edu/api/v1/artworks/" + artworkId;
        JsonNode results = sendGETRequest(url);

        JsonNode data = results.findPath("data");


        return Artwork.builder()
                .apiId(data.path("id").asLong(0))
                .title(data.path("title").asText("Unknown"))
                .altText(data.path("thumbnail").path("alt_text").asText("None Provided"))
                .apiOrigin("Chicago Institute")
                .artistName(data.path("artist_title").asText("Unknown"))
                .description(data.path("description").asText("No Description Provided"))
                .imageUrl(CHICAGO_IMAGE_BASE_URL + data.path("image_id").asText("No Image") + "/full/843,/0/default.jpg")
                .build();
    }



    private ArrayList<Artwork> getChicagoSearchArtwork(String query, Integer page) {
        String url = String.format("https://api.artic.edu/api/v1/artworks/search?q=%s&page=%d&limit=10", query, page);
        JsonNode results = sendGETRequest(url);
        JsonNode data = results.findPath("data");
        ArrayList<Artwork> searchResults = new ArrayList<>();

        for(JsonNode node : data) {


            Artwork art = Artwork.builder()
                    .id(0L)
                    .apiId(node.path("id").asLong(0))
                    .title(node.path("title").asText("Unknown"))
                    .build();

            searchResults.add(art);
        }
        return searchResults;
    }

    private ArrayList<Artwork> getAllHomeChicagoArtworks(Integer page) {

        ArrayList<Artwork> chicagoArtworks = new ArrayList<>();

        String url = CHICAGO_ARTWORK_ALL_ARTWORKS_URL + page;
        JsonNode results = sendGETRequest(url);

        JsonNode data = results.findPath("data");
        for(JsonNode node : data) {


            Artwork art = Artwork.builder()
                    .id(0L)
                    .apiId(node.path("id").asLong(0))
                    .title(node.path("title").asText("Unknown"))
                    .altText(node.path("thumbnail").path("alt_text").asText("None Provided"))
                    .apiOrigin("Chicago Institute")
                    .artistName(node.path("artist_title").asText("Unknown"))
                    .description(node.path("description").asText("No Description Provided"))
                    .imageUrl(CHICAGO_IMAGE_BASE_URL + node.path("image_id").asText("No Image") + "/full/843,/0/default.jpg")
                    .build();

            chicagoArtworks.add(art);
        }

        return chicagoArtworks;
    }

    private JsonNode sendGETRequest(String url) {

        HttpRequest chiSearchRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();

        try {
           HttpResponse<String> response = client.send(chiSearchRequest, HttpResponse.BodyHandlers.ofString());
           return mapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
