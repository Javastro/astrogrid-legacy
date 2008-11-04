/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.adqlEditor.ADQLEditorPanel;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;

/** A {@link LargeTextFormElement} which uses an adql/s pane from an ADQL editor.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 20, 20072:26:23 PM
 */
public class AdqlTextFormElement extends AbstractTaskFormElement implements MouseListener, FocusListener{

    private final ADQLEditorPanel.AdqlsView adqls;
    private final ADQLEditorPanel adqled;

    /**
     * @param pval
     * @param pdesc
     * @param chooser
     */
    public AdqlTextFormElement(final ParameterValue pval, final ParameterBean pdesc,
           final CeaApplication app, final UIComponentWithMenu parent,  final ResourceChooserInternal chooser, final TypesafeObjectBuilder builder ) {
        super(pval, pdesc, chooser);
        this.adqled = builder.createAdqlEditorPanel(pval,app,parent);
        this.adqls = adqled.new AdqlsView(false);
        adqls.addMouseListener(this);
        adqls.addFocusListener(this);
        CSH.setHelpIDString(getEditor(),"task.form.adql");  
    }
    


    protected JComponent createEditor() {
        final JScrollPane sp = new JScrollPane(adqls,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //@todo do something with the default value.
        sp.setPreferredSize(new Dimension(100,100));
        return sp;
    }

    protected String getStringValue() {
        // will already be in the pval??
        return pval.getValue(); //@todo check this is correct.
    }
    
    // overriden to _not_ grey out parameter title.
    public void setEnabled(final boolean b) {
        indirectToggle.setVisible(b);
        getEditor().setVisible(b);
        if (addButton != null) { // no point allowing more to be added if this is disabled..
            addButton.setVisible(b);
        }
    
    }

// document listener interface - think this will cause too much noise / loops
    // hope this isn't too noisy - or that it causes an infinite loop.

    public void changedUpdate(final DocumentEvent e) {
       adqls.executeEditCommand();
    }

    public void insertUpdate(final DocumentEvent e) {
        adqls.executeEditCommand();        
    }

    public void removeUpdate(final DocumentEvent e) {
        adqls.executeEditCommand();        
    }


//mouse listener interface
    public void mouseClicked(final MouseEvent e) {
    }



    public void mouseEntered(final MouseEvent e) {
    }



    public void mouseExited(final MouseEvent e) {
        adqls.executeEditCommand();
    }



    public void mousePressed(final MouseEvent e) {
    }



    public void mouseReleased(final MouseEvent e) {
    }


// focus listener interface
    public void focusGained(final FocusEvent e) {
    }



    public void focusLost(final FocusEvent e) {
        adqls.executeEditCommand();
    }



    /**
     * @return the full editor panel
     */
    public ADQLEditorPanel getEditorPanel() {
        return adqled;
    }
 

}
