/*$Id: DeprecatedSesameDelegate.java,v 1.1 2004/10/05 19:19:18 mch Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.impl.cds.sesame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.impl.cds.generated.sesame.Sesame;
import org.astrogrid.datacenter.impl.cds.generated.sesame.SesameService;
import org.astrogrid.datacenter.impl.cds.generated.sesame.SesameServiceLocator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Delegate that calls the deprecated methods of the sesame web service.
 * just tests each method, checks the result isn't null. uncomment printlns to see output.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 * @todo - add parsing / recognition of result string, to detect failure to find anything.
 */
public class DeprecatedSesameDelegate {


    public DeprecatedSesameDelegate() throws ServiceException {
       SesameService serv = new SesameServiceLocator();
       ses = serv.getSesame();
    }
    
    public DeprecatedSesameDelegate(String endpoint) throws MalformedURLException, ServiceException {
        this(new URL(endpoint));
    }
    
    public DeprecatedSesameDelegate(URL endpoint) throws ServiceException {
        SesameService serv = new SesameServiceLocator();
        ses = serv.getSesame(endpoint);
    }
    
    
    protected Sesame ses;
    
    public String resolveName(String name) throws RemoteException {
        String result = ses.sesame(name);
        return result;
    }
    
    public Document resolveNameAsXML(String name) throws ParserConfigurationException, SAXException, IOException {
        String result = ses.sesameXML(name);
        InputStream is = new ByteArrayInputStream(result.getBytes());
        Document doc = XMLUtils.newDocument(is);
        return doc;
    }

}


/*
$Log: DeprecatedSesameDelegate.java,v $
Revision 1.1  2004/10/05 19:19:18  mch
Merged CDS implementation into PAL

Revision 1.2  2003/11/20 15:47:18  nw
improved testing

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/
