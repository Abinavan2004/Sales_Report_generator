# SalesReporter - Sales Summary Report Generator

A clean, modular Java command-line interface (CLI) application that reads sales transaction data from a CSV file, computes sales metrics (revenue per product, category totals, best-selling product, highest-revenue product, grand total), and exports formatted summary reports to either standard output or a designated file.

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Prerequisites & Build Instructions](#prerequisites--build-instructions)
3. [CLI Usage & Examples](#cli-usage--examples)
4. [Input CSV Format](#input-csv-format)
5. [Error Handling & Validation Rules](#error-handling--validation-rules)
6. [Architecture & SOLID Principles Breakdown](#architecture--solid-principles-breakdown)
7. [Team Responsibility Breakdown](#team-responsibility-breakdown)

---

## 1. Project Overview

`SalesReporter` is designed following Object-Oriented Programming (OOP) and S.O.L.I.D. design principles to ensure maintainability, testability, and extensibility.

### Key Capabilities:
- **Flexible Data Ingestion**: Parses CSV files with validation and automatic warning/skipping of corrupted records.
- **Business Analytics**: Aggregates revenue per product, category-level totals, identifies top-performing products by unit volume and revenue, and computes grand total sales.
- **Pluggable Exporters**: Supports printing formatted reports directly to the console or exporting to any target file path.
- **Robust CLI Validation & Global Exception Handling**: Prevents raw stack traces and prints clear, user-friendly diagnostic messages to `System.err`.

---

## 2. Prerequisites & Build Instructions

### Prerequisites
- **Java Development Kit (JDK)**: JDK 17 or higher (tested with Oracle OpenJDK / Temurin JDK 22).
- Operating System: Windows, macOS, or Linux.

### Compile the Application
Open a terminal in the project root directory and compile all source files located in `src`:

```bash
# Compile all source files
javac src/*.java
```

Optionally, you can compile to a dedicated output directory:

```bash
# Create build directory and compile
mkdir -p out
javac -d out src/*.java
```

---

## 3. CLI Usage & Examples

### Command Syntax
```bash
java -cp src SalesReporter <csv-file-path> <output-method> [output-file-path]
```

Or if compiled to the `out` directory:
```bash
java -cp out SalesReporter <csv-file-path> <output-method> [output-file-path]
```

### Argument Specifications

| Argument | Status | Description | Valid Values |
| :--- | :--- | :--- | :--- |
| `<csv-file-path>` | Mandatory | File path to the sales CSV input dataset. | Must point to an existing, readable file. |
| `<output-method>` | Mandatory | Target destination for the generated report. | `console` or `file` (case-insensitive). |
| `[output-file-path]` | Conditional | Path to save the report (required when output method is `file`). | Valid file system path. Parent directories are created automatically if missing. |

### Usage Examples

#### 1. Display Report on Console
```bash
java -cp src SalesReporter src/sales.csv console
```
*Sample Output:*
```text
============================================
 PRODUCT SALES SUMMARY REPORT
============================================

--- Revenue Per Product ---
P001  Wireless Mouse     Electronics   $ 306.00
P002  Notebook           Stationery    $ 131.25
P003  USB Hub            Electronics   $ 144.00
P004  Ballpoint Pen      Stationery    $  50.00
P005  HDMI Cable         Electronics   $ 240.00

--- Revenue Per Category ---
Electronics  : $690.00
Stationery   : $181.25

--- Highlights ---
Best-Selling Product : Ballpoint Pen (100 units)
Highest Revenue      : Wireless Mouse ($306.00)
Grand Total Revenue  : $871.25
============================================
```

#### 2. Export Report to File
```bash
java -cp src SalesReporter src/sales.csv file reports/sales_summary.txt
```
*Output:*
```text
Report successfully written to: reports/sales_summary.txt
```

#### 3. Case-Insensitive Output Method
Both lowercase and uppercase options are supported:
```bash
java -cp src SalesReporter src/sales.csv CONSOLE
java -cp src SalesReporter src/sales.csv FILE output.txt
```

---

## 4. Input CSV Format

The sales dataset must be a comma-separated values (CSV) file containing a header line followed by product records.

### Expected Schema

| Column Index | Column Name | Data Type | Description | Example |
| :---: | :--- | :---: | :--- | :--- |
| 0 | `product_id` | String | Unique identifier for the product | `P001` |
| 1 | `product_name` | String | Commercial name of the product | `Wireless Mouse` |
| 2 | `category` | String | Product category or department | `Electronics` |
| 3 | `quantity_sold`| Integer | Number of units sold (must be non-negative) | `12` |
| 4 | `unit_price` | Decimal | Unit price in USD/currency (must be non-negative) | `25.50` |

### Sample `sales.csv`
```csv
product_id, product_name, category, quantity_sold, unit_price
P001, Wireless Mouse, Electronics, 12, 25.50
P002, Notebook, Stationery, 35, 3.75
P003, USB Hub, Electronics, 8, 18.00
P004, Ballpoint Pen, Stationery, 100, 0.50
P005, HDMI Cable, Electronics, 20, 12.00
```

---

## 5. Error Handling & Validation Rules

The application uses defensive programming and top-level exception wrappers to guarantee clean, user-friendly diagnostics printed to `System.err` with exit status code `1`. No unformatted stack traces are displayed to end users.

| Scenario | Handled Condition | Diagnostic Message on `System.err` | Exit Code |
| :--- | :--- | :--- | :---: |
| **Missing Arguments** | `args.length < 2` | `Input Error: Missing or insufficient command-line arguments.` followed by usage instructions. | `1` |
| **Invalid Output Method** | `<output-method>` is neither `console` nor `file` | `Input Error: Invalid output method '<val>'. Supported values are 'console' or 'file' (case-insensitive).` | `1` |
| **Missing Output Path** | Output method is `file` but no output path given | `Input Error: Output file path is mandatory when output method is 'file'.` | `1` |
| **File Not Found** | `<csv-file-path>` does not exist | `Input Error: Input CSV file does not exist: <path>` | `1` |
| **Path is Directory** | `<csv-file-path>` points to a directory | `Input Error: Input path is a directory, not a file: <path>` | `1` |
| **Permission Denied** | `<csv-file-path>` cannot be read | `Input Error: Input CSV file cannot be read (permission denied): <path>` | `1` |
| **Corrupted CSV Rows** | Missing columns or malformed numbers | Prints `Warning: Skipping line <N> due to ...` to `System.err` and processes valid records. | `0` (if at least 1 valid row exists) |
| **Empty CSV / No Valid Data** | CSV file contains 0 valid data rows | `I/O Error: No valid sales records found in CSV file: <path>` | `1` |
| **File Write Failure** | Permission issues or invalid destination path | `I/O Error: <details>` | `1` |

---

## 6. Architecture & SOLID Principles Breakdown

The codebase is organized into decoupled layers: Ingestion, Analytics, Presentation, Output Export, and CLI Orchestration.

```
┌─────────────────────────────────────────────────────────────────┐
│                          SalesReporter                          │
│                   (Main Entry Point & Orchestrator)             │
└───────┬───────────────────┬─────────────────┬───────────────────┘
        │                   │                 │
        ▼                   ▼                 ▼
┌──────────────────┐ ┌──────────────┐ ┌───────────────────────────┐
│CLIArgumentParser │ │SalesAnalyzer │ │   SalesReportGenerator    │
│  & CLIArguments  │ │(Computations)│ │       (Formatting)        │
└──────────────────┘ └──────┬───────┘ └─────────────┬─────────────┘
                            │                       │
                            ▼                       ▼
                     ┌──────────────┐       ┌───────────────┐
                     │ SalesSummary │       │ReportExporter │ (Interface)
                     └──────────────┘       └───┬───────┬───┘
                                                │       │
                       ┌────────────────────────┘       └───────────────────────┐
                       ▼                                                        ▼
            ┌─────────────────────┐                                  ┌────────────────────┐
            │ConsoleReportExporter│                                  │ FileReportExporter │
            └─────────────────────┘                                  └────────────────────┘
```

### SOLID Principles Application

#### 1. Single Responsibility Principle (SRP)
Each class has a single, well-defined reason to change:
- `CLIArgumentParser`: Exclusively parses raw CLI parameters and enforces syntax and validation rules.
- `CLIArguments`: Pure data holder encapsulating validated arguments.
- `FileSalesReader`: Focuses purely on reading CSV streams and converting them to domain objects (`Product`).
- `SalesAnalyzer`: Encapsulates business metrics calculations without concerns for input sources or output presentation.
- `SalesReportGenerator`: Responsible only for converting summary calculations into formatted string reports.
- `ConsoleReportExporter` / `FileReportExporter`: Responsible solely for delivering content to their respective destinations.
- `SalesReporter`: Serves as the top-level workflow controller and exception handler.

#### 2. Open/Closed Principle (OCP)
The system is open for extension but closed for modification:
- **New Exporters**: Adding new report formats (e.g., `HtmlReportExporter`, `PdfReportExporter`, or `JsonReportExporter`) only requires implementing the `ReportExporter` interface. Neither `SalesAnalyzer` nor `SalesReportGenerator` needs modification.
- **New Ingestion Sources**: To ingest sales from a database or REST API, implement `SalesReader` without changing downstream business logic.

#### 3. Liskov Substitution Principle (LSP)
- Implementations of `ReportExporter` (`ConsoleReportExporter`, `FileReportExporter`) are completely interchangeable from the perspective of `SalesReporter`.
- The caller invokes `exporter.export(report)` uniformly without needing to know or depend on the underlying implementation details.

#### 4. Interface Segregation Principle (ISP)
- Rather than bloated monolithic interfaces, the system uses narrow, focused contracts:
  - `SalesReader`: Single method `List<Product> readSales(String source) throws IOException;`
  - `ReportExporter`: Single method `void export(String content) throws IOException;`
- Clients implement only the operations they genuinely perform.

#### 5. Dependency Inversion Principle (DIP)
- High-level orchestration in `SalesReporter` and `Main` depends on abstractions (`SalesReader`, `ReportExporter`), not concrete classes.
- Instantiations are localized via factory/orchestration patterns, decoupling core reporting workflows from direct I/O dependencies.

---

## 7. Team Responsibility Breakdown

| Role | Core Responsibilities | Key Deliverables |
| :--- | :--- | :--- |
| **Member 1** | Data ingestion, file reading, and domain models | - `Product.java`<br>- `SalesReader.java`<br>- `FileSalesReader.java`<br>- Initial CSV parsing |
| **Member 2** | Business computations, summary metrics, report formatting, and export strategy | - `SalesAnalyzer.java`<br>- `SalesSummary.java`<br>- `SalesReportGenerator.java`<br>- `ReportExporter.java`<br>- `ConsoleReportExporter.java`<br>- `FileReportExporter.java` |
| **Member 3** *(Assigned Scope)* | CLI argument handling, input validation, global exception handling, resilient CSV row processing, and comprehensive documentation | - `CLIArguments.java`<br>- `CLIArgumentParser.java`<br>- `InvalidInputException.java`<br>- `SalesReporter.java` (Main CLI controller)<br>- Resilient row-skipping in `FileSalesReader.java`<br>- Directory creation in `FileReportExporter.java`<br>- `Main.java` delegation<br>- `README.md` |
