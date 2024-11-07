import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class WebScraperCopy2 {
    public static void main(String[] args) {
        // Set the path to your ChromeDriver
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        //WebDriverManager.chromedriver().setup();
        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();

        // Create a workbook and a sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Recipes");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Recipe Title");
        headerRow.createCell(1).setCellValue("Image URL");

        try {
            // Open the WeCook website
            driver.get("https://www.wecookmeals.ca/en/week-menu/2024-10-06");

            // Maximize the window
            driver.manage().window().maximize();

            // Scrape main menu items
            scrapeMenuItems(driver, sheet, ".swiper-slide.w-fit.page-menu-item__meal");

            // Handle family meals section
            clickAndScrapeSection(driver, sheet, ".swiper-slide.w-fit.page-menu-item__family-meal", "Family Meal Recipes");

            // Handle week upsells section
            clickAndScrapeSection(driver, sheet, ".swiper-slide.w-fit.page-menu-item__week_upsells", "Week Upsells Recipes");

            // Handle groceries section
            clickAndScrapeSection(driver, sheet, ".swiper-slide.w-fit.page-menu-item__groceries", "Groceries Recipes");

            // Write the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream("recipes.csv")) {
                workbook.write(fileOut);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser and the workbook
            driver.quit();
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void scrapeMenuItems(WebDriver driver, Sheet sheet, String parentClass) {
        List<WebElement> recipeImage = driver.findElements(By.xpath("//img[contains(@src,'https://cdn.')]"));
        for (int i = 0; i < recipeImage.size(); i++) {
            String url = recipeImage.get(i).getAttribute("src");
            String rtext = recipeImage.get(i).getAttribute("alt");

            if (!rtext.isEmpty()) { // Check if the recipe name is not empty
                System.out.println("Scraped Recipe: " + rtext + " | Image URL: " + url);

                // Write the recipe name and image URL to the Excel sheet
                Row row = sheet.createRow(sheet.getLastRowNum() + 1); // Create a new row
                row.createCell(0).setCellValue(rtext);
                row.createCell(1).setCellValue(url);
            }
        }
    }

    public static void clickAndScrapeSection(WebDriver driver, Sheet sheet, String cssSelector, String sectionName) {
        // Use WebDriverWait to wait for the element to be clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        WebElement sectionElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssSelector)));
        sectionElement.click();  // Perform the click action

        // Wait for the section to load (wait for any unique element in the section)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".text-body-sm, .text-body-md"))); // Adjust if necessary

        // Write section title to Excel sheet
        Row sectionRow = sheet.createRow(sheet.getLastRowNum() + 1); // Create a new row
        sectionRow.createCell(0).setCellValue(sectionName); // Optionally write the section title

        // Scrape menu items from the clicked section
        scrapeMenuItems(driver, sheet, cssSelector);
    }
}
