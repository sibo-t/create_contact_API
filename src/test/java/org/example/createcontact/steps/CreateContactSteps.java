package org.example.createcontact.steps;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateContactSteps {
    private final RequestSpecBuilder requestSpec;
    Map<String, String> contact;
    private String token;
    private Response response;
    private Map<String, String> createdContact;

    public CreateContactSteps() {
        requestSpec = new RequestSpecBuilder().setBaseUri("https://thinking-tester-contact-list.herokuapp.com/");
        contact = new HashMap<>();
    }

    @Given("the user is registered to use the service")
    public void theUserIsRegisteredToUseTheSerice() {
        String randomEmail = UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String request = String.format("{\n" +
                "    \"firstName\": \"Test\",\n" +
                "    \"lastName\": \"User\",\n" +
                "    \"email\": \"%s\",\n" +
                "    \"password\": \"myPassword\"\n" +
                "}", randomEmail);
        response = given().spec(requestSpec.build()).contentType("application/json").body(request).post("users");
        token = response.getBody().jsonPath().getString("token");
    }

    @Given("the contact has a first name {string}, a last name {string} and birthdate {string}")
    public void theContactHasAFirstNameALastNameAndBirthdate(String name, String surname, String birthdate) {
        contact.put("firstName", name);
        contact.put("lastName", surname);
        contact.put("birthdate", birthdate);
    }

    @Given("the contact has an email address {string} and cellphone number {string}")
    public void theContactHasAnEmailAddressAndCellphoneNumber(String email, String cellNumber) {
        if(email.equals("auto-generated")){
            email = UUID.randomUUID().toString().substring(0, 8) + "@contact.com";
        }

        if(cellNumber.equals("auto-generated")){
            cellNumber = "0" + (int)(Math.random() * 1000000000);
        }

        contact.put("email", email);
        contact.put("phone", cellNumber);
    }

    @Given("the contact has a physical address of {string}, {string}, {string} {string} {string} {string}")
    public void theContactHasAPhysicalAddressOf(String street1, String street2, String city, String state, String postal, String country) {
        contact.put("street1", street1);
        contact.put("street2", street2);
        contact.put("city", city);
        contact.put("stateProvince", state);
        contact.put("postalCode", postal);
        contact.put("country", country);
    }

    @When("the user adds a new contact")
    public void theUserAddsANewContact() {
        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String gsonString = gson.toJson(contact,gsonType);

        response = given().spec(requestSpec.build())
                .contentType("application/json")
                .header("Authorization", "Bearer "+token)
                .body(gsonString)
                .post("contacts");

        createdContact = response.getBody().jsonPath().getMap(".");
    }

    @Then("the new contact is in a list of contacts")
    public void theNewContactIsInAListOfContacts() {
        response = given().spec(requestSpec.build())
                .header("Authorization", "Bearer "+token)
                .get("contacts");

        List<Map<String, String>> createdContacts = response.getBody().jsonPath().getList(".");
        boolean isContactInList = createdContacts.contains(createdContact);
        assertTrue(isContactInList);
    }
}