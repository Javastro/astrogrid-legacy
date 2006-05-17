/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import uk.ac.starlink.ttools.func.Coords;

/** Class of utility methods for working with position.
 * @author Noel Winstanley
 * @since May 15, 20067:16:40 PM
 */
public class PositionUtils {
/**
 * 
 */
private PositionUtils() {
}
	/**
	gets the RA from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
	 * sesame.
	 * @param pos a string position that MUST be in the format of "ra,dec"
	 * @return the ra in a degrees unit.
	 */
	public static double getRADegrees(String pos) {
	    if(PositionUtils.hasFullRegion(pos))
	        return getPosition(pos.split(",")[0],DEGREES_TYPE, true);
	    throw new NumberFormatException("No number");
	}

	/**
	gets the dec from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
	 * sesame.
	 * @param pos a string position that MUST be in the format of "ra,dec"
	 * @return the dec in a degrees unit.
	 */
	public static double getDECDegrees(String pos) {
	    if(PositionUtils.hasFullRegion(pos))
	        return getPosition(pos.split(",")[1],DEGREES_TYPE, false);
	    throw new NumberFormatException("No number");        
	}

	/**
 	gets the dec from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
	 * sesame.
	 * @param pos a string position that MUST be in the format of "ra,dec"
	 * @return the dec in a sexagesimal unit degrees:mm:ss.
	 */
	public static String getDECSexagesimal(String pos) {
	    if(PositionUtils.hasFullRegion(pos))
	        return Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
	    throw new NumberFormatException("No number");        
	}

	/**
	 *  gets the ra from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
	 * sesame.
	 * @param pos a string position that MUST be in the format of "ra,dec"
	 * @return the ra in a sexagesimal unit hh:mm:ss.
	 */
	public static String getRASexagesimal(String pos) {
	    if(PositionUtils.hasFullRegion(pos))
	        return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2);
	    throw new NumberFormatException("No number");        
	}
	
	
	public static String decimalRaToSexagesimal(double ra) {
		return Coords.radiansToHms(Coords.degreesToRadians(ra),2);
	}
	
	public static String decimalDecToSexagesimal(double dec) {
		return Coords.radiansToDms(Coords.degreesToRadians(dec),2);
	}
	

	/** convert a decimal degrees position to equivalent sexagesimal position
	 * 
	 * @param decimalPosition a string position that MUST be in the format ra,dec
	 * @return the positioin in a sexagesimal as hh:mm:ss, degrees:mm:ss
	 */
	public static String decimalToSexagesimal(String decimalPosition) {
		String[] arr = decimalPosition.split(",");
		return decimalToSexagesimal(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]));
	}
	
	public static String decimalToSexagesimal(double ra,double dec) {
		return decimalRaToSexagesimal(ra) + "," 
		+ decimalDecToSexagesimal(dec);
	}
	
	
	public static double sexagesimalRaToDecimal(String sexaRa){
		return Coords.radiansToDegrees(Coords.hmsToRadians(sexaRa));
	}
	
	public static double sexagesimalDecToDecimal(String sexaDec) {
		return Coords.radiansToDegrees(Coords.dmsToRadians(sexaDec));		
	}
	
	public static Point2D sexagesimalToDecimal(String sexa) {
		String[] arr = sexa.split(",");
		return new java.awt.geom.Point2D.Double(
				sexagesimalRaToDecimal(arr[0])
				,sexagesimalDecToDecimal(arr[1])
				);
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
   * @todo rename
   */
  public static double getPosition(String pos, int unitType, boolean raPosition) {        
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
	
	static boolean hasFullRegion(String pos) {
	    if(pos == null || pos.trim().length() == 0) {
	        return false;
	    }        
	    return (pos.indexOf(",") != -1);
	}

    private static final int DEGREES_TYPE = 1;    
    private static final int RADIANS_TYPE = 2;
}
