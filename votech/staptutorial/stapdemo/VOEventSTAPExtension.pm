package VOEventSTAPExtension;


=head1 NAME

eSTAR::RTML - Object interface to parse and create VOEvent messages

=head1 SYNOPSIS

To parse a VOEvent file,

   $object = new Astro::VO::VOEvent2( File => $file_name );
  
or    

   $object = new Astro::VO::VOEvent2( XML => $scalar );
   
Or to build a VOEVENT file,   
 
   $xml = $object->build( %hash );
 

=head1 DESCRIPTION

The module can parse VOEvent messages, and serves as a limited convenience
layer for building new messages. Functionality is currently very limited.

=cut

# L O A D   M O D U L E S --------------------------------------------------

use strict;
use vars qw/ $VERSION $SELF /;

use XML::Parser;
use XML::Simple;
use XML::Writer;
use XML::Writer::String;

use Net::Domain qw(hostname hostdomain);
use File::Spec;
use Carp;
use Data::Dumper;

use Astro::VO::VOEvent;

our ($references);
our @ISA=("Astro::VO::VOEvent");


'$Revision: 1.1 $ ' =~ /.*:\s(.*)\s\$/ && ($VERSION = $1);

# C O N S T R U C T O R ----------------------------------------------------

=head1 REVISION

$Id: VOEventSTAPExtension.pm,v 1.1 2008/06/19 16:31:33 pah Exp $

=head1 METHODS

=head2 Constructor

=over 4

=item B<new>

Create a new instance from a hash of options

  $object = new Astro::VO::VOEvent( );

returns a reference to an VOEvent object.

=cut

sub new {
  my $proto = shift;
  my $class = ref($proto) || $proto;

  # bless the query hash into the class
  my $block = bless { DOCUMENT => undef,
                      WRITER   => undef,
                      BUFFER   => undef }, $class;

  # Configure the object
  $block->configure( @_ ); 

  return $block;

}

# A C C E S S O R   M E T H O D S -------------------------------------------

=back

=head2 Accessor Methods

=over 4

 
=cut


=item B{timeinstant}

Return the TimeInstant of the object as given in the <WhereWhen> tag

  $object = new Astro::VO::VOEventSTAP( XML => $scalar );
  $time = $object->timeinstant();

=cut

sub timeinstant {
  my $self = shift;
  
  my $timeinstant;  
  if ( defined $self->{DOCUMENT}->{WhereWhen}->{type} &&
       $self->{DOCUMENT}->{WhereWhen}->{type} eq "simple" ) {
       
    $timeinstant = $self->{DOCUMENT}->{WhereWhen}->{Time}->{Value};
    
  } else { 
  
    # old style eSTAR default
    $timeinstant = $self->{DOCUMENT}->{WhereWhen}->{"stc:ObservationLocation"}->
      {"crd:AstroCoords"}->{"crd:Time"}->{"crd:TimeInstant"}->{"crd:ISOTime"};
    
    # RAPTOR default  
    unless ( defined $timeinstant ) {
        
       $timeinstant = $self->{DOCUMENT}->{WhereWhen}->{"stc:ObsDataLocation"}
                    ->{"stc:ObservationLocation"}->{"stc:AstroCoords"}
		    ->{"stc:Time"}->{"stc:TimeInstant"}->{"stc:ISOTime"};
	      
    }		        
    
    # new style v1.1 default
    unless ( defined $timeinstant ) {
      $timeinstant = $self->{DOCUMENT}->{WhereWhen}->{'ObsDataLocation'}
        ->{'ObservationLocation'}->{'AstroCoords'}->{"Time"}
	->{"TimeInstant"}->{"ISOTime"};
    }
    

    # Try new style v1.1 default with <ObservatoryLocation> and
    # the <AstroCoordSystem> tags
    unless ( defined $timeinstant ) {
      $timeinstant = $self->{DOCUMENT}->{WhereWhen}->{'ObsDataLocation'}
        ->{'ObservatoryLocation'}->{'ObservationLocation'}
	->{'AstroCoordSystem'}->{'AstroCoords'}
	->{"Time"}->{"TimeInstant"}->{"ISOTime"};
    }    
  }  
  
  # There isn't a (valid?) <WhereWhen> see if there is a timestamp in
  # the <Who> tag as this might also carry a publication datestamp.
  unless ( defined $timeinstant ) {
    $timeinstant = $self->{DOCUMENT}->{Who}->{Date};
  }
  
  return $timeinstant;
}

