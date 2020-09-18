package libraryAPI;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.ReusableMethods;
import files.payload;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {
	
	@DataProvider(name = "BooksData")
	public Object[][] getData() {
		//array = collection of elements
		//multidimensional array = collection of arrays
		
		return new Object[][] {{"joj", "565"}, {"swe", "343"}, {"aga", "787"}};
		
	}
	
	@Test(dataProvider = "BooksData")
	public void addBook(String isbn, String aisle) {
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String resp = 
				given().header("Content-Type", "application/json")
				.body(payload.Addbook(isbn, aisle))
				.when()
				.post("/Library/Addbook.php")
				.then().assertThat().statusCode(200)
				.extract().response().asString();
		
		JsonPath js = ReusableMethods.rawToJson(resp);
		
		String id = js.getString("ID");
		
		System.out.println(id);
		
		
		
		
		
	}

}
