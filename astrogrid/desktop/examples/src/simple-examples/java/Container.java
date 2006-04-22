import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.opt.AcrPicoContainer;
import org.astrogrid.acr.system.Configuration;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.Iterator;
import java.util.Map;

/*$Id: Container.java,v 1.1 2006/04/22 12:41:55 nw Exp $
 * Created on 13-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Jan-2006
 *advanced - example of instantiating a component in a picocontainer, where dependencies are supplied from the remote acr.

requires acr client jars in groovy classpath
connects to acr using rmi access -
 */
public class Container {
//  sample component
    public static class MyComponent implements Runnable {
        public MyComponent(Configuration c) { //has a dependency on the configuration service. obviously could have many more dependencies here.
            System.out.println( "component instantiated");
            this.c =c;
        }
        private final Configuration c;
        public void run() {
            try {
            System.out.println("component running");
            Map l = c.list();
            for (Iterator i = l.entrySet().iterator(); i.hasNext(); ) {
                System.out.println(i.next());
            }
            } catch (ACRException e) {
                e.printStackTrace();
            }
        }   
    }


    public static void main(String[] args) {
        try {
            Finder f = new Finder();
            ACR acr = f.find();
                        
//          create the acr-pico container.
            PicoContainer cont = new AcrPicoContainer(acr);
//             create a normal container as a child of this one (work-around), and register a component with it.
            MutablePicoContainer mut = new DefaultPicoContainer(cont);
            mut.registerComponentImplementation(MyComponent.class);

//             retrieve an instance of that component, and run it.
            System.out.println( "retrieving component instance");
            Runnable r =  (Runnable)mut.getComponentInstance(MyComponent.class);
            r.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
//      shut the app down - necessary, as won't close by itself.
        System.exit(0);        
    }
}


/* 
$Log: Container.java,v $
Revision 1.1  2006/04/22 12:41:55  nw
refactored workbench into ar.

Revision 1.1  2006/01/13 15:44:22  nw
new set of examples.
 
*/