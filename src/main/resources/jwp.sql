drop table if exists users;

create table users (
    userId varchar(12) not null primary key,
    password varchar(12) not null,
    name varchar(20) not null,
    email varchar(50)
);

insert into users values('admin', 'admin', '김진억', 'jinuk17@gmail.com');
insert into users values('a1', 'a1', '기승', 'a1@mail.com');
insert into users values('a2', 'a2', '미진', 'a2@mail.com');
insert into users values('a3', 'a3', '범희', 'a3@mail.com');
insert into users values('a4', 'a4', '현지', 'a4@mail.com');