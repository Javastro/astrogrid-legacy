set autocommit true;
create table developers (
  id INTEGER NOT NULL PRIMARY KEY,
  firstName VARCHAR(20),
  lastName VARCHAR(20),
  location VARCHAR(20),
  phone INTEGER
);
