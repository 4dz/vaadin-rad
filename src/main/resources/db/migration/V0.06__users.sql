create table role (
  name varchar(10) primary key not null
);

create table user (
  user_name varchar(100) primary key not null,
  hashed_password varchar(255) not null,
  first_name varchar(50) not null,
  last_name varchar(100) not null,
  title varchar(20) not null,
  male boolean not null,
  email varchar(255) not null,
  location varchar(255) not null,
  phone varchar(50) not null,
  newsletter_subscription integer,
  website varchar(1024),
  bio varchar(1024)

);

create table user_role (
  user_name varchar(100) not null,
  role_name varchar(10) not null,

  FOREIGN KEY (user_name) REFERENCES user(user_name),
  FOREIGN KEY (role_name) REFERENCES role(name),
  PRIMARY KEY (user_name, role_name)
);

insert into role (name) values ('admin'), ('guest'), ('author');

// password=password
insert into user values ('admin', '$2a$10$emvvR53LIzBlEnl.F7GAqeC4/IcppozwcFkNkPVPvbji/e1aToEvK', 'Adam', 'Perry', 'Mr', true, 'email@domain.com', 'Mars Colony', '07999 999999',1,'','');
insert into user_role values ('admin','admin'), ('admin', 'author');
insert into user values ('guest', '$2a$10$JpketdDvmmaxrt.Kn17H7.jkc24l9RXNfJsMiu.F/njEKk6kJUw.y', 'Joe', 'Bloggs', 'Mx', true, 'email@domain.com', 'Venus Colony', '07888 888888',0,'http://www.vaadin.com/','');
insert into user_role values ('guest','guest');
