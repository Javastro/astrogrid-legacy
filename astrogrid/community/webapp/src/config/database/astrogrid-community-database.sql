/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/webapp/src/config/database/astrogrid-community-database.sql,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: astrogrid-community-database.sql,v $
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.1  2004/02/20 01:02:38  dave
 *   Added database config files to webapp.
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
    name        VARCHAR NULL,
    email       VARCHAR NULL,
    display     VARCHAR NULL,
    description VARCHAR NULL,
    PRIMARY KEY ( ident )
    ) ;

--
--
-- Drop the passwords table
DROP TABLE passwords IF EXISTS ;

--
-- Create the passwords table
CREATE TABLE passwords
    (
    account     VARCHAR NOT NULL,
    password    VARCHAR NULL,
    encryption  VARCHAR NOT NULL,
    PRIMARY KEY ( account )
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

