/* Generated By: AdqlStoX.jjt,v 1.18 2007/07/25 12&JJTree: Do not edit this line. AST_PolygonCartesian.java */

package org.astrogrid.adql;

import org.apache.xmlbeans.XmlObject;
import org.astrogrid.stc.beans.*;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_PolygonCartesian extends SimpleNode {
    
  private static Log log = LogFactory.getLog( AST_PolygonCartesian.class ) ;

  public AST_PolygonCartesian(AdqlStoX p, int id) {
    super(p, id) ;
  }
  
  public void buildXmlTree( XmlObject xo ) {
      if( log.isTraceEnabled() ) enterTrace( log, "AST_PolygonCartesian.buildXmlTree()" ) ; 
      getTracker().setType( PolygonType.type ) ;
      //
      // We know this is a Cartesian Polygon type, else why are we here.
      // So set the appropriate astro coord system...
      AST_RegionPredicate.setAstroCoordSystem_Cartesian( this ) ;
      //
      // "Create" the appropriate polygon type using XmlBeans magic.
      PolygonType p = (PolygonType)xo.changeType( PolygonType.type ) ;
    
      this.generatedObject = p ;
      super.buildXmlTree(p) ;
      if( log.isTraceEnabled() ) exitTrace( log, "AST_PolygonCartesian.buildXmlTree()" ) ; 
  }

}