=item B<starttime>

=cut

sub starttime {
  my $self = shift;
  my $starttime;  
  if ( defined $self->{DOCUMENT}->{WhereWhen}->{type} &&
       $self->{DOCUMENT}->{WhereWhen}->{type} eq "simple" ) {
       
    $starttime = $self->{DOCUMENT}->{WhereWhen}->{Time}->{Value};
    
  } else { 
    # old style eSTAR default
    $starttime = $self->{DOCUMENT}->{WhereWhen}->{"stc:ObservationLocation"}->
      {"crd:AstroCoords"}->{"crd:Time"}->{"crd:TimeInstant"}->{"crd:ISOTime"};
    
    # SOLAR LASCO, BATSE, NOAA default  
    unless ( defined $starttime ) {
        
       $starttime = $self->{DOCUMENT}->{WhereWhen}->{"stc:ObsDataLocation"}
                    ->{"stc:ObservationLocation"}->{"stc:AstroCoordArea"}
		    ->{"stc:TimeInterval"}->{"stc:StartTime"}->{"stc:ISOTime"};
    }
  }
  # There isn't a (valid?) <WhereWhen> see if there is a timestamp in
  # the <Who> tag as this might also carry a publication datestamp.
  unless ( defined $starttime ) {
    $starttime = $self->{DOCUMENT}->{Who}->{Date};
  }
  
  return $starttime;
}

=item B{stoptime}

 Try to get stop time from solarevents

=cut

sub stoptime {
  my $self = shift;
  
  my $stoptime;  
  if ( defined $self->{DOCUMENT}->{WhereWhen}->{type} &&
       $self->{DOCUMENT}->{WhereWhen}->{type} eq "simple" ) {
       
    $stoptime = $self->{DOCUMENT}->{WhereWhen}->{Time}->{Value};
    
  } else { 
  
    # old style eSTAR default
    $stoptime = $self->{DOCUMENT}->{WhereWhen}->{"stc:ObservationLocation"}->
      {"crd:AstroCoords"}->{"crd:Time"}->{"crd:TimeInstant"}->{"crd:ISOTime"};
    
    # SOLAR LASCO, BATSE, NOAA default  
    unless ( defined $stoptime ) {
        
       $stoptime = $self->{DOCUMENT}->{WhereWhen}->{"stc:ObsDataLocation"}
                    ->{"stc:ObservationLocation"}->{"stc:AstroCoordArea"}
		    ->{"stc:TimeInterval"}->{"stc:StartTime"}->{"stc:ISOTime"};
	      
    }		        
  }  
  
  # There isn't a (valid?) <WhereWhen> see if there is a timestamp in
  # the <Who> tag as this might also carry a publication datestamp.
  unless ( defined $stoptime ) {
    $stoptime = $self->{DOCUMENT}->{Who}->{Date};
  }
  
  return $stoptime;
}


=item B{concept}

 Try to get concept from events

=cut

sub concept {
  my $self = shift;
  my $concept;  
  $concept = $self->{DOCUMENT}->{Why}->{Inference}->{Concept};
  if ($concept eq "") {
    $concept = $self->{DOCUMENT}->{Why}->{Concept};
  }
  return $concept;
}

=item B{name}

 Try to get event name from events

=cut

sub name {
  my $self = shift;
  my $name;  
  $name = $self->{DOCUMENT}->{Why}->{Inference}->{Name};
  if ($name eq "") {
    $name = $self->{DOCUMENT}->{Why}->{Name};
  }
  return $name;
}

