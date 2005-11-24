/*$Id: VotableResultTransformerSet.java,v 1.2 2005/11/24 01:13:24 nw Exp $
 * Created on 23-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Nov-2005
 *
 */
public class VotableResultTransformerSet extends DocumentResultTransformerSet {

    /** Construct a new VotableResultTransformerSet
     * 
     */
    public VotableResultTransformerSet() {
        super();
        setHtmlTransformer(Votable2XhtmlTransformer.getInstance());
    }

}


/* 
$Log: VotableResultTransformerSet.java,v $
Revision 1.2  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.1.2.1  2005/11/23 04:43:32  nw
added transformer set for votable results.
 
*/