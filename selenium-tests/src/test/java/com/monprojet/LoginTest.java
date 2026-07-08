package com.monprojet;

// Importation des classes Selenium nécessaires pour manipuler le navigateur et les éléments web
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

// Importation des classes Selenium pour gérer les attentes explicites
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// Importation des classes TestNG pour créer et exécuter les tests
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;


// Classe contenant les tests automatisés de connexion
public class LoginTest {

    // Instance du navigateur utilisé pendant les tests
    WebDriver driver;

    // Objet permettant d'attendre qu'un élément soit disponible avant une action
    WebDriverWait wait;


    // URL de la page de connexion de l'application testée
    private static final String LOGIN_URL =
            "https://app-uat.codereview.allence.cloud/auth/login";


    // Temps de pause utilisé pour ralentir l'exécution et voir les actions dans Chrome
    private static final int PAUSE = 1500;


    // Méthode permettant de créer une pause entre les différentes étapes du test
    private void pause() {
        try {
            Thread.sleep(PAUSE);
        } catch (InterruptedException e) {
            // Restaure l'état d'interruption du thread en cas d'erreur
            Thread.currentThread().interrupt();
        }
    }


    // Méthode exécutée avant chaque test
    // Elle initialise le navigateur et ouvre la page de connexion
    @BeforeMethod
    public void setup() {

        // Configuration des options du navigateur Chrome
        ChromeOptions options = new ChromeOptions();


        // Permet d'afficher Chrome avec une fenêtre visible et maximisée
        // Aucun mode headless n'est utilisé
        options.addArguments("--start-maximized");

        // Désactive les notifications du navigateur
        options.addArguments("--disable-notifications");

        // Bloque les fenêtres popup automatiques
        options.addArguments("--disable-popup-blocking");


        // Création d'une nouvelle instance Chrome pour le test
        driver = new ChromeDriver(options);


        // Création d'un système d'attente explicite avec un délai maximum de 30 secondes
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));


        // Agrandissement de la fenêtre Chrome
        driver.manage().window().maximize();


        // Pause avant le chargement de la page
        pause();


        // Ouverture de la page de connexion
        driver.get(LOGIN_URL);


        // Pause pour laisser le temps au chargement de la page
        pause();
    }



    // Méthode permettant d'effectuer une connexion avec un email et un mot de passe donnés
    public void effectuerLogin(String email, String mdp) {


        // Recherche du champ email et attente jusqu'à ce qu'il soit cliquable
        WebElement emailField = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("email"))
        );


        // Nettoyage du champ email
        emailField.clear();

        // Pause avant la saisie
        pause();


        // Saisie de l'adresse email caractère par caractère
        for(char c : email.toCharArray()) {
            emailField.sendKeys(String.valueOf(c));

            try {
                // Petite pause entre chaque caractère pour simuler un utilisateur réel
                Thread.sleep(100);
            } catch(Exception e) {}
        }


        // Pause après la saisie de l'email
        pause();



        // Recherche du champ mot de passe
        // Attente jusqu'à ce qu'il soit disponible
        WebElement passwordField = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("input[type='password']")
                )
        );


        // Nettoyage du champ mot de passe
        passwordField.clear();


        // Pause avant la saisie du mot de passe
        pause();



        // Saisie du mot de passe caractère par caractère
        for(char c : mdp.toCharArray()) {
            passwordField.sendKeys(String.valueOf(c));

            try {
                // Pause entre chaque caractère
                Thread.sleep(100);
            } catch(Exception e) {}
        }


        // Pause avant le clic sur le bouton
        pause();



        // Recherche du bouton de connexion et attente jusqu'à ce qu'il soit cliquable
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[type='submit']")
                )
        );


        // Clic sur le bouton de connexion
        button.click();


        // Pause après la tentative de connexion
        pause();
    }



    // Test 1 :
    // Vérifie qu'un utilisateur avec un email correct mais un mauvais mot de passe
    // reste sur la page de connexion
    @Test(priority = 1)
    public void testEmailValideMdpInvalide(){

        effectuerLogin(
                "raniabenammar491@gmail.com",
                "mauvais_mdp"
        );


        // Vérification que l'utilisateur n'a pas été connecté
        Assert.assertTrue(
                driver.getCurrentUrl().contains("/auth/login"),
                "Le test a échoué : le mauvais mot de passe a accepté la connexion"
        );
    }



    // Test 2 :
    // Vérifie qu'un email incorrect avec un mot de passe valide est refusé
    @Test(priority = 2)
    public void testEmailInvalideMdpValide(){

        effectuerLogin(
                "fausse_rania@gmail.com",
                "1234566"
        );


        // Vérification que la page de login reste affichée
        Assert.assertTrue(
                driver.getCurrentUrl().contains("/auth/login"),
                "Le test a échoué : un faux email a été accepté"
        );
    }



    // Test 3 :
    // Vérifie que la connexion échoue lorsque l'email est vide
    @Test(priority = 3)
    public void testEmailVide(){

        effectuerLogin(
                "",
                "1234566"
        );


        // Vérification du maintien sur la page de connexion
        Assert.assertTrue(
                driver.getCurrentUrl().contains("/auth/login")
        );
    }



    // Test 4 :
    // Vérifie que la connexion échoue lorsque le mot de passe est vide
    @Test(priority = 4)
    public void testMotPasseVide(){

        effectuerLogin(
                "raniabenammar491@gmail.com",
                ""
        );


        // Vérification du maintien sur la page de connexion
        Assert.assertTrue(
                driver.getCurrentUrl().contains("/auth/login")
        );
    }



    // Test 5 :
    // Vérifie qu'une connexion avec des identifiants valides réussit
    @Test(priority = 5)
    public void testConnexionValide(){


        effectuerLogin(
                "raniabenammar491@gmail.com",
                "1234566"
        );


        // Attente jusqu'à ce que l'utilisateur soit redirigé
        // (la page URL ne doit plus contenir /auth/login)
        boolean ok = wait.until(
                ExpectedConditions.not(
                        ExpectedConditions.urlContains("/auth/login")
                )
        );


        // Vérification que la connexion a réussi
        Assert.assertTrue(
                ok,
                "La connexion n'a pas réussi"
        );
    }



    // Méthode exécutée après chaque test
    // Elle ferme le navigateur pour libérer les ressources
    @AfterMethod
    public void finish(){


        // Pause avant fermeture du navigateur
        pause();


        // Vérifie que le navigateur existe avant de le fermer
        if(driver != null){

         
        }
    }
}