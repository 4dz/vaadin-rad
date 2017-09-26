create table cinema_booking (
  id BIGINT auto_increment primary key not null,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  showing_date timestamp not null,
  number_seats int not null,
  movie_id BIGINT not null,

  FOREIGN KEY (movie_id) REFERENCES movie(id)
);