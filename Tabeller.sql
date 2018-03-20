SET foreign_key_checks = 0;
DROP TABLE IF EXISTS DockingStation, Dock, Bicycle, bicycleStatus, Model, Repair, Customer, PaymentCard, TripPayment, Employee;
SET foreign_key_checks = 1;

CREATE TABLE DockingStation (
station_id INT,
name VARCHAR(100),
active_status TINYINT(1),
capacity INT,
PRIMARY KEY(station_id)
);

CREATE TABLE Dock (
dock_id INT,
station_id INT,
isAvailable TINYINT(1),
PRIMARY KEY(dock_id)
);

CREATE TABLE Bicycle (
bicycle_id INT,
dock_id INT,
powerlevel INT,
make VARCHAR(20),
model VARCHAR(20),
production_date DATE,
bicycleStatus VARCHAR(20),
totalKM INT,
trips INT,
nr_of_repairs INT,
PRIMARY KEY(bicycle_id)
);

CREATE TABLE bicycleStatus (
bicycleStatus VARCHAR(20),
PRIMARY KEY(bicycleStatus)
);

CREATE TABLE Repair (
repair_id INT,
description_before VARCHAR(200),
date_sent DATE,
date_received DATE,
repair_cost INT,
repair_description_after VARCHAR(200),
employee_id INT,
bicycle_id INT,
PRIMARY KEY(repair_id)
);

CREATE TABLE Model (
model VARCHAR(20),
price INT,
PRIMARY KEY(model)
);

CREATE TABLE Customer (
cust_id INT,
cardNumber INT,
first_name VARCHAR(100),
last_name VARCHAR(50),
phone INT,
email VARCHAR(50),
password VARCHAR(20),
PRIMARY KEY(cust_id)
);

CREATE TABLE PaymentCard (
cardNumber INT,
cust_id INT,
balance INT,
active_status TINYINT(1),
PRIMARY KEY(cardNumber)
);

CREATE TABLE Employee (
employee_id INT,
password VARCHAR(20),
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
trip_id INT,
cust_id INT,
bicycle_id INT,
time_received TIME,
time_delivered TIME,
station_id_received INT,
station_id_delivered INT,
tripKM INT,
sumPayment INT,
PRIMARY KEY(trip_id)
);

ALTER TABLE Dock
ADD FOREIGN KEY (station_id) REFERENCES DockingStation(station_id);

ALTER TABLE Bicycle
ADD FOREIGN KEY (dock_id) REFERENCES Dock(dock_id),
ADD FOREIGN KEY (model) REFERENCES Model(model),
ADD FOREIGN KEY (bicycleStatus) REFERENCES bicycleStatus(bicycleStatus);

ALTER TABLE Repair
ADD FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
ADD FOREIGN KEY (bicycle_id) REFERENCES Bicycle(bicycle_id);

ALTER TABLE Customer
ADD FOREIGN KEY (cardNumber) REFERENCES PaymentCard(cardNumber);

ALTER TABLE PaymentCard
ADD FOREIGN KEY (cust_id) REFERENCES Customer(cust_id);

ALTER TABLE TripPayment
ADD FOREIGN KEY (cust_id) REFERENCES Customer(cust_id),
ADD FOREIGN KEY (bicycle_id) REFERENCES Bicycle(bicycle_id),
ADD FOREIGN KEY (station_id_received) REFERENCES DockingStation(station_id),
ADD FOREIGN KEY (station_id_delivered) REFERENCES DockingStation(station_id);

INSERT INTO bicycleStatus VALUES('in dock'), ('DBR'), ('lost'), ('need repair'), ('not in dock'), ('not employed');

INSERT INTO Model VALUES('family', 100), ('cargo', 150), ('regular', 100) ;

INSERT INTO DockingStation VALUES  (2, 'Munkegata', 1, 20);

INSERT INTO DockingStation VALUES (1, 'Prinsen', 0, 20);
INSERT INTO DockingStation VALUES (3, 'HJEM', 3, 20);

SELECT * FROM DockingStation;

