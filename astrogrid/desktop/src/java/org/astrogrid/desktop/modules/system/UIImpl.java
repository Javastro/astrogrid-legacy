/*$Id: UIImpl.java,v 1.2 2005/04/13 12:59:12 nw Exp $
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

import org.astrogrid.desktop.framework.Module;
import org.astrogrid.desktop.framework.ModuleRegistry;
import org.astrogrid.desktop.framework.NewModuleEvent;
import org.astrogrid.desktop.framework.NewModuleListener;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.framework.Shutdown;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.Descriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;
import org.astrogrid.desktop.framework.descriptors.ValueDescriptor;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.PositionRememberingJFrame;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.Startable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;

import java.awt.Component;
import java.awt.Dimension;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;
import com.l2fprod.common.swing.StatusBar;
import com.l2fprod.common.swing.JButtonBar;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.CardLayout;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 */
public class UIImpl extends PositionRememberingJFrame implements Startable,NewModuleListener, UI,InvocationHandler {

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(UIImpl.class);

	private javax.swing.JPanel jContentPane = null;

	private JMenuBar appMenuBar = null;
	private JMenu modulesMenu = null;
	private JMenu helpMenu = null;
	private JMenuItem agOverviewMenuItem = null;
	private JMenuItem aboutMenuItem = null;
	private JMenuItem modulesLinkMenuItem = null;
	private JLabel statusLabel = null;
	private JMenuItem exitMenuItem = null;
	private JLabel throbberLabel = null;  //  @jve:decl-index=0:visual-constraint="567,77"
    private JMenuItem helpContentsMenuItem = null;

    /** development - only constrcutor */
    public UIImpl() {
        this.browser=null;
        this.shutdown = null;
        this.reg = null;
    }
    