=item B{contactname}

 Try to get contact name from events

=cut

sub contactname {
  my $self = shift;
  my $contactname;  
  $contactname = $self->{DOCUMENT}->{Who}->{Author}->{contactName};
  return $contactname;
}

=item B{contactname}

 Try to get contact name from events

=cut

sub contactemail {
  my $self = shift;
  my $contactemail;  
  $contactemail = $self->{DOCUMENT}->{Who}->{Author}->{contactEmail};
  return $contactemail;
}


=item B{references}

Return the <Reference>'s name and uri from the <What> tag,

  $object = new Astro::VO::VOEvent( XML => $scalar );
  $references = $object->references();

=cut

sub references {
  my $self = shift;
  my $refs = "";
  my $ref = "";
  my $refname = "";
  my $refuri = "";
  if ( $self->{DOCUMENT}->{What}->{Reference}->{'uri'} ne "") {
    $refname = $self->{DOCUMENT}->{What}->{Reference}->{'name'};
    $refuri = $self->{DOCUMENT}->{What}->{Reference}->{'uri'};
  }
  else {

    while ( my ($key, $value) = each ( %{$self->{DOCUMENT}->{What}->{Reference}})) {
      if ( ref($value) eq "HASH") {
        $refname = $key;
        $refuri = $value->{'uri'};
      }
      else {
        if ($key eq "name") {
          $refname = $value;
        } 
        if ($key eq "uri") {
          $refuri = $value;
        }
      }
    }  

  } 

  if ($refuri ne "") {
    if ($refname ne "") {
      $ref = $refname . "=" . $refuri;
    }
    else {
     $ref = "ref" . "=" . $refuri;
    }
    $refs = $refs . $ref . ", "; 
  }

  $refs = $refs . $references;
  $references = "";
  return $refs;
}

=item B{params}

Return the <Group><Param></Group>'s name, value and unit from the <What> tag,

  $object = new Astro::VO::VOEvent( XML => $scalar );
  $group_params = $object->group_params();

=cut

sub params {
  my $self = shift;
  my $param = "";
  my $params = "";

  # Get each reference contained  in //VOEvent/What
  while ( my ($key, $value) = each ( %{$self->{DOCUMENT}->{What}})) {

    # Test reference for hash of  //VOEvent/What/Group
    if ( ref($value) eq "HASH" && $key eq "Group" ) {

          if ($value->{Param} eq "") {
              while ( my ($key2, $value2) = each ( %{$value})) { 
                while ( my ($key3, $value3) = each ( %{$value2->{Param}} ) ) {
                  $param = param_vals($key3, $value3);
                  if ($param ne "") {
                    $params = $params . $param . ", ";
                  }
                }
              }
          } else {
               while ( my ($key2, $value2) = each ( %{$value->{Param}} ) ) {
                 while ( my ($key3, $value3) = each ( %{$value2->{Param}} ) ) {
                   $param = param_vals($key3, $value3);
                   if ($param ne "") {
                     $params = $params . $param . ", ";
                  }
                }
              }

          }
    }

    # Test reference for hash of  //VOEvent/What/Param
    if ( ref($value) eq "HASH" && $key eq "Param" ) {
      if ($value->{'value'} ne "") {
        $param = construct_param($value->{'name'}, $value->{'value'}, $value->{'unit'}, $value->{'units'});
        if ($param ne "") {
          $params = $params . $param . ", ";
        }
      }
      else {
    
        # Get comma-separated param key / value pairs below What
        while ( my ($key2, $value2) = each ( %{$value})) {
            $param = param_vals($key2, $value2);
            if ($param ne "") {
              $params = $params . $param . ", ";
            }
        }
      }  
    }

    # Test reference for array of  //VOEvent/What/Group
    if ( ref($value) eq "ARRAY" && $key eq "Group" ) {
      foreach (@{$value}) {
        while ( my ($key2, $value2) = each ( %{$_->{Param}})) {
            $param = param_vals($key2, $value2);
            if ($param ne "") {
              $params = $params . $param . ", ";
            }
        }  
      } 
    }
  }

  return $params;
}

