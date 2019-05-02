package com.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.helper.SMS_US;
import com.helper.commonHelper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import com.utilities.DataColumnMapping;
import com.utilities.RandomNumber;
import com.utilities.excelReader;
import com.utilities.propertyReader;

public class subscriptionManagementSystem {

	propertyReader prop;
	String sheetName = "SubscriptionManagementService";
	HashMap<String, String> values = new HashMap<>();
	public static ExtentTest logger;
	public static com.relevantcodes.extentreports.ExtentReports extent;

	@BeforeClass
	public void report_setUp() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/subscriptionManagementReport.html",
				true);
		extent.addSystemInfo("Host Name", "API Testing").addSystemInfo("Environment", "API Automation Testing")
				.addSystemInfo("User Name", "Nandhini");
		extent.loadConfig(new File(System.getProperty("user.dir") + "/testXML/extent.xml"));

	}

	@Test(priority = 1, invocationCount = 1, description = "Verify Catalog subscription has been created successfully with all the default values.", groups = {
			"smoke", "regression" })
	public void verifyCreateSubscription() {
		try {
			int storeid = RandomNumber.getNumericString(1, 10);
			String sku = Integer.toString(RandomNumber.getNumericString(1, 10));
			String createSubscription_body = "{\n" + "  \"active\":" + true + "," + "\n"
					+ "  \"allowCustomSecondMonthSelection\":" + true + "," + "\n"
					+ "  \"allowCustomerFirstMonthSelection\":" + true + "," + "\n" + "  \"billingPlanIds\": [\r\n"
					+ "    1\r\n" + "  ]," + "\n" + "  \"customFirstMonthRange\": \"Some name\"," + "\n"
					+ "  \"customSecondMonthRange\": \"Some name\"," + "\n" + "  \"firstCycleDay\":" + 0 + "," + "\n"
					+ "  \"firstCycleOutDay\":" + 0 + "," + "\n" + "  \"firstExpShippingDay\":" + 0 + "," + "\n"
					+ "  \"maxPackageCount\":" + 10 + "," + "\n" + "  \"packageListId\":" + 1 + "," + "\n"
					+ "  \"regCclDay\":" + 0 + "," + "\n" + "  \"regCclOutCatchUpDay\":" + 0 + "," + "\n"
					+ "  \"regCclOutDay\":" + 0 + "," + "\n" + "  \"regExpShippingDay\": " + 0 + "," + "\n"
					+ "  \"sku\": " + sku + "," + "\n" + "  \"storeId\": " + storeid + "," + "\n"
					+ "  \"subscriptionType\": \"Regular\"\n" + "}";

			logger = extent.startTest(
					"Test ID : CS-1 TestScenario : Verify Catalog subscription has been created successfully with all the default values.");
			Response response1 = commonHelper.getInstance().doPostRequest("/catalog/subscriptions",
					createSubscription_body, "SMS");
			System.out.println(response1.getBody().asString());
			int statuscode = response1.getStatusCode();
			if (statuscode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.PASS, " the subscription is created with store id " + storeid,
						"Full response : " + response1.asString());
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.FAIL,
						" the subscription is NOT created with store id " + response1.asString() + " for reference");
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 2, description = "Verify Catalog subscription has not been created without providing SKU value in the request.", groups = {
			"smoke", "regression" })
	public void verifyCreateSubscriptionwithoutSKU() {
		try {
			int storeid = +RandomNumber.getNumericString(5, 15);
			String createSubscription_body = "{\n" + "  \"active\":" + true + "," + "\n"
					+ "  \"allowCustomSecondMonthSelection\":" + true + "," + "\n"
					+ "  \"allowCustomerFirstMonthSelection\":" + true + "," + "\n" + "  \"billingPlanIds\": [\r\n"
					+ "    1\r\n" + "  ]," + "\n" + "  \"customFirstMonthRange\": \"Some name\"," + "\n"
					+ "  \"customSecondMonthRange\": \"Some name\"," + "\n" + "  \"firstCycleDay\":" + 0 + "," + "\n"
					+ "  \"firstCycleOutDay\":" + 0 + "," + "\n" + "  \"firstExpShippingDay\":" + 0 + "," + "\n"
					+ "  \"maxPackageCount\":" + 10 + "," + "\n" + "  \"packageListId\":" + 1 + "," + "\n"
					+ "  \"regCclDay\":" + 0 + "," + "\n" + "  \"regCclOutCatchUpDay\":" + 0 + "," + "\n"
					+ "  \"regCclOutDay\":" + 0 + "," + "\n" + "  \"regExpShippingDay\": " + 0 + "," + "\n"
					+ "  \"storeId\": " + storeid + "," + "\n" + "  \"subscriptionType\": \"Regular\"\n" + "}";

			logger = extent.startTest(
					"Test ID : CS-2 TestScenario : Verify Catalog subscription has not been created without providing SKU value in the request.");

			System.out.println(createSubscription_body);
			Response response1 = commonHelper.getInstance().doPostRequest("/catalog/subscriptions",
					createSubscription_body, "SMS");
			System.out.println(response1.getBody().asString());
			int statuscode = response1.getStatusCode();
			if (statuscode == 400) {
				logger.log(LogStatus.PASS,
						" the subscription is NOT created with store id when SKU is empty " + storeid,
						"Status code is : " + statuscode + "Full response : " + response1.asString());
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 3, description = "Verify Catalog subscription details retrieved successfully by sending the request by catalog subscription ID.", groups = {
			"smoke", "regression" })
	public void getCatlogSubscriptionDetails() {
		try {
			logger = extent.startTest(
					"Test Id : CS-3 TestScenario : Verify Catalog subscription details retrieved successfully by sending the request by catalog subscription ID.");
			values = excelReader.readExcel(sheetName, "getCatlogSubscriptionDetails");
			String catlogsubscid = values.get(DataColumnMapping.CATLOGSUBSCRIPTIONID);
			Response response1 = commonHelper.getInstance().doGetRequest("/catalog/subscriptions/" + catlogsubscid,
					"SMS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			if (statuscode == 200) {
				logger.log(LogStatus.PASS,
						" Verify whether the Catlog Subscription Details Are getting displayed properly for id "
								+ catlogsubscid,
						"Catlog Subscription Details displayed properly with Status code is : " + statuscode
								+ "response : " + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.FAIL,
						" Verify whether the Catlog Subscription Details Are getting displayed properly for id "
								+ catlogsubscid,
						"Catlog Subscription Details is NOT  displayed properly , response for ref : "
								+ responseString);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 4, description = "Verify Catalog subscription details can be updated by providing catalog subscription ID in the request. ", groups = {
			"smoke", "regression" })
	public void updateCatlogSubscriptionDetails() {
		try {
			logger = extent.startTest(
					"Test Id : CS-4 TestScenario : Verify CatalogSubscription details can be updated by providing catalog subscription ID in the request");
			values = excelReader.readExcel(sheetName, "getCatlogSubscriptionDetails");
			String catlogsubscid = values.get(DataColumnMapping.CATLOGSUBSCRIPTIONID);
			String storeid = Integer.toString(RandomNumber.getNumericString(1, 5));
			String request = "{\r\n" + "  \"active\": true,\r\n" + "  \"allowCustomSecondMonthSelection\": true,\r\n"
					+ "  \"allowCustomerFirstMonthSelection\": true,\r\n" + "  \"billingPlanIds\": [\r\n" + "    1\r\n"
					+ "  ],\r\n" + "  \"customFirstMonthRange\": \"string\",\r\n"
					+ "  \"customSecondMonthRange\": \"string\",\r\n" + "  \"firstCycleDay\": 0,\r\n"
					+ "  \"firstCycleOutDay\": 0,\r\n" + "  \"firstExpShippingDay\": 0,\r\n"
					+ "  \"maxPackageCount\": 0,\r\n" + "  \"packageListId\": 0,\r\n" + "  \"regCclDay\": 0,\r\n"
					+ "  \"regCclOutCatchUpDay\": 0,\r\n" + "  \"regCclOutDay\": 0,\r\n"
					+ "  \"regExpShippingDay\": 0,\r\n" + "  \"sku\": \"string\",\r\n" + "  \"storeId\": " + storeid
					+ ",\r\n" + "  \"subscriptionType\": \"Regular\"\r\n" + "}";
			Response response1 = commonHelper.getInstance().doPutRequest("/catalog/subscriptions/" + catlogsubscid,
					request, "SMS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			if (statuscode == 200) {
				logger.log(LogStatus.PASS,
						" Verify whether the Catlog Subscription Details Are updating and  displayed properly for id "
								+ catlogsubscid,
						"Catlog Subscription Details displayed properly with Status code is : " + statuscode
								+ " response : " + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.FAIL,
						" Verify whether the Catlog Subscription Details Are updating and  displayed properly for id "
								+ catlogsubscid,
						"Catlog Subscription Details is not updated and  displayed properly with response : "
								+ responseString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 5, description = "CreatePackageListAndVerifyUniqueIdentifier")
	public void CreatePackageListAndVerifyUniqueIdentifier() {
		try {
			logger = extent.startTest(
					"Test ID : PL-1 TestScenario : Verify able to create package_list and identifier is unique .");
			int identifierRandom = RandomNumber.getNumericString(1, 10000);
			String packageList_body = "{\r\n" + "\"active\": true,\r\n" + "\"identifier\": \"" + identifierRandom
					+ "\",\r\n" + "\"packageListName\": \"march07_1\",\r\n"
					+ "\"packageListType\": \"NON_MAGAZINE\"\r\n" + "}";
			Response response = commonHelper.getInstance().doPostRequest("/catalog/package-lists", packageList_body,
					"PL");
			int statusCode = response.getStatusCode();
			if (statusCode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify whether the packages are getting created successfully",
						"packages are NOT getting created properly : " + statusCode);
			}
			Response response1 = commonHelper.getInstance().doPostRequest("/catalog/package-lists", packageList_body,
					"PL");
			int statusCode1 = response1.getStatusCode();
			if (statusCode1 == 400) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode1 + " Verified Identifier is unique");
			} else {
				logger.log(LogStatus.FAIL, " Verify if the packages are getting created successfully" + statusCode1
						+ " Verified Identifier is not unique");
			}

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Package List Service Working ",
					"Package List service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 6, description = "VerifyTypeMagazineAndNonMagazine")
	public void VerifyTypeMagazineAndNonMagazine() {
		try {
			logger = extent.startTest(
					"Test Id : PL-2 TestScenario : Verify Type accepts only Magazine And NonMagazine URI :/catalog/package-lists");
			int identifierRandom = RandomNumber.getNumericString(1, 10000);
			String request = "{\r\n" + "\"active\": true,\r\n" + "\"identifier\": \"" + identifierRandom + "\",\r\n"
					+ "\"packageListName\": \"march07_1\",\r\n" + "\"packageListType\": \"Basic\"\r\n" + "}";
			Response response = commonHelper.getInstance().doPostRequest("/catalog/package-lists", request, "PL");
			int statusCode = response.getStatusCode();
			Assert.assertEquals(statusCode, 400);
			System.out.println("Verified Package list Type and which allows only 'NON_MAGAZINE, MAGAZINE'");

			if (statusCode == 400) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statusCode
						+ " Verified Package list TYPE and which allows only 'NON_MAGAZINE, MAGAZINE'");
			} else {
				logger.log(LogStatus.FAIL, " Verify if the packages are getting created successfully" + statusCode
						+ " Verified Package list TYPE accepts everything including 'NON_MAGAZINE, MAGAZINE'");
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Package List Service Working ",
					"Package List service failed and its Exception stach trace : " + e.getMessage());
		}

	}

	@Test(priority = 7, description = "CreateUserSubcription")
	public void CreateUserSubcription() {
		try {
			logger = extent.startTest("Test Id : US-1 TestScenario : CreateUserSubcription");
			String CreateUserSubcription_body = "{\r\n" + "  \"autoRenew\": true,\r\n" + "  \"billingPlanId\": 1,\r\n"
					+ "  \"catalogSubscriptionId\": 1,\r\n" + "  \"comments\": [\r\n" + "    {\r\n"
					+ "      \"comment\": \"US-Created\"\r\n" + "    }\r\n" + "  ],\r\n"
					+ "  \"delayedStartDate\": \"2019-06-16\",\r\n" + "  \"earlyAutoRenew\": true,\r\n"
					+ "  \"giftInfo\": {\r\n" + "    \"birthMonth\": 1,\r\n" + "    \"birthYear\": 2011,\r\n"
					+ "    \"gender\": \"Male\",\r\n" + "    \"message\": \"HappyBirthday\"\r\n" + "  },\r\n"
					+ "  \"orderId\": 1,\r\n" + "  \"packageListId\": 1,\r\n" + "  \"paymentMethodId\": \"1\",\r\n"
					+ "  \"productId\": 1,\r\n" + "  \"reactivationEnabled\": true,\r\n" + "  \"storeId\": 1,\r\n"
					+ "  \"subscriptionStartDate\": \"2019-05-19\",\r\n"
					+ "  \"userSubscriptionState\": \"active\",\r\n" + "  \"userSubscriptionStatus\": \"active\"\r\n"
					+ "}";
			System.out.println("catlog body :" + CreateUserSubcription_body);
			Response response = commonHelper.getInstance().doPostRequest("1/subscriptions", CreateUserSubcription_body,
					"US");
			int statusCode = response.getStatusCode();
			if (statusCode == 201) {
				logger.log(LogStatus.PASS, " Able to create UserSubscription and Verify Response Status code",
						"Status code is : " + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify whether the UserSubscription are getting created successfully",
						"UserSubscription are NOT getting created properly : " + statusCode);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 8, description = "Retrieve Package List with id")
	public void VerifygetPackageList() {
		try {
			logger = extent.startTest("Test Id : PL-3 TestScenario : Verify Get Package List");
			values = excelReader.readExcel(sheetName, "VerifygetPackageList");
			String packageListId = values.get(DataColumnMapping.PACKAGELISTID);
			Response response = commonHelper.getInstance().doGetRequest("/catalog/package-lists/" + packageListId,
					"PL");
			String responseBody = response.getBody().asString();
			int statusCode = response.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  get package list response for the package list id "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "   get package list response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Package List Service Working ",
					"Package List service failed and its Exception stach trace : " + e.getMessage());
		}

	}

	@Test(priority = 9, description = "Update Package List with id")
	public void VerifyUpdatePackageList() {
		try {
			logger = extent.startTest("Test Id : PL-4 TestScenario : Verify Update Package List with id");
			values = excelReader.readExcel(sheetName, "getCatlogSubscriptionDetails");
			String packageListId = values.get(DataColumnMapping.PACKAGELISTID);
			String packageShipDate = commonHelper.getInstance().postDate(3);
			String dontShipBeforeDate = commonHelper.getInstance().getDate();
			String id = Integer.toString(RandomNumber.getNumericString(1, 5));
			String Request = "{\r\n" + "    \"packageListName\": \"April_17_new\",\r\n" + "    \"identifier\": \"" + id
					+ "\",\r\n" + "    \"packageListType\": \"NON_MAGAZINE\",\r\n"
					+ "    \"createdAt\": \"2019-04-19T07:28:41.741940Z\",\r\n"
					+ "    \"updatedAt\": \"2019-04-19T07:28:41.741940Z\",\r\n" + "    \"noOfPackages\": 1,\r\n"
					+ "    \"active\": true,\r\n" + "    \"packageMappingsList\": [\r\n" + "        {\r\n"
					+ "            \"id\": 34,\r\n" + "            \"packageMappingName\": \"April17_1_new\",\r\n"
					+ "            \"packageSkuId\": \"April17_pk1_new\",\r\n" + "            \"sortOrder\": 0,\r\n"
					+ "            \"packageShipDate\": \"" + packageShipDate + "\",\r\n"
					+ "            \"dontShipBeforeDate\": \"" + dontShipBeforeDate + "\",\r\n"
					+ "            \"createdAt\": \"2019-04-19T07:28:41.743975Z\",\r\n"
					+ "            \"updatedAt\": \"2019-04-19T07:28:41.743975Z\",\r\n"
					+ "            \"isFirstPackage\": true\r\n" + "        }\r\n" + "    ]\r\n" + "}";
			System.out.println(Request);
			Response response = commonHelper.getInstance().doPutRequest("/catalog/package-lists/" + packageListId,
					Request, "PL");
			String responseBody = response.getBody().asString();
			int statusCode = response.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update package list response for the package list id "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update package list response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Package List Service Working ",
					"Package List service failed and its Exception stach trace : " + e.getMessage());
		}

	}

	@Test(priority = 10, description = "Get User Subscription")
	public void getUserSubcription() {
		try {
			logger = extent.startTest("Test Id : US-2 TestScenario : GetUserSubcription");
			Response res = SMS_US.getInstance().createUserSubscription();
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			String subscriptionid = Integer.toString(ps_jsonPathEvaluator.get("id"));
			String userid = Integer.toString(ps_jsonPathEvaluator.get("userId"));
			Response response1 = commonHelper.getInstance().doGetRequest(userid+"/subscriptions/" + subscriptionid, "US");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			System.out.println("Status code  :" + statuscode + " response :" + response1);
			if (statuscode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.PASS,
						" Verify whether the Subscription Details Are getting displayed properly for id "
								+ subscriptionid,
						"Subscription Details displayed properly with response : " + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.FAIL,
						" Verify whether the Subscription Details Are getting displayed properly for id "
								+ subscriptionid,
						"Subscription Details is NOT  displayed properly , response for ref : " + responseString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@Test(priority = 11, description = "Update User Subscription")
	public void updateUserSubcription() {
		try {
			logger = extent.startTest("Test Id : US-2 TestScenario : UpdateUserSubcription");
			Response res = SMS_US.getInstance().createUserSubscription();
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			Integer subscriptionid = ps_jsonPathEvaluator.get("id");
			Integer userid = ps_jsonPathEvaluator.get("userId");
			System.out.println("user id :"+userid+" id :"+subscriptionid);
			ArrayList comment_info=ps_jsonPathEvaluator.get("comments.id");
			String commentsid=comment_info.get(0).toString();
			//ArrayList gift_temp=ps_jsonPathEvaluator.get("giftInfo.id");
			String giftinfo_id=Integer.toString(ps_jsonPathEvaluator.get("giftInfo.id"));
			
			String Request="{\r\n" + 
					"  \"autoRenew\": true,\r\n" + 
					"  \"billingPlanId\": 1,\r\n" + 
					"  \"catalogSubscriptionId\": 1,\r\n" + 
					"  \"comments\": [\r\n" + 
					"    {\r\n" + 
					"       \"comment\": \"US-Created\",\r\n" + 
					"      \"id\":"+commentsid+"\r\n" + 
					"    }\r\n" + 
					"  ],\r\n" + 
					"  \"delayedStartDate\": \"2019-06-16\",\r\n" + 
					"  \"earlyAutoRenew\": true,\r\n" + 
					"  \"giftInfo\": {\r\n" + 
					"    \"birthMonth\": 1,\r\n" + 
					"    \"birthYear\": 2011,\r\n" + 
					"    \"gender\": \"Male\",\r\n" + 
					"    \"id\": "+giftinfo_id+",\r\n" + 
					"    \"message\": \"HappyBirthday\"\r\n" + 
					"  },\r\n" + 
					"  \"orderId\": 1,\r\n" + 
					"  \"packageListId\": 1,\r\n" + 
					"  \"paymentMethodId\": \"1\",\r\n" + 
					"  \"productId\": 1,\r\n" + 
					"  \"reactivationEnabled\": false,\r\n" + 
					"  \"shippingAddressId\": \"1\",\r\n" + 
					"  \"storeId\": 1,\r\n" + 
					"  \"subscriptionStartDate\": \"2019-05-19\",\r\n" + 
					"  \"userSubscriptionState\": \"active\",\r\n" + 
					"  \"userSubscriptionStatus\": \"active\"\r\n" + 
					"}";
			System.out.println("update req :"+Request);
			Response response1 = commonHelper.getInstance().doPutRequest(userid+"/subscriptions/"+subscriptionid,Request, "US");
			String responseBody = response1.getBody().asString();
			int statusCode = response1.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update user Subscription  response for the user id "+userid+" and response "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update user Subscription response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@Test(priority = 12, description = "Validate Immediate Billing Plan change functionality")
	public void patchimmediateBillingPlanChange() {
		try {
			logger = extent.startTest("Test Id : US-3 TestScenario : Validate Immediate Billing Plan change functionality");
			Response res = SMS_US.getInstance().createUserSubscription();
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			Integer subscriptionid = ps_jsonPathEvaluator.get("id");
			Integer userid = ps_jsonPathEvaluator.get("userId");
			values = excelReader.readExcel(sheetName, "patchimmediateBillingPlanChange");
			String billingPlanId = values.get(DataColumnMapping.BILLPLANID);
			
			String Request="{\r\n" + 
					"  \"billingPlanId\": "+billingPlanId+",\r\n" + 
					"  \"isProrated\": true,\r\n" + 
					"  \"stack\": true\r\n" + 
					"}";
			System.out.println("update req :"+Request);
			Response response1 = commonHelper.getInstance().doPatchRequest(userid+"/subscriptions/"+subscriptionid+"/billing-plan",Request, "US");
			String responseBody = response1.getBody().asString();
			int statusCode = response1.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update Billing plan id user Subscription  response for the user id "+userid+" and response "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update billing plan id user Subscription response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@Test(priority = 13, description = "Validate cancel User Subscription ")
	public void cancelUserSubscription() {
		try {
			logger = extent.startTest("Test Id : US-4 TestScenario : Validate cancel User Subscription ");
			Response res = SMS_US.getInstance().createUserSubscription();
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			Integer subscriptionid = ps_jsonPathEvaluator.get("id");
			Integer userid = ps_jsonPathEvaluator.get("userId");
			values = excelReader.readExcel(sheetName, "patchimmediateBillingPlanChange");
			Response response1 = commonHelper.getInstance().doPatchRequest(userid+"/subscriptions/"+subscriptionid+"/cancel","", "US");
			String responseBody = response1.getBody().asString();
			int statusCode = response1.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  cancel   user Subscription  response for the user id "+userid+" and response "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "  cancel  user Subscription response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@Test(priority = 14, description = "Validate Renewing User Subscription")
	public void renewUserSubscription() {
		try {
			logger = extent.startTest("Test Id : US-5 TestScenario : Validate Renewing User Subscription");
			String CreateUserSubcription_body = "{\r\n" + "  \"autoRenew\": false,\r\n" + "  \"billingPlanId\": 4,\r\n"
					+ "  \"catalogSubscriptionId\": 1,\r\n" + "  \"comments\": [\r\n" + "    {\r\n"
					+ "      \"comment\": \"US-Created\"\r\n" + "    }\r\n" + "  ],\r\n"
					+ "  \"delayedStartDate\": \"2019-06-16\",\r\n" + "  \"earlyAutoRenew\": true,\r\n"
					+ "  \"giftInfo\": {\r\n" + "    \"birthMonth\": 1,\r\n" + "    \"birthYear\": 2011,\r\n"
					+ "    \"gender\": \"Male\",\r\n" + "    \"message\": \"HappyBirthday\"\r\n" + "  },\r\n"
					+ "  \"orderId\": 1,\r\n" + "  \"packageListId\": 48,\r\n" + "  \"paymentMethodId\": \"1\",\r\n"
					+ "  \"productId\": 1,\r\n" + "  \"reactivationEnabled\": true,\r\n" + "  \"storeId\": 1,\r\n"
					+ "  \"subscriptionStartDate\": \"2019-05-19\",\r\n" + "  \"userSubscriptionState\": \"active\",\r\n"
					+ "  \"userSubscriptionStatus\": \"active\"\r\n" + "}";
			Response res = commonHelper.getInstance().doPostRequest("1/subscriptions", CreateUserSubcription_body, "US");
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			Integer subscriptionid = ps_jsonPathEvaluator.get("id");
			Integer userid = ps_jsonPathEvaluator.get("userId");
			Response response1 = commonHelper.getInstance().doPatchRequest(userid+"/subscriptions/"+subscriptionid+"/renew","", "US");
			String responseBody = response1.getBody().asString();
			int statusCode = response1.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "Renew user Subscription  response for the user id "+userid+" and response "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "Renew user Subscription response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
			Response response2 = commonHelper.getInstance().doPatchRequest(userid+"/subscriptions/"+subscriptionid+"/summary","", "US");
			int statusCode_summary = response2.getStatusCode();
			if (statusCode_summary == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "User Subscription summary response for the user id "+userid+" and response "
								+ responseBody + " status code :" + statusCode);
				JsonPath summary_jsonPathEvaluator = res.jsonPath();
				String nextRenewalDate = summary_jsonPathEvaluator.get("nextRenewalDate");
				if(nextRenewalDate!=null) {
				logger.log(LogStatus.PASS, " Verify the presence of next renewal date",
						"Next Renewal date is present : "+nextRenewalDate+" Renewal is successful");
				}
				else {
					logger.log(LogStatus.FAIL, " Verify the presence of next renewal date",
							"Next Renewal date is NOT present and  Renewal is NOT successful");
				}
				
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "User Subscription Summary response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@AfterTest
	public void endReport() {
		extent.endTest(logger);
		extent.flush();

	}

}
