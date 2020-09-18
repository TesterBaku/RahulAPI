import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.ReusableMethods;
import files.payload;


public class basics {

	public static void main(String[] args) {
		
		//Add place --> Update Place with new address --> Get Place to validate if Neew address is present in response
		
		//given -- all input details
		//when -- submit the API (resource, http method)
		//then -- validate the response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(payload.AddPlace())
			.when().post("/maps/api/place/add/json")
			.then()
			    .assertThat()
				  .statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)")
				.extract()
				.response().asString();
		
		System.out.println(response);
		
		JsonPath js = ReusableMethods.rawToJson(response);
		String placeId = js.getString("place_id");
		
		System.out.println(placeId);
		
		//Update Place
		
		String newAddress = "Summer Walk, Africa";
		
		given().log().all()
		   .queryParam("key", "qaclick123").header("Content-Type", "application/json")
		   .body("{\r\n" + 
		   		"\"place_id\":\""+placeId+"\",\r\n" + 
		   		"\"address\":\""+newAddress+"\",\r\n" + 
		   		"\"key\":\"qaclick123\"\r\n" + 
		   		"}")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get place
		//no header is needed for GET 
		
		String getPlaceResponse = 
		given().log().all()
		    .queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		
		Assert.assertEquals(actualAddress, getPlaceResponse);
		
		
		
		
	}

}
