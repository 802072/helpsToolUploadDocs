package uploadDocTest;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bouncycastle.util.Properties;
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

	// Sample 1
	@SuppressWarnings("rawtypes")

	// @Test(priority = 1, dataProviderClass = DataProviderTestdata.class,
	// dataProvider = "UploadFilePath")
	public void verifyUploadDoc(String fileName, String path) throws InterruptedException, IOException, AWTException {

		login();
		// Click Import HCS Assessment
		ArrayList TS08 = d.getData("TS08", "TC");
		WebElement importBtn = driver.findElement(By.xpath((String) TS08.get(5)));
		String log8 = (String) TS08.get(0) + " " + TS08.get(1);
		extentTest.log(Status.PASS, log8,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log8 + ".jpg")).build());
		importBtn.click();
		Thread.sleep(5000);
		// Click Choose File

		ArrayList TS09 = d.getData("TS09", "TC");
		// String fileName = (String) TS09.get(6);
		WebElement chooseFileBtn = driver.findElement(By.xpath((String) TS09.get(5)));
		String log9 = (String) TS09.get(0) + " " + TS09.get(1);
		extentTest.log(Status.PASS, (String) TS09.get(0) + " Choose File: " + fileName,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log9 + ".jpg")).build());

		// log Upload File
		ArrayList TS10 = d.getData("TS10", "TC");
		// String path= (String) TS10.get(6);
		String log10 = (String) TS10.get(0) + " " + TS10.get(1);
		extentTest.log(Status.PASS, (String) TS10.get(0) + " " + TS10.get(1) + " " + fileName,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log10 + ".jpg")).build());

		Actions builder = new Actions(driver);
		builder.moveToElement(chooseFileBtn).click().build().perform();

		// Upload Function
		Thread.sleep(5000);
		Robot rb = new Robot();
		StringSelection str = new StringSelection(path);
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
		ArrayList TS11 = d.getData("TS11", "TC");
		WebElement submitBtn = driver.findElement(By.xpath((String) TS11.get(5)));
		submitBtn.click();
		String log11 = (String) TS11.get(0) + " " + TS11.get(1);
		extentTest.log(Status.PASS, (String) TS11.get(0) + " Submit File: " + fileName,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log11 + ".jpg")).build());

		Thread.sleep(5000);
//

