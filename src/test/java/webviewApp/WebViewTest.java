package webviewApp;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class WebViewTest {

    static AndroidDriver driver;
    WebDriverWait wait;

    /* Este teste usa a versão 1.2 da apk abaixo:
    https://play.google.com/store/apps/details?id=com.snc.test.webview2 */

    @Before
    public void configuracoesIniciais() throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
        cap.setCapability(MobileCapabilityType.UDID, "192.168.57.103:5555");
        cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.snc.test.webview2");
        cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.snc.test.webview.activity.MainActivity");
        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 30);

    }

    @Test
    public void vagasFPFTechWebViewTest() {

        // Comandos para abrir site
        MobileElement expandir_menu_webview_app = (MobileElement) driver.findElementByAccessibilityId("Open navigation drawer");
        wait.until(ExpectedConditions.visibilityOf(expandir_menu_webview_app)).click();
        MobileElement option_webview = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.support.v7.widget.LinearLayoutCompat[1]/android.widget.CheckedTextView");
        option_webview.click();
        MobileElement input_site = (MobileElement) driver.findElementById("com.snc.test.webview2:id/input_url");
        input_site.clear();
        input_site.sendKeys("fpftech.com");
        MobileElement btn_go_site = (MobileElement) driver.findElementById("android:id/button1");
        btn_go_site.click();

        //Printar lista de Contextos
        System.out.println(driver.getContextHandles());

        // Listando contextos e acessando por posição de contexto 0 - NATIVE_APP e 1 - WebView
        Set<String> contextNames = driver.getContextHandles();
        driver.context((String) contextNames.toArray()[1]);

        //Exibir código fonte da página
        System.out.println(driver.getPageSource());

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button.navbar-toggler.pull-left.no-outline"))).click();
        driver.findElement(By.xpath("//a[contains(@href,'/talentos/central')]")).click();
        Actions action = new Actions(driver);
        WebElement btn_vagas = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@href,'https://vagas.apto.academy/organization/fpf-tech')]")));
        action.moveToElement(btn_vagas).build().perform();
        assertTrue("Botão de vagas não existe", btn_vagas.isDisplayed());

    }

    @After
    public void tearDown() throws Exception {
        //Retornar ao contexto Nativo
        driver.context("NATIVE_APP");
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
