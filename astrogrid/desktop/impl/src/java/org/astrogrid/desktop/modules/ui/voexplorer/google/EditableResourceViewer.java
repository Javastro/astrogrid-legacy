/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import javax.swing.event.ChangeListener;

/** Extended interface to a resource viewer that 
 * allows editing of the resource in some way.
 * 
 * it's the resource viewer's responsibility to persist the saved data
 * but this interface provides a way for the viewer to notify the parent
 * ui of changes - which would require updates to the resource table, etc
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 10, 20072:03:26 PM
 */
public interface EditableResourceViewer extends ResourceViewer {
    public void addChangeListener(ChangeListener e) ;
    public void removeChangeListener(ChangeListener e);
}