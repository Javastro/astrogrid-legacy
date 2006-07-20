/*$Id: CompositeToolEditorPanel.java,v 1.21 2006/07/20 12:30:59 nw Exp $
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.ProcessingInstruction;

/** Tool Editor Panel that composites together a bunch of other ones, and determines which
 * to show.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2005
 *
 */
public class CompositeToolEditorPanel extends AbstractToolEditorPanel {
    
   /** listen to changes, display icon and metadata */
	protected final class ProviderCreditsButton extends JButton {
		public ProviderCreditsButton() {
			super();
			setEnabled(toolModel.getTool() != null);
			setBackground(Color.WHITE);
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			ToolEditListener listener = new ToolEditAdapter() {
				public void toolCleared(ToolEditEvent ignored) {
					setEnabled(false);
					setIcon(null);
					setText("");
					setToolTipText("");
				}
				public void toolSet(ToolEditEvent ignored) {
					setEnabled(true);
					final ApplicationInformation info = toolModel.getInfo();
					if (info.getLogoURL() == null) {
						setIcon(null);
					} else {
						(new BackgroundWorker(parent,"Fetching Creator Icon") {
							protected Object construct() throws Exception {
								return new ImageIcon(IconHelper.loadIcon(info.getLogoURL()).getImage().getScaledInstance(-1,48,Image.SCALE_SMOOTH));
							}
							protected void doFinished(Object result) {
								setIcon((Icon)result);
							}
							protected void doError(Throwable ex) {//ignore
							}
						}).start();
					}
					String creatorName = "todo" ; //@FIXME fil in from rsource/curartion/creator/name - once have improved reg entry parsing in general.
					setText( creatorName);
					setToolTipText("<html>This service: " + info.getName() + " <br>provides access to materials created by<br>" + creatorName);
					//@todo on  button click launch browser to go to homepage. - can't find suitable url in current schema.
				}
			};
			
			toolModel.addToolEditListener(listener);
			// finally, trigger the listener to setup 
			if (toolModel.getTool() == null) {
				listener.toolCleared(null);
			} else {
				listener.toolSet(null);
			}
		}
		
		public void actionPerformed(ActionEvent arg0) {
		}
	}
	
    protected final class ExecuteAction extends AbstractAction {

        public ExecuteAction() {
            super("Execute !", IconHelper.loadIcon("run_tool.gif"));
            this.putValue(SHORT_DESCRIPTION,"Execute this application");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
            this.setEnabled(toolModel.getTool() != null);
            toolModel.addToolEditListener(new ToolEditAdapter() {
                public void toolSet(ToolEditEvent te) {
                    setEnabled(toolModel.getTool() != null);
                }
                public void toolCleared(ToolEditEvent te) {
                    setEnabled(false);
                }                
            });
        }

