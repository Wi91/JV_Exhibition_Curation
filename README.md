# JV_Exhibition_Curation

# Museo Backend API

## About

**Museo** is a backend API service designed to power a frontend Android application that lets users explore and curate art collections. It integrates with two external museum/art collection APIs to fetch data on artworks and exhibitions. Users can create custom exhibitions and save individual artworks within them, enabling personalized curation experiences.


- Integration with external museum APIs for rich art data.
- Support for user-generated custom exhibitions.
- Scalable RESTful API design for seamless frontend consumption.

## Dependencies

Museo is built using **Spring Boot** and relies on several key libraries and frameworks:

| Dependency                      | Purpose                                                                 |
|--------------------------------|-------------------------------------------------------------------------|
| `spring-boot-starter-web`       | Provides core web functionalities including RESTful API support.        |
| `spring-boot-starter-data-jpa`  | Enables integration with relational databases via JPA and Hibernate.    |
| `spring-boot-starter-test`      | Includes testing libraries for unit and integration tests (JUnit, etc). |
| `h2`                            | In-memory database used for development and testing.                    |
| `postgresql`                    | PostgreSQL driver for production-level relational database access.     |
| `lombok`                        | Reduces boilerplate code by auto-generating getters, setters, etc.     |
| `hibernate-validator`           | Provides validation framework for request data using annotations.       |

> Java version used: **21**  
> Spring Boot version: **3.4.5**

## Link to App

