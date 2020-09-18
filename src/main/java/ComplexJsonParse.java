import org.testng.Assert;

import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {

		JsonPath js = new JsonPath(payload.CoursePrice());
		
//		1. Print No of courses returned by API
		
		int count = js.getInt("courses.size()");
		
		System.out.println(count);

//		2.Print Purchase Amount
		
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		
		System.out.println(totalAmount);

//		3. Print Title of the first course
		
		String titleFirstCourse = js.get("courses[0].title");
		
		System.out.println(titleFirstCourse);

//		4. Print All course titles and their respective Prices

		for(int i = 0; i < count; i++) {
			String courseTitles = js.get("courses["+ i +"].title");
			int coursePrices = js.getInt("courses["+ i +"].price");
			System.out.println(courseTitles);
			System.out.println(coursePrices);
		}
		
//		5. Print no of copies sold by RPA Course
		
		System.out.println("Print no of copies sold for RPA course");
		int copies = 0;
		for(int i = 0; i < count; i++) {
			
			String courseTitles = js.get("courses["+ i +"].title");
			if(courseTitles.equalsIgnoreCase("RPA")) {
				copies = js.getInt("courses["+ i +"].copies");
				break;
			}
			
		}
		System.out.println(copies);
		

//		6. Verify if Sum of all Course prices matches with Purchase Amount
		
		System.out.println("------------------------------------");
		
		int copiesSold = 0;
		int price = 0;
		int totalAmountExpected = js.getInt("dashboard.purchaseAmount");
		int totalAmountActual = 0;
		
for(int k = 0; k < count; k++) {
				copies = js.getInt("courses["+ k +"].copies");
				price = js.getInt("courses["+ k +"].price");
				totalAmountActual = totalAmountActual + (copies*price);
	}

System.out.println(totalAmountActual);
Assert.assertEquals(totalAmountActual, totalAmountExpected);

}
	
}
