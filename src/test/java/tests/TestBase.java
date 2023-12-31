package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.BrowserUtils;
import utils.ConfigurationReader;
import utils.Driver;

import java.io.IOException;

public abstract class TestBase {
    protected static ExtentReports extentReports;
    //    The ExtentHtmlReporter creates a rich standalone HTML file. It allows several
    protected static ExtentSparkReporter extentHtmlReporter;
    //    Defines a test. You can add logs, snapshots, assign author and categories to a test and its children.
    protected static ExtentTest extentTest;

    @BeforeTest
    public void beforeTest() {
        //location of report
        //it's gonna be next to target folder, test-output folder
        String reportName = "report";
        String filePath = System.getProperty("user.dir") + "/test-output/" + reportName + ".html";
        extentReports = new ExtentReports();
        extentHtmlReporter = new ExtentSparkReporter(filePath);
        extentReports.attachReporter(extentHtmlReporter);
        extentHtmlReporter.config().setReportName("Test Results");
        //system information
        String env = ConfigurationReader.getProperty("baseUrl");
        extentReports.setSystemInfo("Environment", env);
        extentReports.setSystemInfo("Browser", ConfigurationReader.getProperty("browser"));
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
    }

    @AfterTest
    public void afterTest() {
        extentReports.flush();
    }

    @BeforeMethod
    public void setup() {
        Driver.get().manage().window().maximize();

    }

    @AfterMethod
    public void teardown(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.fail(result.getName());
            extentTest.fail(result.getThrowable());
            extentTest.addScreenCaptureFromPath(BrowserUtils.getScreenshot(result.getName()));
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("Test case was skipped : " + result.getName());
        }
        Driver.close();
    }
}
