SET foreign_key_checks = 0;
DROP TABLE IF EXISTS DockingStation, Dock, Bicycle, bicycleStatus, Model, Repair, Customer, PaymentCard, TripPayment, Employee;
SET foreign_key_checks = 1;

CREATE TABLE DockingStation (
station_id INT AUTO_INCREMENT,
name VARCHAR(100) NOT NULL,
active_status BOOLEAN,
capacity INT,
longitude DOUBLE,
latitude DOUBLE,
powerUsage DOUBLE,
PRIMARY KEY(station_id)
);

CREATE TABLE Dock (
dock_id INT AUTO_INCREMENT,
station_id INT,
isAvailable BOOLEAN,
PRIMARY KEY(dock_id)
);

CREATE TABLE Bicycle (
bicycle_id INT AUTO_INCREMENT,
dock_id INT,
powerlevel INT,
make VARCHAR(20),
model VARCHAR(20),
registration_date DATE,
bicycleStatus VARCHAR(20),
totalKM DOUBLE,
trips INT,
nr_of_repairs INT,
price_of_bike DOUBLE,
longitude DOUBLE,
latitude DOUBLE,
PRIMARY KEY(bicycle_id)
);

CREATE TABLE bicycleStatus (
bicycleStatus VARCHAR(20),
PRIMARY KEY(bicycleStatus)
);

CREATE TABLE Repair (
repair_id INT AUTO_INCREMENT,
description_before TEXT,
date_sent DATE,
date_received DATE,
repair_cost DOUBLE,
repair_description_after TEXT,
employee_id INT,
bicycle_id INT,
PRIMARY KEY(repair_id)
);

CREATE TABLE Model (
model VARCHAR(20),
price DOUBLE,
PRIMARY KEY(model)
);

CREATE TABLE Customer (
cust_id INT,
cardNumber INT,
first_name VARCHAR(100),
last_name VARCHAR(50),
phone INT,
email VARCHAR(50),
password TEXT,
PRIMARY KEY(cust_id)
);

CREATE TABLE PaymentCard (
cardNumber INT,
cust_id INT,
balance DOUBLE,
active_status TINYINT(1),
PRIMARY KEY(cardNumber)
);

CREATE TABLE Employee (
employee_id INT,
password TEXT,
email VARCHAR(50),
first_name VARCHAR(50),
last_name VARCHAR(50),
address VARCHAR(50),
phone INT,
isHired TINYINT(1),
isAdmin TINYINT(1),
PRIMARY KEY(employee_id)
);

CREATE TABLE TripPayment (
trip_id INT AUTO_INCREMENT,
cust_id INT,
bicycle_id INT,
time_received TIMESTAMP,
time_delivered TIMESTAMP,
station_id_received INT,
station_id_delivered INT,
tripKM DOUBLE,
sumPayment DOUBLE,
PRIMARY KEY(trip_id)
);

ALTER TABLE Dock
ADD FOREIGN KEY (station_id) REFERENCES DockingStation(station_id);

ALTER TABLE Bicycle
ADD UNIQUE (dock_id),
ADD FOREIGN KEY (dock_id) REFERENCES Dock(dock_id),
ADD FOREIGN KEY (model) REFERENCES Model(model),
ADD FOREIGN KEY (bicycleStatus) REFERENCES bicycleStatus(bicycleStatus);

ALTER TABLE Repair
ADD FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
ADD FOREIGN KEY (bicycle_id) REFERENCES Bicycle(bicycle_id);

ALTER TABLE Customer
ADD UNIQUE (email),
ADD FOREIGN KEY (cardNumber) REFERENCES PaymentCard(cardNumber);

ALTER TABLE Employee
ADD UNIQUE (email);

ALTER TABLE PaymentCard
ADD FOREIGN KEY (cust_id) REFERENCES Customer(cust_id);

ALTER TABLE TripPayment
ADD FOREIGN KEY (cust_id) REFERENCES Customer(cust_id),
ADD FOREIGN KEY (bicycle_id) REFERENCES Bicycle(bicycle_id),
ADD FOREIGN KEY (station_id_received) REFERENCES DockingStation(station_id),
ADD FOREIGN KEY (station_id_delivered) REFERENCES DockingStation(station_id);

INSERT INTO bicycleStatus VALUES('in dock'), ('DBR'), ('lost'), ('need repair'), ('not in dock'), ('in storage');

INSERT INTO Model VALUES('family', 100.00), ('cargo', 150.00), ('regular', 100.00) ;



