Feature: Test AskGamblers.com

  Scenario: Successful Login
    Given I go to the login page
    And Enter correct login credentials
    When I click the login button
    Then My profile should be visible in the topnav

  Scenario: Failed Login
    Given I go to the login page
    And Enter incorrect login credentials
    When I click the login button
    Then I will get an invalid credentials error

  @ForgotPassword
  Scenario: Forgot Password
    Given I go to the forgot password page
    And Enter my username in forgot password page
    When I click reset password button
    Then I will receive an email to reset password

  Scenario: Top Ten Casinos
    Given I go to the login page
    And Enter correct login credentials
    When I click the login button
    Then The top ten casinos are displayed on home page