package consumoGasolina;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class ConsumoGasolinaTest {

    static AndroidDriver driver;
    WebDriverWait wait;

    @Before
    public void configuracoesIniciais() throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(AndroidMobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        cap.setCapability(MobileCapabilityType.APP, "/Users/vaniltonpinheiro/projetos/tech_talks_em_casa/apk/consumo_gasolina.apk");

        // Setando o pacote da aplicação para as capacidades
        cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "br.com.consumogasolina");

        // Setando a Activity da aplicação do telefone
        cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "MainActivity");

        // Dando permissão de acesso as funções do celular como: leitura e escrita no storage
        cap.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);

        // Não limpa os dados locais do app
        cap.setCapability(MobileCapabilityType.NO_RESET, false);

        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
        wait = new WebDriverWait(driver, 5);
    }

    @Test
    public void CadastroDeAbastecimento() throws InterruptedException {
        assertEquals("Abastecimentos", driver.findElement(By.id("android:id/action_bar_title")).getText());
        driver.findElement(By.id("br.com.consumogasolina:id/action_opcoes")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//android.widget.TextView[@text='Adicionar Abastecimento']"))).click();
        driver.findElement(By.id("br.com.consumogasolina:id/editText_km_abastecimento")).sendKeys("22950");
        driver.findElement(By.id("br.com.consumogasolina:id/editText_quantidade_litros")).sendKeys("34.14");
        driver.findElement(By.id("br.com.consumogasolina:id/editText_valor")).sendKeys("106.86");
        driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Cadastrar\")").click();

        assertEquals("0.0 KM/Litro(s)/Mês", driver.findElement(By.id("br.com.consumogasolina:id/editText_km_litro")).getText());
        assertEquals("R$ 106.86", driver.findElement(By.id("br.com.consumogasolina:id/editText_valor_abastecimento")).getText());
        assertEquals("1 Vez(es)/Mês", driver.findElement(By.id("br.com.consumogasolina:id/editText_quantidade_abastecimento")).getText());
        assertEquals("34.14 Litro(s)/Mês", driver.findElement(By.id("br.com.consumogasolina:id/editText_quantidade_litros")).getText());
        assertEquals("0 KM/Mês", driver.findElement(By.id("br.com.consumogasolina:id/editText_quantidade_km")).getText());

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
