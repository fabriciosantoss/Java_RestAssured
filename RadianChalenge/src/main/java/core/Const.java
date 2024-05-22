package core;
import io.restassured.http.ContentType;

public interface Const {

        String BASE_URI = "http://localhost:8080/test-api/";
        String CONSENTS = "consents/v1/consents/";
        String ACCOUNTS = "account/v1/accounts/";
        String ACCOUNT = "account/v1/account/";
        ContentType APP_CONTENT_TYPE = ContentType.JSON;

        Long MAX_TIMEOUT = 5000L;
}
