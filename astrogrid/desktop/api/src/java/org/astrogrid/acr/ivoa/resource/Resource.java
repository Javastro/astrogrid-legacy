/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;

import org.astrogrid.acr.ivoa.Registry;

/**  Base class for all resources returned from an IVOA Registry.
 * 
 *   
 *   An instance of this class can represent any entity or component of a VO application that is
           describable and identifiable by a IVOA Identifier.
 @note Most resouces will be represented by an object that implements <b>one or more</b> subclasses of this class. 
* @see <a href="http://www.ivoa.net/Documents/PR/ResMetadata/RM-20061212.html">IVOA Registry Metadata Document</a>
   @see Registry
 * @author Noel Winstanley
 */
public interface Resource extends Serializable {
	/**       A description of the quality of the
                  resource description.
                  It can tbe used to indicate the confidence an end-user
                  can put in the resource as part of a VO application
                  or research study.  */
	Validation[] getValidationLevel();
	
	/**  the Full name given to the resource 
	 * @note expected to always be provided*/
	String getTitle();
	/**      The unique ID for this resource.
	 * 
	 * @return Unambiguous and unique reference to the resource conforming to the IVOA
                 standard for identifiers */
	URI getId();
	/**     A short name or abbreviation given to the resource.
	 * @note this is not guaranteed to be unique, and often is not available */
	String getShortName();
	/**      Curation information about the resource */
	Curation getCuration();
	/**   Description of the content of the resource */
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

	/** the xsi:type attribute of this resource.
	 * @note This is one method of distinguishing between different kinds of resource. However, it's often easier to 
	 * just check what sub-interfaces of Resource a particular instance implements. */
	String getType();
}
