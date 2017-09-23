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

insert into transaction (time,theater_room_id,movie_id,seats,price) values
  (timestamp '2015-05-29 10:30:00', 1, 1, 3, 10.5),
  (timestamp '2015-05-29 08:00:01', 1, 1, 3, 30),
  (timestamp '2015-05-29 12:07:32', 1, 1, 2, 20),
  (timestamp '2015-05-30 18:24:58', 1, 1, 2, 20),

  (timestamp '2015-05-29 11:12:54', 2, 2, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 2, 2, 3, 30),
  (timestamp '2015-05-29 12:00:03', 2, 2, 1, 10),
  (timestamp '2015-05-30 09:00:58', 2, 2, 2, 20),

  (timestamp '2015-05-29 09:30:00', 3, 3, 3, 10.5),
  (timestamp '2015-05-28 15:45:17', 3, 3, 1, 10),
  (timestamp '2015-05-29 12:00:02', 3, 3, 2, 20),
  (timestamp '2015-05-30 10:00:58', 3, 3, 2, 20),

  (timestamp '2015-05-29 11:30:00', 4, 1, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 4, 1, 3, 30),
  (timestamp '2015-05-28 12:00:32', 4, 2, 1, 8.75),
  (timestamp '2015-05-30 12:00:04', 4, 2, 2, 20),

  (timestamp '2015-05-29 10:30:00', 5, 1, 3, 10.5),
  (timestamp '2015-05-29 13:45:05', 5, 1, 3, 30),
  (timestamp '2015-05-29 12:00:32', 5, 1, 1, 12.5),
  (timestamp '2015-05-30 09:00:58', 5, 1, 2, 20),

  (timestamp '2015-05-29 10:30:00', 6, 2, 3, 10.5),
  (timestamp '2015-05-29 14:45:06', 6, 2, 3, 30),
  (timestamp '2015-05-28 12:00:32', 6, 2, 1, 10),
  (timestamp '2015-05-28 09:00:58', 6, 2, 2, 20),

  (timestamp '2015-05-28 10:30:00', 7, 3, 1, 10.5),
  (timestamp '2015-05-29 15:45:17', 7, 3, 3, 30),
  (timestamp '2015-05-29 15:00:07', 7, 3, 2, 20),
  (timestamp '2015-05-30 09:00:58', 7, 3, 2, 20),

  (timestamp '2015-05-29 10:30:00', 8, 1, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 8, 1, 1, 5),
  (timestamp '2015-05-29 16:00:08', 8, 2, 2, 20),
  (timestamp '2015-05-30 09:00:58', 8, 2, 2, 20),

  (timestamp '2015-05-29 17:30:00', 9, 3, 3, 10.5),
  (timestamp '2015-05-30 15:45:17', 9, 3, 1, 8),
  (timestamp '2015-05-29 12:00:09', 9, 3, 2, 20),
  (timestamp '2015-05-30 09:00:58', 9, 3, 2, 20),

  (timestamp '2015-05-29 18:30:00', 10, 1, 3, 10.5),
  (timestamp '2015-05-30 15:45:17', 10, 1, 3, 30),
  (timestamp '2015-05-29 12:00:10', 10, 1, 2, 20),
  (timestamp '2015-05-30 19:00:58', 10, 2, 2, 20),

  (timestamp '2015-05-29 10:30:00', 11, 2, 3, 10.5),
  (timestamp '2015-05-30 15:45:17', 11, 3, 3, 30),
  (timestamp '2015-05-28 12:18:11', 11, 3, 2, 20),
  (timestamp '2015-05-30 20:00:58', 11, 3, 2, 20),

  (timestamp '2015-05-29 21:30:00', 12, 2, 3, 10.5),
  (timestamp '2015-05-28 15:45:17', 12, 3, 1, 9.99),
  (timestamp '2015-05-29 12:00:32', 12, 3, 2, 20),
  (timestamp '2015-05-30 22:00:58', 12, 3, 2, 20),

  (timestamp '2015-05-29 06:30:00', 13, 2, 3, 10.5),
  (timestamp '2015-05-28 15:45:17', 13, 3, 3, 30),
  (timestamp '2015-05-29 12:00:32', 13, 3, 2, 20),
  (timestamp '2015-05-30 07:00:58', 13, 3, 2, 20),

  (timestamp '2015-05-28 10:30:00', 14, 1, 6, 45.65),
  (timestamp '2015-05-29 15:45:17', 14, 3, 3, 30),
  (timestamp '2015-05-29 08:00:32', 14, 3, 2, 20),
  (timestamp '2015-05-30 09:00:58', 14, 2, 2, 20),

  (timestamp '2015-05-28 10:30:00', 15, 2, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 15, 2, 3, 30),
  (timestamp '2015-05-29 09:00:32', 15, 2, 2, 20),
  (timestamp '2015-05-30 09:00:58', 15, 2, 2, 20),

  (timestamp '2015-05-28 10:30:00', 16, 3, 3, 10.5),
  (timestamp '2015-05-29 10:45:17', 16, 3, 3, 30),
  (timestamp '2015-05-29 12:00:32', 16, 1, 2, 20),
  (timestamp '2015-05-30 09:00:58', 16, 1, 2, 20),

  (timestamp '2015-05-28 10:30:00', 18, 1, 3, 10.5),
  (timestamp '2015-05-28 15:45:17', 18, 2, 3, 30),
  (timestamp '2015-05-29 11:00:32', 18, 2, 8, 80),
  (timestamp '2015-05-30 09:00:58', 18, 3, 2, 20),

  (timestamp '2015-05-29 10:30:00', 20, 2, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 20, 3, 3, 30),
  (timestamp '2015-05-29 12:00:32', 20, 1, 2, 20),
  (timestamp '2015-05-30 12:00:58', 20, 3, 2, 20),

  (timestamp '2015-05-29 10:30:00', 21, 2, 3, 10.5),
  (timestamp '2015-05-29 14:45:17', 21, 2, 3, 30),
  (timestamp '2015-05-29 12:00:32', 21, 3, 2, 20),
  (timestamp '2015-05-30 13:00:58', 21, 3, 2, 20),

  (timestamp '2015-05-29 15:30:00', 22, 1, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 22, 1, 3, 30),
  (timestamp '2015-05-29 12:00:32', 23, 1, 12, 120),
  (timestamp '2015-05-30 06:00:58', 24, 1, 2, 20),

  (timestamp '2015-05-30 10:30:00', 25, 2, 3, 10.5),
  (timestamp '2015-05-30 17:45:17', 25, 2, 3, 30),
  (timestamp '2015-05-29 12:00:32', 25, 2, 2, 20),
  (timestamp '2015-05-30 09:00:58', 25, 2, 2, 20),

  (timestamp '2015-05-29 10:30:00', 26, 3, 3, 10.5),
  (timestamp '2015-05-29 18:45:17', 26, 3, 3, 30),
  (timestamp '2015-05-29 12:00:32', 26, 3, 2, 20),
  (timestamp '2015-05-30 09:00:58', 26, 3, 2, 20),

  (timestamp '2015-05-29 10:30:00', 27, 3, 3, 10.5),
  (timestamp '2015-05-29 19:45:17', 27, 2, 3, 30),
  (timestamp '2015-05-29 12:00:32', 27, 1, 2, 20),
  (timestamp '2015-05-30 09:00:58', 28, 1, 2, 20),

  (timestamp '2015-05-30 10:30:00', 29, 2, 3, 10.5),
  (timestamp '2015-05-29 20:45:17', 30, 3, 3, 30),
  (timestamp '2015-05-29 12:00:32', 31, 1, 7, 65.47),
  (timestamp '2015-05-30 21:00:58', 32, 2, 2, 20),

  (timestamp '2015-05-29 10:30:00', 33, 3, 3, 10.5),
  (timestamp '2015-05-28 22:45:17', 33, 3, 3, 30),
  (timestamp '2015-05-29 12:00:32', 33, 3, 2, 20),
  (timestamp '2015-05-30 09:00:58', 33, 3, 2, 20),

  (timestamp '2015-05-29 10:30:00', 34, 2, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 34, 2, 3, 30),
  (timestamp '2015-05-28 23:00:32', 34, 2, 2, 20),
  (timestamp '2015-05-30 09:00:58', 34, 2, 2, 20),

  (timestamp '2015-05-29 10:30:00', 35, 1, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 35, 1, 3, 30),
  (timestamp '2015-05-28 08:00:32', 35, 1, 2, 20),
  (timestamp '2015-05-30 09:00:58', 35, 1, 2, 20),

  (timestamp '2015-05-28 10:30:00', 36, 1, 3, 10.5),
  (timestamp '2015-05-28 09:45:17', 36, 2, 3, 30),
  (timestamp '2015-05-29 12:00:32', 37, 3, 2, 20),
  (timestamp '2015-05-30 09:00:58', 37, 1, 2, 20),

  (timestamp '2015-05-29 10:30:00', 38, 2, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 38, 3, 3, 30),
  (timestamp '2015-05-29 10:00:32', 39, 1, 2, 20),
  (timestamp '2015-05-30 09:00:58', 39, 2, 2, 20),

  (timestamp '2015-05-30 10:30:00', 40, 1, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 40, 1, 3, 30),
  (timestamp '2015-05-29 11:00:32', 40, 1, 2, 20),
  (timestamp '2015-05-30 09:00:58', 40, 1, 2, 20),

  (timestamp '2015-05-29 10:30:00', 41, 2, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 41, 2, 3, 30),
  (timestamp '2015-05-29 12:00:32', 41, 2, 2, 20),
  (timestamp '2015-05-30 12:00:58', 41, 2, 2, 20),

  (timestamp '2015-05-29 10:30:00', 42, 3, 3, 10.5),
  (timestamp '2015-05-30 15:45:17', 43, 3, 3, 30),
  (timestamp '2015-05-29 13:00:32', 44, 3, 2, 20),
  (timestamp '2015-05-30 09:00:58', 45, 1, 2, 20),

  (timestamp '2015-05-29 14:30:00', 46, 2, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 46, 3, 3, 30),
  (timestamp '2015-05-29 12:00:32', 46, 3, 2, 20),
  (timestamp '2015-05-29 09:00:58', 46, 3, 2, 20),

  (timestamp '2015-05-29 15:30:00', 47, 1, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 47, 1, 3, 30),
  (timestamp '2015-05-29 12:00:32', 47, 1, 2, 20),
  (timestamp '2015-05-28 16:00:58', 47, 1, 2, 20),

  (timestamp '2015-05-29 10:30:00', 50, 2, 3, 10.5),
  (timestamp '2015-05-30 17:45:17', 50, 2, 3, 30),
  (timestamp '2015-05-29 12:00:32', 50, 2, 2, 20),
  (timestamp '2015-05-28 09:00:58', 50, 2, 2, 20),

  (timestamp '2015-05-29 10:30:00', 51, 3, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 51, 3, 3, 30),
  (timestamp '2015-05-30 18:00:32', 51, 3, 2, 20),
  (timestamp '2015-05-29 09:00:58', 51, 3, 2, 20),

  (timestamp '2015-05-29 10:30:00', 52, 1, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 52, 1, 3, 30),
  (timestamp '2015-05-28 19:00:32', 52, 1, 2, 20),
  (timestamp '2015-05-29 09:00:58', 52, 1, 2, 20),

  (timestamp '2015-05-29 10:30:00', 53, 2, 3, 10.5),
  (timestamp '2015-05-29 15:45:17', 54, 2, 3, 30),
  (timestamp '2015-05-28 12:00:32', 55, 3, 2, 20);


















