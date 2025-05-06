# Purchase Manager

A university project Java Swing application for managing purchases, customers, and inventory with MySQL as the backend database.

You can either view the screencast or follow the setup instructions and try the application yourself.

## Requirements
- Java 17+
- MySQL Server
- MySQL Connector/J (`mysql-connector-j-9.2.0.jar`, included)
- Optional: Database GUI (e.g., MySQL Workbench, phpMyAdmin)

## Setup Instructions

### 1. Import the Database

#### Option A: Terminal
1. Open a terminal.
2. Log in to MySQL:
   ```bash
   mysql -u root -p
   ```
3. Create the database:
   ```sql
   CREATE DATABASE purchase_manager;
   ```
4. Exit MySQL and import the dump:
   ```bash
   mysql -u root -p purchase_manager < purchase_manager.sql
   ```

#### Option B: GUI Tools
- **MySQL Workbench**:
  1. Connect to your MySQL server.
  2. Create a new schema named `purchase_manager`.
  3. Go to `File → Open SQL Script`, select `purchase_manager.sql`, and run it.
- **phpMyAdmin**:
  1. Log in to phpMyAdmin.
  2. Create a database named `purchase_manager`.
  3. Go to the `Import` tab and upload `purchase_manager.sql`.

### 2. Running the Application
Ensure `PurchaseManager.jar` and `mysql-connector-j-9.2.0.jar` are in the same folder.

#### Run from Terminal
- **Linux/macOS**:
  ```bash
  java -cp ".:mysql-connector-j-9.2.0.jar" -jar PurchaseManager.jar
  ```
- **Windows**:
  ```bash
  java -cp ".;mysql-connector-j-9.2.0.jar" -jar PurchaseManager.jar
  ```

### 3. Database Configuration
The application connects to:
- Host: `localhost`
- Port: `3306`
- Database: `purchase_manager`
- User: `root`
- Password: (your MySQL password)

If your database credentials differ, update the configuration in `DBConnection.java` (see below).

## Files Included
- `PurchaseManager.jar`: Compiled Java Swing application
- `mysql-connector-j-9.2.0.jar`: MySQL JDBC driver
- `purchase_manager.sql`: Database dump

## Modifying Database Configuration
The database connection is configured in `DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/purchase_manager";
private static final String USER = "root";
private static final String ENCRYPTED_PASSWORD = "SjI0blZlQWNYIQ=="; // Base64 for 'Z24VeAcX!'
```

To update:
1. Open `DBConnection.java` in an IDE or text editor.
2. Modify the `URL`, `USER`, or `ENCRYPTED_PASSWORD` as needed:
   ```java
   private static final String URL = "jdbc:mysql://your-host:your-port/your-database";
   private static final String USER = "your-username";
   private static final String ENCRYPTED_PASSWORD = "base64-encoded-password";
   ```
3. Optionally, encode your password in Base64:
   - Use [base64encode.org](https://www.base64encode.org/)
   - Or from the terminal:
     ```bash
     echo -n "your-password" | base64
     ```

**Note**: Ensure the password matches your MySQL user configuration.

## Recompiling the Application
If you modify `DBConnection.java`, recompile the application:

1. Place all `.java` files and `mysql-connector-j-9.2.0.jar` in a project folder (e.g., `PurchaseManager/`).
2. Open a terminal in the folder.
3. Compile:
   - **Linux/macOS**:
     ```bash
     javac -cp ".:mysql-connector-j-9.2.0.jar" *.java
     ```
   - **Windows**:
     ```bash
     javac -cp ".;mysql-connector-j-9.2.0.jar" *.java
     ```
4. Create a new JAR:
   ```bash
   jar cfe PurchaseManager.jar MainClassName *.class
   ```
   Replace `MainClassName` with the class containing `public static void main(String[] args)`.
5. Run the updated application:
   ```bash
   java -cp ".:mysql-connector-j-9.2.0.jar" -jar PurchaseManager.jar
   ```
