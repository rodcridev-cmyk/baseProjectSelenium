package org.driver;

import org.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
public class DriverFactory {
    // Cada hilo tendrá su propia instancia de WebDriver para evitar problemas de concurrencia
    // fundamental para pruebas paralelas
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {

        // Lee el navegador desde la configuración
        String browser = ConfigReader.get("browser");

       if (browser == null) {
            throw new RuntimeException("Browser no está definido");
        }

        switch (browser.toLowerCase()) {

            case "chrome":
                // Configura el driver de Chrome automáticamente usando WebDriverManager
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver());
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver.set(new FirefoxDriver());
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driver.set(new EdgeDriver());
                break;

            default:
                throw new RuntimeException(
                    "Navegador no soportado: " + browser +
                    ". Usa chrome, firefox o edge"
                );
        }

        driver.get().manage().window().maximize();
    }

    // Devuelve la instancia de WebDriver para el hilo actual
    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
