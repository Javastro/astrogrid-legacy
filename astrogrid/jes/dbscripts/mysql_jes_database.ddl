
-----------------------------------------------------------------------------
-- DDL for Database 'jes'
-----------------------------------------------------------------------------
-- drop database jes ;
create database jes ;
use jes ;

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.job'
-- JOBURN, JOBNAME, STATUS, SUBMITTIMESTAMP, USERID, COMMUNITY, GROUP, TOKEN, JOBXML, DESCRIPTION
-----------------------------------------------------------------------------
create table job (
	JOBURN                          varchar(128)                     not null  ,
	JOBNAME                         char(32)                             null  ,
	STATUS                          char(32)                         not null  ,
	SUBMITTIMESTAMP                 char(32)                         not null  ,
	USERID                          varchar(32)                      not null  ,
	COMMUNITY                       varchar(32)                      not null  ,
	COGROUP                         varchar(64)                      not null  ,
	COTOKEN                         char(8)                          not null  ,
	JOBXML                          text                             not null  ,
	DESCRIPTION						varchar(128)					 	 null 
);

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.jobstep'
-- JOBURN, STEPNUMBER, STEPNAME, STATUS, COMMENT, SEQUENCENUMBER, JOINCONDITION
-----------------------------------------------------------------------------
create table jobstep (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	STEPNAME                        char(32)                             null  ,
	STATUS                          char(32)                         not null  ,
	COMMENT                         varchar(128)                         null  ,
	SEQUENCENUMBER					char(8)						     not null  ,
	JOINCONDITION					char(8)						     not null 
);
-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.tool'
-- JOBURN, STEPNUMBER, TOOLNAME
-----------------------------------------------------------------------------
create table tool (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(8)                          not null  ,
	TOOLNAME                        varchar(64)                      not null   
);

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.parameter'
-- JOBURN, STEPNUMBER, TOOLNAME, PARAMNAME, DIRECTION, TYPE, LOCATION, CONTENTS
-----------------------------------------------------------------------------
create table parameter (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(8)                          not null  ,
	TOOLNAME                        varchar(64)                      not null  ,
	PARAMNAME                       varchar(64)                      not null  , 
	DIRECTION                       char(16)                         not null  ,  
	TYPE                        	varchar(64)                      not null  ,  
	LOCATION                        varchar(128)                     not null  ,  
	CONTENTS                        text                             null        
);
