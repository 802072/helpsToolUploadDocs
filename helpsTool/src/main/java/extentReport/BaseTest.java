package extentReport;

import java.io.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.internal.TestNGMethod;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.file.UnableSaveSnapshotException;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import dataDriven.DataDrivenDD;
import dataDriven.DataDrivenHT;
import dataDriven.writeDataExcel;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.WebDriverManagerException;
import java.util.HashMap;
import org.testng.annotations.DataProvider;

public class BaseTest {
	public static WebDriver driver;
	public static String screenshotsSubFolderName;
	public static ExtentReports extentReports;
	public static ExtentTest extentTest;
	public static ExtentTest testStepExtentTest;

	DataDrivenHT d = new DataDrivenHT();

	writeDataExcel excelWR = new writeDataExcel();
	
	Date date = new Date();
	String fileDate = date.toString().replace(":", "_").replace(" ", "_");

	@Parameters("browserName")
	@BeforeTest
	public void setup(ITestContext context, @Optional("chrome") String browserName)
			throws IOException, InterruptedException {
		switch (browserName.toLowerCase()) {
		case "chrome":

			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().window().maximize();

			break;

		case "edge":

			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			driver.manage().window().maximize();
			break;
		}

		// Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
		// String device = capabilities.getBrowserName() + " "
		// + capabilities.getVersion().substring(0,
		// capabilities.getVersion().indexOf("."));
		// String author = context.getCurrentXmlTest().getParameter("author");

		extentTest = extentReports.createTest(context.getName());

		// extentTest.assignAuthor(author);
		// extentTest.assignDevice(device);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@BeforeSuite
	public void initialiseExtentReports() {
		ExtentSparkReporter sparkReporter_all = new ExtentSparkReporter("HelpsToolUploadDocuments.html");
		sparkReporter_all.config().setReportName("Helps Tool: Verify File Upload");

		extentReports = new ExtentReports();
		extentReports.attachReporter(sparkReporter_all);

		extentReports.setSystemInfo("OS", System.getProperty("os.name"));
		extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
		extentReports.setSystemInfo("Environment", "Test Environment");
		// extentReports.setSystemInfo("Environment", "Production Environment");
	}

	@AfterSuite
	public void generateExtentReports() throws Exception {
		extentReports.flush();
		// Desktop.getDesktop().browse(new File("ProviderPortalTests.html").toURI());
		// Desktop.getDesktop().browse(new File("FailedTests.html").toURI());
		// excelWR.writeIntoExcel();
	}

	@AfterMethod
	public void checkStatus(Method m, ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {

			String screenshotpath = null;
			screenshotpath = captureScreenshot("failTest.jpg");
			extentTest.fail(m.getName() + " has failed");
			extentTest.log(Status.FAIL, result.getThrowable(),
					MediaEntityBuilder.createScreenCaptureFromPath(screenshotpath).build());
		} else if (result.getStatus() == ITestResult.SUCCESS) {

		}

		extentTest.assignCategory(m.getAnnotation(Test.class).groups());
	}

	public String captureScreenshot(String screenShotName) throws IOException {
//		File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//		String screenshotpath = "./Screenshots/" + screenShotName;
//		File dest = new File(screenshotpath);
//		FileUtils.copyFile(sourceFile, dest);
//		return screenshotpath;

		// Ashot working script
//		Files.createDirectories(Paths.get(System.getProperty("user.dir")+"/Screenshots/"));
//		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(1.50f),5000)).takeScreenshot(driver);
//		String dest = "/Screenshots/" + screenShotName;
//		ImageIO.write(screenshot.getImage(), "PNG", new File(dest));
//		return dest;

		// Shutterbug Working Code
		Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/screenshots/"));
		BufferedImage image = Shutterbug.shootPage(driver, Capture.FULL, true).getImage();
		String dest = "./screenshots/" + screenShotName;
		writeImage(image, "PNG", new File(dest));
		return dest;
	}

	public static void writeImage(BufferedImage imageFile, String extension, File fileToWriteTo) {
		try {
			ImageIO.write(imageFile, extension, fileToWriteTo);
		} catch (IOException e) {
			throw new UnableSaveSnapshotException(e);
		}
	}

	public void login() throws InterruptedException, IOException {
		// open login page
		ArrayList LI001 = d.getData("LI001", "beforeTest");
		driver.get((String) LI001.get(6));
		Thread.sleep(5000);
		String log1 = (String) LI001.get(0) + " " + LI001.get(1);
		extentTest.log(Status.PASS, log1,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log1 +fileDate)).build());

