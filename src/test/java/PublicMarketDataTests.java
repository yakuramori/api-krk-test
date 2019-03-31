import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


class PublicMarketDataTests {

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://api.kraken.com/0/public";
    }

    @Test
    @DisplayName("Verify Time endpoint")
    void verifyTimeEndpoint() {
        long startTime = System.currentTimeMillis() / 1000 - 1;// make sure even rounded time is before actual
        Response response = given()
                .accept(ContentType.ANY)
                .get("/Time")
                .then()
                .statusCode(200)
                .and()
                .body("error", Matchers.iterableWithSize(0))
                .extract().response();
        TimeResponse timeResponse = response.as(TimeResponse.class);
        assertThat(timeResponse.error).isEmpty();
        assertThat(timeResponse.result).isNotNull();
        assertThat(timeResponse.result.unixTime).isAtLeast(startTime);
    }

    @Test
    @DisplayName("Verify Time Schema")
    void verifyTimeSchema() {
        given()
                .get("/Time")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("time-schema.json"));
    }

    @Test
    @DisplayName("Get tradable asset pairs")
    void verifyGetPairs() {
        given()
                .get("/AssetPairs")
                .then()
                .statusCode(200)
                .and()
                .body("error", Matchers.iterableWithSize(0));
    }

    @Test
    @DisplayName("Verify ticker info parsing with wrapper")
    void verifyTickerParse() {
        TickerResponse tickerResponse = given()
                .accept(ContentType.JSON)
                .param("pair", "ADAETH")
                .get("/Ticker")
                .then()
                .statusCode(200)
                .and()
                .body("error", Matchers.iterableWithSize(0))
                .extract().response().as(TickerResponse.class);
        assertThat(tickerResponse.error).isEmpty();
        assertThat(tickerResponse.result).isNotNull();
        assertThat(tickerResponse.result.pairName).isNotNull();
        assertThat(tickerResponse.result.pairName.o.doubleValue()).isAtMost(0.99999);
    }

    @DisplayName("Get ticker information")
    @ParameterizedTest
    @ValueSource(strings = {"ADACAD", "ADAETH"})
    void verifyGetTicker(String ticker) {
        given()
                .accept(ContentType.JSON)
                .param("pair", ticker)
                .get("/Ticker")
                .then()
                .statusCode(200)
                .and()
                .body("error", Matchers.iterableWithSize(0))
                .and().body("result." + ticker, Matchers.anything())// make sure pair name is there
                .log().body();// log to to visually verify
    }

    @Test
    @DisplayName("Try get ticker information without ticker")
    void verifyGetTickerError() {
        given()
                .accept(ContentType.JSON)
                .get("/Ticker")
                .then()
                .statusCode(200)
                .body("error", Matchers.hasItem("EGeneral:Invalid arguments"));
    }
}
