
-----------------------------------------------------------------------------
-- DDL for Database 'jes'
-----------------------------------------------------------------------------
print 'jes'

use master
go

create database jes on master = 2
go


-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.job'
-----------------------------------------------------------------------------
print 'Creating Table jes.dbo.job'
go 

use jes 
go 

setuser 'dbo' 
go 

create table job (
	JOBURN                          varchar(128)                     not null  ,
	JOBNAME                         char(32)                             null  ,
	STATUS                          char(32)                         not null  ,
	SUBMITTIMESTAMP                 char(32)                         not null  ,
	USERID                          char(32)                         not null  ,
	COMMUNITY                       char(32)                         not null  ,
	JOBXML                          text		                     not null  ,
	DESCRIPTION						varchar(128)					 	 null 
)
lock allpages
with exp_row_size = 1 on 'default'
go 


setuser 
go 

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.jobstep'
-----------------------------------------------------------------------------
print 'Creating Table jes.dbo.jobstep'
go 

use jes 
go 

setuser 'dbo' 
go 

create table jobstep (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	STEPNAME                        char(32)                             null  ,
	STATUS                          char(32)                         not null  ,
	COMMENT                         varchar(128)                         null  ,
	SEQUENCENUMBER					char(32)						 not null  ,
	JOINCONDITION					char(16)						 not null 
)
lock allpages
 on 'default'
go 


setuser 
go 

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.query'
-----------------------------------------------------------------------------
print 'Creating Table jes.dbo.query'
go 

use jes 
go 

setuser 'dbo' 
go 

create table query (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null   
)
lock allpages
 on 'default'
go 


setuser 
go 

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.catalog'
-----------------------------------------------------------------------------
print 'Creating Table jes.dbo.catalog'
go 

use jes 
go 

setuser 'dbo' 
go 

create table catalog (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	CATALOGNAME                     char(32)                             null   
)
lock allpages
 on 'default'
go 


setuser 
go 

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.catalogtable'
-----------------------------------------------------------------------------
print 'Creating Table jes.dbo.catalogtable'
go 

use jes 
go 

setuser 'dbo' 
go 

create table catalogtable (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	CATALOGNAME                     char(32)                         not null  ,
	TABLENAME                       char(32)                         not null   
)
lock allpages
 on 'default'
go 


setuser 
go 

-----------------------------------------------------------------------------
-- DDL for Table 'jes.dbo.service'
-----------------------------------------------------------------------------
print 'Creating Table jes.dbo.service'
go 

use jes 
go 

setuser 'dbo' 
go 

create table service (
	JOBURN                          varchar(128)                     not null  ,
	STEPNUMBER                      char(16)                         not null  ,
	CATALOGNAME                     char(32)                             null  ,
	SERVICENAME                     char(32)                             null  ,
	SERVICEURL                      varchar(128)                         null   
)
lock allpages
 on 'default'
go 


setuser 
go 

-----------------------------------------------------------------------------
-- Dependent DDL for Object(s)
-----------------------------------------------------------------------------
use jes
go

exec sp_changedbowner 'sa'
go

exec master.dbo.sp_dboption jes, 'select into/bulkcopy', true
go

checkpoint
go

use jes 
go 

sp_addthreshold jes, 'logsegment', 16, sp_thresholdaction 
go 


-----------------------------------------------------------------------------
-- DDL for Table 'job.dbo.job'
-----------------------------------------------------------------------------
print 'Creating Table job.dbo.job'
go 

use job 
go 

setuser 'dbo' 
go 

create table job (
	JOBURN                          varchar(128)                     not null  ,
	JOBNAME                         char(32)                             null  ,
	RUNTIMESTAMP                    char(32)                         not null  ,
	USERID                          char(32)                         not null  ,
	COMMUNITY                       char(32)                         not null  ,
	STATUS                          char(32)                         not null  ,
	COMMENT                         varchar(128)                         null   
)
lock allpages
with exp_row_size = 1 on 'default'
go 


setuser 
go 