        public void actionPerformed(ActionEvent e) {
            
            final Tool tOrig = getToolModel().getTool();
            final String securityMethod = getToolModel().getSecurityMethod();
            logger.info("Security method = " + securityMethod);
            (new BackgroundWorker(parent,"Executing..") {

                protected Object construct() throws Exception {
                    logger.debug("Executing");
                    Document doc = XMLUtils.newDocument();
                    Marshaller.marshal(tOrig,doc);
                    if (securityMethod != null) {
                      logger.info("Setting security instruction on the tool document");
                      ProcessingInstruction p 
                          = doc.createProcessingInstruction("CEA-strategy-security", 
                                                            securityMethod);
                      doc.appendChild(p);
                    }
                    ResourceInformation[] services = apps.listProvidersOf(new URI("ivo://" + tOrig.getName())); 
                    logger.debug("resolved app to " + services.length + " servers");
                    if (services.length == 0) {// no providing servers found.
                    	return null;
                    } else if (services.length > 1) {
                        URI[] names = new URI[services.length];
                        for (int i = 0 ; i < services.length; i++) {
                            names[i] = services[i].getId();
                        }
                    URI chosen = (URI)JOptionPane.showInputDialog(CompositeToolEditorPanel.this
                            ,"More than one CEA server provides this application - please choose one"
                            ,"Choose Server"
                            ,JOptionPane.QUESTION_MESSAGE
                            ,null
                            , names
                            ,names[0]);
                       return apps.submitTo(doc,chosen);
                    } else {
                    return apps.submitTo(doc,services[0].getId());
                    }
                }
                
                protected void doFinished(Object o) {
                	if (o == null) { // unable to launch the app
                		JOptionPane.showMessageDialog(CompositeToolEditorPanel.this,"<b>Failed to launch task</b><br>Could not find any servers that provide the task: " + tOrig.getName()
                				,"Cannot launch task",JOptionPane.ERROR_MESSAGE);
                	} else {
                		// not much point constructing this if we're not going to display it...
                		//ResultDialog rd = new ResultDialog(CompositeToolEditorPanel.this,"ExecutionId : " + o);
                		//rd.show();        
                		if (lookout != null) {
                			lookout.show();
                		}
                	}
               }
                
            }).start();
        }
        
    }
    
    protected final class NewAction extends AbstractAction {
        public NewAction() {
            super("New",IconHelper.loadIcon("newfile_wiz.gif"));
            this.putValue(SHORT_DESCRIPTION,"Create a new task");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
            this.setEnabled(true);
        }
        public void actionPerformed(ActionEvent e) {
            int code = JOptionPane.showConfirmDialog(CompositeToolEditorPanel.this,"Discard current task?","Are you sure?",JOptionPane.OK_CANCEL_OPTION);
            if (code == JOptionPane.OK_OPTION) {
                getToolModel().clear();
            }
        }
    }
    protected final class OpenAction extends AbstractAction {

        public OpenAction() {
            super("Open",IconHelper.loadIcon("file_obj.gif"));
            this.putValue(SHORT_DESCRIPTION,"Load task document from storage");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
        }        

        public void actionPerformed(ActionEvent e) {
            final URI u = rChooser.chooseResourceWithParent("Select tool document to load",true,true, true,CompositeToolEditorPanel.this);
            if (u == null) {
                return;
            }                   
                (new BackgroundWorker(parent,"Opening task definition") {
                    private ApplicationInformation newApp;
                    private InterfaceBean newInterface;
                    protected Object construct() throws Exception {
                    	Reader fr = null;
                    	try {
                        fr = new InputStreamReader(myspace.getInputStream(u));
                       Tool t = Tool.unmarshalTool(fr);
                       
                       newApp = apps.getApplicationInformation(new URI("ivo://" + t.getName()));                       
                       InterfaceBean[] candidates = newApp.getInterfaces();
                       for (int i = 0; i < candidates.length; i++) {
                           if (candidates[i].getName().equalsIgnoreCase(t.getInterface().trim())) {
                               newInterface  = candidates[i];
                           }
                       }
                       if (newInterface == null) {
                           throw new IllegalArgumentException("Cannot find interface " + t.getInterface() + " in registry entry for " + t.getName());
                       }
                       return t;
                    	} finally {
                    		if (fr != null) {
                    			try {
                    				fr.close();
                    			} catch (IOException ignored) {
                    			}
                    		}
                    	}
                    }
                    protected void doFinished(Object o) {
                        getToolModel().populate((Tool)o,newApp);
                    }
                }).start();
            }
            
        
    }

 
    protected final class SaveAction extends AbstractAction {
 
        public SaveAction() {
            super("Save",IconHelper.loadIcon("fileexport.png"));
            this.putValue(SHORT_DESCRIPTION,"Save task document");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
            this.setEnabled(toolModel.getTool() != null);
            toolModel.addToolEditListener(new ToolEditAdapter() {
                public void toolSet(ToolEditEvent te) {
                    setEnabled(toolModel.getTool() != null);
                }
                public void toolCleared(ToolEditEvent te) {
                    setEnabled(false);
                }                
            });
        }

