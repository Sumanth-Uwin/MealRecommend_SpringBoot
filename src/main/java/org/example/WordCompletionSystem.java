package org.example;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.*;

// TreeNode class for Red-Black Tree
class TreeNode {
    String text;
    int count;
    TreeNode leftChild, rightChild, ancestor;
    boolean red;

    public TreeNode(String text) {
        this.text = text;
        this.count = 1;
        this.red = true;
        this.leftChild = this.rightChild = this.ancestor = null;
    }
}

class RedBlackTreeStructure {
    public TreeNode rootNode;
    public TreeNode sentinel;

    public RedBlackTreeStructure() {
        sentinel = new TreeNode("");
        sentinel.red = false;
        rootNode = sentinel;
    }

    public void add(String text) {
        TreeNode node = rootNode;
        TreeNode parent = sentinel;

        while (node != sentinel) {
            parent = node;
            int comp = text.compareTo(node.text);
            if (comp == 0) {
                node.count++;
                return;
            } else if (comp < 0) {
                node = node.leftChild;
            } else {
                node = node.rightChild;
            }
        }

        TreeNode newNode = new TreeNode(text);
        newNode.ancestor = parent;
        newNode.leftChild = sentinel;
        newNode.rightChild = sentinel;

        if (parent == sentinel) {
            rootNode = newNode;
        } else if (text.compareTo(parent.text) < 0) {
            parent.leftChild = newNode;
        } else {
            parent.rightChild = newNode;
        }

        maintainAfterInsert(newNode);
    }

    private void maintainAfterInsert(TreeNode k) {
        while (k.ancestor.red) {
            if (k.ancestor == k.ancestor.ancestor.leftChild) {
                TreeNode uncle = k.ancestor.ancestor.rightChild;

                if (uncle.red) {
                    k.ancestor.red = false;
                    uncle.red = false;
                    k.ancestor.ancestor.red = true;
                    k = k.ancestor.ancestor;
                } else {
                    if (k == k.ancestor.rightChild) {
                        k = k.ancestor;
                        rotateLeft(k);
                    }
                    k.ancestor.red = false;
                    k.ancestor.ancestor.red = true;
                    rotateRight(k.ancestor.ancestor);
                }
            } else {
                TreeNode uncle = k.ancestor.ancestor.leftChild;

                if (uncle.red) {
                    k.ancestor.red = false;
                    uncle.red = false;
                    k.ancestor.ancestor.red = true;
                    k = k.ancestor.ancestor;
                } else {
                    if (k == k.ancestor.leftChild) {
                        k = k.ancestor;
                        rotateRight(k);
                    }
                    k.ancestor.red = false;
                    k.ancestor.ancestor.red = true;
                    rotateLeft(k.ancestor.ancestor);
                }
            }

            if (k == rootNode) {
                break;
            }
        }
        rootNode.red = false;
    }

    private void rotateLeft(TreeNode x) {
        TreeNode y = x.rightChild;
        x.rightChild = y.leftChild;

        if (y.leftChild != sentinel) {
            y.leftChild.ancestor = x;
        }

        y.ancestor = x.ancestor;

        if (x.ancestor == sentinel) {
            rootNode = y;
        } else if (x == x.ancestor.leftChild) {
            x.ancestor.leftChild = y;
        } else {
            x.ancestor.rightChild = y;
        }

        y.leftChild = x;
        x.ancestor = y;
    }

    private void rotateRight(TreeNode x) {
        TreeNode y = x.leftChild;
        x.leftChild = y.rightChild;

        if (y.rightChild != sentinel) {
            y.rightChild.ancestor = x;
        }

        y.ancestor = x.ancestor;

        if (x.ancestor == sentinel) {
            rootNode = y;
        } else if (x == x.ancestor.rightChild) {
            x.ancestor.rightChild = y;
        } else {
            x.ancestor.leftChild = y;
        }

        y.rightChild = x;
        x.ancestor = y;
    }

    public List<Suggestion> searchByPrefix(String prefix) {
        List<Suggestion> suggestions = new ArrayList<>();
        searchHelper(rootNode, prefix, suggestions);

        suggestions.sort((s1, s2) -> Integer.compare(s2.occurrence, s1.occurrence));
        return suggestions;
    }

    private void searchHelper(TreeNode node, String prefix, List<Suggestion> suggestions) {
        if (node == sentinel) return;

        int cmp = prefix.compareTo(node.text.substring(0, Math.min(prefix.length(), node.text.length())));

        if (cmp == 0) {
            if (node.text.startsWith(prefix)) {
                suggestions.add(new Suggestion(node.text, node.count));
            }
            searchHelper(node.leftChild, prefix, suggestions);
            searchHelper(node.rightChild, prefix, suggestions);
        } else if (cmp < 0) {
            searchHelper(node.leftChild, prefix, suggestions);
        } else {
            searchHelper(node.rightChild, prefix, suggestions);
        }
    }
}



// Dictionary class to manage the word suggestions system
class Dictionary {
    private RedBlackTreeStructure rbTree;

    public Dictionary() {
        rbTree = new RedBlackTreeStructure();
    }

    // Load words from a file
    public void importFromExcel(String filepath) throws IOException {
        FileInputStream fis = new FileInputStream(filepath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Cell cell = row.getCell(0);
            if (cell != null && cell.getCellType() == CellType.STRING) {
                String[] words = cell.getStringCellValue().toLowerCase().split("[\\s,]+");
                for (String word : words) {
                    word = word.replaceAll("[^a-zA-Z]", "");
                    if (!word.isEmpty()) {
                        rbTree.add(word);
                    }
                }
            }
        }
        workbook.close();
    }

    // Fetch word suggestions for a prefix, sorted by frequency
    public List<Suggestion> getSuggestions(String prefix) {
        return rbTree.searchByPrefix(prefix.toLowerCase());
    }
}

// Example usage
public class WordCompletionSystem {
    public static void main(String[] args) {
        try {
            Dictionary dictionary = new Dictionary();

            // Load words from the Excel file
            dictionary.importFromExcel("recipes.csv");

            // Test word suggestions
            String prefix = "ch"; // Adjust as needed
            List<Suggestion> suggestions = dictionary.getSuggestions(prefix);

            System.out.println("Suggestions for '" + prefix + "':");
            System.out.printf("%-5s | %-12s | %s%n", "Rank", "Term", "Frequency");
            System.out.println("-----------------------------------");

            int rank = 1;
            for (Suggestion suggestion : suggestions) {
                System.out.printf("%-5d | %-12s | %d%n", rank++, suggestion.term, suggestion.occurrence);
            }

        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
        }
    }
}
