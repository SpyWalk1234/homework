package com.wandoo.homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wandoo.homework.dto.PaymentDTO;
import com.wandoo.homework.model.CurrentTransaction;
import com.wandoo.homework.model.CurrentUser;
import com.wandoo.homework.model.Message;
import com.wandoo.homework.model.RegisterUserResponse;
import com.wandoo.homework.utils.JsonConverter;
import com.wandoo.homework.utils.TestFixtures;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions {

    private final CurrentUser currentUser = new CurrentUser();
    private final List<CurrentTransaction> currentTransactions = new ArrayList<>();
    SessionFilter sessionFilter = new SessionFilter(); //for using the same session for all Rest-Assured requests in order to skip authentication.

    @BeforeEach
    public void setUp() {


        RestAssured.baseURI = "http://localhost:8080";
//        RestAssured.sessionId = "333";
        RestAssured.config()
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, str) -> new ObjectMapper())); //Convert object to JSON. Probably, request can be stored in JSON file but prefer to use objects because of better structure preview.
    }

    @Given("Register new client")
    public void registerClient() {
        val request = TestFixtures.prepareRegistrationRequest();

        val responseBody = given()
                .contentType(ContentType.JSON)
                .filter(sessionFilter)
                .body(request)
                .when()
                .post("/public/sign-up")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(RegisterUserResponse.class);

        val responseUser = responseBody.getUser();
        assertThat(responseUser).isNotNull();
        assertThat(responseUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(responseUser.getFirstName()).isNull();
        assertThat(responseUser.getSurname()).isNull();
        assertThat(responseUser.getId()).isNotNull();
        assertThat(responseUser.getPersonalId()).isNull();

        val responseMessage = responseBody.getMessage();
        assertThat(responseMessage).isNotNull();
        assertThat(responseMessage.getStatus()).isEqualTo("SUCCESS");
        assertThat(responseMessage.getMessage()).isEqualTo("User registered");

        currentUser.setId(responseUser.getId());
        currentUser.setEmail(request.getEmail());
        currentUser.setPassword(request.getPassword());
    }

    @And("Update personal data")
    public void personalData() {
        val request = TestFixtures.preparePersonalDataRequest(currentUser.getId());

        val responseBody = given()
//                .auth()
//                .basic(currentUser.getEmail(), currentUser.getPassword())
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/personal-data")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(RegisterUserResponse.class);

        val responseUser = responseBody.getUser();
        assertThat(responseUser).isNotNull();
        assertThat(responseUser.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(responseUser.getSurname()).isEqualTo(request.getSurname());
        assertThat(responseUser.getPersonalId()).isEqualTo(request.getPersonalId());

        currentUser.setFullName(request.getFirstName() + " " + request.getSurname());
        currentUser.setPersonalId(request.getPersonalId());
    }

    @And("Add {string} EUR funds to a client's wallet")
    public void addFunds(String amount) {
        val request = TestFixtures.prepareTransactionRequest(currentUser, amount);

        val response = given()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/add-funds")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertThat(response).contains("Payment imported, id: ");
        val transaction = TestFixtures.prepareCurrentTransaction(request);
        currentTransactions.add(transaction);

    }

    @And("Check the client's balance is {string} EUR")
    public void checkBalance(String amount) {

        val response = given()
                .filter(sessionFilter)
                .when()
                .get("/api/balance")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();

        assertThat(response).contains(amount);
    }

    @And("Check client's payments")
    public void checkPayments() {

        val response = given()
                .filter(sessionFilter)
                .when()
                .get("/api/payments")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", PaymentDTO.class);

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getTransactionType()).isEqualTo(currentTransactions.get(0).getTransactionType());
        assertThat(response.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(5).setScale(1, RoundingMode.UP));

        val rawResponse = response.get(0).getRawResponse();
        assertThat(JsonConverter.getTree(rawResponse)).isEqualTo(JsonConverter.getTree(currentTransactions.get(0).getRawResponse()));

    }

    @And("Fail to register invalid user")
    public void registerInvalidUser() {
        val request = TestFixtures.prepareInvalidRegistrationRequest();

        val responseBody = given()
                .contentType(ContentType.JSON)
                .filter(sessionFilter)
                .body(request)
                .when()
                .post("/public/sign-up")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @And("Fail to register existing client")
    public void registerExistingClient() {
        val request = TestFixtures.prepareRegistrationRequest();
        request.setEmail(currentUser.getEmail());

        val validationMessage = given()
                .contentType(ContentType.JSON)
                .filter(sessionFilter)
                .body(request)
                .when()
                .post("/public/sign-up")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(Message.class);

        assertThat(validationMessage).isNotNull();
        assertThat(validationMessage.getStatus()).isEqualTo("fail");
        assertThat(validationMessage.getMessage()).isEqualTo("Email already exists.");

    }

}
