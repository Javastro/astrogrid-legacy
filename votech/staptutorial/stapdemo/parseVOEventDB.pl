use strict;

use Getopt::Long;
use VOEventSTAPExtension;
use DBI;
use LWP::Simple;



# USER CONFIGURATION - CUSTOMIZE THE FOLLOWING 4 VALUES
my $dbuser="stapuser";
my $dbpasswd="stappwd";
my $dbhost="localhost";
my $db="stap";

unless ( scalar @ARGV >= 1) {
	die "USAGE: $0 -file|-url filename";
}
my $isfile = 1;
my $status = GetOptions("file" => \$isfile, "url" => sub {$isfile = 0});

my $object;

if ($isfile) {
  $object = new VOEventSTAPExtension(File => $ARGV[0]);
  } else {
  	my $xmlvoevent  = get($ARGV[0]);
  	#read URL
    $object = new VOEventSTAPExtension(XML => $xmlvoevent);
  }

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
         my $refs = $object->references();

         my $dbtable = "ogle";
         my $url;
         
           my $dbh = DBI->connect('DBI:mysql:' . $db . ';host=' . $dbhost, $dbuser,$dbpasswd) || die "Could not connect to database: $DBI::errstr";

           # Insert ivorn, url, ra, dec, starttime, stoptime, timeinstant, concept, name, contactname, contactemail, params, refs
           $dbh->do("INSERT INTO $dbtable (ivorn, url, ra, dc, start, stop, instant, concept, name, 
              contactname, contactemail, params, refs) 
              values('$id', '$url', '$ra', '$dec', '$starttime', '$stoptime', 
              '$timeinstant', '$concept', '$name', '$contactname', '$contactemail', '$params', '$refs')");
