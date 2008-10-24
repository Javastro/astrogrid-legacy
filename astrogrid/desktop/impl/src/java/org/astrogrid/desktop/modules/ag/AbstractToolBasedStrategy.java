/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunityPolicyException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
            final Tool t = AbstractToolBasedStrategy.parseTool(doc);   
            final URI id = AbstractToolBasedStrategy.getResourceId(t);
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
    
    
    // mangling myspace paths.
    /**
     * Creates a deep copy of the tool converting identifiers for MySpace locations to the concrete form.
     * Abstract form is the account IVORN with the MySpace path added
     * as a URI fragment. Concrete form is the IVORN of the services hosting
     * the space with the MySpace path added as a URI fragment.
     */
  protected Tool makeMySpaceIvornsConcrete(final Tool intool) throws InvalidArgumentException {
          Tool tool = null;
          try {
          final Node node = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
          Marshaller.marshal(intool, node);
          tool = (Tool)Unmarshaller.unmarshal(Tool.class, node);
          final Input input = tool.getInput();
          for (int i = 0; i < input.getParameterCount(); i++) {
              final ParameterValue p = input.getParameter(i);
              makeMySpaceIvornsConcrete(p);
          }

          final Output output = tool.getOutput();
          for (int i = 0; i < output.getParameterCount(); i++) {
              final ParameterValue p = output.getParameter(i);
              makeMySpaceIvornsConcrete(p);
          }
          } catch (final Exception ex) {
          throw new InvalidArgumentException("Failed to make VOSpace references concrete", ex);
          }
          return tool;
      }
    
    /**
     * Makes the IVORN of an indirect parameter concrete.
     * If the given parameter is indirect, its value may be
     * an IVORN denoting a location in MySpace. If so, then
     * the IVORN may be either concrete (based on the IVORN
     * for the service hosting the space) or abstract (based
     * on the name of the account owning the space. This method
     * detects indirect parameters with IVORN values and changes
     * the values to be concrete IVORNs; this involves a transaction
     * with registry and one with the community service.
     *
     * @throws URISyntaxException If the indirect-parameter value is not a valid URI.
     * @throws CommunityServiceException If the community service fails to satisfy an information request.
     * @throws CommunityIdentifierException If the parameter value is in scheme ivo:// but is invalid.
     * @throws CommunityResolverException If the client-side resolver-library cannot parse the IVORN.
     * @throws RegistryException If the community indicated in the IVORN cannnot be found in the registry.
     */
    private void makeMySpaceIvornsConcrete(final ParameterValue p) 
        throws URISyntaxException, 
               CommunityServiceException, 
               CommunityIdentifierException, 
               CommunityPolicyException, 
               CommunityResolverException, 
               RegistryException {
      if (p.getIndirect()) {
        final String value = p.getValue();
        if (value != null && value.startsWith("ivo://")) {
            final CommunityAccountSpaceResolver resolver = new CommunityAccountSpaceResolver();
          final Ivorn ivorn1 = new Ivorn(value);
          final Ivorn ivorn2 = resolver.resolve(ivorn1);
          p.setValue(ivorn2.toString());
          logger.info(ivorn1 + " was resolved to " + ivorn2);
        }
      }
    }




    /** parse a document into a tool, performing any necessary adjustments */
    public static Tool parseTool(final Document doc) throws InvalidArgumentException{
    	try {
    	final Tool tool = (Tool)Unmarshaller.unmarshal(Tool.class, doc);
    	// munge name in document, if incorrect..       
    	// The application name is supposed to be an IVOID without the
    	// ivo:// prefix. Strip the prefix if it is present.
    	if (tool.getName().startsWith("ivo://")) {
    		tool.setName(tool.getName().substring(6));
    	}
    	return tool;
    	} catch (final MarshalException e) {
    		throw new InvalidArgumentException(e);
    	} catch (final ValidationException e) {
    		throw new InvalidArgumentException(e);
    	}
    }




    /** get the resource Id correctly from a tool document 
     * @throws InvalidArgumentException */
    public static URI getResourceId(final Tool t) throws InvalidArgumentException {
        try {
            return new URI("ivo://" + t.getName());
        } catch (final URISyntaxException e) {
            throw new InvalidArgumentException("Failed to construct a valid resourceID from " + t.getName(),e);
        }
    }
    
}
