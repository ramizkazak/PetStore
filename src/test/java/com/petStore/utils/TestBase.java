package com.petStore.utils;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class TestBase {

    public static String petStoreToken;
    public static RequestSpecification petStoreReqSpec;
    public static ResponseSpecification petStoreResSpec;
    public static String uri = ConfigurationReader.getProperty("url");
    public static String username = ConfigurationReader.getProperty("username");
    public static String password = ConfigurationReader.getProperty("password");


    @BeforeAll
    public static void init() {
        baseURI = uri;
        petStoreToken = login();
        petStoreReqSpec = given()
                         .auth().basic(username, password);
        petStoreResSpec = expect().statusCode(200)
                         .contentType(ContentType.JSON);
    }

    public static Map<String, Object> getRandomPet() {

        Faker faker = new Faker();
        String[] list = {"available", "pending", "sold"};

        Map<String, Object> newPet = new HashMap<>();
        newPet.put("name", faker.animal().name());
        newPet.put("status", faker.options().option(list));

        return newPet;
    }

    public static String login() {

        return given()
                     .contentType(ContentType.JSON)
                     .queryParam("username", username)
                     .queryParam("password", password)
                .when()
                     .get("/user/login")
                     .path("token");
    }

    @AfterAll
    public static void cleanUp() {
        reset();
    }

}
