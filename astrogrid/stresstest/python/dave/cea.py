"""
A module to make synchronous access to Common Execution Architecture applications easier
"""
import sys
import time
import thread
import logging
from xmlrpclib import Fault

def isIndirect(param):
    "Decide whether a parameter is likely to be a URL or not"
    prefixes = ['http://','file:','ivo://','ftp://']
    for prefix in prefixes:
        if param.startswith(prefix):
            return True;
    return False;
    
class Application:
    """
    Represents a CEA-style application.
    
    Usage:
    app = Application("ivo://starlink.ac.uk/stilts","tcopy")
    print app.execute({'tcopy_in':"http://wiki.eurovotech.org/twiki/pub/VOTech/AnomalyDetection/example.vot", 'tcopy_ofmt' : 'ascii' },{})
    
    In the above, an empty dictionary is provided for the output parameters, indicating that all results should be returned by value from this call.  If a dictionary of
    outputs is provided, then they should be URIs to locations where the output can be sent.
    
    If you prefer to work asynchronously, then you need to provide a callback to receive the results:
    
    def mycallback(outputs):
        print "Callback called"
        print outputs
    
    app.executeasynch({'tcopy_in':"http://wiki.eurovotech.org/twiki/pub/VOTech/AnomalyDetection/example.vot", 'tcopy_ofmt' : 'csv' },{}, mycallback)
    
    """
    def __init__(self, ar, ivorn, interface):
        "Create an application with the supplied ar, ivorn and interface from a registry lookup"

        self.service  = ar.astrogrid.applications.getCeaApplication(ivorn)
        self.template = ar.astrogrid.applications.createTemplateStruct(ivorn, interface)
                
    def execute(self, ar, inputs, outputs):
        """
        Execute the application with the given inputs and outputs.  The ins and outs are dictionaries of parameters - the keys
        must match those expected by the application.  In the case of output parameters, you only need to supply URIs for those values
        you want to be returned indirectly (e.g. to MySpace).  Outputs that are not specified here will be returned by value as a dictionary
        from this call.  This method uses a simple heuristic to decide whether a given parameter is a reference or a value - if it begins
        with http://, ivo://, file: etc...then it's treated as a reference.  It's possible that this isn't what you want in
        some rare situations. 
        """
        ident = self.start(ar, inputs, outputs)
        return self.wait(ar, ident)
        
    def executeasynch(self, ar, inputs, outputs, callback):
        """
        Execute the application with the given inputs and outputs.  Executes asynchronously  The ins and outs are dictionaries of parameters - the keys
        must match those expected by the application.  In the case of output parameters, you only need to supply URIs for those values
        you want to be returned indirectly (e.g. to MySpace).  Outputs that are not specified here will be returned by value as a dictionary
        and sent to the supplied callback.  This method uses a simple heuristic to decide whether a given parameter is a reference or a value - if it begins
        with http://, ivo://, file: etc...then it's treated as a reference.  It's possible that this isn't what you want in
        some rare situations. 
        """
        ident = self.start(ar, inputs, outputs)
        
        def onnewthread(ar, ident,callback):
            #print "New thread"
            result = self.wait(ar, ident)
            callback(result)
            
        #onnewthread(ar, job_id, callback)
        thread.start_new_thread(
            onnewthread,
            (ar, ident, callback)
            )

    def param(self, value):
    	# Create a param value structure.
        return {'indirect' : isIndirect(value), 'value' : value}
        
    # Build a CEA task document, filling in the input and output params.
    def build(self, inputs, outputs):
        import copy
        docstruct = copy.deepcopy(self.template)

        for param in inputs:

            if not param in docstruct['input']:
                raise "Unknown input parameter ", param

            docstruct['input'][param] = self.param(inputs[param])
            
        for param in outputs:
            
            if not param in docstruct['output']:
                raise "Unknown output parameter ", param

            docstruct['output'][param] = self.param(outputs[param])
            
        return docstruct

    # Start a CEA job, returning the job ident.
    def start(self, ar, inputs, outputs):
        # Build the task document and send it.
        return ar.astrogrid.applications.submit(
            ar.astrogrid.applications.convertStructToDocument(
                self.build(inputs, outputs)
                )
            )
      
    # Wait for a CEA job.
    def wait(self, ar, ident):
        # Poll the job's status, looping until it has completed
        status = 'UNKNOWN'
        while(True):
            # Pause between each call.
            time.sleep(1)
            # Try checking the status
            for retry in range(6):
                # Call the AR to check the status.
                try:
                    info   = ar.astrogrid.applications.getExecutionInformation(ident);
                    status = info['status']
                # Call to AR threw a Fault.
# This is here because the ACR regularly throws NotFound exceptions.
                except Fault, (ouch) :
                    status = 'EXCEPT'
                    logging.error("RPC Fault polling job status %s", ouch.faultString)
                    # Pause longer each attempt.
                    pause = 2 ** (retry + 1)
                    logging.debug("Pausing for %d", pause)
                    time.sleep(pause)
                # Call to AR threw an exception.
                except Exception, (ouch) :
                    status = 'EXCEPT'
                    logging.error("Exception polling job status %s", str(ouch))
                    # Exit the for loop
                    break
                # No exception, so exit the for loop
                else:
                    break

            logging.debug("Status %s", status)

            if status=="EXCEPT":
                return status

            if status=="ERROR":
                return status
            
            if status=="COMPLETED":
                return status
            
    # Generic request for CEA job results.
    def result(self, ar, ident):
        return ar.astrogrid.applications.getResults(ident)

