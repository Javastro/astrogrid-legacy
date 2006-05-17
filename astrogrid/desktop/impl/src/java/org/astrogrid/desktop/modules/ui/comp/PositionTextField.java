package org.astrogrid.desktop.modules.ui.comp;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.vecmath.Point2d;

import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle.DecSexListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.starlink.ttools.func.Coords;

/**
 * UI filed that allows input of a point position
 * Description: Overrides TextField in dealing with positions in the sky and converting/finding positions in the sky.

 */ 
public class PositionTextField extends JFormattedTextField implements DecSexToggle.DecSexListener{


    
    public class DecimalPositionFormatter extends AbstractFormatter {

    	private NumberFormat nfd = NumberFormat.getNumberInstance();
		{
			nfd.setGroupingUsed(false);
			nfd.setMinimumFractionDigits(3);
		}
		public Object stringToValue(String arg0) throws ParseException {
			String[] nums = arg0.split(",");
			if (nums.length != 2) {
				throw new ParseException("Expected 2 numbers",0);
			}
			return new Point2D.Double(
					nfd.parse(nums[0]).doubleValue()
					,nfd.parse(nums[1]).doubleValue()
					);
		}

		public String valueToString(Object arg0) throws ParseException {
			Point2D dim = (Point2D)arg0;
			if (dim == null) {
				return null;
			}
				return nfd.format(dim.getX()) + "," + nfd.format(dim.getY());
		}
    }
    
    public class SexagesimalPositionFormatter extends AbstractFormatter {

    	public Object stringToValue(String arg0) throws ParseException {
			String[] nums = arg0.split(",");
			if (nums.length != 2) {
				throw new ParseException("Expected 2 numbers",0);
			}			
			return new Point2D.Double (
					PositionUtils.sexagesimalRaToDecimal(nums[0])
					,PositionUtils.sexagesimalDecToDecimal(nums[1])
							);
		}

		public String valueToString(Object arg0) throws ParseException {
			Point2D dim = (Point2D)arg0;
			if (dim == null) {
				return null;
			}
				return PositionUtils.decimalToSexagesimal(dim.getX(),dim.getY());
		}
    }
    
    /**
     * Method: PositionTextField constructor
     * Description: 
     *
     */
    public PositionTextField() {
        super();
        degreesSelected(null);
        Point2D p = new Point2D.Double(0,0);
        setFormatterFactory(new AbstractFormatterFactory() {

			public AbstractFormatter getFormatter(JFormattedTextField tf) {
				return isDecimal ? decimal : sexa;
			}
        });
        setValue(p);
    }
    
    
    private boolean isDecimal = true;
    protected AbstractFormatter decimal = new DecimalPositionFormatter();
    protected AbstractFormatter sexa = new SexagesimalPositionFormatter();
    
    /**
     * Method: PositionTextField constructor
     * Description: 
     * @param text initialize the text field with this string.
     */    
    public PositionTextField(String text) {
        this();
        setText(text);
    }
    

    /** return a copy of the position this field currently points at
     * @return position in decimal degrees 
     * <ul><li>x - ra value
     * <li>y - dec value
     * </ul> */
    public Point2D getPosition() {
    	return (Point2D)((Point2D)getValue()).clone();
    }
    
    
    public void setPosition(double ra,double dec) {
    	setValue(new Point2D.Double(ra,dec));
    }
    
