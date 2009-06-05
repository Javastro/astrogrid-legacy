/*
 * $Id: AbstractNamespaceMapper.java,v 1.1 2009/06/05 13:06:20 pah Exp $
 * 
 * Created on 5 Jun 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.jaxb;

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
 * Revision 1.1  2009/06/05 13:06:20  pah
 * RESOLVED - bug 2921: add capabilities to the automatic registration
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2921
 * marshalling of capabilities and namespace changes
 *
 */
