package base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

//Base method class for common methods
public class Util {


	//Method to take the screenshots of application pages at runtime
	public static String takeSnapShot(WebDriver webdriver) throws Exception{

		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		File SrcFile = ((TakesScreenshot)(webdriver)).getScreenshotAs(OutputType.FILE);
		String fileWithPath = System.getProperty("user.dir") + "/reports/"+dateName+".png";
		File DestFile=new File(fileWithPath);
		FileUtils.copyFile(SrcFile, DestFile);
		
		
		return fileWithPath;

	}

}
