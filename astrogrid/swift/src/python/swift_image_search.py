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
#	print 'entry: validateArgs()'
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
#	print 'exit: validateArgs()'
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

#####################################################
#    Mainline                                       #
#####################################################
print 'Started at: ' + time.strftime('%T') 

# Validate the input arguments passed to us...
validateArgs()
print ra, dec, radius, ifile, odir

# Retrieve ivorns for the sources we wish to search...
sources = processIvornFile( ifile )

# Log in to Astrogrid runtime...
acr.login() 				
print 'Logged in at ' + time.strftime('%T') 

# Create the overall search output directory locally and in VOSpace...
m = MySpace()
print( 'Output folder is called ' + odir )
os.mkdir( odir )
print( 'Making myspace dir at ' + time.strftime('%T') )
m.mkdir( '#' + odir )
print( 'Myspace dir made by ' + time.strftime('%T') )

#siap = SiapSearch( 'ivo://archive.eso.org/dss' )
#print siap.info['content']['description']

# Trigger off the image searches on separate threads...
# We have a list sources...
sIndex = 0
for s in sources:
	# We have a list of ivorns for each source...
	iIndex = 0
	for i in s:
		print i[0] 
		# Generate a service for each ivorn...
		siap = SiapSearch( i[0] ) 
		i.append( siap )
		# Generate a directory in vospace for each service's results...
		imageDir = '#' + odir + '/' + str(sIndex) + '_' + str(iIndex)
		m.mkdir( imageDir )
		# Start the execution on a separate thread...
		formats = 'image/fits,image/png,image/jpeg'
		res, thread = siap.execute( ra, dec, radius, format='image/fits', saveDatasets = imageDir )
		i.append( thread ) # save the thread in the list for later referencing
		i.append( False )  # for each thread save its state as not finished. An optimization.
#		print siap.info['content']['description']
		iIndex = iIndex+1
	sIndex = sIndex+1

#
# We now wait on the threads of execution...
# (We allow a maximun of 60 seconds)
count = 0
sourcesFinished = 0
while count < 60 :
	for s in sources:
		for i in s:
      # Only if we have not flagged it as already finished...
			if i[3] == False:
      	# If one thread in a source is finished, that's good enough...
				if i[2].isAlive() == False:
					print 'Finished: ' + i[0] 
					sourcesFinished = sourcesFinished+1 
					i[3] = True # flag this one as finished. An optimization.
			breakf
	if sourcesFinished == len( sources ):
		print 'About to break on count of ' + str( count ) 
		break 
	print 'Sleeping for 1 second on count of ' + str( count ) 
	time.sleep(1)
	count = count+1

#
# At least one service for each source has returned.
# We start the download process...
#
print( 'Downloading results at ' + time.strftime('%T') )
sIndex = 0
for s in sources:
	iIndex = 0
	for i in s:
		if i[2].isAlive() == False:
			d = odir + '/' + str(sIndex) + '_' + str(iIndex)
			print 'Retrieving directory list for ' + d + 'starting at ' + time.strftime('%T')
			list = m.ls( directory= '#' + d ) ;
			print 'Directory list retrieved by ' + time.strftime('%T')
			# We only try if there are any results to download...
			if len( list ) > 0:
				os.mkdir( d )
				for x in list:
					print( 'Downloading ' + x[2] )
					m.readfile( '#' + d + '/' + x[2], ofile= d  + '/' + x[2] )
					print( 'Download finished at ' + time.strftime('%T') )
			break
		iIndex = iIndex+1
	sIndex = sIndex+1

print( 'Finished at '  + time.strftime('%T') ) 
