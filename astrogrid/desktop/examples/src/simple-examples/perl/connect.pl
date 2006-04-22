#!/usr/bin/perl
#Noel Winstanley, Astrogrid, 2005
#basic perl example - incomplete.
#connects to acr using xmlrpc interface.

#xmlrpc client for perl, downloadable from cpan
use Frontier::Client;

# create the server
# don't know how to find current user's home dir, 
#or how to read in files nicely - hope someone can show me this
open(CONFIG_FILE,"/home/noel/.astrogrid-desktop") 
   || die("Could not open acr config - check ACR is running");
$prefix=<CONFIG_FILE>;
close(CONFIG_FILE);
chomp $prefix;
$url = $prefix . "xmlrpc";
#create xmlrpc client
$acr = Frontier::Client->new(url => $url);

# call some methods on the acr
$record = $acr->call('astrogrid.registry.getRecord'
			,'ivo://org.astrogrid/Pegase');
print $record, "\n";

$endpoint = $acr->call('astrogrid.registry.resolveIdentifier'
			,'ivo://uk.ac.le.star/filemanager');
print $endpoint, "\n";
