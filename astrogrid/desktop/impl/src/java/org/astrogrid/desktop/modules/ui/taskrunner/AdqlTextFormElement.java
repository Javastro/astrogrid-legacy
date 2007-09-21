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
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** subclass of largetextform which uses an adql/s pane from an ADQL editor.
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
    public AdqlTextFormElement(ParameterValue pval, ParameterBean pdesc,
           CeaApplication app, UIComponent parent,  ResourceChooserInternal chooser, TypesafeObjectBuilder builder ) {
        super(pval, pdesc, chooser);
        this.adqled = builder.createAdqlEditorPanel(pval,app,parent);
        this.adqls = adqled.new AdqlsView(false);
        adqls.addMouseListener(this);
        adqls.addFocusListener(this);
        
    }
    


    protected JComponent createEditor() {
        JScrollPane sp = new JScrollPane(adqls,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //@todo do something with the default value.
        sp.setPreferredSize(new Dimension(100,100));
        return sp;
    }

    protected String getStringValue() {
        // will already be in the pval??
        return pval.getValue(); //@todo check this is correct.
    }
    
    // overriden to _not_ grey out parameter title.
    public void setEnabled(boolean b) {
        indirectToggle.setVisible(b);
        getEditor().setVisible(b);
        if (addButton != null) { // no point allowing more to be added if this is disabled..
            addButton.setVisible(b);
        }
    
    }

// document listener interface - think this will cause too much noise / loops
    // hope this isn't too noisy - or that it causes an infinite loop.

    public void changedUpdate(DocumentEvent e) {
       adqls.executeEditCommand();
    }

    public void insertUpdate(DocumentEvent e) {
        adqls.executeEditCommand();        
    }

    public void removeUpdate(DocumentEvent e) {
        adqls.executeEditCommand();        
    }


//mouse listener interface
    public void mouseClicked(MouseEvent e) {
    }



    public void mouseEntered(MouseEvent e) {
    }



    public void mouseExited(MouseEvent e) {
        adqls.executeEditCommand();
    }



    public void mousePressed(MouseEvent e) {
    }



    public void mouseReleased(MouseEvent e) {
    }


// focus listener interface
    public void focusGained(FocusEvent e) {
    }



    public void focusLost(FocusEvent e) {
        adqls.executeEditCommand();
    }



    /**
     * @return the full editor panel
     */
    public ADQLEditorPanel getEditorPanel() {
        return adqled;
    }
 

}
