#! /bin/sh -
### @GENERATED_FILE@
#
# Usage: $progname [options]                    # perform incremental update
#        $progname [options] --all              # new db, upload all data
#        $progname [options] startdate enddate  # upload data in range
# Options:
#     --dry-run      : don't do anything, just show what would be done
#     --inc='spec'   : use given increment, eg --inc="5 day"
#
# The first invocation is intended for the cron job, and will do all
# the updates in the time interval between the last time this script
# ran (based on the date of the timestamp file written at the end);
# the second is intended to do a complete re-conversion and re-upload;
# and the second is for maintainance use, to upload a specific period.
# 
# The rhessi2dbase.pro script turns out to be quadratic (!) in the length
# of the interval given to it.  Uploading in increments of 5 days
# seems to be a reasonable tradeoff point.
#
# We expect the environment variable $RHESSI_DSA_HOME to be set to the
# home of this script, so that $RHESSI_DSA_HOME/scripts/upload.sh is
# this script.  The default is @BASEDIR@.
#
# `date` is rather flaky about parsing times (it seems to parse
# 2007-01-01 and 2007-01-01T00:00:00 differently!), so be rather careful
# about dates below, in general leaving times out of anything passed to `date`.
#
# $Id: upload.sh,v 1.3 2007/02/08 15:32:30 norman Exp $


progname=`basename $0`

dsahome=${RHESSI_DSA_HOME-@BASEDIR@}
if ! test -d $dsahome; then
    echo "$progname: No directory $dsahome -- exiting"
    exit 1
fi

# where do the scripts live?
scriptdir=$dsahome/bin

# the stamp file used by update mode
stampfile=$dsahome/last-run-stamp

# where does the RHESSI software live
rhessihome=/opt/rhessi

# when doing an update, where's the root of the tree where we start
# 'find'ing updates.
#
# FIXME: This is specific to 2007.  Once the upload is confirmed to be
# running smoothly, adjust this to include $rhessihome perhaps, or
# whatever the current year is.  Not ideal -- what's the best plan?
searchbase=$rhessihome/2007

# which machine is the database on
mysql_host=bern

# credentials for connection to the MySQL server
mysql_credentials='-unorman -psitrepdentist'

########################################
#
# Functions

Usage() {
    cat >&2 <<EOF
Usage: $progname [options]                    # perform incremental update
       $progname [options] --all              # new db, upload all data
       $progname [options] startdate enddate  # upload data in range
Options:
    --dry-run      : don't do anything, just show what would be done
    --inc='spec'   : use given increment, eg --inc="5 day"
EOF
    exit 1
}

process_dates() {
    start=$1
    finish=$2

    echo "$progname: process_dates $start -- $finish"
    rm -f rhessi2dbase.err

    uploadfile=rhessi.upload-$start
    rm -f $uploadfile

    {
      echo ".com $scriptdir/rhessi2dbase"
      echo "rhessi2dbase,time_interval=['$start','$finish'],outfn='$uploadfile'"
    } | $scriptdir/ssw-for-rhessi >ssw-$start.log 2>&1

    if test -f rhessi2dbase.err; then
        # send error message to (original) stdout
        echo "$progname: rhessi2dbase failed in directory $PWD"
        echo "$progname: rhessi2dbase failed in directory $PWD" >&3
        cat rhessi2dbase.err >&3
        exit 1                                     # JUMP OUT
    elif ! test -f $uploadfile; then
        echo "$progname: rhessi2dbase appears to have failed: no file $PWD/$uploadfile"
        echo "$progname: rhessi2dbase appears to have failed: no file $PWD/$uploadfile" >&3
        exit 1                                     # JUMP OUT
    else
        # all OK!
        rm -f ssw-$start.log
    fi

    # Upload this $uploadfile in a separate process
    # A call to 'exit' in this process won't abort the parent process,
    # so leave behind a file upload.stop in that case.
    (
        # collapse spaces at the beginning and end of fields
        # (delimited by tabs)
        sed 's/ *	 */	/g' $uploadfile >$uploadfile-temp
        mv $uploadfile-temp $uploadfile

        echo "load data low_priority local infile '$PWD/$uploadfile' into table rhessi;" | \
          mysql -h$mysql_host $mysql_credentials dsa >mysql-$start.log 2>&1

        status=$?
        if test $status = 0; then
            echo "$progname: successfully uploaded $uploadfile"
            echo "$progname: NOT removing $uploadfile"
            #rm -f $uploadfile mysql-$start.log
        else
            echo "$progname: mysql upload failed for file $PWD/$uploadfile" >&3
            {
                echo "mysql upload failed for file $PWD/$uploadfile"
                echo "... exit status=$status"
                echo "Logfile:"
                cat mysql-$start.log
            } >upload.stop
        fi
    ) &
    echo "$progname: launched uploader for file $uploadfile"
}

########################################
#
# Get going...

########################################
#
# Setup

