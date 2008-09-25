"""
MySpace

Interface to the services offered my MySpace. Uses ACR.

Author: Francesco Pierfederici <fpierfed@eso.org>
Licensed under the Academic Free License version 2.0 (see LICENSE.txt).
"""
# Define which symbols we export.
__all__ = ['NodeInformation',
           'NodeAttributes',
           'Session',
           'exists', 
           'getwd', 
           'isfile', 
           'isdir', 
           'listdir', 
           'mkdir', 
           'remove', 
           'rename', 
           'rmdir', 
           'stat', 
           'mkfile', 
           'download',
           ]

# Imports
import ACR
import VOObject
from Exceptions import *



# Constants
# Optimization: create a proxy for the locally running ACR once and for all
# instead of creating one on demand all the time.
# TODO: Handle stale connections.
CURRENT_ACR = ACR.ACR()





# Utility classes
class NodeInformation(VOObject.VOObject):
    """
    NodeInformation

    Describe the properties of a node in a MySpace.
    """
    pass

class NodeAttributes(VOObject.VOObject):
    """
    NodeAttributes

    Encode low level MySpace node attibutes.
    """
    pass



# Session class
# FIXME: implement session management for MySpace.*()
class Session(VOObject.VOObject):
    """
    Session
    
    Keep track of user specific variables such as the working directory.
    """
    def __init__(self, userName, passwd, community):
        """
        Constructor
        """
        # User Info
        self.userName = userName
        self.passwd = passwd
        self.community = community
        
        # ACR Info
        self._acr = ACR.ACR()
        self.proxy = self._acr.connect()
        
        # File/directory Info
        self.cwd = None
        
        # Session Info
        # FIXME: assign unique session ID
        self.sessionID = None
        return



# MySpace functions
def exists(path):
    """
    Return true is path exists, False otherwise.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        return(proxy.astrogrid.myspace.exists(path))
    except:
        raise(MethodInvocationError())
    return
    
def getwd():
    """
    Return the NodeInformation object corresponding to the current working 
    directory.
    """
    raise(NotImplementedError())

def isfile(path):
    """
    Return true is path is a file, False otherwise.
    """
    raise(stat(path).file)

def isdir(path):
    """
    Return true is path is a directory, False otherwise.
    """
    raise(stat(path).folder)

def listdir(path):
    """
    Return a list of NodeInformation objects for the entries in the directory.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        return(proxy.astrogrid.myspace.list(path))
    except:
        raise(MethodInvocationError())
    return

def mkdir(path):
    """
    Create a directory named path.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        return(proxy.astrogrid.myspace.createFolder(path))
    except:
        raise(MethodInvocationError())
    return

def remove(path):
    """
    Remove the file/directory path. If path is a directory and is not empty,
    an exception is raised. Users need to empty the directory first.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        return(proxy.astrogrid.myspace.remove(path))
    except:
        raise(MethodInvocationError())
    return

def rename(src, dst):
    """
    Rename the file or directory src to dst. If dst exists it will not be 
    overwritten and an exception is raised.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        return(proxy.astrogrid.myspace.rename(src, dst))
    except:
        raise(MethodInvocationError())
    return

def rmdir(path):
    """
    Remove the directory path. Alias for remove().
    """
    return(remove(path))

def stat(path):
    """
    Return information (i.e. a NodeInformation object) for file or directory 
    path.
    """
   # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        rawInfo = proxy.astrogrid.myspace.getNodeInformation(path)
    except:
        raise(MethodInvocationError())
    
    # Create a new NodeInformation object with data from rawInfo. Remove the
    # 'attribute' key from rawInfo and move it to a NodeAttributes instance.
    info = NodeInformation()
    attr = NodeAttributes()
    attr.__dict__ = rawInfo['attributes']
    del(rawInfo['attributes'])
    info.__dict__ = rawInfo
    info.attributes = attr
    return(info)

# FIXME: Should this be a class?
def mkfile(path, url):
    """
    Create the file path by uploading the local file pointed to by url.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        proxy.astrogrid.myspace.createFile(path)
        proxy.astrogrid.myspace.copyURLToContent(url, path)
    except:
        raise(MethodInvocationError())
    return

def download(path, url):
    """
    Download the remote file path to the local file pointed to by url.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        return(proxy.astrogrid.myspace.copyContentToURL(path, url))
    except:
        raise(MethodInvocationError())
    return

def _forceMetadataRefresh(path):
    """
    Force the remote MyStore server to recompute the metadata information 
    corresponding to path.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        return(proxy.astrogrid.myspace.transferCompleted(path))
    except:
        raise(MethodInvocationError())
    return

def _login(username, passwd, community):
    """
    Log into the local MySpace as user username from community and identified
    by passwd.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        msg = proxy.astrogrid.community.login(username, passwd, community)
    except:
        raise(MethodInvocationError())
    return(True)

def _logout():
    """
    Log into the local MySpace as user username from community and identified
    by passwd.
    """
    # Get a connection to ACR.
    proxy = CURRENT_ACR
    
    # Invoke the method.
    try:
        msg = proxy.astrogrid.community.logout()
    except:
        raise(MethodInvocationError())
    return(True)














