This program is desiged to generate simple sql from a list of fits files 
to be used to insert into a database.  You will find in this same directory a 
sqlgen.props that has properties to customize the sql.
To run the program:
Unix/Linux/Max
./generate.sh -file {filename} {tablename}
Windows
generate.bat -file {filename} {tablename}

Whereby {filename} is a simple text file with a fits file url per line of the text file.  A url is typically
'http://....'  but you may desire to use 'file://...' but using the file:// reference will require
you to run a string replace/subst command on the insert.sql file to produce a valid url to serve up the fits file.

{tablename} is like it says is the table name to be used for the database insert and create statements.
This program will generate a {tablename}_insert.sql and a {tablename}_create.sql as output.