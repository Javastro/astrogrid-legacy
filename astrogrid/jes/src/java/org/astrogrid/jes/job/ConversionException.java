/*$Id: ConversionException.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 25-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.job;

import org.astrogrid.jes.JesException;

/** Exception type thrown when marshalling to xml
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2004
 *
 */
public class ConversionException extends JesException {
    /** Construct a new ConversionException
     * @param s
     */
    public ConversionException(String s) {
        super(s);
    }
    /** Construct a new ConversionException
     * @param s
     * @param e
     */
    public ConversionException(String s, Exception e) {
        super(s, e);
    }
}


/* 
$Log: ConversionException.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:24:43  nw
refined tool interface
 
*/