package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class InteractiveAutocompleteSession {
    private AutocompleteEngine engine;

    public InteractiveAutocompleteSession() {
        engine = new AutocompleteEngine();
    }

    // Load vocabulary words from an Excel file
    public void loadVocabulary(String filepath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filepath)) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming vocabulary is in the first sheet

            for (Row row : sheet) {
                Cell cell = row.getCell(0); // Assuming words are in the first column
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String[] terms = cell.getStringCellValue().toLowerCase().split("[\\s,.:;\"\\(\\)\\[\\]\\{\\}]+");
                    for (String term : terms) {
                        term = term.replaceAll("[^a-zA-Z]", "").trim(); // Clean up the term
                        if (!term.isEmpty()) {
                            engine.insertWord(term);
                        }
                    }
                }
            }
            workbook.close();
        }
    }

    // Start an interactive session for autocomplete based on prefix
    public void initiateSession() {
        Scanner userInput = new Scanner(System.in);

        while (true) {
            System.out.print("\nEnter a prefix (or 'exit' to quit): ");
            String prefix = userInput.nextLine().trim().toLowerCase();

            if (prefix.equals("exit")) {
                break;
            }

            if (prefix.isEmpty()) {
                System.out.println("Please enter a valid prefix.");
                continue;
            }

            // Retrieve and display sorted suggestions
            List<Suggestion> rankedSuggestions = engine.generateSuggestions(prefix);

            if (rankedSuggestions.isEmpty()) {
                System.out.println("No matches found for prefix '" + prefix + "'");
            } else {
                System.out.println("\nTop suggestions for '" + prefix + "':");
                System.out.println("Rank  |  Term          |  Frequency");
                System.out.println("-----------------------------------");

                int rank = 1;
                for (Suggestion suggestion : rankedSuggestions) {
                    System.out.printf("%-5d | %-15s | %d%n", rank++, suggestion.term, suggestion.occurrence);
                }
            }
        }

        userInput.close();
        System.out.println("Thank you for using the autocomplete system!");
    }

    public static void main(String[] args) {
        InteractiveAutocompleteSession autocompleteApp = new InteractiveAutocompleteSession();

        try {
            // Load vocabulary from an Excel file
            System.out.println("Loading vocabulary from recipes.xlsx...");
            autocompleteApp.loadVocabulary("recipes.csv");
            System.out.println("Vocabulary successfully loaded!");

            // Begin interactive session
            System.out.println("\nWelcome to the Autocomplete System!");
            System.out.println("Type a prefix to see suggestions based on frequency.");
            System.out.println("Type 'exit' to end the program.");

            autocompleteApp.initiateSession();

        } catch (FileNotFoundException e) {
            System.err.println("Error: Unable to locate recipes.xlsx");
            System.err.println("Please confirm the file's presence in the working directory.");
        } catch (IOException e) {
            System.err.println("Error while reading the file: " + e.getMessage());
        }
    }
}

// Suggestion class for storing each term and its frequency count
class Suggestion implements Comparable<Suggestion> {
    String term;
    int occurrence;

    public Suggestion(String term, int occurrence) {
        this.term = term;
        this.occurrence = occurrence;
    }

    @Override
    public int compareTo(Suggestion other) {
        return Integer.compare(this.occurrence, other.occurrence);
    }
}

// Autocomplete engine for managing term storage and retrieval
class AutocompleteEngine {
    private RedBlackTreeStructure tree;
    private static final int MAX_RESULTS = 10;

    public AutocompleteEngine() {
        tree = new RedBlackTreeStructure();
    }

    public void insertWord(String term) {
        tree.add(term);
    }

    public List<Suggestion> generateSuggestions(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return new ArrayList<>();
        }

        // Min-heap to keep top K results based on frequency
        PriorityQueue<Suggestion> minHeap = new PriorityQueue<>();
        searchAndCollect(tree.rootNode, prefix.toLowerCase(), minHeap);

        // Convert min-heap to sorted list (highest frequency first)
        List<Suggestion> sortedList = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            sortedList.add(0, minHeap.poll());
        }

        return sortedList;
    }

    private void searchAndCollect(TreeNode node, String prefix, PriorityQueue<Suggestion> minHeap) {
        if (node == tree.sentinel) {
            return;
        }

        int prefixLength = Math.min(prefix.length(), node.text.length());
        String nodePrefix = node.text.substring(0, prefixLength);
        int compareResult = prefix.compareTo(nodePrefix);

        if (compareResult == 0) {
            if (node.text.startsWith(prefix)) {
                addToHeap(minHeap, new Suggestion(node.text, node.count));
            }
            searchAndCollect(node.leftChild, prefix, minHeap);
            searchAndCollect(node.rightChild, prefix, minHeap);
        } else if (compareResult < 0) {
            searchAndCollect(node.leftChild, prefix, minHeap);
        } else {
            searchAndCollect(node.rightChild, prefix, minHeap);
        }
    }

    private void addToHeap(PriorityQueue<Suggestion> minHeap, Suggestion suggestion) {
        if (minHeap.size() < MAX_RESULTS) {
            minHeap.offer(suggestion);
        } else if (suggestion.occurrence > minHeap.peek().occurrence) {
            minHeap.poll();
            minHeap.offer(suggestion);
        }
    }
}
