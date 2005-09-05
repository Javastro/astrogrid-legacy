/*$Id: QueryEditorImpl.java,v 1.1 2005/09/05 11:08:39 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

import java.awt.Component;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/** Implementaiton of the query editor component.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 *
 */
public class QueryEditorImpl implements QueryEditorInternal {

    /** Construct a new QueryEditorImpl
     * 
     */
    public QueryEditorImpl(       Configuration conf, HelpServer help, UIInternal ui,
            RegistryChooser regChooser, Registry reg,
            ApplicationsInternal apps,
            ResourceChooserInternal resourceChooser, MyspaceInternal myspace) {
        super();
        this.myspace = myspace;
        dialog= new QueryEditorDialog(conf,help,ui,regChooser,reg,apps,resourceChooser,myspace);
        dialog.pack();
        //dialog.setSize ...
    }

    private final QueryEditorDialog dialog;
    private final MyspaceInternal myspace;
    /**
     * @see org.astrogrid.desktop.modules.dialogs.QueryEditorInternal#editTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public Tool editTool(Tool t, Component comp) throws InvalidArgumentException, ServiceException {
       dialog.populate(t);
       dialog.setLocationRelativeTo(comp);
       dialog.setVisible(true);
       return dialog.getTool();
    }


    /**
     * @see org.astrogrid.desktop.modules.dialogs.QueryEditorInternal#editStoredTool(java.net.URI, java.awt.Component)
     */
    public Tool editStoredTool(URI toolLocation, Component comp) throws InvalidArgumentException, ServiceException {
        Tool t = null;
        try {
            InputStream is = myspace.getInputStream(toolLocation);
            t = Tool.unmarshalTool(new InputStreamReader(is));
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        } 
        return editTool(t,comp);
    }
    
    /**
     * @see org.astrogrid.desktop.modules.dialogs.QueryEditorInternal#selectAndBuildTool()
     */
    public Tool selectAndBuildTool(Component comp) throws ServiceException {
        dialog.setLocationRelativeTo(comp);
        dialog.setVisible(true);
        return dialog.getTool();       
    }

    /**
     * @see org.astrogrid.acr.dialogs.QueryEditor#edit(org.w3c.dom.Document)
     */
    public Document edit(Document doc) throws InvalidArgumentException, ServiceException {
        try {
            Tool t = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
            Tool t1 = editTool(t,null);
            Document doc1 = XMLUtils.newDocument();
            Marshaller.marshal(t1,doc1);
            return doc1;
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new InvalidArgumentException(e);
            }
    }

    /**
     * @see org.astrogrid.acr.dialogs.QueryEditor#editStored(java.net.URI)
     */
    public Document editStored(URI arg0) throws InvalidArgumentException, ServiceException {
       Tool t = editStoredTool(arg0,null);
       try {
       Document doc1 = XMLUtils.newDocument();
       Marshaller.marshal(t,doc1);
       return doc1;
       } catch (Exception e) {
           throw new ServiceException(e);
       }
    }    
    
    /**
     * @see org.astrogrid.acr.dialogs.QueryEditor#selectAndBuild()
     */
    public Document selectAndBuild() throws ServiceException {
        Tool t1 = selectAndBuildTool(null);
        try {
            Document doc1 = XMLUtils.newDocument();
            Marshaller.marshal(t1,doc1);
            return doc1;
            } catch (Exception e) {
                throw new ServiceException(e);
            }
    }




}


/* 
$Log: QueryEditorImpl.java,v $
Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/