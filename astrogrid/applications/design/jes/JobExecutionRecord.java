/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: JobExecutionRecord.java,v 1.1 2004/08/10 21:09:29 pah Exp $
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
import org.astrogrid.workflow.beans.v1.execution.ExecutionRecordType;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/**
 * A record of a single execution of a job
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/10 21:09:29 $
 */
public class JobExecutionRecord extends ExecutionRecordType 
implements Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _jobId
     */
    private JobURN _jobId;
//-- org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord()


      //-----------/
     //- Methods -/
    //-----------/

//-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'jobId'.
     * 
     * @return the value of field 'jobId'.
     */
    public JobURN getJobId()
    {
        return this._jobId;
    } //-- org.astrogrid.workflow.beans.v1.execution.JobURN getJobId() 

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
     * Sets the value of field 'jobId'.
     * 
     * @param jobId the value of field 'jobId'.
     */
    public void setJobId(JobURN jobId)
    {
        this._jobId = jobId;
    }

 //-- void setJobId(org.astrogrid.workflow.beans.v1.execution.JobURN) 


//-- org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord unmarshalJobExecutionRecord(java.io.Reader) 

//-- void validate() 

}
