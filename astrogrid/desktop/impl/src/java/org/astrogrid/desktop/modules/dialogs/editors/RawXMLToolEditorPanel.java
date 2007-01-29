/*$Id: RawXMLToolEditorPanel.java,v 1.4 2007/01/29 11:11:37 nw Exp $
 * Created on 08-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.editors;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.JButton;

import jedit.JEditTextArea;
import jedit.SyntaxDocument;
import jedit.XMLTokenMarker;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

/** Cheap n cheerful Tool Editor Panel that just displays raw xml.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Sep-2005

 */
public class RawXMLToolEditorPanel extends AbstractToolEditorPanel {

    /** Construct a new RawXMLToolEditorPanel
     * @param toolModel
     */
    public RawXMLToolEditorPanel(ToolModel toolModel, Applications a,UIComponent p) {       
        super(toolModel);
        this.apps = a;
        this.parent = p;        
        
        button = new JButton("Edit");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (button.getText().equals("Edit")) {
                    button.setText("Commit");
                    jedit.setEnabled(true);
                    jedit.setEditable(true);
                } else {
                    button.setText("Edit");
                    jedit.setEditable(false);
                    jedit.setEnabled(false);                    
                    (new BackgroundWorker(parent,"Parsing XML") {

                        protected Object construct() throws Exception {
                            StringReader reader =new StringReader(jedit.getText());
                            Tool t = Tool.unmarshalTool(reader);
                            Document doc = XMLUtils.newDocument();
                            Marshaller.marshal(t,doc);
                            apps.validate(doc);
                            return t;
                        }
                        
                        protected void doFinished(Object o) {
                            getToolModel().populate((Tool)o,getToolModel().getInfo());

                        }
                    }).start();
                }
            }
        });
        
        jedit = new JEditTextArea();
        jedit.setDocument(new SyntaxDocument()); // necessary to prevent aliasing between jeditors.        
        jedit.setTokenMarker(new XMLTokenMarker());
        jedit.setFont(Font.decode("Helvetica 10"));
       
       jedit.setEditable(false);
        this.setLayout(new BorderLayout());
        
        this.add(jedit,BorderLayout.CENTER);
        this.add(button,BorderLayout.SOUTH);
        getToolModel().addToolEditListener(new ToolEditListener() {

            public void toolSet(ToolEditEvent te) {
                if (te.getSource() != RawXMLToolEditorPanel.this) { 
                    pendingUpdates = true;
                }
            }

            public void parameterChanged(ToolEditEvent te) {
                if (te.getSource() != RawXMLToolEditorPanel.this) {
                    pendingUpdates = true;
                    }
            }

            public void parameterAdded(ToolEditEvent te) {
                if (te.getSource() != RawXMLToolEditorPanel.this) {
                    pendingUpdates = true;
                    }
            }

            public void parameterRemoved(ToolEditEvent te) {
                if (te.getSource() != RawXMLToolEditorPanel.this) {
                    pendingUpdates = true;
                    }
            }

            public void toolChanged(ToolEditEvent te) {
                if (te.getSource() != RawXMLToolEditorPanel.this) {
                    pendingUpdates = true;
                    }
            }

            public void toolCleared(ToolEditEvent te) {
                if (te.getSource() != RawXMLToolEditorPanel.this) {
                    jedit.setText("");
                    }                

            }
        });
        reloadContent();
        // listen to 'showing' events - and if updates have happened, reload the content of the text editor.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                if (pendingUpdates) {
                    pendingUpdates = false;
                    reloadContent();
                }                
            }
        });
  
    }

    private final JButton button;
    private final JEditTextArea jedit;
    private final Applications apps;
    private final UIComponent parent;
    private boolean pendingUpdates;
    private void reloadContent() {
        final Tool t= getToolModel().getTool();
        if (t != null) {
            (new BackgroundWorker(parent,"Updating XML Display") {
                protected Object construct() throws Exception {
                    Document doc= XMLUtils.newDocument();
                    Marshaller.marshal(t,doc);
                    StringWriter sw = new StringWriter();
                    XMLUtils.PrettyDocumentToWriter(doc,sw);
                    return sw;
                }

                protected void doFinished(Object result) {
                    jedit.setText(result.toString());                    
                }            
            }).start();

        }
    }
    
    /** suitable (just about) for any non-null tool */
    public boolean isApplicable(Tool t, CeaApplication info) {
        return t != null;
    }

}


/* 
$Log: RawXMLToolEditorPanel.java,v $
Revision 1.4  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.3  2006/08/15 10:22:06  nw
migrated from old to new registry models.

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.56.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/