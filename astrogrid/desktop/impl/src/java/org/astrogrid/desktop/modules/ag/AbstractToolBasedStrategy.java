/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.workflow.beans.v1.Tool;
import org.w3c.dom.Document;

/** Base class for remote process strategies which
 * use the Tool document as their representation
 * (never mind whether this is actually submitted to the remote service
 * )
 * 
 *
 * 
 * provides some utility methods, etc.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 23, 20081:04:40 PM
 */
public abstract class AbstractToolBasedStrategy implements
        RemoteProcessStrategy {
    private static final Log logger = LogFactory.getLog(AbstractToolBasedStrategy.class);
    protected final Registry reg;
    
    /**
     * @param reg
     */
    public AbstractToolBasedStrategy(final Registry reg) {
        super();
        this.reg = reg;
    }



    
    /** readymade implementation of canProcess(Document)
     * which assumes that document is to be parsed as a tool,
     * then the name extracted, and checked in the registry to 
     * see what type of resource it is.
     * @param inst
     * @return value suitable for returning straight from canProcess
     */
    protected final String canProcessSupport(final Document doc, final Class instanceOf ) {
        try {
            final Tool t = CeaHelper.parseTool(doc);   
            final URI id = CeaHelper.getResourceId(t);
            final Resource resource = reg.getResource(id);
            if ( instanceOf.isInstance(resource)){
                return t.getName();
            } else {
                return null;
            }
        } catch (final NotFoundException x) {
            logger.debug("Not found",x);           
        } catch (final ServiceException x) {
            logger.warn("Unable to query registry",x);
        } catch(final InvalidArgumentException e) {
            // means it's not a tool document.
            logger.debug(e);
        }         
        return null;
    }
    
    protected final boolean canProcessSupport(final URI globalExecId,final Class instanceOf) {
        logger.debug(globalExecId);
        try {
            final Resource resource = reg.getResource(extractServiceID(globalExecId));
            return instanceOf.isInstance(resource);
        } catch (final NotFoundException x) {
            logger.debug(globalExecId + ": not found",x);
        } catch (final ServiceException x) {
            logger.warn("Unable to query registry",x);
        }     
        return false;
    }
    
    /** construct a globale exec id by concatenating
     * the service ivorn:// with the exec Id emitted by that service
     * @param localExecId
     * @param service
     * @return
     */
    protected final URI mkGlobalExecId(final String localExecId,final Service service) {
        try {
            final URI id = service.getId();
            return new URI(id.getScheme(),id.getSchemeSpecificPart(),localExecId);
        } catch (final URISyntaxException e) {
            throw new ProgrammerError("Invalid URI made by mangling",e);
        }
    }
    
    /** from a global execId, extract the ivo:// of the server */
    protected final URI extractServiceID(final URI globalExecId) {
        try {
            return new URI(globalExecId.getScheme(),globalExecId.getSchemeSpecificPart(),null);
        } catch (final URISyntaxException e) {
            throw new ProgrammerError("Invalid URI made by mangling",e);            
        }
        
    }
    
    /** from a global execId, extract the local execution Id */
    protected final String extractLocalExecId(final URI globalExecId) {
        return globalExecId.getFragment();
    }
    
}
