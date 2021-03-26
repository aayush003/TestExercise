package com.restAPI;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Pets {


	public static void get_available_pets() {

		Response resp = get("https://petstore.swagger.io/v2/pet/findByStatus?status=available");

		Assert.assertEquals(resp.getStatusCode(), 200);

	}

	public static long insert_new_pet() {

		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json");

		JSONObject pet_data = new JSONObject();
		pet_data.put("id", 0);

		JSONObject pet_category = new JSONObject();
		pet_category.put("id", 0);
		pet_category.put("name", "string");

		pet_data.put("category", pet_category);
		pet_data.put("name", "aayush");
		pet_data.put("status", "available");

		request.body(pet_data.toJSONString());
		Response response = request.post("https://petstore.swagger.io/v2/pet/");
		String new_pet_id_string = response.jsonPath().getString("id");

		long new_pet_id = response.jsonPath().getLong("id");
		Assert.assertEquals(response.getStatusCode(), 200);

		return new_pet_id;

	}

	public static void sold_pet(long pet_id) {
		RequestSpecification request = RestAssured.given();

		
		JSONObject pet_data = new JSONObject();
		pet_data.put("id", pet_id);
		pet_data.put("status", "sold");
				
		Response put_resp = given().
				body (pet_data.toJSONString())
				.when ()
				.contentType (ContentType.JSON)
				.put("https://petstore.swagger.io/v2/pet");
		
		Assert.assertEquals(put_resp.getStatusCode(), 200);
	}

	public static void delete_pet(long pet_id) {

		Response resp = delete("https://petstore.swagger.io/v2/pet/"+pet_id+"/");
		Assert.assertEquals(resp.getStatusCode(), 200);
		
	}

	@Test
	public static void run() {

		long id = 0;
		get_available_pets();
		id =insert_new_pet();
		sold_pet(id);
		delete_pet(id);
	}
}


