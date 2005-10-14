import org.astrogrid.acr.*

f = new Finder()
acr = f.find()

#first check what JVM we're running under.

println("Java Version: " + System.getProperty('java.version'))

myspace = acr.getService("astrogrid.myspace")	
		
#remove ms file if it prevously exists.
msfile = new java.net.URI("#writeURLToContent.test")

if (myspace.exists(msfile)) {
	myspace.delete(msfile)
}		
		
# test first using straight write and read
myspace.write(msfile,"potatoes")
result = myspace.read(msfile)
println("Straight read and write - expected 'potatoes', got " + result)
	/*
# test using using straight write and readContentURL
myspace.write(msfile,"carrots")
readURL = myspace.getReadContentURL(msfile)
result = readURL.getText()
println("Straight write and readContentURL - expected 'carrots', got " + result)

	
# test next using writeURLToContent -
# so we construct a file URL, and pass it to the ACR, and ask it to copy the data
# so basically using ACR's file transfer method.
	
	
dataFile = java.io.File.createTempFile("testing","txt")
dataFile.write("hello world")
dataURL = dataFile.toURL()	
		
myspace.copyURLToContent(dataURL,msfile)
	
# check what we've got.
result = myspace.read(msfile)
println("copyURLToContnet, then straight write - expected 'hello world', got " + result)

# test using copyURLToContent then readContentURL

dataFile.delete()
dataFile.write("oranges")
myspace.copyURLToContent(dataURL,msfile)
resultURL = myspace.getReadContentURL(msfile)
result = resultURL.getText()
println("coopyURLToContent, then readContentURL - expected 'oranges', got " + result)

	
## finally, writeContentURL, followed by readContentURL
myspace.createFile(msfile)
writeURL = myspace.getWriteContentURL(msfile)	
println(writeURL)
conn = writeURL.openConnection()
#conn.setAllowUserInteraction(false)
#conn.setDoInput(true)
conn.setChunkedStreamingMode(1024)
#conn.setUseCaches(false)
conn.setDoOutput(true)
conn.setRequestMethod("PUT")
#conn.setRequestProperty("User-Agent","org.astrogrid.filestore.common.FileStoreOutputStream")

conn.connect()
os = conn.getOutputStream()
os.write("lemons".getBytes())
#os.flush()
os.close()

println(conn.getResponseCode())

#myspace.transferCompleted(msfile)
#myspace.refresh(msfile)

resultURL = myspace.getReadContentURL(msfile)
result = resultURL.getText()
#result = myspace.read(msfile)
println("writeContentURL followed by readContentURL - expected 'lemons', got " + result)
	*/
# close the program down - necessary as we have listeners
System.exit(0)	