"""
ACR

Abstraction layer for the methods exposed by the XML-RPC interface of the
Astrogrid Client Runtime (ACR).

Author: Francesco Pierfederici <fpierfed@eso.org>
Licensed under the Academic Free License version 2.0 (see LICENSE.txt).
"""
import os
import xmlrpclib

import VOObject
from Exceptions import *




# ACR access classes/routines
class ACR(VOObject.VOObject):
    """
    Local proxy for a locally running ACR instance.
    """
    def __init__(self, introspect=False):
        """
        Contructor
        
        Acquire an ACR connnection.
        """
        # ACR writes its URL to teh file $HOME/.astrogrid-desktop
        acrAddrFile = os.path.expanduser('~/.astrogrid-desktop')
        
        # Build the ACR XML-RPC URL from the content of self._acrAddrFile
        self._url = '%sxmlrpc' % (file(acrAddrFile).readline().strip())
        
        # TODO: implement a connection pool.
        self._proxy = self._acrConnect()
        
        # FIXME: Handle the setting of config key/value pairs.
        self.config = self._acrGetConfig()
        
        # Try and fetch the available methods.
        if(introspect):
            # OK, this is a bit lame. We should really try and preserve the 
            # method hierarchy!
            methods = self._proxy.system.listMethods()
            self.__dict__.update(dict([(name, None) for name in methods]))
        return
    
    def _acrConnect(self):
        """
        Lazily connect to a running ACR. 
        
        Return 
            a local proxy to the ACR XML-RPC server. 
        
        Raise
            EnvironmentError if no address filecan be found/read.
            IOError          if an unsupported protocol is supplied.
        """
        # Connect to the ACR
        return(xmlrpclib.ServerProxy(self._url))
    
    def _acrGetConfig(self):
        """
        Fetch the current configuration off of a running <acr> and store it in
        a dictionary. 
        
        Return 
            the configuration dictionary on success.
        
        Raise
            MethodInvocationError if the method invocation onto the running 
                                  ACR fails.
            ConnectionError       if the ACR proxy handle is stale.
        """
        # Fetch the current configuration (just for backup in case something goes
        # wrong).
        try:
            savedConfig = self._proxy.system.configuration.list()
        except:
            raise(MethodInvocationError())
        return(savedConfig)
    
    def __getattr__(self, name):
        """
        A bit of magic to give the illusion that the local object is an opaque
        proxy to the remote app.
        """
        if(name == '__del__'):
            raise(AttributeError, name)
        return(getattr(self._proxy, name))

        











