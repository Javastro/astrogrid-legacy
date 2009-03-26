/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JComponent;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle;
import org.astrogrid.desktop.modules.ui.comp.RadiusTextField;

/** Form element for a radius value
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 30, 200711:19:36 PM
 */
public class RadiusFormElement extends AbstractTaskFormElement implements PropertyChangeListener {

    private final boolean isExternalToggle;

    /** create a new form, which listens to sex-dec changes from an
     * external toggle
     * @param pval
     * @param pdesc
     * @param fileChooser
     * @param externalToggle
     */
    public RadiusFormElement(ParameterValue pval, ParameterBean pdesc,
            ResourceChooserInternal fileChooser, DecSexToggle externalToggle) {
        super(pval, pdesc, fileChooser);
        this.isExternalToggle = true;
        this.toggle = externalToggle;
        disableIndirect();
        CSH.setHelpIDString(getEditor(),"task.form.radius");
    }
    
    /** create a new form, which creates and displays it's own
     * sex-dec toggle.
     * @param pval
     * @param pdesc
     * @param fileChooser
     */
    public RadiusFormElement(ParameterValue pval, ParameterBean pdesc,
            ResourceChooserInternal fileChooser) {
        super(pval, pdesc, fileChooser);
        this.toggle = new DecSexToggle();
        this.isExternalToggle = false;
        CSH.setHelpIDString(getEditor(),"task.form.radius");
    }
    
    private final DecSexToggle toggle;
    private RadiusTextField radiusField;

    @Override
    protected JComponent createEditor() {
        // setup default values.
        if (StringUtils.isEmpty(pval.getValue())) {
            pval.setValue(StringUtils.isEmpty(pdesc.getDefaultValue()) ? "0.1": pdesc.getDefaultValue());
        }
        
        radiusField = new RadiusTextField(Double.parseDouble(pval.getValue()));
        radiusField.addPropertyChangeListener(RadiusTextField.VALUE_PROPERTY,this);
        toggle.addListener(radiusField);
        if (isExternalToggle) {
            return radiusField;
        } else {
            Box p = Box.createVerticalBox();
            p.add(radiusField);
            p.add(toggle.getDegreesRadio());
            p.add(toggle.getSexaRadio());

            return p;            
        }
    }

    @Override
    protected String getStringValue() {
        return Double.toString(radiusField.getRadius());
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // notification of a value change.
        pval.setValue(Double.toString(radiusField.getRadius()));
    }

}
