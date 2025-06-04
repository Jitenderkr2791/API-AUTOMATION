package stepdefinition;
import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.*;
import static io.restassured.RestAssured.*;

public class Post_Mark_Absent_Graduate
{
    private String providerToken = BaseMethods.getProviderToken();
    private Map<String, String> headers = BaseMethods.getDefaultHeaders();
    private Response response;
    private Map<String, Object> requestBody;
    private ExtentTest test = Extent_Report_Manager.getTest();;
    Payload Pl = new Payload();

    @When("I send a POST request to providers\\/enrollments\\/mark-absent with {int}")
    public void i_send_a_post_request_to_providers_enrollments_mark_absent_with(Integer enrollmentid)
	    {	
			requestBody = Pl.MarkingAbsence(enrollmentid);
			test.log(Status.INFO, "Sending POST request to: " + Endpoints.baseURL +" "+Endpoints.mark_absent);
			test.log(Status.INFO, "Prepared request body: " + requestBody.toString());
			APIUtils.logRequestHeaders(test, headers);
			System.out.println(requestBody);
	        response = given()
	                .baseUri(Endpoints.baseURL)
	                .headers(headers)
	                .header("Authorization", "Bearer " + providerToken)
	                .body(requestBody)
	                .when()
	                .post(Endpoints.mark_absent);

	        APIUtils.logResponseToExtent(response, test);
	        System.out.println("API Response:\n" + response.asPrettyString());
	    }

	    @Then("the response status code should be {int}")
	    public void theResponseStatusCodeShouldBe(int expectedCode)
		{
	        int actualCode = response.getStatusCode();
	        Assert.assertEquals("Unexpected status code!", expectedCode, actualCode);
	        test.pass("Validated status code: " + actualCode);
	    }

	    @Then("the response body should contain {string} as true")
	    public void theResponseBodyShouldContainKeyAsTrue(String key)
		{
	    	    JsonPath jsonPath = response.jsonPath();
	    	    Boolean actualValue = jsonPath.getBoolean(key);
	    	    Assert.assertNotNull("Expected key '" + key + "' not found in response.", actualValue);
	    	    Assert.assertTrue("Expected value of '" + key + "' to be true but was false.", actualValue);
	    }

	    @Then("the response body should contain the keyvalue {string}")
	    public void theResponseBodyShouldContainKeyvalue(String key)
		{
	        Object value = response.jsonPath().get(key);
	        Assert.assertNotNull("Expected response to contain key: " + key, value);
	        test.pass("Response contains key: " + key + " with value: " + value);
	    }

	    @When("I send a POST request to providers\\/enrollments\\/mark-graduation with {int}")
	    public void i_send_a_post_request_to_providers_enrollments_mark_graduation_with(Integer enrollmentid)
	    {
			requestBody = Pl.GraduateChild(enrollmentid);
			test.log(Status.INFO, "Sending POST request to: " + Endpoints.baseURL +" "+Endpoints.child_Graduate);
			test.log(Status.INFO, "Prepared request body: " + requestBody.toString());
			APIUtils.logRequestHeaders(test, headers);
			System.out.println(requestBody);
			System.out.println(Endpoints.baseURL+Endpoints.child_Graduate);
	        response = given()
	                .baseUri(Endpoints.baseURL)
	                .headers(headers)
	                .header("Authorization", "Bearer " + providerToken)
	                .body(requestBody)
	                .when()
	                .post(Endpoints.child_Graduate);

	        APIUtils.logResponseToExtent(response, test);
	        System.out.println("API Response:\n" + response.asPrettyString());
	    }
}