package jira;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	public static void main(String[] args) {
		
		//Getting sessionID
		//Topics covered: SessionFilter, passing data set in pathparam into resourse URL as a variable --> (example: id)  
		//relaxedHTTPSValidation() --> this method takes care of Https certification
		
		SessionFilter session = new SessionFilter();
		String response = 
		given()
			.relaxedHTTPSValidation()
			.header("Content-Type", "application/json").body("{\"username\":\"testerbaku\", \"password\": \"Password\"}")
			.filter(session)
		.when()
			.post("/rest/auth/1/session")
		.then()
			.extract().response().asString();
		
		//Add comment
		
		String expectedMessage = "Hi How are you?";
		RestAssured.baseURI = "http://localhost:8080";
		
		String addCommentResponse =
		given()
			.header("Content-Type", "application/json")
			.pathParam("id", "10104").body("{\r\n" + 
				"    \"body\": \""+expectedMessage+"\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}")
			.filter(session)
		.when()
			.post("/rest/api/2/issue/{id}/comment")
		.then()
			.assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.getString("id");
		
		System.out.println(commentId);
		
		//Add attachment
		//Multipart
		
		given()
			.header("X-Atlassian-Token", "no-check").filter(session).pathParam("id", "10104")
			.header("Content-Type", "multipart/form-data").multiPart("file", new File("Jira.txt"))
		.when()
			.post("/rest/api/2/issue/{id}/attachments")
		.then()
			.assertThat().statusCode(200);
		
		
		//get issue
		String issueDetails =
		given().filter(session).pathParam("key", "10104")
		.queryParam("fields", "comment")
		.when().get("/rest/api/2/issue/{key}")
		.then().extract().response().asString();
		
		JsonPath js1 = new JsonPath(issueDetails);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		
		System.out.println(commentsCount);
		
		for(int i=0; i < commentsCount; i++) {
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
			if(commentIdIssue.equalsIgnoreCase(commentId)) {
				String message = js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
				
			}
		}
		
		
			
		}
	
	

	}


