/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import java.io.IOException;

import org.astrogrid.acr.util.Tables;

import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.StarTableOutput;
import uk.ac.starlink.table.StoragePolicy;

/** Implementation of the tables utility.
 * @author Noel Winstanley
 * @since Aug 25, 20061:31:28 AM
 */
public class TablesImpl implements Tables {

	public TablesImpl() {
		// configure to be more memory-efficient.
		StoragePolicy.setDefaultPolicy( StoragePolicy.PREFER_DISK ) ;
		
	}
	
	
	public void convert(String inLocation, String inFormat
			, String outLocation, String outFormat)
			throws IOException {
		
		 new StarTableOutput().writeStarTable( new StarTableFactory( false ) 
         .makeStarTable( inLocation, 
                         inFormat ), 
          outLocation, outFormat ); 
	}

}
