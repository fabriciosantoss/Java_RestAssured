package core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest implements Const {

    @BeforeAll
    public static void setUp(){

        RestAssured.baseURI = BASE_URI;

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(APP_CONTENT_TYPE);
        RestAssured.requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static String generateConsentToken(){
        JSONObject payload = new JSONObject();
        payload.put("scope", "consents");
        payload.put("client_id", "client1");

        JSONObject header = new JSONObject();
        header.put("alg", "none");
        header.put("typ", "JWT");

        String payloadBase64 = new String(Base64.encodeBase64(payload.toString().getBytes()));
        String headerBase64 = new String(Base64.encodeBase64(header.toString().getBytes()));

        return headerBase64 + "." + payloadBase64 + ".";
    }

    public static String generateAccountToken(String consentID){
        JSONObject payloadAccount = new JSONObject();
        payloadAccount.put("scope", "accounts consent:" + consentID);
        payloadAccount.put("client_id", "client1");

        JSONObject header = new JSONObject();
        header.put("alg", "none");
        header.put("typ", "JWT");

        String payloadBase64 = new String(Base64.encodeBase64(payloadAccount.toString().getBytes()));
        String headerBase64 = new String(Base64.encodeBase64(header.toString().getBytes()));

        return headerBase64 + "." + payloadBase64 + ".";
    }
}
