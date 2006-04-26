package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.JTextField;
import org.astrogrid.acr.cds.Sesame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.starlink.ttools.func.Coords;

/**
 * Class: PositionTextField
 * Description: Overrides TextField in dealing with positions in the sky and converting/finding positions in the sky.
 * @todo  Needs refactoring this class is doing both finding and converting positions and/or objects.  The conversions should
 * be moved out to some other class.
 */
public class PositionTextField extends JTextField {
    
    
    private static final int DEGREES_TYPE = 1;    
    private static final int RADIANS_TYPE = 2;
    static final Log logger = LogFactory.getLog(PositionTextField.class);
    
    //name resolver.
    private Sesame ses;

    /**
     * Method: PositionTextField constructor
     * Description: 
     *
     */
    public PositionTextField() {
        super();
    }
    
    /**
     * Method: PositionTextField constructor
     * Description: 
     * @param text initialize the text field with this string.
     */    
    public PositionTextField(String text) {
        super(text);
    }
    
    /**
     * Method: PositionTextField constructor
     * Description: constructor to use the Sesame object finding web service.
     * @param ses cds type resolover service for resolving given objects into positions in the sky.
     */
    public PositionTextField(Sesame ses) {
        this();
        this.ses = ses;
    }
    
    /**
     * Method: PositionTextField constructor
     * @param text initialize the text field with this string.
     * @param ses cds type resolover service for resolving given objects into positions in the sky.
     */
    public PositionTextField(String text, Sesame ses) {
        this(text);
        this.ses = ses;
    }
    
    /**
     * Method: getPositionDegrees
     * Description: Returns a "ra,dec" type string in degrees format, it will convert and/or find objects if 
     * necessary.
     * @return a string in a "ra,dec" format in the unit of degrees.
     */
    public String getPositionDegrees() {
        String pos = getText().trim();
        if(hasFullRegion()) {        
            return getPosition(pos.split(",")[0],DEGREES_TYPE,true) + "," + getPosition(pos.split(",")[1],DEGREES_TYPE,false);
        }else {
            if(ses != null) {
                pos = getPositionFromObject();
                if(hasFullRegion(pos))
                    return getPosition(pos.split(",")[0],DEGREES_TYPE,true) + "," + getPosition(pos.split(",")[1],DEGREES_TYPE,false);
            }
        }
        return null;
        //throw new IllegalArgumentException("Could not obtain position string with given input");
    }
    