		// Click Login Button
		ArrayList LI002 = d.getData("LI002", "beforeTest");
		WebElement loginButton = driver.findElement(By.xpath((String) LI002.get(5)));
		loginButton.click();
		String log2 = (String) LI002.get(0) + " " + LI002.get(1);
		extentTest.log(Status.PASS, log2,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log2 +fileDate)).build());

		// Click Welcome
		try {
			ArrayList TS067 = d.getData("TS067", "TC01");
			WebElement welcome = driver.findElement(By.xpath((String) TS067.get(6)));
			if (welcome.isDisplayed()) {

				welcome.click();
				String log67 = (String) TS067.get(0) + " " + TS067.get(2);
				extentTest.log(Status.PASS, "Click Welcome",
						MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log67 +fileDate)).build());

				// Click Sign Out
				ArrayList TS068 = d.getData("TS068", "TC01");
				WebElement signOut = driver.findElement(By.xpath((String) TS068.get(6)));
				signOut.click();
				String log68 = (String) TS068.get(0) + " " + TS068.get(2);
				extentTest.log(Status.PASS, "Click Sign Out Button",
						MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log68 +fileDate)).build());

				// Click Login Button
				ArrayList LI002A = d.getData("LI002", "beforeTest");
				WebElement loginButton1 = driver.findElement(By.xpath((String) LI002A.get(5)));
				loginButton1.click();
				// String log2A = (String) LI002.get(0) + " " + LI002.get(1);
				extentTest.log(Status.PASS, log2,
						MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log2 +fileDate)).build());

				// Click Use Another Account
				ArrayList LI003A = d.getData("LI003A", "beforeTest");
				WebElement useAnotherAcc = driver.findElement(By.xpath((String) LI003A.get(5)));
				useAnotherAcc.click();
				String log3A = (String) LI003A.get(0) + " " + LI003A.get(1);
				extentTest.log(Status.PASS, log3A,
						MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log3A +fileDate)).build());
			}
		} catch (Exception e) {

		}

		// enter username
		ArrayList LI003 = d.getData("LI003", "beforeTest");
		WebElement userName = driver.findElement(By.xpath((String) LI003.get(5)));
		userName.sendKeys((String) LI003.get(6));
		String log3 = (String) LI003.get(0) + " " + LI003.get(1);
		extentTest.log(Status.PASS, log3,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log3 +fileDate)).build());

		// click Next
		ArrayList LI004 = d.getData("LI004", "beforeTest");
		WebElement next = driver.findElement(By.xpath((String) LI004.get(5)));
		next.click();
		String log4 = (String) LI004.get(0) + " " + LI004.get(1);
		extentTest.log(Status.PASS, log4,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log4 +fileDate)).build());

		Thread.sleep(5000);
		// enter password
		ArrayList LI005 = d.getData("LI005", "beforeTest");
		WebElement pwd = driver.findElement(By.xpath((String) LI005.get(5)));
		pwd.sendKeys((String) LI005.get(6));
		String log5 = (String) LI005.get(0) + " " + LI005.get(1);
		extentTest.log(Status.PASS, log5,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log5 +fileDate)).build());

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// click Signin
		ArrayList LI006 = d.getData("LI006", "beforeTest");
		WebElement signIn = driver.findElement(By.xpath((String) LI006.get(5)));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", signIn);
		// signIn.click();
		System.out.println("Clicked signin button");
		Thread.sleep(5000);
		String log6 = (String) LI006.get(0) + " " + LI006.get(1);
		extentTest.log(Status.PASS, log6,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log6 +fileDate)).build());

		// Click Yes to "Stay Signed In?"
		try {
			ArrayList LI007 = d.getData("LI007", "beforeTest");
			WebElement no = driver.findElement(By.xpath((String) LI007.get(6)));
			if (no.isDisplayed()) {

				// Click Use Another Account
				no.click();
				Thread.sleep(5000);
				String log7 = (String) LI007.get(0) + " " + LI007.get(1);
				extentTest.log(Status.PASS, log7,
						MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log7 +fileDate)).build());
			}
		} catch (Exception e) {

		}
	}

	@SuppressWarnings("rawtypes")
	public void uploadAndSubmitFile(String rowName) throws InterruptedException, IOException, AWTException {
		ArrayList listName = d.getData(rowName, "filePath");
		String fileName = (String) listName.get(0);
		ArrayList listPath = d.getData(rowName, "filePath");
		String path = System.getProperty("user.dir") + (String) listPath.get(1);

		// Click Import HCS Assessment
		ArrayList UF001 = d.getData("UF001", "beforeTest");
		WebElement importBtn = driver.findElement(By.xpath((String) UF001.get(5)));
		String log8 = (String) UF001.get(0) + " " + UF001.get(1);
		extentTest.log(Status.PASS, log8,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log8 + fileName +fileDate)).build());

		importBtn.click();
		Thread.sleep(5000);

		// Click Choose File
		ArrayList UF002 = d.getData("UF002", "beforeTest");
		// String fileName = (String) UF002.get(6);
		WebElement chooseFileBtn = driver.findElement(By.xpath((String) UF002.get(5)));
		String log9 = (String) UF002.get(0) + " " + UF002.get(1);
		extentTest.log(Status.PASS, (String) UF002.get(0) + " Choose File: " + fileName,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log9 + fileName +fileDate)).build());

		// log Upload File
		ArrayList UF003 = d.getData("UF003", "beforeTest");
		// String path= (String) UF003.get(6);
		String log10 = (String) UF003.get(0) + " " + UF003.get(1);
		extentTest.log(Status.PASS, (String) UF003.get(0) + " " + UF003.get(1) + " " + fileName,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log10 + fileName +fileDate)).build());
		chooseFileBtn.sendKeys(path);
		//
