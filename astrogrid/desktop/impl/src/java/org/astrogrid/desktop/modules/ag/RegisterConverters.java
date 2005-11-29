/*$Id: RegisterConverters.java,v 1.4 2005/11/29 11:28:05 nw Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.desktop.modules.system.converters.CastorBeanUtilsConvertor;
import org.astrogrid.desktop.modules.system.converters.URIConverter;
import org.astrogrid.desktop.modules.system.converters.VectorConvertor;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.picocontainer.Startable;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

/**
 * registers all the result converters used in the ag module.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *  
 */
public class RegisterConverters implements IRegisterConverters, Startable {

    /**
     * Construct a new RegisterConverters
     *  
     */
    public RegisterConverters() {
        super();
    }

    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        ConvertUtils.register(URIConverter.getInstance(),URI.class);
        ConvertUtils.register(CastorBeanUtilsConvertor.getInstance(), Workflow.class);
        ConvertUtils.register(new Converter() {

            public Object convert(Class arg0, Object arg1) {
                if (arg0 != JobURN.class) {
                    throw new RuntimeException("Can only convert to JobURNs " + arg0.getName());
                }
                JobURN urn = new JobURN();
                urn.setContent(arg1.toString());
                return urn;
            }
        }, JobURN.class);
        ConvertUtils.register(CastorBeanUtilsConvertor.getInstance(), Tool.class);
        ConvertUtils.register(new Converter() {

            public Object convert(Class arg0, Object arg1) {
                if (arg0 != URL.class) {
                    throw new RuntimeException("Can only convert to URLs " + arg0.getName());
                }
                try {
                    return new URL(arg1.toString());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }, URL.class);

        ConvertUtils.register(new Converter() {

            public Object convert(Class arg0, Object arg1) {
               if (arg0 != Document.class) { 
                   throw new RuntimeException("Can only convert to documents " + arg0.getName());
               }
               ByteArrayInputStream is= new ByteArrayInputStream(arg1.toString().getBytes());
               try {
                return XMLUtils.newDocument(is);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
                   
            }
        },Document.class);
        ConvertUtils.register(VectorConvertor.getInstance(), Vector.class);        
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
    }

}

/*
 * $Log: RegisterConverters.java,v $
 * Revision 1.4  2005/11/29 11:28:05  nw
 * refactored converters
 *
 * Revision 1.3  2005/09/12 15:21:16  nw
 * reworked application launcher. starting on workflow builder
 *
 * Revision 1.2  2005/08/25 16:59:58  nw
 * 1.1-beta-3
 *
 * Revision 1.1  2005/08/11 10:15:00  nw
 * finished split
 *
 * Revision 1.6  2005/08/09 17:33:07  nw
 * finished system tests for ag components.
 *
 * Revision 1.5  2005/08/05 11:46:55  nw
 * reimplemented acr interfaces, added system tests.
 *
 * Revision 1.4  2005/05/12 15:59:12  clq2
 * nww 1111 again
 *
 * Revision 1.2.20.1  2005/05/11 14:25:24  nw
 * javadoc, improved result transformers for xml
 *
 * Revision 1.2  2005/04/13 12:59:11  nw
 * checkin from branch desktop-nww-998
 *
 * Revision 1.1.2.2  2005/03/30 12:48:22  nw
 * added two more lttle modules
 *
 * Revision 1.1.2.1  2005/03/22 12:04:03  nw
 * working draft of system and ag components.
 *
 */