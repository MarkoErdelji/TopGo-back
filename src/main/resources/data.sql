insert into users (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_blocked,user_type) values  (1,'test','test1','test','test','098f6bcd4621d373cade4e832627b4f6','test','test',FALSE,0);
insert into users (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_blocked,user_type) values  (2,'test','test2','test','test','098f6bcd4621d373cade4e832627b4f6','test','test',FALSE,1);
insert into users (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_blocked,user_type) values  (3,'test','test3','test','test','098f6bcd4621d373cade4e832627b4f6','test','test',FALSE,2);
insert into drivers (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_blocked,user_type,is_active,vehicle_id) values (4,'test','test4','test','test','098f6bcd4621d373cade4e832627b4f6','test','test',FALSE,1,TRUE,null);
insert into geo_location (id,address,latitude,longitude) values (1,'Kristijana Golubovica 5',45.237520,19.826419);
insert into vehicle_type(id,price_by_km,vehicle_name) values (1,100,0);
insert into vehicle (id,for_animals,for_babies,licence_plate,model,seat_number,location_id,driver_id,type_id) values  (1,TRUE,TRUE,'top-g','Golf GTI',4,1,4,1);
UPDATE drivers
SET vehicle_id = 1
WHERE id = 4;

insert into drivers (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_blocked,user_type,is_active,vehicle_id) values (5,'Tunguzija','kris@buba.com','Kristijan','Bubasvaba','098f6bcd4621d373cade4e832627b4f6','test','test',FALSE,1,TRUE,null);
insert into geo_location (id,address,latitude,longitude) values (2,'Dejana Matica 5',45.246893,19.840011);
insert into vehicle_type(id,price_by_km,vehicle_name) values (2,200,1);
insert into vehicle (id,for_animals,for_babies,licence_plate,model,seat_number,location_id,driver_id,type_id) values  (2,TRUE,TRUE,'top-g','Lamburdzini',4,2,5,2);
UPDATE drivers
SET vehicle_id = 2
WHERE id = 5;

insert into drivers (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_blocked,user_type,is_active,vehicle_id) values (6,'Donja Dobrinja','deki@gmail.com','Dejan','Kanta','098f6bcd4621d373cade4e832627b4f6','test','test',FALSE,1,TRUE,null);
insert into geo_location (id,address,latitude,longitude) values (3,'Tita 5',45.248113,19.816799);
insert into vehicle_type(id,price_by_km,vehicle_name) values (3,300,2);
insert into vehicle (id,for_animals,for_babies,licence_plate,model,seat_number,location_id,driver_id,type_id) values  (3,FALSE,FALSE,'100%','Mecka',2,3,6,3);
UPDATE drivers
SET vehicle_id = 3
WHERE id = 6;


-- alter table if exists messages add constraint sender_id_fk foreign key (sender_id) references users;
-- alter table if exists messages add constraint receiver_id_fk foreign key (receiver_id) references users;
--
-- insert into messages (id,message,ride_id,time_of_sending,type,receiver_id,sender_id) values(1,'cao',null,'2022-11-25T17:32:28Z',0,1,2);
-- insert into messages (id,message,ride_id,time_of_sending,type,receiver_id,sender_id) values(2,'cao',null,'2022-11-25T17:32:28Z',0,1,3);
-- insert into messages (id,message,ride_id,time_of_sending,type,receiver_id,sender_id) values(3,'cao',null,'2022-11-25T17:32:28Z',0,3,2);
-- insert into messages (id,message,ride_id,time_of_sending,type,receiver_id,sender_id) values(4,'cao',null,'2022-11-25T17:32:28Z',0,4,2);