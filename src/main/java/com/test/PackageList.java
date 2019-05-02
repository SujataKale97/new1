package com.test;

import java.io.File;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helper.commonHelper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.utilities.RandomNumber;
import com.utilities.propertyReader;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PackageList {

	// propertyReader prop;
	RequestSpecification httpRequest;

	ExtentReports extent;
	ExtentTest logger;

	@BeforeClass
	public void report_setUp() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/packagelist.html", true);
		extent.addSystemInfo("Host Name", "API Testing").addSystemInfo("Environment", "API Automation Testing")
				.addSystemInfo("User Name", "mahantesh.sapgole");
		extent.loadConfig(new File(System.getProperty("user.dir") + "/testXML/extent.xml"));

	}

	@BeforeMethod
	public void setUp() {
		try {
			RestAssured.baseURI = propertyReader.readingProperty("catloguesubscription-BaseURI");
			httpRequest = RestAssured.given();
		} catch (IOException e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Package List Service Working ",
					"Package List service failed and its Exception stach trace : " + e.getMessage());
		}

	}

	@Test(priority = 1, description = "CreatePackageListAndVerifyUniqueIdentifier")
	public void CreatePackageListAndVerifyUniqueIdentifier() {
		try {
			logger = extent.startTest(
					"Test ID : PL-1 TestScenario : Verify able to create package_list and identifier is unique .");
			int identifierRandom = RandomNumber.getNumericString(1, 10000);
			String packageList_body = "{\r\n" + "\"active\": true,\r\n" + "\"identifier\": \"" + identifierRandom
					+ "\",\r\n" + "\"name\": \"march07_1\",\r\n" + "\"type\": \"NON_MAGAZINE\"\r\n" + "}";
			Response response = commonHelper.getInstance().doPostRequest("/catalog/package-lists", packageList_body,
					"PL");
			int statusCode = response.getStatusCode();
			if (statusCode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify whether the packages are getting created successfully",
						"packages are NOT getting created properly : " + statusCode);
			}
			Response response1 =  commonHelper.getInstance().doPostRequest("/catalog/package-lists", packageList_body,
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

	@Test(priority = 2, description = "VerifyTypeMagazineAndNonMagazine")
	public void VerifyTypeMagazineAndNonMagazine() {
		try {
			logger = extent.startTest(
					"Test Id : PL-2 TestScenario : Verify Type accepts only Magazine And NonMagazine URI :/catalog/package-lists");
			int identifierRandom = RandomNumber.getNumericString(1, 10000);
			String request = "{\r\n" + "\"active\": true,\r\n" + "\"identifier\": \"" + identifierRandom
					+ "\",\r\n" + "\"name\": \"march07_1\",\r\n" + "\"type\": \"Basic\"\r\n" + "}";
			Response response =  commonHelper.getInstance().doPostRequest("/catalog/package-lists", request,
					"PL");
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

	@AfterTest
	public void endReport() {
		extent.endTest(logger);
		extent.flush();
	}
}
