package com.petStore.tests;

import com.petStore.utils.TestBase;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.given;

public class Create_A_New_Pet extends TestBase {

    @DisplayName("POST new Pet")
    @Test
    public void createNewPet() {

        Response rs =
                given()
                        .spec(petStoreReqSpec)
                        .contentType(ContentType.JSON)
                        .body(getRandomPet())
                .when()
                        .post("/pet");


        long newPetID = rs.path("id");
        String names = rs.path("name");
        System.out.println("names = " + names);
        System.out.println("newPetID = " + newPetID);

        JsonPath jp = rs.jsonPath();
        newPetID = jp.getLong("id");
        System.out.println("result is :" +newPetID);


        File image = new File("src/test/resources/CaBit.jpg");

        String str = given()
                        .spec(petStoreReqSpec)
                        .multiPart(image)
                        .pathParam("petId", newPetID)
                .when()
                        .post("/pet/{petId}/uploadImage")
                .then()
                        .spec(petStoreResSpec)
                        .statusCode(200)
                        .extract().path("message");
        System.out.println("rs = " + str);
    }
}
