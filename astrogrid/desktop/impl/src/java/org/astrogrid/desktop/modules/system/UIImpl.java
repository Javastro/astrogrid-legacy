/*$Id: UIImpl.java,v 1.5 2005/10/12 13:30:10 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.Module;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.framework.MutableACR;
import org.astrogrid.desktop.framework.NewModuleEvent;
import org.astrogrid.desktop.framework.NewModuleListener;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.Descriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;
import org.astrogrid.desktop.framework.descriptors.ValueDescriptor;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.ui.PositionRememberingJFrame;
import org.astrogrid.desktop.modules.ui.UIComponent;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.Startable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;

import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;
import com.l2fprod.common.swing.StatusBar;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
/**Implementation of the UI component
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 */
public class UIImpl extends UIComponent implements Startable,UIInternal,InvocationHandler {

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(UIImpl.class);

	private javax.swing.JPanel jContentPane = null;

	private JMenuBar appMenuBar = null;
	private ObservingJMenu modulesMenu = null;
	private JMenu helpMenu = null;
	private JMenuItem agOverviewMenuItem = null;
	private JMenuItem aboutMenuItem = null;
    private ModulesUIModel mum = null;
	private JMenuItem modulesLinkMenuItem = null;
	private JLabel statusLabel = null;
	private JMenuItem exitMenuItem = null;
	private JLabel throbberLabel = null;  //  @jve:decl-index=0:visual-constraint="567,77"
    private JMenuItem helpContentsMenuItem = null;


    
    /** this is the production constructor */
    public UIImpl(BrowserControl browser,MutableACR reg, Shutdown sh, ConfigurationInternal conf, HelpServerInternal help) {     
        super(conf,help,null);
        this.confInternal = conf;
        this.browser = browser;
        this.shutdown = sh;
        this.reg = reg;
        this.maybeRegisterSingleInstanceListener();

    }
    protected final ConfigurationInternal confInternal;
    protected final BrowserControl browser;
    protected final Shutdown shutdown;
    protected final MutableACR reg;

