/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Sequence.java,v 1.1 2004/08/10 21:09:29 pah Exp $
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
import org.astrogrid.workflow.beans.v1.ActivityContainer;

/**
 * a collection of activities to be performed sequentially
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/10 21:09:29 $
 */
public class Sequence extends jes.ActivityContainer 
implements Serializable
{


//-- org.astrogrid.workflow.beans.v1.Sequence()


      //-----------/
     //- Methods -/
    //-----------/

//-- boolean equals(java.lang.Object) 


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


//-- org.astrogrid.workflow.beans.v1.Sequence unmarshalSequence(java.io.Reader) 


 //-- void validate() 


    /**
     * @link aggregation
     * @supplierCardinality 0..* 
     */
    private Flow lnkFlow;

    /**
     * @link aggregation
     * @supplierCardinality 0..* 
     */
    private Step lnkStep;
}
