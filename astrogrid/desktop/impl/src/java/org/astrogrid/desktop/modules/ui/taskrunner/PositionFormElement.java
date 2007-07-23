/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle;
import org.astrogrid.desktop.modules.ui.comp.NameResolvingPositionTextField;

/** Composite form element that edits an RA and DEC parameter.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20075:08:35 PM
 */
public class PositionFormElement extends AbstractTaskFormElement {

	private final ParameterValue dec;
    private final ParameterBean decDesc;
    private final  UIComponent parent;
    private final  Sesame ses;

    /**
	 * @param ra
	 * @param raDesc
	 * @param dec
	 * @param decDesc
	 * @param chooser 
	 */
	public PositionFormElement(ParameterValue ra, ParameterBean raDesc, ParameterValue dec, ParameterBean decDesc,  UIComponent parent,ResourceChooserInternal chooser, Sesame ses) {
	    super(ra,raDesc,chooser);
        this.dec = dec;
        this.decDesc = decDesc;
        this.parent = parent;
        this.ses = ses;
	}

    protected JComponent createEditor() {
        JPanel p = new JPanel(new BorderLayout());
        NameResolvingPositionTextField f = new NameResolvingPositionTextField(parent,ses);
        p.add(f,BorderLayout.CENTER);
        DecSexToggle t = new DecSexToggle();
        //p.add(,BorderLayout.SOUTH);
        t.addListener(f);
        
        return f;
    }

    protected String getStringValue() {
        return null;
    }
	
	
	

}