    /**
     * Method: getPositionSexagesimal
     * Description: Returns a "ra,dec" type string in sexagesimal format, it will convert and/or find objects if 
     * necessary.
     * @return a string in a "ra,dec" format in the unit of sexagesimal.
     */
    public String getPositionSexagesimal() {
        String pos = getText().trim();
        if(hasFullRegion()) {        
            return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2) + "," + Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
        }else {
            if(ses != null) {
                pos = getPositionFromObject();
                if(hasFullRegion(pos))
                    return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2) + "," + Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
            }
        }
        return null;
        //throw new IllegalArgumentException("Could not obtain position string with given input");
    }
    
    
    /**
     * Method: hasFullRegion
     * Description: Checks to make sure there is a "ra,dec" text string in that EXACT string format does not matter on the units.
     * @return true/false if in that format of "ra,dec"
     */
    public boolean hasFullRegion() {
        return hasFullRegion(getText());
    }
    
    private static boolean hasFullRegion(String pos) {
        if(pos == null || pos.trim().length() == 0) {
            return false;
        }        
        return (pos.trim().indexOf(",") != -1);
    }

    /**
     * Method: getRA()
     * Description: get the current RA in the text box or find the object and return the RA in degrees unit.
     * Advise to use getRA(getPosition??()) instead of this method, if you will also call getDEC and there
     * is a object name to be looked up.
     * @return
     */
    public double getRADegrees() {
        String pos = getText().trim();
        if(hasFullRegion()) {
            return getPosition(pos.split(",")[0],DEGREES_TYPE,true);
        }
            pos = getPositionFromObject();
            return getPosition(pos.split(",")[0],DEGREES_TYPE,true);
    }
    
    /**
     * Method: getDECDegrees()
     * Description: get the current RA in the text box or find the object and return the RA in degrees unit.
     * Advise to use getDEC(getPosition(int unitType)) instead of this method, if you will also call getRA and there
     * is a object name or you need converting.
     * @return
     */    
    public double getDECDegrees() {
        String pos = getText().trim();
        if(hasFullRegion()) {
            return getPosition(pos.split(",")[1],DEGREES_TYPE,false);
        }
            pos = getPositionFromObject();
            return getPosition(pos.split(",")[1],DEGREES_TYPE,false);

    }
    
    
    
    /**
     * Method: getRA()
     * Description: get the current RA in the text box or find the object and return the RA in sexagesimal hh:mm:ss unit.
     * Advise to use getRASexagesimal(getPosition??()) instead of this method, if you will also call getDEC and there
     * is a object name to be looked up.
     * @return
     */
    public String getRASexagesimal() {
        String pos = getText().trim();
        if(hasFullRegion()) {
            return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2);
        }
            pos = getPositionFromObject();
            return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2);

    }
    
    /**
     * Method: isSexagesimal
     * Description: check if this seems to be in a sexagesimal format, currently just does it by checking if there is a ':' character.
     * @return
     */
    public boolean isSexagesimal() {
        return (getText().trim().indexOf(":") != -1);
    }
    
    /**
     * Method: getDECSexagesimal()
     * Description: get the current dec in the text box or find the object and return the dec in sexagesimal unit degrees:mm:ss.
     * Advise to use getDEC(getPosition??) instead of this method, if you will also call getRA and there
     * is a object name or you need converting.
     * @return
     */    
    public String getDECSexagesimal() {
        String pos = getText().trim();
        if(hasFullRegion()) {
            return Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
        }
            pos = getPositionFromObject();
            return Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
    }        
    
    /**
     * Method: getRADegrees
     * Descrption: gets the RA from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
     * sesame.
     * @param pos a string position that MUST be in the format of "ra,dec"
     * @return the ra in a degrees unit.
     */
    public static double getRADegrees(String pos) {
        if(hasFullRegion(pos))
            return getPosition(pos.split(",")[0],DEGREES_TYPE, true);
        throw new NumberFormatException("No number");
    }

    /**
     * Method: getDECDegrees
     * Descrption: gets the dec from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
     * sesame.
     * @param pos a string position that MUST be in the format of "ra,dec"
     * @return the dec in a degrees unit.
     */
    public static double getDECDegrees(String pos) {
        if(hasFullRegion(pos))
            return getPosition(pos.split(",")[1],DEGREES_TYPE, false);
        throw new NumberFormatException("No number");        
    }
    
    /**
     * Method: getRASexagesimal
     * Descrption: gets the ra from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
     * sesame.
     * @param pos a string position that MUST be in the format of "ra,dec"
     * @return the ra in a sexagesimal unit hh:mm:ss.
     */
    public static String getRASexagesimal(String pos) {
        if(hasFullRegion(pos))
            return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2);
        throw new NumberFormatException("No number");        
    }

    /**
     * Method: getDecSexagesimal
     * Descrption: gets the dec from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
     * sesame.
     * @param pos a string position that MUST be in the format of "ra,dec"
     * @return the dec in a sexagesimal unit degrees:mm:ss.
     */
    public static String getDECSexagesimal(String pos) {
        if(hasFullRegion(pos))
            return Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
        throw new NumberFormatException("No number");        
    }
    

    /**
     * Method: getPosition
     * Description: Does the necessary checking and unit conversion to make a position string in the form of
     * "ra,dec".
     * @param pos a string position in any (Sexagesimal or degrees) unit format in the format of "ra,dec"
     * @param unitType the type to be converted in currently only degrees and radians.
     * @param raPosition boolean to check if this is the "ra" part of the string. Makes a difference in conversions from
     * sexagesimal dealing with hms (hour-minuts-seconds) and dms (degrees-minutes-seconds) 
     * @return
     */
    private static double getPosition(String pos, int unitType, boolean raPosition) {        
        if(pos.indexOf(':') != -1) {
            if(unitType == DEGREES_TYPE)
                if(raPosition)
                    return Coords.radiansToDegrees(Coords.hmsToRadians(pos));
                else
                    return Coords.radiansToDegrees(Coords.dmsToRadians(pos));
            else
                if(raPosition)
                    return Coords.hmsToRadians(pos);
                else
                    return Coords.dmsToRadians(pos);
        }
        
        if(unitType == DEGREES_TYPE)
            return Double.parseDouble(pos);        
        return Coords.degreesToRadians(Double.parseDouble(pos));
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