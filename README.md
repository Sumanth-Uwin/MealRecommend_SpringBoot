# Meal Kit Comparison and Recommendation System

## Overview
This project is a comprehensive Java-based application that crawls meal kit service websites, extracts relevant data, and provides a comparison and recommendation system to help users choose the best meal kit delivery service based on their preferences.

## Key Components
1. **Web Crawling and Data Extraction**:
   - **Libraries Used**: JSoup for static web content extraction, Selenium for dynamic content loading.
   - **Functionality**: Crawls meal kit service websites and extracts details such as subscription plans, dietary preferences, delivery options, ingredient quality, and additional features.

2. **Data Storage and Structuring**:
   - **Classes Defined**: Java classes such as `MealKitService`, `SubscriptionPlan`, `DietaryPreference`.
   - **Data Storage Methods**: In-memory Java collections for small data sets, or an external database (e.g., SQLite) for larger data.

3. **Data Comparison Engine**:
   - **Comparator Implementation**: A custom engine that evaluates meal kit services based on user-selected parameters (e.g., price, dietary options).
   - **Sorting and Filtering**: Uses Java’s `Comparator` and `Stream` API for efficient sorting and filtering.

4. **User Preferences and Recommendation System**:
   - **User Input**: A console-based form for users to specify their preferences.
   - **Matching Algorithm**: Ranks services based on how well they meet user criteria.
   - **Output**: Displays a list of services, highlighting features that align with the user's input.

5. **Pattern Finder for Contact Details**:
   - **Regex Implementation**: Java Regex patterns used to search for phone numbers, emails, and URLs in text files.
   - **File Analysis**: Reads all text files in a specified directory, matches patterns, and outputs the results in a formatted table.

## Project Structure
### Directories and Files
- **src/org/example/**:
  - `WebCrawler.java`: Handles web crawling using JSoup and Selenium.
  - `DataExtractor.java`: Extracts and structures data into Java objects.
  - `RecommendationSystem.java`: Contains the logic for comparing meal kit services.
  - `PatternFinder.java`: Searches for contact details within text files.
- **resources/**: Contains sample text files and any configuration files.

## Installation and Setup
### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Maven (optional, for build and dependency management)

### Steps to Run the Application
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/MealKitComparisonSystem.git
   cd MealKitComparisonSystem
   ```
2. **Compile and Run**:
   ```bash
   javac -d bin src/org/example/*.java
   java -cp bin org.example.Main
   ```
3. **Run Pattern Finder**:
   ```bash
   java -cp bin org.example.PatternFinder
   ```

## Example Output
### Web Crawling and Data Extraction
```
Visiting: https://www.examplemealkit.com/plans
Extracted data: Plan Name - Family Pack, Weekly Cost - $59.99, Dietary Options - Vegan, Vegetarian
```

### Pattern Finder
```
Search Results:
+----------------+--------------------------+--------------------------+
| Pattern Type   | File Name               | Found Content            |
+----------------+--------------------------+--------------------------+
| Phone Number   | sample.txt              | +1-800-555-1234           |
| Email Address  | contact.txt             | support@example.com       |
| URL            | links.txt               | https://www.example.com   |
+----------------+--------------------------+--------------------------+
```

## Customization
- **Modify Regex Patterns**: Update the `SearchPattern` enum in `PatternFinder.java`.
- **Adjust Crawling Logic**: Edit `WebCrawler.java` to include or exclude specific HTML elements.

## Contributing
1. Fork this repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-branch
   ```
3. Commit changes:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push to your branch:
   ```bash
   git push origin feature-branch
   ```
5. Open a Pull Request.

## License
This project is licensed under the MIT License. See `LICENSE` for details.

## Contact
For questions or feedback, please contact [your email/contact info].