    private JMenuItem reportBugMenuItem;
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setJMenuBar(getAppMenuBar());
		this.setSize(500, 600);
        JPanel main = getJContentPane();
        main.add(getJSplitPane(), java.awt.BorderLayout.CENTER);    
		this.setContentPane(main);
        getHelpServer().enableHelpKey(this.getRootPane(),"system.ui");          
		this.setTitle("Astrogrid Workbench");
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                int code = JOptionPane.showConfirmDialog(UIImpl.this,"Closing the UI. Do you want  the ACR service to continue to run in the background?", 
                        "Closing UI",JOptionPane.INFORMATION_MESSAGE);
                hide(); // always fo this..
                if (code == JOptionPane.NO_OPTION) {                    
                        shutdown.halt(); 
                }
            }
        });
    }

    private JLabel getThrobberLabel() {
        if (throbberLabel == null) {
            throbberLabel = new JLabel();
            throbberLabel.setText("");
            throbberLabel.setIcon(IconHelper.loadIcon("flashpoint.gif"));
            throbberLabel.setDisabledIcon(IconHelper.loadIcon("sleeping.gif"));            
            throbberLabel.setEnabled(false);            
            throbberLabel.setToolTipText("When active, something is communicating with VO services");
        }
        return throbberLabel;
    }
    
	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */    
	private JMenuBar getAppMenuBar() {
		if (appMenuBar == null) {
			appMenuBar = new JMenuBar();
			appMenuBar.add(getModulesMenu());
			appMenuBar.add(getHelpMenu());
		}
		return appMenuBar;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	public JMenu getModulesMenu() {
		if (modulesMenu == null) {
			modulesMenu = new ObservingJMenu();
            getModulesUIModel().addObserver(modulesMenu);
		}
		return modulesMenu;
	}
    
    private class ObservingJMenu extends JMenu implements Observer {

        {
            setText("Modules");
            add(getModulesLinkMenuItem());
            add(new JSeparator());
            add(getCloseMenuItem());
            add(getExitMenuItem());            
        }
        /**
         * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
         */
        public void update(Observable o, Object arg) {
            removeAll();
            add(getModulesLinkMenuItem());
            add(new JSeparator());
            for (Iterator i = ((ModulesUIModel)o).menuIterator(); i.hasNext(); ) {
                add((Component)i.next());
            }
            add(new JSeparator());
            add(getCloseMenuItem());
            add(getExitMenuItem());            
        }
    }

	/**
	 * This method initializes jMenu2	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	public JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setName("");
			helpMenu.setText("Help");
            helpMenu.add(getHelpContentsMenuItem());
            helpMenu.add(getReportBugMenuItem());
            helpMenu.add(new JSeparator());
            helpMenu.add(getResetMenuItem());
            helpMenu.add(new JSeparator());            
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}
    
    private JMenuItem resetMenuItem;
    
    private JMenuItem getResetMenuItem() {
        if (resetMenuItem == null) {
            resetMenuItem = new JMenuItem();
            resetMenuItem.setText("Reset configuration");
            resetMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int code = JOptionPane.showConfirmDialog(
                            UIImpl.this
                            ,"<html>This will reset the application's configuration<br>All user settings will be lost<br>Proceed?</html>"
                            ,"Confirm"
                           ,JOptionPane.OK_CANCEL_OPTION
                           ,JOptionPane.WARNING_MESSAGE
                           );
                    if (code == JOptionPane.OK_OPTION) {
                        try {
                        confInternal.reset();
                        } catch (ServiceException ex) {
                            showError("Failed to reset configuration",ex);
                        }
                    }
                }
            });
        }
        return resetMenuItem;
    }
    
    private JMenuItem getReportBugMenuItem() {
        if (reportBugMenuItem == null) {
            reportBugMenuItem = new JMenuItem();
            reportBugMenuItem .setText("Report a Bug / Request a Feature");
            reportBugMenuItem .addActionListener(new java.awt.event.ActionListener() { 
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {                        
                        browser.openURL(new URL("http://www.astrogrid.org/bugzilla/enter_bug.cgi?product=Workbench"));
                    } catch (Exception ex) {
                        logger.error(e);
                    }
                }
            });
        }
        return    reportBugMenuItem ;
    }
    
    private JMenuItem getHelpContentsMenuItem() {
        if (helpContentsMenuItem == null) {
            helpContentsMenuItem = new JMenuItem();
            helpContentsMenuItem.setText("Help Contents");
            helpContentsMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    help.showHelp();
                }
            });
        } 
        return helpContentsMenuItem;
    }
	/**
	 * This method initializes jMenuItem3	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About Astrogrid Workbench");
			aboutMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                    String version = System.getProperty("workbench.version");
                    JOptionPane.showMessageDialog(UIImpl.this,"Astrogrid Workbench v" + version +"\nhttp://software.astrogrid.org/userdocs/workbench.html","About Astrogrid Workbench",JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
		return aboutMenuItem;
	}
	/**
	 * This method initializes jMenuItem4	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getModulesLinkMenuItem() {
		if (modulesLinkMenuItem == null) {
			modulesLinkMenuItem = new JMenuItem();
			modulesLinkMenuItem.setText("HTML interface");
			modulesLinkMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					try {
                        browser.openRelative(""); // open the services root.
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
				}
			});
		}
		return modulesLinkMenuItem;
	}

	/**
	 * This method initializes jMenuItem6	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
            exitMenuItem.setToolTipText("Close the UI and halt the ACR service");
            exitMenuItem.setIcon(IconHelper.loadIcon("exit.png"));
			exitMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(UIImpl.this,"Exit Workbench - are you sure?","Exit?",JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {       
                        hide();
                        shutdown.halt();
                    }
				}
			});
		}
		return exitMenuItem;
	}
    private JMenuItem closeMenuItem;
    private JMenuItem getCloseMenuItem() {
        if (closeMenuItem == null) {
            closeMenuItem = new JMenuItem();
            closeMenuItem.setText("Close");
            closeMenuItem.setToolTipText("Close the UI, but keep the ACR service running");
            closeMenuItem.setIcon(IconHelper.loadIcon("fileclose.png"));
            closeMenuItem.addActionListener(new java.awt.event.ActionListener() { 
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(UIImpl.this,"Close the UI - are you sure?","Close?",JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        hide();
                    }
                }
            });
        }
        return closeMenuItem;
    }

    // initializes the gui 
    public void start() {
        if (SwingUtilities.isEventDispatchThread()) {
            initialize();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    initialize();
                }
            });
        }
    }
    /** if this key is set to true, doen't display the gui on startup */
    public static final String START_HIDDEN_KEY = "system.ui.hide";

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                hide();
                dispose();
            }
        });
    }
    
    public void show() {
        super.show();        
        requestFocus();
    }



    
    /** model class for the ui - listens to new module events from the registry, builds menus from these new modules,
     * and zap display objects when new menus need to be added. 
     * @author Noel Winstanley nw@jb.man.ac.uk 09-May-2005
     *
     */
    public class ModulesUIModel  extends Observable implements NewModuleListener {

        private class MUMItem {
            public final int priority;
            public final String name;
            public final JMenu menu;
            public final ModuleButton button;
            
            public MUMItem(ModuleDescriptor md, JMenu menu) {
                String str = md.getProperty("priority");
                name = md.getName();
                if (str == null) {
                    priority = 10;
                } else {
                    priority =  Integer.parseInt(str);
                }
                this.menu = menu;
                this.button = ui.new ModuleButton(md);
            }
        }
        
        public ModulesUIModel(BrowserControl browser,JPanel actionCards, UIImpl ui) {
            this.browser =browser;
            this.actionCards = actionCards;
            this.ui = ui;
        }
        
        private final BrowserControl browser;
        private final JPanel actionCards;
        private final UIImpl ui;

        /**
         * @param md
         * @param m
         * @param moduleGroup
         */
        private void addLinkActions(final Descriptor md, final JMenu m, JTaskPaneGroup moduleGroup) {
            List l1 = new ArrayList();
            if (md.getProperty(MENU_LINK_KEY) != null) {
                dig.clear();
                dig.push(l1);                       
                try {
                    dig.parse(new StringReader(md.getPropertyDocument(MENU_LINK_KEY)));
                } catch (IOException e1) {
                    logger.warn("IOException",e1);
                } catch (SAXException e1) {
                    logger.warn("SAXException",e1);
                }
            }
            List l =  l1;
            for (Iterator i = l.iterator(); i.hasNext(); ) {            
                Action a = (Action)i.next();
                m.add(a);            
                moduleGroup.add(a);
            }
        }

        /**
         * @param cd
         * @return
         */
        private JTaskPaneGroup buildTaskPaneGroup(ComponentDescriptor cd) {
            JTaskPaneGroup g = new JTaskPaneGroup();
            g.setText(cd.getUIName());
            g.setToolTipText(cd.getDescription());
            getHelpServer().enableHelp(g,mungeInterfaceToHelpKey(cd.getInterfaceClass()));
            String icon = cd.getProperty("icon");
            g.setExpanded(false);        
            if (icon != null) {
                g.setIcon(IconHelper.loadIcon(icon));
            }
            return g;
        }
        
        private String mungeInterfaceToHelpKey(Class iface) {
            return iface.getName().replace('.','_') + ".html";
        }
                
        
        /** Build a menu from a descriptor. - name, description, any link annotations.
         * uses digester to do this work
         * @param md
         * @return
         */
        private JMenu buildDescriptorMenu(Descriptor md) {
            JMenu m = new JMenu();
            m.setName(md.getName());
            m.setText(md.getUIName());
            m.setToolTipText(md.getDescription());
            String icon = md.getProperty("icon");
            if (icon != null) {
                m.setIcon(IconHelper.loadIcon(icon));
            }        
            return m;
        }

        /**
         * @param md
         * @return
         */
        private JTaskPane buildDescriptorPane(ModuleDescriptor md) {
           return new JTaskPane();
        }
        
        
        /** digester used to parse menu descriptions  - 
         * autimatically creates menu items for types of link, and adds them to the menu on top of the stack. */
        protected final Digester dig = new Digester(){{
            addFactoryCreate("*/link",new AbstractObjectCreationFactory() {
                public Object createObject(Attributes arg0) throws Exception {
                    return new LinkMenuAction();
                }
            });
            addSetProperties("*/link");
            addSetNext("*/link","add");
            
            addFactoryCreate("*/localLink",new AbstractObjectCreationFactory() {
                public Object createObject(Attributes arg0) throws Exception {
                    return new LocalLinkAction();
                }
            });
            addSetProperties("*/localLink");
            addSetNext("*/localLink","add");
            
            addFactoryCreate("*/helpLink",new AbstractObjectCreationFactory() {
                public Object createObject(Attributes arg0) throws Exception {
                    return new HelpLinkAction();
                }
            });
            addSetProperties("*/helpLink");
            addSetNext("*/helpLink","add");
               
        }};
        
        /** class used to model a menu item that links to a local url */
        public class LocalLinkAction extends AbstractAction {
            public void setText(String text) {
                putValue(Action.NAME,text);            
            }
            public void setIcon(String icon) {

                putValue(Action.SMALL_ICON,IconHelper.loadIcon(icon));                   
            }
            public void setToolTipText(String tt) {
                putValue(Action.SHORT_DESCRIPTION,tt);                   
            }
            private String path;
            public void setPath(String path) {
                this.path = path.trim();
            }

            public void actionPerformed(java.awt.event.ActionEvent e) {    
                        try {
                            browser.openRelative(path); 
                        } catch (Exception ex) {
                            logger.error(ex);
                        }         
            }
        }
        
        /** class used to model a menu item that links to a help entry */
        public class HelpLinkAction extends AbstractAction {
            { setIcon("help.gif");
              setText("Help..");
            }
            public void setText(String text) {
                putValue(Action.NAME,text);
            }
            public void setIcon(String icon) {
                putValue(Action.SMALL_ICON,IconHelper.loadIcon(icon));
            }
            public void setToolTipText(String tt) {
                putValue(Action.SHORT_DESCRIPTION,tt);                   
            }
            private String id;
            public void setId(String id) {
                this.id = id.trim();
            }
            public void actionPerformed(ActionEvent e) {
                getHelpServer().showHelpForTarget(id);
            }
        }
        /** class used to model a menu item that links to aremote url */
        public class LinkMenuAction extends AbstractAction {
            public void setText(String text) {
                putValue(Action.NAME,text);            
            }
            public void setIcon(String icon) {

                putValue(Action.SMALL_ICON,IconHelper.loadIcon(icon));                   
            }
            public void setToolTipText(String tt) {
                putValue(Action.SHORT_DESCRIPTION,tt);                   
            }
            private String url;
            public void setUrl(String url) {
                this.url = url.trim();
            }
            public void actionPerformed(java.awt.event.ActionEvent e) {    
                        try {
                            browser.openURL(new URL(url)); 
                        } catch (Exception ex) {
                            logger.error(ex);
                        }
                    }                      
        }
        
        /** configutation key that indicates a method should be made executable on the ui menu */
        public static final String MENU_ENTRY_KEY = "system.ui.menu.entry";

        /** configuration key that contains embedded xml that lists URLs to link to from the menu */
        public static final String MENU_LINK_KEY = "system.ui.menu.links";
        
        /** sorted set of menus*/
        private final SortedSet menuSet = new TreeSet(new Comparator() {

            public int compare(Object o1, Object o2) {
                MUMItem a = (MUMItem)o1;
                MUMItem b = (MUMItem)o2;
                return  a.priority == b.priority ? a.name.compareTo(b.name) : a.priority - b.priority;              
            }
        });
        
        public Iterator moduleButtonIterator() {
            return IteratorUtils.transformedIterator(menuSet.iterator(),new Transformer() {
                public Object transform(Object arg0) {
                    return ((MUMItem)arg0).button;
                }
            });
        }
        
        public Iterator menuIterator() {
            return IteratorUtils.transformedIterator(menuSet.iterator(),new Transformer() {
                public Object transform(Object arg0) {
                    return ((MUMItem)arg0).menu;
                }
            });            
        }
        
        
        /**  listener to registry - inspects new modules, and publishes their methods.
         * @see org.astrogrid.desktop.framework.NewModuleListener#newModuleRegistered(org.astrogrid.desktop.framework.NewModuleEvent)
         */
        public void newModuleRegistered(NewModuleEvent e) {
            final ModuleDescriptor md = ((DefaultModule)e.getModule()).getDescriptor();
            logger.info("Inspecting " + md.getName());
            //create menu
            final JMenu m = buildDescriptorMenu(md);
            // create descriptor pane, and module group in it.
            final JTaskPane taskPane = buildDescriptorPane(md);        
            JTaskPaneGroup moduleGroup = new JTaskPaneGroup();
            moduleGroup.setSpecial(true);
            moduleGroup.setText(md.getUIName());        
            taskPane.add(moduleGroup);
            
            addLinkActions(md, m, moduleGroup);
            
            // search for annotations in components..
            for (Iterator i =md.componentIterator(); i.hasNext(); ) {
                final ComponentDescriptor cd = (ComponentDescriptor)i.next();
                JMenu c = buildDescriptorMenu(cd);
                JTaskPaneGroup g = buildTaskPaneGroup(cd);
                addLinkActions(cd, c,g);
                
                for (Iterator j = cd.methodIterator(); j.hasNext(); ) {
                    final MethodDescriptor methd = (MethodDescriptor)j.next();
                    if (methd.getProperty(MENU_ENTRY_KEY) != null ){                    
                            Action act= new AbstractAction() {{
                              putValue(Action.SHORT_DESCRIPTION,methd.getDescription());         
                              String icon = methd.getProperty("icon");
                              if (icon != null) {
                                  putValue(SMALL_ICON,IconHelper.loadIcon(icon));
                              }
                              //setName(methd.getProperty(MENU_ENTRY_KEY));
                              putValue(NAME,methd.getProperty(MENU_ENTRY_KEY));
                            }
                            public void actionPerformed(ActionEvent e) {
                                ui.doCall(md,cd,methd);
                            }};
                            c.add(act);
                            g.add(act);
                    }     
                }// end for
                if (c.getItemCount() > 0) {// add component menu, if any good.
                    m.add(c);
                    taskPane.add(g);
                    }
            }        
            if (m.getItemCount() > 0) { // add module menu, if anything on it.
                menuSet.add(new MUMItem(md,m));
 
                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        actionCards.add(taskPane,md.getName());                        
                        setChanged();
                        notifyObservers();
                    }
                });
            }                
        }
    }
    

    private class ModuleButton extends JButton {
        public ModuleButton(final ModuleDescriptor md) {
            this.setToolTipText(md.getDescription());
            this.setText(md.getUIName());
            maybeRegisterHelpId(md);
            String icon = md.getProperty("icon");
            if (icon != null) {
                this.setIcon(IconHelper.loadIcon(icon));
            }
     
            this.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ModuleButton.this.setSelected(true);
                    cardManager.show(actionCards,md.getName());
                    Component[] comps = getButtonBar().getComponents(); // could make this mor efficient by holding a reference to the stored component somewhere.
                    for (int i = 0; i < comps.length; i++) {
                        if (comps[i] != ModuleButton.this) {
                         ((ModuleButton)comps[i]).setSelected(false);
                        }
                    }
                }
            });
        }
        private void maybeRegisterHelpId(ModuleDescriptor md) {
            Iterator i = md.componentIterator();
            if (!i.hasNext()) { // no components, so no javadoc.                
                return;
            }
            ComponentDescriptor cd = (ComponentDescriptor) i.next();
            String ifaceName = cd.getInterfaceClass().getName();
            String packageName = StringUtils.substringBeforeLast(ifaceName,".");
            String id =  packageName.replace('.','_') + "_package-summary.html";
            getHelpServer().enableHelp(this,id);
        }
    }
    
    // make throbber calls nest.
    
    private int throbberCallCount = 0;
	private StatusBar statusBar = null;  //  @jve:decl-index=0:visual-constraint="413,168"
	private JLabel loginLabel = null;  //  @jve:decl-index=0:visual-constraint="942,210"
	private ObservingButtonBar buttonBar = null;
	private JSplitPane jSplitPane = null;
	private JPanel actionCards = null;
    /**
     * @throws InterruptedException
     * @throws InvocationTargetException
     * @see org.astrogrid.acr.system.UI#startThrobbing()
     */
    public  void startThrobbing() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getThrobberLabel().setEnabled(++throbberCallCount > 0);
            }
        });
    }
    /**
     * @throws InvocationTargetException
     * @throws InterruptedException
     * @see org.astrogrid.acr.system.UI#stopThrobbing()
     */
    public synchronized void stopThrobbing() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {        
                    getThrobberLabel().setEnabled(--throbberCallCount > 0);
            }
        });
    }
    /**
     * @see org.astrogrid.acr.system.UI#setStatusMessage(java.lang.String)
     */
    public void setStatusMessage(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIImpl.super.setStatusMessage(msg);
            }
        });
    }
 
    
    /** @todo candidate for refactoring into a separate 'dispatch' component */
    public void doCall(ModuleDescriptor moduleD, ComponentDescriptor componentD, MethodDescriptor md) {
            // find the correct objects..
        Module module = reg.getModule(moduleD.getName());
        if (module == null) {
            showError("Module not found: " + moduleD.getName());
            return;
        }

        Object component;
        try {
            component = module.getComponent(componentD.getName());
        } catch (Exception e) {
            showError("Failed to access component: " + componentD.getName(),e);
            return;
        }
        if (component == null) {
           showError("Component not found: " +componentD.getName());
           return;
        }             
            // prompt for any args..
        java.util.List args =new ArrayList();
        for (Iterator i = md.parameterIterator(); i.hasNext(); ) {
            ValueDescriptor vd = (ValueDescriptor)i.next();
            String result = JOptionPane.showInputDialog(this,"<html>Enter value for: <b>" + vd.getName() + "</b><br>" + vd.getDescription(),"Enter value",JOptionPane.QUESTION_MESSAGE);
            if (result == null) { // user hit cancel.
                return;
            }
            args.add(result);
        }
        SwingWorker worker = new InvokerWorker(component,md,(String[])args.toArray(new String[]{}));
        worker.start();                                  
    }
    
    class InvokerWorker extends BackgroundOperation {
        public InvokerWorker(Object component,MethodDescriptor md, String[] args) {
            super("Running " + md.getUIName());
            this.component = component;
            this.strArgs = args;
            this.md= md;
        }
        protected final Object component;
        protected final String[] strArgs;
        protected final MethodDescriptor md;
        /**
         * @see EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker#construct()
         */
        protected Object construct() throws Exception {
            Method method = ReflectionHelper.getMethodByName(component.getClass(),md.getName());                        
            Class[] parameterTypes = method.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];
            // pluck parameter values out of request, convert to correct types.
            Iterator i = md.parameterIterator();
            for (int j = 0; j < parameterTypes.length &&  i.hasNext(); j++) {
                ValueDescriptor p = (ValueDescriptor)i.next();
                Converter conv = XmlRpcServlet.getConverter(p); 
                args[j] = conv.convert(parameterTypes[j],strArgs[j]);
            }                
            Object result = MethodUtils.invokeMethod(component,md.getName(),args);
                if (result != null) { // display results, if this method produced any.
                    return XmlRpcServlet.getTransformerSet(md.getReturnValue()).getHtmlTransformer().transform(result);
                } else {
                    return null;
                }
        }
        
        /** configuration key that indicates that the result of a call to a method should be displayed in the browser */
        public static final String DISPLAY_RESULT_BROWSER_KEY  = "system.ui.result.browser";
        protected void doFinished(Object r) {
                if (r != null) {
                    String s = md.getProperty(DISPLAY_RESULT_BROWSER_KEY) ;
                    if (s != null && s.trim().equals("true")) {
                        try {
                            File f = File.createTempFile("ui-result","html");
                            Writer out = new FileWriter(f);
                            out.write(s);
                            out.close();
                            f.deleteOnExit();
                            browser.openURL(f.toURL());
                        } catch (Exception e1) {                           
                            showError("Could not display result in browser",e1);                                    
                        }
                        
                    } else {                  
                        ResultDialog rd = new ResultDialog(UIImpl.this,r);
                        rd.show();
                    }
                }    
         }
        
    }
    
    /**
     * if running under webstart, register a single instance listener - tricky, 
     * as can't link directly against any of the classes.
     */
    private final void maybeRegisterSingleInstanceListener() {
        try {
            // get jnlp manager.
        Class managerClass= Class.forName("javax.jnlp.ServiceManager");
        Method lookupMethod = ReflectionHelper.getMethodByName(managerClass,"lookup");
        // try to get instance of singleInstanceService
        Object singleInstanceService = lookupMethod.invoke(null,new Object[]{"javax.jnlp.SingleInstanceService"});
        // construct a single instance listener. - pointing back to our invoke() method.
        Class listenerClass = Class.forName("javax.jnlp.SingleInstanceListener");
        Object listenerInstance = Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{listenerClass},this); 
        // register listener..
        Method registerMethod = ReflectionHelper.getMethodByName(singleInstanceService.getClass(),"addSingleInstanceListener");
        registerMethod.invoke(singleInstanceService,new Object[]{listenerInstance});
        } catch (Throwable t) { // oh well, can live without it..
            logger.info("Failed to register single instance listener - probably not running under Webstart");
            logger.debug(t);
        }
    }
    /** implementation method for dynamically generated singleInstanceListener.
     * shows the gui, if hidden.
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (!isShowing()) {
                    show();            
                }
            }
        });

        return null;
        
    }

    /**
     * @see org.astrogrid.acr.system.UI#getComponent()
     */
    public Component getComponent() {
        return this;
    }    
	/**
	 * overridden method - add extra display components.
	 */    
	protected StatusBar getBottomPanel() {
		if (statusBar == null) {
			statusBar = super.getBottomPanel();
            statusBar.addZone("login",getLoginLabel(),"18");
            statusBar.addZone("throbber",getThrobberLabel(),"26");
		}
		return statusBar;
	}
    
    private ModulesUIModel getModulesUIModel() {
        if (mum == null) {
            mum = new ModulesUIModel(browser,getActionCards(),this);
            reg.addNewModuleListener(mum);
        }
        return mum;
    }
    
	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */    
	private JLabel getLoginLabel() {
		if (loginLabel == null) {
			loginLabel = new JLabel();
			loginLabel.setText("");
            loginLabel.setDisabledIcon(IconHelper.loadIcon("connect_no.png"));
            loginLabel.setIcon(IconHelper.loadIcon("connect_established.png"));
            loginLabel.setEnabled(false);
            loginLabel.setToolTipText("Indicates login status");
		}
		return loginLabel;
	}

    /**
     * @see org.astrogrid.acr.system.UI#setLoggedIn(boolean)
     */
    public void setLoggedIn(boolean value) {
        getLoginLabel().setEnabled(value);
    }
	/**
	 * This method initializes jButtonBar	
	 * 	
	 * @return com.l2fprod.common.swing.JButtonBar	
	 */    
	private ObservingButtonBar getButtonBar() {
		if (buttonBar == null) {
			buttonBar = new ObservingButtonBar();    
            getModulesUIModel().addObserver(buttonBar);
            buttonBar.setOrientation(JButtonBar.VERTICAL);
		}
		return buttonBar;
	}
    
    private class ObservingButtonBar extends JButtonBar implements Observer {

        public void update(Observable o, Object arg) {
            this.removeAll();
            for (Iterator i = ((ModulesUIModel)o).moduleButtonIterator(); i.hasNext(); ) {
                ModuleButton mb = (ModuleButton)i.next();
                if (this.getComponentCount() == 0) { // it's the first one - so fire this button.
                    mb.doClick();
                }
                this.add(mb);                
            }       
            this.invalidate();            
        }
    }
    
	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */    
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(new JScrollPane(getButtonBar()));
            jSplitPane.setDividerLocation(0);
			//jSplitPane.setDividerLocation(130);
			//jSplitPane.setDividerSize(5);
			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setRightComponent(new JScrollPane(getActionCards()));
		}
        
		return jSplitPane;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getActionCards() {
		if (actionCards == null) {
			actionCards = new JPanel();
			actionCards.setLayout(cardManager);
		}
		return actionCards;
	}
    
    private CardLayout cardManager = new CardLayout();
       }  //  @jve:decl-index=0:visual-constraint="10,11"


