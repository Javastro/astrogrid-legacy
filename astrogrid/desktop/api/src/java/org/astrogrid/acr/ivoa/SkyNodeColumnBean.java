/*$Id: SkyNodeColumnBean.java,v 1.1 2006/02/24 12:17:52 nw Exp $
 * Created on 22-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ivoa;

import org.astrogrid.acr.astrogrid.ColumnBean;
/** extension of column bean for sky node services, which provide further metadata
 * @since 1.9
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Feb-2006
 *
 */
public class SkyNodeColumnBean extends ColumnBean {

    public SkyNodeColumnBean(String name, String description, String ucd, String datatype, String unit, int precision, int byteSize, int rank) {
        super(name, description, ucd, datatype, unit);
        this.precision = precision;
        this.byteSize = byteSize;
        this.rank = rank;
    }
    

    //additional metadata provided by skynode..
    private final int precision;
    private final int byteSize;
    private final int rank;
    
    /** access bytesize.
     */
    public int getByteSize() {
        return this.byteSize;
    }
    /** access precision
     * 
     * @return
     */
    public int getPrecision() {
        return this.precision;
    }
    /** access rank of this column */
    public int getRank() {
        return this.rank;
    }

}


/* 
$Log: SkyNodeColumnBean.java,v $
Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/