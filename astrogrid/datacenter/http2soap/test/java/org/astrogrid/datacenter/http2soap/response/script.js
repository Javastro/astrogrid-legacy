importPackage(java.lang);
importPackage(java.nio);
importPackage(java.io);
/// example script - doesn't do anything very interesting.
var bos = new ByteArrayOutputStream();
var os = new PrintWriter( new OutputStreamWriter(bos));
os.print("hi there");
os.close();
var bb = ByteBuffer.wrap(bos.toByteArray());
outChan.write(bb);