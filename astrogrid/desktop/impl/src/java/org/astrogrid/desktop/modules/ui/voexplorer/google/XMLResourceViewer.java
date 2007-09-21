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
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** displays raw xml of resource.
 * 		@todo work out how to get drag-n-drop working on this.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:33:10 PM
 */
public class XMLResourceViewer extends JTextArea implements ResourceViewer, ActionListener, PropertyChangeListener {
	/**
	 * 
	 */
	public XMLResourceViewer(RegistryInternal reg, Preference advanced) {
		this.reg = reg;
		this.advancedPreference = advanced;
		CSH.setHelpIDString(this, "reg.xml");		
		setBorder(BorderFactory.createEmptyBorder());		
		setEditable(false);
		clear();
	
		scroller =new JScrollPane(this);
	}
	
	private final Preference advancedPreference;
	private final Point offset = new Point(8,8);
 	
	private UIComponentBodyguard parent;
	private final RegistryInternal reg;
	public void clear() {
		setText("No entry selected");
	}

	public void display( final Resource res) {

		(new BackgroundWorker(parent.get(),"Fetching Record") {

			protected Object construct() throws Exception {
				Document doc = reg.getResourceXML(res.getId());
				//return XMLUtils.DocumentToString(doc);
				return DomHelper.DocumentToString(doc);
			}
			protected void doFinished(Object o) {
				setText(o.toString());
				setCaretPosition(0); 
			}
		}).start();
	}

	// called to copy text to clipboard.
	public void actionPerformed(ActionEvent e) {
		copy();
	}

	// delayed add. we take a reference to everything we need,
	// but add in the propertyChange listener..
	public void addTo(UIComponentBodyguard parent, JTabbedPane t) {
		this.parent = parent;
		this.tabPane = t;
		advancedPreference.addPropertyChangeListener(this);
		advancedPreference.initializeThroughListener(this);		
	}
	
	protected JTabbedPane tabPane;
	protected final JScrollPane scroller;
	

	/** triggered when value of preference changes. - shows / hides xml representation. */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == this.advancedPreference ) {
			if (advancedPreference.asBoolean()) {
				tabPane.addTab("XML entry", IconHelper.loadIcon("xml.gif"), scroller, "View the XML as entered in the registry");       			
			} else {
				int ix = tabPane.indexOfComponent(scroller);
				if (ix != -1) {
					tabPane.removeTabAt(ix);
				}
			}

		}
	}
	
}
