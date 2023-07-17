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

	// File Sample-1
	@Test
	public void verifyInfoSample1() throws InterruptedException, IOException, AWTException {
		login();
		ArrayList filenameRow = d.getData("FileName", "beforeTest");
		String fileName = (String) filenameRow.get(1);
		ArrayList filePathRow = d.getData("FilePath", "beforeTest");
		String path = (String) filePathRow.get(1);
		uploadAndSubmitFile(fileName, path);
		verifyInfo("TC01");
	}

	// File Sample-2
	@Test
	public void verifyUploadSample2() throws InterruptedException, IOException, AWTException {
		login();
		ArrayList filenameRow = d.getData("FileName", "TC");
		String fileName = (String) filenameRow.get(2);
		ArrayList filePathRow = d.getData("FilePath", "TC");
		String path = (String) filePathRow.get(2);
		uploadAndSubmitFile(fileName, path);
		verifyInfo("TC02");

	}

	@Test
	public void verifyUploadSample3() throws InterruptedException, IOException, AWTException {

	}

	@Test
	public void verifyUploadSample4() throws InterruptedException, IOException, AWTException {

	}

	@Test
	public void verifyUploadSample5() throws InterruptedException, IOException, AWTException {

	}

	@Test
	public void verifyUploadSample6() throws InterruptedException, IOException, AWTException {

	}

	@Test
	public void verifyUploadSample7() throws InterruptedException, IOException, AWTException {

	}

	@Test
	public void verifyUploadSample8() throws InterruptedException, IOException, AWTException {

	}

}