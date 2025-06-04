package Utils;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import stepdefinition.Post_Login;

public class BaseMethods 
{
	static Map<String, String> tokens = new HashMap<>();
	static Post_Login login = new Post_Login();
	public static String parentToken;
	public static String providerToken;
	
    public static void validateStatusCode(Response res, int expectedCode, ExtentTest test)
    {
        int actualCode = res.getStatusCode();
        test.info("Asserting status code. Expected: " + expectedCode + ", Actual: " + actualCode);
        Assert.assertEquals("Unexpected status code", expectedCode, actualCode);
    }
    
	public static String formatBirthdate(String dateString) 
	{
	    if (dateString != null && !dateString.trim().isEmpty())
	    {
	        try {
	            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
	            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yy");
	            Date parsedDate = inputFormat.parse(dateString.trim());
	            return outputFormat.format(parsedDate);
	        	}
	        catch (Exception e) 
	        {
	            throw new RuntimeException("Birthdate format is invalid: " + dateString, e);
	        }
	    } 
	    else 
	    {
	        throw new IllegalArgumentException("birthdate field is missing or empty");
	    }
	 }
	
	public static String decodeJWT(String jwt) 
	{
	    String[] split = jwt.split("\\.");
	    return new String(Base64.getUrlDecoder().decode(split[1]));
	}
	
	public static void providerLogin()
	{ 	 
		 login.the_provides_email_and_password("Provider", Endpoints.provider_email, Endpoints.provider_password);
		 tokens.put("ProviderToken", login.the_sends_a_post_request_to_the_login_endpoint("Provider"));
		 ConfigReader.writeMultipleProperties(tokens);
		 ConfigReader.waitAndReloadConfig(3000);
	}
	
	public static  void parentLogin()
	{
		 login.the_provides_email_and_password("Parent", Endpoints.parent_email, Endpoints.parent_password);
		 tokens.put("ParentToken", login.the_sends_a_post_request_to_the_login_endpoint("Parent"));
		 ConfigReader.writeMultipleProperties(tokens);
		 ConfigReader.waitAndReloadConfig(3000);
	}

	public static Map<String, String> getDefaultHeaders()
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Accept", "*/*");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Cache-Control", "no-cache");
		headers.put("Connection", "keep-alive");
		headers.put("Content-Type", "application/json");
		headers.put("User-Agent", "PostmanRuntime/7.44.0");
		return headers;
	}
	
	 public static String getParentToken() 
	 {	parentToken = ConfigReader.getProperty("ParentToken");
	    if (parentToken == null || parentToken.trim().isEmpty() || tokenExpired(parentToken)) 
	        {
	            BaseMethods.parentLogin();  // this should refresh and set new token
	            parentToken = ConfigReader.getProperty("ParentToken");
	        }
	        return parentToken;
	    }
	 
	 public static String getProviderToken() 
	 {  providerToken = ConfigReader.getProperty("ProviderToken");
	    if (providerToken == null || providerToken.trim().isEmpty() || tokenExpired(providerToken)) 
	        {
	            BaseMethods.providerLogin();  
	            providerToken = ConfigReader.getProperty("ProviderToken");
	        }
	        return providerToken;
	    }

	 private static boolean tokenExpired(String token)
	    {   try {
	    	      
	    	        String[] parts = token.split("\\.");				  // Split JWT into header, payload, and signature
	    	        if (parts.length < 2) return true; 					 // Invalid token
	    	        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));		  // Decode the payload part (2nd part)
	    	        JSONObject json = new JSONObject(payload);									 // Parse payload as JSON
	    	       
	    	        long exp = json.getLong("exp");												 // Read the expiry time (in seconds)
	    	        
	    	        long currentTime = System.currentTimeMillis() / 1000;						 // Get current time in seconds

	    	        return currentTime > exp;											   // Return true if token is expired
	    	    } 
	    	  catch (Exception e) 
	    	  {
	    	        return true;							                             // If token is invalid or parsing fails, treat it as expired
	    	    }
	     }

}