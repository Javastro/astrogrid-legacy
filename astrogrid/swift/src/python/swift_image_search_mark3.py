#!/usr/bin/env python
import string, sys, os, time, thread, math
import xmlrpclib
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

#
# Global arguments passed in...
#
ra = 0
dec = 0
radius = 0
ifile = ''
odir = ''
minimages = 2
maximages = 5

#
# Global variables to control logging:
DEBUG = True
TRACE = True
#
# Format of image files requested.
# Can be a coma-delimited string:
# FORMAT = 'image/fits,image/png,image/jpeg,image/gif'
#
# Can be set to 'ALL', but you must be prepared to filter.
# Some services provide lots of bumf, eg: html with requests.
#
FORMAT = 'image/fits'

##################################################################
# SiapSearch class to substitute for the standard one,
# With this one I can switch options on/off.
#
class SiapSearch:

	ar = None
	tables = None

	def __init__( self, ivorn ):
		#connect to acr
		fname = os.path.expanduser( "~/.astrogrid-desktop" )
		assert os.path.exists( fname ),  'No AR running: Please start your AR and rerun this script'
		prefix = file( fname ).next().rstrip()
		SiapSearch.ar = xmlrpclib.Server( prefix + "xmlrpc" )
		self.ivorn = ivorn
		#
		#import service we're going to use from ar.
		SiapSearch.tables = SiapSearch.ar.util.tables

	#
	#
	def execute( self, ra, dec, radius ):	
		global FORMAT
		# use AR to build the query URL       
		query = SiapSearch.ar.ivoa.siap.constructQuery( self.ivorn, ra, dec, radius )
		fullURL = SiapSearch.ar.ivoa.cone.addOption( query, "FORMAT", FORMAT )
		doc = SiapSearch.tables.convertFromFile( fullURL, "votable", "votable" )
		return doc

##################################################################
# A simple class to hold service specific data.
#
class Service:

	def __init__( self, name, ivorn ):
		self.name = name
		self.ivorn = ivorn
		self.siapSearch = None         # The search itself is created later
### end of class Service ##########################################


##############################################################
# A simple local class to help manage thread termination 
#
class TerminateFlag:

	def __init__( self ):
		self.flag = [ False ]
		self.lock = thread.allocate_lock()

	def isSet( self ):
		self.lock.acquire()
		flagValue = self.flag[0]
		self.lock.release()
		return flagValue

	def set( self ):
		self.lock.acquire()
		self.flag[0] = True 
		self.lock.release()
		return
### end of class TerminateFlag #########################

#
# Validate arguments passed. 
# Nothing fancy here, so I'm not using anything heavyweight.
# 
# arg1: ra 
# arg2: dec
# arg3: radius
# arg4: path to file containing list of service ivorns.
# arg5: unique name for the overall search directory to hold image results for this search (will be created!).
# arg6: the minimum number of images to be considered suitable from a source
# arg7: the maximum number of images to be downloaded from any one service
#
def validateArgs():
	global ra, dec, radius, ifile, odir, minimages, maximages
	# If no arguments were given, print a helpful message
	if len(sys.argv)!=8:
		print 'Usage: ra dec radius file_of_ivorns results_directory min_images_acceptable max_images_from_each_service'
		sys.exit(0)
	else:
		ra = float( sys.argv[1] )
		dec = float( sys.argv[2] )
		radius = float( sys.argv[3] )
		ifile = sys.argv[4]
		odir = sys.argv[5]
		minimages = int(sys.argv[6])
		maximages = int(sys.argv[7])
	return
# end of validateArgs()

#
# Process the input file of service ivorns.
#
# For each image source we wish to search, there is a list of alternative ivorns.
# For example, if we wished to search three image sources and there were
# two alternative services providing images for each source, that would
# make 6 ivorns altogether.
#
# Returns a list of service objects
#
#
def processIvornFile( filePath ):
	global countServices
	sources = []
	services = []
	for line in open( filePath ).readlines():
		l = line.strip()
    # Comments lines and blank lines are ignored...
		if len(l) > 0 and l.startswith( '#' ) == 0:
			# Process the line containing the number of services...
			if l.isdigit():
				if len( services ) > 0:
					sources.append( services )
					services = []
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
					# save:
					# 0: short name for this service 
          # 1: its ivorn
					service_name = bits[0].strip()
					ivorn = bits[1].strip()
					service = Service( service_name, ivorn ) 
					services.append( service )
	#
	# Add the last one on if not zero...
	if len( services ) > 0:
		sources.append( services )
	print sources
	return sources
