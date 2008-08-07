
--
--
-- Drop the testdata table
DROP TABLE testdata IF EXISTS ;

--
-- Create the testdata table
CREATE TABLE testdata
    (
    ident       VARCHAR NOT NULL,
    version     VARCHAR NULL,
    PRIMARY KEY ( ident )
    ) ;

--
-- Insert at least one row into the test data.
INSERT INTO testdata (ident, version)
    VALUES (
    'ident',
    'version'
    ) ;

--
--
-- Drop the accounts table
DROP TABLE accounts IF EXISTS ;

--
-- Create the accounts table
CREATE TABLE accounts
    (
    ident       VARCHAR NOT NULL,
    display     VARCHAR NULL,
    description VARCHAR NULL,
    home        VARCHAR NULL,
    email       VARCHAR NULL,
    PRIMARY KEY ( ident )
    ) ;

--
-- Drop the groups table
DROP TABLE groups IF EXISTS ;

--
-- Create the groups table
CREATE TABLE groups
    (
    ident       VARCHAR NOT NULL,
    name        VARCHAR NULL,
    display     VARCHAR NULL,
    description VARCHAR NULL,
    type        VARCHAR NULL,
    PRIMARY KEY ( ident )
    ) ;

--
-- Drop the members table
DROP TABLE members IF EXISTS ;

--
-- Create the members table
-- TODO Should have both fields as the primary key (prevent duplicates).
CREATE TABLE members
    (
    groupident   VARCHAR NOT NULL,
    accountident VARCHAR NOT NULL,
    PRIMARY KEY (accountident, groupident)
    ) ;

--
-- Drop the resources table
DROP TABLE resources IF EXISTS ;

--
-- Create the resources table
CREATE TABLE resources
    (
    ident       VARCHAR NOT NULL,
    display     VARCHAR NULL,
    description VARCHAR NULL,
    PRIMARY KEY ( ident )
    ) ;

--
-- Drop the communities table
DROP TABLE communities IF EXISTS ;

--
-- Create the communities table
CREATE TABLE communities
    (
    ident       VARCHAR NOT NULL,
    service     VARCHAR NULL,
    manager     VARCHAR NULL,
    identity    VARCHAR NULL,
    display     VARCHAR NULL,
    description VARCHAR NULL,
    PRIMARY KEY ( ident )
    ) ;

--
-- Drop the permissions table
DROP TABLE permissions IF EXISTS ;

--
-- Create the permissions table
CREATE TABLE permissions
    (
    action      VARCHAR NOT NULL,
    resourceid  VARCHAR NOT NULL,
    groupid     VARCHAR NOT NULL,
    status      INTEGER,
    reason      VARCHAR NULL,
    PRIMARY KEY (resourceid, groupid, action)
    ) ;

--
--
-- Drop the tokens table
DROP TABLE tokens IF EXISTS ;

--
-- Create the tokens table
CREATE TABLE tokens
    (
    account  VARCHAR NOT NULL,
    token    VARCHAR NOT NULL,
    status   INTEGER NOT NULL,
    PRIMARY KEY ( token )
    ) ;

--
--
-- Drop the secrets table
DROP TABLE secrets IF EXISTS ;

--
-- Create the secrets table
CREATE TABLE secrets
    (
    account     VARCHAR NOT NULL,
    password    VARCHAR NULL,
    encryption  VARCHAR NOT NULL,
    PRIMARY KEY ( account )
    ) ;

INSERT INTO SECRETS (account, password, encryption) VALUES (
  'frog',
  'croakcroak',
  'CLEAR_TEXT'
);