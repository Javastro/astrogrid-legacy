
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



