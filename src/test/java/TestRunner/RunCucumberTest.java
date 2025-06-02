package TestRunner;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith (Cucumber.class)
@CucumberOptions(features = "src/test/resources/feature/",
		glue="stepdefinition",
		

		//dryRun = false,
		//monochrome=true,
		plugin = {"pretty",
			    "html:target/cucumber-reports.html",
			    "json:target/cucumber.json", 
			    "junit:target/cucumber-xml.xml",
			    }
		)

public class RunCucumberTest 
{

}

//	      "classpath:feature/Post_login.feature",
//  	     "classpath:feature/Get_children_List_API.feature",
//		     "classpath:feature/Get_Enrollments.feature",
//          "classpath:feature/Post_Child_Information_Step1.feature",
//           "classpath:feature/View_capacity_management.feature",
//		    "classpath:feature/Put_Enrollment_Status.feature",
//          "classpath:feature/Post_Create_Classroom.feature",
//		  "classpath:feature/Post_Mark_Absent_Graduate.feature"
//		  "classpath:feature/enroll_Regular_Dropin.feature"
// "classpath:feature/Happy_Path_Flow.feature"