//		Actions builder = new Actions(driver);
//		builder.moveToElement(chooseFileBtn).click().build().perform();
//
//		// Upload Function
//		Thread.sleep(5000);
//		Robot rb = new Robot();
//		StringSelection str = new StringSelection(path);
//		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
//		Thread.sleep(5000);
//
//		// press Contol+V for pasting
//		rb.keyPress(KeyEvent.VK_CONTROL);
//		rb.keyPress(KeyEvent.VK_V);
//		Thread.sleep(2000);
//
//		// release Contol+V for pasting
//		rb.keyRelease(KeyEvent.VK_CONTROL);
//		rb.keyRelease(KeyEvent.VK_V);
//		Thread.sleep(2000);
//
//		// for pressing and releasing Enter
//		rb.keyPress(KeyEvent.VK_ENTER);
//		rb.keyRelease(KeyEvent.VK_ENTER);
//
//		Thread.sleep(5000);

		// Click Submit File
		ArrayList UF004 = d.getData("UF004", "beforeTest");
		WebElement submitBtn = driver.findElement(By.xpath((String) UF004.get(5)));
		submitBtn.click();
		String log11 = (String) UF004.get(0) + " " + UF004.get(1);
		extentTest.log(Status.PASS, (String) UF004.get(0) + " Submit File: " + fileName,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log11 + fileName +fileDate)).build());

		Thread.sleep(5000);

	}

	// Verify field data
	public ArrayList<String> verifyInfo(String sheetName, String sampleSheet)
			throws InterruptedException, IOException, AWTException {
		ArrayList<String> UIlist = new ArrayList<String>();
		ArrayList<String> TSIDlist = new ArrayList<String>();
		ArrayList<String> allList = new ArrayList<String>();
		allList.addAll(UIlist);
		allList.addAll(TSIDlist);

//		// First Name
//		ArrayList TS001 = d.getData("TS001", sheetName);
//		WebElement firstName = driver.findElement(By.xpath((String) TS001.get(6)));
//		UIlist.add(firstName.getAttribute("value"));
//		TSIDlist.add("TS001");
//		String log1 = (String) TS001.get(0) + " " + TS001.get(2);
//		extentTest.log(Status.PASS, log1 + ". Expected Value is: " + (String) TS001.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log1+sheetName+sheetName+".jpg")).build());
//
//		// Last Name
//		ArrayList TS002 = d.getData("TS002", sheetName);
//		WebElement lastName = driver.findElement(By.xpath((String) TS002.get(6)));
//		////// assertEquals((String) TS002.get(7), lastName.getAttribute("value"));
//		String log2 = (String) TS002.get(0) + " " + TS002.get(2);
//		UIlist.add(lastName.getAttribute("value"));
//		TSIDlist.add("TS002");
//		extentTest.log(Status.PASS, log2 + ". Expected Value is: " + (String) TS002.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log2 + sheetName+".jpg")).build());
//
//		// Verify Medicaid ID
//		ArrayList TS003 = d.getData("TS003", sheetName);
//		WebElement medicaidID = driver.findElement(By.xpath((String) TS003.get(6)));
//		////// assertEquals((String) TS003.get(7), medicaidID.getAttribute("value"));
//		String log3 = (String) TS003.get(0) + " " + TS003.get(2);
//		UIlist.add(medicaidID.getAttribute("value"));
//		TSIDlist.add("TS003");
//		extentTest.log(Status.PASS, log3 + ". Expected Value is: " + (String) TS003.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log3 + sheetName+".jpg")).build());
//
//		// Verify NFLOC
//		ArrayList TS004 = d.getData("TS004", sheetName);
//		WebElement NFLOC = driver.findElement(By.xpath((String) TS004.get(6)));
//		//// assertEquals((String) TS004.get(7), NFLOC.getAttribute("value"));
//		String log4 = (String) TS004.get(0) + " " + TS004.get(2);
//		UIlist.add(NFLOC.getAttribute("value"));
//		TSIDlist.add("TS004");
//		extentTest.log(Status.PASS, log4 + ". Expected Value is: " + (String) TS004.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log4 + sheetName+".jpg")).build());
//
//		// Verify Date of Assessment
//		ArrayList TS005 = d.getData("TS005", sheetName);
//		WebElement dateOfAssessment = driver.findElement(By.xpath((String) TS005.get(6)));
//		System.out.println(dateOfAssessment.getAttribute("value"));
//		//// assertEquals((String) TS005.get(7),
//		//// dateOfAssessment.getAttribute("value"));
//		String log5 = (String) TS005.get(0) + " " + TS005.get(2);
//		UIlist.add(dateOfAssessment.getAttribute("value"));
//		TSIDlist.add("TS005");
//		extentTest.log(Status.PASS, log5 + ". Expected Value is: " + (String) TS005.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log5 + sheetName+".jpg")).build());
//
//		// Verify Nurse Assessor
//		ArrayList TS006 = d.getData("TS006", sheetName);
//		WebElement nurseAssessor = driver.findElement(By.xpath((String) TS006.get(6)));
//		//// assertEquals((String) TS006.get(7), nurseAssessor.getAttribute("value"));
//		String log6 = (String) TS006.get(0) + " " + TS006.get(2);
//		UIlist.add(nurseAssessor.getAttribute("value"));
//		TSIDlist.add("TS006");
//		extentTest.log(Status.PASS, log6 + ". Expected Value is: " + (String) TS006.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log6 + sheetName+".jpg")).build());
//
//		// Verify Assessment Reason
//		ArrayList TS007 = d.getData("TS007", sheetName);
//		WebElement assessmentReason = driver.findElement(By.xpath((String) TS007.get(6)));
//		assessmentReason.click();
//		String optionItemAR = new Select(assessmentReason).getFirstSelectedOption().getText();
//		////// assertEquals((String) TS007.get(7), optionItemAR);
//		String log7 = (String) TS007.get(0) + " " + TS007.get(2);
//		UIlist.add(optionItemAR);
//		TSIDlist.add("TS007");
//		extentTest.log(Status.PASS, log7 + ". Expected Value is: " + (String) TS007.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log7 + sheetName+".jpg")).build());
//
//		// Click Mutual Case Dropdown
//		ArrayList TS008 = d.getData("TS008", sheetName);
//		WebElement mutualCase = driver.findElement(By.xpath((String) TS008.get(6)));
//		mutualCase.click();
//		String optionItemMc = new Select(mutualCase).getFirstSelectedOption().getText();
//		////// assertEquals((String) TS008.get(7), optionItemMc);
//		UIlist.add(optionItemMc);
//		TSIDlist.add("TS008");
//		String log8 = (String) TS008.get(0) + " " + TS008.get(2);
//		extentTest.log(Status.PASS, log8 + ". Expected Value is: " + (String) TS008.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log8 + sheetName+".jpg")).build());
//
//		// Select Yes from Mutual Case Dropdown
//		ArrayList TS009 = d.getData("TS009", sheetName);
//		WebElement yes = driver.findElement(By.xpath((String) TS009.get(6)));
//		yes.click();
//		//// assertEquals((String) TS009.get(7), yes.getText());
//		UIlist.add(yes.getText());
//		TSIDlist.add("TS009");
//		String log9 = (String) TS009.get(0) + " " + TS009.get(2);
//		extentTest.log(Status.PASS, log9 + ". Expected Value is: " + (String) TS009.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log9 + sheetName+".jpg")).build());
//
//		// Type '12345' into the Mutual Case Medicaid ID field.
//		ArrayList TS010 = d.getData("TS010", sheetName);
//		WebElement mutualCaseMedID = driver.findElement(By.xpath((String) TS010.get(6)));
//		mutualCaseMedID.sendKeys((String) TS010.get(7));
//		String log10 = (String) TS010.get(0) + " " + TS010.get(2);
//		UIlist.add(mutualCaseMedID.getAttribute("value"));
//		TSIDlist.add("TS010");
//		extentTest.log(Status.PASS, log10 + ". Expected Value is: " + (String) TS010.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log10 + sheetName+".jpg")).build());
//
//		// Verify dropdown value for Cognitive Skills for Daily Decision Making
//		ArrayList TS011 = d.getData("TS011", sheetName);
//		WebElement cognitiveSkillDM = driver.findElement(By.xpath((String) TS011.get(6)));
//		cognitiveSkillDM.click();
//		String optionItemCs = new Select(cognitiveSkillDM).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS011.get(7), optionItemCs);
//		String log11 = (String) TS011.get(0) + " " + TS011.get(2);
//		extentTest.log(Status.PASS, log11 + ". Expected Value is: " + (String) TS011.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log11 + sheetName+".jpg")).build());
//		TSIDlist.add("TS011");
//		UIlist.add(optionItemCs);
//
//		// Verify dropdown value for Psychiatric Anxiety
//		ArrayList TS012 = d.getData("TS012", sheetName);
//		WebElement psychiatricAnxiety = driver.findElement(By.xpath((String) TS012.get(6)));
//		psychiatricAnxiety.click();
//		String optionItemPa = new Select(psychiatricAnxiety).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS012.get(7), optionItemPa);
//		String log12 = (String) TS012.get(0) + " " + TS012.get(2);
//		extentTest.log(Status.PASS, log12 + ". Expected Value is: " + (String) TS012.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log12 + sheetName+".jpg")).build());
//		UIlist.add(optionItemPa);
//		TSIDlist.add("TS012");
//
//		// Verify dropdown value for Wandering
//		ArrayList TS013 = d.getData("TS013", sheetName);
//		WebElement wandering = driver.findElement(By.xpath((String) TS013.get(6)));
//		wandering.click();
//		String optionItemWa = new Select(wandering).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS013.get(7), optionItemWa);
//		String log13 = (String) TS013.get(0) + " " + TS013.get(2);
//		extentTest.log(Status.PASS, log13 + ". Expected Value is: " + (String) TS013.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log13 + sheetName+".jpg")).build());
//		UIlist.add(optionItemWa);
//		TSIDlist.add("TS013");
//
//		// Verify dropdown value for Psychiatric Depression
//		ArrayList TS014 = d.getData("TS014", sheetName);
//		WebElement psychiatricDepression = driver.findElement(By.xpath((String) TS014.get(6)));
//		psychiatricDepression.click();
//		String optionItemPd = new Select(psychiatricDepression).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS014.get(7), optionItemPd);
//		String log14 = (String) TS014.get(0) + " " + TS014.get(2);
//		extentTest.log(Status.PASS, log14 + ". Expected Value is: " + (String) TS014.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log14 + sheetName+".jpg")).build());
//		UIlist.add(optionItemPd);
//		TSIDlist.add("TS014");
//
//		// Verify dropdown value for Verbal Abuse
//		ArrayList TS015 = d.getData("TS015", sheetName);
//		WebElement verbalAbuse = driver.findElement(By.xpath((String) TS015.get(6)));
//		verbalAbuse.click();
//		String optionItemVa = new Select(verbalAbuse).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS015.get(7), optionItemVa);
//		String log15 = (String) TS015.get(0) + " " + TS015.get(2);
//		extentTest.log(Status.PASS, log15 + ". Expected Value is: " + (String) TS015.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log15 + sheetName+".jpg")).build());
//		UIlist.add(optionItemVa);
//		TSIDlist.add("TS015");
//
//		// Verify dropdown value for Psychiatric Schizophrenia
//		ArrayList TS016 = d.getData("TS016", sheetName);
//		WebElement psychiatricSchizophrenia = driver.findElement(By.xpath((String) TS016.get(6)));
//		psychiatricSchizophrenia.click();
//		String optionItemPs = new Select(psychiatricSchizophrenia).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS016.get(7), optionItemPs);
//		String log16 = (String) TS016.get(0) + " " + TS016.get(2);
//		extentTest.log(Status.PASS, log16 + ". Expected Value is: " + (String) TS016.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log16 + sheetName+".jpg")).build());
//		UIlist.add(optionItemPs);
//		TSIDlist.add("TS016");
//
//		// Verify dropdown value for Physical Abuse
//		ArrayList TS017 = d.getData("TS017", sheetName);
//		WebElement physicalAbuse = driver.findElement(By.xpath((String) TS017.get(6)));
//		physicalAbuse.click();
//		String optionItemPab = new Select(physicalAbuse).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS017.get(7), optionItemPab);
//		String log17 = (String) TS017.get(0) + " " + TS017.get(2);
//		extentTest.log(Status.PASS, log17 + ". Expected Value is: " + (String) TS017.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log17 + sheetName+".jpg")).build());
//		UIlist.add(optionItemPab);
//		TSIDlist.add("TS017");
//
//		// Verify dropdown value for Dyspnea
//		ArrayList TS018 = d.getData("TS018", sheetName);
//		WebElement dyspnea = driver.findElement(By.xpath((String) TS018.get(6)));
//		dyspnea.click();
//		String optionItemDy = new Select(dyspnea).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS018.get(7), optionItemDy);
//		String log18 = (String) TS018.get(0) + " " + TS018.get(2);
//		extentTest.log(Status.PASS, log18 + ". Expected Value is: " + (String) TS018.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log18 + sheetName+".jpg")).build());
//		UIlist.add(optionItemDy);
//		TSIDlist.add("TS018");
//
//		// Verify dropdown value for Socially inappropriate or disruptive behavior
//		ArrayList TS019 = d.getData("TS019", sheetName);
//		WebElement sociallyInappBehv = driver.findElement(By.xpath((String) TS019.get(6)));
//		sociallyInappBehv.click();
//		String optionItemSib = new Select(sociallyInappBehv).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS019.get(7), optionItemSib);
//		String log19 = (String) TS019.get(0) + " " + TS019.get(2);
//		extentTest.log(Status.PASS, log19 + ". Expected Value is: " + (String) TS019.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log19 + sheetName+".jpg")).build());
//		UIlist.add(optionItemSib);
//		TSIDlist.add("TS019");
//
//		// Verify dropdown value for Fatigue
//		ArrayList TS020 = d.getData("TS020", sheetName);
//		WebElement fatigue = driver.findElement(By.xpath((String) TS020.get(6)));
//		fatigue.click();
//		String optionItemFa = new Select(fatigue).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS020.get(7), optionItemFa);
//		String log20 = (String) TS020.get(0) + " " + TS020.get(2);
//		extentTest.log(Status.PASS, log20 + ". Expected Value is: " + (String) TS020.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log20 + sheetName+".jpg")).build());
//		UIlist.add(optionItemFa);
//		TSIDlist.add("TS020");
//
//		// Verify dropdown value for Inappropriate public sexual behavior or public
//		// disrobing
//		ArrayList TS021 = d.getData("TS021", sheetName);
//		WebElement inappSexualBehv = driver.findElement(By.xpath((String) TS021.get(6)));
//		inappSexualBehv.click();
//		String optionItemIsb = new Select(inappSexualBehv).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS021.get(7), optionItemIsb);
//		String log21 = (String) TS021.get(0) + " " + TS021.get(2);
//		extentTest.log(Status.PASS, log21 + ". Expected Value is: " + (String) TS021.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log21 + sheetName+".jpg")).build());
//		UIlist.add(optionItemIsb);
//		TSIDlist.add("TS021");
//
//		// Verify dropdown value for Pain
//		ArrayList TS022 = d.getData("TS022", sheetName);
//		WebElement pain = driver.findElement(By.xpath((String) TS022.get(6)));
//		pain.click();
//		String optionItemPain = new Select(pain).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS022.get(7), optionItemPain);
//		String log22 = (String) TS022.get(0) + " " + TS022.get(2);
//		extentTest.log(Status.PASS, log22 + ". Expected Value is: " + (String) TS022.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log22 + sheetName+".jpg")).build());
//		UIlist.add(optionItemPain);
//		TSIDlist.add("TS022");
//
//		// Verify dropdown value for Resist care
//		ArrayList TS023 = d.getData("TS023", sheetName);
//		WebElement resistCare = driver.findElement(By.xpath((String) TS023.get(6)));
//		resistCare.click();
//		String optionItemRc = new Select(resistCare).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS023.get(7), optionItemRc);
//		String log23 = (String) TS023.get(0) + " " + TS023.get(2);
//		extentTest.log(Status.PASS, log23 + ". Expected Value is: " + (String) TS023.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log23 + sheetName+".jpg")).build());
//		UIlist.add(optionItemRc);
//		TSIDlist.add("TS023");
//
//		// Verify the dropdown value for the Meal Preparation IADL
//		ArrayList TS024 = d.getData("TS024", sheetName);
//		WebElement mealPrepIADL = driver.findElement(By.xpath((String) TS024.get(6)));
//		mealPrepIADL.click();
//		String optionItemMp = new Select(mealPrepIADL).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS024.get(7), optionItemMp);
//		String log24 = (String) TS024.get(0) + " " + TS024.get(2);
//		extentTest.log(Status.PASS, log24 + ". Expected Value is: " + (String) TS024.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log24 + sheetName+".jpg")).build());
//		UIlist.add(optionItemMp);
//		TSIDlist.add("TS024");
//
//		// Verify the Weekly Total Minutes assigned to the Meal Preparation IADL
//		ArrayList TS025 = d.getData("TS025", sheetName);
//		WebElement weeklyTotalMinsMeal = driver.findElement(By.xpath((String) TS025.get(6)));
//		//// assertEquals((String) TS025.get(7),
//		//// weeklyTotalMinsMeal.getAttribute("value"));
//		String log25 = (String) TS025.get(0) + " " + TS025.get(2);
//		extentTest.log(Status.PASS, log25 + ". Expected Value is: " + (String) TS025.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log25 + sheetName+".jpg")).build());
//		UIlist.add(weeklyTotalMinsMeal.getAttribute("value"));
//		TSIDlist.add("TS025");
//
//		// Verify the dropdown value for the Ordinary Housework IADL
//		ArrayList TS026 = d.getData("TS026", sheetName);
//		WebElement houseworkIADL = driver.findElement(By.xpath((String) TS026.get(6)));
//		houseworkIADL.click();
//		String optionItemHw = new Select(houseworkIADL).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS026.get(7), optionItemHw);
//		String log26 = (String) TS026.get(0) + " " + TS026.get(2);
//		extentTest.log(Status.PASS, log26 + ". Expected Value is: " + (String) TS026.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log26 + sheetName+".jpg")).build());
//		UIlist.add(optionItemHw);
//		TSIDlist.add("TS026");
//
//		// Verify the Weekly Total Minutes assigned to the Ordinary Housework IADL
//		ArrayList TS027 = d.getData("TS027", sheetName);
//		WebElement weeklyTotalMinsHousework = driver.findElement(By.xpath((String) TS027.get(6)));
//		//// assertEquals((String) TS027.get(7),
//		//// weeklyTotalMinsHousework.getAttribute("value"));
//		String log27 = (String) TS027.get(0) + " " + TS027.get(2);
//		extentTest.log(Status.PASS, log27 + ". Expected Value is: " + (String) TS027.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log27 + sheetName+".jpg")).build());
//		UIlist.add(weeklyTotalMinsHousework.getAttribute("value"));
//		TSIDlist.add("TS027");
//
//		// Verify the dropdown value for the Shopping IADL
//		ArrayList TS028 = d.getData("TS028", sheetName);
//		WebElement shoppingIADL = driver.findElement(By.xpath((String) TS028.get(6)));
//		shoppingIADL.click();
//		String optionItemSh = new Select(shoppingIADL).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS028.get(7), optionItemSh);
//		String log28 = (String) TS028.get(0) + " " + TS028.get(2);
//		extentTest.log(Status.PASS, log28 + ". Expected Value is: " + (String) TS028.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log28 + sheetName+".jpg")).build());
//		UIlist.add(optionItemSh);
//		TSIDlist.add("TS028");
//
//		// Verify the Weekly Total Minutes assigned to the Shopping IADL
//		ArrayList TS029 = d.getData("TS029", sheetName);
//		WebElement weeklyTotalMinsShopping = driver.findElement(By.xpath((String) TS029.get(6)));
//		//// assertEquals((String) TS029.get(7),
//		//// weeklyTotalMinsShopping.getAttribute("value"));
//		String log29 = (String) TS029.get(0) + " " + TS029.get(2);
//		extentTest.log(Status.PASS, log29 + ". Expected Value is: " + (String) TS029.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log29 + sheetName+".jpg")).build());
//		UIlist.add(weeklyTotalMinsShopping.getAttribute("value"));
//		TSIDlist.add("TS029");
//
//		// Verify the dropdown value for the Bathing ADL
//		ArrayList TS030 = d.getData("TS030", sheetName);
//		WebElement bathingADL = driver.findElement(By.xpath((String) TS030.get(6)));
//		bathingADL.click();
//		String optionItemBa = new Select(bathingADL).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS030.get(7), optionItemBa);
//		String log30 = (String) TS030.get(0) + " " + TS030.get(2);
//		extentTest.log(Status.PASS, log30 + ". Expected Value is: " + (String) TS030.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log30 + sheetName+".jpg")).build());
//		UIlist.add(optionItemBa);
//		TSIDlist.add("TS030");
//
//		// Verify the Mins per day assigned to the Bathing ADL
//		ArrayList TS031 = d.getData("TS031", sheetName);
//		WebElement dailyMinsbathingADL = driver.findElement(By.xpath((String) TS031.get(6)));
//		//// assertEquals((String) TS031.get(7),
//		//// dailyMinsbathingADL.getAttribute("value"));
//		String log31 = (String) TS031.get(0) + " " + TS031.get(2);
//		extentTest.log(Status.PASS, log31 + ". Expected Value is: " + (String) TS031.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log31 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinsbathingADL.getAttribute("value"));
//		TSIDlist.add("TS031");
//
//		// Verify the Total Minutes assigned to the Bathing ADL
//		ArrayList TS032 = d.getData("TS032", sheetName);
//		WebElement totalMinsbathingADL = driver.findElement(By.xpath((String) TS032.get(6)));
//		//// assertEquals((String) TS032.get(7),
//		//// totalMinsbathingADL.getAttribute("value"));
//		String log32 = (String) TS032.get(0) + " " + TS032.get(2);
//		extentTest.log(Status.PASS, log32 + ". Expected Value is: " + (String) TS032.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log32 + sheetName+".jpg")).build());
//		UIlist.add(totalMinsbathingADL.getAttribute("value"));
//		TSIDlist.add("TS032");
//
//		// Verify the dropdown value for the Personal Hygiene ADL
//		ArrayList TS033 = d.getData("TS033", sheetName);
//		WebElement personalHygADL = driver.findElement(By.xpath((String) TS033.get(6)));
//		personalHygADL.click();
//		String optionItemPh = new Select(personalHygADL).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS033.get(7), optionItemPh);
//		String log33 = (String) TS033.get(0) + " " + TS033.get(2);
//		extentTest.log(Status.PASS, log33 + ". Expected Value is: " + (String) TS033.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log33 + sheetName+".jpg")).build());
//		UIlist.add(optionItemPh);
//		TSIDlist.add("TS033");
//
//		// Verify the Mins per day assigned to the Personal Hygiene ADL
//		ArrayList TS034 = d.getData("TS034", sheetName);
//		WebElement dailyMinsPH = driver.findElement(By.xpath((String) TS034.get(6)));
//		//// assertEquals((String) TS034.get(7), dailyMinsPH.getAttribute("value"));
//		String log34 = (String) TS034.get(0) + " " + TS034.get(2);
//		extentTest.log(Status.PASS, log34 + ". Expected Value is: " + (String) TS034.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log34 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinsPH.getAttribute("value"));
//		TSIDlist.add("TS034");
//
//		// Verify the Total Minutes assigned to the Personal Hygiene ADL
//		ArrayList TS035 = d.getData("TS035", sheetName);
//		WebElement totalMinsPH = driver.findElement(By.xpath((String) TS035.get(6)));
//		//// assertEquals((String) TS035.get(7), totalMinsPH.getAttribute("value"));
//		String log35 = (String) TS035.get(0) + " " + TS035.get(2);
//		extentTest.log(Status.PASS, log35 + ". Expected Value is: " + (String) TS035.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log35 + sheetName+".jpg")).build());
//		UIlist.add(totalMinsPH.getAttribute("value"));
//		TSIDlist.add("TS035");
//
//		// Verify the dropdown value for the Dressing Upper Body ADL
//		ArrayList TS036 = d.getData("TS036", sheetName);
//		WebElement dressingUB = driver.findElement(By.xpath((String) TS036.get(6)));
//		dressingUB.click();
//		String optionItemUb = new Select(dressingUB).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS036.get(7), optionItemUb);
//		String log36 = (String) TS036.get(0) + " " + TS036.get(2);
//		extentTest.log(Status.PASS, log36 + ". Expected Value is: " + (String) TS036.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log36 + sheetName+".jpg")).build());
//		UIlist.add(optionItemUb);
//		TSIDlist.add("TS036");
//
//		// Verify the Mins per day assigned to the Dressing Upper Body ADL
//		ArrayList TS037 = d.getData("TS037", sheetName);
//		WebElement dailyMinsDressingUB = driver.findElement(By.xpath((String) TS037.get(6)));
//		//// assertEquals((String) TS037.get(7),
//		//// dailyMinsDressingUB.getAttribute("value"));
//		String log37 = (String) TS037.get(0) + " " + TS037.get(2);
//		extentTest.log(Status.PASS, log37 + ". Expected Value is: " + (String) TS037.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log37 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinsDressingUB.getAttribute("value"));
//		TSIDlist.add("TS037");
//
//		// Verify the Total Minutes assigned to the Dressing Upper Body ADL
//		ArrayList TS038 = d.getData("TS038", sheetName);
//		WebElement totalMinsDressingUB = driver.findElement(By.xpath((String) TS038.get(6)));
//		//// assertEquals((String) TS038.get(7),
//		//// totalMinsDressingUB.getAttribute("value"));
//		String log38 = (String) TS038.get(0) + " " + TS038.get(2);
//		extentTest.log(Status.PASS, log38 + ". Expected Value is: " + (String) TS038.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log38 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinsDressingUB.getAttribute("value"));
//		TSIDlist.add("TS038");
//
//		// Verify the dropdown value for the Dressing Lower Body ADL
//		ArrayList TS039 = d.getData("TS039", sheetName);
//		WebElement dressingLB = driver.findElement(By.xpath((String) TS039.get(6)));
//		dressingLB.click();
//		String optionItemLb = new Select(dressingLB).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS039.get(7), optionItemLb);
//		String log39 = (String) TS039.get(0) + " " + TS039.get(2);
//		extentTest.log(Status.PASS, log39 + ". Expected Value is: " + (String) TS039.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log39 + sheetName+".jpg")).build());
//		UIlist.add(optionItemLb);
//		TSIDlist.add("TS039");
//
//		// Verify the Mins per day assigned to the Dressing Lower Body ADL
//		ArrayList TS040 = d.getData("TS040", sheetName);
//		WebElement dailyMinsDressingLB = driver.findElement(By.xpath((String) TS040.get(6)));
//		//// assertEquals((String) TS040.get(7),
//		//// dailyMinsDressingLB.getAttribute("value"));
//		String log40 = (String) TS040.get(0) + " " + TS040.get(2);
//		extentTest.log(Status.PASS, log40 + ". Expected Value is: " + (String) TS040.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log40 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinsDressingLB.getAttribute("value"));
//		TSIDlist.add("TS040");
//
//		// Verify the Total Minutes assigned to the Dressing Lower Body ADL
//		ArrayList TS041 = d.getData("TS041", sheetName);
//		WebElement totalMinsDressingLB = driver.findElement(By.xpath((String) TS041.get(6)));
//		//// assertEquals((String) TS041.get(7),
//		//// totalMinsDressingLB.getAttribute("value"));
//		String log41 = (String) TS041.get(0) + " " + TS041.get(2);
//		extentTest.log(Status.PASS, log41 + ". Expected Value is: " + (String) TS041.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log41 + sheetName+".jpg")).build());
//		UIlist.add(totalMinsDressingLB.getAttribute("value"));
//		TSIDlist.add("TS041");
//
//		// Verify the dropdown value for the Locomotion ADL
//		ArrayList TS042 = d.getData("TS042", sheetName);
//		WebElement locomotionADL = driver.findElement(By.xpath((String) TS042.get(6)));
//		locomotionADL.click();
//		String optionItemLo = new Select(locomotionADL).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS042.get(7), optionItemLo);
//		String log42 = (String) TS042.get(0) + " " + TS042.get(2);
//		extentTest.log(Status.PASS, log42 + ". Expected Value is: " + (String) TS042.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log42 + sheetName+".jpg")).build());
//		UIlist.add(optionItemLo);
//		TSIDlist.add("TS042");
//
//		// Verify the Mins per day assigned to the Locomotion ADL
//		ArrayList TS043 = d.getData("TS043", sheetName);
//		WebElement dailyMinsLocoADL = driver.findElement(By.xpath((String) TS043.get(6)));
//		//// assertEquals((String) TS043.get(7),
//		//// dailyMinsLocoADL.getAttribute("value"));
//		String log43 = (String) TS043.get(0) + " " + TS043.get(2);
//		extentTest.log(Status.PASS, log43 + ". Expected Value is: " + (String) TS043.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log43 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinsLocoADL.getAttribute("value"));
//		TSIDlist.add("TS043");
//
//		// Verify the Total Minutes assigned to the Locomotion ADL
//		ArrayList TS044 = d.getData("TS044", sheetName);
//		WebElement totalMinsLocoADL = driver.findElement(By.xpath((String) TS044.get(6)));
//		//// assertEquals((String) TS044.get(7),
//		//// totalMinsLocoADL.getAttribute("value"));
//		String log44 = (String) TS044.get(0) + " " + TS044.get(2);
//		extentTest.log(Status.PASS, log44 + ". Expected Value is: " + (String) TS044.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log44 + sheetName+".jpg")).build());
//		UIlist.add(totalMinsLocoADL.getAttribute("value"));
//		TSIDlist.add("TS044");
//
//		// Verify the dropdown value for the Transfer Toilet ADL
//		ArrayList TS045 = d.getData("TS045", sheetName);
//		WebElement transferToiletADL = driver.findElement(By.xpath((String) TS045.get(6)));
//		transferToiletADL.click();
//		String optionItemTt = new Select(transferToiletADL).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS045.get(7), optionItemTt);
//		String log45 = (String) TS045.get(0) + " " + TS045.get(2);
//		extentTest.log(Status.PASS, log45 + ". Expected Value is: " + (String) TS045.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log45 + sheetName+".jpg")).build());
//		UIlist.add(optionItemTt);
//		TSIDlist.add("TS045");
//
//		// Verify the Mins per day assigned to the Transfer Toilet ADL
//		ArrayList TS046 = d.getData("TS046", sheetName);
//		WebElement dailyMinstransferToilet = driver.findElement(By.xpath((String) TS046.get(6)));
//		//// assertEquals((String) TS046.get(7),
//		//// dailyMinstransferToilet.getAttribute("value"));
//		String log46 = (String) TS046.get(0) + " " + TS046.get(2);
//		extentTest.log(Status.PASS, log46 + ". Expected Value is: " + (String) TS046.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log46 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinstransferToilet.getAttribute("value"));
//		TSIDlist.add("TS046");
//
//		// Verify the Total Minutes assigned to the Transfer Toilet ADL
//		ArrayList TS047 = d.getData("TS047", sheetName);
//		WebElement totalMinstransferToilet = driver.findElement(By.xpath((String) TS047.get(6)));
//		//// assertEquals((String) TS047.get(7),
//		//// totalMinstransferToilet.getAttribute("value"));
//		String log47 = (String) TS047.get(0) + " " + TS047.get(2);
//		extentTest.log(Status.PASS, log47 + ". Expected Value is: " + (String) TS047.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log47 + sheetName+".jpg")).build());
//		UIlist.add(totalMinstransferToilet.getAttribute("value"));
//		TSIDlist.add("TS047");
//
//		// Verify the dropdown value for the Toileting ADL
//		ArrayList TS048 = d.getData("TS048", sheetName);
//		WebElement toileting = driver.findElement(By.xpath((String) TS048.get(6)));
//		toileting.click();
//		String optionItemTo = new Select(toileting).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS048.get(7), optionItemTo);
//		String log48 = (String) TS048.get(0) + " " + TS048.get(2);
//		extentTest.log(Status.PASS, log48 + ". Expected Value is: " + (String) TS048.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log48 + sheetName+".jpg")).build());
//		UIlist.add(optionItemTo);
//		TSIDlist.add("TS048");
//
//		// Verify the Mins per day assigned to the Toileting ADL
//		ArrayList TS049 = d.getData("TS049", sheetName);
//		WebElement dailyMinsToileting = driver.findElement(By.xpath((String) TS049.get(6)));
//		//// assertEquals((String) TS049.get(7),
//		//// dailyMinsToileting.getAttribute("value"));
//		String log49 = (String) TS049.get(0) + " " + TS049.get(2);
//		extentTest.log(Status.PASS, log49 + ". Expected Value is: " + (String) TS049.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log49 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinsToileting.getAttribute("value"));
//		TSIDlist.add("TS049");
//
//		// Verify the Total Minutes assigned to the Toileting ADL
//		ArrayList TS050 = d.getData("TS050", sheetName);
//		WebElement totalMinsToileting = driver.findElement(By.xpath((String) TS050.get(6)));
//		//// assertEquals((String) TS050.get(7),
//		//// totalMinsToileting.getAttribute("value"));
//		String log50 = (String) TS050.get(0) + " " + TS050.get(2);
//		extentTest.log(Status.PASS, log50 + ". Expected Value is: " + (String) TS050.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log50 + sheetName+".jpg")).build());
//		UIlist.add(totalMinsToileting.getAttribute("value"));
//		TSIDlist.add("TS050");
//
//		// Verify the dropdown value for the Bed Mobility ADL
//		ArrayList TS051 = d.getData("TS051", sheetName);
//		WebElement bedMobility = driver.findElement(By.xpath((String) TS051.get(6)));
//		bedMobility.click();
//		String optionItemBm = new Select(bedMobility).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS051.get(7), optionItemBm);
//		String log51 = (String) TS051.get(0) + " " + TS051.get(2);
//		extentTest.log(Status.PASS, log51 + ". Expected Value is: " + (String) TS051.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log51 + sheetName+".jpg")).build());
//		UIlist.add(optionItemBm);
//		TSIDlist.add("TS051");
//
//		// Verify the Mins per day assigned to the Bed Mobility ADL
//		ArrayList TS052 = d.getData("TS052", sheetName);
//		WebElement dailyMinsBedMobility = driver.findElement(By.xpath((String) TS052.get(6)));
//		//// assertEquals((String) TS052.get(7),
//		//// dailyMinsBedMobility.getAttribute("value"));
//		String log52 = (String) TS052.get(0) + " " + TS052.get(2);
//		extentTest.log(Status.PASS, log52 + ". Expected Value is: " + (String) TS052.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log52 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinsBedMobility.getAttribute("value"));
//		TSIDlist.add("TS052");
//
//		// Verify the Total Minutes assigned to the Bed Mobility ADL
//		ArrayList TS053 = d.getData("TS053", sheetName);
//		WebElement totalMinsBedMobility = driver.findElement(By.xpath((String) TS053.get(6)));
//		//// assertEquals((String) TS053.get(7),
//		//// totalMinsBedMobility.getAttribute("value"));
//		String log53 = (String) TS053.get(0) + " " + TS053.get(2);
//		extentTest.log(Status.PASS, log53 + ". Expected Value is: " + (String) TS053.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log53 + sheetName+".jpg")).build());
//		UIlist.add(totalMinsBedMobility.getAttribute("value"));
//		TSIDlist.add("TS053");
//
//		// Verify the dropdown value for the Eating ADL
//		ArrayList TS054 = d.getData("TS054", sheetName);
//		WebElement eatingADL = driver.findElement(By.xpath((String) TS054.get(6)));
//		eatingADL.click();
//		String optionItemEa = new Select(eatingADL).getFirstSelectedOption().getText();
//		//// assertEquals((String) TS054.get(7), optionItemEa);
//		String log54 = (String) TS054.get(0) + " " + TS054.get(2);
//		extentTest.log(Status.PASS, log54 + ". Expected Value is: " + (String) TS054.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log54 + sheetName+".jpg")).build());
//		UIlist.add(optionItemEa);
//		TSIDlist.add("TS054");
//
//		// Verify the Mins per day assigned to the Eating ADL
//		ArrayList TS055 = d.getData("TS055", sheetName);
//		WebElement dailyMinsEatingADL = driver.findElement(By.xpath((String) TS055.get(6)));
//		//// assertEquals((String) TS055.get(7),
//		//// dailyMinsEatingADL.getAttribute("value"));
//		String log55 = (String) TS055.get(0) + " " + TS055.get(2);
//		extentTest.log(Status.PASS, log55 + ". Expected Value is: " + (String) TS055.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log55 + sheetName+".jpg")).build());
//		UIlist.add(dailyMinsEatingADL.getAttribute("value"));
//		TSIDlist.add("TS055");
//
//		// Verify the Total Minutes assigned to the Eating ADL
//		ArrayList TS056 = d.getData("TS056", sheetName);
//		WebElement totalMinsEatingADL = driver.findElement(By.xpath((String) TS056.get(6)));
//		//// assertEquals((String) TS056.get(7),
//		//// totalMinsEatingADL.getAttribute("value"));
//		String log56 = (String) TS056.get(0) + " " + TS056.get(2);
//		extentTest.log(Status.PASS, log56 + ". Expected Value is: " + (String) TS056.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log56 + sheetName+".jpg")).build());
//		UIlist.add(totalMinsEatingADL.getAttribute("value"));
//		TSIDlist.add("TS056");
//
//		// Verify the Total Hour
//		ArrayList TS057 = d.getData("TS057", sheetName);
//		WebElement totalHrs = driver.findElement(By.xpath((String) TS057.get(6)));
//		//// assertEquals((String) TS057.get(7), totalHrs.getText());
//		String log57 = (String) TS057.get(0) + " " + TS057.get(2);
//		extentTest.log(Status.PASS, log57 + ". Expected Value is: " + (String) TS057.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log57 + sheetName+".jpg")).build());
//		UIlist.add(totalHrs.getText());
//		TSIDlist.add("TS057");
//
//		// Verify the Day / Week
//		ArrayList TS058 = d.getData("TS058", sheetName);
//		WebElement dayAndWeek = driver.findElement(By.xpath((String) TS058.get(6)));
//		//// assertEquals((String) TS058.get(7), dayAndWeek.getText());
//		String log58 = (String) TS058.get(0) + " " + TS058.get(2);
//		extentTest.log(Status.PASS, log58 + ". Expected Value is: " + (String) TS058.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot("TS058" + sheetName+".jpg")).build());
//		UIlist.add(dayAndWeek.getText());
//		TSIDlist.add("TS058");
//
//		// Verify the Signature Date
//		ArrayList TS059 = d.getData("TS059", sheetName);
//		WebElement signDate = driver.findElement(By.xpath((String) TS059.get(6)));
//		Format f = new SimpleDateFormat("M/dd/yyyy");
//		String strDate = f.format(new Date());
//		assertTrue(signDate.getText().contains(strDate));
//		String log59 = (String) TS059.get(0) + " " + TS059.get(2);
//		extentTest.log(Status.PASS, log59 + ". Expected Value is: " + strDate,
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log59 + sheetName+".jpg")).build());
//		UIlist.add(signDate.getText());
//		TSIDlist.add("TS059");
//
//		// Verify the Completeness Bar at the top of the assessment.
//		ArrayList TS060 = d.getData("TS060", sheetName);
//		WebElement completenessBar = driver.findElement(By.xpath((String) TS060.get(6)));
//		//// assertEquals((String) TS060.get(7), completenessBar.getText());
//		String log60 = (String) TS060.get(0) + " " + TS060.get(2);
//		extentTest.log(Status.PASS, log60 + ". Expected Value is: " + (String) TS060.get(7),
//				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log60 + sheetName+".jpg")).build());
//		UIlist.add(completenessBar.getText());
//		TSIDlist.add("TS060");

		// Add a Signature into the Nurse Signature Box
		ArrayList TS061 = d.getData("TS061", sheetName);
		WebElement canvas = driver.findElement(By.xpath((String) TS061.get(6)));
		Actions builder = new Actions(driver);
		Action signature = builder.moveToElement(canvas).clickAndHold().moveByOffset(200, 50).moveByOffset(10, 0)
				.click().build();
		signature.perform();
		Thread.sleep(5000);
		String log61 = (String) TS061.get(0) + " " + TS061.get(2);
		extentTest.log(Status.PASS, "Add a Signature into the Nurse Signature Box",
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log61 + sheetName +fileDate)).build());

		// Click Accept Signature Button
		ArrayList TS062 = d.getData("TS062", sheetName);
		WebElement acceptSign = driver.findElement(By.xpath((String) TS062.get(6)));
		acceptSign.click();
		String log62 = (String) TS062.get(0) + " " + TS062.get(2);
		extentTest.log(Status.PASS, "Click Accept Signature Button",
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log62 + sheetName +fileDate)).build());

		// Click Clear Signature Button
		ArrayList TS063 = d.getData("TS063", sheetName);
		WebElement clearSign = driver.findElement(By.xpath((String) TS063.get(6)));
		clearSign.click();
		Thread.sleep(5000);
		String log63 = (String) TS063.get(0) + " " + TS063.get(2);
		extentTest.log(Status.PASS, "Click Clear Signature Button",
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log63 + sheetName +fileDate)).build());

		// Add a Signature into the Nurse Signature Box
		ArrayList TS064 = d.getData("TS064", sheetName);
		WebElement canvas1 = driver.findElement(By.xpath((String) TS064.get(6)));
		Action signature1 = builder.moveToElement(canvas1).clickAndHold().moveByOffset(200, 50).moveByOffset(10, 0)
				.click().build();
		signature1.perform();
		Thread.sleep(5000);
		String log64 = (String) TS064.get(0) + " " + TS064.get(2);
		extentTest.log(Status.PASS, "Add a Signature into the Nurse Signature Box",
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log64 + sheetName +fileDate)).build());

		// Click Accept Signature Button
		ArrayList TS065 = d.getData("TS065", sheetName);
		WebElement acceptSign1 = driver.findElement(By.xpath((String) TS065.get(6)));
		acceptSign1.click();
		String log65 = (String) TS065.get(0) + " " + TS065.get(2);
		extentTest.log(Status.PASS, log65,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log65 + sheetName +fileDate)).build());

		// Click the Complete Assessment Button
		ArrayList TS066 = d.getData("TS066", sheetName);
		WebElement completeAssessment = driver.findElement(By.xpath((String) TS066.get(6)));
		completeAssessment.click();
		String log66 = (String) TS066.get(0) + " " + TS066.get(2);
		extentTest.log(Status.PASS, "Click the Complete Assessment Button",
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log66 + sheetName +fileDate)).build());

		// Click Welcome
		ArrayList TS067 = d.getData("TS067", sheetName);
		WebElement welcome = driver.findElement(By.xpath((String) TS067.get(6)));
		welcome.click();
		String log67 = (String) TS067.get(0) + " " + TS067.get(2);
		extentTest.log(Status.PASS, "Click Welcome",
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log67 + sheetName +fileDate)).build());

		// Click Sign Out
		ArrayList TS068 = d.getData("TS068", sheetName);
		WebElement signOut = driver.findElement(By.xpath((String) TS068.get(6)));
		signOut.click();
		String log68 = (String) TS068.get(0) + " " + TS068.get(2);
		extentTest.log(Status.PASS, "Click Sign Out Button",
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log68 + sheetName +fileDate)).build());
		String sampleSheetName = sampleSheet;
		excelWR.writeIntoExcel(UIlist, TSIDlist, sampleSheetName);
		return allList;

	}

	public void logout(String sheetName, String sampleSheet) throws IOException {

		// Click Welcome
		ArrayList TS067 = d.getData("TS067", sheetName);
		WebElement welcome = driver.findElement(By.xpath((String) TS067.get(6)));
		welcome.click();
		String log67 = (String) TS067.get(0) + " " + TS067.get(2);
		extentTest.log(Status.PASS, log67,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log67 + sheetName +fileDate)).build());

		// Click Sign Out
		ArrayList TS068 = d.getData("TS068", sheetName);
		WebElement signOut = driver.findElement(By.xpath((String) TS068.get(6)));
		signOut.click();
		String log68 = (String) TS068.get(0) + " " + TS068.get(2);
		extentTest.log(Status.PASS, log68,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log68 + sheetName +fileDate)).build());
		String sampleSheetName = sampleSheet;

	}

}
