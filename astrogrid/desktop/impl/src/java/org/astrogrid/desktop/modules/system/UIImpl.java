/*$Id: UIImpl.java,v 1.12 2006/04/21 13:48:11 nw Exp $
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.order.Orderer;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.system.contributions.UIActionContribution;
import org.astrogrid.desktop.modules.system.contributions.UIStructureContribution;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;

import com.l2fprod.common.swing.StatusBar;
/**Implementation of the UI component
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 */
public class UIImpl extends UIComponentImpl implements UIInternal {

    public class InvokerWorker extends BackgroundOperation {
        private final UIActionContribution action;
        public InvokerWorker(UIActionContribution action) {
            super("Running " + action.getName());
            this.action = action;
        }
          /**
         * @see EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker#construct()
         */
        protected Object construct() throws Exception {
            Method method = ReflectionHelper.getMethodByName(action.getObject().getClass(),action.getMethodName());                        
            Class[] parameterTypes = method.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];
            // convert parameter values to correct types.
            Iterator i =action.getParameters().iterator();
            for (int j = 0; j < parameterTypes.length && i.hasNext(); j++) {
                args[j] = conv.convert(parameterTypes[j],i.next());
            }                
            Object result = MethodUtils.invokeMethod(action.getObject(),action.getMethodName(),args);
                if (result != null) { // display results, if this method produced any.
                    return trans.transform(result);
                } else {
                    return null;
                }
        }
        
