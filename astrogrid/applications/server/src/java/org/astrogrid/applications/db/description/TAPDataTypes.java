/*
 * $Id: TAPDataTypes.java,v 1.2 2011/09/02 21:55:54 pah Exp $
 * 
 * Created on 8 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db.description;

import java.sql.SQLException;

/**
 * The TAP data types.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 8 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public enum TAPDataTypes {

    BOOLEAN,
    SMALLINT,
    INTEGER,
    BIGINT,
    REAL,
    DOUBLE,
    TIMESTAMP,
    CHAR,
    VARCHAR,
    BINARY,
    VARBINARY,
    POINT,
    REGION,
    CLOB,
    BLOB
    ;
    
    public static TAPDataTypes fromJDBCType(int jdbctype) throws SQLException{
        switch (jdbctype) {
        case java.sql.Types.BOOLEAN:
            return BOOLEAN;
        case java.sql.Types.SMALLINT:
            return SMALLINT;
            
        case java.sql.Types.INTEGER:
            return INTEGER;
            
        case java.sql.Types.BIGINT:
            return BIGINT;
            
        case java.sql.Types.REAL:
            return REAL;
            
        case java.sql.Types.DOUBLE:
            return DOUBLE;
            
        case java.sql.Types.TIMESTAMP:
            return TIMESTAMP;
  
        case java.sql.Types.CHAR:
            return CHAR;
        
        case java.sql.Types.VARCHAR:
            return VARCHAR;

        case java.sql.Types.BINARY:
            return BINARY;
            
        case java.sql.Types.VARBINARY:
            return VARBINARY;
            
        case java.sql.Types.CLOB:
            return CLOB;
            
        case java.sql.Types.BLOB:
            return BLOB;
            
 //FIXME - need to decide about POINT...
 
     
        default:
            throw new SQLException("cannot find mapping for "+jdbctype);
        }
    }
}


/*
 * $Log: TAPDataTypes.java,v $
 * Revision 1.2  2011/09/02 21:55:54  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/16 19:51:57  pah
 * NEW - bug 2944: add DAL support
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2944
 *
 * initial metadata querier implementation
 *
 */
