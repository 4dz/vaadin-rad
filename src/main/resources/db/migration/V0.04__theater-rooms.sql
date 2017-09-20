create table theater_room (
  id BIGINT auto_increment primary key not null,
  theater_id BIGINT not null,
  room_name varchar(50) not null,

  FOREIGN KEY (theater_id) REFERENCES theater(id)
);

insert into theater_room values (1, 1, 'Room 1');