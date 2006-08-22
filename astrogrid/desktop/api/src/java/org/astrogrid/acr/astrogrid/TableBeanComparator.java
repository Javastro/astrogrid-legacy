package org.astrogrid.acr.astrogrid;

import java.util.Comparator;

public class TableBeanComparator implements Comparator {

	public int compare( Object o1, Object o2 ) {
		
		TableBean table1 = (TableBean)o1 ;
		TableBean table2 = (TableBean)o2 ;
		
		return table1.name.compareTo( table2.name) ;

	}

}
