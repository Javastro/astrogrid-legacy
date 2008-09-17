#!/usr/bin/env python
import string, sys, os, time, thread
from astrogrid import acr
from astrogrid import ConeSearch
from astrogrid import utils
from astrogrid.threadpool import easy_pool
from urllib import *
from urlparse import *

#
#		Swift catalogue cone search.
#

#
# Global arguments passed in...
#
ra = 0
dec = 0
radius = 0
ifile = ''
odir = ''

#
# Index into sources/services list...
SI_SERVICE_TAG = 0
SI_IVORN = 1
SI_TABLE = 2
SI_COLS = 3
SI_STAR_ARGS = 4
SI_GALAXY_ARGS = 5
SI_CONE = 6

#
# A simple local class to help manage thread termination 
#
class Termination:

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

#
# Validate arguments passed. 
# Nothing fancy here, so I'm not using anything heavyweight.
# 
# arg1: ra 
# arg2: dec
# arg3: radius
# arg4: path to file containing list of service ivorns and other info
# arg5: unique name for the overall search directory to hold results.
#
def validateArgs():
	global ra, dec, radius, ifile, odir
	# If no arguments were given, print a helpful message
	if len(sys.argv)!=6:
		print 'Usage: ra dec radius file_of_ivorns results_directory'
		sys.exit(0)
	else:
		ra = float( sys.argv[1] )
		dec = float( sys.argv[2] )
		radius = float( sys.argv[3] )
		ifile = sys.argv[4]
		odir = sys.argv[5]
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
# Returns a two-dimensional list of service information
#
#
def processIvornFile( filePath ):
	sources = []
	ivorns = []
	for line in open( filePath ).readlines():
		l = line.strip()
    # Comments lines are ignored...
		if len(l) > 0 and l.startswith( '#' ) == 0:
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
					# save:
					# 0: the name for this service 
          # 1: its ivorn
					# 2: table name, or nothing if only one table in the collection
					# 3: space separated list of column names
					# 4: stars flag, which is one of SOME, NONE or ALL
					# 5: galaxies flag, which is one of SOME, NONE or ALL
					service_tag = bits[0].strip()
					ivorn = bits[1].strip()
					table_name = bits[2].strip()
					#
					# Setting the table to None makes the cone object easier to invoke...
					if len(table_name) == 0:
						table_name = None 
					col_names = bits[3].strip()
					stars_flag = bits[4].strip()
					gals_flag = bits[5].strip()
					ivorns.append( [ service_tag, ivorn, table_name, col_names, stars_flag, gals_flag ] ) 
	#
	# Add the last one on if not zero...
	if len( ivorns ) > 0:
		sources.append( ivorns )
	print sources
	return sources
# end of processIvornFile( filePath )

#
# Search function to pass to thread pool.
# ( service_tag, i[ SI_CONE ], i[SI_TABLE], ra, dec, radius, terminate )
def searchAndRetrieve( service_tag, cone, table, ra, dec, radius, terminate ) :
	if terminate.isSet():
		print service_tag + ' has been terminated prematurely!'
		return 
	try:
		print 'searchAndRetrieve: ' + service_tag, cone, table, ra, dec, radius	
		res = cone.execute( ra, dec, radius, dsatab=table )
		print res
		terminate.set()
	except Exception, e:
		print service_tag + ' cone Search failed: ', e	
	return
	
# end of searchAndRetrieve( service_tag, cone, table, ra, dec, radius, terminate )


#####################################################
#    Mainline                                       #
#####################################################
print 'Started at: ' + time.strftime('%T') 
#
# Validate the input arguments passed to us...
validateArgs()
print ra, dec, radius, ifile, odir

#
# Retrieve ivorns for the sources we wish to search...
sources = processIvornFile( ifile )
#sys.exit(0)

#
# Log in to Astrogrid runtime...
#print 'Logging in to Astrogrid runtime at ' + time.strftime('%T')
#acr.login() 				
#print 'Logged in at ' + time.strftime('%T') 
#
# Create the overall search output directory...
print( 'Output folder is called ' + odir )
os.mkdir( odir )

#
#
# We have a list of sources and a list of ivorns for each source.
# Generate a service for each ivorn...
serviceCount = 0
for s in sources:
	# We have a list of ivorns for each source...
	iIndex = 0
	for i in s:
		# Generate a service for each ivorn...
		try:
			print 'Generating service for: ' + i[ SI_IVORN ] 
			cone = ConeSearch( i[ SI_IVORN ] )
			i.append( cone )
			serviceCount += 1
			print cone.info['content']['description']
		except:
			print 'ConeSearch() failed for ' + i[ SI_IVORN ]
			i.append( None )

#
# Define the command to execute and the pool size
pool = easy_pool( searchAndRetrieve )
#pool.start_threads( len(sources) )
pool.start_threads( serviceCount )

for s in sources:
	# We have a list of ivorns for each source.
	# Fill out the thread information...
	iIndex = 0
	terminate = Termination()
	for i in s:
		if isinstance( i[ SI_CONE ], ConeSearch ):
			#
			# Form the output service tag name...
			service_tag = odir + '/' + i[ SI_SERVICE_TAG ]
			# Add the service and its inputs to the thread pool...
			input = ( service_tag, i[ SI_CONE ], i[SI_TABLE], ra, dec, radius, terminate )
			pool.put( input )

#print sources

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
