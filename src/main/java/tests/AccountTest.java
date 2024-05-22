package tests;

import constants.ExpiryDate;
import constants.Permission;
import constants.Status;
import core.BaseTest;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static constants.ExpiryDate.*;
import static constants.Permission.ACCOUNTS_READ;
import static constants.Permission.CREDIT_CARD_READ;
import static constants.Status.AUTHORISED;
import static constants.Status.REJECTED;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AccountTest extends BaseTest {

    private String generateConsent(Permission permission, ExpiryDate expiryDate, Status status) {
        Map<String, String> body = new HashMap<>();
        body.put("permissions", permission.getName());
        body.put("expirationDateTime",expiryDate.getName());

        Map<String, Map<String, String>> data = new HashMap<>();
        data.put("data", body);

        Response response = given()
                .body(data)
                .header("Authorization","Bearer " + generateConsentToken())
                .when()
                .post(CONSENTS);


        String consentID = response.jsonPath().getString("data.consentId");

        Map<String, String> bodyUpdate = new HashMap<>();
        Map<String, Map<String, String>> dataUpdate = new HashMap<>();
        bodyUpdate.put("status", status.getName());
        dataUpdate.put("data", bodyUpdate);

        Response responseUpdate = given()
                .body(dataUpdate)
                .header("Authorization","Bearer " + generateConsentToken())
                .when()
                .put(CONSENTS + consentID);


        return consentID;
    }

    @Test
    public void getAccounts() {
        String token = "Bearer " + generateAccountToken(generateConsent(ACCOUNTS_READ, VALID_DATE, AUTHORISED));
        Response response = given()
                .log().all()
                .header("Authorization", token)
                .when()
                .get(ACCOUNTS);
        response.then().log().all();
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("payloads/AccountsList.json")).statusCode(200);
        response.then().body("data.bank", hasItem("Itau")).extract().response();
    }

    @Test
    public void getAccountsWithoutConsent() {
        String token = "Bearer " + generateAccountToken("test123");
        Response response = given()
                .log().all()
                .header("Authorization", token)
                .when()
                .get(ACCOUNTS);
        response.then().log().all();
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("payloads/AccountsWithoutConsent.json")).statusCode(403);
        response.then().body("_embedded.errors[0].message", equalTo("Consent Id not present on the request"))
                .extract()
                .response();
    }

    @Test
    public void getAccountsWithConsentExpiredDate() {
        String token = generateAccountToken(generateConsent(ACCOUNTS_READ, EXPIRED_DATE, AUTHORISED));
        Response response = given()
                .log().all()
                .header("Authorization","Bearer " + token)
                .when()
                .get(ACCOUNTS);
        response.then().log().all();
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("payloads/AccountsWithExpiredDate.json")).statusCode(403);
        response.then().body("_embedded.errors[0].message", containsString("is not in the right status"))
                .extract()
                .response();
    }

    @Test
    public void getAccountsWithoutPermission() {
        String token = generateAccountToken(generateConsent(CREDIT_CARD_READ, EXPIRED_DATE, AUTHORISED));
        Response response = given()
                .log().all()
                .header("Authorization","Bearer " + token)
                .when()
                .get(BASE_URI+ ACCOUNTS);
        response.then().log().all();
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("payloads/AccountsWithoutPermission.json"))
                .statusCode(403);
        response.then().body("_embedded.errors[0].message", containsString("doesn't have right permissions"))
                .extract()
                .response();
    }

    @Test
    public void getAccountsWithInvalidDate() {
        String token = generateAccountToken(generateConsent(CREDIT_CARD_READ, INVALID_DATE, AUTHORISED));
        Response response = given()
                .log().all()
                .header("Authorization","Bearer " + token)
                .when()
                .get(BASE_URI+ ACCOUNTS);
        response.then().log().all();
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("payloads/AccountsWithInvalidDate.json"))
                .statusCode(403);
        response.then().body("_embedded.errors[0].message", equalTo("Consent Id not present on the request"))
                .extract()
                .response();
    }

    @Test
    public void getAccount() {
        String token = generateAccountToken(generateConsent(ACCOUNTS_READ, VALID_DATE, AUTHORISED));
        Response response = given()
                .log().all()
                .header("Authorization","Bearer " + token)
                .when()
                .get(BASE_URI+ ACCOUNTS);
        String accountID = response.jsonPath().getString("data.id[0]");
        response.then().log().all();

        Response responseAccount = given()
                .log().all()
                .header("Authorization","Bearer " + token)
                .when()
                .get(BASE_URI+ ACCOUNT + accountID);
        responseAccount.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("payloads/AccountInfo.json")).log().all().assertThat()
                .statusCode(200);
        response.then().body("data.bank[0]", is("Nubank")).extract().response();
    }

    //bug, is returning 500, because not received the consentID. should be displayed a message about the null information and return the status code 404
    @Test
    public void getAccountWithoutConsent() {
        String token = generateAccountToken(generateConsent(ACCOUNTS_READ, VALID_DATE, REJECTED));
        Response response = given()
                .log().all()
                .header("Authorization","Bearer " + token)
                .when()
                .get(BASE_URI+ ACCOUNTS);
        String accountID = response.jsonPath().getString("data.id[0]");
        response.then().log().all();

        Response responseAccount = given()
                .log().all()
                .header("Authorization","Bearer " + token)
                .when()
                .get(BASE_URI+ ACCOUNT + accountID);
        responseAccount.then().log().all().assertThat().statusCode(404);
    }
    //bug, is returning 500, because not received the consentID. should be displayed a message about the null information and return the status code 404
    @Test
    public void getAccountWithExpiredDate() {
        String token = generateAccountToken(generateConsent(ACCOUNTS_READ, EXPIRED_DATE, AUTHORISED));
        Response response = given()
                .log().all()
                .header("Authorization","Bearer " + token)
                .when()
                .get(BASE_URI+ ACCOUNTS);

        String accountID = response.jsonPath().getString("data.id[0]");
        response.then().log().all();

        Response responseAccount = given().log().all()
                .header("Authorization","Bearer " + token)
                .when()
                .get(BASE_URI+ ACCOUNT + accountID);

        responseAccount.then().log().all().assertThat().statusCode(403);
    }
}

