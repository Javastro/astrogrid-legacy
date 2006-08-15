/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.util.HashMap;
import java.util.Map;

import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;

/**
 * @author Noel Winstanley
 * @since Aug 7, 200612:40:02 PM
 */
public class SiapProtocolKnowledge {
	 public static ParameterBean[] parameters;
	    public static InterfaceBean[] ifaces;
	    static { // build static info for a siap applicaiton - doesn't mention optional parameters.
	        // missing paramers are ucd, default value, units, type, subtype,options
	        ParameterBean pos = new ParameterBean("POS","Position",
	                "The position of the region of interest, expressed as the right-ascension and declination of the field center, in decimal degrees" +
	                " using the ICRS coordinate system. A comma should delimit the two values; embedded whitespace is not permitted." +
	                " Example: '12.821,-33.4",null,null,"decimal degrees","float,float",null,null);
	        ParameterBean sz = new ParameterBean("SIZE","Size","The angular size of the region",null,null,"decimal degrees","float",null,null);
	        ParameterBean format = new ParameterBean("FORMAT","Format","desired format(fs) of images",null,null,"comma-delimited list of: mime type|ALL|GRAPHIC|METADATA","String",null,null);
	        ParameterBean intersect = new ParameterBean("INTERSECT","Intersect","indicates how matched images should intersec the  region of interest",
	                null,"OVERLAPS",null,"String",null,new String[]{"COVERS","ENCLOSED","CENTER","OVERLAPS"});
	        
	        ParameterBean naxis = new ParameterBean("NAXIS","Naxis","Size of the output image in pixels",null,null,null,"int,int",null,null);
	        ParameterBean cframe = new ParameterBean("CFRAME","CFrame","Coordinate system reference frame",null,"ICRS",null,"String",null,new String[]{"ICRS","FK5","FK4","ECL","GAL","SGAL"});
	        ParameterBean equinox = new ParameterBean("EQUINOX","Equinox","Epoch of the mean equator and equinox for the specified CFrame",null,null,null,"String",null,null);
	        ParameterBean crpix = new ParameterBean("CRPIX","CRPix","Coordinates of the reference pixel, expressed in the pixel coordinates of the output image",null,null,null,"int,int",null,null);
	        ParameterBean crval = new ParameterBean("CRVAL","CRVal","World coordinates relative to CFrame at the reference point",null,null,null,"coordinates",null,null);
	        ParameterBean cdelt = new ParameterBean("CDELT","CDelt","The scale of the output image",null,null,"decimal degrees per pixel","int,int",null,null);
	        ParameterBean rotang = new ParameterBean("ROTANG","Rotation angle", "Rotation angle of the image in degrees relative to CFrame",null,"0",null,"angle",null,null);
	        ParameterBean proj = new ParameterBean("PROJ","Projection","Celestial projection of the output image",null,"TAN",null,"String",null,null);
	        ParameterBean verb = new ParameterBean("VERB","Verbose","Desired level of information to be returned",null,",1",null,"int",null,new String[]{"0","1","2","3"});
	        
	        ParameterBean result = new ParameterBean("result","Result","Votable of results",null,null,null,null,null,null);
	        
	        parameters = new ParameterBean[]{
	        		pos
	        		,sz
	        		,result
	        		,format
	        		,intersect
	        		,naxis
	        		,cframe
	        		,equinox
	        		,crpix
	        		,crval
	        		,cdelt
	        		,rotang
	        		,proj
	        		,verb
	        };

	        
	        ifaces = new InterfaceBean[] {
	                new InterfaceBean(
	                        "Minimal"
	                        , new ParameterReferenceBean[] {
	                                new ParameterReferenceBean(pos.getName(),1,1)
	                                , new ParameterReferenceBean(sz.getName(),1,1)
	                        }
	                        , new ParameterReferenceBean[] {
	                                new ParameterReferenceBean(result.getName(),1,1)
	                        }
	                )                
	               ,new InterfaceBean(
	                       "Full"
	                       , new ParameterReferenceBean[] {
	                               new ParameterReferenceBean(pos.getName(),1,1)
	                               , new ParameterReferenceBean(sz.getName(),1,1)
	                               , new ParameterReferenceBean(intersect.getName(),1,0)
	                               ,new ParameterReferenceBean(format.getName(),1,0)
	                             ,new ParameterReferenceBean(naxis.getName(),1,0)
	                             ,new ParameterReferenceBean(cframe.getName(),1,0)
	                             ,new ParameterReferenceBean(equinox.getName(),1,0)
	                             ,new ParameterReferenceBean(crpix.getName(),1,0)
	                             ,new ParameterReferenceBean(crval.getName(),1,0)
	                             ,new ParameterReferenceBean(cdelt.getName(),1,0)
	                             ,new ParameterReferenceBean(rotang.getName(),1,0)
	                             ,new ParameterReferenceBean(proj.getName(),1,0) 
	                             ,new ParameterReferenceBean(verb.getName(),1,0)
	                       }
	                       , new ParameterReferenceBean[] {
	                               new ParameterReferenceBean(result.getName(),1,1)
	                       }
	               )
	        };
	    }        
}
