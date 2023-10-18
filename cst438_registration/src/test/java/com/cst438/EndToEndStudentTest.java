package com.cst438;

import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EndToEndStudentTest {

    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";
    public static final String URL = "http://localhost:3000";
    public static final String TEST_STUDENT_NAME = "Marcos Arroyo";
    public static final String UPDATED_STUDENT_NAME = "Dale Sanchez";
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void addStudentTest() throws Exception {
        try {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, 10);
            
            driver.findElement(By.id("addStudent")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("studentName"))).sendKeys(TEST_STUDENT_NAME);
            driver.findElement(By.id("add")).click();

            WebElement studentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[td='" + TEST_STUDENT_NAME + "']")));
            assertNotNull(studentElement, "Test student not found in the list.");

        } finally {
            driver.quit();
        }
    }

    @Test
    public void updateStudentTest() throws Exception {
        try {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, 10);

            WebElement studentElement = driver.findElement(By.xpath("//tr[td='" + TEST_STUDENT_NAME + "']"));
            assertNotNull(studentElement, "Test student not found in the student list.");
            
            WebElement editButton = studentElement.findElement(By.id("editStudent"));
            editButton.click();
            
            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("studentName")));
            nameInput.clear();
            nameInput.sendKeys(UPDATED_STUDENT_NAME);
            driver.findElement(By.id("update")).click();
            
            WebElement updatedStudentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[td='" + UPDATED_STUDENT_NAME + "']")));
            assertNotNull(updatedStudentElement, "Updated student not found in the student list.");
            assertTrue(driver.findElements(By.xpath("//tr[td='" + TEST_STUDENT_NAME + "']")).isEmpty());

        } finally {
            driver.quit();
        }
    }

    @Test
    public void deleteStudentTest() throws Exception {
        try {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, 10);

            WebElement studentElement = driver.findElement(By.xpath("//tr[td='" + UPDATED_STUDENT_NAME + "']"));
            assertNotNull(studentElement, "Student to delete not found.");

            driver.findElement(By.id("deleteStudent")).click();
            driver.findElement(By.id("confirmDelete")).click();
            
            assertTrue(wait.until(ExpectedConditions.invisibilityOf(studentElement)));

        } finally {
            driver.quit();
        }
    }
}
