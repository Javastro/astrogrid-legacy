/*$Id: PropertiesConfig.java,v 1.4 2004/10/07 14:33:44 mch Exp $
 * Created on 27-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.jes.component;

import org.astrogrid.config.Config;
import org.astrogrid.config.PropertyNotFoundException;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Feb-2004
 *
 */
public class PropertiesConfig extends Config {
    /** Construct a new MapConfig
     *
     */
    public PropertiesConfig() {
        super();
        props = new Properties();
    }
    protected final Properties props;
    /**
     * @see org.astrogrid.config.Config#getProperty(java.lang.String)
     */
    public Object getProperty(String arg0) throws PropertyNotFoundException {
        Object o = props.get(arg0);
        if (o == null) {
            throw new PropertyNotFoundException("not found");
        }
        return o;
    }
    /**
     * @see org.astrogrid.config.Config#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(String arg0, Object arg1) {
        props.put(arg0,arg1);
    }

    /**
     * @see org.astrogrid.config.Config#getProperties(java.lang.String).
     * Unused in this implementation
     */
    public Object[] getProperties(String key) {
        throw new UnsupportedOperationException("Didn't think this was used");
    }

    
    /**
     * @see org.astrogrid.config.Config#loadUrl(java.net.URL)
     */
    public void loadFromUrl(URL arg0) throws IOException {
        props.load(arg0.openStream());
    }
    /**
     * @see org.astrogrid.config.Config#dumpConfig(java.io.Writer)
     */
    public void dumpConfig(Writer arg0) {
    }
    /**
     * @see org.astrogrid.config.Config#keySet()
     */
    public Set keySet() {
        return props.keySet();
    }
}


/*
$Log: PropertiesConfig.java,v $
Revision 1.4  2004/10/07 14:33:44  mch
Added new Config method

Revision 1.3  2004/04/06 08:29:55  nw
bugfix

Revision 1.2  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/
