create table "informix".system_data (
  id                     DECIMAL(32,0) not null,
  version                varchar(20) not null,
  creationDate           datetime year to fraction not null
);
create unique index system_data_pk on "informix".system_data(id);

create table "informix".message_id (
  id                     DECIMAL(32,0) not null,
  maxId                  DECIMAL(32,0) not null
);
create unique index message_id_pk on "informix".message_id(id);

create table "informix".seeds (
  name                   varchar(20) not null,
  seed                   DECIMAL(32,0) not null
);
create unique index seeds_pk on "informix".seeds(name);

create table "informix".destinations (
  name                   varchar(255) not null,
  isQueue                int not null,
  destinationId          DECIMAL(32,0) not null
);
create unique index destinations_pk on "informix".destinations(name);

create table "informix".messages (
  messageId             DECIMAL(32,0),
  destinationId         DECIMAL(32,0) not null,
  messageType           varchar(20) not null,
  priority              int,
  createTime            DECIMAL(32,0) not null,
  expiryTime            DECIMAL(32,0),
  processed             int,
  messageBlob           byte not null
);
create index messages_pk on "informix".messages(messageId);
 

CREATE TABLE "informix".message_handles (
   messageId            DECIMAL(32,0),
   destinationId        DECIMAL(32,0) NOT NULL,
   consumerId           DECIMAL(32,0) NOT NULL,
   priority             int,
   acceptedTime         DECIMAL(32,0),
   sequenceNumber       DECIMAL(32,0),
   expiryTime           DECIMAL(32,0),
   delivered            int
);
CREATE INDEX message_handles_pk ON "informix".message_handles(destinationId, consumerId, messageId);

create table "informix".consumers (
  name                 varchar(50) not null,
  destinationId        DECIMAL(32,0) not null,
  consumerId           DECIMAL(32,0) not NULL,
  created              DECIMAL(32,0) NOT NULL
);
create unique index consumers_pk on "informix".consumers(name, destinationId);
