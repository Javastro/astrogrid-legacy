/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import org.astrogrid.adql.v1_0.beans.* ;
import org.apache.xmlbeans.*;
/**
 * Interactive
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 18, 2006
 */
public class Interactive {
	
	 public static void main(String args[]) {
		 AdqlStoX compiler = new AdqlStoX(System.in);
		 SelectDocument queryDoc = null ;

			System.out.println("Reading from standard input...");
			System.out.print("Enter an SQL-like expression :");			
			queryDoc = compiler.exec();  
			if( queryDoc != null )
				System.out.println( queryDoc.toString() ) ;

	 }

}
