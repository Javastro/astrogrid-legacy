set autocommit true;
drop table PEOPLE if exists;
create table PEOPLE (
  id INTEGER NOT NULL PRIMARY KEY,
  firstName VARCHAR(20),
  lastName VARCHAR(20),
  age INTEGER
  
); 
insert into PEOPLE values (1,'fred','bloggs',50);
insert into PEOPLE values(2,'mavis','goggs',95);
insert into PEOPLE values(3,'tiny','rugrat',1);