        public void actionPerformed(ActionEvent e) {
            final URI u = rChooser.chooseResourceWithParent("Save Task Document",true,true, true,CompositeToolEditorPanel.this);
            if (u == null) {
                return;
            }
                (new BackgroundWorker(parent,"Saving task definition") {

                    protected Object construct() throws Exception {
                    	Writer w = null;
                    	try {
                        Tool t = getToolModel().getTool();
                         w = new OutputStreamWriter(myspace.getOutputStream(u));
                        t.marshal(w);                       
                        return null;
                    	} finally {
                    		if (w != null) {
                    			try {
                    				w.close();
                    			} catch (IOException ignored) {
                    			}
                    		}
                    	}
                    }
                }).start();            
            
        }
    }
    
    /** close action */
    protected final class CloseAction extends AbstractAction {
        public CloseAction() {
            super("Close",IconHelper.loadIcon("exit_small.png"));
            this.putValue(SHORT_DESCRIPTION,"Close");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
        }

        public void actionPerformed(ActionEvent e) {
            parent.hide();
            parent.dispose();
        }
    }
  
    
    /** the controller -composites together the various editors, 
     *  listens to tool change events, and enables / disables components depending on theiir applicability */
    private class Controller extends ToolEditAdapter {
  

        public Controller() {
            // setup the state.
            enableApplicable(toolModel.getTool(),toolModel.getInfo());
        }

        public void toolChanged(ToolEditEvent te) {
            enableApplicable(toolModel.getTool(),toolModel.getInfo());
        }
        public void toolCleared(ToolEditEvent te) {
            enableApplicable(null,null);
        }        
        
