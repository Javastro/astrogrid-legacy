#!/usr/bin/env python
# This submits a bunch of conesearches one after the other (in series)

import sys
import os

#-----------------------------------------------------------------------------
# CONFIG SETTINGS
#-----------------------------------------------------------------------------
# The ivorn of the cea cone service
# ROE
cmd = '/usr/bin/curl "http://srif112.roe.ac.uk/mysql-first/SubmitCone?RA=0&DEC=5&SR=5&Format=VOTable&Compression=NONE"'

numqueries = 1000

for i in range(numqueries) :
	print( "TEST %d" % (i))
	os.system(cmd)
