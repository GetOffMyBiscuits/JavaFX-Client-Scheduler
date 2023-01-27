README: CLIENT SCHEDULER

Application Name: 	Client Scheduler
Version: 		        0.1
Author: 		        Joshua Hillary
Date: 			        10-21-2022
Email: 			        j.andrew.hillary@gmail.com


IDE: 			        IntelliJ IDEA 2022.1.3
Java Version: 		18.0.1
JavaFX SDK: 		  18
JDBC Driver: 		  mysql-connector-java-8.0.22

PURPOSE
The purpose of the Client Scheduler is to perform appointment scheduling by creating, updating, and deleting Appointment and Customer records from a local MySQL database in a simple graphical user interface. It also includes a login page that verifies users based on the stored user records in the database.  There is also a reports page that displays appointments for each contact, as well as total appointments by month and type, and total customers by state.  

HOW TO RUN
The application can be run with IntelliJ IDE with similar specs as listed above. Install the connector module and JavaFX.
When using the application, a user can login with a username and password. The user's timezone is displayed in the bottom left corner of the login screen. 
Once inside the application, You can view your Appointments and Customers on the home screen. Both can be added to, updated, or deleted based on the user's needs. The appointments table is accompanied by three buttons to filter the records by date based on week, month, or all time. There is also a reports tab where you can choose between all contacts to view their three pre-built reports.  

To ensure that the time zones are operating accurately, ensure that the MySQL database has the correct timezone set.
The way my system works, it is important that the database timezone is set to UTC time.

INSTRUCTIONS
To set the timezone, run these two queries in the database from workbench:
SET GLOBAL time_zone = '+00:00'; --to set the timezone to UTC
SELECT @@global.time_zone; --to ensure that it's set to UTC and not System Default, or some other timezone.

ADDITIONAL REPORT
The additional report consists of the total customers based on contacts and the associated state. 

