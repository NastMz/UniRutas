CREATE TABLE Student
(
  code VARCHAR(10) NOT NULL,
  name VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(10) NULL,
  security_phrase VARCHAR(255) NULL,
  PRIMARY KEY (code)
);

CREATE TABLE Administrative
(
  code VARCHAR(10) NOT NULL,
  name VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(10) NULL,
  security_phrase VARCHAR(255) NULL,
  PRIMARY KEY (code)
);

CREATE TABLE Direction
(
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Journey
(
  id VARCHAR(255) NOT NULL,
  direction_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (direction_id) REFERENCES Direction(id)
);

CREATE TABLE Route
(
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  journey_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (journey_id) REFERENCES Journey(id)
);

CREATE TABLE Section
(
  id VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Coordinate
(
  id VARCHAR(255) NOT NULL,
  latitude DECIMAL(9, 6) NOT NULL,
  longitude DECIMAL(9, 6) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Stop
(
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  previous_section VARCHAR(255) NOT NULL,
  next_section VARCHAR(255) NOT NULL,
  coordinate_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (coordinate_id) REFERENCES Coordinate(id)
);

CREATE TABLE Driver
(
  document_number VARCHAR(10) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (document_number)
);

CREATE TABLE Bus
(
  id VARCHAR(255) NOT NULL,
  plate_number VARCHAR(10) NOT NULL,
  capacity INT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Schedule
(
  id VARCHAR(255) NOT NULL,
  hour TIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE DriverBus
(
  document_number VARCHAR(10) NOT NULL,
  bus_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (document_number, bus_id),
  FOREIGN KEY (document_number) REFERENCES Driver(document_number),
  FOREIGN KEY (bus_id) REFERENCES Bus(id)
);

CREATE TABLE JourneySection
(
  journey_id VARCHAR(255) NOT NULL,
  section_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (journey_id, section_id),
  FOREIGN KEY (journey_id) REFERENCES Journey(id),
  FOREIGN KEY (section_id) REFERENCES Section(id)
);

CREATE TABLE SectionCoordinate
(
  section_id VARCHAR(255) NOT NULL,
  coordinate_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (section_id, coordinate_id),
  FOREIGN KEY (section_id) REFERENCES Section(id),
  FOREIGN KEY (coordinate_id) REFERENCES Coordinate(id)
);

CREATE TABLE JourneyStop
(
  journey_id VARCHAR(255) NOT NULL,
  stop_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (journey_id, stop_id),
  FOREIGN KEY (journey_id) REFERENCES Journey(id),
  FOREIGN KEY (stop_id) REFERENCES Stop(id)
);

CREATE TABLE Service
(
  id VARCHAR(255) NOT NULL,
  route_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (route_id) REFERENCES Route(id)
);

CREATE TABLE ServiceBus
(
  service_id VARCHAR(255) NOT NULL,
  bus_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (service_id, bus_id),
  FOREIGN KEY (service_id) REFERENCES Service(id),
  FOREIGN KEY (bus_id) REFERENCES Bus(id)
);

CREATE TABLE ServiceSchedule
(
  service_id VARCHAR(255) NOT NULL,
  schedule_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (service_id, schedule_id),
  FOREIGN KEY (service_id) REFERENCES Service(id),
  FOREIGN KEY (schedule_id) REFERENCES Schedule(id)
);

CREATE TABLE Alert
(
  id VARCHAR(255) NOT NULL,
  date DATE NOT NULL,
  description VARCHAR(255) NOT NULL,
  image VARCHAR(255) NOT NULL,
  service_id VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (service_id) REFERENCES Service(id)
);

CREATE TABLE ServiceAlert
(
  service_id VARCHAR(255) NOT NULL,
  alert_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (service_id, alert_id),
  FOREIGN KEY (service_id) REFERENCES Service(id),
  FOREIGN KEY (alert_id) REFERENCES Alert(id)
);

CREATE TABLE StudentAlert
(
  is_read BOOLEAN NOT NULL,
  student_code VARCHAR(10) NOT NULL,
  alert_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (student_code, alert_id),
  FOREIGN KEY (student_code) REFERENCES Student(code),
  FOREIGN KEY (alert_id) REFERENCES Alert(id)
);

CREATE TABLE AdministrativeAlert
(
  administrative_code VARCHAR(10) NOT NULL,
  alert_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (administrative_code, alert_id),
  FOREIGN KEY (administrative_code) REFERENCES Administrative(code),
  FOREIGN KEY (alert_id) REFERENCES Alert(id)
);

CREATE TABLE StudentSubscription
(
  student_code VARCHAR(10) NOT NULL,
  service_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (student_code, service_id),
  FOREIGN KEY (student_code) REFERENCES Student(code),
  FOREIGN KEY (service_id) REFERENCES Service(id)
);