package com.monprojet;

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

    private static final String LOGIN_URL =
            "https://app-uat.codereview.allence.cloud/auth/login";

    private static final int PAUSE = 1500;

    private void pause() {
        try {
            Thread.sleep(PAUSE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    @BeforeMethod
    public void setup() {

        ChromeOptions options = new ChromeOptions();

        // IMPORTANT : ne pas mettre --headless
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        driver.manage().window().maximize();

        pause();

        driver.get(LOGIN_URL);

        pause();
    }


    public void effectuerLogin(String email, String mdp) {

        WebElement emailField = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("email"))
        );

        emailField.clear();
        pause();

        for(char c : email.toCharArray()) {
            emailField.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(100);
            } catch(Exception e) {}
        }

        pause();


        WebElement passwordField = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("input[type='password']")
                )
        );


        passwordField.clear();
        pause();


        for(char c : mdp.toCharArray()) {
            passwordField.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(100);
            } catch(Exception e) {}
        }


        pause();


        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[type='submit']")
                )
        );


        button.click();

        pause();
    }



    @Test(priority = 1)
    public void testEmailValideMdpInvalide(){

        effectuerLogin(
                "raniabenammar491@gmail.com",
                "mauvais_mdp"
        );

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/auth/login"),
                "Le test a échoué : le mauvais mot de passe a accepté la connexion"
        );
    }



    @Test(priority = 2)
    public void testEmailInvalideMdpValide(){

        effectuerLogin(
                "fausse_rania@gmail.com",
                "1234566"
        );

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/auth/login"),
                "Le test a échoué : un faux email a été accepté"
        );
    }



    @Test(priority = 3)
    public void testEmailVide(){

        effectuerLogin(
                "",
                "1234566"
        );

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/auth/login")
        );
    }



    @Test(priority = 4)
    public void testMotPasseVide(){

        effectuerLogin(
                "raniabenammar491@gmail.com",
                ""
        );

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/auth/login")
        );
    }



    @Test(priority = 5)
    public void testConnexionValide(){

        effectuerLogin(
                "raniabenammar491@gmail.com",
                "1234566"
        );


        boolean ok = wait.until(
                ExpectedConditions.not(
                        ExpectedConditions.urlContains("/auth/login")
                )
        );


        Assert.assertTrue(
                ok,
                "La connexion n'a pas réussi"
        );
    }



    @AfterMethod
    public void finish(){

        pause();

        if(driver != null){
            driver.quit();
        }
    }
}