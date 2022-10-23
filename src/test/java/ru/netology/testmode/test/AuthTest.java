package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {
    @BeforeEach
    void setup() {
        open("http://127.0.0.1:9999");
    }

    @Test
    @DisplayName("Success registered user")
    void successRegisteredUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login'] .button__content").click();
        $$(".heading").findBy(text("  Личный кабинет")).shouldBe(visible);
    }
    @Test
    @DisplayName("Error login not registered")
    void errorLoginNotRegistered() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login'] .button__content").click();
        $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Error login blocked")
    void errorLoginBlocked() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login'] .button__content").click();
        $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Error login invalid login")
    void errorInvalidLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login'] .button__content").click();
        $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Error invalid password")
    void errorInvalidPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("[data-test-id='action-login'] .button__content").click();
        $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Ошибка! Неверно указан логин или пароль"));
    }
}
