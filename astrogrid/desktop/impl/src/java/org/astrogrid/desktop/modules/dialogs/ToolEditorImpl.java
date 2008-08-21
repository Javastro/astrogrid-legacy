/*$Id: ToolEditorImpl.java,v 1.19 2008/08/21 12:56:29 nw Exp $
 * Created on 16-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Component;
import java.net.URI;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

/** Implementation of the ToolEditor component
 *  @author Noel Winstanley noel.winstanley@manchester.ac.uk 16-May-2005
 *
 */
public class ToolEditorImpl implements ToolEditorInternal {
   private final ToolEditorDialog dialog;   


    public ToolEditorImpl(final UIContext context
            ,final TypesafeObjectBuilder builder, final ApplicationsInternal apps, final RegistryGoogleInternal chooser) {
        this.dialog = new ToolEditorDialog(context,builder,apps,chooser);   
    }
    

    public Document edit(final Document doc) throws InvalidArgumentException {
        try {
        final Tool t = (Tool)Unmarshaller.unmarshal(Tool.class,doc);        
        final Tool t1 = editTool(t,null);
        if (t1 == null) {
            return null;
        }
        final Document doc1 = XMLUtils.newDocument();
        Marshaller.marshal(t1,doc1);
        return doc1;
        } catch (final Exception e) {
            throw new InvalidArgumentException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.dialogs.ToolEditor#editStored(java.net.URI)
     */
    public Document editStored(final URI arg0) throws InvalidArgumentException, ServiceException {
        final Tool t = editStoredTool(arg0,null);
        if (t == null) {
            return null;
        }
        try {
        final Document doc1 = XMLUtils.newDocument();
        Marshaller.marshal(t,doc1);
        return doc1;
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }


    public Tool editStoredTool(final URI toolLocation, final Component comp) throws InvalidArgumentException, ServiceException {
        dialog.load(toolLocation);
        dialog.setLocationRelativeTo(comp);
        return dialog.getTool();
    }
    
 
    public Tool editTool(final Tool t,final Component comp) throws InvalidArgumentException {
        dialog.populate(t);
        dialog.setLocationRelativeTo(comp);
        return dialog.getTool();
     
    }

    /**
     * @see org.astrogrid.acr.dialogs.ToolEditor#selectAndBuild()
     */
    public Document selectAndBuild() throws ServiceException {
        final Tool t1 = selectAndBuildTool(null);
        if (t1 == null) {
            return null;
        }
        try {
            final Document doc1 = XMLUtils.newDocument();
            Marshaller.marshal(t1,doc1);
            return doc1;
            } catch (final Exception e) {
                throw new ServiceException(e);
            }
    }

    /**
     * @see org.astrogrid.desktop.modules.dialogs.ToolEditorInternal#selectAndBuildTool(java.awt.Component)
     */
    public Tool selectAndBuildTool(final Component comp) throws ServiceException {
        dialog.setLocationRelativeTo(comp);
        return dialog.getTool();   
    }


}


/* 
$Log: ToolEditorImpl.java,v $
Revision 1.19  2008/08/21 12:56:29  nw
Complete - task 103: tool editor dialogue

Revision 1.18  2007/10/12 10:58:24  nw
re-worked dialogues to use new ui baseclass and new ui components.

Revision 1.17  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.16  2007/07/23 12:21:18  nw
stopgap implementations of tool editor dialog - uses new codebase, but not tested at the moment.

Revision 1.15  2007/06/18 17:03:12  nw
javadoc fixes.

Revision 1.14  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

Revision 1.13  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.12  2007/01/10 19:12:16  nw
integrated with preferences.

Revision 1.11  2006/08/15 10:21:14  nw
added constructor that specifies tool editor to use.upgraded to use new reg model.

Revision 1.9  2006/06/27 19:11:52  nw
fixed to filter on cea apps when needed.

Revision 1.8  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.7.30.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.7.30.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.7  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.6.2.1  2005/11/23 04:49:27  nw
attempted to improve dialogue behaviour

Revision 1.6  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.5  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.4.6.1  2005/10/12 09:21:38  nw
added java help system

Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.4  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:32  nw
first release of application launcher
 
*/