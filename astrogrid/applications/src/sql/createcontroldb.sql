-- $Id: createcontroldb.sql,v 1.3 2004/04/19 17:34:08 pah Exp $
-- creates the control database for the application controller

-- main table of execution parameters
DROP TABLE exestat IF EXISTS ;
CREATE TABLE exestat
    (
    executionId      INTEGER IDENTITY,
    jobstepId    VARCHAR ,
    program      VARCHAR ,
    starttime        DATETIME DEFAULT 'now',
    params         VARCHAR,
    endtime TIMESTAMP,
    status  VARCHAR
    ) ;
