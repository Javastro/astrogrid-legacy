#!/usr/bin/env python
import string, sys

#
#
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
				ivorns.append( l )
	if len( ivorns ) > 0:
		sources.append( ivorns )
	return sources

#
# Mainline...
#
validateArgs()
print ra, dec, radius, ifile, odir
sources = processIvornFile( ifile )
print sources
