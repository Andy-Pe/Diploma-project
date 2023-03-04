package ru.netology.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;


public class DashboardPage {

    private SelenideElement heading = Selenide.$x("//h2[text()='Путешествие дня']");
    private SelenideElement paymentButton = Selenide.$x("//span[text()='Купить']");

    private SelenideElement creditButton = Selenide.$x("//span[text()='Купить в кредит']");

    public DashboardPage() {
        heading.shouldBe(visible);
    }


    public PaymentPage paymentPage() {
        paymentButton.click();
        return new PaymentPage();
    }

    public CreditPage creditPage() {
        creditButton.click();
        return new CreditPage();
    }
}