          protected void doFinished(Object r) {
                if (r != null) {                
                        ResultDialog rd = new ResultDialog(UIImpl.this,r);
                        rd.show();
                        rd.toFront();
                        rd.requestFocus();
                }    
         }
        
    }
    /** parentName for root menus */
	public static final String MENUBAR_NAME = "menubar";
    /** parentName for tabs */
	public static final String TABS_NAME = "tabs";   
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(UIImpl.class);
    protected final BrowserControl browser;
    protected final ConfigurationInternal confInternal;
    protected final Converter conv;
    protected final ErrorHandler err;
    protected final BackgroundExecutor executor;
    protected final ACRInternal reg;   
	protected final Shutdown shutdown;  
    protected final Transformer trans;  
    
    private JMenuBar appMenuBar;
	private JLabel loginLabel;  
    
	private StatusBar statusBar;
    
    private JTabbedPane tabbedPane;
    // make throbber calls nest.    
    private int throbberCallCount = 0;
    
    
    
    private JLabel throbberLabel;
    /** this is the production constructor */
    public UIImpl(BrowserControl browser,ACRInternal reg, Shutdown sh, ConfigurationInternal conf, HelpServerInternal help, BackgroundExecutor executor,Converter conv,Transformer trans, Map structures,ErrorHandler err) {     
        super(conf,help,null);
        this.confInternal = conf;
        this.browser = browser;
        this.shutdown = sh;
        this.reg = reg;
        this.executor = executor;
        this.conv = conv;
        this.trans = trans;
        this.err = err;
        
        this.setJMenuBar(getAppMenuBar());

        addStructures(structures.values());
        
        JPanel main = getMainPanel();
        main.add(getTabbedPane(), java.awt.BorderLayout.CENTER);    
        this.setContentPane(main);
        getHelpServer().enableHelpKey(this.getRootPane(),"top"); 
        setIconImage(IconHelper.loadIcon("AGlogo16x16.png").getImage());            
        //this.setSize(425, ); // same proportions as A4, etc., and 600 high.   
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                int code = JOptionPane.showConfirmDialog(UIImpl.this,"Closing the UI. Do you want  the ACR service to continue to run in the background?", 
                        "Closing UI",JOptionPane.INFORMATION_MESSAGE);
                if (code == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                hide(); // always fo this..
                if (code == JOptionPane.NO_OPTION) {                    
                        shutdown.halt(); 
                }
            }
        });
        this.pack();

    }
    

    
	public BackgroundExecutor getExecutor() {
        return executor;
    }
	//overrridden to return self;
    public UIInternal getUI() {
        return this;
    }

    /**
     * @see org.astrogrid.acr.system.UI#setLoggedIn(boolean)
     */
    public void setLoggedIn(boolean value) {
        getLoginLabel().setEnabled(value);
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
    
    public void show() {
        super.show();
        toFront();
        requestFocus();          
    }
    
    public void setVisible(boolean b) {
        super.setVisible(b);
             toFront();
            requestFocus();            
    }
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
    public void stopThrobbing() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {        
                    getThrobberLabel().setEnabled(--throbberCallCount > 0);
            }
        });
    }    
	/**
     * @see org.astrogrid.desktop.modules.system.UIInternal#wrap(java.lang.Runnable)
     */
    public BackgroundWorker wrap(final Runnable r) {
        return new BackgroundOperation("Background Task") {

            protected Object construct() throws Exception {
                r.run();
                return null;
            }
        };
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

    /** reads in ui structures, assembling them */
    private void addStructures(Collection structures) {
        MultiMap multi = new MultiHashMap();        
        for (Iterator i = structures.iterator(); i.hasNext(); ) {
            UIStructureContribution o = (UIStructureContribution) i.next();
            multi.put(o.getParentName(),o);
        }        
        Object current = getJMenuBar();
        addStructures(current,MENUBAR_NAME,multi);
        current = getTabbedPane();
        addStructures(current,TABS_NAME,multi);        
    }
	/** recursive part of building menu and tab structure */
    private void addStructures(Object current,String name, MultiMap multi) {
        // find all the children of this component.
        Collection menus = (Collection)multi.get(name);
        if (menus == null || menus.size() == 0) {
            return;
        }
        //order them.
        Orderer orderer = new Orderer(err,"Sorting children of UI component '" + name + "' : ");
        for (Iterator i = menus.iterator(); i.hasNext(); ) {
            UIStructureContribution m = (UIStructureContribution)i.next(); 
            orderer.add(m,m.getName(),m.getAfter(),m.getBefore());
        }
        //add them.
        for (Iterator i = orderer.getOrderedObjects().iterator(); i.hasNext(); ) {
            UIStructureContribution m = (UIStructureContribution)i.next();
            // bit ugly.
            if (m instanceof UIActionContribution) {
                UIActionContribution a = (UIActionContribution)m;
                a.setUIImpl(this); // pass reference to self into the component.
                if (current instanceof JMenu) {
                    ((JMenu)current).add(a);
                } else if (current instanceof JComponent) {                
                    JButton b = new JButton(a);
                    b.setText("<html><center>" + StringUtils.replace(a.getText()," ","<br>"));
                    b.setPreferredSize(new Dimension(96,96));
                    b.setAlignmentY(JButton.TOP_ALIGNMENT);
                    ((JComponent)current).add(b);
                } else {
                    err.error(logger,"Unknown type",null,null);
                }
            } else { // assume it's a JComponent - and so is it's parent.
                JComponent o = (JComponent)m;
                addStructures(o,m.getName(),multi); // recursive call

                if (current instanceof JTabbedPane) {
                    JScrollPane sc= new JScrollPane(o);
                    sc.setAutoscrolls(true);
                    //sc.setBorder(EMPTY_BORDER);
                    sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    sc.getHorizontalScrollBar().setBorder(EMPTY_BORDER);
                    sc.getHorizontalScrollBar().putClientProperty("is3DEnabled",Boolean.FALSE);          
                    sc.getViewport().setPreferredSize(new Dimension(600,100));
                    ((JTabbedPane)current).addTab(m.getText(),m.getIcon(),sc,o.getToolTipText());
                } else {
                    ((JComponent)current).add(o);
                }
            }
        }
            
        
        
    }
    
	private JMenuBar getAppMenuBar() {
        if (appMenuBar == null) {
            appMenuBar = new JMenuBar();
            // tart it up a bit..
            
          //  appMenuBar.putClientProperty(Options.HEADER_STYLE_KEY,HeaderStyle.SINGLE);
            //appMenuBar.putClientProperty(PlasticLookAndFeel.IS_3D_KEY, Boolean.TRUE);
            //appMenuBar.putClientProperty(PlasticLookAndFeel.BORDER_STYLE_KEY,BorderStyle.EMPTY);
            
        }
        return appMenuBar;
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
    


    
    
    private JTabbedPane getTabbedPane() {
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane();
            tabbedPane.setBorder(EMPTY_BORDER);
        }
        return tabbedPane;
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




	//overridden
	public Component getComponent() {
		return this;
	}


}


/* 
$Log: UIImpl.java,v $
Revision 1.12  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.11  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.10.22.4  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.10.22.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.10.22.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.10.22.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.10  2005/12/16 09:42:47  jl99
Merge from branch desktop-querybuilder-jl-1404

Revision 1.9  2005/12/02 13:43:18  nw
new compoent that manages a pool of threads to execute background processes on

Revision 1.8  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.7.2.1  2005/11/23 04:44:35  nw
attempted to improve dialogue behaviour

Revision 1.7  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.6  2005/11/09 15:28:21  jdt
fixed broken help  link

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