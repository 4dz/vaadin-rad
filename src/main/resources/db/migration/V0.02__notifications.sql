create table dashboard_notification (
  id identity not null,
  content varchar(255) not null,
  read boolean not null,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  pretty_time varchar(255) not null,
  action varchar(255) not null
);

insert into dashboard_notification
values(1, 'Tesisa ni soapp, so ta romima rosasa nicni. Retrospective', false, 'Joe', 'Bloggs', '25 minutes ago', 'created a document');

insert into dashboard_notification
values(2, 'Isaso nol solamic, softny issogi cewace taka so', false, 'Pekka', 'Taylor', '1 day ago', 'changed the schedule');


