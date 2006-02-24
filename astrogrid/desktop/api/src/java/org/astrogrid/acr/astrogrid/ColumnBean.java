/*$Id: ColumnBean.java,v 1.3 2006/02/24 12:17:52 nw Exp $
 * Created on 12-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.io.Serializable;

/** describes a single column in a tabular database.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Sep-2005
 * @since 1.2
 */
public class ColumnBean implements Serializable{

    /** Construct a new ColumnInformation
     * 
     */
    public ColumnBean(String name,String description,String ucd, String datatype,String unit) {
        super();
        this.name = name;
        this.description = description;
        this.UCD = ucd;
        this.datatype = datatype;
        this.unit = unit;
                
    }
    
    private final String name;
    private final String description;
    private final String UCD;
    private final String datatype;
    private final String unit;

    /**
     * the type of this column     
     */
    public String getDatatype() {
        return this.datatype;
    }
/**
 * human-readable description of this column
 */
    public String getDescription() {
        return this.description;
    }
    /** name of this column */
    public String getName() {
        return this.name;
    }
    /** Universal column descriptor for this column */
    public String getUCD() {
        return this.UCD;
    }
    /** description of theunits of this column */
    public String getUnit() {
        return this.unit;
    }
}


/* 
$Log: ColumnBean.java,v $
Revision 1.3  2006/02/24 12:17:52  nw
added interfaces for skynode

Revision 1.2  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/