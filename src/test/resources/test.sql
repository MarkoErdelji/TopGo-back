insert into vehicle_type(id,price_by_km,vehicle_name) values (1,100,0);



insert into drivers (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_blocked,user_type,is_active,vehicle_id) values (4,'test','ognjen34@gmail.com','Zoran','Budala','$2a$10$Wn4A5JzgizcMBttHv/MA7Omunwewqv5h6Wn1dbLmR9lp40bmxY0y6','test','',FALSE,1,TRUE,null);


insert into geo_location (id,address,latitude,longitude) values (1,'Kristijana Golubovica 5',45.237520,19.826419);
insert into vehicle (id,for_animals,for_babies,licence_plate,model,seat_number,location_id,driver_id,type_id) values  (1,TRUE,TRUE,'top-g','Golf GTI',4,1,4,1);
UPDATE drivers
SET vehicle_id = 1
WHERE id = 4;

insert into drivers (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_blocked,user_type,is_active,vehicle_id) values (5,'test','ognjen34@gmail.com','Kristijan','Bubasvaba','$2a$10$Wn4A5JzgizcMBttHv/MA7Omunwewqv5h6Wn1dbLmR9lp40bmxY0y6','test','',FALSE,1,TRUE,null);



INSERT INTO passenger (ID, ADDRESS, EMAIL, FIRST_NAME, IS_BLOCKED, LAST_NAME, PASSWORD, PHONE_NUMBER, PROFILE_PICTURE, USER_TYPE, IS_ACTIVE)
VALUES (21, '123 Main Street', 'passenger@example.com', 'Boris', false, 'Kesa', '$2a$10$Wn4A5JzgizcMBttHv/MA7Omunwewqv5h6Wn1dbLmR9lp40bmxY0y6', '123-456-7890', '', 0, TRUE);

INSERT INTO passenger (ID, ADDRESS, EMAIL, FIRST_NAME, IS_BLOCKED, LAST_NAME, PASSWORD, PHONE_NUMBER, PROFILE_PICTURE, USER_TYPE, IS_ACTIVE)
VALUES (22, '123 Main Street', 'passenger@example.com', 'Boris', false, 'Kesa', '$2a$10$Wn4A5JzgizcMBttHv/MA7Omunwewqv5h6Wn1dbLmR9lp40bmxY0y6', '123-456-7890', '', 0, TRUE);

INSERT INTO passenger (ID, ADDRESS, EMAIL, FIRST_NAME, IS_BLOCKED, LAST_NAME, PASSWORD, PHONE_NUMBER, PROFILE_PICTURE, USER_TYPE, IS_ACTIVE)
VALUES (23, '123 Main Street', 'passenger@example.com', 'Boris', false, 'Kesa', '$2a$10$Wn4A5JzgizcMBttHv/MA7Omunwewqv5h6Wn1dbLmR9lp40bmxY0y6', '123-456-7890', '', 0, TRUE);

INSERT INTO GEO_LOCATION (ID, ADDRESS, LATITUDE, LONGITUDE)
VALUES (4, '123 Main Street', 45.251636,19.814594),
       (5, '456 Market Street', 45.250367,19.833992);

INSERT INTO ROUTE (ID,LENGHT, FINISH_ID, START_ID)
VALUES (1, 24, 4,5);


INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (1, '2023-01-01T12:00:00', true, true, false, 1230.00, '2023-01-01T09:00:00', 0, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (2, '2023-01-01T18:00:00', true, true, false, 1433.00, '2023-01-01T13:00:00', 1, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (3, '2023-02-01T16:00:00', true, true, false, 1433.00, '2023-02-01T15:00:00', 2, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (4, '2023-01-03T18:00:00', true, true, false, 1433.00, '2023-01-03T17:00:00', 3, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (5, '2023-01-05T15:00:00', true, true, false, 1433.00, '2023-01-05T14:00:00', 4, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (6, '2023-01-06T15:00:00', true, true, false, 1433.00, '2023-01-06T14:00:00', 5, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (7, '2023-01-07T15:00:00', true, true, false, 1433.00, '2023-01-07T14:00:00', 6, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)

VALUES (8, '2023-01-01T15:00:00', true, true, false, 1433.00, '2023-01-01T14:00:00', 7, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (9, '2023-01-01T15:00:00', true, true, false, 1433.00, '2023-01-01T14:00:00', 3, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (10, '2023-01-01T15:00:00', true, true, false, 1433.00, '2023-01-01T14:00:00', 3, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (11, '2023-01-01T15:00:00', true, true, false, 1433.00, '2023-01-01T14:00:00', 1, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (12, '2023-01-01T15:00:00', true, true, false, 1433.00, '2023-01-01T14:00:00', 1, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (13, '2023-01-01T15:00:00', true, true, false, 1433.00, '2023-01-01T14:00:00', 0, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (14, '2023-01-01T15:00:00', true, true, false, 1433.00, '2023-01-01T14:00:00', 0, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (15, '2023-01-08T15:00:00', true, true, false, 1433.00, '2023-01-08T14:00:00', 7, 2, NULL, 4, NULL, 1);
INSERT INTO RIDE (ID, END_TIME, FOR_ANIMALS, FOR_BABIES, PANIC, PRICE, START_TIME, STATUS, VEHICLE_NAME, REJECTION_ID, DRIVER_ID, PAYMENT_ID, ROUTE_ID)
VALUES (16, '2023-01-09T15:00:00', true, true, false, 1433.00, '2023-01-09T14:00:00', 4, 2, NULL, 4, NULL, 1);



INSERT INTO PASSENGER_RIDES (PASSENGER_ID, RIDE_ID)
VALUES (21, 1),(21, 2),(21, 3),(21, 4),(21, 5),(21, 6),(21, 7),(21, 8), (23, 9), (23, 10), (23, 11), (23, 12), (23, 13), (23, 14);
