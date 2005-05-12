/*$Id: RegisterConverters.java,v 1.4 2005/05/12 15:59:12 clq2 Exp $
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
import org.astrogrid.desktop.modules.system.converters.IvornConverter;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.picocontainer.Startable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * registers all the result converters used in the ag module.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *  
 */
public class RegisterConverters implements Startable {

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
        ConvertUtils.register(IvornConverter.getInstance(), Ivorn.class);
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

    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
    }

}

/*
 * $Log: RegisterConverters.java,v $
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