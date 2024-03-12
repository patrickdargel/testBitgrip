package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UsersTests {

    private static final Properties properties = new Properties();

    static String usersPath;
    static String usersUpdatePath;
    static String idJsonPath;
    static String totalJsonPath;

    static int defaultPage;
    static int defaultPerPage;
    static int defaultStatusCode;


    @BeforeAll
    public static void setup() {
        try {
            properties.load(new FileInputStream("src/test/resources/apiResources.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RestAssured.baseURI = properties.getProperty("baseURI");
        usersPath = properties.getProperty("usersPath");
        usersUpdatePath = properties.getProperty("usersUpdatePath");
        idJsonPath = properties.getProperty("idJsonPath");
        totalJsonPath = properties.getProperty("totalJsonPath");

        defaultPage = Integer.parseInt(properties.getProperty("defaultPage"));
        defaultPerPage = Integer.parseInt(properties.getProperty("defaultPerPage"));
        defaultStatusCode = Integer.parseInt(properties.getProperty("defaultStatusCode"));
    }

    @Test
    public void validateResponseSchema() {
        given()
            .header("Accept", "application/json")
            .queryParam("page", defaultPage)
            .queryParam("per_page", defaultPerPage)
        .when()
            .get(usersPath)
        .then()
            .assertThat()
            .statusCode(defaultStatusCode)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(properties.getProperty("usersJsonSchema")));
    }
    
    @Test
    public void validateDifferentPageNumbers() {
        Response response =
            given()
                .queryParam("page", 1)
                .queryParam("per_page", 2)
            .when()
                .get(usersPath)
            .then()
                .statusCode(defaultStatusCode)
            .extract().response();

        int maxTotalUsers = response
                .jsonPath()
                .getInt(totalJsonPath);

        for (int incrementPerPage = 1; incrementPerPage <= 20; incrementPerPage++) {
            given()
                .header("Accept", "application/json")
                .queryParam("page", defaultPage)
                .queryParam("per_page", incrementPerPage)
            .when()
                .get(usersPath)
            .then()
                .statusCode(defaultStatusCode)
                .body("page", equalTo(defaultPage))
                .body("per_page", equalTo(incrementPerPage))
                .body("data", hasSize(Math.min(incrementPerPage, maxTotalUsers)));
        }
    }

    //This test fails, because the API does NOT respond correctly.
    //The API will respond with a default Pagination of "page"=1, "per_page"=6
    //If this is expected behavior, this Test will be replaced by 'validateResponseWithWrongPaginationAsExpected()'
    //That would be a Jira Bug Ticket from my point of view
    @Test
    public void validateResponseWithWrongPagination() {
        given()
            .header("Accept", "application/json")
            .queryParam("page", "z")
            .queryParam("per_page", "a")
        .when()
            .get(usersPath)
        .then()
            .statusCode(400);
    }

    //This Test replaces 'validateResponseWithWrongPagination()' if the behavior is expected
    @Test
    public void validateResponseWithWrongPaginationAsExpected() {
        given()
            .header("Accept", "application/json")
            .queryParam("page", "z")
            .queryParam("per_page", "a")
        .when()
            .get(usersPath)
        .then()
            .statusCode(defaultStatusCode)
            .body("page", equalTo(defaultPage))
            .body("per_page", equalTo(defaultPerPage))
            .body("data", hasSize(defaultPerPage));
    }

    @Test
    public void validateSameDataOnFirstPage() {
        Response responsePage1 =
            given()
                .queryParam("page", 1)
                .queryParam("per_page", 2)
            .when()
                .get(usersPath)
            .then()
                .statusCode(defaultStatusCode)
                .body("page", equalTo(1))
                .body("per_page", equalTo(2))
                .body("data", hasSize(2))
            .extract().response();

        List<Integer> idListOfPage1 = responsePage1
                .jsonPath()
                .getList(idJsonPath);

        Response responsePage2 =
            given()
                .queryParam("page", 1)
                .queryParam("per_page", 2)
            .when()
                .get(usersPath)
                .then()
            .statusCode(defaultStatusCode)
                .body("page", equalTo(1))
                .body("per_page", equalTo(2))
                .body("data", hasSize(2))
            .extract().response();

        List<Integer> idListOfPage2 = responsePage2
                .jsonPath()
                .getList(idJsonPath);

        assertEquals(idListOfPage1, idListOfPage2, "The same call does not provide the same data set");
    }

    @Test
    public void validateDifferentPagesContainsDifferentData() {
        Response responsePage1 =
            given()
                .queryParam("page", 1)
                .queryParam("per_page", 2)
            .when()
                .get(usersPath)
            .then()
                .statusCode(defaultStatusCode)
                .body("page", equalTo(1))
                .body("per_page", equalTo(2))
                .body("data", hasSize(2))
            .extract().response();

        List<Integer> idListOfPage1 = responsePage1
                .jsonPath()
                .getList(idJsonPath);

        assertEquals(2, idListOfPage1.size(), "The first Page does not contain the correct number of elements.");

        Response responsePage2 =
            given()
                .queryParam("page", 2)
                .queryParam("per_page", 2)
            .when()
                .get(usersPath)
            .then()
                .statusCode(defaultStatusCode)
                .body("page", equalTo(2))
                .body("per_page", equalTo(2))
                .body("data", hasSize(2))
            .extract().response();

        List<Integer> idListOfPage2 = responsePage2
                .jsonPath()
                .getList(idJsonPath);

        assertEquals(2, idListOfPage2.size(), "The second Page does not contain the correct number of elements.");

        // Ensure no IDs are repeated between page 1 and page 2
        Set<Integer> uniqueIds = new HashSet<>(idListOfPage1);
        uniqueIds.addAll(idListOfPage2);
        assertEquals(uniqueIds.size(), idListOfPage1.size() + idListOfPage2.size(), "Some IDs are repeated across pages.");
    }

    @Test
    public void usersFlowWithMoreThanOneAPI() {
        String userId = "2";

        String updatePayload = "{ \"email\": \"neo@zion.net\", \"first_name\": \"Thomas\", \"last_name\": \"Anderson\", \"avatar\": \"https://upload.wikimedia.org/wikipedia/en/thumb/c/c6/NeoTheMatrix.jpg/220px-NeoTheMatrix.jpg\" }";
        given()
            .contentType(ContentType.JSON)
            .body(updatePayload)
        .when()
            .put(usersUpdatePath, userId)
        .then()
            .statusCode(defaultStatusCode)
            .body("email", equalTo("neo@zion.net"))
            .body("first_name", equalTo("Thomas"))
            .body("last_name", equalTo("Anderson"))
            .body("avatar", equalTo("https://upload.wikimedia.org/wikipedia/en/thumb/c/c6/NeoTheMatrix.jpg/220px-NeoTheMatrix.jpg"));

        // Delete the same user
        given()
        .when()
            .delete(usersUpdatePath, userId)
        .then()
            .statusCode(204); // Assuming the API returns 204 No Content on successful deletion
    }
}
