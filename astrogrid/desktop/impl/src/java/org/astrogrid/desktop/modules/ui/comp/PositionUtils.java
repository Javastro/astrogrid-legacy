/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

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

	
	/** convert a single right-ascension from decimal degrees to sexagesimal */
	public static String decimalRaToSexagesimal(final double ra) {
		return Coords.radiansToHms(Coords.degreesToRadians(ra),2);
	}
	
	/** convert a single declination from decimal degres to sexagesimal */
	public static String decimalDecToSexagesimal(final double dec) {
		return Coords.radiansToDms(Coords.degreesToRadians(dec),2);
	}
	

//	/** convert a decimal degrees position to equivalent sexagesimal position
//	 * 
//	 * @param decimalPosition a string position that MUST be in the format ra,dec
//	 * @return the positioin in a sexagesimal as hh:mm:ss, degrees:mm:ss
//	 */
//	public static String decimalToSexagesimal(final String decimalPosition) {
//		final String[] arr = decimalPosition.split(",");
//		return decimalToSexagesimal(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]));
//	}
	
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
	
//	/** convert a sexagesimal position to a decimal degrees position */
//	public static Point2D sexagesimalToDecimal(final String sexa) {
//		final String[] arr = sexa.split(",");
//		return new java.awt.geom.Point2D.Double(
//				sexagesimalRaToDecimal(arr[0])
//				,sexagesimalDecToDecimal(arr[1])
//				);
//	}
		
	
	
}
