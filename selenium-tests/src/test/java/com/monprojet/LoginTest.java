package com.monprojet;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class LoginTest {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://app-uat.codereview.allence.cloud/auth/login");
    }

    public void taperLoginLentement(String email, String mdp) throws InterruptedException {
        Thread.sleep(1500); 
        driver.findElement(By.id("email")).sendKeys(email);
        
        Thread.sleep(1000); 
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(mdp);
        
        Thread.sleep(1000); 
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        
        // On attend un peu plus pour laisser le temps à la redirection de se faire
        Thread.sleep(5000); 
    }

    @Test
    public void testToutValide() throws InterruptedException {
        taperLoginLentement("raniabenammar491@gmail.com", "123456");
        
        String urlApresLogin = driver.getCurrentUrl();
        
        // CORRECTION : On vérifie que l'URL a CHANGÉ et ne contient plus "/auth/login"
        // Si ça échoue encore ici, c'est que tes identifiants ne sont pas acceptés par le site !
        Assert.assertFalse(urlApresLogin.contains("/auth/login"), 
            "Erreur : On est toujours sur la page de login. URL actuelle : " + urlApresLogin);
    }

    @Test
    public void testEmailValideMdpInvalide() throws InterruptedException {
        taperLoginLentement("raniabenammar491@gmail.com", "mauvais_mdp");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"));
    }

    @Test
    public void testEmailInvalideMdpValide() throws InterruptedException {
        taperLoginLentement("inconnu@test.com", "123456");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"));
    }

    @Test
    public void testEmailInvalideMdpInvalide() throws InterruptedException {
        taperLoginLentement("inconnu@test.com", "mauvais_mdp");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"));
    }

    @Test
    public void testEmailVideMdpValide() throws InterruptedException {
        taperLoginLentement("", "123456");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"));
    }

    @Test
    public void testEmailValideMdpVide() throws InterruptedException {
        taperLoginLentement("raniabenammar491@gmail.com", "");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"));
    }

    @AfterMethod
    public void finish() throws InterruptedException {
        Thread.sleep(2000); 
        if (driver != null) {
        }
    }
}