/*$Id: AggregatePicoContainer.java,v 1.2 2005/04/13 12:59:12 nw Exp $
 * Created on 17-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.PicoVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** picocontainer that aggregates together a bunch of picocontainers (or modules) into one whole.
 * 
 * uses jakarta commons CollectionsUtils to make the iteration over the set of aggregated containers more concise.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Mar-2005
 *
 */
public class AggregatePicoContainer implements PicoContainer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AggregatePicoContainer.class);

    /** Construct a new AggregatePicoContainer
     * 
     */
    public AggregatePicoContainer(Set l) {
        this.picos = l;
    }
    
    public AggregatePicoContainer(Collection c) {
        this.picos = new HashSet(c);
        logger.debug("Aggregating " + picos.size());
    }
    
    protected final Set picos;
    /**
     * @see org.picocontainer.PicoContainer#getComponentInstance(java.lang.Object)
     */
    public Object getComponentInstance(final Object key) {
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentInstance(key = " + key + ") - start");
        }
        
        StoringPredicate p = new StoringPredicate() {
            protected Object project(Object o) {

                Object returnObject = ((PicoContainer) o).getComponentInstance(key);
  
                return returnObject;
            }
        };        
        CollectionUtils.find(picos,p);

        if (logger.isDebugEnabled()) {
            logger.debug("getComponentInstance() - end");
        }
        return p.result;
    }
    
    private abstract static class StoringPredicate implements Predicate {

        public Object result;
        public final boolean evaluate(Object o) {

            Object r = project(o);
            if (r != null ) {
                logger.debug("Found " + r);
                result = r;
            }
            boolean returnboolean = result == null;
            return returnboolean;             
        }
        protected abstract Object project(Object o);
    }

    /**
     * @see org.picocontainer.PicoContainer#getComponentInstanceOfType(java.lang.Class)
     */
    public Object getComponentInstanceOfType(final Class arg0) {
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentInstanceOfType(arg0 = " + arg0 + ") - start");
        }

        StoringPredicate p = new StoringPredicate() {
            protected Object project(Object o) {
                if (logger.isDebugEnabled()) {
                    logger.debug("project(o = " + o + ") - start");
                }

                Object returnObject = ((PicoContainer) o).getComponentInstanceOfType(arg0);
                if (logger.isDebugEnabled()) {
                    logger.debug("project() - end");
                }
                return returnObject;
            }
        };        
        CollectionUtils.find(picos,p);

        if (logger.isDebugEnabled()) {
            logger.debug("getComponentInstanceOfType() - end");
        }
        return p.result;
    }

    /**
     * @see org.picocontainer.PicoContainer#getComponentInstances()
     */
    public List getComponentInstances() {
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentInstances() - start");
        }

        final List result = new ArrayList();
        CollectionUtils.forAllDo(picos,new Closure() {
            public void execute(Object arg0) {

                result.addAll( ((PicoContainer)arg0).getComponentInstances());
            }
        });

        if (logger.isDebugEnabled()) {
            logger.debug("getComponentInstances() - end");
        }
        return result;                
    }

    /**
     * @see org.picocontainer.PicoContainer#getParent()
     */
    public PicoContainer getParent() {
        if (logger.isDebugEnabled()) {
            logger.debug("getParent() - start");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getParent() - end");
        }
        return null;
    }

    /**
     * @see org.picocontainer.PicoContainer#getComponentAdapter(java.lang.Object)
     */
    public ComponentAdapter getComponentAdapter(final Object arg0) {
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentAdapter(arg0 = " + arg0 + ") - start");
        }

        StoringPredicate p = new StoringPredicate() {
            protected Object project(Object o) {
                Object returnObject = ((PicoContainer) o).getComponentAdapter(arg0);

                return returnObject;
            }
        };        
        CollectionUtils.find(picos,p);
        ComponentAdapter returnComponentAdapter = (ComponentAdapter) p.result;
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentAdapter() - end");
        }
        return returnComponentAdapter;
    }

    /**
     * @see org.picocontainer.PicoContainer#getComponentAdapterOfType(java.lang.Class)
     */
    public ComponentAdapter getComponentAdapterOfType(final Class arg0) {
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentAdapterOfType(arg0 = " + arg0 + ") - start");
        }

        StoringPredicate p = new StoringPredicate() {
            protected Object project(Object o) {

                Object returnObject = ((PicoContainer) o).getComponentAdapterOfType(arg0);
                if (logger.isDebugEnabled() && returnObject != null) {
                    logger.debug("Found " + returnObject);
                }
                return returnObject;
            }
        };        
        CollectionUtils.find(picos,p);
        ComponentAdapter returnComponentAdapter = (ComponentAdapter) p.result;
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentAdapterOfType() - end" + returnComponentAdapter);
        }
        return returnComponentAdapter;
    }

    /**
     * @see org.picocontainer.PicoContainer#getComponentAdapters()
     */
    public Collection getComponentAdapters() {
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentAdapters() - start");
        }

        final List result = new ArrayList();
        CollectionUtils.forAllDo(picos,new Closure() {
            public void execute(Object arg0) {


                result.addAll( ((PicoContainer)arg0).getComponentAdapters());

            }
        });

        if (logger.isDebugEnabled()) {
            logger.debug("getComponentAdapters() - end");
        }
        return result;        
    }

    /**
     * @see org.picocontainer.PicoContainer#getComponentAdaptersOfType(java.lang.Class)
     */
    public List getComponentAdaptersOfType(final Class clazz) {
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentAdaptersOfType(clazz = " + clazz + ") - start");
        }

        final List result = new ArrayList();
        CollectionUtils.forAllDo(picos,new Closure() {
            public void execute(Object arg0) {


                result.addAll( ((PicoContainer)arg0).getComponentAdaptersOfType(clazz));
            }
        });

        if (logger.isDebugEnabled()) {
            logger.debug("getComponentAdaptersOfType() - end");
        }
        return result;        
    }

    /**
     * @see org.picocontainer.PicoContainer#verify()
     */
    public void verify() throws PicoVerificationException {
        if (logger.isDebugEnabled()) {
            logger.debug("verify() - start");
        }

        CollectionUtils.forAllDo(picos,new Closure() {
            public void execute(Object arg0) {
                ((PicoContainer)arg0).verify();
            }
        });

        if (logger.isDebugEnabled()) {
            logger.debug("verify() - end");
        }
    }

    /**
     * @see org.picocontainer.PicoContainer#getComponentInstancesOfType(java.lang.Class)
     */
    public List getComponentInstancesOfType(final Class clazz) throws PicoException {
        if (logger.isDebugEnabled()) {
            logger.debug("getComponentInstancesOfType(clazz = " + clazz + ") - start");
        }

        final List result = new ArrayList();
        CollectionUtils.forAllDo(picos,new Closure() {
            public void execute(Object arg0) {
                result.addAll( ((PicoContainer)arg0).getComponentInstancesOfType(clazz));
            }
        });

        if (logger.isDebugEnabled()) {
            logger.debug("getComponentInstancesOfType() - end");
        }
        return result;   
    }

    /**
     * @see org.picocontainer.PicoContainer#accept(org.picocontainer.PicoVisitor)
     */
    public void accept(final PicoVisitor visitor) {
        if (logger.isDebugEnabled()) {
            logger.debug("accept(visitor = " + visitor + ") - start");
        }

        CollectionUtils.forAllDo(picos,new Closure() {

            public void execute(Object arg0) {

                ((PicoContainer)arg0).accept(visitor);;

            }
        });        

        if (logger.isDebugEnabled()) {
            logger.debug("accept() - end");
        }
    }

    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        if (logger.isDebugEnabled()) {
            logger.debug("start() - start");
        }

        CollectionUtils.forAllDo(picos,new Closure() {
            public void execute(Object arg0) {

                ((PicoContainer)arg0).start();
            }
        });            

        if (logger.isDebugEnabled()) {
            logger.debug("start() - end");
        }
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        if (logger.isDebugEnabled()) {
            logger.debug("stop() - start");
        }

        CollectionUtils.forAllDo(picos,new Closure() {
            public void execute(Object arg0) {
                ((PicoContainer)arg0).stop();
            }
        });                  

        if (logger.isDebugEnabled()) {
            logger.debug("stop() - end");
        }
    }

    /**
     * @see org.picocontainer.Disposable#dispose()
     */
    public void dispose() {
        if (logger.isDebugEnabled()) {
            logger.debug("dispose() - start");
        }

        CollectionUtils.forAllDo(picos,new Closure() {
            public void execute(Object arg0) {


                ((PicoContainer)arg0).dispose();
            }
        });                  

        if (logger.isDebugEnabled()) {
            logger.debug("dispose() - end");
        }
    }

}


/* 
$Log: AggregatePicoContainer.java,v $
Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.4  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.3  2005/04/01 19:03:10  nw
beta of job monitor

Revision 1.1.2.2  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/