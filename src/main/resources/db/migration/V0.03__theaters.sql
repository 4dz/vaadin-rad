create table theater (
  id BIGINT auto_increment primary key not null,
  country varchar(100) not null,
  city varchar(100) not null,
  name varchar(100) not null
);

insert into theater values (1,'China','Shanghai','Theater 1');
insert into theater values (2, 'China', 'Beijing', 'Theater 2');

