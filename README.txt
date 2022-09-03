Application Title: Java Meet
Application Version: v1.1
Date: August 2, 2022
Author: Connor Engstrom
Student ID: 007473978
Contact: connor.e.music@gmail.com
IDE: IntelliJ IDEA 2022.1.3 (Community Edition)
JDK Version: 17.0.3.1
JavaFX Version: 17.0.0.1
JDBC Version: mysql-connector-java-8.0.26

Purpose:
-------
This desktop application can be used to schedule and manage appointments across different time zones for an international company.

Some skills demonstrated in the creation of this application include:
-Advanced Java programming concepts
-Using the JDBC to connect the application to a MySQL database
-Manipulation of data with SQL while considering data validation and primary and foreign key constraints
-Localization through the use of resource bundles
-Generating reports based on the data found in the database
-Support across all time zones
-Use of lambda expressions to improve code (found in the ScheduleScreenController and ContactScreenController)
-Use of JavaFX to build a complete User Interface

Directions to Run:
----------
This program should be run via the main method using Java 17.  No other JDKs have been tested.

The user will be prompted to login when the program starts.  If the login credentials match those found in the database, the user will be redirected to the main scheduling screen of the application.

Additional Report:
-----------------
In my version of this program, I decided to include an additional report that simply shows total customers by country and by state/province.  This would be highly useful in a real-world scenario, as companies are often interested in understanding their customer base more, and geographical location brings a lot of understanding to what a company's customer base is, as well as how they can expand it.
