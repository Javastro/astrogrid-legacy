/*$Id: GroovyTest.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
 * Created on 07-Dec-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting.groovy;

import org.codehaus.groovy.runtime.ScriptTestAdapter;

import java.io.File;
import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import groovy.util.GroovyTestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
public class GroovyTest extends TestSuite  {


    protected GroovyClassLoader loader = new GroovyClassLoader(GroovyTest.class.getClassLoader());

    public static void main(String[] args) {

        TestRunner.run(suite());
    }

    public void loadTestSuite() throws Exception {
          String fileName = new File(new URI(this.getClass().getResource("test1.groovy").toString())).getAbsolutePath();
        
        Class type = compile(fileName);
            String[] args = {};
            if (!Test.class.isAssignableFrom(type) && Script.class.isAssignableFrom(
    type)) {
                addTest(new ScriptTestAdapter(type, args));
            }
            else {
                addTestSuite(type);
            }       
    }
    
    public static Test suite() {
        GroovyTest suite = new GroovyTest();
        try {
            suite.loadTestSuite();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not create the test suite: " + e,
e);
        }
        return suite;
    }
    
    public Class compile(String fileName) throws Exception {
        return loader.parseClass(new File(fileName));
    }
    

    
}


/* 
$Log: GroovyTest.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/