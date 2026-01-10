# Software Projects

A collection of Java-based software applications featuring desktop applications with graphical user interfaces using Swing.

## Projects

### Java Purchase Manager

A desktop application for managing purchase orders, inventory, and vendor relationships with a complete OOP design pattern.

**Tech Stack:**
- Language: Java
- - GUI Framework: Java Swing
- - Database: MySQL with JDBC
- - Design Patterns: MVC (Model-View-Controller), DAO (Data Access Object)
     
- **Features:**
- - Purchase order creation and management
- - Vendor management and tracking
- - Inventory management
- - Order history and reporting
- - User authentication
- - Data validation and error handling
- - Search and filter functionality
                   
- **Key Components:**
- ```
Java_Purchase_Manager/
├── src/
│   ├── models/          # Data models (Order, Vendor, Inventory)
│   ├── views/           # Swing UI components
│   ├── controllers/      # Business logic
│   ├── dao/             # Database access layer
│   ├── database/        # Database connection utilities
│   └── utils/           # Helper functions
├── resources/           # Images and assets
├── database/
│   └── schema.sql       # Database structure
└── build/               # Compiled classes
```

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- - MySQL 5.7 or higher
- - IDE (IntelliJ IDEA, Eclipse, or NetBeans)
- - JDBC MySQL driver
                           
- ### Installation
                           
- 1. **Clone the repository**
2. ```bash
git clone https://github.com/LashaJaparidze15/Software_Projects.git
cd Software_Projects/Java_Purchase_Manager
```

2. **Database Setup**
3. - Create a new MySQL database
- - Import `database/schema.sql`
- - Update database credentials in `src/database/DBConnection.java`
                                       
- 3. **Configuration**
4. ```java
// src/database/DBConnection.java
String url = "jdbc:mysql://localhost:3306/purchase_manager";
String username = "root";
String password = "your_password";
```

4. **Compile and Run**
5. ```bash
javac -d bin src/**/*.java
java -cp bin:lib/mysql-connector.jar models.MainApplication
```

## Usage

### For Managers
- Create and manage purchase orders
- - Track vendor performance
- - Monitor inventory levels
- - Generate purchasing reports
- - Manage user accounts
                                                       
- ### For Clerks
- - Process purchase orders
- - Update inventory records
- - Search order history
- - View vendor information
                                                               
- ## Architecture
                                                               
- The application follows the Model-View-Controller (MVC) pattern:
                                                               
- - **Model**: Represents business entities (Order, Vendor, Product)
- - **View**: Swing components for user interface
- - **Controller**: Handles user interactions and business logic
- - **DAO**: Manages database operations
                                                                       
- ## Contributing
                                                                       
- 1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push to branch (`git push origin feature/your-feature`)
5. Open a Pull Request
                                                                                     
## License
                                                                                     
This project is licensed under the MIT License.
                                                                                     
## Author
                                                                                     
Created by Lasha Japaridze
