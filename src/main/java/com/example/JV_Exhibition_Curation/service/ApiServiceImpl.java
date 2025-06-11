package com.example.JV_Exhibition_Curation.service;

import com.example.JV_Exhibition_Curation.dto.SavedArtworksDTO;
import com.example.JV_Exhibition_Curation.exception.APIPageOutOfBoundsException;
import com.example.JV_Exhibition_Curation.exception.InvalidArtworkException;
import com.example.JV_Exhibition_Curation.exception.UnknownAPIOriginException;
import com.example.JV_Exhibition_Curation.model.Artwork;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {


    private static final String CHICAGO_ARTWORK_ALL_ARTWORKS_URL = "https://api.artic.edu/api/v1/artworks?limit=10&page=";
    private static final String CHICAGO_IMAGE_BASE_URL = "https://www.artic.edu/iiif/2/";


    @Override
    public List<Artwork> getAllHomeArtworks(Integer page, String origin) {
        ArrayList<Artwork> artworks = new ArrayList<>();
        switch (origin) {
            case "Chicago_Institute":
                artworks = getAllHomeChicagoArtworks(page);
                break;
            case "Metropolitan_Museum":
                artworks = getAllMetropolitanArtworks(page);
                break;
            default:
                throw new UnknownAPIOriginException("API Origin " + origin + "is unknown");
        }
        ArrayList<Artwork> artworksChicago = getAllHomeChicagoArtworks(page);
        List<Artwork> list = new ArrayList<>(artworksChicago);

        return artworks;
    }

    private ArrayList<Artwork> getAllMetropolitanArtworks(Integer page) {
        ArrayList<Artwork> metropolitanArtworks = new ArrayList<>();
        int start = (page - 1) * 10 + 1;
        int end = start + 10;

        String metUrl = "https://collectionapi.metmuseum.org/public/collection/v1/search?q=s&hasImages=true";
        String objectUrl = "https://collectionapi.metmuseum.org/public/collection/v1/objects/";

        JsonNode metResults = sendGETRequest(metUrl);
        System.out.println(metResults.asText());
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        for (int i = start; i <= end; i++) {
            String artDetailsUrl = objectUrl + metResults.path("objectIDs").get(i).asText();
            JsonNode detailResult = sendGETRequest(artDetailsUrl);
            System.out.println(detailResult.asText());
            if (detailResult.has("message")){
                continue;
            }
            Artwork metArtwork = Artwork.builder()
                    .apiId(metResults.path("objectIDs").get(i).asLong())
                    .title(detailResult.path("title").asText("Unknown"))

                    .artistName(detailResult.path("constituents") != null & detailResult.has("constituents") ?
                            detailResult.path("constituents").path(0).path("name")
                                    .asText("Unknown Artist"): "Unknown Artist")
                    .description("No Description Provided")
                    .imageUrl(detailResult.path("primaryImage").asText("No Image Provided"))
                    .altText(detailResult.path("title").asText("Unknown"))
                    .apiOrigin("Metropolitan_Museum")
                    .build();

            metropolitanArtworks.add(metArtwork);
        }
        return metropolitanArtworks;
    }

    @Override
    public List<Artwork> getArtworkSearchResult(String query, Integer page) {
        int errorCounter = 0;
        ArrayList<Artwork> artworkArrayList = new ArrayList<>();
        try {
            ArrayList<Artwork> chicagoArtworks = getChicagoSearchArtwork(query.trim(), page);
            if (!chicagoArtworks.isEmpty()) {
                artworkArrayList.addAll(chicagoArtworks);
            }
        } catch (APIPageOutOfBoundsException e) {
            errorCounter++;
        }
        if (errorCounter >= 1) {
            throw new APIPageOutOfBoundsException("No more pages");
        }
        return artworkArrayList;
    }

    @Override
    public Artwork getArtworkDetailsByApi(SavedArtworksDTO savedArtworksDTO) {
        Artwork artwork = new Artwork();
        System.out.println(savedArtworksDTO.getApiOrigin());
        System.out.println("START");
        switch (savedArtworksDTO.getApiOrigin()) {
            case "Chicago Institute":
                System.out.println("CHICAGO");
                artwork = getChicagoApiArtwork(savedArtworksDTO.getArtworkId());
                break;
            case "Metropolitan_Museum":
                //Add something here
            default:
                System.out.println("NOTHING");
                throw new UnknownAPIOriginException("Api origin unknown");
        }
        return artwork;
    }

    private Artwork getChicagoApiArtwork(Long artworkId) {
        String url = "https://api.artic.edu/api/v1/artworks/" + artworkId;
        JsonNode results = sendGETRequest(url);
        if (results.has("status")) {
            throw new InvalidArtworkException("No artworks with Id " + artworkId);
        }

        JsonNode data = results.findPath("data");


        return Artwork.builder()
                .apiId(data.path("id").asLong(0))
                .title(data.path("title").asText("Unknown"))
                .altText(data.path("thumbnail").path("alt_text").asText("None Provided"))
                .apiOrigin("Chicago Institute")
                .artistName(data.path("artist_title").asText("Unknown"))
                .description(data.path("short_description").asText("No Description Provided"))
                .imageUrl(CHICAGO_IMAGE_BASE_URL + data.path("image_id").asText("No Image") + "/full/843,/0/default.jpg")
                .build();
    }


    private ArrayList<Artwork> getChicagoSearchArtwork(String query, Integer page) {
        query = query.replace(" ", "%20");
        String url = "https://api.artic.edu/api/v1/artworks/search?limit=10&q=" + query + "&page=" + page;
        ArrayList<Artwork> searchResults = new ArrayList<>();
        JsonNode results = sendGETRequest(url);
        if (results.has("status")) {
            return searchResults;
        }
        JsonNode data = results.findPath("data");
//        int total = results.path("pagination").path("total").asInt();
        int totalPages = results.path("pagination").path("total_pages").asInt();

        if (page > totalPages) {
            throw new APIPageOutOfBoundsException("Page is invalid");
        }

        for (JsonNode node : data) {
            long id;
            if (node.has("id")) {
                id = node.path("id").asLong();
            } else {
                continue;
            }
            Artwork art = getChicagoApiArtwork(id);

            searchResults.add(art);
        }
        return searchResults;
    }

    private ArrayList<Artwork> getAllHomeChicagoArtworks(Integer page) {

        ArrayList<Artwork> chicagoArtworks = new ArrayList<>();

        String url = CHICAGO_ARTWORK_ALL_ARTWORKS_URL + page;
        JsonNode results = sendGETRequest(url);

        JsonNode data = results.findPath("data");
        for (JsonNode node : data) {


            Artwork art = Artwork.builder()
                    .id(0L)
                    .apiId(node.path("id").asLong(0))
                    .title(node.path("title").asText("Unknown"))
                    .altText(node.path("thumbnail").path("alt_text").asText("None Provided"))
                    .apiOrigin("Chicago Institute")
                    .artistName(node.path("artist_title").asText("Unknown"))
                    .description(node.path("short_description").asText("No Description Provided"))
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
            System.out.println(response.statusCode());
            System.out.println("GOT SENTTTTTTTTTTTT");
            System.out.println(response.body());
            return mapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
