/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/sql/hsqldb/Attic/create.sql,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 11:01:35 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: create.sql,v $
 *   Revision 1.5  2003/09/08 11:01:35  KevinBenson
 *   A check in of the Authentication authenticateToken roughdraft and some changes to the groudata and community data
 *   along with an AdministrationDelegate
 *
 *   Revision 1.4  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 *   Revision 1.3  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
 *
 *   Revision 1.2  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
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
    type        VARCHAR NOT NULL,
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
    service     VARCHAR NULL,
    manager     VARCHAR NULL,
    identity    VARCHAR NULL,    
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

CREATE TABLE securitytokens
    (
    ident       INTEGER IDENTITY,
    token       VARCHAR NOT NULL,
    target      VARCHAR NOT NULL,
    used        BIT DEFAULT 'false',
    expirationdate TIMESTAMP DEFAULT 'now',
    account      VARCHAR NOT NULL,
    todaysdate  TIMESTAMP DEFAULT 'now'
    ) ;
