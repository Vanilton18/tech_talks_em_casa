package gmail;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EnvioDeEmailTest {

    static AndroidDriver driver;
    WebDriverWait wait;

    @Before
    public void configuracoesIniciais() throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(AndroidMobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.google.android.gm");
        cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "ConversationListActivityGmail");
        cap.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        cap.setCapability(MobileCapabilityType.NO_RESET, true);
        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void EnvioDeEmailOnlineTest() {
        if(!driver.getConnection().isWiFiEnabled()){
            driver.setConnection(new ConnectionStateBuilder().withWiFiEnabled().build());
        }

        // Alterar para orientação horizontal
        driver.rotate(ScreenOrientation.LANDSCAPE);
        wait.until(ExpectedConditions.visibilityOf(
                driver.findElementById("com.google.android.gm:id/compose_button"))).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElementById("com.google.android.gm:id/to")))
                .sendKeys("vanilton.sfp@gmail.com");
        driver.findElementById("com.google.android.gm:id/subject").sendKeys("Teste Envio de Email");
        driver.findElementByXPath("//android.webkit.WebView/android.webkit.WebView/android.widget.EditText")
                .sendKeys("Enviando Email Automatizado pelo Appium no #TechTalksEmCasa");
        driver.findElementByAccessibilityId("Send").click();
        driver.rotate(ScreenOrientation.PORTRAIT);
        assertEquals("Sent",
            wait.until(ExpectedConditions.visibilityOf(
                driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Sent\")"))).getText());
    }

    @Test
    public void EnvioDeEmailOfflineTest() {
        if(!driver.getConnection().isAirplaneModeEnabled()){
            // Ativar o Modo Avião
            driver.toggleAirplaneMode();
        }
        wait.until(ExpectedConditions.visibilityOf(
                driver.findElementById("com.google.android.gm:id/compose_button"))).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElementById("com.google.android.gm:id/to")))
                .sendKeys("vanilton.sfp@gmail.com");
        driver.findElementById("com.google.android.gm:id/subject").sendKeys("Teste Envio de Email");
        driver.findElementByXPath("//android.webkit.WebView/android.webkit.WebView/android.widget.EditText")
                .sendKeys("Enviando Email Automatizado pelo Appium no #TechTalksEmCasa");
        driver.findElementByAccessibilityId("Send").click();
        assertEquals("Offline. Message will be sent later",
                wait.until(ExpectedConditions.visibilityOf(
                        driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Offline. Message will be sent later\")"))).getText());
    }


    @After
    public void tearDown() throws Exception {
        driver.openNotifications();
        TiraPrint();
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
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
