# Trendyol Link Converter Backend Application

Trendyol Link Conversion Backend Application implementations

# Getting Started

Clone repository to your local:
git clone 
```

https://github.com/DevelopmentHiring/silacinar.git

```

# Prerequisities

To use this project, you are going to need:

* Java JDK 11
* Maven (3.6.3) compatible with JDK 11 - Dependency Management
* Any Java IDE
* PostgreSQL 13.0.1
* Docker

# Installing:

You will need Docker in order  to run PostgreSQL.
* Run "docker-compose -f docker-compose.yml up" command, it will run docker-compose.yml file under the ./
     
     Required tables "product" and "link_collection" are created since the command executes queries which is in Init/Init.sql file.
* Execute Maven build to create the \target\testcasews-0.0.1-SNAPSHOT.war file.
     
     mvn clean install
* Run the application
      
     mvn spring-boot:run
   
Once the application is running, Api for endpoints will be available under ``` http://localhost:8081/swagger-ui.html. ```

# Running Tests

Run the tests using
  
  mvn test