=item B{params_vals}

Return the <Group><Param></Group>'s name, value and unit from the <What> tag,

  $object = new Astro::VO::VOEvent( XML => $scalar );
  $param = param_vals(key, value);

=cut

#sub param_vals($my($key2, my $value2)) {
sub param_vals {

  my($key2, $value2) = @_;

  my $param_key = "";
  my $param_value = "";
  my $param_unit = "";
  my $param_units = "";
  my $param = "";

  $param_key = $key2;
  $param_value = $value2->{'value'};
  $param_unit = $param_unit = "$value2->{'unit'}";
  $param_units = $param_unit = "$value2->{'units'}";

  $param = construct_param($param_key, $param_value, $param_unit, $param_units);
}

#su param_vals {
sub construct_param {

  my $param_unit = "";
  my $param_units = "";
  my $param = ""; 

  my($param_key, $param_value, $param_unit, $param_units) = @_;

      # Check for param values that should be reference files
      if ((substr $param_value, 0, 6) eq "ftp://") {
        $references = $references . "$param_key=$param_value" . ", ";
      }
      elsif ((substr $param_value, 0, 7) eq "http://") {
        $references = $references . "$param_key=$param_value" . ", ";
      }
      else {

        # Get the parameter's name from $key2 and its value from $value2->'value'
        $param = "$param_key=$param_value";

        # Get the parameter's unit from $value2->'unit'...
        if ($param_unit ne "") {
          $param = $param .  " " . $param_unit; 
        }

        #...unless the parameter's unit is in $value2->'units'...
        elsif ($param_unit eq "") {
          if ($param_units ne "") {
            $param = $param .  " " . $param_units; 
          }

          # ...unless the parameter has no unit.
          elsif ($param_units eq "") {
            $param = $param; 
          }
        }
      }
  return $param;
}

# C O N F I G U R E ---------------------------------------------------------

=back

=head2 General Methods

=over 4

=item B<configure>

Configures the object, takes an options hash as an argument

  $rtml->configure( %options );

does nothing if the hash is not supplied.

=cut

sub configure {
  my $self = shift;

  # BLESS XML WRITER
  # ----------------
  $self->{BUFFER} = new XML::Writer::String();  
  $self->{WRITER} = new XML::Writer( OUTPUT      => $self->{BUFFER},
                                     DATA_MODE   => 1, 
                                     DATA_INDENT => 4 );
				     
  # CONFIGURE FROM ARGUEMENTS
  # -------------------------

  # return unless we have arguments
  return undef unless @_;

  # grab the argument list
  my %args = @_;
				        
  # Loop over the allowed keys
  for my $key (qw / File XML / ) {
     if ( lc($key) eq "file" && exists $args{$key} ) { 
        $self->_parse( File => $args{$key} );
	last;
	
     } elsif ( lc($key) eq "xml"  && exists $args{$key} ) {
        $self->_parse( XML => $args{$key} );
	last;
	      
     }  
  }				     

  # Nothing to configure...
  return undef;

}

# T I M E   A T   T H E   B A R  --------------------------------------------

=back

=head1 COPYRIGHT

Copyright (C) 2002 University of Exeter. All Rights Reserved.

This program was written as part of the eSTAR project and is free software;
you can redistribute it and/or modify it under the terms of the GNU Public
License.

=head1 AUTHORS

Alasdair Allan E<lt>aa@astro.ex.ac.ukE<gt>,

=cut

# P R I V A T E   M E T H O D S ------------------------------------------

=begin __PRIVATE_METHODS__

=head2 Private Methods

These methods are for internal use only.

=over 4

=item B<_parse>

Private method to parse a VOEvent document

  $object->_parse( File => $file_name );
  $object->_parse( XML => $scalar );

this should not be called directly
=cut


# L A S T  O R D E R S ------------------------------------------------------

1;                                                                  
