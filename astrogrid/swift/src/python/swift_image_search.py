#!/usr/bin/env python
import string, sys, os, time, random
from astrogrid import acr
from astrogrid import SiapSearch
from astrogrid import MySpace

#
# Global arguments passed in...
#
ra = 0
dec = 0
radius = 0
ifile = ''
odir = ''

#
# Validate arguments passed.
#
# arg1: ra 
# arg2: dec
# arg3: radius
# arg4: path to local file containing list of service ivorns
# arg5: name of VOSpace directory for holding image results
#
def validateArgs():
	print 'entry: validateArgs()'
	global ra, dec, radius, ifile, odir
	# If no arguments were given, print a helpful message
	if len(sys.argv)!=6:
		print 'Usage: ra dec radius file_of_ivorns vospace_results_directory'
		sys.exit(0)
	else:
		ra = float( sys.argv[1] )
		dec = float( sys.argv[2] )
		radius = float( sys.argv[3] )
		ifile = sys.argv[4]
		odir = sys.argv[5]
	print 'exit: validateArgs()'
	return

#
# Process the input file of service ivorns.
#
# For each image source we wish to search, there is a list of alternative ivorns.
# For example, if we wished to search three image sources and there were
# two alternative services providing images for each source, that would
# make 6 ivorns altogether.
#
# Returns a two-dimensional list of ivorns.
#
#
def processIvornFile( filePath ):
	sources = []
	ivorns = []
	for line in open( filePath ).readlines():
		l = line.strip()
    # Comments lines are ignored...
		if l.strip().startswith( '#' ) == 0:
			if l.isdigit():
				if len( ivorns ) > 0:
					sources.append( ivorns )
					ivorns = []
			else:
				# We make the ivorn itself a first item in a list.
				# Subsequently we will add the service and its thread of execution.
				ivorns.append( [l] )
	if len( ivorns ) > 0:
		sources.append( ivorns )
	return sources

##################################################
# Mainline...                                    #
##################################################
print 'Started at: ' + time.strftime('%T') 

# Validate the input arguments passed to us...
validateArgs()
print ra, dec, radius, ifile, odir

# Retrieve ivorns for the sources we wish to search...
sources = processIvornFile( ifile )

# Log in to Astrogrid runtime...
acr.login() 				
print 'Logged in at ' + time.strftime('%T') 

# Create the output directory locally and in VOSpace...
m = MySpace()
print( 'Output folder is called ' + odir )
os.mkdir( odir )
print( 'making myspace dir at ' + time.strftime('%T') )
m.mkdir( '#' + odir )
print( 'myspace dir made by ' + time.strftime('%T') )

#siap = SiapSearch( 'ivo://archive.eso.org/dss' )
#print siap.info['content']['description']

# Trigger off the image searches on separate threads...
# We have a list sources...
sIndex = 0
for s in sources:
	# We have a list of ivorns for each source...
	iIndex = 0
	for i in s:
#		print 'ivorn item: ####################################################################### '
		print i[0] 
		# Generate a service for each ivorn...
		siap = SiapSearch( i[0] ) 
		i.append( siap )
		imageDir = '#' + odir + '/' + str(sIndex) + '_' + str(iIndex)
		m.mkdir( imageDir )
		res, thread = siap.execute( ra, dec, radius, saveDatasets = imageDir )
		i.append( thread )
#		print siap.info['content']['description']
#		print time.strftime('%T')
		iIndex = iIndex+1
	sIndex = sIndex+1

count = 0
while count < 120 :
	sourcesFinished = 0
	for s in sources:
		for i in s:
      # If one thread in a source is finished, that's good enough...
			if i[2].isAlive() == False:
				sourcesFinished = sourcesFinished+1 
				break
	if sourcesFinished == len( sources ):
		break 
	print( 'Sleeping for 1 second' )
	time.sleep(1)
	count = count+1

print( 'downloading results at ' + time.strftime('%T') )

sIndex = 0
for s in sources:
	iIndex = 0
	for i in s:
		if i[2].isAlive() == False:
			d = odir + '/' + str(sIndex) + '_' + str(iIndex)
			list = m.ls( directory= '#' + d ) ;
			os.mkdir( d )
			for x in list:
				print( 'Downloading ' + x[2] )
				m.readfile( '#' + d + '/' + x[2], ofile= d  + '/' + x[2] )
				print( 'Down finished at ' + time.strftime('%T') )
			break
		iIndex = iIndex+1
	sIndex = sIndex+1

print( 'Finished at '  + time.strftime('%T') ) 
