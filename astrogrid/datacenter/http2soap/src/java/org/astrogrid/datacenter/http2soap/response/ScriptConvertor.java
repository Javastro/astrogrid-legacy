/*$Id: ScriptConvertor.java,v 1.2 2003/11/11 14:43:33 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.response;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.ibm.bsf.BSFException;
import com.ibm.bsf.BSFManager;

/** map a result by executing a script in a scripting language
 *  - requires BSF and appropriate scripting engine.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class ScriptConvertor implements ResponseConvertor {

 
    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.adapter.ResponseConvertor#convertResponse(java.io.InputStream)
     */
    public void convertResponse(ReadableByteChannel is,WritableByteChannel os) throws ResponseConvertorException, IOException {
        try {

        BSFManager bsf = new BSFManager();
        bsf.declareBean("inChan",is,ReadableByteChannel.class);
        bsf.declareBean("outChan",os,WritableByteChannel.class);
        bsf.loadScriptingEngine(this.language);       
        
        bsf.exec(this.language,"script-convertor",1,1,this.script);
        } catch (BSFException e) {
            throw new ResponseConvertorException("Script failed",e);            
        }
        os.close();
    } 
    protected String language;
    protected String script;

    /**
     * @param string
     */
    public void setLanguage(String string) {
        language = string;
    }

    /**
     * @param string
     */
    public void setScript(String string) {
        script = string;
    }
    
    public String getScript() {
        return script;
    }
    
    public String getLanguage() {
        return language;
    }

}


/* 
$Log: ScriptConvertor.java,v $
Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/