        public void toolSet(ToolEditEvent te) {
            enableApplicable(toolModel.getTool(), toolModel.getInfo());
        }
               
        
        private void enableApplicable(Tool t, ApplicationInformation info) {
                    int firstApplicable = 0;
                    for (int i = 1; i < views.length; i++ ) { 
                    	// start at 1, as 0 is hte chooser - always applicable, but other applicable should take precedence
                        int pos = tabPane.indexOfComponent(views[i]);

                        if (views[i].isApplicable(t, info)) {
                            tabPane.setEnabledAt(pos,true);
                            if (firstApplicable == 0) {
                                firstApplicable = pos;
                            }                    
                        } else {
                            tabPane.setEnabledAt(pos,false);
                        }
                    }
                    tabPane.setSelectedIndex(firstApplicable);
                }                   
    }
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CompositeToolEditorPanel.class);
    protected final ApplicationsInternal apps;
    protected final ResourceChooserInternal rChooser;
    protected final MyspaceInternal myspace;
    protected  Lookout lookout;
    protected final UIComponentImpl parent;
    protected final  JTabbedPane tabPane;
    protected final AbstractToolEditorPanel[] views;
    protected Action newAction, saveAction, openAction, executeAction, closeAction;
    protected final JButton creditsButton;
   

    /** set the lookout reference - not passed into constructor, as may not always be available*/
    public void setLookout(Lookout l) {
    	this.lookout = l;
    }
    
    /** constructor when being used as an app. */
    public CompositeToolEditorPanel(
    		List panelFactories
            ,ResourceChooserInternal rChooser
            ,ApplicationsInternal apps
            ,MyspaceInternal myspace
            , UIComponentImpl parent
            ,HelpServerInternal hs
            ) {        
        this.parent = parent;        
        this.rChooser = rChooser;
        this.apps = apps;
        this.myspace = myspace;
        tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPane.setTabPlacement(SwingConstants.LEFT);
        tabPane.setPreferredSize(new Dimension(600,495));
        parent.setJMenuBar(getJJMenuBar());

        List panels = new ArrayList();
        for (Iterator i = panelFactories.iterator(); i.hasNext(); ) {
        	ToolEditorPanelFactory fac = (ToolEditorPanelFactory)i.next();
        	AbstractToolEditorPanel p = fac.create(getToolModel(),parent);
        	panels.add(p);
        	// if anyone of them is interested, register as a property change listener..
        	if (p instanceof PropertyChangeListener) {
        		this.addPropertyChangeListener((PropertyChangeListener)p);
        	}
        	tabPane.addTab(fac.getName(),p);
        }   
        views = (AbstractToolEditorPanel[])panels.toArray(new AbstractToolEditorPanel[panels.size()]);
        hs.enableHelp(tabPane, "userInterface.workflowBuilder.taskEditor");
           
        newAction = new NewAction();
        openAction = new OpenAction();
        saveAction = new SaveAction();
        executeAction = new ExecuteAction();
        closeAction = new CloseAction();
        creditsButton = new ProviderCreditsButton();
        
        JToolBar tb = new JToolBar();
        tb.setRollover(true);
        tb.setFloatable(false);
        tb.add(newAction);
        tb.add(openAction);
        tb.add(saveAction);
        tb.add(executeAction);
        tb.add(Box.createHorizontalGlue());
        tb.add(creditsButton);
        
        fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.add(saveAction);
        fileMenu.add(executeAction);
        
        fileMenu.add(new JSeparator());
        fileMenu.add(closeAction);
        this.setLayout(new BorderLayout());
        this.add(tb,BorderLayout.NORTH);
        this.add(tabPane,BorderLayout.CENTER);
      this.getToolModel().addToolEditListener(new Controller());
        //this.setPreferredSize(new Dimension(600,425));
    }

    /** able to handle everything */
    public boolean isApplicable(Tool t, ApplicationInformation info) {
        return true;
    }
    
    private JMenuBar jJMenuBar;
    private JMenu fileMenu;
	/** client property set when we want to restrict to only cea apps */
	public static String CEA_ONLY_CLIENT_PROPERTY = "CEA_ONLY";
    
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());		
		}
		return jJMenuBar;
	}
   
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.setMnemonic(KeyEvent.VK_F);
		}
		return fileMenu;
	}
    
}


/* 
$Log: CompositeToolEditorPanel.java,v $
Revision 1.21  2006/07/20 12:30:59  nw
added display of tool creator's logo.

Revision 1.20  2006/06/27 19:12:31  nw
fixed to filter on cea apps when needed.

Revision 1.19  2006/06/27 10:28:27  nw
findbugs tweaks

Revision 1.18  2006/05/13 16:34:55  nw
merged in wb-gtr-1537

Revision 1.17  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.12.2.3  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.12.2.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.12.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.16  2006/03/14 15:07:35  pjn3
Typo

Revision 1.15  2006/03/07 16:50:36  pjn3
Menu added, accelerators added

Revision 1.14  2006/03/03 10:10:12  pjn3
#1505

Revision 1.13  2006/03/02 17:42:35  pjn3
#1505 quick fix

Revision 1.12  2006/02/24 13:18:41  pjn3
Re-ordered tabs

Revision 1.11.14.1  2006/02/23 09:56:59  pjn3
re-order tabs so chooser top

Revision 1.11  2005/12/16 09:42:47  jl99
Merge from branch desktop-querybuilder-jl-1404

Revision 1.10  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.9  2005/11/21 18:26:05  pjn3
basic task editor help added

Revision 1.8  2005/11/21 16:43:51  pjn3
Tool -> Task

Revision 1.7  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.6  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.5  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.4  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.6.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.3  2005/10/07 12:12:21  KevinBenson
resorted back to adding to the ResoruceChooserInterface a new method for selecting directories.
And then put back the older one.

Revision 1.2  2005/10/04 20:43:38  KevinBenson
set it to "false" for selecting directories on the local file system.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/