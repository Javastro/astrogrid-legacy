/* Generated By: AdqlStoX.jjt,v 1.18 2007/07/25 12&JJTree: Do not edit this line. AST_BoxJ2000.java */

package org.astrogrid.adql;

import org.apache.xmlbeans.XmlObject;
import org.astrogrid.stc.beans.*;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_BoxJ2000 extends SimpleNode {
    
  private static Log log = LogFactory.getLog( AST_BoxJ2000.class ) ;

  public AST_BoxJ2000(AdqlStoX p, int id) {
    super(p, id) ;
  }
  
  public void buildXmlTree( XmlObject xo ) {
      if( log.isTraceEnabled() ) enterTrace( log, "AST_BoxJ2000.buildXmlTree()" ) ; 
      getTracker().setType( BoxType.type ) ;
      //
      // We know this is a J2000 Box type, else why are we here.
      // So set the appropriate astro coord system...
      AST_RegionPredicate.setAstroCoordSystem_LatLon( this ) ;
      //
      // "Create" the appropriate polygon type using XmlBeans magic.
      BoxType p = (BoxType)xo.changeType( BoxType.type ) ;
    
      this.generatedObject = p ;
      super.buildXmlTree(p) ;
      if( log.isTraceEnabled() ) exitTrace( log, "AST_BoxJ2000.buildXmlTree()" ) ; 
  }

}