dryrun=false
doall=false
daylist=false

workdir=X # dummy

# Current PID is the same as the process group ID for the processes
# started by this script.  "kill -TERM -$pgid" is how to kill the lot at once,
# including this one.
pgid=$$

# The following is a bit of whimsy, to log the memory usage
# of the group of processes.  Leave it in, just in case it
# becomes useful again.
#(
#    while :
#    do
#        echo -n "--- "
#        date --iso-8601=seconds
#        ps -o pid,pgid,stat,bsdtime,cputime,%cpu,%mem,rss,sz,vsz,maj_flt,min_flt,args
#        sleep 5
#    done
#) >pslog-$pgid &
#echo "$progname: Logging ps in $PWD/pslog-$pgid!"

########################################
#
# Argument parsing

while test $# -ge 1; do
    if expr "x$1" : 'x-' >/dev/null; then
        if test "x$1" = x--all; then
            doall=:
        elif test "x$1" = x--dry-run; then
            dryrun=:
        elif expr "x$1" : x--inc= >/dev/null; then
            inc=`expr "x$1" : 'x--inc=\(.*\)'`
        else
            Usage
        fi
    else
        break
    fi
    shift
done

# Make sure $inc is set
if test -z "$inc"; then
    inc='5 day'
fi

if $doall; then
    # ignore any arguments
    # start time before the start of the RHESSI mission on 2002-02-12
    startdate=2002-02-12
    # end date is today
    enddate=`date --utc --iso-8601`
elif test $# = 0; then
#    startdate=`date --utc --iso-8601 -d"2 days ago"`
#    # end time is midnight of today (ie, this morning)
#    enddate=`date --utc --iso-8601`
    if test -f $stampfile; then
        set `find $searchbase -newer $stampfile -name \*.fits|sed 's,/[^/]*$,,'|uniq|sed s,$rhessihome'/\(....\)/\(..\)/\(..\),\1-\2-\3,'`
        daylist=:
    else
        echo "No file $stampfile" >&2
        exit 1
    fi
elif test $# = 2; then
    startdate=$1
    enddate=$2
else
    Usage
fi

########################################
#
# Finished parsing; start work

if ! $dryrun; then
    workdir=`mktemp -p /tmp -d rhessi-upload-XXXXXX` || {
        echo "Failed to make temporary work directory"
        exit 1
    }
    cd $workdir

    echo "$progname: PGID $pgid"
    echo "$progname: work directory $workdir"

    # Save stdout to fd3
    exec 3>&1
    # Redirect stdout
    exec >upload-rhessi.log

    echo "$progname: work directory $workdir"
fi

if $doall; then
    echo "$progname: recreating table"
    if ! $dryrun; then
        { 
          echo "drop table if exists rhessi;"
          cat $scriptdir/dsa-create.mysql
        } | mysql -h$mysql_host $mysql_credentials dsa
        echo "$progname: recreated table"
    fi
    echo "$progname: Uploading all data, in $inc chunks"
elif $daylist; then
    echo "$progname: uploading data for days $@"
else
    echo "$progname: Uploading data in $startdate -- $enddate, in $inc chunks"
fi
echo -n "$progname: starting at..."
date

if $daylist; then

    if test $# = 0; then
        echo "$progname: No updates in period"
    fi
    while test $# -gt 0; do
        endperiod=`date --utc --iso-8601 -d"$1 1 day"`

        # check if an upload failed, so that we should abort this script
        if test -f upload.stop; then
            echo "Upload failed" >&3
            cat upload.stop >&3
            exit 1                              # JUMP OUT
        fi

        # The business...
        if $dryrun; then
            echo "process date $1 --- $endperiod"
        else
            process_dates $1 $endperiod
        fi
        shift
    done

else

    # the +%s time at which we should stop the loop
    endseconds=`date --utc -d"$enddate" +%s`

    # We'll go through the following loop only once in the $doall case
    while test `date --utc -d$startdate +%s` -lt $endseconds
    do
        # check if an upload failed, so that we should abort this script
        if test -f upload.stop; then
            echo "Upload failed" >&3
            cat upload.stop >&3
            exit 1                              # JUMP OUT
        fi

        if test -z "$inc"; then
            endperiod=$enddate
        else
            endperiod=`date --utc --iso-8601 -d"$startdate + $inc"`
        fi

        # The business...
        if $dryrun; then
            echo "process dates $startdate --- $endperiod"
        else
            process_dates $startdate $endperiod
        fi

        startdate=$endperiod
    done

fi

echo -n "$progname: ...ending at "
date

#echo "$progname: Processes in PG $pgid, logged in file pslog-$pgid:"
#ps j
#kill -TERM -$pgid
#ps j

touch $stampfile

# Temporary (?) logging output, mailed to crontab owner,
# to prompt review and tidyup.  Automate this in future?
echo "$progname cron job finished at `date`" >&3
echo "Review and delete work directory `hostname`:$workdir" >&3

exit 0
