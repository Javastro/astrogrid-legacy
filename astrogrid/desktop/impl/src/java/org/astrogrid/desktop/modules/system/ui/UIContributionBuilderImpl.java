/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.order.Orderer;
import org.astrogrid.desktop.modules.system.contributions.UIActionContribution;
import org.astrogrid.desktop.modules.system.contributions.UIStructureContribution;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;

/** consumes the ui contribution from hivemind, and builds UI components using it.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 10, 200711:22:01 AM
 */
public class UIContributionBuilderImpl implements ContributionInvoker, UIContributionBuilder{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(UIContributionBuilderImpl.class);

    protected final Transformer trans;  
    protected final Converter conv;
    protected final ErrorHandler err;
    protected final MultiMap multi;
	public UIContributionBuilderImpl(Map structures,final Transformer trans, final Converter conv, final ErrorHandler err) {
		super();
		this.trans = trans;
		this.conv = conv;
		this.err = err;
		
        multi = new MultiValueMap();
        for (Iterator i = structures.values().iterator(); i.hasNext(); ) {
            UIStructureContribution o = (UIStructureContribution) i.next();
            multi.put(o.getParentName(),o);
        }    
	}    
	

	/** recursive part of building menu and tab structure */
    public void populateWidget(Object current,UIComponent comp,String name) {
        // find all the children of this component.
        Collection menus = (Collection)multi.get(name);
        if (menus == null || menus.size() == 0) {
            return;
        }
        //order copies of  them.
        Orderer orderer = new Orderer(err,"Sorting children of UI component '" + name + "' : ");
        for (Iterator i = menus.iterator(); i.hasNext(); ) {
            UIStructureContribution m = (UIStructureContribution)i.next(); 
            orderer.add(m.cloneStructure(),m.getName(),m.getAfter(),m.getBefore());
        }
        //add them.
        for (Iterator i = orderer.getOrderedObjects().iterator(); i.hasNext(); ) {
            final UIStructureContribution m = (UIStructureContribution)i.next();
            // bit ugly.
            if (m instanceof UIActionContribution) {
                final UIActionContribution a = (UIActionContribution)m;
                a.setContributionInvoker(this); // pass reference to self into the component.
                a.setUiComponent(comp);
           //@todo sort whether we want to get a reference to 'about' menu
                //if (a.getName().equals("about")) {
                //	aboutAction = a;
                //}
               if (current instanceof JMenu) {
                	final JMenuItem mi = new JMenuItem(a);
                	a.setParentComponent(mi);	
                    ((JMenu)current).add(mi);
                    String help = a.getHelpId();
                    if (help != null) {
                    	comp.getContext().getHelpServer().enableHelp(mi, help);
                    }
                } else if (current instanceof JComponent) {                
                    final JButton b = new JButton(a);
                    a.setParentComponent(b);
                    String help = a.getHelpId();
                    if (help != null) {
                    	comp.getContext().getHelpServer().enableHelp(b, help);
                    }                    
                    b.setText("<html><center>" + a.getText()); 
                    // fix for bz 1735
                    b.setHorizontalTextPosition(JButton.CENTER);
                    b.setVerticalTextPosition(JButton.BOTTOM);
                    b.setPreferredSize(new Dimension(120,96));
                    b.setAlignmentY(JButton.TOP_ALIGNMENT);
                    ((JComponent)current).add(b);
                } else {
                    err.error(logger,"Unknown type",null,null);
                }
            } else { // assume it's a JComponent - and so is it's parent.
                final JComponent o = (JComponent)m;
                populateWidget(o,comp,m.getName()); // recursive call

                if (current instanceof JTabbedPane) {
                    final JScrollPane sc= new JScrollPane(o);
                    sc.setAutoscrolls(true);
                    //sc.setBorder(EMPTY_BORDER);
                    sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    sc.getHorizontalScrollBar().setBorder(UIComponentImpl.EMPTY_BORDER);
                    sc.getHorizontalScrollBar().putClientProperty("is3DEnabled",Boolean.FALSE);          
                    sc.getViewport().setPreferredSize(new Dimension(600,100));
                    final JTabbedPane tabs = (JTabbedPane)current;
                    final ComponentAdapter visibilityListener = new ComponentAdapter() {
											public void componentHidden(ComponentEvent e) {
												int ix = tabs.indexOfTab(m.getText());
												if (ix != -1) {
													tabs.removeTabAt(ix);
												}
											}
											public void componentShown(ComponentEvent e) {
												  tabs.addTab(m.getText(),m.getIcon(),sc,o.getToolTipText());				
											}
					                    };
					o.addComponentListener(visibilityListener);
                    // now fire it off to initialize.
					if (o.isVisible()) {
						visibilityListener.componentShown(null);
					} else {
						visibilityListener.componentHidden(null);
					}                    
                } else {
                    ((JComponent)current).add(o);
                }
            }
        }
    }



    // invoke a ui action
	public void invoke(UIActionContribution action) {
        int result = JOptionPane.YES_OPTION;
        final String msg = action.getConfirmMessage();
		if (msg != null  && msg.trim().length() > 0) {
            result = JOptionPane.showConfirmDialog(action.getUiComponent().getFrame(),msg,"Confirmation",JOptionPane.YES_NO_OPTION);
        }
        if (result == JOptionPane.YES_OPTION) {
        	InvokerWorker op = new InvokerWorker(action.getUiComponent(),conv,trans,action);
        	if (action.isOnEventDispatchThread()) { //run direct on swing thread.
        		SwingUtilities.invokeLater(op);
        	} else {
        		op.start(); // run in background.
        	}
        }		
		
	}
        
}
