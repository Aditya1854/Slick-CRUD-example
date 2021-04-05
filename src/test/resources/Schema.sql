create table university(id int primary key,name varchar(200) not null, location varchar(200) not null);
create table student(id int primary key auto_increment,name varchar(200) not null,email varchar(200) unique,university_id int,DOB date, foreign key (university_id) references university(id));
