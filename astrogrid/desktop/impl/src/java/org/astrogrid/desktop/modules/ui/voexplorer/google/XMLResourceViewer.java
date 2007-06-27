/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import jedit.JEditTextArea;
import jedit.SyntaxDocument;
import jedit.XMLTokenMarker;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;
import org.astrogrid.desktop.modules.ui.dnd.XmlTransferable;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** displays raw xml of resource.
 * 		@todo work out how to get drag-n-drop working on this.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:33:10 PM
 */
public class XMLResourceViewer extends JEditTextArea implements ResourceViewer, DragGestureListener, DragSourceListener, ActionListener, PropertyChangeListener {
	private DragSource dragSource;
	/**
	 * 
	 */
	public XMLResourceViewer(RegistryInternal reg, Preference advanced) {
		this.reg = reg;
		this.advancedPreference = advanced;
		CSH.setHelpIDString(this, "reg.xml");		
		setBorder(BorderFactory.createEmptyBorder());		
        setDocument(new SyntaxDocument()); // necessary to prevent aliasing between jeditors.        
        setTokenMarker(new XMLTokenMarker());
        getPainter().setFont(Font.decode("Helvetica 10"));		
		setEditable(false);
		clear();
		setPreferredSize(new Dimension(200,100));
		
		// drag n drop.
		xmlImage = IconHelper.loadIcon("xml.gif").getImage();
		this.dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this,DnDConstants.ACTION_COPY,this);
		// copy and past.
		
		getInputHandler().addKeyBinding("C+C",this);
		getInputHandler().addKeyBinding("A+C",this);
	}
	
	private final Preference advancedPreference;
	private final Image xmlImage;
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

// listen for drag gesturs.
	public void dragGestureRecognized(DragGestureEvent dge) {
		System.out.println("ouch");
		Transferable trans = getSeletionTransferable();
		try {
			dge.startDrag(DragSource.DefaultCopyDrop, xmlImage,offset,trans,this); 
		} catch (InvalidDnDOperationException e) {
		}		
	}
/**
	 * @return
	 */
	private Transferable getSeletionTransferable() {
		return new XmlTransferable(getSelectedText());
	}

	// don't really care about any of these...
	public void dragDropEnd(DragSourceDropEvent dsde) {
	}

	public void dragEnter(DragSourceDragEvent dsde) {
	}

	public void dragExit(DragSourceEvent dse) {
	}

	public void dragOver(DragSourceDragEvent dsde) {
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
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
	

	/** triggered when value of preference changes. - shows / hides xml representation. */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == this.advancedPreference ) {
			if (advancedPreference.asBoolean()) {
				tabPane.addTab("XML entry", IconHelper.loadIcon("xml.gif"), this, "View the XML as entered in the registry");       			
			} else {
				int ix = tabPane.indexOfComponent(this);
				if (ix != -1) {
					tabPane.removeTabAt(ix);
				}
			}

		}
	}
	
}
