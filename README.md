# Digitalwallet
Digital wallet that customers (or employees) can create wallet, deposit and withdraw-payment money.

Java 17 and Spring Boot 3.4.5 used at the development phase.

After maven build you can run the following command to run the application; <br />
java -jar digitalwallet-0.0.1-SNAPSHOT.jar

The following data were inserted as initial data. Additionally, new customers can be created via /customer/createCustomer endpoint.<br />

INSERT INTO customer (name, surname, tckn, role) VALUES ('Ayşe', 'Çimen', '111', 'EMPLOYEE');<br />
INSERT INTO customer (name, surname, tckn, role) VALUES ('Ahmet', 'Toprak', '222', 'CUSTOMER');<br />
INSERT INTO customer (name, surname, tckn, role) VALUES ('Fatma', 'Güneş', '333', 'CUSTOMER');<br />



