
-----------------------------------------------------------------------------
-- DDL for Database 'jeb'
-----------------------------------------------------------------------------
drop database jeb ;
create database jeb ;
use jeb ;

-----------------------------------------------------------------------------
-- DDL for Table 'jeb.dbo.job'
-- JOBURN, JOBNAME, STATUS, SUBMITTIMESTAMP, USERID, COMMUNITY, GROUP, TOKEN, JOBXML, DESCRIPTION
-----------------------------------------------------------------------------
create table job (
	JOBURN                          varchar(128)                     not null  ,
	JOBNAME                         char(32)                             null  ,
	STATUS                          char(32)                         not null  ,
	SUBMITTIMESTAMP                 char(32)                         not null  ,
	USERID                          char(32)                         not null  ,
	COMMUNITY                       char(32)                         not null  ,
	GROUP                           char(32)                         not null  ,
	TOKEN                           char(8)                          not null  ,
	JOBXML                          varchar(2048)                    not null  ,
	DESCRIPTION						varchar(128)					 	 null 
)

-----------------------------------------------------------------------------
-- DDL for Table 'jeb.dbo.jobstep'
-- JOBURN, STEPNUMBER, STEPNAME, STATUS, COMMENT, SEQUENCENUMBER, JOINCONDITION
-----------------------------------------------------------------------------
create table jobstep (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	STEPNAME                        char(32)                             null  ,
	STATUS                          char(32)                         not null  ,
	COMMENT                         varchar(128)                         null  ,
	SEQUENCENUMBER					char(32)						 not null  ,
	JOINCONDITION					char(16)						 not null 
)
-----------------------------------------------------------------------------
-- DDL for Table 'jeb.dbo.tool'
-- JOBURN, STEPNUMBER, TOOLNAME
-----------------------------------------------------------------------------
create table tool (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	TOOLNAME                        char(64)                         not null  ,  
)

-----------------------------------------------------------------------------
-- DDL for Table 'jeb.dbo.parameter'
-- JOBURN, STEPNUMBER, TOOLNAME, PARAMNAME, DIRECTION, TYPE, LOCATION, CONTENTS
-----------------------------------------------------------------------------
create table parameter (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	TOOLNAME                        char(64)                         not null  ,
	PARAMNAME                       char(32)                         not null  , 
	DIRECTION                       char(16)                         not null  ,  
	TYPE                        	char(64)                         not null  ,  
	LOCATION                        char(64)                         not null  ,  
	CONTENTS                        varchar(2048)                        null  ,      
)
