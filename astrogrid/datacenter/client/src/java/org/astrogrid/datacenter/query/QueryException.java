/*
 * @(#)QueryException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.query;

/** Created when an error handling a Query has occured - eg parsing ADQL.  */

public class QueryException extends RuntimeException {

    public QueryException( String message ) {
      super( message ) ;
    }

    public QueryException(  String message, Throwable cause ) {
      super( message, cause ) ;
    }

}
