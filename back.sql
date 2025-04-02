create database backend_project;
use backend_project;

CREATE TABLE pincode_distances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    origin_pincode VARCHAR(6) NOT NULL,
    destination_pincode VARCHAR(6) NOT NULL,
    distance DECIMAL(10, 2),
    duration VARCHAR(50),
    route_mode VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
SHOW TABLES;
INSERT INTO pincode_distance (origin_pincode, destination_pincode, distance, duration, route_mode)
VALUES ('141106', '110060', 10.5, '1 hour', 'DRIVE');
SELECT * FROM pincode_distance;

SELECT * FROM pincode_distances WHERE origin_pincode = '847307' AND destination_pincode = '560064';
