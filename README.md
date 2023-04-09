# JWSAS
## Web Security Analysis System Written In Java Using Spring Boot Framework

### Specifics:
This project is a web application built using the Spring Boot framework. The application includes a frontend component built using HTML, CSS, and JavaScript, and a backend component built using Java.

The frontend component includes a number of HTML files that are served to the client by the server, along with associated CSS and JavaScript files. The HTML files contain dynamic content that is populated by the server at runtime, including the base URL of the application, which is required by the JavaScript code to make HTTP requests.

The backend component includes a number of Java classes that handle requests and responses, including a controller class that maps HTTP requests to specific methods, and a service class that contains the business logic for the application. The backend component also includes code for making HTTP requests to external services using the HttpClient library.

The application is configured using a number of configuration files, including the application.properties file, which contains configuration settings for the Spring Boot framework, and the pom.xml file, which contains dependencies for the application.

Overall, the project is a web application that includes a frontend component for displaying dynamic content to users, and a backend component for handling requests and responses, making HTTP requests to external services, and implementing the business logic for the application.


### This app is using the Apache JMeter API, so you need to install Apache JMeter software on your hardware manually. 


#### Instructions for Apache JMeter installation:


- To install Apache JMeter on your hardware go by this link:
  https://jmeter.apache.org/download_jmeter.cgi

  You also can go for 5.5 version zipped package:
  https://dlcdn.apache.org//jmeter/binaries/apache-jmeter-5.5.zip
- When you downloaded the package unzip the directory and place it where you want to

Alternatively you can visit the official site of Apache JMeter by this link:
https://jmeter.apache.org/

If you have some problems with setting up the environment variables check this link:
https://jmeter.apache.org/usermanual/get-started.html#install 

###### Also, you will need to set this env. variable 'XML_LOAD_TEST_RESULTS_DIRECTORY' for the summary of load tests
It is the path to store all the '.xml' files as reports