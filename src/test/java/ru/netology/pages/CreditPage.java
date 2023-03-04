package ru.netology.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class CreditPage {
    private SelenideElement heading = Selenide.$x("//h3[text()='Кредит по данным карты']");

    private SelenideElement cardNumberField = Selenide.$x("//span[text()='Номер карты']" +
            "/following-sibling::span/input");
    private SelenideElement monthField = Selenide.$x("//span[text()='Месяц']" +
            "/following-sibling::span/input");
    private SelenideElement yearField = Selenide.$x("//span[text()='Год']" +
            "/following-sibling::span/input");
    private SelenideElement cardHolderField = Selenide.$x("//span[text()='Владелец']" +
            "/following-sibling::span/input");
    private SelenideElement cvcField = Selenide.$x("//span[text()='CVC/CVV']" +
            "/following-sibling::span/input");

    private SelenideElement continueButton = Selenide.$x("//span[text()='Продолжить']");
    private SelenideElement errorMsgWithDecline = Selenide.$x("//div[text()='Ошибка!" +
            " Банк отказал в проведении операции.']");
    private SelenideElement approvedMsg = Selenide.$x("//div[text()='Операция одобрена Банком.']");

    private SelenideElement warningCardNumberField = Selenide.$x("//span[text()='Номер карты']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement warningMonthField = Selenide.$x("//span[text()='Месяц']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement warningYearField = Selenide.$x("//span[text()='Год']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement warningCardHolderField = Selenide.$x("//span[text()='Владелец']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement warningCvcField = Selenide.$x("//span[text()='CVC/CVV']" +
            "/following-sibling::span[@class='input__sub']");

    public CreditPage() {
        heading.shouldBe(visible);
    }

    public void insertValidCreditCardDataForBank(DataGenerator.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        cardHolderField.setValue(cardInfo.getHolder());
        cvcField.setValue(cardInfo.getCvc());
        continueButton.click();
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void checkErrorMsgDeclineFromBank() {
        errorMsgWithDecline.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void checkApprovedMsgFromBank() {
        approvedMsg.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void checkWarningUnderCardNumberField(String warningText) {
        warningCardNumberField.shouldBe(visible);
        warningCardNumberField.shouldHave(text(warningText));
    }

    public void checkWarningUnderMonthField(String warningText) {
        warningMonthField.shouldBe(visible);
        warningMonthField.shouldHave(text(warningText));
    }

    public void checkWarningUnderYearField(String warningText) {
        warningYearField.shouldBe(visible);
        warningYearField.shouldHave(text(warningText));
    }

    public void checkWarningUnderCardHolderField(String warningText) {
        warningCardHolderField.shouldBe(visible);
        warningCardHolderField.shouldHave(text(warningText));
    }

    public void checkWarningUnderCvcField(String warningText) {
        warningCvcField.shouldBe(visible);
        warningCvcField.shouldHave(text(warningText));
    }

    public void notCheckWarningUnderAllFields() {
        warningCardNumberField.shouldNotBe(visible);
        warningMonthField.shouldNotBe(visible);
        warningYearField.shouldNotBe(visible);
        warningCardHolderField.shouldNotBe(visible);
        warningCvcField.shouldNotBe(visible);
    }
}
