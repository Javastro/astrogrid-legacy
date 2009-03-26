/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** A {@link ResourceViewer} that displays the raw XML of a resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:33:10 PM
 */
public class XMLResourceViewer extends JTextArea implements ResourceViewer, ActionListener, PropertyChangeListener {
	/**
	 * 
	 */
	public XMLResourceViewer(final UIComponent parent,final RegistryInternal reg, final Preference advanced) {
		this.reg = reg;
		this.parent = parent;
		this.advancedPreference = advanced;
		CSH.setHelpIDString(this, "reg.xml");		
		setBorder(BorderFactory.createEmptyBorder());		
		setEditable(false);
		clear();
	
		scroller =new JScrollPane(this);
	}
	
	private final Preference advancedPreference;
	private final Point offset = new Point(8,8);
 	
	private final UIComponent parent;
	private final RegistryInternal reg;
	final public void clear() {
		setText("No entry selected");
	}

	public void display( final Resource res) {

		(new BackgroundWorker(parent,"Fetching Record",BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {
		    {
		        setTransient(true);
		    }
			@Override
            protected Object construct() throws Exception {
				final Document doc = reg.getResourceXML(res.getId());
				//return XMLUtils.DocumentToString(doc);
				return DomHelper.DocumentToString(doc);
			}
			@Override
            protected void doFinished(final Object o) {
				setText(o.toString());
				setCaretPosition(0); 
			}
		}).start();
	}

	// called to copy text to clipboard.
	public void actionPerformed(final ActionEvent e) {
		copy();
	}

	// delayed add. we take a reference to everything we need,
	// but add in the propertyChange listener..
	public void addTo(final JTabbedPane t) {
		this.tabPane = t;
		advancedPreference.addPropertyChangeListener(this);
		advancedPreference.initializeThroughListener(this);		
	}
	
	protected JTabbedPane tabPane;
	protected final JScrollPane scroller;
	

	/** triggered when value of preference changes. - shows / hides xml representation. */
	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getSource() == this.advancedPreference ) {
			if (advancedPreference.asBoolean()) {
				tabPane.addTab("XML", IconHelper.loadIcon("xml.gif"), scroller, "View the XML source for this resource");       			
			} else {
				final int ix = tabPane.indexOfComponent(scroller);
				if (ix != -1) {
					tabPane.removeTabAt(ix);
				}
			}

		}
	}
	
}
