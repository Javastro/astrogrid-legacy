#!/software/perl-5.8.6/bin/perl

# NOTE: SEE USER CONFIGURATION OPTIONS LINES 7 AND 108
  
#use strict;

# USER CONFIGURATION - CUSTOMIZE THE FOLLOWING 4 VALUES
my $dbuser="stapuser";
my $dbpasswd="stappwd";
my $dbhost="localhost";
my $db="stap";


use IO::Socket;
use POSIX qw/:sys_wait_h/;
use Errno qw/EAGAIN/;
use Getopt::Long;

use VOEventSTAPExtension;
use DBI;

unless ( scalar @ARGV >= 2 ) {
   die "USAGE: $0 [-host hostname] [-port portname]\n";
}

my ( $host, $port );   
my $status = GetOptions( "host=s"	=> \$host,
			 "port=s"	=> \$port  ); 

unless ( defined $host && defined $port ) {
   $host = "127.0.0.1";
   $port = "9999";
}   
 
SOCKET: { 
       
print "Opening client connection to $host:$port\n";    
my $sock = new IO::Socket::INET( PeerAddr => $host,
                                 PeerPort => $port,
                                 Proto    => "tcp" );

unless ( $sock ) {
    my $error = "$@";
    chomp($error);
    print "Warning: $error\n";
    print "Warning: Trying to reopen socket connection...\n";
    sleep 5;
    redo SOCKET;
};           


my $message;
print "Socket open, listening...\n";
my $flag = 1;    
while( $flag ) {
   my $length;  
   my $bytes_read = read( $sock, $length, 4 );

   next unless defined $bytes_read;
   
   print "\nRecieved a packet from $host...\n";
   if ( $bytes_read > 0 ) {

      print "Recieved $bytes_read bytes on $port from ".$sock->peerhost()."\n";
          
      $length = unpack( "N", $length );
      if ( $length > 512000 ) {
        print "Error: Message length is > 512000 characters\n";
        print "Error: Message claims to be $length long\n";
        print "Warning: Discarding bogus message\n";
      } else {   
         
         print "Message is $length characters\n";               
         $bytes_read = read( $sock, $message, $length); 
      
         print "Read $bytes_read characters from socket\n";
         # callback to handle incoming Events     
	 print $message . "\n";

         # Get parameters from VOEvent packet to store in database

         my $object = new VOEventSTAPExtension( XML => $message );

    if ( $object->role() ne "iamalive" ) {
         my $id = $object->id();
         my $ra = $object->ra();
         my $dec = $object->dec();
         my $timeinstant = $object->timeinstant();
         my $starttime = $object->starttime();
         my $stoptime = $object->stoptime();
         my $concept = $object->concept();
         my $name = $object->name();
         my $contactname = $object->contactname();
         my $contactemail = $object->contactemail();
         my $params = $object->params();
         my $references = $object->references();

         my $url;

         # PRINT MESSAGE TO FILE
         # NOTE: uncomment the next 9 lines of code only if you wish to save the voevent
         # packet as an XML file inside a web accessible directory. If you do not
         # wish to do this, leave the code commented out and either
         # 1) write a few lines of code to set $url to the remote location of the VOEvent packet, or
         # 2) leave $url as a blank value in your database. 
         # If $url is blank, then an astroscope user will still be able to view params and reference URLs in 
         # the VOEvent spidergram, but they will not be able to open the VOEvent packet itself in a browser.

         # USER MUST CONFIGURE $DIR AND $URLBASE

         #my $file = substr($id, rindex($id, "/")+1, length($id)-rindex($id, "/")) . ".xml";
         #my $subdir = substr($id, rindex($id, "/", rindex($id, "/")-1)+1,  rindex($id, "/")-(rindex($id, "/", rindex($id, "/")-1)+1));
         #my $dir = '/full/path/to/directory/';
         #my $filename = $dir . $subdir . "_" . $file;
         #my $urlbase = "http://your.server.com/path/to/filename/";
         #$url = $urlbase . $file;
         #open (DATA, ">$filename") || die("Cannot open this file: $filename");
         #print DATA "$message";
         #close(DATA);


         # Parse ivorn and select $table based on ivorn
         # NOTE: adjust the parsing code to look only for VOEvents that you want to store in your database
         # Try connecting to eSTAR broker on -host vo.astro.ex.ac.uk -port 8099

         my $dbtable = "";

         # Check for GCN events:
         if (index($id, "#gcn") gt -1) { 
           $dbtable = "gcn"; 
         } 
         # Check for OGLE events:
         elsif (index($id, "ogle#") gt -1) {
           $dbtable = "ogle";
         }
         # Check for SDSS events:
         elsif (index($id, "#sdssss") gt -1) {
           $dbtable = "sdss";
         }
         # Check for ROBONET events:
         elsif (index($id, "robonet-1.0") gt -1) {
           $dbtable = "robonet";
         }
         # Check for ESSENCE events:
         elsif (index($id, "#essence") gt -1) {
           $dbtable = "essence";
         }
         # Check for locally produced test events:
         elsif (index($id, "test") gt -1) {
           $dbtable = "test1";
         } 

         # If $dbtable has been assigned, insert VOEvent values into table
         if ($dbtable ne "") {

           # insert data into DB
           #my $dbh = DBI->connect('DBI:mysql:' . $db . ';host=' . $dbhost, '$dbuser','$dbpasswd') || die "Could not connect to database: $DBI::errstr";
           print $db . $dbhost . $dbuser . $dbpasswd . "\n";
           my $dbh = DBI->connect('DBI:mysql:' . $db . ';host=' . $dbhost, $dbuser,$dbpasswd) || die "Could not connect to database: $DBI::errstr";

           # Insert ivorn, url, ra, dec, starttime, stoptime, timeinstant, concept, name, contactname, contactemail, params, refs
           $dbh->do("INSERT INTO $dbtable (ivorn, url, ra, dc, start, stop, instant, concept, name, 
              contactname, contactemail, params, refs) 
              values('$id', '$url', '$ra', '$dec', '$starttime', '$stoptime', 
              '$timeinstant', '$concept', '$name', '$contactname', '$contactemail', '$params', '$refs')");
         }
    }
	 my $response;
	 if ( $object->role() eq "iamalive" ) {
	    $response = $message;
         } else {
	    $response = "<?xml version='1.0' encoding='UTF-8'?>"."\n".
'<VOEvent role="ack" id="ivo://estar.ex/144.173.229.20.1" version="1.1">'."\n".
' <Who>'."\n".
'   <PublisherID>ivo://ex.ac.uk</PublisherID>'."\n".
' </Who>'."\n".
'</VOEvent>'."\n";
	 }
	 
	 my $bytes = pack( "N", length($response) ); 
         print "Sending " . length($response) . "bytes to socket\n";
         print $sock $bytes;
         $sock->flush();
         print $sock $response;
	 print "$response\n";
         $sock->flush(); 
	 print "Done.\n";
	  
      }
                      
   } elsif ( $bytes_read == 0 && $! != EWOULDBLOCK ) {
      print "Recieved an empty packet on $port from ".$sock->peerhost()."\n";   
      print "Closing socket connection...";      
      $flag = undef;
   } elsif ( $bytes_read == 0 ) {
      print "Recieved an empty packet on $port from ".$sock->peerhost()."\n";   
      print "Closing socket connection...";      
      $flag = undef;   
   }
   
   unless ( $sock->connected() ) {
      print "Warning: Not connected, socket closed...\n";
      $flag = undef;
   }    

}  
  
print "Warning: Trying to reopen socket connection...\n";
redo SOCKET;

   
}  
exit;                                     
  
