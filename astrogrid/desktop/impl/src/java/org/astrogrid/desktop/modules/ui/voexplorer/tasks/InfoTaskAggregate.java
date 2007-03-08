/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTextArea;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.TreeBag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** information about the selection.
 * @todo provide more metadata here.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20071:29:37 PM
 */
public final class InfoTaskAggregate extends TaskAggregate {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(InfoTaskAggregate.class);

public InfoTaskAggregate() {
	setHelpId("resourceActions.info");	
    setTitle("Details");
    setIconName("info16.png");
    
    this.typeField = new JTextArea();
    typeField.setRows(1);
    typeField.setEditable(false);
    typeField.setLineWrap(true);
    typeField.setWrapStyleWord(true);
    typeField.setVisible(false);
    add(typeField);
    
}

private final JTextArea typeField;
private final Bag types = new TreeBag();

	public void manySelected(List l) {
		typeField.setVisible(true);
		types.clear();
		for (int i = 0; i < l.size(); i++) {
			types.add(ResourceFormatter.formatType(((Resource)l.get(i)).getType()));
		}
		typeField.setText("Selected:");
		for (Iterator i = types.uniqueSet().iterator(); i.hasNext();) {
			String t = (String) i.next();
			int count = types.getCount(t);
			typeField.append("\n  " + count + " " + t);
		}
	}

	public void noneSelected() {
		typeField.setVisible(false);
		super.noneSelected();
	}

	public void oneSelected(Resource r) {
		typeField.setText("Type: " + ResourceFormatter.formatType(r.getType()));
		typeField.setVisible(true);
	}

	public void selected(Transferable r) {
		try {
		if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE)) {
			oneSelected((Resource)r.getTransferData(VoDataFlavour.RESOURCE));
		} else if (r.isDataFlavorSupported(VoDataFlavour.RESOURCE)) {
			oneSelected((Resource)r.getTransferData(VoDataFlavour.RESOURCE));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_LIST)) {
			manySelected((List)r.getTransferData(VoDataFlavour.LOCAL_RESOURCE_LIST));
		} else if (r.isDataFlavorSupported(VoDataFlavour.RESOURCE_LIST)) {
			manySelected((List)r.getTransferData(VoDataFlavour.RESOURCE_LIST));
		}
		super.selected(r);
		} catch (IOException x) {
			logger.error("UnsupportedFlavorException",x);			
		} catch (UnsupportedFlavorException x) {
			logger.error("UnsupportedFlavorException",x);
		}
	}

}
