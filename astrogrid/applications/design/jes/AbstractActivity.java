/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AbstractActivity.java,v 1.1 2004/08/10 21:09:29 pah Exp $
 */

package jes;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * The abstract base class of all activities that can be performed
 * in a workflow
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/10 21:09:29 $
 */
public class AbstractActivity extends org.astrogrid.common.bean.BaseBean 
implements Serializable
{
      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

//-- org.astrogrid.workflow.beans.v1.AbstractActivity()


      //-----------/
     //- Methods -/
    //-----------/

//-- boolean equals(java.lang.Object) 


    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'id'.
     */
    public String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

 //-- boolean isValid() 


//-- void marshal(java.io.Writer) 

//-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(String id)
    {
        this._id = id;
    }

 //-- void setId(java.lang.String) 


//-- org.astrogrid.workflow.beans.v1.AbstractActivity unmarshalAbstractActivity(java.io.Reader) 

//-- void validate() 

}
