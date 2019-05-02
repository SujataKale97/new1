package com.helper;

import java.io.IOException;
import java.util.HashMap;

import com.utilities.propertyReader;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SMS_US {
	String sheetName = "PaymentService";
	HashMap<String, String> values = new HashMap<>();
	RequestSpecification httpRequest;
	private static final SMS_US SMSobject = new SMS_US();

	private SMS_US() {

	}

	public static SMS_US getInstance() {
		return SMSobject;
	}

	public void servicesetUp() {
		try {

			RestAssured.baseURI = propertyReader.readingProperty("User-Subscription-BaseURI");
			httpRequest = RestAssured.given();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			// Log.assertLog(false, e.getMessage());
		}

	}

	public Response createUserSubscription() throws IOException {
		String CreateUserSubcription_body = "{\r\n" + "  \"autoRenew\": true,\r\n" + "  \"billingPlanId\": 1,\r\n"
				+ "  \"catalogSubscriptionId\": 1,\r\n" + "  \"comments\": [\r\n" + "    {\r\n"
				+ "      \"comment\": \"US-Created\"\r\n" + "    }\r\n" + "  ],\r\n"
				+ "  \"delayedStartDate\": \"2019-06-16\",\r\n" + "  \"earlyAutoRenew\": true,\r\n"
				+ "  \"giftInfo\": {\r\n" + "    \"birthMonth\": 1,\r\n" + "    \"birthYear\": 2011,\r\n"
				+ "    \"gender\": \"Male\",\r\n" + "    \"message\": \"HappyBirthday\"\r\n" + "  },\r\n"
				+ "  \"orderId\": 1,\r\n" + "  \"packageListId\": 1,\r\n" + "  \"paymentMethodId\": \"1\",\r\n"
				+ "  \"productId\": 1,\r\n" + "  \"reactivationEnabled\": true,\r\n" + "  \"storeId\": 1,\r\n"
				+ "  \"subscriptionStartDate\": \"2019-05-19\",\r\n" + "  \"userSubscriptionState\": \"active\",\r\n"
				+ "  \"userSubscriptionStatus\": \"active\"\r\n" + "}";
System.out.println("req structure : "+CreateUserSubcription_body);
		Response response = commonHelper.getInstance().doPostRequest("1/subscriptions",
				CreateUserSubcription_body, "US");
		return response;

	}
}
