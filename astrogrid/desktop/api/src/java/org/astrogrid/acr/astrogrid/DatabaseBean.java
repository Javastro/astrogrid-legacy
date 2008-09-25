/*$Id: DatabaseBean.java,v 1.7 2008/09/25 16:02:04 nw Exp $
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

import org.astrogrid.acr.ivoa.resource.Catalog;

/** describes a  single database in a TablularDB registry entry
 * @exclude 
 * @deprecated prefer the 'Catalog' type instead.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-Sep-2005
 */
@Deprecated
public class DatabaseBean extends Catalog{

    public DatabaseBean(final String name,final String description, final TableBean[] tables) {
        super();
        this.name = name;
        this.description = description;
        this.tables = tables;
    }
    
    static final long serialVersionUID = -3221050086290430194L;
  
}


/* 
$Log: DatabaseBean.java,v $
Revision 1.7  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.6  2007/03/08 17:46:56  nw
removed deprecated interfaces.

Revision 1.5  2007/01/24 14:04:44  nw
updated my email address

Revision 1.4  2006/06/15 09:01:27  nw
provided implementations of equals()

Revision 1.3  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.2.6.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2.6.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/