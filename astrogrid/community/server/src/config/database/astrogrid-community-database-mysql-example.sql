/*
 *
 * <cvs:source>$Source: /devel/astrogrid/community/server/src/config/database/astrogrid-community-database.sql,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: astrogrid-community-database.sql,v $
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
 
DROP TABLE  IF EXISTS testdata;

CREATE TABLE testdata
    (
    IDENT       VARCHAR(100) NOT NULL,
    VERSION     VARCHAR(100) NULL,
    PRIMARY KEY ( IDENT )
    ) ;

INSERT INTO testdata (IDENT, VERSION)
    VALUES (
    'ident',
    'version'
    ) ;

DROP TABLE  IF EXISTS accounts;

CREATE TABLE accounts
    (
    IDENT       VARCHAR(100) NOT NULL,
    DISPLAY     VARCHAR(100) NULL,
    DESCRIPTION VARCHAR(100) NULL,
    HOME        VARCHAR(100) NULL,
    EMAIL       VARCHAR(100) NULL,
    PRIMARY KEY ( IDENT )
    ) ;

DROP TABLE  IF EXISTS groups;

CREATE TABLE groups
    (
    IDENT       VARCHAR(100) NOT NULL,
    NAME        VARCHAR(100) NULL,
    DISPLAY     VARCHAR(100) NULL,
    DESCRIPTION VARCHAR(100) NULL,
    TYPE        VARCHAR(100) NULL,
    PRIMARY KEY ( IDENT )
    ) ;

DROP TABLE  IF EXISTS members;

CREATE TABLE members
    (
    GROUPIDENT   VARCHAR(100) NOT NULL,
    ACCOUNTIDENT VARCHAR(100) NOT NULL,
    PRIMARY KEY (ACCOUNTIDENT, GROUPIDENT)
    ) ;

DROP TABLE  IF EXISTS resources;

CREATE TABLE resources
    (
    IDENT       VARCHAR(100) NOT NULL,
    DISPLAY     VARCHAR(100) NULL,
    DESCRIPTION VARCHAR(100) NULL,
    PRIMARY KEY ( IDENT )
    ) ;

DROP TABLE  IF EXISTS communities;

CREATE TABLE communities
    (
    IDENT       VARCHAR(100) NOT NULL,
    SERVICE     VARCHAR(100) NULL,
    MANAGER     VARCHAR(100) NULL,
    IDENTITY    VARCHAR(100) NULL,
    DISPLAY     VARCHAR(100) NULL,
    DESCRIPTION VARCHAR(100) NULL,
    PRIMARY KEY ( IDENT )
    ) ;

DROP TABLE  IF EXISTS permissions;

CREATE TABLE permissions
    (
    ACTION      VARCHAR(100) NOT NULL,
    RESOURCEID  VARCHAR(100) NOT NULL,
    GROUPID     VARCHAR(100) NOT NULL,
    STATUS      INTEGER,
    REASON      VARCHAR(100) NULL,
    PRIMARY KEY (RESOURCEID, GROUPID, ACTION)
    ) ;

DROP TABLE  IF EXISTS tokens;

CREATE TABLE tokens
    (
    ACCOUNT  VARCHAR(100) NOT NULL,
    TOKEN    VARCHAR(100) NOT NULL,
    STATUS   INTEGER NOT NULL,
    PRIMARY KEY ( TOKEN )
    ) ;

DROP TABLE  IF EXISTS secrets;

CREATE TABLE secrets
    (
    ACCOUNT     VARCHAR(100) NOT NULL,
    PASSWORD    VARCHAR(100) NULL,
    ENCRYPTION  VARCHAR(100) NOT NULL,
    PRIMARY KEY ( ACCOUNT )
    );