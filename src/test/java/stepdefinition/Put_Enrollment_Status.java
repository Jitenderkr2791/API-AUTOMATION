package stepdefinition;

import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.*;

public class Put_Enrollment_Status
{

    String childId = "312" ;
    Response res;
    Map<String, String> requestBody;
    private String baseURL;
    private Map<String, String> headers;
    String endpoint;
    private ExtentTest test;
    private String contentType="application/json";

    public Put_Enrollment_Status()
    {
        this.baseURL = ConfigReader.getProperty("baseURL");
        this.headers = ConfigReader.getHeadersFromConfig("header");
        this.test = Extent_Report_Manager.getTest();
        this.endpoint=Endpoints.CHANGE_ENROLLMERNT_STATUS;
    }

    @Given("I have a valid child ID for update the enrollment status")
    public String i_have_a_valid_child_id_for_update_the_enrollment_status()
    {
        this.childId="312";
       /* if (GlobalTokenStore.getChildId() == null || GlobalTokenStore.getChildId().isEmpty())
        {
            String generatedId = GlobalTokenStore.createChildAndGetId();  // generate and set it
            GlobalTokenStore.setChildId(generatedId);                     // optional, if not already set
            this.childId = generatedId;
            test.info("Generated new child ID: " + generatedId);
        }

        {
            this.childId = GlobalTokenStore.getChildId();  // reuse existing
            test.info("Fetched existing child ID from GlobalTokenStore: " + childId);
        }
        System.out.println("   childId: " + childId);*/
        return childId;
    }


    @Given("I prepare a request body with enrollment status {string}")
    public void iPrepareARequestBodyWithEnrollmentStatus(String status)
    {
        requestBody = new HashMap<>();
        requestBody.put("enrollment_status", status);
    }


    @When("I send a PUT request to endpoint with child id")
    public void iSendAPUTRequestToEndpointWithChildId()
    {
        String providerToken =GlobalTokenStore.getToken("provider");
        APIUtils.logRequestHeaders(test, headers);
        APIUtils.logRequestBody(test, requestBody);
        test.info("Sending POST request to endpoint: " + endpoint);
        res = given()
                .baseUri(baseURL)
                .headers(headers)
                .contentType(contentType)
                .header("Authorization", "Bearer " + providerToken)
                .body(requestBody)
                .when()
                .put(endpoint + childId);

        test.info("Received response: " + res.asString());
        System.out.println(res.asPrettyString());
        APIUtils.logResponseToExtent(res, test);
    }

    @Then("the update Enrollment status code  should be {int}")
    public void the_update_Enrollment_status_code_should_be(Integer Statuscode)
    {
        test.info("Validating response status code. Expected: " + Statuscode + ", Actual: " + res.getStatusCode());
        BaseMethods.validateStatusCode(res, Statuscode, test);
    }

    @And("the response body should contain:")
    public void theResponseBodyShouldContain(DataTable dataTable)
    {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows)
        {
            for (Map.Entry<String, String> entry : row.entrySet())
            {
                String expectedKey = entry.getKey();
                String expectedValue = entry.getValue();

                String actualValue = res.jsonPath().getString(expectedKey);
                Assert.assertEquals("Mismatch for key: " + expectedKey, expectedValue, actualValue);
            }
        }

    }
}
