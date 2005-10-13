import org.astrogrid.acr.*

f = new Finder()
acr = f.find()
browser =  acr.getService(org.astrogrid.acr.dialogs.ResourceChooser.class)
uri = browser.fullChooseResource("Select destination",true,false,false);
System.out.println(uri);
#myspace =  acr.getService(org.astrogrid.acr.astrogrid.Myspace.class);
#url = new java.net.URL("http://www.slashdot.org")
#myspace.copyURLToContent(url, uri);
#System.out.println("FINISHED")
