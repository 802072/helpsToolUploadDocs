package uploadDocTest;

import java.io.IOException;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import dataDriven.DataDrivenHT;
import extentReport.BaseTest;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DocumentsUploadTest extends BaseTest {

	JavascriptExecutor js = (JavascriptExecutor) driver;
	DataDrivenHT d = new DataDrivenHT();

	@Test
	public void login () throws InterruptedException, IOException {
		driver.get("https://helps.vnshealth-test.mso.vnsny.org/");
		ArrayList TS01 = d.getData("TS01", "TC01");
		//driver.get((String) TS01.get(6));
		
		String log1 = (String) TS01.get(0) + " " + TS01.get(1);
		extentTest.log(Status.PASS, log1,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log1 + ".jpg")).build());

//		ArrayList TS0002 = d.getData("TS0002", "TC0038");
//		String loginUrl = (String) TS0002.get(6);
//		driver.get(loginUrl);
//
//		String description2 = (String) TS0002.get(0) + " " + TS0002.get(1);
//		extentTest.log(Status.PASS, description2,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(description2 + ".jpg")).build());
//
//		ArrayList TS0003 = d.getData("TS0003", "TC0038");
//		String enterBtn = (String) TS0003.get(5);
//		WebElement enter = driver.findElement(By.xpath(enterBtn));
//		enter.click();
//
//		String description3 = (String) TS0003.get(0) + " " + TS0003.get(1);
//		extentTest.log(Status.PASS, description3,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(description3 + ".jpg")).build());
//
//		// enter username
//		ArrayList TS0004 = d.getData("TS0004", "TC0038");
//		String username = (String) TS0004.get(6);
//		String unamePath = (String) TS0004.get(5);
//		WebElement unameField = driver.findElement(By.xpath(unamePath));
//		unameField.sendKeys(username);
//
//		String description4 = (String) TS0004.get(0) + " " + TS0004.get(1);
//		extentTest.log(Status.PASS, description4,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(description4 + ".jpg")).build());
//
//		// enter password
//		ArrayList TS0005 = d.getData("TS0005", "TC0038");
//		String pwdPath = (String) TS0005.get(5);
//		WebElement pwd = driver.findElement(By.xpath(pwdPath));
//		String password = (String) TS0005.get(6);
//		pwd.sendKeys(password);
//
//		String description5 = (String) TS0005.get(0) + " " + TS0005.get(1);
//		extentTest.log(Status.PASS, description5,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(description5 + ".jpg")).build());
//
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//
//		// login
//		ArrayList TS0006 = d.getData("TS0006", "TC0038");
//		String signOnPath = (String) TS0006.get(5);
//		WebElement signOn = driver.findElement(By.xpath(signOnPath));
//		signOn.click();
//		Thread.sleep(5000);
//		String description6 = (String) TS0006.get(0) + " " + TS0006.get(1);
//		extentTest.log(Status.PASS, description6,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(description6 + ".jpg")).build());
//		Thread.sleep(5000);

	}

	@Test(priority = 2, dependsOnMethods = "login", dataProviderClass = DataProviderTestdata.class, dataProvider = "UploadFilePath")
	public void verifyDocumentUpload(String Path, String fileName)
			throws InterruptedException, IOException, AWTException {

		// Upload Files
		ArrayList TS0013 = d.getData("TS0013", "TC0038");
		String uploadBtnPath = (String) TS0013.get(5);
		WebElement uploadBtn = driver.findElement(By.xpath(uploadBtnPath));
		String description13 = (String) TS0013.get(0) + " " + TS0013.get(1);
		extentTest.log(Status.PASS, description13,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(description13 + ".jpg")).build());
		uploadBtn.click();

		Thread.sleep(5000);
		Robot rb = new Robot();
		StringSelection str = new StringSelection(Path);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
		Thread.sleep(5000);

		// press Contol+V for pasting
		rb.keyPress(KeyEvent.VK_CONTROL);
		rb.keyPress(KeyEvent.VK_V);
		Thread.sleep(2000);
		// release Contol+V for pasting
		rb.keyRelease(KeyEvent.VK_CONTROL);
		rb.keyRelease(KeyEvent.VK_V);
		Thread.sleep(2000);

		// for pressing and releasing Enter
		rb.keyPress(KeyEvent.VK_ENTER);
		rb.keyRelease(KeyEvent.VK_ENTER);

		// Thread.sleep(5000);

		Thread.sleep(5000);
		ArrayList TS0014 = d.getData("TS0014", "TC0038");

		String desPath = (String) TS0014.get(5);
		WebElement desc = driver.findElement(By.xpath(desPath));
		desc.sendKeys(fileName);
		String description14 = (String) TS0014.get(0) + " " + TS0014.get(1);
		extentTest.log(Status.PASS, description14,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(description14 + ".jpg")).build());

		Thread.sleep(5000);
	}
}