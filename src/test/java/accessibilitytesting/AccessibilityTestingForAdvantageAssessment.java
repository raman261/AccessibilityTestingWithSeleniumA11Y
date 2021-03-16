package accessibilitytesting;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.deque.axe.AXE;
import base.Util;

public class AccessibilityTestingForAdvantageAssessment {
	private static final URL scriptUrl = AccessibilityTestingForAdvantageAssessment.class.getResource("/axe.min.js");
	// private static final URL chromedriverLocation =
	// AccessibilityTestingA11YWithSelenium.class.getResource("/chromedriver.exe");
	WebDriver driver;

	//AppiumDriver<WebElement> driver;
	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	ExtentTest testcase;
	String timeStamp;
	String fileWithPath;

	@BeforeMethod
	public void setup() {

		timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/reports/"+"AdvantageAssessment_AccessibilityTestResults.html");
		htmlReporter.config().setDocumentTitle("Accessibility Test report for AdvantageAssessment");
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("Test report");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir")+"\\src\\test\\resources\\chromedriver.exe");

		driver = new ChromeDriver();
		driver.get("https://advassessment-qa.demos.hclets.com/login");
		
	}

	@Test
	public void advassessmentAllyTest() throws Exception {

		testcase = extent.createTest("AdvantageAssessment_AccessibilityTest");
		driver.findElement(By.xpath("(//input[@formcontrolname='username'])[1]")).sendKeys("ramansha@hcl.com");
		driver.findElement(By.xpath("(//input[@formcontrolname='password'])[1]")).sendKeys("Password@123");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(5000);
		fileWithPath = Util.takeSnapShot(driver);
		if(driver.findElement(By.xpath("//span[text()='Request Access']")).isDisplayed()) {
			testcase.pass("Successfully logged-in in Advantage assessment application").addScreenCaptureFromPath(fileWithPath);
		}else {
			testcase.fail("Failed to login in the application").addScreenCaptureFromPath(fileWithPath);
		}
		driver.findElement(By.xpath("//span[text()='Request Access']")).click();

		fileWithPath = Util.takeSnapShot(driver);
		if(driver.findElement(By.xpath("//h2[text()='Raise Access Requests']")).isDisplayed()) {
			testcase.pass("Successfully clicked on Request Access button").addScreenCaptureFromPath(fileWithPath);
		}else {
			testcase.fail("Failed to click on Request Access button").addScreenCaptureFromPath(fileWithPath);
		}

		Thread.sleep(5000);
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl).analyze();
		JSONArray violations = responseJSON.getJSONArray("violations");
		System.out.println("violations.length()   "+ violations.length());
		if (violations.length() == 0) {
			System.out.println("no errors");
			Assert.assertTrue(true, "no violations found");
			testcase.pass("No violations found");
		} else {
			AXE.writeResults(("path & name of the file you want to save the  report"), responseJSON);
			testcase.error(AXE.report(violations));

		}

	}

	@AfterMethod
	public void teardown() {
		driver.quit();
		extent.flush();
	}

}
