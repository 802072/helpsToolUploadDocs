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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
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

	@Test(priority = 2, dataProviderClass = DataProviderTestdata.class, dataProvider = "UploadFilePath")
	public void verifyDocumentUpload(String Path, String fileName)
			throws InterruptedException, IOException, AWTException {
		login();
		// Click Import HCS Assessment
		ArrayList TS08 = d.getData("TS08", "TC01");
		WebElement importBtn = driver.findElement(By.xpath((String) TS08.get(5)));
		String log8 = (String) TS08.get(0) + " " + TS08.get(1);
		extentTest.log(Status.PASS, log8,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log8 + ".jpg")).build());
		importBtn.click();
		Thread.sleep(5000);

		// Click Choose File
		ArrayList TS09 = d.getData("TS09", "TC01");
		WebElement chooseFileBtn = driver.findElement(By.xpath((String) TS09.get(5)));
		String log9 = (String) TS09.get(0) + " " + TS09.get(1);
		extentTest.log(Status.PASS, "Choose File: " + fileName,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log9 + ".jpg")).build());
		Actions builder = new Actions(driver);
		builder.moveToElement(chooseFileBtn).click().build().perform();

		// Upload Function
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

		Thread.sleep(5000);
		// Click Submit File
		ArrayList TS11 = d.getData("TS11", "TC01");
		WebElement submitBtn = driver.findElement(By.xpath((String) TS11.get(5)));
		submitBtn.click();
		String log11 = (String) TS11.get(0) + " " + TS11.get(1);
		extentTest.log(Status.PASS, "Submit File: " + fileName,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log11 + ".jpg")).build());

		Thread.sleep(5000);
		// Click Patient Info Menu
		ArrayList TS12 = d.getData("TS12", "TC01");
		WebElement patientInfoMenu = driver.findElement(By.xpath((String) TS12.get(5)));
		patientInfoMenu.click();
		String log12 = (String) TS12.get(0) + " " + TS12.get(1);
		extentTest.log(Status.PASS, log12,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log12 + ".jpg")).build());

		// Click AUS Assessment Menu
		ArrayList TS13 = d.getData("TS13", "TC01");
		WebElement AUSAssessmntMenu = driver.findElement(By.xpath((String) TS13.get(5)));
		AUSAssessmntMenu.click();
		String log13 = (String) TS13.get(0) + " " + TS13.get(1);
		extentTest.log(Status.PASS, log13,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log13 + ".jpg")).build());

		// Click IADL Capacity Menu
		ArrayList TS14 = d.getData("TS14", "TC01");
		WebElement IADLCapMenu = driver.findElement(By.xpath((String) TS14.get(5)));
		IADLCapMenu.click();
		String log14 = (String) TS14.get(0) + " " + TS14.get(1);
		extentTest.log(Status.PASS, log14,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log14 + ".jpg")).build());

		// Click ADL Self Performance
		ArrayList TS15 = d.getData("TS15", "TC01");
		WebElement ADLSelfPerfMenu = driver.findElement(By.xpath((String) TS15.get(5)));
		ADLSelfPerfMenu.click();
		String log15 = (String) TS15.get(0) + " " + TS15.get(1);
		extentTest.log(Status.PASS, log15,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log15 + ".jpg")).build());

		// Click Final Recommendation
		ArrayList TS16 = d.getData("TS16", "TC01");
		WebElement finalRecMenu = driver.findElement(By.xpath((String) TS16.get(5)));
		finalRecMenu.click();
		String log16 = (String) TS16.get(0) + " " + TS16.get(1);
		extentTest.log(Status.PASS, log16,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log16 + ".jpg")).build());

		// Draw Sign
		ArrayList TS17 = d.getData("TS17", "TC01");
		WebElement canvas = driver.findElement(By.xpath((String) TS17.get(5)));
		Actions action = new Actions(driver);
		 action.clickAndHold(canvas).moveByOffset(200,
		 0).release().build().perform();
		//Action drawSign = action.moveToElement(canvas, 200, 0).clickAndHold().moveByOffset(200, 2).click().build();
		//drawSign.perform();

		Thread.sleep(5000);
		String log17 = (String) TS17.get(0) + " " + TS17.get(1);
		extentTest.log(Status.PASS, log17,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log17 + ".jpg")).build());

		// Accept Sign
		ArrayList TS18 = d.getData("TS18", "TC01");
		WebElement acceptSign = driver.findElement(By.xpath((String) TS18.get(5)));
		acceptSign.click();
		String log18 = (String) TS18.get(0) + " " + TS18.get(1);
		extentTest.log(Status.PASS, log18,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log18 + ".jpg")).build());

		// Complete Assessment
		ArrayList TS19 = d.getData("TS19", "TC01");
		WebElement completeAssessmnt = driver.findElement(By.xpath((String) TS19.get(5)));
		completeAssessmnt.click();
		String log19 = (String) TS19.get(0) + " " + TS19.get(1);
		extentTest.log(Status.PASS, log19,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log19 + ".jpg")).build());

		System.out.println("Complete button clicked");
		Thread.sleep(5000);

		// Click Welcome Button
		ArrayList TS20 = d.getData("TS20", "TC01");
		WebElement welcome = driver.findElement(By.xpath((String) TS20.get(5)));
		welcome.click();
		String log20 = (String) TS20.get(0) + " " + TS20.get(1);
		extentTest.log(Status.PASS, log20,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log20 + ".jpg")).build());

		// Select sign out
		ArrayList TS21 = d.getData("TS21", "TC01");
		WebElement signOut = driver.findElement(By.xpath((String) TS21.get(5)));
		signOut.click();
		Thread.sleep(7000);
		String log21 = (String) TS21.get(0) + " " + TS21.get(1);
		extentTest.log(Status.PASS, log21,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log21 + ".jpg")).build());

		// Click Login Button
		ArrayList TS22 = d.getData("TS22", "TC01");
		WebElement loginButton = driver.findElement(By.xpath((String) TS22.get(5)));
		loginButton.click();
		Thread.sleep(3000);
		String log22 = (String) TS22.get(0) + " " + TS22.get(1);
		extentTest.log(Status.PASS, log22,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log22 + ".jpg")).build());

		// Click Use Another Account
		ArrayList TS23 = d.getData("TS23", "TC01");
		WebElement useAnotherAcc = driver.findElement(By.xpath((String) TS23.get(5)));
		useAnotherAcc.click();
		String log2 = (String) TS23.get(0) + " " + TS23.get(1);
		extentTest.log(Status.PASS, log2,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log2 + ".jpg")).build());

	}
}