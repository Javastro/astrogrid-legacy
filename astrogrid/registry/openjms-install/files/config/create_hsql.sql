drop table system_data;
create table system_data (
  id                     int not null,
  version                varchar(20) not null,
  creationDate           datetime not null
);
create unique index system_data_pk on system_data(id);

drop table message_id;
create table message_id (
  id                     numeric(28) not null,
  maxId                  numeric(28) not null
);
create unique index message_id_pk on message_id(id);

drop table seeds;
create table seeds (
  name                   varchar(20) not null,
  seed                   numeric(28) not null
);
create unique index seeds_pk on seeds(name);

drop table txids;
CREATE TABLE txids (
  id                    numeric(28) NOT NULL,
  xid                  binary NOT NULL,
  status                numeric(3) NOT NULL,
  timeout               numeric(28) NOT NULL
);
CREATE UNIQUE INDEX txids_pk ON txids(id);

drop table tx_messages;
CREATE TABLE tx_messages (
  messageId             numeric(28) NOT NULL,
  txid                  numeric(28) NOT NULL,
  messageBlob           binary NOT NULL
);
CREATE UNIQUE INDEX tx_messages_pk ON tx_messages(messageId)

drop table destinations;
create table destinations (
  name                   varchar(255) not null,
  isQueue                bit not null,
  destinationId          numeric(28) not null
);
create unique index destinations_pk on destinations(name);

drop table messages;
create table messages (
  messageId             numeric(28),
  destinationId         numeric(28) not null,
  messageType           varchar(20) not null,
  priority              numeric(3),
  createTime            numeric(28) not null,
  expiryTime            numeric(28),
  processed             numeric(3),
  messageBlob           binary not null
);
create index messages_pk on messages(messageId);

drop table message_handles;
CREATE TABLE message_handles (
   messageId            numeric(28),
   destinationId        numeric(28) NOT NULL,
   consumerId           numeric(28) NOT NULL,
   priority             numeric(3),
   acceptedTime         numeric(28),
   sequenceNumber       numeric(28),
   expiryTime           numeric(28),
   delivered            numeric(3)
);
CREATE INDEX message_handles_pk ON message_handles(destinationId, consumerId, messageId);

drop table consumers;
create table consumers (
  name                 varchar(50) not null,
  destinationId        numeric(28) not null,
  consumerId           numeric(28) not NULL,
  created              numeric(28) NOT NULL
);
create unique index consumers_pk on consumers(name, destinationId);


