package org.astrogrid.acr.astrogrid;

import java.util.Comparator;
/** comparator for table beans.
 * 
 * tests for equality on name of the table only
 * @author Noel Winstanley
 * @since Aug 24, 200611:56:35 PM
 */
public class TableBeanComparator implements Comparator {

	public int compare( Object o1, Object o2 ) {
		
		TableBean table1 = (TableBean)o1 ;
		TableBean table2 = (TableBean)o2 ;
		
		return table1.name.compareTo( table2.name) ;

	}

}
