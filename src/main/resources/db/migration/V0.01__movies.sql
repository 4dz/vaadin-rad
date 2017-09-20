create table movie (
  id BIGINT auto_increment primary key not null,
  title varchar(255) not null,
  synopsis varchar(1024) not null,
  thumb_url varchar(1024) not null,
  poster_url varchar(1024) not null,
  duration integer not null,
  release_date timestamp not null,
  score integer not null
);

insert into movie
values(1,
  'San Andreas',
  'Journey 2: The Mysterious Island''s Brad Peyton steps into disaster movie territory with this 3D film surrounding an earthquake that hits the West Coast. Carlton Cuse provides the script. ~ Jeremy Wheeler, Rovi',
  'http://resizing.flixster.com/AeaFMmg9JiQcPXr64RZhonhPQCw=/54x80/dkpu1ddg7pbsk.cloudfront.net/movie/11/19/13/11191363_ori.jpg',
  'http://resizing.flixster.com/AeaFMmg9JiQcPXr64RZhonhPQCw=/54x80/dkpu1ddg7pbsk.cloudfront.net/movie/11/19/13/11191363_ori.jpg',
  114,
  date '2015-05-29',
  48
);

insert into movie
values(2,
       'Poltergeist',
       'Legendary filmmaker Sam Raimi ("Spiderman," "Evil Dead", "The Grudge") and director Gil Kenan ("Monster House") contemporize the classic tale about a family whose suburban home is haunted by evil forces. When the terrifying apparitions escalate their attacks and hold the youngest daughter captive, the family must come together to rescue her before she disappears forever. (C) Fox',
       'http://resizing.flixster.com/bkYeEV4fy7nKB7-QnOPE_PL57ZI=/54x80/dkpu1ddg7pbsk.cloudfront.net/movie/11/19/08/11190873_ori.jpg',
       'http://resizing.flixster.com/bkYeEV4fy7nKB7-QnOPE_PL57ZI=/54x80/dkpu1ddg7pbsk.cloudfront.net/movie/11/19/08/11190873_ori.jpg',
       93,
       date '2015-05-22',
       32
);


insert into movie
values(3,
       'Mad Max: Fury Road',
       'Filmmaker George Miller gears up for another post-apocalyptic action adventure with Fury Road, the fourth outing in the Mad Max film series. Charlize Theron stars alongside Tom Hardy (Bronson), with Zoe Kravitz, Adelaide Clemens, and Rosie Huntington Whiteley heading up the supporting cast. ~ Jeremy Wheeler, Rovi',
       'http://resizing.flixster.com/fYBKxclWsYLoJy9l5Oa2632NMNM=/54x80/dkpu1ddg7pbsk.cloudfront.net/movie/11/19/12/11191276_ori.jpg',
       'http://resizing.flixster.com/fYBKxclWsYLoJy9l5Oa2632NMNM=/54x80/dkpu1ddg7pbsk.cloudfront.net/movie/11/19/12/11191276_ori.jpg',
       120,
       date '2015-05-15',
       98
);
