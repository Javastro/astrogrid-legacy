

create table job (
	JOBURN                          varchar(128)                     not null  ,
	JOBNAME                         char(32)                             null  ,
	STATUS                          char(32)                         not null  ,
	SUBMITTIMESTAMP                 char(32)                         not null  ,
	USERID                          char(32)                         not null  ,
	COMMUNITY                       char(32)                         not null  ,
	JOBXML                          varchar(128)		                     not null  ,
	DESCRIPTION						varchar(128)					 	 null 
)

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.jobstep'
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
-- DDL for Table 'jes.dbo.query'
-----------------------------------------------------------------------------


create table query (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null   
)


-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.catalog'
-----------------------------------------------------------------------------
 

create table catalog (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	CATALOGNAME                     char(32)                             null   
)
 

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.catalogtable'
-----------------------------------------------------------------------------


create table catalogtable (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	CATALOGNAME                     char(32)                         not null  ,
	TABLENAME                       char(32)                         not null   
)


-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.service'
-----------------------------------------------------------------------------


create table service (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	CATALOGNAME                     char(32)                             null  ,
	SERVICENAME                     char(32)                             null  ,
	SERVICEURL                      varchar(128)                         null   
)



-----------------------------------------------------------------------------
-- DDL for Table 'job.dbo.job'
-----------------------------------------------------------------------------


create table job2 (
	JOBURN                          varchar(128)                     not null  ,
	JOBNAME                         char(32)                             null  ,
	RUNTIMESTAMP                    char(32)                         not null  ,
	USERID                          char(32)                         not null  ,
	COMMUNITY                       char(32)                         not null  ,
	STATUS                          char(32)                         not null  ,
	COMMENT                         varchar(128)                         null   
)

