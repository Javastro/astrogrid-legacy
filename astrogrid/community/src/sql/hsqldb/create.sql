/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/sql/hsqldb/Attic/create.sql,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/08/28 17:33:56 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: create.sql,v $
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
--
-- Drop the accounts table
DROP TABLE accounts IF EXISTS ;

--
-- Create the accounts table
CREATE TABLE accounts
    (
    ident       VARCHAR NOT NULL,
    description VARCHAR NULL,
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
    description VARCHAR NULL,
    PRIMARY KEY ( ident )
    ) ;

--
-- Drop the members table
DROP TABLE members IF EXISTS ;

--
-- Create the members table
CREATE TABLE members
    (
    groupident   VARCHAR NOT NULL,
    accountident VARCHAR NOT NULL
    ) ;

--
-- Drop the resources table
DROP TABLE resources IF EXISTS ;

--
-- Create the resources table
CREATE TABLE resources
    (
    ident       VARCHAR NOT NULL,
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
    valid       BIT,
    reason      VARCHAR NULL,
    PRIMARY KEY (resourceid, groupid, action)
    ) ;


