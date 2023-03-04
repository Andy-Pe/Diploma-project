package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.pages.DashboardPage;


import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;

public class PaymentTest {

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
    @DisplayName("The payment card must be approved")
    void shouldSuccessTransactionWithPaymentCard() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }


    @Test
    @DisplayName("The payment card must be declined")
    void shouldUnSuccessTransactionWithPaymentCard() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithDeclineCard();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkErrorMsgDeclineFromBank();
    }

    @Test
    @DisplayName("A payment card must be approved indicating the month in one digit")
    void shouldSuccessTransactionWithMonthWithoutZero() {
        var paymentPage = page.paymentPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear
                ("5", String.valueOf(validYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("A payment card with a maximum date must be approved")
    void shouldSuccessTransactionWithMaxAllowedDate() {
        var paymentPage = page.paymentPage();
        var currentMonth = DataGenerator.getCurrentMonth();
        var maxYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 5;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear(currentMonth,
                String.valueOf(maxYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("A payment card with a maximum date minus 1 month must be approved")
    void shouldSuccessTransactionWithPreMaxAllowedDate() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithMaxDateMinusOneMonth();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("A payment card with a minimum date (current month) must be approved")
    void shouldSuccessTransactionWithMinAllowedDate() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear
                (DataGenerator.getCurrentMonth(), DataGenerator.getCurrentYear());
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("A payment card with an expiration date next month must be approved")
    void shouldSuccessTransactionWithPreMinAllowedDate() {
        var paymentPage = page.paymentPage();
        var nextMonth = Integer.parseInt(DataGenerator.getCurrentMonth()) + 1;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear
                (String.valueOf(nextMonth), DataGenerator.getCurrentYear());
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("Should use a payment card with the name of the cardholder of the maximum length")
    void shouldSuccessTransactionMaxLengthCardHolderName() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedLengthCardHolderName(21);
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("Should use a payment card with the name of the cardholder of the minimum length")
    void shouldSuccessTransactionMinLengthCardHolderName() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedLengthCardHolderName(2);
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }

    @Test
    @DisplayName("Should reject a payment card with a random number")
    void shouldDeclineWithRandomPaymentCard() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithRandomCardNumber();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkErrorMsgDeclineFromBank();
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty card number field")
    void shouldShowMsgWithEmptyCardNumberField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setNumber("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardNumberField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty month field")
    void shouldShowMsgWithEmptyMonthField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setMonth("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty year field")
    void shouldShowMsgWithEmptyYearField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setYear("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderYearField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty card holder field")
    void shouldShowMsgWithEmptyCardHolderField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setHolder("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty cvc field")
    void shouldShowMsgWithEmptyCvcField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        cardInfo.setCvc("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should be displayed with an empty all field")
    void shouldShowMsgWithEmptyAllField() {
        var paymentPage = page.paymentPage();
        paymentPage.clickContinueButton();
        paymentPage.checkWarningUnderCardNumberField("Неверный формат");
        paymentPage.checkWarningUnderMonthField("Неверный формат");
        paymentPage.checkWarningUnderYearField("Неверный формат");
        paymentPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
        paymentPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    @DisplayName("A red warning should not be displayed after filling in the fields")
    void shouldNotShowMsgAfterFillingAllFields() {
        var paymentPage = page.paymentPage();
        paymentPage.clickContinueButton();
        paymentPage.checkWarningUnderCardNumberField("Неверный формат");
        paymentPage.checkWarningUnderMonthField("Неверный формат");
        paymentPage.checkWarningUnderYearField("Неверный формат");
        paymentPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
        paymentPage.checkWarningUnderCvcField("Неверный формат");

        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
        paymentPage.notCheckWarningUnderAllFields();
    }

    @Test
    @DisplayName("A red warning with an expired card for a year should be displayed")
    void shouldShowMsgWithExpiredCardForYear() {
        var paymentPage = page.paymentPage();
        var currentMonth = DataGenerator.getCurrentMonth();
        var lastYear = Integer.parseInt(DataGenerator.getCurrentYear()) - 1;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear(currentMonth,
                String.valueOf(lastYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderYearField("Истёк срок действия карты");
    }

    @Test
    @DisplayName("A red warning with an expired card for a month should be displayed")
    void shouldShowMsgWithExpiredCardForMonth() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithAnExpiredCardForOneMonth();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("A red warning with 00 month should be displayed.")
    void shouldShowMsgWhenMonthIsZero() {
        var paymentPage = page.paymentPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear
                ("00", String.valueOf(validYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("A red warning should be displayed with invalid data in the month field")
    void shouldShowMsgWithInvalidMonthData() {
        var paymentPage = page.paymentPage();
        var currentYear = DataGenerator.getCurrentYear();
        var cardInfo = DataGenerator.generateDataWithApprovedCardAndParametrizedMonthAndYear("13",
                currentYear);
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("A red warning about the maximum value for the 'Holder' field should be displayed")
    void shouldShowMsgWithMoreMaxLengthCardHolderName() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedLengthCardHolderName(22);
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Имя не должно быть длинее 21 символа");
    }

    @Test
    @DisplayName("A red warning should be displayed with the name of the cardholder of the minimum length")
    void shouldShowMsgWithLessMinLengthCardHolderName() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedLengthCardHolderName(1);
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Имя не должно быть короче 2 символов");
    }

    @Test
    @DisplayName("A red warning should be displayed with the cardholder's name written in Cyrillic")
    void shouldShowMsgWithCyrillicCardHolderName() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedCardHolderName("Иванов Иван");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }

    @Test
    @DisplayName("A red warning should be displayed if the cardholder's name contains numbers")
    void shouldShowMsgWithNumbersCardHolderName() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedCardHolderName("123 Иван");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }

    @Test
    @DisplayName("A red warning should be displayed if the cardholder's name contains special characters")
    void shouldShowMsgWithSpecCharsCardHolderName() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.generateDataWithParametrizedCardHolderName("!№;%:?");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }
}