//
//		// Click AUS Assessment Menu
//		ArrayList TS13 = d.getData("TS13", "TC");
//		WebElement AUSAssessmntMenu = driver.findElement(By.xpath((String) TS13.get(5)));
//		AUSAssessmntMenu.click();
//		String log13 = (String) TS13.get(0) + " " + TS13.get(1);
//		extentTest.log(Status.PASS, log13,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log13 + ".jpg")).build());
//
//		// Click IADL Capacity Menu
//		ArrayList TS14 = d.getData("TS14", "TC");
//		WebElement IADLCapMenu = driver.findElement(By.xpath((String) TS14.get(5)));
//		IADLCapMenu.click();
//		String log14 = (String) TS14.get(0) + " " + TS14.get(1);
//		extentTest.log(Status.PASS, log14,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log14 + ".jpg")).build());
//
//		// Click ADL Self Performance
//		ArrayList TS15 = d.getData("TS15", "TC");
//		WebElement ADLSelfPerfMenu = driver.findElement(By.xpath((String) TS15.get(5)));
//		ADLSelfPerfMenu.click();
//		String log15 = (String) TS15.get(0) + " " + TS15.get(1);
//		extentTest.log(Status.PASS, log15,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log15 + ".jpg")).build());
//
//		// Click Final Recommendation
//		ArrayList TS16 = d.getData("TS16", "TC");
//		WebElement finalRecMenu = driver.findElement(By.xpath((String) TS16.get(5)));
//		finalRecMenu.click();
//		String log16 = (String) TS16.get(0) + " " + TS16.get(1);
//		extentTest.log(Status.PASS, log16,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log16 + ".jpg")).build());
//
//		// Draw Sign
//		ArrayList TS17 = d.getData("TS17", "TC");
//		WebElement canvas = driver.findElement(By.xpath((String) TS17.get(5)));
//		Action signature = builder.moveToElement(canvas).clickAndHold().moveByOffset(200, 50).moveByOffset(10, 0)
//				.click().build();
//		signature.perform();
//		Thread.sleep(5000);
//		String log17 = (String) TS17.get(0) + " " + TS17.get(1);
//		extentTest.log(Status.PASS, log17,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log17 + ".jpg")).build());
//
//		// Accept Sign
//		ArrayList TS18 = d.getData("TS18", "TC");
//		WebElement acceptSign = driver.findElement(By.xpath((String) TS18.get(5)));
//		acceptSign.click();
//		String log18 = (String) TS18.get(0) + " " + TS18.get(1);
//		extentTest.log(Status.PASS, log18,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log18 + ".jpg")).build());
//
//		// Complete Assessment
//		ArrayList TS19 = d.getData("TS19", "TC");
//		WebElement completeAssessmnt = driver.findElement(By.xpath((String) TS19.get(5)));
//		completeAssessmnt.click();
//		String log19 = (String) TS19.get(0) + " " + TS19.get(1);
//		extentTest.log(Status.PASS, log19,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log19 + ".jpg")).build());
//
//		Thread.sleep(5000);
//
//		// Click Welcome Button
//		ArrayList TS20 = d.getData("TS20", "TC");
//		WebElement welcome = driver.findElement(By.xpath((String) TS20.get(5)));
//		welcome.click();
//		String log20 = (String) TS20.get(0) + " " + TS20.get(1);
//		extentTest.log(Status.PASS, log20,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log20 + ".jpg")).build());
//
//		// Select sign out
//		ArrayList TS21 = d.getData("TS21", "TC");
//		WebElement signOut = driver.findElement(By.xpath((String) TS21.get(5)));
//		signOut.click();
//		Thread.sleep(7000);
//		String log21 = (String) TS21.get(0) + " " + TS21.get(1);
//		extentTest.log(Status.PASS, log21,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log21 + ".jpg")).build());

	}

	// Sample1 upload
	@Test
	public void verifyUploadSample1() throws InterruptedException, IOException, AWTException {
		// Passing Upload FileName and FilePath
		ArrayList filenameRow = d.getData("FileName", "TC");
		String fileName = (String) filenameRow.get(1);
		ArrayList filePathRow = d.getData("FilePath", "TC");
		String path = (String) filePathRow.get(1);
		verifyUploadDoc(fileName, path);
	}

	//verify patient info fields sample 1
	@Test
	public void verifyPatientInfoSample1() throws InterruptedException, IOException, AWTException {
		verifyUploadSample1();
		
//		// Click Patient Info Menu
//		ArrayList TS12 = d.getData("TS12", "TC");
//		WebElement patientInfoMenu = driver.findElement(By.xpath((String) TS12.get(5)));
//		patientInfoMenu.click();
//		String log012 = (String) TS12.get(0) + " " + TS12.get(1);
//		extentTest.log(Status.PASS, log012,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log012 + ".jpg")).build());
		
		// First Name
		ArrayList TS012 = d.getData("TS012", "TC09");
		WebElement firstName = driver.findElement(By.xpath((String) TS012.get(6)));
		assertEquals((String) TS012.get(7), firstName.getAttribute("value"));
		String log12 = (String) TS012.get(0) + " " + TS012.get(2);
		extentTest.log(Status.PASS, log12+">> "+ firstName.getAttribute("value"),
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log12 + ".jpg")).build());

		// Last Name
		ArrayList TS013 = d.getData("TS013", "TC09");
		WebElement lastName = driver.findElement(By.xpath((String) TS013.get(6)));
		assertEquals((String) TS013.get(7), lastName.getAttribute("value"));
		String log13 = (String) TS013.get(0) + " " + TS013.get(2);
		extentTest.log(Status.PASS, log13+">> "+lastName.getAttribute("value"),
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log13 + ".jpg")).build());

	}

	@Test
	public void verifyUploadSample2() throws InterruptedException, IOException, AWTException {
		ArrayList filenameRow = d.getData("FileName", "TC");
		String fileName = (String) filenameRow.get(2);

		ArrayList filePathRow = d.getData("FilePath", "TC");
		String path = (String) filePathRow.get(2);
		verifyUploadDoc(fileName, path);

	}

	@Test
	public void verifyUploadSample3() throws InterruptedException, IOException, AWTException {
		ArrayList filenameRow = d.getData("FileName", "TC");
		String fileName = (String) filenameRow.get(3);

		ArrayList filePathRow = d.getData("FilePath", "TC");
		String path = (String) filePathRow.get(3);
		verifyUploadDoc(fileName, path);

	}

	@Test
	public void verifyUploadSample4() throws InterruptedException, IOException, AWTException {
		ArrayList filenameRow = d.getData("FileName", "TC");
		String fileName = (String) filenameRow.get(4);

		ArrayList filePathRow = d.getData("FilePath", "TC");
		String path = (String) filePathRow.get(4);
		verifyUploadDoc(fileName, path);

	}

	@Test
	public void verifyUploadSample5() throws InterruptedException, IOException, AWTException {
		ArrayList filenameRow = d.getData("FileName", "TC");
		String fileName = (String) filenameRow.get(5);

		ArrayList filePathRow = d.getData("FilePath", "TC");
		String path = (String) filePathRow.get(5);
		verifyUploadDoc(fileName, path);

	}

	@Test
	public void verifyUploadSample6() throws InterruptedException, IOException, AWTException {
		ArrayList filenameRow = d.getData("FileName", "TC");
		String fileName = (String) filenameRow.get(6);

		ArrayList filePathRow = d.getData("FilePath", "TC");
		String path = (String) filePathRow.get(6);
		verifyUploadDoc(fileName, path);

	}

	@Test
	public void verifyUploadSample7() throws InterruptedException, IOException, AWTException {
		ArrayList filenameRow = d.getData("FileName", "TC");
		String fileName = (String) filenameRow.get(7);

		ArrayList filePathRow = d.getData("FilePath", "TC");
		String path = (String) filePathRow.get(7);
		verifyUploadDoc(fileName, path);

	}

	@Test
	public void verifyUploadSample8() throws InterruptedException, IOException, AWTException {
		ArrayList filenameRow = d.getData("FileName", "TC");
		String fileName = (String) filenameRow.get(8);

		ArrayList filePathRow = d.getData("FilePath", "TC");
		String path = (String) filePathRow.get(8);
		verifyUploadDoc(fileName, path);

	}

}