import static io.restassured.RestAssured.given;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;

import io.restassured.path.json.JsonPath;

import io.restassured.response.Response;

import io.restassured.response.ResponseBody;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

public class test1 {

	public static void main(String[] args) throws InterruptedException {
		
		String[] courseTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};
		
	    String code = "4%2F3gFbSCS6kLqjrZFuvQqBOyHszTh0x-hap4J-66-XAQvuppgNSrDxy3-FTTgvBygcXVE5b9YKOLSlUD6Rma9lYHI";
	    
	// code keeps on changing and can be generated everytime using 
	 //https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php
	 			
		String accessTokenResponse = given()
				.urlEncodingEnabled(false)
				.queryParams("code", code)
			.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
			.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
			.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
			.queryParams("grant_type","authorization_code")
			.when().log().all()
			.post("https://www.googleapis.com/oauth2/v4/token")
			.asString();
		
		System.out.println(accessTokenResponse);
		JsonPath jp = new JsonPath(accessTokenResponse);
		String access_token = jp.getString("access_token");
		
		System.out.println(access_token);
			
			
			GetCourse gc = given().queryParam("access_token", access_token).expect().defaultParser(Parser.JSON)
			.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
			
			System.out.println(gc.getLinkedIn());
			System.out.println(gc.getInstructor());
			
			System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
			
			List<Api> apiCourses = gc.getCourses().getApi();
			
			for (int i = 0; i < apiCourses.size(); i++) {
				if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
					System.out.println(apiCourses.get(i).getPrice());
				}
			}
			
			ArrayList<String> a = new ArrayList<String>();
			List<WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation();
			
			for(int i =0; i < webAutomationCourses.size(); i++) {
				a.add(webAutomationCourses.get(i).getCourseTitle());
			}
			
			List<String> expectedList = Arrays.asList(courseTitles);
			
			Assert.assertTrue(a.equals(expectedList));
			
		}
}
