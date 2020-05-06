import unittest
from appium import webdriver
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as ec
import os
from datetime import datetime

class MobileWebTest(unittest.TestCase):

    def setUp(self):
        desired_caps = {}
        desired_caps['platformName'] = 'Android'
        desired_caps['deviceName'] = 'Android Emulator'
        desired_caps['autoGrantPermissions'] = True
        desired_caps['app'] = '/Users/vaniltonpinheiro/projetos/tech_talks_em_casa/apk/consumo_gasolina.apk'
        desired_caps['appPackage'] = 'br.com.consumogasolina'
        desired_caps['appActivity'] = 'MainActivity'
        desired_caps['noReset'] = False
        self.driver = webdriver.Remote('http://localhost:4723/wd/hub', desired_caps)

    def test_cadastro_abastecimento(self):
        self.assertEqual("Abastecimentos", self.driver.find_element_by_id("android:id/action_bar_title").text)
        self.driver.find_element_by_id("br.com.consumogasolina:id/action_opcoes").click()
        WebDriverWait(self.driver, 30).until(ec.visibility_of_element_located((By.XPATH, "//android.widget.TextView[@text='Adicionar Abastecimento']")))
        self.driver.find_element_by_xpath("//android.widget.TextView[@text='Adicionar Abastecimento']").click()
        self.driver.find_element_by_id("br.com.consumogasolina:id/editText_km_abastecimento").send_keys("22950")
        self.driver.find_element_by_id("br.com.consumogasolina:id/editText_quantidade_litros").send_keys("34.14")
        self.driver.find_element_by_id("br.com.consumogasolina:id/editText_valor").send_keys("106.86")
        self.driver.find_element_by_android_uiautomator("new UiSelector().text(\"Cadastrar\")").click()
        self.assertEqual("0.0 KM/Litro(s)/Mês",
                         self.driver.find_element_by_id("br.com.consumogasolina:id/editText_km_litro").text)
        self.assertEqual("R$ 106.86",
                     self.driver.find_element_by_id("br.com.consumogasolina:id/editText_valor_abastecimento").text)
        self.assertEqual("1 Vez(es)/Mês", self.driver.find_element_by_id("br.com.consumogasolina:id/editText_quantidade_abastecimento").text)
        self.assertEqual("34.14 Litro(s)/Mês",
                     self.driver.find_element_by_id("br.com.consumogasolina:id/editText_quantidade_litros").text)
        self.assertEqual("0 KM/Mês",
                     self.driver.find_element_by_id("br.com.consumogasolina:id/editText_quantidade_km").text)

    def tearDown(self):
        now = datetime.now()
        dt_string = now.strftime("%d_%m_%Y_%H_%M_%S")
        directory = os.getcwd()
        file_name = 'screenshot_'+dt_string+'.png'
        self.driver.save_screenshot(directory + file_name)
        self.driver.close_app()


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(MobileWebTest)
    unittest.TextTestRunner(verbosity=2).run(suite)