#Quick script for packrat to setup the myspace database on astrogrid1
#14.12.03 JDT
# you should have astrogrid1.servers in your current directory (the base dir of the webapp)
java -cp "WEB-INF/classes/:WEB-INF/lib/hsqldb-1.7.1.jar" org.astrogrid.mySpace.mySpaceDemo.CreateMySpaceRegistry astrogrid1