    public void setPosition(Point2D pos) {
    	setValue(pos);
    }
    public void setPosition(String s) throws ParseException {
    	setValue(getFormatter().stringToValue(s));
    }
    
//    /**
//     * Method: getPositionDegrees
//     * Description: Returns a "ra,dec" type string in degrees format, it will convert and/or find objects if 
//     * necessary.
//     * @return a string in a "ra,dec" format in the unit of degrees.
//     */
//    private String getPositionDegrees() {
//        String pos = getText().trim();
//        if(hasFullRegion()) {        
//            return getPosition(pos.split(",")[0],DEGREES_TYPE,true) + "," + getPosition(pos.split(",")[1],DEGREES_TYPE,false);
//        }else {
//            if(ses != null) {
//                pos = getPositionFromObject();
//                if(PositionUtils.hasFullRegion(pos))
//                    return getPosition(pos.split(",")[0],DEGREES_TYPE,true) + "," + getPosition(pos.split(",")[1],DEGREES_TYPE,false);
//            }
//        }
//        return null;
//        //throw new IllegalArgumentException("Could not obtain position string with given input");
//    }
//   
//    
//    /**
//     * Method: getPositionSexagesimal
//     * Description: Returns a "ra,dec" type string in sexagesimal format, it will convert and/or find objects if 
//     * necessary.
//     * @return a string in a "ra,dec" format in the unit of sexagesimal.
//     */
//    private String getPositionSexagesimal() {
//        String pos = getText().trim();
//        if(hasFullRegion()) {        
//            return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2) + "," + Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
//        }else {
//            if(ses != null) {
//                pos = getPositionFromObject();
//                if(PositionUtils.hasFullRegion(pos))
//                    return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2) + "," + Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
//            }
//        }
//        return null;
//        //throw new IllegalArgumentException("Could not obtain position string with given input");
//    }
//    
//    
//    /**
//     * Method: hasFullRegion
//     * Description: Checks to make sure there is a "ra,dec" text string in that EXACT string format does not matter on the units.
//     * @return true/false if in that format of "ra,dec"
//     */
//    private boolean hasFullRegion() {
//        return PositionUtils.hasFullRegion(getText());
//    }
//    
//    /**
//     * Method: getRA()
//     * Description: get the current RA in the text box or find the object and return the RA in degrees unit.
//     * Advise to use getRA(getPosition??()) instead of this method, if you will also call getDEC and there
//     * is a object name to be looked up.
//     * @return
//     */
//    public double getRADegrees() {
//        String pos = getText().trim();
//        if(hasFullRegion()) {
//            return getPosition(pos.split(",")[0],DEGREES_TYPE,true);
//        }
//            pos = getPositionFromObject();
//            return getPosition(pos.split(",")[0],DEGREES_TYPE,true);
//    }
//    
//    /**
//     * Method: getDECDegrees()
//     * Description: get the current RA in the text box or find the object and return the RA in degrees unit.
//     * Advise to use getDEC(getPosition(int unitType)) instead of this method, if you will also call getRA and there
//     * is a object name or you need converting.
//     * @return
//     */    
//    public double getDECDegrees() {
//        String pos = getText().trim();
//        if(hasFullRegion()) {
//            return getPosition(pos.split(",")[1],DEGREES_TYPE,false);
//        }
//            pos = getPositionFromObject();
//            return getPosition(pos.split(",")[1],DEGREES_TYPE,false);
//
//    }
//    
//    
//    
//    /**
//     * Method: getRA()
//     * Description: get the current RA in the text box or find the object and return the RA in sexagesimal hh:mm:ss unit.
//     * Advise to use getRASexagesimal(getPosition??()) instead of this method, if you will also call getDEC and there
//     * is a object name to be looked up.
//     * @return
//     */
//    private String getRASexagesimal() {
//        String pos = getText().trim();
//        if(hasFullRegion()) {
//            return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2);
//        }
//            pos = getPositionFromObject();
//            return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2);
//
//    }
//    
//    /**
//     * Method: isSexagesimal
//     * Description: check if this seems to be in a sexagesimal format, currently just does it by checking if there is a ':' character.
//     * @return
//     */
//    private boolean isSexagesimal() {
//        return (getText().trim().indexOf(":") != -1);
//    }
//    
//    /**
//     * Method: getDECSexagesimal()
//     * Description: get the current dec in the text box or find the object and return the dec in sexagesimal unit degrees:mm:ss.
//     * Advise to use getDEC(getPosition??) instead of this method, if you will also call getRA and there
//     * is a object name or you need converting.
//     * @return
//     */    
//    private String getDECSexagesimal() {
//        String pos = getText().trim();
//        if(hasFullRegion()) {
//            return Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
//        }
//            pos = getPositionFromObject();
//            return Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
//    }        
//    
//    
//        
//    

	public void degreesSelected(EventObject e) {
		isDecimal = true;
		setValue(getValue()); // forces display to update.
	}

	public void sexaSelected(EventObject e) {
		isDecimal = false;
		setValue(getValue()); // forces display to update
	}    
}