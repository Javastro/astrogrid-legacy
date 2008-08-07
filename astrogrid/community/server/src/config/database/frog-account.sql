-- Creates the standard DB-schema and loads one account named frog with
-- password croakcroak.


DROP TABLE testdata IF EXISTS ;


CREATE TABLE testdata
    (
    ident       VARCHAR NOT NULL,
    version     VARCHAR NULL,
    PRIMARY KEY ( ident )
    ) ;

INSERT INTO testdata (ident, version)
    VALUES (
    'ident',
    'version'
    ) ;

DROP TABLE accounts IF EXISTS ;

CREATE TABLE accounts
    (
    ident       VARCHAR NOT NULL,
    display     VARCHAR NULL,
    description VARCHAR NULL,
    home        VARCHAR NULL,
    email       VARCHAR NULL,
    PRIMARY KEY ( ident )
    ) ;

DROP TABLE secrets IF EXISTS ;

CREATE TABLE secrets
    (
    account     VARCHAR NOT NULL,
    password    VARCHAR NULL,
    encryption  VARCHAR NOT NULL,
    PRIMARY KEY ( account )
    ) ;

INSERT INTO SECRETS (account, password, encryption) VALUES (
  'ivo://pond/frog',
  'croakcroak',
  'CLEAR_TEXT'
);