-- $Id: createcontroldb.sql,v 1.2 2003/12/07 01:09:48 pah Exp $
-- creates the control database for the application controller

-- main table of execution parameters
DROP TABLE exestat IF EXISTS ;
CREATE TABLE exestat
    (
    executionId      INTEGER IDENTITY,
    jobstepId    VARCHAR ,
    program      VARCHAR ,
    starttime        DATETIME DEFAULT 'now',
    endtime TIMESTAMP
    ) ;
