-- $Id: createcontroldb.sql,v 1.1 2003/12/01 22:24:59 pah Exp $
-- creates the control database for the application controller

-- main table of execution parameters
DROP TABLE execution IF EXISTS ;
CREATE TABLE execution
    (
    executionId       VARCHAR NOT NULL,
    jobstepId    VARCHAR NOT NULL,
    program      VARCHAR NOT NULL,
    starttime        DATETIME DEFAULT 'now',
    endtime TIMESTAMP,
    PRIMARY KEY (executionId)
    ) ;
