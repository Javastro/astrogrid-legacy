#!/usr/bin/env python
# Gets some posthoc stats from the ADQL query stresstesting (to be used
# when the main script crashed out midway)

from astrogrid import acr

#checkfolder = "ivo://ktn@org.astrogrid.regtest/community#/stresstest_folder"
checkfolder = "ivo://org.astrogrid.regtest/ktn#/stresstest_folder_smallq/"
expected = 10

def fileSizes(folderToUse, expected):
	badCount = 0
	goodCount = 0
	folderInfo = acr.astrogrid.myspace.getNodeInformation(folderToUse)
	if not folderInfo['folder']:
		print folderToUse, "is not a folder!!"
	else : 
		contents = acr.astrogrid.myspace.listIvorns(folderToUse)
		for f in contents:
			info = acr.astrogrid.myspace.getNodeInformation(f)
			filename = info['name']
			try:
				filesize = info['attributes']['org.astrogrid.filestore.content.size']
				print "File ",filename, "has size ",filesize
				if (filesize == "0") :
					badCount = badCount + 1
				else :
					goodCount = goodCount + 1
			except Exception, e :
				print "[Can't get attributes for file ",filename,"]"
				badCount = badCount + 1

	print "GOOD TRANSFERS: ",goodCount,"    BAD TRANSFERS: ",badCount,"    MISSING FILES: ",(expected - (goodCount + badCount))

fileSizes(checkfolder,expected)

