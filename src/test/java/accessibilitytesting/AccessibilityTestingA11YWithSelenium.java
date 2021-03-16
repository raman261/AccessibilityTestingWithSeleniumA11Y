package accessibilitytesting;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
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

public class AccessibilityTestingA11YWithSelenium {
	private static final URL scriptUrl = AccessibilityTestingA11YWithSelenium.class.getResource("/axe.min.js");
	// private static final URL chromedriverLocation =
	// AccessibilityTestingA11YWithSelenium.class.getResource("/chromedriver.exe");
	WebDriver driver;
	
	//AppiumDriver<WebElement> driver;
	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	ExtentTest test;
	String timeStamp;
	String chromeDriverLocation;
	String geckDriverLocation;
	String fileWithPath;

	@BeforeMethod
	public void setup() {
		
		timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/reports/"+"Amazon_AccessibilityTestResults.html");
		htmlReporter.config().setDocumentTitle("Accessibility Test report for Amazon");
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("Test report");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir")+"\\src\\test\\resources\\chromedriver.exe");

		// Instantiate a ChromeDriver class.
		driver = new ChromeDriver();
		driver.get("https://www.amazon.com");
	}

	@Test
	public void amazonAllyTest() {
		// AXE.inject(driver, scriptUrl);
		test = extent.createTest("Amazon_AccessibilityTest");
		JSONObject responseJSON = new AXE.Builder(driver, scriptUrl).analyze();
		JSONArray violations = responseJSON.getJSONArray("violations");
		if (violations.length() == 0) {
			System.out.println("no errors");
			Assert.assertTrue(true, "no violations found");
			test.pass("No violations found");
		} else {
			AXE.writeResults(("path & name of the file you want to save the  report"), responseJSON);
			test.error(AXE.report(violations));
			//Assert.assertTrue(false, AXE.report(violations));
			
		}

	}

	@AfterMethod
	public void teardown() {
		driver.quit();
		extent.flush();
	}

}
