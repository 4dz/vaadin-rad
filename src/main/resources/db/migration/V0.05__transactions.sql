create table transaction (
  id BIGINT auto_increment primary key not null,
  time timestamp not null,
  theater_room_id BIGINT not null,
  seats integer not null,
  price double not null,
  movie_id BIGINT not null,

  FOREIGN KEY (theater_room_id) REFERENCES theater_room(id),
  FOREIGN KEY (movie_id) REFERENCES movie(id)
);

insert into transaction values (1, date '2015-05-29', 1, 3, 10.5,1);