package com.codechallenge.weathertracking.controller;

import com.codechallenge.weathertracking.dto.WeatherRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class WeatherApiTest {

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
        void givenInvalidDtoWithEmptyValue_whenSaveOne_thenReturnErrorWithBadRequestStatus() {
            var givenBody = new WeatherRequestDto(" ", "10001");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost").port(port).basePath("/app/weather")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.size()", is(1))
                    .body("errors", hasItem("User cannot be null or empty string or whitespace-only"))
                    .log().all(true);
        }
//
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
                    .body("size()", is(14))
                    .log().all(true);
        }
    }


    @Nested
    class FindTests {

        @Test
        void givenInvalidPostalCode_whenFindAll_thenReturnErrorWithServerErrorStatus() {
            var inValidPostalCode = 1234;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost").port(port)
                    .basePath("/app/history").queryParam("Postalcode", inValidPostalCode)
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
                    .body("errors", hasItem("You must provide either 'user' or 'postalcode', but not both or neither."))
                    .log().all(true);
        }

        @Test
        void givenBothPostalCodeAndUser_whenFindAll_thenReturnErrorWithBadRequestStatus() {
            String validPostalCode = "12345";
            String user = "user123";

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/app/history")
                    .queryParam("postalcode", validPostalCode)
                    .queryParam("user", user)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body("errors", hasItem("You must provide either 'user' or 'postalcode', but not both or neither."))
                    .log().all(true);
        }
//
        @Test
        void givenValidPostalCode_whenFindAll_thenReturnDtoWithOKStatus() {
            String validPostalCode = "94041";

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/app/history")
                    .queryParam("postalcode", validPostalCode)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                 //   .body("size()", greaterThan(0))
                    .log().all(true);
        }

    }
}