/* 
$Log: UIImpl.java,v $
Revision 1.5  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.3.16.2  2005/10/12 09:21:38  nw
added java help system

Revision 1.3.16.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.4  2005/10/05 11:52:32  nw
hide module buttons on startup

Revision 1.3  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.2  2005/08/16 13:19:31  nw
fixes for 1.1-beta-2

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.12  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.11  2005/07/08 14:06:30  nw
final fixes for the workshop.

Revision 1.10  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.9  2005/06/22 08:48:51  nw
latest changes - for 1.0.3-beta-1

Revision 1.8  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2

Revision 1.7  2005/06/17 12:06:14  nw
added changelog, made start on docs.
fixed race condition.

Revision 1.6  2005/06/16 12:55:36  nw
updated build process to use new aggregate projects.

Revision 1.5  2005/05/12 15:59:10  clq2
nww 1111 again

Revision 1.3.8.6  2005/05/12 12:42:48  nw
finished core applications functionality.

Revision 1.3.8.5  2005/05/11 14:25:22  nw
javadoc, improved result transformers for xml

Revision 1.3.8.4  2005/05/11 10:59:05  nw
made results selectable, so can be copied and pasted.

Revision 1.3.8.3  2005/05/09 17:49:51  nw
refactored model into separate static class.

Revision 1.3.8.2  2005/05/09 17:30:32  nw
reordered modules for usability, removed most dangerous
methods from the public ui.

Revision 1.3.8.1  2005/05/09 14:51:02  nw
renamed to 'myspace' and 'workbench'
added confirmation on app exit.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.3  2005/04/26 19:10:45  nw
added bugreport menu item.

Revision 1.2.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 10:55:01  nw
minor bugfix

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.9  2005/04/06 16:18:50  nw
finished icon set

Revision 1.1.2.8  2005/04/06 15:03:53  nw
added new front end - more modern, with lots if icons.

Revision 1.1.2.7  2005/04/05 11:41:47  nw
added 'hidden.modules',
allowed more methods to be called from ui.

Revision 1.1.2.6  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.5  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.4  2005/04/01 19:03:09  nw
beta of job monitor

Revision 1.1.2.3  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.2  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/