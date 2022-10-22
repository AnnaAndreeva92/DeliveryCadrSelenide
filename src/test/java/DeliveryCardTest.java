import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;



public class DeliveryCardTest {
    @BeforeEach
    public void openForm() {
        open("http://localhost:9999/");
    }

    @Test
    public void shouldFillFormValidData() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder='Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id ='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id ='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").should(exactText("Встреча успешно забронирована на " + meetingDate));

    }

    @Test
    public void shouldEnteringLastNameWithHyphen() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder='Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Андреева-Хашиг Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").should(exactText("Встреча успешно забронирована на " + meetingDate));
    }

    @Test
    public void shouldNameMustBeEnteredWithHyphen() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder='Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Андреева Анна-Стефания");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").should(exactText("Встреча успешно забронирована на " + meetingDate));
    }

    @Test
    public void shouldEnteringTheCityInLatin() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val(" Moscow");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder='Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='city'] .input__sub").should(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldEnteringTheLastNameAndFirstNameInLatin() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder='Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Andreeva Anna");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='name'] .input__sub").should(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldEnteringDateEarlierThanThreeDaysFromTheCurrentDate() {
        $x("//input[@placeholder='Город']").val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder='Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='date'] .input__sub").should(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldEnteringAnInvalidPhoneNumber() {
        $x("//input[@placeholder='Город']").val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder='Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='phone'] .input__sub").should(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldCheckboxValidation() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder= 'Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id=agreement] .checkbox__text").should(exactText("Я соглашаюсь с условиями обработки и использования  моих персональных данных"));
    }

    @Test
    public void shouldFirstAndLastNameFieldsAreEmpty() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder= 'Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='name'] .input__sub").should(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldPhoneNumberIsNotFilled() {
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder='Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='phone'] .input__sub").should(exactText("Поле обязательно для заполнения"));
    }
    @Test
    public void shouldInsertingZerosInDate(){
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        $x("//input[@placeholder='Дата встречи']").val("00.00.0000");
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+79409999806");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='date'] .input__sub").should(exactText("Неверно введена дата"));
    }
    @Test
    public void shouldEnteringZerosInThePhoneNumber(){
        $$x("//input[@placeholder='Город']").filter(visible).first().val("Москва");
        $x("//input[@type='tel']").doubleClick().sendKeys("DELETE");
        String meetingDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder='Дата встречи']").val(meetingDate);
        $("[data-test-id='name'] input").val("Андреева Анна");
        $("[data-test-id='phone'] input").val("+00000000000");
        $("[data-test-id ='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id ='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").should(exactText("Встреча успешно забронирована на " + meetingDate));
    }
}
