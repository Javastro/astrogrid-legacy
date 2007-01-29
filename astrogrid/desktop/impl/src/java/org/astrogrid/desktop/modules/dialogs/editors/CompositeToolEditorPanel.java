/*$Id: CompositeToolEditorPanel.java,v 1.31 2007/01/29 11:11:37 nw Exp $
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
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.ui.ApplicationLauncherImpl;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.ProcessingInstruction;

import com.l2fprod.common.swing.JLinkButton;

/** Tool Editor Panel that composites together a bunch of other ones, and determines which
 * to show.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Sep-2005
 *
 */
public class CompositeToolEditorPanel extends AbstractToolEditorPanel implements PropertyChangeListener {
    
   /** listen to changes, display icon and metadata */
	protected final class ProviderCreditsButton extends JLinkButton {
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
					setIcon(null);
					setText(null);
					setToolTipText(null);
					final Resource info = toolModel.getInfo();
					if (info.getCuration().getCreators().length ==0) {
						return;
					}
					if (info.getCuration().getCreators().length > 0) {

						final Creator c = info.getCuration().getCreators()[0];
						if (c.getLogo() != null) {
							(new BackgroundWorker(parent,"Fetching Creator Icon") {
								protected Object construct() throws Exception {
									return new ImageIcon(IconHelper.loadIcon(c.getLogo()).getImage().getScaledInstance(-1,48,Image.SCALE_SMOOTH));
								}
								protected void doFinished(Object result) {
									setIcon((Icon)result);
								}
								protected void doError(Throwable ex) {//ignore
								}
							}).start();
						}
						final String creatorName = c.getName().getValue();
						if (creatorName != null && creatorName.trim().length() > 0) {
								
						setText( "Created by " + creatorName);
						setToolTipText("<html>This service: " + info.getTitle() + " <br>provides access to materials created by<br>" 
								+ creatorName);
						} else {
							setText("");
							setToolTipText("");
						}
					}
				}
			};
			addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						browser.openURL(toolModel.getInfo().getContent().getReferenceURL());
					} catch (Exception ex) {
						// no matter..
					}
				}
			});
			
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
                    setEnabled(toolModel.getTool() != null && parent instanceof ApplicationLauncherImpl);
                }
                public void toolCleared(ToolEditEvent te) {
                    setEnabled(false);
                }                
            });
        }

        public void actionPerformed(ActionEvent e) {
            if( pollPanelsForWarnings( EXECUTE ) == true ) {
                if( displayWarningDialog( EXECUTE ) == false ) {
                    return ;
                }
            }           
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
                    Service[] services = apps.listServersProviding(new URI("ivo://" + tOrig.getName())); 
                    logger.debug("resolved app to " + services.length + " servers");
                    if (services.length == 0) {// no providing servers found.
                    	return null;
                    } else if (services.length > 1) {
                        URI[] names = new URI[services.length];
                        for (int i = 0 ; i < services.length; i++) {
                            names[i] = services[i].getId(); //@todo should be titile here really.
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
            super("Open",IconHelper.loadIcon("fileopen.gif"));
            this.putValue(SHORT_DESCRIPTION,"Load task document from storage");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
        }        

        public void actionPerformed(ActionEvent e) {
            int code = JOptionPane.showConfirmDialog(CompositeToolEditorPanel.this,"Discard current task?","Are you sure?",JOptionPane.OK_CANCEL_OPTION);
            if (code == JOptionPane.CANCEL_OPTION) {
                return ;
            }
            final URI u = rChooser.chooseResourceWithParent("Select tool document to load",true,true, true,CompositeToolEditorPanel.this);
            if (u == null) {
                return;
            }                   
                (new BackgroundWorker(parent,"Opening task definition") {
                    private CeaApplication newApp;
                    private InterfaceBean newInterface;
                    protected Object construct() throws Exception {
                    	Reader fr = null;
                    	try {
                        fr = new InputStreamReader(myspace.getInputStream(u));
                       Tool t = Tool.unmarshalTool(fr);
                       
                       newApp = apps.getCeaApplication(new URI("ivo://" + t.getName()));                       
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
                    	//@FIXME - find the resource object.
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
            int code = JOptionPane.showConfirmDialog(CompositeToolEditorPanel.this,"Discard current task?","Are you sure?",JOptionPane.OK_CANCEL_OPTION);
            if (code == JOptionPane.CANCEL_OPTION) {
                return ;
            }
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
               
        
        private void enableApplicable(Tool t, CeaApplication info) {
        	int firstApplicable = 0;
        	for (int i = 1; i < views.length; i++ ) { 
        		final AbstractToolEditorPanel panel = views[i];
        		// start at 1, as 0 is hte chooser - always applicable, but other applicable should take precedence
        		int pos = tabPane.indexOfComponent(panel);
        		if (pos != -1) { // else it's an advanced one, not visible at the moment.
        			if (panel.isApplicable(t, info)) {
        				tabPane.setEnabledAt(pos,true);
        				if (firstApplicable == 0) {
        					firstApplicable = pos;
        				}                    
        			} else {
        				tabPane.setEnabledAt(pos,false);
        			}
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
    protected final BrowserControl browser;
    protected final Preference advancedPreference;
    
    private ArrayList warningMessages ;
	private final int[] preferenceIndexes;
	private final List panelFactories;
   

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
            ,BrowserControl browser
            ,Preference advancedPref
            ) {        
        this.panelFactories = panelFactories;
		this.parent = parent;        
        this.browser = browser;
        this.rChooser = rChooser;
        this.apps = apps;
        this.myspace = myspace;
        this.advancedPreference = advancedPref;
        advancedPreference.addPropertyChangeListener(this);
        
        tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPane.setTabPlacement(SwingConstants.LEFT);
        tabPane.setPreferredSize(new Dimension(600,495));
        
        parent.setJMenuBar(getJJMenuBar());

        // deep breath. instantiate each panel in turn.
        // for those that are 'standard', add them into the tabpane
        // for those that are 'advanced', note their index, 
        // and later trigger a mass add/remove.
        int advancedCount = 0;
        int[] advancedIndexes = new int[panelFactories.size()];
        views = new AbstractToolEditorPanel[panelFactories.size()];
        for (int i = 0; i < panelFactories.size(); i++) {
        	ToolEditorPanelFactory fac = (ToolEditorPanelFactory)panelFactories.get(i);
        	AbstractToolEditorPanel p = fac.create(getToolModel(),parent);
        	views[i] = p;
        	// if anyone of them is interested, register as a property change listener..
        	if (p instanceof PropertyChangeListener) {
        		this.addPropertyChangeListener((PropertyChangeListener)p);
        	}
        	if (fac.isAdvanced()) {
        		// make a note of it's position.
        		advancedIndexes[advancedCount++] = i;
        	} else { // add it immediately
        		tabPane.addTab(fac.getName(),p);
        	}
        }   
        // trim the array of indexes and store in a member variable.
        this.preferenceIndexes = new int[advancedCount];
        System.arraycopy(advancedIndexes, 0, preferenceIndexes, 0, advancedCount);
        
        // configure which tabs to show.
        advancedPreference.initializeThroughListener(this);
        
        // ok. tricky part all done.
        hs.enableHelp(tabPane, "userInterface.taskEditor");
           
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
    public boolean isApplicable(Tool t, CeaApplication info) {
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
    
   /** 
     * 
     * Polls each of the views to see whether each would like to caution the user
     * on the associated action before the action is performed. Maintains an
     * ArrayList for the purposes of displaying messages from muliple views.
     * 
     * @param actionType the action about to be performed
     * @return a boolean if one or more messages have been built;
     */
    private boolean pollPanelsForWarnings( ActionType actionType ) {
        if( warningMessages != null ) {
            warningMessages.clear() ;
        }
        else {
            warningMessages = new ArrayList() ;
        }
        String message ;
        for( int i=0; i<views.length; i++ ) {
            message = views[i].getActionWarningMessage( actionType ) ;
            if( message != null ) {
                warningMessages.add( message ) ;
            }
        }
        if( warningMessages.size() > 0 )
            return true ;
        return false ;
    }
    
    /** 
     * 
     * Displays a dialog of one or more warning messages associated
     * with a particular action. Returns true if the user says
     * proceed anyway, false otherwise.
     * 
     * @param actionType the action about to be performed
     * @return a boolean indicating whether to continue or not;
     */
    private boolean displayWarningDialog( ActionType actionType ) {
        StringBuffer buffer = new StringBuffer() ;
        Iterator iterator = warningMessages.listIterator() ;
        while( iterator.hasNext() ) {
            buffer
                .append( iterator.next() ) 
                .append( '\n' ) ;
        }
        buffer.append( "\nPress \"OK\" to continue, \"Cancel\" to return." ) ;
        int code = JOptionPane.showConfirmDialog( CompositeToolEditorPanel.this
                                                , buffer.toString()
                                                , "Warning on " + actionType.toString()
                                                , JOptionPane.OK_CANCEL_OPTION ) ;
        if (code == JOptionPane.OK_OPTION) {
            return true ;
        }
        
        return false ;
    }
    
    /** notifies when the advancedPreference changes value. */
	public void propertyChange(PropertyChangeEvent evt) {
			if (advancedPreference.asBoolean()) {
				for (int i = 0; i < this.preferenceIndexes.length; i++) {
					int ix = preferenceIndexes[i];
					ToolEditorPanelFactory fac = (ToolEditorPanelFactory) panelFactories.get(ix);
					AbstractToolEditorPanel panel = views[ix];
					tabPane.insertTab(fac.getName(), null, panel, null, ix);	
				}
			} else {
				for (int i = 0; i < this.preferenceIndexes.length; i++) {
					int ix = preferenceIndexes[i];
					AbstractToolEditorPanel panel = views[ix];
					int tabIndex = tabPane.indexOfComponent(panel);
					if (tabIndex != -1) {
						tabPane.removeTabAt(tabIndex);
					}
				}
			}
	}
    
}


/* 
$Log: CompositeToolEditorPanel.java,v $
Revision 1.31  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.30  2007/01/23 11:48:48  nw
preferences integration.

Revision 1.29  2007/01/11 18:15:50  nw
fixed help system to point to ag site.

Revision 1.28  2007/01/10 19:12:16  nw
integrated with preferences.

Revision 1.27  2006/11/27 18:38:09  jl99
Merge of branch workbench-jl-2022

Revision 1.26.6.1  2006/11/23 10:33:32  jl99
Attempts to deal with multiple warning messages issued by multiple panels when a user chooses an action where there may be errors somewhere in the overall tools dialog. This situation will get easier when the Query Builder reduces its set of views of a query.

Revision 1.26  2006/10/16 15:04:50  pjn3
new file open image #1889

Revision 1.25  2006/08/31 21:34:46  nw
minor tweaks and doc fixes.

Revision 1.24  2006/08/15 16:16:49  pjn3
Minor change to prevent execute button being enabled when parent is workflow builder

Revision 1.23  2006/08/15 10:22:05  nw
migrated from old to new registry models.

Revision 1.22  2006/08/02 13:30:09  nw
improved presentation of provider logo.

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