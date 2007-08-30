/*$Id: ApplicationsImpl.java,v 1.26 2007/08/30 23:46:47 nw Exp $
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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

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
import org.astrogrid.desktop.modules.ivoa.AdqlInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl.KnowledgeAddingResourceArrayBuilder;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Application service.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 31-Jan-2005
 * * @todo refine exception reporting.
 */
public class ApplicationsImpl implements ApplicationsInternal {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ApplicationsImpl.class);
    
    /** 
     * 
     */
    public ApplicationsImpl(RemoteProcessManager manager,FileSystemManager vfs, 
    		RegistryInternal nuReg, AdqlInternal adql,
    		Ehcache applicationResourceCache) throws  ACRException{
        this.manager = manager;
        this.vfs = vfs;
        this.adql = adql;
        this.nuReg = nuReg;
        this.applicationResourceCache = applicationResourceCache;
       }
    protected final Ehcache applicationResourceCache;
    protected final RegistryInternal nuReg;
    protected final RemoteProcessManager manager;
    private final FileSystemManager vfs;
    protected final AdqlInternal adql;

   
    public URI[] list() throws ServiceException {   
        	Resource[] arr = nuReg.xquerySearch(getRegistryXQuery()); 
            URI[] results = new URI[arr.length];
            for (int i= 0;i < arr.length; i++) {
              results[i] = arr[i].getId();
            }
            return results;
    
    }
    

    
    public String getQueryToListApplications() {
        return getRegistryQuery();
    }
    
    public String getRegistryQuery() {
    	return getRegistryAdqlQuery();
    }
   
	public String getRegistryAdqlQuery() {
	     return "Select * from Registry where " +
	     " (@xsi:type like '%CeaApplicationType' or " +
	     " @xsi:type like '%CeaHttpApplicationType')" ; //+
	     //@issue" and ( not( @status = 'inactive' or @status = 'deleted') )";
	}
	
	public String getRegistryXQuery() {
		return "//vor:Resource[(@xsi:type &= '*CeaApplicationType' or @xsi:type &= '*CeaHttpApplicationType')" +
				" and ( not ( @status = 'inactive' or @status='deleted'))]";
/* KMB
		return "//RootResource[(matches(@xsi:type,'.*:CeaApplicationType') or matches(@xsi:type,'.*:CeaHttpApplicationType'))" +
		" and (@status = 'active')]";
*/
	}



    
    public CeaApplication getInfoForTool(Tool t) throws ServiceException, NotFoundException, InvalidArgumentException {
        URI uri;
        try {
            uri = new URI(t.getName().startsWith("ivo://") ? t.getName() : "ivo://" + t.getName());
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        }
        return getCeaApplication(uri);
    }
    
	public CeaApplication getCeaApplication(URI arg0) throws ServiceException, NotFoundException, InvalidArgumentException {
		net.sf.ehcache.Element el = applicationResourceCache.get(arg0);
		if (el != null) {
			return (CeaApplication)el.getValue();
		} else {
		KnowledgeAddingResourceArrayBuilder proc = new KnowledgeAddingResourceArrayBuilder();

		nuReg.getResourceStream(arg0,proc );
		Resource[] arr = proc.getResult();
		if (arr == null || arr.length < 1) {
			throw new NotFoundException(arg0.toString());
		}
		Resource r = arr[0];
		if (r instanceof CeaApplication) { // anything else that can be made to look like a cea app - cone, siap, etc, will be packaged as one too.
			applicationResourceCache.put(new Element(arg0,r));
			return (CeaApplication)r;
		} else {
			throw new InvalidArgumentException("Not a recognized kind of application: " + arg0);
		}			
		}
			
	}

    
 



 
    
    
  public String getDocumentation(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException {
	  return getInfoFor(getCeaApplication(applicationName));
  }
  

  private String getInfoFor(CeaApplication descr) {      
        StringBuffer result = new StringBuffer();
         result.append("Application: ")
                .append(descr.getTitle())
                .append("\n")
                .append(descr.getContent().getDescription())
                .append("\n");
         logger.debug("Added name and description");
         ParameterBean[] params = descr.getParameters();
         for(int i = 0; i < params.length; i++ ) {
             ParameterBean param = params[i];
            result.append("\nParameter ")
                .append("\n")
                .append(param.getUiName())
                .append("\n")
                .append(param.getDescription());

         
         if (param.getName() != null && param.getName().trim().length() > 0) result.append("\n\t").append("name :").append(param.getName());
         if (param.getType() != null ) result.append("\n\t").append("type :").append(param.getType());
         if (param.getSubType() != null && param.getSubType().trim().length() > 0) result.append("\n\t").append("subtype :").append(param.getSubType());
         if (param.getUnits() != null && param.getUnits().trim().length() > 0) result.append("\n\t").append("units :").append(param.getUnits());
         //if (param.getAcceptEncodings() != null && param.getAcceptEncodings().trim().length() > 0) result.append("\n\t").append("accept encodings :").append(param.getAcceptEncodings());
         if (param.getDefaultValue() != null && param.getDefaultValue().trim().length() > 0) result.append("\n\t").append("default value :").append(param.getDefaultValue());
         if (param.getUcd() != null && param.getUcd().trim().length() > 0) result.append("\n\t").append("UCD :").append(param.getUcd());  
         if (param.getOptions() != null ) result.append("\n\t").append("option list :").append(Arrays.asList(param.getOptions()));         

         }
         logger.debug("Added parameters");
           InterfaceBean[] ifaces = descr.getInterfaces();
           for (int i = 0; i < ifaces.length; i++) {
               InterfaceBean iface = ifaces[i];
               result.append("\nInterface ")
                   .append(iface.getName())
                   .append("\nInputs\n");
               ParameterReferenceBean[] prefs = iface.getInputs();
               for (int j = 0; j < prefs.length; j++) {
                   ParameterReferenceBean p = prefs[j];
                   result.append("\t ").append(p.getRef()).append(" max ").append(p.getMax()).append(", min ").append(p.getMin()).append("\n");
               }
               result.append("\nOutputs\n");
               prefs = iface.getOutputs();
               for (int j = 0; j < prefs.length; j++) {
                   ParameterReferenceBean p = prefs[j];
                   result.append("\t ").append(p.getRef()).append("max ").append(p.getMax()).append(", min ").append(p.getMin()).append("\n");
               }               
           }
         return result.toString();
    }    
   
  public Document createTemplateDocument(URI applicationName, String interfaceName) throws ServiceException, NotFoundException, InvalidArgumentException {
        try {
        	CeaApplication descr = getCeaApplication(applicationName);
        Tool t = createTemplateTool(interfaceName, descr);
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(t,doc);
        return doc;

        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (MarshalException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
  


public Tool createTemplateTool(String interfaceName, CeaApplication descr) throws IllegalArgumentException {
    if (interfaceName != null && (! interfaceName.equals("default"))) {
        InterfaceBean[] ifaces = descr.getInterfaces();
        InterfaceBean iface = findInterface(interfaceName, ifaces);
        if (iface == null) {
            throw new IllegalArgumentException("Cannot find interface named " + interfaceName );
        }
         return createTool(descr,iface);
    } else {
        return createTool(descr,null);
    }
}



public static InterfaceBean findInterface(String interfaceName, InterfaceBean[] ifaces) {
	InterfaceBean iface = null;
	for (int i = 0; i < ifaces.length; i++) {
	    if (ifaces[i].getName().equals(interfaceName)) {
	        iface =ifaces[i];
	    }
	}
	return iface;
}

private Tool createTool(CeaApplication descr,InterfaceBean iface) {
    if (iface == null) {
        iface = descr.getInterfaces()[0];
    }
    Tool t = new Tool();
    t.setInterface(iface.getName());
    t.setName(descr.getId().getAuthority() + descr.getId().getPath()); //@todo should I drop the 'ivo' part.? - yes. for now. until cea accept this
    Input input = new Input();
    Output output = new Output();
    t.setInput(input);
    t.setOutput(output);
    
    // populate inputs
    ParameterReferenceBean[] refs = iface.getInputs();
    ParameterBean[] parameters = descr.getParameters();
    ParameterValue[] arr = new ParameterValue[refs.length];
    for (int i = 0; i < refs.length; i++) {
        ParameterBean pb =findParameter(parameters,refs[i].getRef());
        arr[i] = createParameterValue(pb);         
    }
    input.setParameter(arr);
    
    // populate outputs.
    refs = iface.getOutputs();
    arr = new ParameterValue[refs.length];
    for (int i = 0; i < refs.length; i++) {
        ParameterBean pb =findParameter(parameters,refs[i].getRef());
        arr[i] = createParameterValue(pb);
        
    }   
    output.setParameter(arr);
    
    // fill in the blanks.
    return t;
}

public static ParameterBean findParameter(ParameterBean[] arr,String name) {
	for (int i =0; i < arr.length; i++) {
		if (arr[i].getName().equals(name)){
			return arr[i];
		}
	}
	return null;
}

  
  private ParameterValue createParameterValue(ParameterBean pb) {
      ParameterValue pv = new ParameterValue();
      pv.setName(pb.getName());
      if (pb.getDefaultValue()!= null) {
          pv.setValue(pb.getDefaultValue());
      } else {
          pv.setValue("");
      }
      return pv;
      
  }
    
    public Map createTemplateStruct(URI applicationName, String interfaceName)
    throws ServiceException, NotFoundException, InvalidArgumentException {
        Document t = createTemplateDocument(applicationName,interfaceName);
        return convertDocumentToStruct(t);
    }
    
    public Map convertDocumentToStruct(Document doc) throws InvalidArgumentException {
        
        Tool document;
        try {
            document = (Tool)Unmarshaller.unmarshal(Tool.class,doc);    
        Map result = new LinkedHashMap();
        result.put("interface",document.getInterface());
        result.put("name",document.getName());
        Hashtable inputs= new Hashtable();
        Hashtable outputs = new Hashtable();
        result.put("input",inputs);
        for (int i = 0; i < document.getInput().getParameterCount(); i++) {
            convertParameterToHash(inputs, document.getInput().getParameter(i));
        }
        result.put("output",outputs);
        for (int i = 0; i < document.getOutput().getParameterCount(); i++) {
            convertParameterToHash(outputs, document.getOutput().getParameter(i));
        }        
        return result;
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }        
    }
    
    private void convertParameterToHash(Hashtable collection,ParameterValue v) {
        Hashtable result = new Hashtable();
        collection.put(v.getName(),result);
        result.put("value",v.getValue());
        result.put("indirect",Boolean.valueOf( v.getIndirect()));
    }
    
    private ParameterValue convertHashToParameter(String name,Map h) {
        ParameterValue v= new ParameterValue();
        v.setName(name);
        v.setIndirect(((Boolean)h.get("indirect")).booleanValue());
        v.setValue(h.get("value").toString());
        return v;
    }

    private ParameterValue[] convertParams(Map inputHash) {
        ParameterValue[] arr = new ParameterValue[inputHash.size()];
        Iterator i = inputHash.entrySet().iterator();
        for (int ix = 0; ix < arr.length; ix++) {
            Map.Entry e =(Map.Entry) i.next();
            arr[ix] =convertHashToParameter(e.getKey().toString(),(Map)e.getValue());                        
        }
        return arr;
    }
    
    public Document convertStructToDocument(Map struct) throws InvalidArgumentException {
        Tool t = new Tool();
        t.setName(struct.get("name").toString());
        t.setInterface(struct.get("interface").toString());
        org.astrogrid.workflow.beans.v1.Input input = new org.astrogrid.workflow.beans.v1.Input();
        
        Map paramHash= (Map)struct.get("input");
        input.setParameter(convertParams(paramHash));
        t.setInput(input);
        
        Output output = new Output();
        paramHash = (Map)struct.get("output");               
       output.setParameter(convertParams(paramHash));
        t.setOutput(output);

        try {
            Document doc = XMLUtils.newDocument();
            Marshaller.marshal(t,doc);
            return doc;
        } catch (ParserConfigurationException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) { 
            throw new InvalidArgumentException(e);
        }
    }
    //@todo test.
    public void validate(Document document) throws ServiceException, InvalidArgumentException {
        Tool t;
        try {
            t = (Tool)Unmarshaller.unmarshal(Tool.class,document);
            t.validate();
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        }
        CeaApplication desc;
        try {
        URI uri = new URI("ivo://" + t.getName());
        desc = getCeaApplication(uri);
        } catch(URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (NotFoundException e) {
            throw new InvalidArgumentException(e);
        }
        InterfaceBean iface = null;
        InterfaceBean[] ifs = desc.getInterfaces();
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

    private void validateReference( ParameterReferenceBean bean, BaseBean searchRoot) throws InvalidArgumentException {
        Iterator results = searchRoot.findXPathIterator("/parameter[name = '" + bean.getRef() +"']");        
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
        

    public void validateStored(URI documentLocation) throws ServiceException, InvalidArgumentException, NotFoundException {
        Document doc;
        try {
            InputStream r = vfs.resolveFile(documentLocation.toString()).getContent().getInputStream();
            doc = XMLUtils.newDocument(r);
  
        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (SAXException e) {
            throw new InvalidArgumentException(e);
        } catch (IOException e) {
            throw new NotFoundException(e);
        } 
        validate(doc);
        

    }
   

    //@todo return service info for other kinds of server.
	public Service[] listServersProviding(URI arg0) throws ServiceException, NotFoundException, InvalidArgumentException {
		CeaApplication c = getCeaApplication(arg0);
		if (c instanceof Service) { // it's already a service - this is a 'fake' cea application on a different protocol.
			return new Service[]{(Service)c};
		}
			
		Resource[] res =  nuReg.xquerySearch("//vor:Resource[not (@status='inactive' or @status='deleted') " +
		//KMB 	Resource[] res =  nuReg.xquerySearch("//RootResource[@status='active' " +
	
				"and cea:ManagedApplications/cea:ApplicationReference='"+ arg0 +"']");
		List result = new ArrayList();
		// check ttypes.
		for (int i = 0; i < res.length; i++) {
			if (res[i] instanceof CeaService) {
				result.add(res[i]);
			}
		}
		return (CeaService[])result.toArray(new CeaService[result.size()]);
	}           
    

    public URI submitStored(URI documentLocation) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException {
        return manager.submitStored(documentLocation);       
    }
           
    public URI submitStoredTo(URI documentLocation, URI server) throws InvalidArgumentException, ServiceException, SecurityException, NotFoundException {
        return manager.submitStoredTo(documentLocation,server);    
    } 
    

    public URI submit(Document doc) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
       return manager.submit(doc);
    }
    
    public URI submitTo(Document doc, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException {
      return manager.submitTo(doc,server); 
    }   
    
  
    
    /**
     * @param application
     * @param document
     * @throws ServiceException
     */
    public void translateQueries(Resource application, Tool document) throws  ServiceException {
        // fiddle any embedded queries..
        String[] adqlParameters = listADQLParameters(document.getInterface(),application);
        if (adqlParameters.length > 0) {            
            for (int i = 0; i < adqlParameters.length; i++) {
                ParameterValue val = (ParameterValue) document.findXPathValue("input/parameter[name='" + adqlParameters[i] + "']");
                if (!val.getIndirect()) { // it's an inline value
                    InputStream is = new ByteArrayInputStream(val.getValue().getBytes());
                    // try parsing the value as xml
                    try {
                        XMLUtils.newDocument(is);
                    } catch (Exception e) { // aha - got a string query.
						try {
							String adqlx = adql.s2xs(val.getValue());
							val.setValue(adqlx);
						} catch (InvalidArgumentException x) {
							throw new ServiceException(x);
						}
                    }
                }               
            }
        }
    }

 
 
    public void cancel(URI executionId) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        manager.halt(executionId);
    }  

    public ExecutionInformation getExecutionInformation(URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException {
        return manager.getExecutionInformation(executionId);
    }
    public Map getResults(URI executionId) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        return manager.getResults(executionId);
    }



	/** returns true if this app has an adql parameter */
	public static String[] listADQLParameters(String interfaceName,Resource r) {
		if (! (r instanceof CeaApplication)) {
			return new String[0];
		}
		CeaApplication info = (CeaApplication)r;
	    InterfaceBean ib = null;
	    List results = new ArrayList();
	    for (int i = 0; i < info.getInterfaces().length; i++) {        
	        if (info.getInterfaces()[i].getName().equals(interfaceName)) {
	            ib = info.getInterfaces()[i];
	        }            
	    }
	    if (ib == null) {
	        return new String[]{};
	    }
	    ParameterBean[] arr = info.getParameters();
	    for (int i =0; i < ib.getInputs().length; i++) {
	        ParameterReferenceBean prb = ib.getInputs()[i];
	        ParameterBean pb = findParameter(arr,prb.getRef());
	        if (pb ==null) {
	            return new String[]{};
	        }
	        if (pb.getType() != null && pb.getType().equalsIgnoreCase("adql")) {
	            results.add(pb.getName());
	        }
	    }
	    return (String[])results.toArray(new String[results.size()]);
	    
	}







}


/* 
$Log: ApplicationsImpl.java,v $
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