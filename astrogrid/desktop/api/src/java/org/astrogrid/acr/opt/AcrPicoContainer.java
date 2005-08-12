/*$Id: AcrPicoContainer.java,v 1.2 2005/08/12 08:45:16 nw Exp $
 * Created on 10-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.opt;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.PicoVisitor;
import org.picocontainer.defaults.AbstractComponentAdapter;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.CachingComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** A Picocontainer that exports services of the ACR .
 * <p>
 * A picocontainer is a very small IoC container that takes care of instantiating classes registered with it. In particular, it passes
 * dependencies into constructors, and works out the order that various classes need to be instantiated. This leads to a 
 * style of programming called <i>constructor-injection</i>, which removes the need to retreive objects in-code - they are passed in by the container, and so 
 * can be guaranteed to be present at object creation.
 * 
 * <p>
 * I don't think I explained that very well, you'll just have to trust that it's a good thing to do. Try it.
 * <p>
 * So this class makes an ACR appear like a picocontainer - so client classes can be instantiated and the service objects they depend on will be passed
 * in at construction. This works both when directly linked to an ACR, and when the link is over JavaRMI.
 * <p>
 * Picocontainers are used within the ACR implementation to manage the service objects - so this class would be useful when developing plugins that are intended to 
 * be either run in-process or external to the ACR.
 * 
 * @example
 * <pre>
 * import org.astrogrid.acr.system.*;
 * import org.astrogrid.acr.astrogrid.*;
 * import org.astrogrid.acr.*;
 * import org.astrogrid.acr.builtin.*;
 * import org.picocontainer.*;
 * import org.picocontainer.defaults.*;
 * 
 *  Finder f = new Finder();
     ACR acr = f.find();
     PicoContainer acrPico = new AcrPicoContainer(acr); // creates an immutable container, from which acr services can be accessed
     MutablePicoContainer pico = new DefaultPicoContainer(acrPico); // create a mutable container which has the acrPico as it's parent.
     pico.registerComponentImplementation(MyClass.class);
     pico.start();
     ....
      public static MyClass implements Startable {
         public MyClass(Configuration c, Myspace m) {
           this.c = c;
           this.m = m; 
           }
         private final Configuration c;
         private final Myspace m;
         public void start() {
            // do something with m and c.
             }
        public void stop() {
          // do something else
          }
                 
 * </pre>
 * 
 * @see http://www.picocontainer.org
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Aug-2005
 *
 */
public class AcrPicoContainer implements PicoContainer {
/**
 * A component adapter that wraps a service provided by the ACR
 *<p>
 *Used in the <tt>AcrPicoContainer</tt>, but may also be used in other picocontainer implementations
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Aug-2005
 *
 */
    public static class AcrComponentAdapter extends AbstractComponentAdapter {
        private final ACR acr;

        /** Construct a new AcrComponentAdapter
         * @param arg1 the service interface
         * @param acr the acr that is to provide this interface.
         * @throws AssignabilityRegistrationException
         */
        public AcrComponentAdapter( Class arg1, ACR acr) throws AssignabilityRegistrationException {
            super(arg1, arg1);
            this.acr = acr;
        }

        public Object getComponentInstance(PicoContainer arg0) throws PicoInitializationException, PicoIntrospectionException {
            try {
                return acr.getService((Class)getComponentKey());
            } catch (InvalidArgumentException e) {
                throw new PicoInitializationException(e);
            } catch (NotFoundException e) {
                throw new PicoInitializationException(e);
            } catch (ACRException e) {
                throw new PicoInitializationException(e);
            }
        }

        public void verify(PicoContainer arg0) throws PicoIntrospectionException {
        }
    }

    /** Construct a new AcrPicoContainer
     * @param acr the ACR from which to retreive services.
     * 
     */
    public AcrPicoContainer(ACR acr) {
        super();
        this.acr = acr;
    }
    
    protected final ACR acr;

    public Object getComponentInstance(Object arg0) {
        if (arg0.getClass() != Class.class) {
            throw new PicoInitializationException("Cannot get instances from keys other than classes");
        }
        return getComponentInstanceOfType((Class)arg0);
    }

    public Object getComponentInstanceOfType(Class arg0) {
        try {
            return acr.getService(arg0);
        } catch (InvalidArgumentException e) {
            return null;
        } catch (NotFoundException e) {
            return null;
        } catch (ACRException e) {
            throw new PicoInitializationException(e);
        }
    }

    public List getComponentInstances() {
        throw new PicoIntrospectionException("Can't list component instances");
    }
    public List getComponentInstancesOfType(Class arg0) throws PicoException {
        List l = new ArrayList();
        try {
            Object o = acr.getService(arg0);
            l.add(o);
        } catch (InvalidArgumentException e) {
            // do nothing.
        } catch (NotFoundException e) {
            // fair enough, do nothing.
        } catch (ACRException e) {
            throw new PicoInitializationException(e);
        }
        return l;
    }    

    public PicoContainer getParent() {
        // no parent.
        return null;
    }

    public ComponentAdapter getComponentAdapter(Object arg0) {
        if (arg0.getClass() != Class.class) {
            throw new PicoInitializationException("Cannot get adapters from keys other than classes");
        }
        return getComponentAdapterOfType((Class)arg0);        
    }

    public ComponentAdapter getComponentAdapterOfType(Class arg0) {
        return new CachingComponentAdapter(new AcrComponentAdapter( arg0, acr));
    }

    public Collection getComponentAdapters() {
        throw new PicoIntrospectionException("Can't list component instances");
    }

    public List getComponentAdaptersOfType(Class arg0) {
        List l = new ArrayList();
        l.add(getComponentAdapterOfType(arg0));
        return l;
    }

    public void verify() throws PicoVerificationException {
        // do nothing.
    }


    public void accept(PicoVisitor arg0) {
        // do nothing
    }

    public void start() {
        // do nothing
    }

    public void stop() {
        // do nothing
    }

    public void dispose() {
        // do nothing
    }

}


/* 
$Log: AcrPicoContainer.java,v $
Revision 1.2  2005/08/12 08:45:16  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:01  nw
finished split
 
*/