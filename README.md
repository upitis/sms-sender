# sms


SMS_SENDER is Vaadin application that requires a Servlet container to run.

It uses Mobitech technologies m.MMSender server also as eqipment for sending SMS http://mobitechnologies.com/en/products/busapps/mmsender_brief.php

Dependencies:

1.	MySQL DB scphones_db (use scphones_db.Contacts.sql for creating)

2.	Mobitech m.MMSender


# Configuration

All basic parameters are located in **.env** file
You can run application in **Docker** environment by 
	
	> docker-compose up

Application will be available at URL-address as http://localhost:8080/sms-1.0/, for example. (sms-1.0 - the name of war-file in target dir)

Configuration of docker composer is in **composer.yml**

Application file in **.war** format distributed with sources for fast start and located in target folder. But you can go through workflow to compile your own.

In **db__init_data** directory there is initial data for database creation. It creates the database with name scphones_db  and the "Conacts" table. To fill the table by phones use any database tools:
- import **scphones_db.Contacts.sql** file for creating database structure
- fill by contacts and phone numbers the table
- export the pre-filled database to the db_init_data directory instead existing scphones_db.Contacts.sql (replace it)

Another way is expose db container port by editing composer.yml and fill cotacts on working application.


# Workflow

To compile the entire project, run "mvn install".

To run the application, run "mvn jetty:run" and open http://localhost:8080/ .

Debugging client side code
  - run "mvn vaadin:run-codeserver" on a separate console while the application is running
  - activate Super Dev Mode in the debug window of the application

To produce a deployable production mode WAR:
- change productionMode to true in the servlet class configuration (nested in the UI class)
- run "mvn clean package"
- test the war file with "mvn jetty:run-war"

## Developing a theme using the runtime compiler

When developing the theme, Vaadin can be configured to compile the SASS based
theme at runtime in the server. This way you can just modify the scss files in
your IDE and reload the browser to see changes.

To use the runtime compilation, open pom.xml and comment out the compile-theme 
goal from vaadin-maven-plugin configuration. To remove a possibly existing 
pre-compiled theme, run "mvn clean package" once.

When using the runtime compiler, running the application in the "run" mode 
(rather than in "debug" mode) can speed up consecutive theme compilations
significantly.

It is highly recommended to disable runtime compilation for production WAR files.

## Using Vaadin pre-releases

If Vaadin pre-releases are not enabled by default, use the Maven parameter
"-P vaadin-prerelease" or change the activation default value of the profile in pom.xml .
