#!/usr/bin/env python
import string, sys, os, time
from astrogrid import acr
from astrogrid import SiapSearch
from astrogrid import utils
from astrogrid.threadpool import easy_pool
from urllib import *
from urlparse import *

#
#		Swift SIAP search.
#		This one is downloading the images itself.
#   
#   NOTE. I am not sure the votables should be parsed on slave threads.
#         I don't know whether the parsing mechanism used will be thread safe.
#         So perhaps there should be a bit more rejigging here.
#

#
# Global arguments passed in...
#
ra = 0
dec = 0
radius = 0
ifile = ''
odir = ''
maximages = 5

#
# Index into sources/services list...
SI_IMAGE_OUTPUT_DIRECTORY = 0
SI_IVORN = 1
SI_SIAP_SERVICE_OBJECT = 2

#
# Format of image files requested.
# Can be a coma-delimited string:
# FORMAT = 'image/fits,image/png,image/jpeg,imag/gif'
#
# Can be set to 'ALL', but you must be prepared to filter.
# Some services provide lots of bumf.
#
FORMAT = 'image/fits'

#
# Validate arguments passed. 
# Nothing fancy here, so I'm not using anything heavyweight.
# 
# arg1: ra 
# arg2: dec
# arg3: radius
# arg4: path to file containing list of service ivorns.
# arg5: unique name for the overall search directory to hold image results for this search (will be created!).
# arg6: the maximum number of images to be downloaded from any one service
#
def validateArgs():
	global ra, dec, radius, ifile, odir, maximages
	# If no arguments were given, print a helpful message
	if len(sys.argv)!=7:
		print 'Usage: ra dec radius file_of_ivorns results_directory max_images_from_each_service'
		sys.exit(0)
	else:
		ra = float( sys.argv[1] )
		dec = float( sys.argv[2] )
		radius = float( sys.argv[3] )
		ifile = sys.argv[4]
		odir = sys.argv[5]
		maximages = int(sys.argv[6])
	return

#
# Process the input file of service ivorns.
#
# For each image source we wish to search, there is a list of alternative ivorns.
# For example, if we wished to search three image sources and there were
# two alternative services providing images for each source, that would
# make 6 ivorns altogether.
#
# Returns a two-dimensional list of service information
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
				# For each service we make the name of the output directory
				# and the ivorn itself a first item in a list.
				# Subsequently we will add the service object itself.
				lbrace = l.rfind( '[' )
				rbrace = l.rfind( ']' )
				if lbrace == -1 or rbrace == -1:
					print 'Malformed line in ivorn file'
				else:
					l = l[lbrace+1:rbrace]
#					print l
					bits = l.split( '|' )
					# save the name of the given output directory for this service and its ivorn together...
					ivorns.append( [ bits[0].strip(), bits[1].strip() ] ) 
#					print ivorns
	if len( ivorns ) > 0:
		sources.append( ivorns )
	return sources

#
# Search function to pass to thread pool.
#
def searchAndRetrieve( image_directory, siap, ra, dec, radius, maximages ) :
	global FORMAT
	res = siap.execute( ra, dec, radius, format=FORMAT )
	if res: 
		try:
			vot = utils.read_votable( res, ofmt='votable' )
			retrieveImages( image_directory, vot, maximages )
		except:
			print image_directory + ': bad vot!'
	else:
		print image_directory + ': no result!'
	return

#
# Download function (retrieves images).
# 
def retrieveImages( image_directory, vot, maximages ) :
	# Get column index for access url
	urlColIdx = vot.getColumnIdx ('VOX:Image_AccessReference')
	formatIdx = vot.getColumnIdx ('VOX:Image_Format')
	if urlColIdx < 0:
		print "No access reference found"
		return
	
	dataRows = vot.getDataRows ()
	getData = vot.getData

	countImages = len( dataRows )
	print "Number of records: ", countImages
	if countImages > 0:
		print( 'Making image directory: ' + image_directory )
		os.mkdir( image_directory )
	cnt = 0
	for row in dataRows:
		cells = getData (row)
		url = cells[urlColIdx]
		fmt = cells[formatIdx]
		ext = '.'
		if 'jpeg' in fmt:
			ext += 'jpg'
		elif 'fits' in fmt:
			ext += 'fits'
		elif 'gif' in fmt:
			ext += 'gif'
		else:
			ext = ''
		iname = "image%d%s" % (cnt, ext)
		print "Saving image %d as %s" % (cnt, iname) 
		retrieveOneImage (image_directory, url, iname)
		cnt += 1
		if cnt >= maximages:
			break
	return
# end of retrieveImages( vot )

#
# Retrieve one image 
#
def retrieveOneImage( image_directory, url, fname ) :
	infile = urlopen( url )
	try:
		outfile = file( image_directory + '/' + fname, "wb" )
		outfile.write( infile.read () )
	except Exception, e:
		print "Failed to retrieve image from ", url, e
	infile.close()
	outfile.close()
	return
# end of retrieveOneImage( url, fname )

#####################################################
#    Mainline                                       #
#####################################################
print 'Started at: ' + time.strftime('%T') 
#
# Validate the input arguments passed to us...
validateArgs()
print ra, dec, radius, ifile, odir, maximages
#
# Retrieve ivorns for the sources we wish to search...
sources = processIvornFile( ifile )
#
# Log in to Astrogrid runtime...
print 'Logging in to Astrogrid runtime at ' + time.strftime('%T')
acr.login() 				
print 'Logged in at ' + time.strftime('%T') 
#
# Create the overall search output directory...
print( 'Output folder is called ' + odir )
os.mkdir( odir )
#
# Define the command to execute and the pool size
pool = easy_pool( searchAndRetrieve )
pool.start_threads( len(sources) )
#
# We have a list of sources and a list of ivorns for each source.
# Create a service for each ivorn and store it in the list...
ivornCount = 0
for s in sources:
	# We have a list of ivorns for each source...
	iIndex = 0
	for i in s:
		print i[ SI_IVORN ] 
		# Generate a service for each ivorn...
		siap = SiapSearch( i[ SI_IVORN ] ) 
#   print '=============================================='
#		print siap.info['content']['description']
		i.append( siap )
		# Form the image directory path/name...
		image_directory = odir + '/' + i[ SI_IMAGE_OUTPUT_DIRECTORY ]
		# Add the service and its inputs to the thread pool...
		input = ( image_directory, siap, ra, dec, radius, maximages )
		pool.put( input )
		iIndex = iIndex+1
		ivornCount = ivornCount+1

#print sources

#
# Now observe the real work:
# Print information while executing...
i=0
while 1 :
	p = pool.qinfo()
	print "Time: %3d sec    Queued: %2d    Running: %2d    Finished: %2d" % \
                                        (i, p[1], ivornCount-p[1]-p[3], p[3])
	time.sleep(1)
	i=i+1
	if p[3]==ivornCount: break
    
pool.stop_threads()
print( 'Finished at '  + time.strftime('%T') ) 
