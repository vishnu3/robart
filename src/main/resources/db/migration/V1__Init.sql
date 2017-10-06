
CREATE TABLE Robots (
	name VARCHAR PRIMARY KEY,
	hardware_version VARCHAR
);

CREATE SEQUENCE Seq_Firmwares;
CREATE TABLE Firmwares (
	name VARCHAR PRIMARY KEY,
	 -- For simplicity: Base64 encoded binary data
	 -- However never mind the data and its contents at all
	data VARCHAR
);

INSERT INTO Robots (name, hardware_version) VALUES ('robot_1', 'hardware-model-abc');
INSERT INTO Robots (name, hardware_version) VALUES ('robot_2', 'hardware-model-abc');
INSERT INTO Robots (name, hardware_version) VALUES ('robot_3', 'hardware-model-abc');
INSERT INTO Robots (name, hardware_version) VALUES ('robot_4', 'hardware-model-def');
INSERT INTO Robots (name, hardware_version) VALUES ('robot_5', 'hardware-model-def');
INSERT INTO Robots (name, hardware_version) VALUES ('robot_6', 'hardware-model-def');
INSERT INTO Robots (name, hardware_version) VALUES ('robot_7', 'hardware-model-ghi');
INSERT INTO Robots (name, hardware_version) VALUES ('robot_8', 'hardware-model-ghi');
INSERT INTO Robots (name, hardware_version) VALUES ('robot_9', 'hardware-model-ghi');
INSERT INTO Robots (name, hardware_version) VALUES ('robot_10', 'hardware-model-xyz');

INSERT INTO Firmwares (name, data) VALUES ('firmware_stock_A', 'R29vZCB5b3Uga25vdyBob3cgdG8gZGVjb2RlIGJhc2UgNjQgOi0p');
INSERT INTO Firmwares (name, data) VALUES ('firmware_upgrade_A_1', 'WWV0IGFnYWluIGEgYmFzZSA2NCBlbmNvZGVkIHRleHQu');