/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;

/**
 * @author Noel Winstanley
 * @since Aug 7, 200612:44:28 PM
 */
public class ConeProtocolKnowledge {
	  public static ParameterBean[] parameters;
	    public static InterfaceBean[] ifaces;
	    static { // build static info for a cone applicaiton.
	        // missing paramers are ucd, default value, units, type, subtype,options
	        ParameterBean ra = new ParameterBean("ra","Right Ascension","right ascension of the position",null,null,null,"float",null,null); // @todo improve the metadata
	        ParameterBean dec = new ParameterBean("dec","Declination","declination of the position",null,null,null,"float",null,null);
	        ParameterBean sz = new ParameterBean("sz","Size","size of search radius",null,"1.0",null,"float",null,null);
	        ParameterBean result = new ParameterBean("result","Result","Votable of results",null,null,null,"votable",null,null);
	        
	        parameters = new ParameterBean[] {
	        		ra
	        		,dec
	        		,sz
	        		,result
	        };
	        
	        ifaces = new InterfaceBean[] {
	              new InterfaceBean(
	                       "Cone"
	                       , new ParameterReferenceBean[] {
	                               new ParameterReferenceBean(ra.getName(),1,1)
	                               , new ParameterReferenceBean(dec.getName(),1,1)
	                               , new ParameterReferenceBean(sz.getName(),1,1)
	                       }
	                       , new ParameterReferenceBean[] {
	                               new ParameterReferenceBean(result.getName(),1,1)
	                       }
	               )
	        };
	    }    

}
