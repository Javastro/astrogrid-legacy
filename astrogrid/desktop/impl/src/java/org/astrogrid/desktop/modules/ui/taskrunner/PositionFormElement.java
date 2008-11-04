/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.Box;
import javax.swing.JComponent;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle;
import org.astrogrid.desktop.modules.ui.comp.NameResolvingPositionTextField;

/** Composite form element that edits RA and Dec parameters.
 * 
 * needs a bit of trickery to shoehorn 2 parameters into 1 space.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20075:08:35 PM
 */
public class PositionFormElement extends AbstractTaskFormElement implements PropertyChangeListener{

    private static final Log logger = LogFactory
    .getLog(PositionFormElement.class);
    private final ParameterValue dec;
    private final ParameterBean decDesc;
    private final UIComponent parent;
    private final Sesame ses;
    private final ParameterValue ra;
    private final ParameterBean raDesc;
    private NameResolvingPositionTextField positionField;
    private DecSexToggle toggle;

    public PositionFormElement(final ParameterValue ra, final ParameterBean raDesc, final ParameterValue dec, final ParameterBean decDesc,  final UIComponent parent,final ResourceChooserInternal chooser, final Sesame ses) {
        super(ra /*ignored*/,new CompositeParameterBean(raDesc,decDesc)
        ,chooser);
        this.ra = ra;
        this.raDesc = raDesc;	    
        this.dec = dec;
        this.decDesc = decDesc;
        this.parent = parent;
        this.ses = ses;

           disableIndirect();
           CSH.setHelpIDString(getEditor(),"task.form.position");           
    }

    @Override
    protected JComponent createEditor() {
        // setup default values.
        if (StringUtils.isEmpty(ra.getValue()) ) {
            ra.setValue(StringUtils.isEmpty(raDesc.getDefaultValue()) ? "0.0" : raDesc.getDefaultValue());
        }
        if (StringUtils.isEmpty(dec.getValue())) {
            dec.setValue(StringUtils.isEmpty(decDesc.getDefaultValue()) ? "0.0" : decDesc.getDefaultValue());
        }
        logger.debug("ra :" + ra.getValue());
        logger.debug("dec:" + dec.getValue());        

        positionField = new NameResolvingPositionTextField(parent,ses);
        positionField.addPropertyChangeListener(NameResolvingPositionTextField.VALUE_PROPERTY,this);
        try { 
            positionField.setPosition(ra.getValue() + "," + dec.getValue());
        } catch (final ParseException x) {
            logger.error("ParseException",x);
        }
        toggle = new DecSexToggle();
        toggle.addListener(positionField);

        final Box p = Box.createVerticalBox();
        p.add(positionField);
        p.add(toggle.getDegreesRadio());
        p.add(toggle.getSexaRadio());
        return p;
    }

    // only called when flipping between direct and indirect view - and as we've 
    // disabled this, it'll never get called.
    // as this is the case, means that all alterations to values of ra and dec occur within
    // this class only - and not in the base class.
    @Override
    protected String getStringValue() {
        throw new RuntimeException("unimplemented - as should never be called");
    }

    // copy contents of position field back into ra and dec.
    public void updateParameters() {
        final Point2D position = positionField.getPosition();
        logger.debug("updating position to" + position);
        ra.setValue(Double.toString(position.getX()));
        dec.setValue(Double.toString(position.getY()));
    }


    public void propertyChange(final PropertyChangeEvent evt) {
        updateParameters();
    }

    /** A udged parameter bean which describes both the RA and Dec  parameters.
     * just used for UI display. */
    private static class CompositeParameterBean extends ParameterBean {

        public CompositeParameterBean(final ParameterBean raDesc, final ParameterBean decDesc) {
            super(raDesc.getName() // unimportant
                    , raDesc.getUiName() + ","+ decDesc.getUiName()
                    ,raDesc.getUiName() + " : " + raDesc.getDescription() 
                    + "<p >" 
                    + decDesc. getUiName() + ":" + decDesc.getDescription()
                    ,raDesc.getUcd()+","+decDesc.getUcd()
                    ,raDesc.getDefaultValue() // unused
                    ,raDesc.getUnits()+","+decDesc.getUnits()
                    , raDesc.getType() + ", " + decDesc.getType()
                    , raDesc.getSubType() // unused
                    ,raDesc.getOptions() // unused
            );
        }
    }

    /**
     * @return the toggle
     */
    public final DecSexToggle getToggle() {
        return this.toggle;
    }





}
