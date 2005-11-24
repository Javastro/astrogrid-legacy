# ACR connection module
""" A module to connect to the ACR
 Use it as:
   from astrogrid import acr
   Then acr.astrogrid.myspace ....etc
   Author: jdt@roe.ac.uk
"""
import os
import xmlrpclib
import webbrowser
import time
try:
    __propsFile = file(os.path.expanduser('~/.astrogrid-desktop'));
except IOError,e:
    print "Couldn't load the ACR config file - attempting to start the ACR"
    webbrowser.open_new("http://software.astrogrid.org/jnlp/astrogrid-desktop/astrogrid-desktop.jnlp");
    #wait for a while and see if we succeed.
    __numberTries = 20
    __wait = 10
    __tries = 0
    __notConnected=True
    while (__notConnected):
        try:
            __propsFile = file(os.path.expanduser('~/.astrogrid-desktop'));
            __notConnected=False
            #Pause to catch breath...not really very robust
            time.sleep(__wait)
        except IOError,e:
            __tries = __tries + 1
            if (__tries==__numberTries):
                print "Couldn't load the ACR config file - please start your ACR by hand"
                os._exit(1)
            print "Not connected to ACR yet...waiting for %i seconds"  %  __wait
            time.sleep(__wait)
    
    
__url = __propsFile.readline().rstrip()
acr = xmlrpclib.Server(__url+"xmlrpc");

