create table theater (
  id BIGINT auto_increment primary key not null,
  country varchar(100) not null,
  city varchar(100) not null,
  name varchar(100) not null
);

insert into theater (country, city, name) values
  ('China', 'Shanghai','Theater 1'),
  ('China', 'Shanghai','Theater 2'),
  ('China', 'Shanghai','Theater 3'),
  ('China', 'Shanghai','Theater 4'),
  ('China', 'Beijing', 'Theater 1'),
  ('China', 'Beijing', 'Theater 2'),
  ('Republic of China (Taiwan)', 'New Taipei', 'Theater 1'),
  ('Republic of China (Taiwan)', 'New Taipei', 'Theater 2'),
  ('Iran', 'Tehran', 'Theater 1'),
  ('Iran', 'Tehran', 'Theater 2'),
  ('Iran', 'Tehran', 'Theater 3'),
  ('Iran', 'Tehran', 'Theater 4'),
  ('Myanmar', 'Yangon', 'Theater 1'),
  ('Kenya', 'Nairobi', 'Theater 1'),
  ('United Kingdom', 'London', 'Theater 1'),
  ('United Kingdom', 'London', 'Theater 2'),
  ('United Kingdom', 'London', 'Theater 3'),
  ('United Kingdom', 'Wolverhampton', 'Theater 1'),
  ('United Kingdom', 'Birmingham', 'Theater 1'),
  ('United Kingdom', 'Manchester', 'Theater 1');


;
