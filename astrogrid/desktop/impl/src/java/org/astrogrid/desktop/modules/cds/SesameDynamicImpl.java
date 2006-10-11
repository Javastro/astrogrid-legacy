/*$Id: SesameDynamicImpl.java,v 1.5 2006/10/11 10:38:39 nw Exp $
 * Created on 28-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.cds;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axis.client.Service;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
/**
 * implementaiton of the sesame client, using dynamic webservice invocation - i.e.
 * without using marshalling beans
 * 
 * nice simple xml.rpc style interface - so can call through rpc api - next to no messing with soap.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Feb-2006
 *
 */
public class SesameDynamicImpl implements Sesame {

    public SesameDynamicImpl() {
        super();
        service = new Service();
        inputFactory = XMLInputFactory.newInstance(); 
       
    }
    protected final Service service;
    protected final XMLInputFactory inputFactory;
    protected  String endpoint = DEFAULT_ENDPOINT;
    public final static String DEFAULT_ENDPOINT =  "http://cdsws.u-strasbg.fr/axis/services/Sesame";

    public void setEndpoint(String e) {
        this.endpoint = e;
    }
    
    public String sesame(String arg0, String arg1) throws ServiceException {
    try {
        Call call = (Call)service.createCall();
        call.setTargetEndpointAddress(endpoint);
        call.setOperationName(new QName("urn:Sesame","sesame"));
        return (String)call.invoke(new Object[]{arg0,arg1});
    } catch (javax.xml.rpc.ServiceException e) {
        throw new ServiceException(e);
    } catch (RemoteException e) {
        throw new ServiceException(e);
    }
    }

    public String sesameChooseService(String arg0, String arg1, boolean arg2, String arg3)
            throws ServiceException {
        try {
            Call call = (Call)service.createCall();
            call.setTargetEndpointAddress(endpoint);
            call.setOperationName(new QName("urn:Sesame","sesame"));
            return (String)call.invoke(new Object[]{arg0,arg1,Boolean.valueOf(arg2),arg3});
        } catch (javax.xml.rpc.ServiceException e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }        
    }

	public SesamePositionBean resolve(String arg0) throws ServiceException, NotFoundException {
		String response = this.sesame(arg0,"xi");
		SesamePositionBean result = new SesamePositionBean();
		List infoList = new ArrayList();
		Set aliases = new HashSet();
		try {
			XMLStreamReader is = inputFactory.createXMLStreamReader(new StringReader(response));
			for (is.next(); ! (is.isEndElement() && is.getLocalName().equals("Sesame")); is.next()) {
				if (is.isStartElement()) { // otherwise, we don't care.
					String elementName = is.getLocalName();
					if (elementName.equals("target")) {
						result.setTarget(is.getElementText());
					} else if (elementName.equals("Resolver")) {
						// reset the position bean, only copying across the target field.
						String target = result.getTarget();
						result = new SesamePositionBean();
						result.setTarget(target);
						result.setService(is.getAttributeValue(null,"name"));
					} else if (elementName.equals("INFO")) { // confusingly, also used to indicate an error..
						// add to message list..
						infoList.add(result.getService() + ": " + is.getElementText());
					} else if (elementName.equals("otype")) {
						result.setOType(is.getElementText());
					} else if (elementName.equals("jpos")) {
						result.setPosStr(is.getElementText());
					} else if (elementName.equals("jradeg")) {
						try{
							result.setRa(Double.parseDouble(is.getElementText()));
						} catch (NumberFormatException e) {
							infoList.add(result.getService() + ": Failed to parse jradeg");
						}
					} else if (elementName.equals("jdedeg")) {
						try{
							result.setDec(Double.parseDouble(is.getElementText()));
						} catch (NumberFormatException e) {
							infoList.add(result.getService() + ": Failed to parse jdedeg");
						}						
					} else if (elementName.equals("errRAmas")) {
						try{
							result.setRaErr(Double.parseDouble(is.getElementText()));
						} catch (NumberFormatException e) {
							infoList.add(result.getService() + ": Failed to parse errRAmas");
						}						
					} else if (elementName.equals("errDEmas")) {
						try{
							result.setDecErr(Double.parseDouble(is.getElementText()));
						} catch (NumberFormatException e) {
							infoList.add(result.getService() + ": Failed to parse errDEmas");
						}						
					} else if (elementName.equals("oname")) {
						result.setOName(is.getElementText());
					} else if (elementName.equals("alias")) {
						aliases.add(is.getElementText());
					}
				}
			}
		} catch (XMLStreamException x) {
			throw new ServiceException("Failed to parse response from Sesame",x);
		}
		// we've either got a resilt with a valid position, or we need to throw a not found exception..
		if (result.getPosStr() == null) {
			throw new NotFoundException(infoList.toString());
		}
		result.setAliases((String[])aliases.toArray(new String[aliases.size()]));
		return result;
	}
  
}


/* 
$Log: SesameDynamicImpl.java,v $
Revision 1.5  2006/10/11 10:38:39  nw
added response parsing.

Revision 1.4  2006/06/27 10:26:25  nw
findbugs tweaks

Revision 1.3  2006/06/15 09:46:47  nw
removed cruft, added schema for result vaidation.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development
 
*/