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
insert into user values ('4dz', 'ChJzErIAKCp3szkwE0RGn2tVziBv+QjP8GX/a5Rcgh2vxPB72FfmIiMfm3kCHn5SwNGhLX9t9FzBjLzE8LJBmg==', 'Adam', 'Perry', 'Mr', true, 'email@domain.com', 'Mars Colony', '07999 999999',1,'','');
insert into user_role values ('4dz','admin'), ('4dz', 'author');
