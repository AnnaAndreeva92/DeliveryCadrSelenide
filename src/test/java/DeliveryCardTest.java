import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;



public class DeliveryCardTest {
    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    @BeforeEach
    public void openForm() {
        open("http://localhost:9999/");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }

    @Test
    public void shouldFillFormValidData() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id ='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id ='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").should(exactText("Встреча успешно забронирована на " + newDate.format(formatter)));

    }

    @Test
    public void shouldEnteringLastNameWithHyphen() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("Андреева-Хашиг Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").should(exactText("Встреча успешно забронирована на " + newDate.format(formatter)));
    }

    @Test
    public void shouldNameMustBeEnteredWithHyphen() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("Андреева Анна-Стефания");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").should(exactText("Встреча успешно забронирована на " + newDate.format(formatter)));
    }

    @Test
    public void shouldEnteringTheCityInLatin() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val(" Moscow");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='city'] .input__sub").should(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldEnteringTheLastNameAndFirstNameInLatin() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("Andreeva Anna");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='name'] .input__sub").should(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldLessThanThreeFromTheCurrentDate() {
        $x("//input[@placeholder='Город']").val("Москва");
        $("[data-test-id='date'] input").setValue("20.10.2022");
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='date'] .input__sub").should(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldEnteringAnInvalidPhoneNumber() {
        $x("//input[@placeholder='Город']").val("Москва");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='phone'] .input__sub").should(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldCheckboxValidation() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id=agreement] .checkbox__text").should(exactText("Я соглашаюсь с условиями обработки и использования  моих персональных данных"));
    }

    @Test
    public void shouldFirstAndLastNameFieldsAreEmpty() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='name'] .input__sub").should(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldPhoneNumberIsNotFilled() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='phone'] .input__sub").should(exactText("Поле обязательно для заполнения"));
    }
    @Test
    public void shouldInsertingZerosInDate(){
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys("00.00.0000");
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='date'] .input__sub").should(exactText("Неверно введена дата"));
    }
    /*@Test
    public void shouldEnteringZerosInThePhoneNumber(){
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+00000000000");
        $("[data-test-id ='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id ='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='phone'] .input__sub").should(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }*/
}
