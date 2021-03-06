/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.astrogrid.security.rfc3820.tomcat6;

import java.net.Socket;

import org.apache.tomcat.util.net.SSLImplementation;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.ServerSocketFactory;
import javax.net.ssl.SSLSession;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * JSSEImplementation:

   Concrete implementation class for JSSE
 *
 * This class is copied from the Tomcat-6.0.26 code-base.

   @author EKR
*/
        
public class JSSEImplementation extends SSLImplementation
{
    static final String SSLSocketClass = "javax.net.ssl.SSLSocket";

    static Log logger =  LogFactory.getLog(JSSEImplementation.class);

    private JSSEFactory factory = null;

    public JSSEImplementation() throws ClassNotFoundException {
        // Check to see if JSSE is floating around somewhere
        Class.forName(SSLSocketClass);
        factory = new JSSEFactory();
    }


    public String getImplementationName(){
      return "AstroGrid-JSSE-RFC3820";
    }
      
    public ServerSocketFactory getServerSocketFactory()  {
        ServerSocketFactory ssf = factory.getSocketFactory();
        return ssf;
    } 

    public SSLSupport getSSLSupport(Socket s) {
        SSLSupport ssls = factory.getSSLSupport(s);
        return ssls;
    }

    public SSLSupport getSSLSupport(SSLSession session) {
        SSLSupport ssls = factory.getSSLSupport(session);
        return ssls;
    }

}
