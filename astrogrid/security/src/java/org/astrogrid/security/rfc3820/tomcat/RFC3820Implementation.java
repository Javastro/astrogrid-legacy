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

package org.astrogrid.security.rfc3820.tomcat;

import java.net.Socket;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.net.SSLImplementation;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.ServerSocketFactory;

/**
 * An implementation of SSL for Tomcat that supports RFC3820.
 * This is copied from the JSSE-based SSL implementation supplied with Tomcat 
 * and does, indeed, use the JSSE for most functions. The socket factory is
 * specific to this implementation in order to inject the RFC3820 support.
 * This class is simply a facade that ensures the use of the correct
 * socket factory.
 *
 * @author Guy Rixon
*/     
public class RFC3820Implementation extends SSLImplementation
{
    static Log logger = LogFactory.getLog(RFC3820Implementation.class);
    
    public RFC3820Implementation() throws ClassNotFoundException {
      super();
    }

    public String getImplementationName(){
      return "Astrogrid-RFC3820";
    }
      
    public ServerSocketFactory getServerSocketFactory()  {
        ServerSocketFactory ssf = new RFC3820SocketFactory();
        return ssf;
    } 

    public SSLSupport getSSLSupport(Socket s) {
        SSLSupport ssls = new JSSESupport((SSLSocket)s);
        return ssls;
    }
}
