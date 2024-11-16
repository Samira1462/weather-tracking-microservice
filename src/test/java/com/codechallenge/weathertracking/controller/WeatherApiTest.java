package com.codechallenge.weathertracking.controller;

import com.codechallenge.weathertracking.dto.WeatherRequestDto;
import com.codechallenge.weathertracking.service.WeatherService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class WeatherApiTest {

    @Inject
    WeatherService testAssistant;


    @LocalServerPort
    int port;

    @Nested
    class SaveTests {

        @Test
        void givenDto_whenSaveOne_thenReturnIdWithCreatedStatus() {
            var givenBody = new WeatherRequestDto("john_doe", "10001");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost").port(port).basePath("/app/weather")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", containsString("/app/weather"))
                    .body("", notNullValue())
                    .log().all(true);
        }

        @Test
        void givenInvalidDto_whenSaveOne_thenReturnErrorWithBadRequestStatus() {
            var givenBody = new WeatherRequestDto("john_doe", "123");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost").port(port).basePath("/app/weather")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.size()", is(1))
                    .body("errors", hasItem("Invalid US postal code"))
                    .log().all(true);
        }

        @Test
        void givenValidDto_whenSaveOne_thenReturnDtoAndOKRequestStatus() {
            var givenBody = new WeatherRequestDto("john_doe", "94041");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost").port(port).basePath("/app/weather")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", containsString("/app/weather"))
                    .body("", notNullValue())
                    .log().all(true);
        }
    }


    @Nested
    class FindTests {

        @Test
        void givenInvalidPostalCode_whenFindAll_thenReturnErrorWithServerErrorStatus() {
            var givenPostalCode = 1234;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/app/history").queryParam("PostalCode", givenPostalCode)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body("errors.size()", is(1))
                    .log().all(true);
        }

        @Test
        void givenNoQueryParams_whenFindAll_thenReturnErrorWithServerErrorStatus() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/app/history")
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body("errors", hasItem("You must provide either 'user' or 'postal code', but not both or neither."))
                    .log().all(true);
        }

        @Test
        void givenBothPostalCodeAndUser_whenFindAll_thenReturnErrorWithBadRequestStatus() {
            var givenPostalCode = "12345";
            var user = "user";

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/app/history")
                    .queryParam("postalCode", givenPostalCode)
                    .queryParam("user", user)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body("errors", hasItem("You must provide either 'user' or 'postal code', but not both or neither."))
                    .log().all(true);
        }

        @Test
        void givenValidPostalCode_whenFindAll_thenReturnDtoWithOKStatus() {
            var givenPostalCode = "94041";
            testAssistant.save(new WeatherRequestDto("john_doe", givenPostalCode));

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/app/history")
                    .queryParam("postalCode", givenPostalCode)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(1))
                    .log().all(true);
        }

    }
}
