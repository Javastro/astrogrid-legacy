/*$Id: ResultTransformerSet.java,v 1.1 2005/02/22 01:10:31 nw Exp $
 * Created on 21-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.service.annotation;

import org.apache.commons.collections.Transformer;

/**Bean that contains a set of transformers to convert a result into a variety of different forms.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public interface ResultTransformerSet {
    public abstract Transformer getHtmlTransformer();


    public abstract Transformer getPlainTransformer();


    public abstract Transformer getXmlrpcTransformer();


    public abstract Transformer getXmlTransformer();


    
    public abstract String getXmlrpcType();

    
    
    public final static String INT = "int";
    public final static String BOOLEAN = "boolean";
    public final static String STRING = "string";
    public final static String DOUBLE = "double";
    public final static String DATE_TIME = "dateTime.iso8601";
    public final static String STRUCT = "struct";
    public final static String ARRAY = "array";
    public final static String BINARY = "base64";
}

/* 
 $Log: ResultTransformerSet.java,v $
 Revision 1.1  2005/02/22 01:10:31  nw
 enough of a prototype here to do a show-n-tell on.
 
 */