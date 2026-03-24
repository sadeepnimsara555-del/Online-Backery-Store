Online Baker Store System
Project Overview

The Online Baker Store System is a simple web-based application designed to simulate a restaurant cashiering and ordering system. It allows users to browse products, create orders, and manage a cart efficiently.

This project is developed using HTML and CSS for the frontend and Java with Spring Boot for the backend. It incorporates the Bubble Sort algorithm to organize and display order data and follows Object-Oriented Programming (OOP) principles to ensure a clean and maintainable structure.

Features

User authentication (Sign Up / Login)

Product browsing

Add-to-cart functionality

Order creation and management

Simple form validation

Sorting of orders using the Bubble Sort algorithm

Implementation of Object-Oriented Programming concepts

Object-Oriented Programming Concepts

The system is developed using core OOP principles:

Encapsulation: Data and methods are grouped within classes such as User, Product, and Order.

Abstraction: Business logic is handled in service classes, hiding implementation details.

Inheritance: Common properties are shared across related classes.

Polymorphism: Methods can be overridden or overloaded for flexible behavior.

System Architecture

The system follows a layered architecture:

Presentation Layer: HTML and CSS interface

Application Layer: Spring Boot controllers and services

Business Logic Layer: Handles order processing and sorting

Data Layer: Basic or in-memory data storage

Sorting Mechanism

The Bubble Sort algorithm is used to organize order data before displaying it. This demonstrates the application of a basic algorithm in a real-world system.

Getting Started
Prerequisites

Java (JDK 8 or higher)

IntelliJ IDEA

Maven or Gradle

Web browser

Running the Project in IntelliJ IDEA

Open IntelliJ IDEA.

Click on Open and select the project folder.

Wait for IntelliJ IDEA to load dependencies (Maven/Gradle will automatically import).

Locate the main class (usually annotated with @SpringBootApplication).

Right-click the main class and select Run 'ApplicationName'.

Once the server starts, open your browser and go to:

http://localhost:8080/dashboard
Alternative Run Method (Command Line)

Clone the repository:

git clone <your-repo-link>

Navigate to the project directory:

cd online-baker-store

Run the application:

mvn spring-boot:run
Usage Instructions

When the application starts, users must sign up or log in. After authentication, users can browse products, add items to the cart, and create and manage orders. The system includes basic validation to ensure proper data entry.

System Access

Access the system locally via:
http://localhost:8080/dashboard

Purpose of the Project

This project is intended for learning web development, understanding Spring Boot architecture, applying Object-Oriented Programming concepts, and demonstrating sorting algorithms in a practical environment.

Future Improvements

Integration with a database (MySQL/PostgreSQL)

Implementation of advanced sorting algorithms

Payment gateway integration

Improved user interface and experience

Author

Developed as a sample academic or project system for educational purposes.
