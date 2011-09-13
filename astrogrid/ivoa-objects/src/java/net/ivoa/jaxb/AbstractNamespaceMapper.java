/*
 * $Id: AbstractNamespaceMapper.java,v 1.2 2011/09/13 13:43:33 pah Exp $
 * 
 * Created on 5 Jun 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa.jaxb;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import org.astrogrid.contracts.Namespaces;

public abstract class AbstractNamespaceMapper extends NamespacePrefixMapper {

    @Override
    public final String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        String retval;
        Namespaces namespace = Namespaces.getNameSpaceFromURI(namespaceUri);
        if ((namespace != null)) {
            retval = namespace.getPrefix();
        } else
        {
            retval = suggestion;
        }
        return retval;
    }

  
}


/*
 * $Log: AbstractNamespaceMapper.java,v $
 * Revision 1.2  2011/09/13 13:43:33  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.1  2011/06/09 22:18:56  pah
 * basic VOResource schema nearly done - but not got save/recall working
 *
 *
 */
