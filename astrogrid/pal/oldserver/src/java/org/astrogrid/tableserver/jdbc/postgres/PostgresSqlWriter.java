/*$Id: PostgresSqlWriter.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.tableserver.jdbc.postgres;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.queryable.SearchField;
import org.astrogrid.geom.Angle;
import org.astrogrid.query.condition.MathExpression;
import org.astrogrid.query.condition.NumericComparison;
import org.astrogrid.tableserver.jdbc.StdSqlWriter;

/**
 * Produced Postgres-specific SQL.  This means:
 * <ul>
 * <li> All &lt;&gt; are replaced by &&
 * <li> No aggregate functions in WHERE
 * <li> Trig functions take radians as arguments
 * <li> Older postgres databases cannot compare ints with doubles, etc, so these have to be cast
 * </ul>
 * *
 */
public class PostgresSqlWriter extends StdSqlWriter {

	public void visitMath(MathExpression math)
	throws IOException
		{
      //cast everything to double
/*
      current.append("DOUBLE(");
      math.getLHS().acceptVisitor(this);
      current.append( ") "+math.getOperator()+" DOUBLE(");
      math.getRHS().acceptVisitor(this);
      current.append(") ");
 */
//
// DOUBLE(x) should be CAST(2 AS double precision)
//
		current.append("CAST(");
			math.getLHS().acceptVisitor(this);
		current.append( " AS double precision) ");
		current.append(math.getOperator());
		current.append(" CAST(");
			math.getRHS().acceptVisitor(this);
		current.append(" AS double precision) ");
		}

	public void visitNumericComparison(NumericComparison expression)
	throws IOException
		{
/*
      //cast everything to double
      current.append("DOUBLE(");
      expression.getLHS().acceptVisitor(this);
      current.append(") "+ expression.getOperator().toString()+" DOUBLE(");
      expression.getRHS().acceptVisitor(this);
      current.append(") ");
*/
		current.append("CAST(");
			expression.getLHS().acceptVisitor(this);
		current.append(" AS double precision) ");
		current.append(expression.getOperator().toString());
		current.append(" CAST(");
			expression.getRHS().acceptVisitor(this);
		current.append(" AS double precision) ");

		}

   /** Returns the SQL condition expression for a circle.  This circle is
    * 'flat' on the sphere, when vieweing the sphere from the center.  ie, the
    * radius is a declination angle from the given RA & DEC point.  This means
    * the circle is distorted in coordinate space
    */
	public String makeSqlCircleCondition(SearchField raCol, SearchField decCol, Angle ra, Angle dec, Angle radius)
		{
		//Some db functions take radians, some degrees.  Fail if the config is not
		//specified, so we force people to get it right...
      
		String raColRad  = makeColumnRadiansId(raCol);
		String decColRad = makeColumnRadiansId(decCol);
      
		//'haversine' distance formulae.  The correct one to use...
/*
		return
			makeSqlBoundsCondition(raCol, decCol, ra, dec, radius) + " AND "+
			"("+
			"(2 * ASIN( SQRT( "+
			// note that the (float) typecasts are for postgres sql that can't cope
			// with subtracting double precisions from single.  No point in casting
			// the functions to double if the precision isn't there.
			// Surround dec with extra brackets in order to cope with negative decs
			"POWER( SIN( ("+decColRad+" - ("+ (float) dec.asRadians()+") ) / 2 ) ,2) + "+
			"COS("+dec.asRadians()+") * COS("+decColRad+") * "+
			"POWER( SIN( ("+raColRad+" - "+(float) ra.asRadians()+") / 2 ), 2) "+
			"))) < "+(float) radius.asRadians()+
			")";
 */
		return
			makeSqlBoundsCondition(raCol, decCol, ra, dec, radius) +
			" AND "+
			" ( "+
				" ( " +
					" 2 "+
					" * "+
					" asin( " +
						" sqrt( "+
							// note that the (float) typecasts are for postgres sql that can't cope
							// with subtracting double precisions from single.  No point in casting
							// the functions to double if the precision isn't there.
							// Surround dec with extra brackets in order to cope with negative decs
							" pow( "+
								" sin( "+
									" ( " +
										decColRad +
										" - "+
											" ( "+
											(float) dec.asRadians()+
											" ) "+
									" ) "+
									"/ "+
									" 2 "+
								" ) "+
								" , "+
								" 2 "+
							" ) "+
							" + "+
							"cos( "+
								dec.asRadians()+
							" ) "+
							" * "+
							"cos( "+
								decColRad+
							" ) "+
							" * "+
							"pow( "+
								"sin( "+
									" ( "+
										raColRad+
										" - "+
										(float) ra.asRadians()+
									" ) "+
									" / 2 "+
								" ) "+
								" , "+
								" 2 "+
							" ) "+
						" ) "+
					" ) "+
				" ) "+
			" < "+
			(float) radius.asRadians()+
			" ) ";
		}
	}


/*
$Log: PostgresSqlWriter.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.1  2006/04/20 15:23:08  kea
Checking old sources in in oldserver directory (rather than just
deleting them, might still be useful).

Revision 1.3  2005/06/08 10:53:46  clq2
*** empty log message ***

Revision 1.2.4.1  2005/06/07 21:47:41  dave
Fixed double and power for postgres DB server.

Revision 1.2  2005/05/27 16:21:06  clq2
mchv_1

Revision 1.1.2.2  2005/05/03 14:02:32  mch
some validating fixes

 
*/

