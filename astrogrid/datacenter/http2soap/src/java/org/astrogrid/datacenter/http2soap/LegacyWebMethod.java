/*$Id: LegacyWebMethod.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

import java.io.IOException;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.astrogrid.datacenter.http2soap.builder.StringBuilder;


/** Container object that represents a single legacy web service method 
 *  - collects together the different objects that manage the  details of mapping to / from service
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class LegacyWebMethod {
    /** the request method to use */
    protected RequestMapper requester;
    /** a list of  response convertors to use */
    protected List convertors = new ArrayList();

    /** the result builder to use - defaults to stringBuilder */
    protected ResultBuilder builder = new StringBuilder(); // sensible defaults.
    /** the name of the web method */
    protected String name;
    
    /** datastructure to store legacy services in
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
     *
     */
    public static class Store {
        private Map m = new HashMap();
        /** add a new web method to the store */
        public void addService(LegacyWebMethod lsa) {
            m.put(lsa.getName(),lsa);
        }
        /** retrive a legacy web method, based on its name */
        public LegacyWebMethod lookupService(String methodName) {
            return (LegacyWebMethod) m.get(methodName);
        }
    }
    /** do the call to the legacy method,
     * <p>
     *  do the request, convert response, use to build return result */
    public Object doCall(Object[] args) throws LegacyServiceException, IOException {
        ReadableByteChannel responseChan = this.requester.doRequest(args);
        ResponseConvertorThread[] threads = new ResponseConvertorThread[convertors.size()];
        for (int i = 0; i < convertors.size() ; i++) {
            Pipe p = Pipe.open();
            threads[i] = new ResponseConvertorThread(responseChan,p.sink(), (ResponseConvertor)convertors.get(i));          
            threads[i].start();
            responseChan = p.source();
        }                  
        Object result = builder.build(responseChan);
        // now loop checking for errors.
        for (int i = 0; i < threads.length; i++) {
          if (threads[i].hasError()) { 
              throw new ResponseConvertorException("Convertor failed with",threads[i].getError());
          }
        }
       return result;       
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param convertor
     */
    public void addConvertor(ResponseConvertor convertor) {
        this.convertors.add(convertor);
    }

    /**
     * @param mapper
     */
    public void setRequester(RequestMapper mapper) {
        requester = mapper;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param builder
     */
    public void setBuilder(ResultBuilder builder) {
        this.builder = builder;
    }

}


/* 
$Log: LegacyWebMethod.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/