# end of processIvornFile( filePath )


#
# Search function to pass to thread pool.
#
def searchAndRetrieve( service, ra, dec, radius, minimages, maximages, terminate ) :
	global FORMAT
	if terminate.isSet():
		print service.name + ' has been terminated prematurely!'
		return 
	try:
		if DEBUG: print 'About to image search: ' + service.name, ra, dec, radius	
		res = service.siapSearch.execute( ra, dec, radius )
		if res: 
			try:
				vot = utils.read_votable( res, ofmt='votable' )
				countImages = retrieveImages( service, vot, maximages, terminate )
				if countImages >= minimages :
					terminate.set()
			except:
				print service.name + ': bad vot!'
		else:
			print service.name + ': no result!'
	except Exception, e2:
		print service.name + ' image Search failed: ', e2	
	return
# end of searchAndRetrieve( image_directory, siap, ra, dec, radius, maximages )

#
# Download function (retrieves images).
# 
def retrieveImages( service, vot, maximages, terminate ) :
	# Get column index for access url
	urlColIdx = vot.getColumnIdx ('VOX:Image_AccessReference')
	formatIdx = vot.getColumnIdx ('VOX:Image_Format')
	if urlColIdx < 0:
		print "No access reference found"
		return 0
	
	# Examine this. There's something lax here...
	dataRows = vot.getDataRows ()
	getData = vot.getData
	countImages = len( dataRows )
	print "Number of records: ", countImages
#	if countImages > 0:
#		print( 'Making image directory: ' + image_directory )
#		os.mkdir( image_directory )
	cnt = 0
	for row in dataRows:
		if terminate.isSet():
			print service.name + ' has been terminated prematurely!'
			break 
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
		
		iname = "%s_image%d%s" % ( service.name, cnt, ext )
		print "Saving image %d as %s" % (cnt, iname) 
		retrieveOneImage( url, iname )	
		cnt += 1	
		if cnt >= maximages:
			cnt -= 1
			break
	return cnt
# end of retrieveImages( image_directory, vot, maximages )

#
# Retrieve one image 
#
def retrieveOneImage( url, fname ) :
	global odir
	infile = urlopen( url )
	try:
		outfile = file( odir + '/' + fname, "wb" )
		outfile.write( infile.read () )
	except Exception, e:
		print "Failed to retrieve image from ", url, e
	infile.close()
	outfile.close()
	return
# end of retrieveOneImage( image_directory, url, fname )

#####################################################
#    Mainline                                       #
#####################################################
print 'Started at: ' + time.strftime('%T') 
#
# Validate the input arguments passed to us...
validateArgs()
print ra, dec, radius, ifile, odir, minimages, maximages
#sys.exit(0)

#
# Retrieve ivorns for the sources we wish to search...
sources = processIvornFile( ifile )
#
# We have a list of sources and a list of services for each source.
# Generate a cone search for each service...
serviceCount = 0
for source in sources:
	# We have a list of services for each source...
	for service in source:
		# Generate a siap search for each service...
		try:
			if DEBUG: print 'Generating service for: ' + service.name + ' ivorn: --->' + service.ivorn + '<---'
			service.siapSearch = SiapSearch( service.ivorn )
			serviceCount += 1 # The count gets incremented only if the service is kosher
		except:
			# A failure means we have to deal with a None value in the service!
			print 'SiapSearch() failed for ' + service.name

#
# Create the overall search output directory...
if DEBUG: print( 'Output folder is called ' + odir )
os.mkdir( odir )

#
# Define the command to execute and the pool size
pool = easy_pool( searchAndRetrieve )
pool.start_threads( serviceCount )

for source in sources:
	# We have a list of services for each source.
	# Fill out the thread information...
	iIndex = 0
	terminateFlag = TerminateFlag()
	for service in source:
		# This is where we deal with a None value within the service object
		if isinstance( service.siapSearch, SiapSearch ):
			#
			# Add the service and its inputs to the thread pool...
			input = ( service, ra, dec, radius, minimages, maximages, terminateFlag )
			pool.put( input )

#
# Now observe the real work:
# Print information while executing...
i=0
while 1 :
	p = pool.qinfo()
	print "Time: %3d sec    Queued: %2d    Running: %2d    Finished: %2d" % \
                                        (i, p[1], serviceCount-p[1]-p[3], p[3])
	time.sleep(1)
	i=i+1
	if p[3]==serviceCount: break
    
pool.stop_threads()
print( 'Finished at '  + time.strftime('%T') ) 
