package com.monprojet;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        // Headless désactivé pour voir l'exécution
        // options.addArguments("--headless=new"); 
        
        options.addArguments("--no-sandbox");   
        options.addArguments("--disable-dev-shm-usage"); 
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        driver.manage().window().maximize();
        driver.get("https://app-uat.codereview.allence.cloud/auth/login");
    }

    /**
     * Méthode commune pour saisir les identifiants et cliquer sur login
     */
    public void effectuerLogin(String email, String mdp) {
        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        emailField.clear();
        emailField.sendKeys(email);
        
        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='password']")));
        passwordField.clear();
        passwordField.sendKeys(mdp);
        
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        submitBtn.click();
    }

    // 1. EMAIL VALIDE / MDP INVALIDE
    @Test(priority = 1)
    public void testEmailValideMdpInvalide() {
        effectuerLogin("raniabenammar491@gmail.com", "mauvais_mdp");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"), "ERREUR : On aurait dû rester sur la page login (MDP faux).");
    }

    // 2. EMAIL INVALIDE / MDP VALIDE
    @Test(priority = 2)
    public void testEmailInvalideMdpValide() {
        effectuerLogin("fausse_rania@gmail.com", "123456");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"), "ERREUR : On aurait dû rester sur la page login (Email faux).");
    }

    // 3. EMAIL VIDE / MDP VALIDE
    @Test(priority = 3)
    public void testEmailVideMdpValide() {
        effectuerLogin("", "123456");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"), "ERREUR : On aurait dû rester sur la page login (Email vide).");
    }

    // 4. EMAIL VALIDE / MDP VIDE
    @Test(priority = 4)
    public void testEmailValideMdpVide() {
        effectuerLogin("raniabenammar491@gmail.com", "");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"), "ERREUR : On aurait dû rester sur la page login (MDP vide).");
    }

    // 5. TOUT VALIDE (Le test positif)
    @Test(priority = 5)
    public void testToutValide() {
        effectuerLogin("raniabenammar491@gmail.com", "123456");
        
        // On attend que l'URL ne contienne plus "/auth/login" (redirection vers dashboard)
        boolean redirectionReussie = wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/auth/login")));
        
        Assert.assertTrue(redirectionReussie, "ERREUR : La connexion a échoué avec des identifiants valides.");
    }

    @AfterMethod
    public void finish() {
        if (driver != null) {
            // driver.quit(); // Garde la fenêtre ouverte pour voir le résultat final
        }
    }
}