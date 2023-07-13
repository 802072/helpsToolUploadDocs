package extentReport;

import java.io.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
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
import org.openqa.selenium.remote.RemoteWebDriver;
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

import dataDriven.DataDrivenHT;
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

		case "mobile":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			DevTools devtool = ((HasDevTools) driver).getDevTools();
			devtool.createSession();

			Map<String, Object> dm = new HashedMap<String, Object>();
			dm.put("width", 414);
			dm.put("height", 896);
			dm.put("deviceScaleFactor", 50);
			dm.put("mobile", true);
			((HasCdp) driver).executeCdpCommand("Emulation.setDeviceMetricsOverride", dm);
			Thread.sleep(5000);

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
		// open login page
		ArrayList TS01 = d.getData("TS01", "TC01");
		driver.get((String) TS01.get(6));
		driver.get("https://helps.vnshealth-test.mso.vnsny.org/#!/login");
		String log1 = (String) TS01.get(0) + " " + TS01.get(1);
		extentTest.log(Status.PASS, log1,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log1 + ".jpg")).build());

		// Click Login Button
		ArrayList TS02 = d.getData("TS02", "TC01");
		WebElement loginButton = driver.findElement(By.xpath((String) TS02.get(5)));
		loginButton.click();
		String log2 = (String) TS02.get(0) + " " + TS02.get(1);
		extentTest.log(Status.PASS, log2,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log2 + ".jpg")).build());
	}

	public void login() throws InterruptedException, IOException {

		// enter username
		ArrayList TS03 = d.getData("TS03", "TC01");
		WebElement userName = driver.findElement(By.xpath((String) TS03.get(5)));
		userName.sendKeys((String) TS03.get(6));
		String log3 = (String) TS03.get(0) + " " + TS03.get(1);
		extentTest.log(Status.PASS, log3,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log3 + ".jpg")).build());

		// click Next
		ArrayList TS04 = d.getData("TS04", "TC01");
		WebElement next = driver.findElement(By.xpath((String) TS04.get(5)));
		next.click();
		String log4 = (String) TS04.get(0) + " " + TS04.get(1);
		extentTest.log(Status.PASS, log4,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log4 + ".jpg")).build());

		// enter password
		ArrayList TS05 = d.getData("TS05", "TC01");
		WebElement pwd = driver.findElement(By.xpath((String) TS05.get(5)));
		pwd.sendKeys((String) TS05.get(6));
		String log5 = (String) TS05.get(0) + " " + TS05.get(1);
		extentTest.log(Status.PASS, log5,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log5 + ".jpg")).build());

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// click Signin
		ArrayList TS06 = d.getData("TS06", "TC01");
		WebElement signIn = driver.findElement(By.xpath((String) TS06.get(5)));
		signIn.click();
		Thread.sleep(5000);
		String log6 = (String) TS06.get(0) + " " + TS06.get(1);
		extentTest.log(Status.PASS, log6,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log6 + ".jpg")).build());

		// Click Yes to "Stay Signed In?"
		ArrayList TS07 = d.getData("TS07", "TC01");
		WebElement no = driver.findElement(By.xpath((String) TS07.get(6)));
		no.click();
		Thread.sleep(5000);
		String log7 = (String) TS07.get(0) + " " + TS07.get(1);
		extentTest.log(Status.PASS, log7,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log7 + ".jpg")).build());

	}

	
	@BeforeSuite
	public void initialiseExtentReports() {
		ExtentSparkReporter sparkReporter_all = new ExtentSparkReporter("HelpsToolUploadDocuments.html");
		sparkReporter_all.config().setReportName("Helps Tool Upload Document Automation Test Report");

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

//	@AfterTest
//	public void teardown() {
//		driver.close();
//	}

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

	public static String check_file_exist(String filename) {
		String home = System.getProperty("user.home");
		String file_name = filename;
		String file_with_location = home + "\\Downloads\\" + file_name;

		File file = new File(file_with_location);
		if (file.exists()) {

			extentTest.log(Status.PASS, filename + " has been downloaded");
			String result = "File Present";
			return result;
		} else {
			extentTest.log(Status.PASS, filename + " has NOT been downloaded");
			String result = "File not Present";
			String result1 = result;
			return result1;
		}

	}

}