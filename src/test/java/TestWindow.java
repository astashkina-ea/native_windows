import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TestWindow {

    private WebDriver driver;
    private String url;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterEach
    public void setDown() {
        if (driver != null)
            driver.quit();
    }

    @Test
    public void alertTest() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.xpath("//*[@id='alertButton']")).click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals("You clicked a button", alert.getText(), "Что-то пошло не так");
        System.out.println(alert.getText());
        alert.accept();
    }

    @Test
    public void confirmTest() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.xpath("//*[@id='confirmButton']")).click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals("Do you confirm action?", alert.getText(), "Что-то пошло не так");
        System.out.println(alert.getText());
        alert.accept(); //dismiss
        WebElement actual = driver.findElement(By.xpath("//*[@id='confirmResult']"));
        Assertions.assertEquals("You selected Ok", actual.getText(), "Что-то пошло не так");
    }


    @Test
    public void promptTest() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.xpath("//*[@id='promtButton']")).click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals("Please enter your name", alert.getText(), "Что-то пошло не так");
        System.out.println(alert.getText());
        alert.sendKeys("Kate"); //dismiss
        alert.accept(); //dismiss
        WebElement actual = driver.findElement(By.xpath("//*[@id='promptResult']"));
        Assertions.assertEquals("You entered Kate", actual.getText(), "Что-то пошло не так");
    }

    //как обойти базовую авторизацию
    @Test
    public void baseAuth() {
        driver.get("http://admin:password@demoqa.com");
    }

    @Test
    public void uploadTest() {
        driver.get("https://the-internet.herokuapp.com/upload");
        driver.findElement(By.xpath("//*[@id='file-upload']")).sendKeys("/Users/user/Desktop/otus-lesson-25/src/test/resources/img.png");
        driver.findElement(By.xpath("//*[@id='file-submit']")).submit();
    }

    @Test
    public void iFrameTest() {
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='frame1']"))); //переход в iframe
        WebElement element = driver.findElement(By.xpath("//*[@id='sampleHeading']"));
        System.out.println(element.getText());
        driver.switchTo().parentFrame(); //в родителя defaultContent - в самый верх всех фреймов
    }

    @Test
    public void windowTest() {
        driver.get("https://ya.ru");
        driver.findElement(By.xpath("//*[@name='text']")).sendKeys("wiki" + Keys.ENTER);
        String yaPage = driver.getWindowHandle(); //данные текущей вкладки
        driver.findElement(By.cssSelector("#search-result > li:nth-child(3) > div > div.VanillaReact.OrganicTitle.OrganicTitle_multiline.Typo.Typo_text_l.Typo_line_m.organic__title-wrapper > a > h2 > span")).click();
        Set<String> allPages = driver.getWindowHandles();//список всех окон
        for (String pageid:allPages) {
            driver.switchTo().window(pageid);
        }
        driver.close();
        driver.switchTo().window(yaPage);
    }
}