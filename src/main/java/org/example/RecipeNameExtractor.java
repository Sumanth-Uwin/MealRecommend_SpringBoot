package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeNameExtractor {
    public static void main(String[] args) {
        try {
            // Load the Excel file
            FileInputStream file = new FileInputStream("recipes.csv");
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0); // Assuming the recipes are in the first sheet

            // Extract recipe names from the Excel file
            List<String> recipeNames = new ArrayList<>();
            for (Row row : sheet) {
                Cell cell = row.getCell(0); // Assuming recipe names are in the first column
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    recipeNames.add(cell.getStringCellValue().trim());
                }
            }
            workbook.close();

            // Print recipe names to the console
            System.out.println("Successfully extracted " + recipeNames.size() + " recipe names from recipes.xlsx");
            System.out.println("\nExtracted recipes:");
            for (String recipe : recipeNames) {
                System.out.println("- " + recipe);
            }

        } catch (IOException e) {
            System.err.println("Error processing the Excel file: " + e.getMessage());
        }
    }
}
