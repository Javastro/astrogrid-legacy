/*$Id: AdqlVizierTranslator.java,v 1.1 2003/11/28 19:12:16 nw Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cds.querier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.DynamicVisitor;
import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.adql.generated.ArrayOfString;
import org.astrogrid.datacenter.adql.generated.Circle;
import org.astrogrid.datacenter.adql.generated.ColumnExpr;
import org.astrogrid.datacenter.adql.generated.ComparisonPred;
import org.astrogrid.datacenter.adql.generated.LikePred;
import org.astrogrid.datacenter.adql.generated.ScalarExpression;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.adql.generated.Table;
import org.astrogrid.datacenter.adql.generated.Where;
import org.astrogrid.datacenter.cdsdelegate.vizier.DecimalDegreesTarget;
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.w3c.dom.Element;

/** Translate an adql query to a VizierCone object - 
 *  a matter of stripping out the right bits.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class AdqlVizierTranslator  implements Translator {

    
    public Object translate(Element arg0) throws Exception {

        Select adql = ADQLUtils.unmarshalSelect(arg0);        
        VizierVisitor visitor = new VizierVisitor();
        adql.acceptTopDown(visitor);
        return visitor.getCone();
    }

    public static class VizierVisitor implements DynamicVisitor {
        static Log log = LogFactory.getLog(VizierVisitor.class);
        public VizierVisitor() {
            cone = new VizierCone();
        }
        VizierCone cone;
        public VizierCone getCone() {
            return cone;
        }
        
        public void visit(Table t) {
            if (t.getName().equalsIgnoreCase("metadata") 
                || t.getAliasName().equalsIgnoreCase("metadata")) {
                    cone.setMetaData(true);
                }
        }
        
        public void visit(Circle c) {
            log.debug("Found circle");
            DecimalDegreesTarget target = new DecimalDegreesTarget(c.getRa().getValue(),c.getDec().getValue());
            cone.setTarget(target);
            cone.setRadius(c.getRadius().getValue());
            
        }
        /** assume any 'like' operation is specifying an extra parameter */
        public void visit(LikePred like) {
            log.debug("Found like predi");
            ArrayOfString as = like.getValue().getStringLiteral().getValue();
            String result = cone.getAdditionalTerms() == null ? "" : cone.getAdditionalTerms();
            for (int i = 0; i < as.getStringCount(); i++) {
                result += " " + as.getString(i); 
            }
        }
        
        
        public void visit(ComparisonPred comp) {
            // too strict
            String operator = comp.getCompare().toString().trim();
            if (! operator.equals("=")) {
                log.warn("Ignoring predicate with operator " + operator);
            }
            // now need to find table and string expressions.
            ScalarExpression exp1 = comp.getFirstExpr();
            ScalarExpression exp2 = comp.getSecondExpr();
            if (exp1 instanceof ColumnExpr) {
                String value = getValue(exp2);
                 setColumn((ColumnExpr)exp1,value);
            } else {
 
                if (exp2 instanceof ColumnExpr) {
                    String value = getValue(exp1);
                    setColumn((ColumnExpr)exp2,value);
                } else {
                    log.warn("ignoring predicate with no reference to column");
                    return;
                }
            } 
        
        }
        /**
         * @param exp2
         * @return
         */
        private String getValue(ScalarExpression exp2) {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * @param expr
         * @param value
         */
        private void setColumn(ColumnExpr expr, String value) {
            // TODO Auto-generated method stub
            
        }

        public void visit(QOM arg0) throws Exception {
            //do nothing
            
        }
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.Translator#getResultType()
     */
    public Class getResultType() {
        return VizierCone.class;
    };

}


/* 
$Log: AdqlVizierTranslator.java,v $
Revision 1.1  2003/11/28 19:12:16  nw
getting there..
 
*/