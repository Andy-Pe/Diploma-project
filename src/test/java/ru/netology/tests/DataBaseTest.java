package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.APIHelper;
import ru.netology.data.DataGenerator;
import ru.netology.data.SQLHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataBaseTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should add the payment data to the database with APPROVAL via the API")
    void shouldSuccessTransactionWithApprovedPaymentCardViaAPI() {
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        APIHelper.createPayment(cardInfo);
        var paymentCardData = SQLHelper.getPaymentCardData();
        assertEquals("APPROVED", paymentCardData.getStatus());

    }

    @Test
    @DisplayName("Should add the credit data to the database with APPROVAL via the API")
    void shouldSuccessTransactionWithApprovedCreditCardViaAPI() {
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        APIHelper.createCredit(cardInfo);
        var creditCardData = SQLHelper.getCreditCardData();
        assertEquals("APPROVED", creditCardData.getStatus());

    }

    @Test
    @DisplayName("Should add the payment data to the database with a DECLINED via the API")
    void shouldSuccessTransactionWithDeclinedPaymentCardViaAPI() {
        var cardInfo = DataGenerator.generateDataWithDeclineCard();
        APIHelper.createPayment(cardInfo);
        var paymentCardData = SQLHelper.getPaymentCardData();
        assertEquals("DECLINED", paymentCardData.getStatus());

    }

    @Test
    @DisplayName("Should add the credit data to the database with a DECLINED via the API")
    void shouldSuccessTransactionWithDeclinedCreditCardViaAPI() {
        var cardInfo = DataGenerator.generateDataWithDeclineCard();
        APIHelper.createCredit(cardInfo);
        var creditCardData = SQLHelper.getCreditCardData();
        assertEquals("DECLINED", creditCardData.getStatus());

    }

    @Test
    @DisplayName("Should add the correct created date to the payment table with the APPROVED card")
    void shouldAddCorrectDateInPaymentTableWithApprovedCard() {
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        APIHelper.createPayment(cardInfo);
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        var paymentCardData = SQLHelper.getPaymentCardData();
        String dateFromDB = paymentCardData.getCreated();
        var dateDB = dateFromDB.substring(0, dateFromDB.length() - 10);
        assertEquals(formatForDateNow.format(dateNow), dateDB);
    }

    @Test
    @DisplayName("Should add the correct created date to the credit table with the APPROVED card")
    void shouldAddCorrectDateInCreditTableWithApprovedCard() {
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        APIHelper.createCredit(cardInfo);
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        var creditCardData = SQLHelper.getCreditCardData();
        String dateFromDB = creditCardData.getCreated();
        var dateDB = dateFromDB.substring(0, dateFromDB.length() - 10);
        assertEquals(formatForDateNow.format(dateNow), dateDB);
    }

    @Test
    @DisplayName("Should add the correct created date to the payment table with the DECLINED card")
    void shouldAddCorrectDateInPaymentTableWithDeclinedCard() {
        var cardInfo = DataGenerator.generateDataWithDeclineCard();
        APIHelper.createPayment(cardInfo);
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        var paymentCardData = SQLHelper.getPaymentCardData();
        String dateFromDB = paymentCardData.getCreated();
        var dateDB = dateFromDB.substring(0, dateFromDB.length() - 10);
        assertEquals(formatForDateNow.format(dateNow), dateDB);
    }

    @Test
    @DisplayName("Should add the correct created date to the credit table with the DECLINED card")
    void shouldAddCorrectDateInCreditTableWithDeclinedCard() {
        var cardInfo = DataGenerator.generateDataWithDeclineCard();
        APIHelper.createCredit(cardInfo);
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        var creditCardData = SQLHelper.getCreditCardData();
        String dateFromDB = creditCardData.getCreated();
        var dateDB = dateFromDB.substring(0, dateFromDB.length() - 10);
        assertEquals(formatForDateNow.format(dateNow), dateDB);
    }

    @Test
    @DisplayName("Should add the correct payment data in order_entity table")
    void shouldAddCorrectPaymentDataInOrderTable() {
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        APIHelper.createPayment(cardInfo);
        var cardDataFromPaymentTable = SQLHelper.getPaymentCardData();
        var cardDataFromOrderTable = SQLHelper.getTableOrderEntity();
        assertEquals(cardDataFromPaymentTable.getTransaction_id(), cardDataFromOrderTable.getPayment_id());
    }

    @Test
    @DisplayName("Should add the correct credit data in order_entity table")
    void shouldAddCorrectCreditDataInOrderTable() {
        var cardInfo = DataGenerator.generateDataWithApprovedCard();
        APIHelper.createCredit(cardInfo);
        var cardDataFromCreditTable = SQLHelper.getCreditCardData();
        var cardDataFromOrderTable = SQLHelper.getTableOrderEntity();
        assertEquals(cardDataFromCreditTable.getBank_id(), cardDataFromOrderTable.getCredit_id());
    }
}
