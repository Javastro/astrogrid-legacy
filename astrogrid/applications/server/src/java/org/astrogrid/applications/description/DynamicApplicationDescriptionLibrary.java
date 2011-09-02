/*
 * $Id: DynamicApplicationDescriptionLibrary.java,v 1.2 2011/09/02 21:55:49 pah Exp $
 * 
 * Created on 8 Oct 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;

import junit.framework.Test;

import org.astrogrid.applications.contracts.Configuration;
import org.xml.sax.InputSource;

/**
 * An {@link ApplicationDescriptionLibrary} that allows its content to be dynamically redefined.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 8 Oct 2008
 * @version $Name:  $
 * @since VOTech Stage 8
 */
public class DynamicApplicationDescriptionLibrary extends
        ConfigFileReadingDescriptionLibrary {

    private String dynappDescr;
    public DynamicApplicationDescriptionLibrary(BaseApplicationDescriptionFactory df) {
        super(df);
    }

    @Override
    public String getName() {
       return "DynamicApplicationDescriptionLibrary";
    }
    
    public boolean addApplicationDescription(String string){
        
        dynappDescr = string;
        InputSource is = new InputSource(new StringReader(string));

        return loadApplications(is);
    }

    @Override
    public Test getInstallationTest() {
      return null; //should not try to test 
    }
    
    public void saveDescription(String location) throws IOException
    {
        
        File out = new File(location);
        out.createNewFile();
        Writer wr = new FileWriter(out);
        wr.write(dynappDescr);
        wr.close();
       
        
    }
    
}


/*
 * $Log: DynamicApplicationDescriptionLibrary.java,v $
 * Revision 1.2  2011/09/02 21:55:49  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.4.3  2011/09/02 19:38:47  pah
 * change setup of dynamic description library
 *
 * Revision 1.1.4.2  2010/02/05 12:56:06  pah
 * RESOLVED - bug 2974: create application page does not save
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2974
 *
 * Revision 1.1.4.1  2009/07/15 09:48:12  pah
 * redesign of parameterAdapters
 *
 * Revision 1.1  2008/10/09 11:47:27  pah
 * add dynamic app description library & refactor more funtionality to base class
 *
 */
