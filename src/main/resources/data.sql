insert into users (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_active,is_blocked) values  (1,'test','test','test','test','test','test','test',FALSE,FALSE);
insert into users (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_active,is_blocked) values  (2,'test','test','test','test','test','test','test',FALSE,FALSE);
insert into users (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_active,is_blocked) values  (3,'test','test','test','test','test','test','test',FALSE,FALSE);
insert into users (id,address,email,first_name,last_name,password,phone_number,profile_picture,is_active,is_blocked) values  (4,'test','test','test','test','test','test','test',FALSE,FALSE);

alter table if exists messages add constraint sender_id_fk foreign key (sender_id) references users;
alter table if exists messages add constraint receiver_id_fk foreign key (receiver_id) references users;

insert into messages (id,message,ride_id,time_of_sending,type,receiver_id,sender_id) values(1,'cao',null,'2022-11-25T17:32:28Z',0,1,2);
insert into messages (id,message,ride_id,time_of_sending,type,receiver_id,sender_id) values(2,'cao',null,'2022-11-25T17:32:28Z',0,1,3);
insert into messages (id,message,ride_id,time_of_sending,type,receiver_id,sender_id) values(3,'cao',null,'2022-11-25T17:32:28Z',0,3,2);
insert into messages (id,message,ride_id,time_of_sending,type,receiver_id,sender_id) values(4,'cao',null,'2022-11-25T17:32:28Z',0,4,2);