/*$Id: ResultBuilderType.java,v 1.1 2003/11/18 11:48:15 nw Exp $
 * Created on 02-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.builder;


/** Enumeration of available result builders
 * 
 * <p>
 * @todo - extend range of types available - i.e. arrays of things?
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Oct-2003
 *
 */
public class ResultBuilderType {

    /**
     * 
     */
    private ResultBuilderType() {
    }

       public static ResultBuilder createResultBuilder(String type) {
                    if (ResultBuilderType.BYTES.equalsIgnoreCase(type)) {
                          return new BytesBuilder();
                      }
                      if (ResultBuilderType.ELEMENT.equalsIgnoreCase(type)) {
                          return new ElementBuilder();
                      }
                      if (ResultBuilderType.FLOAT.equalsIgnoreCase(type)) {
                          return new FloatBuilder();
                      }
                      if (ResultBuilderType.INT.equalsIgnoreCase(type)) {
                          return new IntBuilder();
                      }
                      if (ResultBuilderType.STRING.equalsIgnoreCase(type)) {
                          return new StringBuilder();
                      }     
                      if (BOOLEAN.equalsIgnoreCase(type)) {
                          return new BooleanBuilder();
                      }
                      if (VOID.equalsIgnoreCase(type)) {
                          return new VoidBuilder();
                      }                                      
                  throw new IllegalArgumentException("Result Object: Unknown Type");
            }
    
    public final static String BYTES = "bytes";
    public final static String ELEMENT = "element";
    public final static String FLOAT = "float";
    public final static String INT = "int";
    public final static String STRING = "string";
    public final static String BOOLEAN = "boolean";
    public final static String VOID = "void";

}


/* 
$Log: ResultBuilderType.java,v $
Revision 1.1  2003/11/18 11:48:15  nw
mavenized http2java

Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/