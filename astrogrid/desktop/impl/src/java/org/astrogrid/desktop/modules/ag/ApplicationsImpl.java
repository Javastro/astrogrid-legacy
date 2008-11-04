/*$Id: ApplicationsImpl.java,v 1.37 2008/11/04 14:35:47 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.common.bean.BaseBean;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.desktop.modules.ivoa.AdqlInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Implementation of {@code ApplicationsInternal}
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 31-Jan-2005
 * * @todo refine exception reporting.
 * @TEST mock up the underpinnings of this class.
 */
public class ApplicationsImpl implements ApplicationsInternal {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ApplicationsImpl.class);
    
    /** 
     * 
     */
    public ApplicationsImpl(final RemoteProcessManager manager,final FileSystemManager vfs, 
    		final RegistryInternal nuReg, final AdqlInternal adql) throws  ACRException{
        this.manager = manager;
        this.vfs = vfs;
        this.adql = adql;
        this.nuReg = nuReg;
        this.manipulator = new ToolManipulator();
       }
    
    protected final RegistryInternal nuReg;
    protected final RemoteProcessManager manager;
    private final FileSystemManager vfs;
    protected final AdqlInternal adql;
    protected final ToolManipulator manipulator;

   
    @Deprecated
    public URI[] list() throws ServiceException {   
        	final Resource[] arr = nuReg.xquerySearch(getRegistryXQuery()); 
            final URI[] results = new URI[arr.length];
            for (int i= 0;i < arr.length; i++) {
              results[i] = arr[i].getId();
            }
            return results;
    
    }
    

    
    @Deprecated
    public String getQueryToListApplications() {
        return getRegistryQuery();
    }
    
    @Deprecated
    public String getRegistryQuery() {
    	return getRegistryAdqlQuery();
    }
   
	@Deprecated
    public String getRegistryAdqlQuery() {
	     return "Select * from Registry r where " +
	     " (@xsi:type like '%CeaApplicationType')" ; //+
	     //@issue" and ( not( @status = 'inactive' or @status = 'deleted') )";
	}
	
	public String getRegistryXQuery() {
		return "//vor:Resource[(@xsi:type &= '*CeaApplication')" +
				" and ( not ( @status = 'inactive' or @status='deleted'))]";
/* KMB
		return "//RootResource[(matches(@xsi:type,'.*:CeaApplicationType') or matches(@xsi:type,'.*:CeaHttpApplicationType'))" +
		" and (@status = 'active')]";
*/
	}



    
    public CeaApplication getInfoForTool(final Tool t) throws ServiceException, NotFoundException, InvalidArgumentException {
        URI uri;
        try {
            uri = new URI(t.getName().startsWith("ivo://") ? t.getName() : "ivo://" + t.getName());
        } catch (final URISyntaxException e) {
            throw new InvalidArgumentException(e);
        }
        return getCeaApplication(uri);
    }
    
    /** duff method - time to remove it ? 
     * @deprecated*/
    @Deprecated
    public CeaApplication getCeaApplication(final URI arg0) throws ServiceException, NotFoundException, InvalidArgumentException {
        final Resource resource = nuReg.getResource(arg0);
        if (resource == null) {
            throw new NotFoundException(arg0.toString());
        }
        if (resource instanceof CeaApplication) { 
            return (CeaApplication)resource;
        } else {
            throw new InvalidArgumentException("Not a CEA Application " + arg0);
        }	

    }

    
 



 
    
    
  public String getDocumentation(final URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException {
	  final CeaApplication app = getCeaApplication(applicationName);
	  
    return getInfoFor(app);
  }
  

  private String getInfoFor(final CeaApplication descr) {      
        final StringBuffer result = new StringBuffer();
         result.append("Application: ")
                .append(descr.getTitle())
                .append("\n")
                .append(descr.getContent().getDescription())
                .append("\n");
         logger.debug("Added name and description");
         final ParameterBean[] params = descr.getParameters();
         for(int i = 0; i < params.length; i++ ) {
             final ParameterBean param = params[i];
            result.append("\nParameter ")
                .append("\n")
                .append(param.getUiName())
                .append("\n")
                .append(param.getDescription());

         
         if (param.getName() != null && param.getName().trim().length() > 0) {
            result.append("\n\t").append("name :").append(param.getName());
        }
         if (param.getType() != null ) {
            result.append("\n\t").append("type :").append(param.getType());
        }
         if (param.getSubType() != null && param.getSubType().trim().length() > 0) {
            result.append("\n\t").append("subtype :").append(param.getSubType());
        }
         if (param.getUnits() != null && param.getUnits().trim().length() > 0) {
            result.append("\n\t").append("units :").append(param.getUnits());
        }
         //if (param.getAcceptEncodings() != null && param.getAcceptEncodings().trim().length() > 0) result.append("\n\t").append("accept encodings :").append(param.getAcceptEncodings());
         if (param.getDefaultValue() != null && param.getDefaultValue().trim().length() > 0) {
            result.append("\n\t").append("default value :").append(param.getDefaultValue());
        }
         if (param.getUcd() != null && param.getUcd().trim().length() > 0) {
            result.append("\n\t").append("UCD :").append(param.getUcd());
        }  
         if (param.getOptions() != null ) {
            result.append("\n\t").append("option list :").append(Arrays.asList(param.getOptions()));
        }         

         }
         logger.debug("Added parameters");
           final InterfaceBean[] ifaces = descr.getInterfaces();
           for (int i = 0; i < ifaces.length; i++) {
               final InterfaceBean iface = ifaces[i];
               result.append("\nInterface ")
                   .append(iface.getName())
                   .append("\nInputs\n");
               ParameterReferenceBean[] prefs = iface.getInputs();
               for (int j = 0; j < prefs.length; j++) {
                   final ParameterReferenceBean p = prefs[j];
                   result.append("\t ").append(p.getRef()).append(" max ").append(p.getMax()).append(", min ").append(p.getMin()).append("\n");
               }
               result.append("\nOutputs\n");
               prefs = iface.getOutputs();
               for (int j = 0; j < prefs.length; j++) {
                   final ParameterReferenceBean p = prefs[j];
                   result.append("\t ").append(p.getRef()).append("max ").append(p.getMax()).append(", min ").append(p.getMin()).append("\n");
               }               
           }
         return result.toString();
    }    
   
  public Document createTemplateDocument(final URI applicationName, final String interfaceName) throws ServiceException, NotFoundException, InvalidArgumentException {
        try {
        	final CeaApplication descr = getCeaApplication(applicationName);
        final Tool t = createTemplateTool(interfaceName, descr);
        final Document doc = XMLUtils.newDocument();
        Marshaller.marshal(t,doc);
        return doc;

        } catch (final ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (final MarshalException e) {
            throw new ServiceException(e);
        } catch (final ValidationException e) {
            throw new ServiceException(e);
        }
    }
  


public Map createTemplateStruct(final URI applicationName, final String interfaceName)
    throws ServiceException, NotFoundException, InvalidArgumentException {
        final Document t = createTemplateDocument(applicationName,interfaceName);
        return convertDocumentToStruct(t);
    }
    //@todo test.
    public void validate(final Document document) throws ServiceException, InvalidArgumentException {
        Tool t;
        try {
            t = (Tool)Unmarshaller.unmarshal(Tool.class,document);
            t.validate();
        } catch (final ValidationException e) {
            throw new InvalidArgumentException(e);
        } catch (final MarshalException e) {
            throw new InvalidArgumentException(e);
        }
        CeaApplication desc;
        try {
        final URI uri = new URI("ivo://" + t.getName());
        desc = getCeaApplication(uri);
        } catch(final URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (final NotFoundException e) {
            throw new InvalidArgumentException(e);
        }
        InterfaceBean iface = null;
        final InterfaceBean[] ifs = desc.getInterfaces();
        for (int i = 0 ; i < ifs.length; i++) {
            if (ifs[i].getName().equals(t.getInterface())) {
                iface = ifs[i];
                break;
            }
        }
        if (iface == null) {
            throw new InvalidArgumentException("Interface " + t.getInterface() + " not found");
        }
        // checkeac parameter against the interface.
            ParameterReferenceBean[] refs = iface.getInputs();            
            BaseBean searchRoot = t.getInput();
            for (int i = 0; i < refs.length; i++) {
                validateReference(refs[i],searchRoot);
            }
            // now the outputs.
            refs = iface.getOutputs();
            searchRoot = t.getOutput();
            for (int i = 0; i < refs.length; i++) {
                validateReference(refs[i],searchRoot);
            }           
    }

    private void validateReference( final ParameterReferenceBean bean, final BaseBean searchRoot) throws InvalidArgumentException {
        final Iterator results = searchRoot.findXPathIterator("/parameter[name = '" + bean.getRef() +"']");        
//unused        ParameterBean pb = (ParameterBean)desc.getParameters().get(bean.getRef());
        int count = 0;
        while (results.hasNext()) {
            count++;
            results.next();
        }
        //fix for bz2158
        if (count < bean.getMin() || (bean.getMax() > 0 && count > bean.getMax())) {
            throw new InvalidArgumentException("Parameter " + bean.getRef() + " occurs " + count +". Should occur between " 
                    + bean.getMin() + " and " + (bean.getMax() == 0 ? "unlimited" : Integer.toString(bean.getMax())) + " times");
        }
    }
        

    public void validateStored(final URI documentLocation) throws ServiceException, InvalidArgumentException, NotFoundException {
        Document doc;
        try {
            final InputStream r = vfs.resolveFile(documentLocation.toString()).getContent().getInputStream();
            doc = XMLUtils.newDocument(r);
  
        } catch (final ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (final SAXException e) {
            throw new InvalidArgumentException(e);
        } catch (final IOException e) {
            throw new NotFoundException(e);
        } 
        validate(doc);
        

    }
   

    //@todo return service info for other kinds of server.
	public Service[] listServersProviding(final URI arg0) throws ServiceException, NotFoundException, InvalidArgumentException {
		final CeaApplication c = getCeaApplication(arg0);
		if (c instanceof Service) { // it's already a service - this is a 'fake' cea application on a different protocol.
			return new Service[]{(Service)c};
		}
			
		final Resource[] res =  nuReg.xquerySearch("//vor:Resource[not (@status='inactive' or @status='deleted') " +
	
				"and capability[@xsi:type &= '*CeaCapability' or @standardID='"
		        + StandardIds.CEA_1_0
//		        +"']/managedApplications/ApplicationReference='"+ arg0 +"']");
                +"']/managedApplications/*='"+ arg0 +"']");  // work around for pauls cosmetic changes to schema.
		        final List result = new ArrayList();
		// check ttypes.
		for (int i = 0; i < res.length; i++) {
			if (res[i] instanceof CeaService) {
				result.add(res[i]);
			}
		}
		return (CeaService[])result.toArray(new CeaService[result.size()]);
	}           
    

    @Deprecated
    public URI submitStored(final URI documentLocation) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException {
        return manager.submitStored(documentLocation);       
    }
           
    @Deprecated
    public URI submitStoredTo(final URI documentLocation, final URI server) throws InvalidArgumentException, ServiceException, SecurityException, NotFoundException {
        return manager.submitStoredTo(documentLocation,server);    
    } 
    

    @Deprecated
    public URI submit(final Document doc) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
       return manager.submit(doc);
    }
    
    @Deprecated
    public URI submitTo(final Document doc, final URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException {
      return manager.submitTo(doc,server); 
    }   
    
  
    
    /**
     * @param application
     * @param document
     * @throws ServiceException
     */
    public void translateQueries(final Resource application, final Tool document) throws  ServiceException {
        // fiddle any embedded queries..
        final String[] adqlParameters = ToolManipulator.listADQLParameters(document.getInterface(),application);
        if (adqlParameters.length > 0) {            
            for (int i = 0; i < adqlParameters.length; i++) {
                final ParameterValue val = (ParameterValue) document.findXPathValue("input/parameter[name='" + adqlParameters[i] + "']");
                if (!val.getIndirect()) { // it's an inline value
                    final InputStream is = new ByteArrayInputStream(val.getValue().getBytes());
                    // try parsing the value as xml
                    try {
                        XMLUtils.newDocument(is);
                    } catch (final Exception e) { // aha - got a string query.
						try {
							final String adqlx = adql.s2xs(val.getValue());
							val.setValue(adqlx);
						} catch (final InvalidArgumentException x) {
							throw new ServiceException(x);
						}
                    }
                }               
            }
        }
    }

 
 
    @Deprecated
    public void cancel(final URI executionId) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        manager.halt(executionId);
    }  

    @Deprecated
    public ExecutionInformation getExecutionInformation(final URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException {
        return manager.getExecutionInformation(executionId);
    }
    @Deprecated
    public Map getResults(final URI executionId) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        return manager.getResults(executionId);
    }



	public Map convertDocumentToStruct(final Document arg0)
            throws InvalidArgumentException {
        return DocumentToStructureConversion.convertDocumentToStruct(arg0);
    }



    public Document convertStructToDocument(final Map arg0)
            throws InvalidArgumentException {
        return DocumentToStructureConversion.convertStructToDocument(arg0);
    }



    public Tool createTemplateTool(final String interfaceName, final CeaApplication descr)
            throws IllegalArgumentException {
        return this.manipulator.createTemplateTool(interfaceName, descr);
    }







}


/* 
$Log: ApplicationsImpl.java,v $
Revision 1.37  2008/11/04 14:35:47  nw
javadoc polishing

Revision 1.36  2008/10/30 11:53:50  nw
Complete - taskpaul cockup.

Revision 1.35  2008/10/23 16:34:02  nw
Incomplete - taskadd support for TAP

Revision 1.34  2008/08/04 16:37:24  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

Revision 1.33  2008/07/18 17:15:52  nw
Complete - task 433: Strip out unused internal CEA

Revision 1.32  2008/06/06 13:39:58  nw
refactored applicationsImpl in prep for reinstating CDS tasks.

Revision 1.31  2008/04/23 10:51:44  nw
marked as needing test.

Revision 1.30  2008/03/13 07:19:17  nw
fix for 2632

Revision 1.29  2008/01/30 08:38:38  nw
Incomplete - task 313: Digest registry upgrade.

RESOLVED - bug 2526: voexdesktop help needs to point to beta.astrogrid.org
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2526
Incomplete - task 314: get login working again.

Revision 1.28  2008/01/25 07:53:25  nw
Complete - task 134: Upgrade to reg v1.0

Revision 1.27  2008/01/21 09:53:58  nw
Incomplete - task 134: Upgrade to reg v1.0

Revision 1.26  2007/08/30 23:46:47  nw
Complete - task 73: upgrade filechooser dialogue to new fileexplorer code
replaced uses of myspace by uses of vfs where sensible

Revision 1.25  2007/07/23 12:15:50  nw
got adql translation working in tool document submission

Revision 1.24  2007/07/12 10:17:42  nw
task runner ui

Revision 1.23  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

Revision 1.21  2007/03/08 17:44:04  nw
first draft of voexplorer

Revision 1.20  2007/01/29 10:57:16  nw
moved cache configuration into hivemind.

Revision 1.19  2007/01/19 19:55:16  jdt
Move flush cache to the public interface.   It's currently in the IVOA module, which is probably not the right place.  *Not tested*  I can't test because Eclipse seems to be getting confused with the mixture of JDKs 1.4 and 1.5.

Revision 1.18  2007/01/09 16:20:45  nw
use linkedhashmap instead of map

Revision 1.17  2006/09/15 14:37:50  nw
added caching to applications - prevents workflow builder freezups.

Revision 1.16  2006/08/31 21:28:59  nw
doc fix.

Revision 1.15  2006/08/15 10:15:59  nw
migrated from old to new registry models.

Revision 1.14  2006/06/27 19:11:09  nw
adjusted todo tags.

Revision 1.13  2006/06/27 10:23:51  nw
findbugs tweaks

Revision 1.12  2006/05/26 15:22:30  nw
organized imports.

Revision 1.11  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.10  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.8.34.3  2006/04/14 02:45:01  nw
finished code.
extruded plastic hub.

Revision 1.8.34.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.8.34.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.9  2006/03/13 18:27:34  nw
fixed queries to not restrict to @status='active'

Revision 1.8  2005/11/11 17:53:27  nw
added cea polling to lookout.

Revision 1.7  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.6  2005/10/18 16:53:03  nw
deprecated a badly-named method

Revision 1.5  2005/10/17 16:02:10  nw
improved query to find apps in reg

Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.10  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.9  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.8  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.7  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.6  2005/06/08 14:51:59  clq2
1111

Revision 1.3.8.4  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.3.8.3  2005/05/12 15:49:22  nw
litte lix

Revision 1.3.8.2  2005/05/12 12:42:48  nw
finished core applications functionality.

Revision 1.3.8.1  2005/05/12 01:14:33  nw
got applications component half working.

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 10:54:36  nw
added missing methods to vospace.
made a start at getting applications working again.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/