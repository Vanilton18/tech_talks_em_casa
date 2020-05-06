package mobileWebApp;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class MobileWebAppTest {

    static AndroidDriver driver;
    WebDriverWait wait;

    @Before
    public void configuracoesIniciais() throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        cap.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);

    }

    @Test
    public void MobileAppTestExample() {
        driver.get("http://demo.guru99.com/test/drag_drop.html");
        Actions action = new Actions(driver);

        WebElement FromBank = driver.findElement(By.xpath("//li[contains(.,'BANK')]/a"));
        WebElement ToDebit = driver.findElement(By.xpath("//h3[contains(.,'DEBIT SIDE')]/..//h3[contains(.,'Account')]/..//li"));
        WebElement FromBankNumber = driver.findElement(By.xpath("//li[@id='fourth'][1]"));
        WebElement ToBankDebit = driver.findElement(By.xpath("//h3[contains(.,'DEBIT SIDE')]/..//h3[contains(.,'Amount')]/..//li"));
        action.dragAndDrop(FromBank, ToDebit).build().perform();
        action.dragAndDrop(FromBankNumber, ToBankDebit).build().perform();

        WebElement FromCredit = driver.findElement(By.xpath("//li[contains(.,'SALES')]/a"));
        WebElement ToCredit = driver.findElement(By.xpath("//h3[contains(.,'CREDIT SIDE')]/..//h3[contains(.,'Account')]/..//li"));
        WebElement FromCreditNumber = driver.findElement(By.xpath("//li[@id='fourth'][2]"));
        WebElement ToCreditSide = driver.findElement(By.xpath("//h3[contains(.,'CREDIT SIDE')]/..//h3[contains(.,'Amount')]/..//li"));

        action.dragAndDrop(FromCredit, ToCredit).build().perform();
        action.dragAndDrop(FromCreditNumber, ToCreditSide).build().perform();
        assertTrue(wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@id='equal' and @style]")))).isDisplayed());
    }

    @After
    public void tearDown() throws Exception {
        TiraPrint();
        driver.closeApp();
    }

    public static void TiraPrint() throws IOException {
        java.util.Date data = new java.util.Date();
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd_MM_yyyy_HHmmss");
        File screenshot = driver
                .getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("log/Screenshot"+dataFormatada.format(data)+".png"));
    }
}
