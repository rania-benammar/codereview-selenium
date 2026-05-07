//package com.monprojet;

//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.*;
//import org.testng.annotations.*;
//import io.github.bonigarcia.wdm.WebDriverManager;
//import java.time.Duration;

//public class LicenseTest {

   // WebDriver driver;
   // WebDriverWait wait;
    
  //  private final String BASE_URL = "https://app-uat.codereview.allence.cloud";
   // private final String LICENCE_ID = "69e0c17d670c8e28460c51bd";

   // @BeforeClass
   // public void setUp() {
      //  WebDriverManager.chromedriver().setup();
     //   driver = new ChromeDriver();
      //  driver.manage().window().maximize();
     //   wait = new WebDriverWait(driver, Duration.ofSeconds(20));
  //  }

   // private void quickPause(int ms) {
    //    try { Thread.sleep(ms); } catch (InterruptedException e) { }
    //}

    /**
     * Login corrigé : utilise ENTER pour valider et vérifie le changement d'URL
     */
  //  public void login() {
       // driver.get(BASE_URL + "/auth/login");
        
        // Attente du champ email
     //   WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
     //   emailField.clear();
      //  emailField.sendKeys("raniabenammar491@gmail.com");
        
        // Saisie du mot de passe
      //  WebElement passField = driver.findElement(By.cssSelector("input[type='password']"));
      //  passField.clear();
      //  passField.sendKeys("123456");
        
     //   quickPause(500);
        
        // Validation via la touche ENTER (souvent plus fiable que le clic sur bouton en UAT)
      //  passField.sendKeys(Keys.ENTER);
        
        // On attend soit la redirection, soit un message d'erreur de login
      //  try {
       //     wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/auth/login")));
       // } catch (TimeoutException e) {
            // Si on est encore sur login, c'est peut-être que les identifiants sont refusés
         //   System.err.println("Échec de la redirection. Vérifiez vos identifiants ou la lenteur du serveur.");
          //  throw e;
      //  }
  //  }

    // @Test
   // public void testAjouterLicenceFlow() {
       // login();

        // Navigation vers la page des licences
      //  driver.get(BASE_URL + "/client/licences");

        // Cliquer sur "Ajouter la licence"
       // WebElement btnAjouter = wait.until(ExpectedConditions.elementToBeClickable(
          //  By.xpath("//button[contains(., 'Ajouter la licence')]")
      //  ));
       // btnAjouter.click();

        // Remplir la popup
        //WebElement inputIdPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(
        //    By.cssSelector("mat-dialog-container input, input[formcontrolname='id']")
       // ));
        
      //  quickPause(500);
       // inputIdPopup.sendKeys(LICENCE_ID);

        // Valider l'ajout
       // WebElement btnValider = driver.findElement(By.cssSelector("button[type='submit']"));
       // btnValider.click();

        // Attendre la fin de l'action
       // wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("cdk-overlay-container")));
       // System.out.println("Licence traitée avec succès.");
   // }

    // @AfterClass
   // public void tearDown() {
      //  if (driver != null) {
       //     quickPause(2000);
       //     driver.quit();
       // }
   // }
//}