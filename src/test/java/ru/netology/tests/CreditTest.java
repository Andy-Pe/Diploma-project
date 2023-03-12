package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.pages.DashboardPage;


import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;

public class CreditTest {

    DashboardPage page = open("http://localhost:8080/", DashboardPage.class);

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }


    @Test
    @DisplayName("The credit card must be approved")
    void shouldSuccessTransactionWithCreditCard() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }


    @Test
    @DisplayName("The credit card must be declined")
    void shouldUnSuccessTransactionWithCreditCard() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithDeclineCard();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkErrorMsgDeclineFromBank();
    }

    @Test
    @DisplayName("A credit card must be approved indicating the month in one digit")
    void shouldSuccessTransactionWithMonthWithoutZero() {
        var creditPage = page.creditPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear
                ("8", String.valueOf(validYear));
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("A credit card with a maximum date must be approved")
    void shouldSuccessTransactionWithMaxAllowedDate() {
        var creditPage = page.creditPage();
        var currentMonth = DataGenerator.getCurrentMonth();
        var maxYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 5;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear(currentMonth,
                String.valueOf(maxYear));
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("A credit card with a maximum date minus 1 month must be approved")
    void shouldSuccessTransactionWithPreMaxAllowedDate() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithMaxDateMinusOneMonth();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("A credit card with a minimum date (current month) must be approved")
    void shouldSuccessTransactionWithMinAllowedDate() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear
                (DataGenerator.getCurrentMonth(), DataGenerator.getCurrentYear());
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("A credit card with an expiration date next month must be approved")
    void shouldSuccessTransactionWithPreMinAllowedDate() {
        var creditPage = page.creditPage();
        var nextMonth = Integer.parseInt(DataGenerator.getCurrentMonth()) + 1;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear
                (String.valueOf(nextMonth), DataGenerator.getCurrentYear());
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("Should use a credit card with the name of the cardholder of the maximum length")
    void shouldSuccessTransactionMaxLengthCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedLengthCardHolderName(21);
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("Should use a credit card with the name of the cardholder of the minimum length")
    void shouldSuccessTransactionMinLengthCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedLengthCardHolderName(2);
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("Should reject a credit card with a random number")
    void shouldDeclineWithRandomCreditCard() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithRandomCardNumber();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkErrorMsgDeclineFromBank();
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty card number field")
    void shouldShowMsgWithEmptyCardNumberField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setNumber("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardNumberField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty month field")
    void shouldShowMsgWithEmptyMonthField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setMonth("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty year field")
    void shouldShowMsgWithEmptyYearField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setYear("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderYearField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty card holder field")
    void shouldShowMsgWithEmptyCardHolderField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setHolder("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty cvc field")
    void shouldShowMsgWithEmptyCvcField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setCvc("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty all field")
    void shouldShowMsgWithEmptyAllField() {
        var creditPage = page.creditPage();
        creditPage.clickContinueButton();
        creditPage.checkWarningUnderCardNumberField("Неверный формат");
        creditPage.checkWarningUnderMonthField("Неверный формат");
        creditPage.checkWarningUnderYearField("Неверный формат");
        creditPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
        creditPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should not be displayed after filling in the fields")
    void shouldNotShowMsgAfterFillingAllField() {
        var creditPage = page.creditPage();
        creditPage.clickContinueButton();
        creditPage.checkWarningUnderCardNumberField("Неверный формат");
        creditPage.checkWarningUnderMonthField("Неверный формат");
        creditPage.checkWarningUnderYearField("Неверный формат");
        creditPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
        creditPage.checkWarningUnderCvcField("Неверный формат");

        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
        creditPage.notCheckWarningUnderAllFields();
    }

    @Test
    @DisplayName("A red warning with an expired card for a year should be displayed")
    void shouldShowMsgWithExpiredCardForYear() {
        var creditPage = page.creditPage();
        var currentMonth = DataGenerator.getCurrentMonth();
        var lastYear = Integer.parseInt(DataGenerator.getCurrentYear()) - 1;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear(currentMonth,
                String.valueOf(lastYear));
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderYearField("Истёк срок действия карты");
    }

    @Test
    @DisplayName("A red warning with an expired card for a month should be displayed")
    void shouldShowMsgWithExpiredCardForMonth() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithAnExpiredCardForOneMonth();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("A red warning with 00 month should be displayed")
    void shouldShowMsgWhenMonthIsZero() {
        var creditPage = page.creditPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear
                ("00", String.valueOf(validYear));
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("A red warning should be displayed with invalid data in the month field")
    void shouldShowMsgWithInvalidMonthData() {
        var creditPage = page.creditPage();
        var currentYear = DataGenerator.getCurrentYear();
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear("13",
                currentYear);
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("A red warning about the maximum value for the 'Holder' field should be displayed")
    void shouldShowMsgWithMoreMaxLengthCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedLengthCardHolderName(22);
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Имя и фамилия не должны быть длиннее 21 символа");
    }

    @Test
    @DisplayName("A red warning should be displayed with the name of the cardholder of the minimum length")
    void shouldShowMsgWithLessMinLengthCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedLengthCardHolderName(1);
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Имя не должно быть короче 2 символов");
    }

    @Test
    @DisplayName("A red warning should be displayed with the cardholder's name written in Cyrillic")
    void shouldShowMsgWithCyrillicCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedCardHolderName("Иванов Иван");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }

    @Test
    @DisplayName("A red warning should be displayed if the cardholder's name contains numbers")
    void shouldShowMsgWithNumbersInCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedCardHolderName("123 Иван");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }

    @Test
    @DisplayName("A red warning should be displayed if the cardholder's name contains special characters")
    void shouldShowMsgWithSpecCharsCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedCardHolderName("!#$%^&");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }
}
