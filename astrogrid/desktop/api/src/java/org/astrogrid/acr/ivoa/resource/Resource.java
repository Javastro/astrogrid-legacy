/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;

/**    Any entity or component of a VO application that is
           describable and identifiable by a IVOA Identifier. 
  * @see http://www.ivoa.net/Documents/PR/ResMetadata/RM-20061212.html         
 * @author Noel Winstanley
 * @since Jul 31, 20063:43:25 PM
 */
public interface Resource extends Serializable {
	/**       A numeric grade describing the quality of the
                  resource description, when applicable, 
                  to be used to indicate the confidence an end-user
                  can put in the resource as part of a VO application
                  or research study.  */
	Validation[] getValidationLevel();
	
	/**  the full name given to the resource */
	String getTitle();
	/**      Unambiguous reference to the resource conforming to the IVOA
                 standard for identifiers */
	URI getId();
	/**     a short name or abbreviation given to the resource. */
	String getShortName();
	/**      Information regarding the general curation of the resource */
	Curation getCuration();
	/**   Information regarding the general content of the resource */
	Content getContent();
	/** a tag indicating whether this resource is believed to be still
              actively maintained. */
	String getStatus();
	/**         The UTC date and time this resource metadata description
              was created. 
             */
	String getCreated();
	/**        The UTC date this resource metadata description was last updated. */
	String getUpdated();

	/** the xsi:type attribute of this resource */
	String getType();
}