Frontend Android [Application](https://github.com/Wi91/JV_Exhibition_Curation_Frontend)  


## Base URL

The API base URL for all endpoints is:

`/api/v1/museo`

For local development, the full base URL is:

`http://localhost:8080/api/v1/museo`

> This is the default path unless configured differently in the application settings.



## Endpoints

All endpoints are prefixed with the base URL:  
`http://localhost:8080/api/v1/museo`

| Method | Endpoint                                        | Description                                      |
|--------|-------------------------------------------------|--------------------------------------------------|
| GET    | `/artwork/home`                                 | Get a paginated list of artworks for the home feed. Requires `page` and `origin` query params. |
| GET    | `/artwork/home/search`                          | Search for artworks based on a query string. Requires `query` and `page` query params. |
| POST   | `/exhibitions`                                  | Create a new empty exhibition.                   |
| GET    | `/exhibitions`                                  | Retrieve all exhibitions.                        |
| GET    | `/exhibitions/{exhibitionId}`                   | Get a specific exhibition by its ID.             |
| DELETE | `/exhibitions/{exhibitionId}`                   | Delete an exhibition by its ID.                  |
| POST   | `/exhibitions/{exhibitionId}/artworks`          | Add an artwork to a specific exhibition. Requires a JSON body. |
| DELETE | `/exhibitions/{exhibitionId}/artworks`          | Remove an artwork from a specific exhibition. Requires a JSON body. |

> Note: Most exhibition-related endpoints require the `exhibitionId` path variable, and some artwork actions expect a request body in the `SavedArtworksDTO` format.

## Required Request Bodies

### 📌 POST `/exhibitions/{exhibitionId}/artworks`
### 📌 DELETE `/exhibitions/{exhibitionId}/artworks`

Both endpoints require a JSON request body in the following format:

```jsonc
{
  "artworkId": 12345,
  "apiOrigin": "Chicago_Institute" /  "Metropolitan_Museum"
}
```


## Field Descriptions

| Field      | Type   | Required | Description                                                  |
|------------|--------|----------|--------------------------------------------------------------|
| artworkId  | Long   | Yes      | The unique ID of the artwork from the source API. Must be ≥ 0. |
| apiOrigin  | String | Yes      | The name or code of the external API where the artwork came from (e.g., `"met"` or `"harvard"`). Cannot be null or blank. |


## Response Examples

`✅ GET /artwork/home?page=1&origin=met`

```jsonc
[
{
"id": 12345,
"title": "Starry Night",
"artist": "Vincent van Gogh",
"imageUrl": "https://images.metmuseum.org/12345.jpg",
"apiOrigin": "met"
},
{
"id": 12346,
"title": "Mona Lisa",
"artist": "Leonardo da Vinci",
"imageUrl": "https://images.metmuseum.org/12346.jpg",
"apiOrigin": "met"
}
]
```

`🔍 GET /artwork/home/search?query=portrait&page=1`

```jsonc
[
  {
    "id": 54321,
    "title": "Self-Portrait",
    "artist": "Rembrandt",
    "imageUrl": "https://images.harvardartmuseums.org/54321.jpg",
    "apiOrigin": "harvard"
  }
]
```


`➕ POST /exhibitions/{exhibitionId}/artworks`

```jsonc
{
  "id": 1,
  "title": "Modern Masters",
  "artworks": [
    {
      "id": 12345,
      "title": "Starry Night",
      "artist": "Vincent van Gogh",
      "imageUrl": "https://images.metmuseum.org/12345.jpg",
      "apiOrigin": "met"
    }
  ]
}
```


`🗑 DELETE /exhibitions/{exhibitionId}/artworks`

```jsonc
{
  "id": 1,
  "title": "Modern Masters",
  "artworks": []
}
```


`🆕 POST /exhibitions`

```jsonc
{
  "id": 2,
  "title": "Untitled Exhibition",
  "artworks": []
}
```


`📋 GET /exhibitions`

```jsonc
[
  {
    "id": 1,
    "title": "Modern Masters",
    "artworks": [ /* artwork objects */ ]
  },
  {
    "id": 2,
    "title": "Renaissance Focus",
    "artworks": [ /* artwork objects */ ]
  }
]
```

## Object Summary

### Artwork Object

Represents an individual piece of artwork retrieved from an external museum API.

| Field      | Type   | Description                                                                           |
|------------|--------|---------------------------------------------------------------------------------------|
| `id`       | Long   | Unique identifier of the artwork.                                                     |
| `title`    | String | Title or name of the artwork.                                                         |
| `artist`   | String | Name of the artist who created the artwork.                                           |
| `imageUrl` | String | URL to the image of the artwork.                                                      |
| `apiOrigin`| String | Identifier for the API source (e.g., `"Chicago_Institute"`, `"Metropolitan_Museum"`). |

---

### Exhibition Object

Represents a user-curated collection of artworks.

| Field       | Type           | Description                                          |
|-------------|----------------|------------------------------------------------------|
| `id`        | Long           | Unique identifier of the exhibition.                 |
| `title`     | String         | Title of the exhibition (default: `"Exhibition #"`). |
| `artworks`  | List<Artwork>  | A list of artworks included in the exhibition.       |

## Validation Rules

Validation is applied to request bodies using `jakarta.validation` annotations.

### SavedArtworksDTO

| Field       | Type   | Rule                                       | Error Message Example                          |
|-------------|--------|--------------------------------------------|------------------------------------------------|
| `artworkId` | Long   | Required, must be ≥ 0                      | `"ArtworkId cannot be null"`  <br> `"ArtworkId cannot be less than 0"` |
| `apiOrigin` | String | Required, cannot be null or blank          | `"Api Origin cannot be null"`  <br> `"Api Origin cannot be blank"`     |


## Instructions for Running App First Time

Follow these steps to set up and run the Museo backend application for the first time:

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/JV_Exhibition_Curation.git
   cd JV_Exhibition_Curation
   ```
2. **Install Java 21**

Ensure that Java Development Kit (JDK) version 21 is installed and available in your system path.

3. **Build the project with Maven**

    ```bash
   mvn clean install
   ```
   
4. **Configure Application Properties

Create or edit the ```application.properties``` file in ```src/main/resources/``` and provide necessary configurations:

### Example: PostgreSQL configuration

```jsonc
spring.datasource.url=jdbc:postgresql://localhost:5432/museo
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.datasource.driver-class-name=org.postgresql.Driver

# Server port (default: 8080)
server.port=8080
```

5. **Run the Application**

```mvn spring-boot:run```

The application will be available at:

```http://localhost:8080/api/v1/museo```

✅ You can now start sending requests to the API or connect the frontend Android app!