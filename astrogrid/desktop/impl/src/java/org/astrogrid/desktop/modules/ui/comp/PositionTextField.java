package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.cds.Sesame;

/**
 * Class: PositionTextField
 * Description: Overrides TextField in dealing with positions in the sky and converting/finding positions in the sky.
 * Currently not being used at the moment till the unit conversion tool comes into affect.
 */
public class PositionTextField extends JTextField {
    
    
    private static final int DEGREES_TYPE = 1;    
    private static final int SEXAGESIMAL_TYPE = 2;
    static final Log logger = LogFactory.getLog(PositionTextField.class);
    
    //name resolver.
    private Sesame ses;

    public PositionTextField() {
        super();
    }
    
    public PositionTextField(String text) {
        super(text);
    }
    
    /**
     * Method: PositionTextField constructor
     * Description: constructor to use the Sesame object finding web service.
     * @param ses
     */
    public PositionTextField(Sesame ses) {
        this();
        this.ses = ses;
    }
    
    public PositionTextField(String text, Sesame ses) {
        this(text);
        this.ses = ses;
    }
    
    /**
     * Method: getPosition
     * Description: Returns a "ra,dec" type string, it will convert and/or find positions if necessary
     * depending on the unitType passed in.  unitType is a type defined in this class of what unit they wish to have
     * results in. 
     * @return
     */
    public String getPosition(int unitType) {
        String pos = getText().trim();
        return getPosition(pos,unitType);
    }
    
    /**
     * Method: hasFullRegion
     * Description: Checks to make sure there is a "ra,dec" text string.
     * @return
     */
    public boolean hasFullRegion() {
        return hasFullRegion(getText());
    }
    
    private boolean hasFullRegion(String pos) {
        if(pos == null || pos.trim().length() == 0) {
            return false;
        }        
        return (pos.trim().indexOf(",") != -1);
    }

    /**
     * Method: getRA()
     * Description: get the current RA in the text box or find the object and return the RA in degrees unit.
     * Advise to use getRA(getPosition(int unitType)) instead of this method, if you will also call getDEC and there
     * is a object name or you need converting.
     * @return
     */
    public String getRA() {
        if(hasFullRegion()) {
            return getText().trim().split(",")[0];
        }else {
            return getRA(getPositionFromObject());
        }
    }
    
    /**
     * Method: getDEC()
     * Description: get the current RA in the text box or find the object and return the RA in degrees unit.
     * Advise to use getDEC(getPosition(int unitType)) instead of this method, if you will also call getRA and there
     * is a object name or you need converting.
     * @return
     */    
    public String getDEC() {
        String []val;
        if(hasFullRegion()) {
            return getText().trim().split(",")[1];
        }else {
            return getDEC(getPositionFromObject());
        }
    }        
    
    /**
     * Method: getRA
     * Descrption: gets the RA from a particular position string.  Assumes position is in "ra,dec" type string normally
     * from calling getPosition(int unitType)
     * @param pos
     * @return
     */
    public String getRA(String pos) {
        if(hasFullRegion(pos)) {
            return pos.trim().split(",")[0];
        }
        return null;
    }

    /**
     * Method: getDEC
     * Descrption: gets the DEC from a particular position string.  Assumes position is in "ra,dec" type string normally
     * from calling getPosition(int unitType)
     * @param pos
     * @return
     */    
    public String getDEC(String pos) {
        if(hasFullRegion(pos)) {
            return pos.trim().split(",")[1];
        }
        return null;
    }

    /**
     * Method: getPosition
     * Description: Does the necessary checking and unit conversion to make a position string in the form of
     * "ra,dec", if need be it will look for an object in the sky using the Sesame service if provided and convert it
     * appropriately.
     * @param pos
     * @param unitType
     * @return
     */
    private String getPosition(String pos, int unitType) {        
        if(pos == null || pos.trim().length() == 0) {
            return null;
        }        
        String expression = "\\+?-?\\d+\\.?\\d*";        
        if(pos.matches(expression)) {
            //its in degrees make it a switch statement
            if(unitType == DEGREES_TYPE)
                return pos;
            else {
                //conversion needed.
            }
        }
        expression = "\\d+\\:\\d+(\\:\\d+(\\.\\d)?)?\\s\\+?-?\\d+\\.?\\d*";
        if(pos.matches(expression)) {
            //its in degrees make it a switch statement
            if(unitType == SEXAGESIMAL_TYPE)
                return pos;
            else {
                //conversion needed.
            }            
        }
        if(ses != null) {
            pos = getPositionFromObject();
            if (pos != null) {
                return getPosition(pos,unitType);
            }
        }
        return null;
    }
        
    /**
     * method: getPositionFromObject
     * Description: Queries CDS-Simbad service for a position in the sky based on a object name.  This is typically 
     * called if the user enters an invalid position then it will attempt a lookup.
     * @return position in the sky based on a object name.
     */
    private String getPositionFromObject() {
        String pos = null;  
        String inputPos = getText().trim();
        try {
            String temp = ses.sesame(inputPos,"x");
            //logger.debug("here is the xml response from sesame = " + temp);            
            pos = temp.substring(temp.indexOf("<jradeg>")+ 8, temp.indexOf("</jradeg>"));
            pos += "," + temp.substring(temp.indexOf("<jdedeg>")+ 8, temp.indexOf("</jdedeg>"));
            //logger.debug("here is the position extracted from sesame = " + pos);
        }catch(Exception e) {
            //hmmm I think glueservice is throwing an exception but things seem to be okay.
            //e.printStackTrace();
                logger.debug("error from sesame - ho hum",e);
        }
        return pos;
    }    
    
    
}