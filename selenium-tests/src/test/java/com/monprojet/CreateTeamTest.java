package com.monprojet;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.Random;

public class CreateTeamTest {

    WebDriver driver;
    WebDriverWait wait;
    
    private By teamNameField = By.cssSelector("input[formcontrolname='nom']");
    private By saveButton = By.cssSelector("button[type='submit']");
    private By spinner = By.className("spinner-overlay");

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // --- Petites pauses rapides pour le rythme ---
    private void quickPause(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }

    private void fastSendKeys(WebElement element, String text) {
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            quickPause(30); // Saisie très rapide mais visible
        }
    }

    public void login() {
        driver.get("https://app-uat.codereview.allence.cloud/auth/login");
        
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        fastSendKeys(email, "raniabenammar491@gmail.com");
        
        WebElement pass = driver.findElement(By.cssSelector("input[type='password']"));
        fastSendKeys(pass, "123456");
        
        quickPause(300);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/auth/login")));
    }

    private String addTeam() {
        quickPause(800);
        WebElement btnCreer = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'créer')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnCreer);
        
        WebElement inputNom = wait.until(ExpectedConditions.visibilityOfElementLocated(teamNameField));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
        
        String randomName = "sel_" + this.genRadomString();
        inputNom.clear();
        fastSendKeys(inputNom, randomName);

        quickPause(400);
        driver.findElement(saveButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("cdk-overlay-container")));
        return randomName;
    }

    private void updateTeam(String teamNameToEdit) {
        quickPause(800);
        String xpathEditBtn = String.format("//tr[td[contains(text(), '%s')]]//button[contains(@class, 'edit') or contains(., 'edit')]", teamNameToEdit);

        try {
            WebElement editIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathEditBtn)));
            editIcon.click();

            WebElement inputNom = wait.until(ExpectedConditions.visibilityOfElementLocated(teamNameField));
            quickPause(300);
            inputNom.clear();
            fastSendKeys(inputNom, teamNameToEdit + "_MOD");

            quickPause(400);
            driver.findElement(saveButton).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("cdk-overlay-container")));
        } catch (Exception e) {
            Assert.fail("Erreur modif rapide : " + e.getMessage());
        }
    }

    private void deleteTeam(String teamNameToDelete) {
        quickPause(800);
        String xpathDel = String.format("//tr[td[contains(text(), '%s')]]//button[contains(@class, 'delete') or contains(., 'delete')]", teamNameToDelete);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathDel))).click();
            
            quickPause(500);
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'confirm') or contains(., 'Oui') or contains(., 'Supprimer')]")
            )).click();
            
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[contains(text(), '" + teamNameToDelete + "')]")));
        } catch (Exception e) {
            Assert.fail("Erreur suppr rapide : " + e.getMessage());
        }
    }

    @Test
    public void testFullCycle() {
        login();
        driver.get("https://app-uat.codereview.allence.cloud/client/teams");
        
        String name = addTeam();
        updateTeam(name);
        deleteTeam(name + "_MOD");
        
        System.out.println("Test terminé avec succès (Mode Rapide) ✔");
    }

    @AfterClass
    public void tearDown() {
        quickPause(1500);
        if (driver != null) ;
    }

    private String genRadomString() {
        return Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 4);
    }
}