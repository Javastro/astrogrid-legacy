/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.geom.Point2D;

import uk.ac.starlink.ttools.func.Coords;

/** Utility methods for working with position.
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
	 * @deprecated as uses getPosition
	 */
	@Deprecated
    public static double getRADegrees(final String pos) {
	    if(PositionUtils.hasFullRegion(pos)) {
            return getPosition(pos.split(",")[0],DEGREES_TYPE, true);
        }
	    throw new NumberFormatException("No number");
	}

	/**
	gets the dec from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
	 * sesame.
	 * @param pos a string position that MUST be in the format of "ra,dec"
	 * @return the dec in a degrees unit
	 * @deprecated as uses getPosition.
	 */
	@Deprecated
    public static double getDECDegrees(final String pos) {
	    if(PositionUtils.hasFullRegion(pos)) {
            return getPosition(pos.split(",")[1],DEGREES_TYPE, false);
        }
	    throw new NumberFormatException("No number");        
	}

	/**
 	gets the dec from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
	 * sesame.
	 * @param pos a string position that MUST be in the format of "ra,dec"
	 * @return the dec in a sexagesimal unit degrees:mm:ss.
	 * @deprecated as uses getPosition.	 * 
	 */
	@Deprecated
    public static String getDECSexagesimal(final String pos) {
	    if(PositionUtils.hasFullRegion(pos)) {
            return Coords.radiansToDms(getPosition(pos.split(",")[1],RADIANS_TYPE,false),2);
        }
	    throw new NumberFormatException("No number");        
	}

	/**
	 *  gets the ra from a particular position string.  Performans any conversions if necessary, but does NOT do a object lookup via
	 * sesame.
	 * @param pos a string position that MUST be in the format of "ra,dec"
	 * @return the ra in a sexagesimal unit hh:mm:ss.
	 * @deprecated as uses getPosition.	 * 
	 */
	@Deprecated
    public static String getRASexagesimal(final String pos) {
	    if(PositionUtils.hasFullRegion(pos)) {
            return Coords.radiansToHms(getPosition(pos.split(",")[0],RADIANS_TYPE,true),2);
        }
	    throw new NumberFormatException("No number");        
	}
	
	/** convert a single right-ascension from decimal degrees to sexagesimal */
	public static String decimalRaToSexagesimal(final double ra) {
		return Coords.radiansToHms(Coords.degreesToRadians(ra),2);
	}
	
	/** convert a single declination from decimal degres to sexagesimal */
	public static String decimalDecToSexagesimal(final double dec) {
		return Coords.radiansToDms(Coords.degreesToRadians(dec),2);
	}
	

	/** convert a decimal degrees position to equivalent sexagesimal position
	 * 
	 * @param decimalPosition a string position that MUST be in the format ra,dec
	 * @return the positioin in a sexagesimal as hh:mm:ss, degrees:mm:ss
	 */
	public static String decimalToSexagesimal(final String decimalPosition) {
		final String[] arr = decimalPosition.split(",");
		return decimalToSexagesimal(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]));
	}
	
	/** convert a decimal degrees position to equivalent sexagesimal position */
	public static String decimalToSexagesimal(final double ra,final double dec) {
		return decimalRaToSexagesimal(ra) + "," 
		+ decimalDecToSexagesimal(dec);
	}
	
	/** convert a sexagesimal right-ascension to decimal degrees */
	public static double sexagesimalRaToDecimal(final String sexaRa){
		return Coords.radiansToDegrees(Coords.hmsToRadians(sexaRa));
	}
	
	/** conveert a sexagesimal declination to decimal degrees */
	public static double sexagesimalDecToDecimal(final String sexaDec) {
		return Coords.radiansToDegrees(Coords.dmsToRadians(sexaDec));		
	}
	
	/** convert a sexagesimal position to a decimal degrees position */
	public static Point2D sexagesimalToDecimal(final String sexa) {
		final String[] arr = sexa.split(",");
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
   * 
   * @deprecated  am wary about this method - it does too much, depending what flags and values are passed into it.
   * I much prefer methods that do one thing well - then the client can select what is intended to happen by calling specific method names,
   * rather than relying on internal magic to 'do the right thing'
   */
  @Deprecated
public static double getPosition(final String pos, final int unitType, final boolean raPosition) {        
      if(pos.indexOf(':') != -1) {
          if(unitType == DEGREES_TYPE) {
            if(raPosition) {
                return Coords.radiansToDegrees(Coords.hmsToRadians(pos));
            } else {
                return Coords.radiansToDegrees(Coords.dmsToRadians(pos));
            }
        } else
              if(raPosition) {
                return Coords.hmsToRadians(pos);
            } else {
                return Coords.dmsToRadians(pos);
            }
      }
       
      if(unitType == DEGREES_TYPE) {
        return Double.parseDouble(pos);
    }        
      return Coords.degreesToRadians(Double.parseDouble(pos));
  }
	
	static boolean hasFullRegion(final String pos) {
	    if(pos == null || pos.trim().length() == 0) {
	        return false;
	    }        
	    return (pos.indexOf(',') != -1);
	}

    public static final int DEGREES_TYPE = 1;    
    public static final int RADIANS_TYPE = 2;
}
