/*
 * @(#)QueryFactory.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.query;


import org.w3c.dom.Element;

public interface QueryFactory {

    Query createQuery( Element queryElement ) throws QueryException ;

    public void execute(Query query) throws QueryException ;

    public void end() ;

    public Object getImplementation() ;

} // end of interface QueryFactory