    /** this is the production constructor */
    public UIImpl(BrowserControl browser,ModuleRegistry reg, Shutdown sh, Configuration conf) {     
        super(conf,null);
        this.browser = browser;
        this.shutdown = sh;
        this.reg = reg;
        reg.addNewModuleListener(this);  
        this.maybeRegisterSingleInstanceListener();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initialize();
            }
        });
    }
    protected final BrowserControl browser;
    protected final Shutdown shutdown;
    protected final ModuleRegistry reg;
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setJMenuBar(getAppMenuBar());
		this.setSize(500, 600);
		this.setContentPane(getJContentPane());
		this.setTitle("Astrogrid Dashboard");
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                int code = JOptionPane.showConfirmDialog(UIImpl.this,"This closes the user interface. Do you with  the desktop server to continue to run in the background?", 
                        "Closing GUI",JOptionPane.INFORMATION_MESSAGE);
                switch(code) {
                    case JOptionPane.YES_OPTION:
                        hide(); break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0); break;                  
                }
            }
        });
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getStatusBar(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(new JScrollPane(getButtonBar()), java.awt.BorderLayout.WEST);
			jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
    
    private JLabel getStatusLabel() {
        if (statusLabel == null) {
            statusLabel = new JLabel();
            statusLabel.setText("");
            statusLabel.setForeground(java.awt.SystemColor.activeCaption);
            statusLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
        }
        return statusLabel;
    }
    
    private JLabel getThrobberLabel() {
        if (throbberLabel == null) {
            throbberLabel = new JLabel();
            throbberLabel.setText("");
            throbberLabel.setIcon(IconHelper.loadIcon("flashpoint.gif"));
            throbberLabel.setDisabledIcon(IconHelper.loadIcon("sleeping.gif"));            
            throbberLabel.setEnabled(false);            
            throbberLabel.setToolTipText("When active, communicating with astrogrid servers");
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
	private JMenu getModulesMenu() {
		if (modulesMenu == null) {
			modulesMenu = new JMenu();
			modulesMenu.setText("Modules");
            modulesMenu.add(getModulesLinkMenuItem());
            modulesMenu.add(new JSeparator());
            modulesMenu.add(new JSeparator());
			modulesMenu.add(getExitMenuItem());
		}
		return modulesMenu;
	}

	/**
	 * This method initializes jMenu2	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setName("");
			helpMenu.setText("Help");
			helpMenu.add(getAgOverviewMenuItem());
            helpMenu.add(getHelpContentsMenuItem());
            helpMenu.add(new JSeparator());
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem2	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getAgOverviewMenuItem() {
		if (agOverviewMenuItem == null) {
			agOverviewMenuItem = new JMenuItem();
			agOverviewMenuItem.setText("Astrogrid Overview");
			agOverviewMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {                        
                        browser.openURL("http://www.astrogrid.org");
                    } catch (Exception ex) {
                        logger.error(e);
                    }
				}
			});
		}
		return agOverviewMenuItem;
	}
    
    private JMenuItem getHelpContentsMenuItem() {
        if (helpContentsMenuItem == null) {
            helpContentsMenuItem = new JMenuItem();
            helpContentsMenuItem.setText("Help Contents");
            helpContentsMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        browser.openURL("http://software.astrogrid.org");
                    } catch (Exception ex) {
                        logger.error(e);
                    }
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
			aboutMenuItem.setText("About Astrogrid Desktop");
			aboutMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                    JOptionPane.showMessageDialog(UIImpl.this,"Astrogrid Desktop v0.1\nhttp://software.astrogrid.org","About Astrogrid Desktop",JOptionPane.INFORMATION_MESSAGE);
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
			exitMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {   
                    hide();
                    shutdown.halt();
				}
			});
		}
		return exitMenuItem;
	}

    /** show the gui, unless key is set.
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        String key = configuration.getKey(START_HIDDEN_KEY);
        if (key != null && Boolean.valueOf(key).equals(Boolean.TRUE)) {
            // do nothing
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    show();
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

    /**  listener to registry - inspects new modules, and publishes their methods.
     * @see org.astrogrid.desktop.framework.NewModuleListener#newModuleRegistered(org.astrogrid.desktop.framework.NewModuleEvent)
     */
    public void newModuleRegistered(NewModuleEvent e) {
        final ModuleDescriptor md = e.getModule().getDescriptor();
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
                            UIImpl.this.doCall(md,cd,methd);
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
            final JMenu modules = getModulesMenu();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    modules.add(m,modules.getItemCount()- 2); // want to add it at end - 2
                    actionCards.add(taskPane,md.getName());
                    getButtonBar().add(new ModuleButton(md));
                }
            });
        }                
    }
    
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
        String icon = cd.getProperty("icon");
        g.setExpanded(false);        
        if (icon != null) {
            g.setIcon(IconHelper.loadIcon(icon));
        }
        return g;
    }


    /**@todo change appearance on click - 'selected' */
    private class ModuleButton extends JButton {
        public ModuleButton(final ModuleDescriptor md) {
            this.setToolTipText(md.getDescription());
            this.setText(md.getUIName());
            String icon = md.getProperty("icon");
            if (icon != null) {
                this.setIcon(IconHelper.loadIcon(icon));
            }
            // @todo not the nicest way - could probalby override a method directly, but don't know which one.
            this.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cardManager.show(actionCards,md.getName());
                }
            });
        }
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
                        browser.openURL(url); 
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                }                      
    }
    
    /** configutation key that indicates a method should be made executable on the ui menu */
    public static final String MENU_ENTRY_KEY = "system.ui.menu.entry";
    /** configuration key that indicates that the result of a call to a method should be displayed in the browser */
    public static final String DISPLAY_RESULT_BROWSER_KEY  = "system.ui.result.browser";
    /** configuration key that contains embedded xml that lists URLs to link to from the menu */
    public static final String MENU_LINK_KEY = "system.ui.menu.links";
    
    // make throbber calls nest.
    
    private int throbberCallCount = 0;
	private StatusBar statusBar = null;  //  @jve:decl-index=0:visual-constraint="413,168"
	private JLabel loginLabel = null;  //  @jve:decl-index=0:visual-constraint="942,210"
	private JButtonBar buttonBar = null;
	private JSplitPane jSplitPane = null;
	private JPanel actionCards = null;
    /**
     * @throws InterruptedException
     * @throws InvocationTargetException
     * @see org.astrogrid.desktop.modules.system.UI#startThrobbing()
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
     * @see org.astrogrid.desktop.modules.system.UI#stopThrobbing()
     */
    public synchronized void stopThrobbing() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {        
                    getThrobberLabel().setEnabled(--throbberCallCount > 0);
            }
        });
    }
    /**
     * @see org.astrogrid.desktop.modules.system.UI#setStatusMessage(java.lang.String)
     */
    public void setStatusMessage(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getStatusLabel().setText(msg);
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

        Object component = module.getComponent(componentD.getName());
        if (component == null) {
           showError("Component not found: " +componentD.getName());
           return;
        }             
            // prompt for any args..
        java.util.List args =new ArrayList();
        for (Iterator i = md.parameterIterator(); i.hasNext(); ) {
            ValueDescriptor vd = (ValueDescriptor)i.next();
            String result = JOptionPane.showInputDialog(this,"<html>Enter value for: " + vd.getName() + "<br><i>" + vd.getDescription() + "</i></html>","Enter value",JOptionPane.QUESTION_MESSAGE);
            args.add(result);
        }
        SwingWorker worker = new InvokerWorker(component,md,(String[])args.toArray(new String[]{}));
        worker.start();                                  
    }
    
    /**
     * @param s
     * @throws HeadlessException
     */
    private void showError(String s) throws HeadlessException {
        JOptionPane.showMessageDialog(this,s,"Error",JOptionPane.ERROR_MESSAGE);
    }

    class InvokerWorker extends SwingWorker {
        public InvokerWorker(Object component,MethodDescriptor md, String[] args) {
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
        protected void finished() {
                try {
                Object r = get();
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
                            showError("Could not display result in browser: " + e1.getMessage());                                    
                        }
                        
                    } else {
                    JOptionPane.showMessageDialog(UIImpl.this,r,"Result",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                } catch (InterruptedException ex) {
                    logger.info("Method interrupted",ex);
                    showError("Method was interrupted, or timed out\n");
                } catch (InvocationTargetException e) {
                    Throwable  ex = e.getCause() == null ? e : e.getCause();
                    logger.info("Exception executing method",ex);
                    showError("Exception when executing method\n" + ex.getClass().getName() + "\n" + ex.getMessage());                          
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
     * @see org.astrogrid.desktop.modules.system.UI#getComponent()
     */
    public Component getComponent() {
        return this;
    }    
	/**
	 * This method initializes statusBar	
	 * 	
	 * @return com.l2fprod.common.swing.StatusBar	
	 */    
	private StatusBar getStatusBar() {
		if (statusBar == null) {
			statusBar = new StatusBar();
            statusBar.addZone("status",getStatusLabel(),"*");
            statusBar.addZone("login",getLoginLabel(),"18");
            statusBar.addZone("throbber",getThrobberLabel(),"26");
		}
		return statusBar;
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
     * @see org.astrogrid.desktop.modules.system.UI#setLoggedIn(boolean)
     */
    public void setLoggedIn(boolean value) {
        getLoginLabel().setEnabled(value);
    }
	/**
	 * This method initializes jButtonBar	
	 * 	
	 * @return com.l2fprod.common.swing.JButtonBar	
	 */    
	private JButtonBar getButtonBar() {
		if (buttonBar == null) {
			buttonBar = new JButtonBar();
            buttonBar.setOrientation(JButtonBar.VERTICAL);

		}
		return buttonBar;
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
			jSplitPane.setDividerLocation(130);
			jSplitPane.setDividerSize(5);
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