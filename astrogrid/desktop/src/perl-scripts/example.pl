#!/usr/bin/perl

use Frontier::Client;

# create the server
# don't know how to find current user's home dir, or how to read in files nicely.
open(CONFIG_FILE,"/home/noel/.astrogrid-desktop") || die("Could not open acr config - check ACR is running");
$prefix=<CONFIG_FILE>;
close(CONFIG_FILE);
chomp $prefix;
$url = $prefix . "xmlrpc";
print $url;
$server = Frontier::Client->new(url => $url);

# call some methods on the server
$record = $server->call('astrogrid.registry.getRecord','ivo://org.astrogrid/Pegase');
print $record, "\n";

$endpoint = $server->call('astrogrid.registry.resolveIdentifier','ivo://uk.ac.le.star/filemanager');
print $endpoint, "\n";
