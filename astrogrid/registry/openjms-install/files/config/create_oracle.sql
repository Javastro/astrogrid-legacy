drop table system_data;
create table system_data (
  id                     int not null,
  version                varchar(20) not null,
  creationDate           date not null
);
create unique index system_data_pk on system_data(id);

drop table message_id;
create table message_id (
  id                     NUMBER(28) not null,
  maxId                  NUMBER(28) not null
);
create unique index message_id_pk on message_id(id);

drop table seeds;
create table seeds (
  name                   varchar(20) not null,
  seed                   NUMBER(28) not null
);
create unique index seeds_pk on seeds(name);

DROP TABLE txids;
CREATE TABLE txids (
  id                    NUMBER(28) NOT NULL,
  xid                  LONG RAW NOT NULL,
  status                NUMBER(3) NOT NULL,
  timeout               NUMBER(28) NOT NULL
);
CREATE UNIQUE INDEX txids_pk ON txids(id);

DROP TABLE tx_messages;
CREATE TABLE tx_messages (
  messageId             NUMBER(28) NOT NULL,
  txid                  NUMBER(28) NOT NULL,
  messageBlob           LONG RAW NOT NULL
);
CREATE UNIQUE INDEX tx_messages_pk ON tc_messages(messageId)

drop table destinations;
create table destinations (
  name                   varchar(255) not null,
  isQueue                NUMBER(3) not null,
  destinationId          NUMBER(28) not null
);
create unique index destinations_pk on destinations(name);

drop table messages;
create table messages (
  messageId             NUMBER(28),
  destinationId         NUMBER(28) not null,
  messageType           varchar(20) not null,
  priority              NUMBER(3),
  createTime            NUMBER(28) not null,
  expiryTime            NUMBER(28),
  processed             NUMBER(3),
  messageBlob           LONG raw not null
);
create index messages_pk on messages(messageId);

DROP TABLE message_handles;
CREATE TABLE message_handles (
   messageId            NUMBER(28),
   destinationId        NUMBER(28) NOT NULL,
   consumerId           NUMBER(28) NOT NULL,
   priority             NUMBER(3),
   acceptedTime         NUMBER(28),
   sequenceNumber       NUMBER(28),
   expiryTime           NUMBER(28),
   delivered            NUMBER(3)
);
CREATE INDEX message_handles_pk ON message_handles(destinationId, consumerId, messageId);

drop table consumers;
create table consumers (
  name                 varchar(50) not null,
  destinationId        NUMBER(28) not null,
  consumerId           NUMBER(28) not NULL,
  created              NUMBER(28) NOT NULL
);
create unique index consumers_pk on consumers(name, destinationId);

drop table jndi_context;
