#!/usr/bin/env python
#simple calls to exercise the tool document editor.

import unittest

def selectAndQuery() :
    t = ar.dialogs.toolEditor.selectAndBuild()
    print t

def edit():
    #create t
    tool = ar.astrogrid.applications.createTemplateDocument('ivo://org.astrogrid/BPZ','simple')
    print tool
    t = ar.dialogs.toolEditor.edit(tool)
    print t
    
def editStored():
    #create t on disk - and find it's uri.
    tool = ar.astrogrid.applications.createTemplateDocument('ivo://org.astrogrid/BPZ','simple')
    fo = file("tmp.xml",'w')
    fo.write(tool)
    fo.close()
    #how do I find the location?
    loc = None
    t = ar.dialogs.toolEditor.editStored(loc)
    print t
  
if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #edit()
    selectAndQuery()
