/*
 * @(#)VOTableFactory.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.votable ;

import org.astrogrid.datacenter.config.Configurable;
import org.astrogrid.datacenter.query.Query;
import java.io.OutputStream;

/** a factory for VOTable objects must implememnt this interface */
public interface VOTableFactory extends Configurable {
   /** TODO document me */
    void stream( Query query, OutputStream outStream ) throws VOTableException ;
   /** TODO document me */
    VOTable createVOTable( Query query ) throws VOTableException ;

}
