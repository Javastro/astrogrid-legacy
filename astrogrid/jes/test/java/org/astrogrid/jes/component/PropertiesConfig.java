/*$Id: PropertiesConfig.java,v 1.1 2004/03/03 01:13:42 nw Exp $
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
import java.net.URL;
import java.util.Properties;

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
     * @see org.astrogrid.config.Config#loadUrl(java.net.URL)
     */
    public void loadFromUrl(URL arg0) throws IOException {
        props.load(arg0.openStream());
    }
}


/* 
$Log: PropertiesConfig.java,v $
Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/