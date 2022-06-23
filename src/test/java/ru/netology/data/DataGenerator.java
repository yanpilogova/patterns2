package ru.netology.data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    @Data
    @RequiredArgsConstructor
    public static class RegistrationData {
        private final String login;
        private final String password;
        private final String status;

    }
    public static class Registration {

        public static RegistrationData getUser(String status) {
            return new RegistrationData(getRandomLogin(), getRandomPassword(), status);
        }

        public static String getRandomLogin() {
            Faker faker = new Faker();
            return faker.name().username();
        }

        public static String getRandomPassword() {
            Faker faker = new Faker();
            return faker.internet().password();
        }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    private static void sendRequest(RegistrationData user) {
        Gson gson =new Gson();
        String userBody = gson.toJson(user);
        given()
                .spec(requestSpec)
                .body(userBody)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

        public static RegistrationData getRegisteredUser(String status) {
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }


}