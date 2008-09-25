package org.astrogrid.acr.astrogrid;

import java.util.Comparator;
/** comparator for table beans.
 * @exclude
 * tests for equality on name of the table only
 * @author Noel Winstanley
 */
public class TableBeanComparator implements Comparator {

	public int compare( final Object o1, final Object o2 ) {
		
		final TableBean table1 = (TableBean)o1 ;
		final TableBean table2 = (TableBean)o2 ;
		
		return table1.name.compareTo( table2.name) ;

	}

}
