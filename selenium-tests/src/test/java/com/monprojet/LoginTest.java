package com.monprojet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    private static final String LOGIN_URL = "https://app-uat.codereview.allence.cloud/auth/login";

    // Pause visible entre chaque action (en millisecondes)
    private static final int PAUSE = 1500;

    private void pause() {
        try {
            Thread.sleep(PAUSE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

@BeforeMethod
public void setup() throws MalformedURLException {

    ChromeOptions options = new ChromeOptions();

    String seleniumUrl = System.getProperty("SELENIUM_URL");

    if (seleniumUrl == null || seleniumUrl.trim().isEmpty()) {
        seleniumUrl = System.getenv("SELENIUM_URL");
    }

    if (seleniumUrl != null) {
        seleniumUrl = seleniumUrl.trim();
    }

    System.out.println("SELENIUM_URL utilisé = [" + seleniumUrl + "]");


    if (seleniumUrl == null || seleniumUrl.isEmpty()) {
        throw new RuntimeException(
                "SELENIUM_URL absent. Vérifiez la configuration GitLab CI."
        );
    }


    options.addArguments("--headless=new");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--disable-gpu");
    options.addArguments("--window-size=1920,1080");


    driver = new RemoteWebDriver(
            new URL(seleniumUrl),
            options
    );


    wait = new WebDriverWait(
            driver,
            Duration.ofSeconds(30)
    );


    driver.manage().window().setSize(
            new org.openqa.selenium.Dimension(1920, 1080)
    );


    driver.get(LOGIN_URL);
}

    /**
     * Saisit les identifiants lentement et clique sur le bouton de connexion.
     */
    public void effectuerLogin(String email, String mdp) {
        // Attendre et remplir le champ email
        WebElement emailField = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("email"))
        );
        pause();
        emailField.clear();
        pause();

        // Saisie lettre par lettre pour simuler un utilisateur réel
        for (char c : email.toCharArray()) {
            emailField.sendKeys(String.valueOf(c));
            try { Thread.sleep(80); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        pause();

        // Attendre et remplir le champ mot de passe
        WebElement passwordField = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='password']"))
        );
        pause();
        passwordField.clear();
        pause();

        // Saisie lettre par lettre
        for (char c : mdp.toCharArray()) {
            passwordField.sendKeys(String.valueOf(c));
            try { Thread.sleep(80); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        pause();

        // Pause avant de cliquer sur le bouton
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        pause();
        submitBtn.click();

        // Pause après clic pour laisser le temps à la réponse serveur
        pause();
    }

    // 1. Email valide / mot de passe invalide
    @Test(priority = 1)
    public void testEmailValideMdpInvalide() {
        effectuerLogin("raniabenammar491@gmail.com", "mauvais_mdp");
        pause();
        Assert.assertTrue(
            driver.getCurrentUrl().contains("/auth/login"),
            "ERREUR : On aurait dû rester sur la page login (MDP faux)."
        );
    }

    // 2. Email invalide / mot de passe valide
    @Test(priority = 2)
    public void testEmailInvalideMdpValide() {
        effectuerLogin("fausse_rania@gmail.com", "1234566");
        pause();
        Assert.assertTrue(
            driver.getCurrentUrl().contains("/auth/login"),
            "ERREUR : On aurait dû rester sur la page login (Email faux)."
        );
    }

    // 3. Email vide / mot de passe valide
    @Test(priority = 3)
    public void testEmailVideMdpValide() {
        effectuerLogin("", "1234566");
        pause();
        Assert.assertTrue(
            driver.getCurrentUrl().contains("/auth/login"),
            "ERREUR : On aurait dû rester sur la page login (Email vide)."
        );
    }

    // 4. Email valide / mot de passe vide
    @Test(priority = 4)
    public void testEmailValideMdpVide() {
        effectuerLogin("raniabenammar491@gmail.com", "");
        pause();
        Assert.assertTrue(
            driver.getCurrentUrl().contains("/auth/login"),
            "ERREUR : On aurait dû rester sur la page login (MDP vide)."
        );
    }

    // 5. Identifiants valides — test positif
    @Test(priority = 5)
    public void testToutValide() {
        effectuerLogin("raniabenammar491@gmail.com", "1234566");
        pause();

        boolean redirectionReussie = wait.until(
            ExpectedConditions.not(ExpectedConditions.urlContains("/auth/login"))
        );
        pause();
        Assert.assertTrue(
            redirectionReussie,
            "ERREUR : La connexion a échoué avec des identifiants valides."
        );
    }

@AfterMethod
public void finish() {
    // Pause avant de fermer pour voir le résultat final
    pause();
    if (driver != null) {
        driver.quit();
    }
}
}