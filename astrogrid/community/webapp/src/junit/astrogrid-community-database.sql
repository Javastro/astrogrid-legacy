/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/webapp/src/junit/astrogrid-community-database.sql,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2006/08/16 09:44:16 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: astrogrid-community-database.sql,v $
 *   Revision 1.2  2006/08/16 09:44:16  clq2
 *   gtr_community_1722
 *
 *   Revision 1.1.2.1  2006/08/13 17:05:40  gtr
 *   This was changed as part of the big update to introduce a certificate authority.
 *
 *   Revision 1.4  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.3.12.1  2004/03/10 13:32:01  dave
 *   Added home space to AccountData.
 *   Improved null param checking in AccountManager.
 *   Improved null param checking in AccountManager tests.
 *
 *   Revision 1.3  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.2.2.1  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.1  2004/02/19 14:51:00  dave
 *   Changed DatabaseManager to DatabaseConfigurationFactory.
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.2  2004/01/30 14:55:46  dave
 *   Added PasswordData object
 *
 *   Revision 1.1.2.1  2004/01/26 13:18:08  dave
 *   Added new DatabaseManager to enable local JUnit testing
 *
 * </cvs:log>
 *
 *
 */

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
-- Insert at least one row into the test data.
INSERT INTO accounts (ident, displayName)
    VALUES (
    'test-community/fred',
    'Fred Nothing'
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

--
-- Insert at least one row into the test data.
INSERT INTO secrets (account, password, encryption)
    VALUES (
    'test-community/fred',
    'fred',
    'none'
    );
