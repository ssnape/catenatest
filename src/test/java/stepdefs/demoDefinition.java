/**
 *  Test definitions for the features outline in sample.feature.
 *
 * @author Arielle Bonnici
 * @version 1.0
 * @since 2020-09-04
 */

package stepdefs;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import javax.mail.Message;

import org.junit.Assert;

import datareader.ConfigReader;
import datareader.EmailReader;

public class demoDefinition {

    ConfigReader config;
    WebDriver browser;
    WebDriverWait wait;

    @Before
    public void setup() {
        //Load environment properties
        config = new ConfigReader();

        System.setProperty("webdriver.chrome.driver", config.getDriverPath());

        //Set ChromeDriver options to bypass captcha
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features");
        options.addArguments("--disable-blink-features=AutomationControlled");

        //Initialize ChromeDriver and WebDriverWait
        browser = new ChromeDriver(options);
        wait = new WebDriverWait(browser, 10);
    }

    @Before("@ForgotPassword")
    public void clearMailbox() {
        //Clear mailbox before running Forgot Password test scenario
        EmailReader mailbox = new EmailReader(config.getGmailUser(), config.getGmailPasword());
        Assert.assertTrue(mailbox.clearMailbox());
    }

    @After
    public void tearDown() {
        browser.quit();
    }

    @Given("I go to the login page")
    public void go_to_the_login_page() {
        //Go to login page
        browser.get(config.getLoginUrl());
    }

    @Given("I go to the forgot password page")
    public void go_to_forgot_password_page() {
        //Go to forgot password page
        browser.get(config.getForgotPasswordUrl());
    }

    @And("Enter correct login credentials")
    public void ennter_correct_login_credentials() {
        //Fill username field with valid username from properties file
        WebElement txtUser = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("_username")));
        txtUser.sendKeys(config.getUsername());

        //Fill password field with valid password from properties file
        WebElement txtPassword = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("_password")));
        txtPassword.sendKeys(config.getPassword());
    }

    @And("Enter incorrect login credentials")
    public void ennter_incorrect_login_credentials() {
        //Fill username field with invalid username from properties file
        WebElement txtUser = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("_username")));
        txtUser.sendKeys(config.getInvalidUsername());

        //Fill password field with invalid password from properties file
        WebElement txtPassword = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("_password")));
        txtPassword.sendKeys(config.getInvalidPassword());
    }

    @And("Enter my username in forgot password page")
    public void enter_username_in_forgot_password() {
        //Fill username field from properties file
        WebElement txtUser = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("forgotPassword_usernameOrEmail"))
        );
        txtUser.sendKeys(config.getUsername());
    }

    @When("I click the login button")
    public void click_login_button() {
        //Click login button on login page
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("login")));
        loginButton.click();
    }

    @When("I click reset password button")
    public void click_reset_password_button() {
        //Click reset password button
        WebElement loginButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("forgotPassword_resetPassword"))
        );
        loginButton.click();

        //Explicit wait to allow time for email to get to mailbox
        try {
            Thread.sleep(5 * 1000);
        }
        catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Then ("My profile should be visible in the topnav")
    public void profile_should_be_visible_in_topnav() {
        //Open profile div from topnac
        WebElement divProfile = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("profile")));
        divProfile.click();

        //Find username span and verify username is present
        WebElement userSpan = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div.header p span:nth-of-type(2)"))
        );
        Assert.assertEquals(
                "Incorrect username found",
                config.getUsername().toLowerCase(),
                userSpan.getText().toLowerCase()
        );
    }

    @Then("I will get an invalid credentials error")
    public void invalid_credentials_error() {
        //Find error span and verify expected text
        WebElement lblError = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div.login__box span.error__text"))
        );
        Assert.assertEquals(
                "Incorrect or missing error message",
                config.getInvalidCredentialsError(),
                lblError.getText()
        );
    }

    @Then("I will receive an email to reset password")
    public void receive_email_to_reset_password() {
        //Get rest password email(s) from mailbox by subject
        EmailReader mailbox = new EmailReader(config.getGmailUser(), config.getGmailPasword());
        Message[] matchingEmails = mailbox.getEmailsBySubject(config.getResetPasswordSubject());

        if (matchingEmails != null) {
            Assert.assertTrue(matchingEmails.length >= 1);
        }
        else
            Assert.fail("Forgot password email not found");

    }

    @Then("The top ten casinos are displayed on home page")
    public void top_ten_casinos_on_home_page() {
        //Get the Top 10 Casinos div and verify 10 items are listed
        WebElement listTopTen = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div.top10--hp"))
        );
        Assert.assertEquals(10, listTopTen.findElements(By.cssSelector("li.top10__item")).size());
    }
}
