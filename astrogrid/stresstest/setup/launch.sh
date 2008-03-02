#!/bin/sh
# <meta:header>
#     <meta:title>
#         Stress test launch script.
#     </meta:title>
#     <meta:description>
#         Stress test launch script.
#     </meta:description>
#     <meta:licence>
#         Copyright (C) AstroGrid. All rights reserved.
#         This software is published under the terms of the AstroGrid Software License version 1.2.
#         See [http://software.astrogrid.org/license.html] for details. 
#     </meta:licence>
#     <svn:header>
#         $LastChangedRevision: 286 $
#         $LastChangedDate: 2007-09-06 12:49:40 +0100 (Thu, 06 Sep 2007) $
#         $LastChangedBy: dmorris $
#     </svn:header>
# </meta:header>
#

#HOSTS="alpha astro-00 astro-01 astro-02 astro-03 astro-04 astro-05 astro-06 astro-07"
HOSTS="astro-07"
DOMAINNAME="flexiscale.metagrid.co.uk"
#TEST_ACCOUNTS="agtester-000 agtester-001 agtester-002 agtester-003"
TEST_ACCOUNTS="agtester-000 agtester-001"
# interval between starting processes in seconds
TIME_INTERVAL=10
START_TIME=0

VOEXPLORER='~/voexplorer'
COMMAND1='~/python/dave/readwrite.py --user dave --outer 100 --inner 100 --wait  5 --debug --exit ; echo ; echo "Test done" ; read OK'
COMMAND2='~/python/dave/readwrite.py --user dave --outer 100 --inner 100 --wait 10 --debug --exit ; echo ; echo "Test done" ; read OK'
COMMAND3='~/python/dave/readwrite.py --user dave --outer 100 --inner 100 --wait 15 --debug --exit ; echo ; echo "Test done" ; read OK'
COMMAND4='~/python/dave/readwrite.py --user dave --outer 100 --inner 100 --wait 20 --debug --exit ; echo ; echo "Test done" ; read OK'

COMMAND='~/python/dave/copymove.py --user dave --outer 200 --debug --exit ; echo ; echo "Test done" ; read OK'

#gnome-terminal --geometry=80x20+000+050 --tab -e "ssh agtester-000@astro-01.flexiscale.metagrid.co.uk $VOEXPLORER" --tab -e "ssh agtester-000@astro-01.flexiscale.metagrid.co.uk $COMMAND1"
#gnome-terminal --geometry=80x20+200+150 --tab -e "ssh agtester-001@astro-01.flexiscale.metagrid.co.uk $VOEXPLORER" --tab -e "ssh agtester-001@astro-01.flexiscale.metagrid.co.uk $COMMAND2"
#gnome-terminal --geometry=80x20+400+250 --tab -e "ssh agtester-002@astro-01.flexiscale.metagrid.co.uk $VOEXPLORER" --tab -e "ssh agtester-002@astro-01.flexiscale.metagrid.co.uk $COMMAND3"
#gnome-terminal --geometry=80x20+600+350 --tab -e "ssh agtester-003@astro-01.flexiscale.metagrid.co.uk $VOEXPLORER" --tab -e "ssh agtester-003@astro-01.flexiscale.metagrid.co.uk $COMMAND4"

for HOST in ${HOSTS}
do
   for TESTER in ${TEST_ACCOUNTS}
   do
      echo ${TESTER}@${HOST}.${DOMAINNAME} - ${START_TIME}
      gnome-terminal --geometry=80x20+000+050 --tab -e "ssh ${TESTER}@${HOST}.${DOMAINNAME} $VOEXPLORER" --tab -e "ssh ${TESTER}@${HOST}.${DOMAINNAME} $COMMAND1"

      START_TIME=$[START_TIME + ${TIME_INTERVAL}]
      sleep ${TIME_INTERVAL}
   done
done


#
# Useful to tidy up after a failed test.
# gnome-terminal -e "ssh root@astro-01.flexiscale.metagrid.co.uk"
# for pid in `ps -e | grep java | awk '{ print $1 }'`; do echo $pid ; kill $pid; done

#
# Simpler alternative.
# Just starts the terminals.
# Useful to drive for debugging and repeating tests (the terminal stays active).
gnome-terminal --geometry=80x20+000+050 --tab -e "ssh agtester-000@alpha" --tab -e "ssh agtester-000@alpha"
gnome-terminal --geometry=80x20+200+150 --tab -e "ssh agtester-001@alpha" --tab -e "ssh agtester-001@alpha"
gnome-terminal --geometry=80x20+400+250 --tab -e "ssh agtester-002@alpha" --tab -e "ssh agtester-002@alpha"
gnome-terminal --geometry=80x20+600+350 --tab -e "ssh agtester-003@alpha" --tab -e "ssh agtester-003@alpha"


~/voexplorer
~/python/dave/copymove.py --user dave --outer 20 --debug --tidy
~/python/dave/readwrite.py --user dave --outer 100 --inner 100 --debug --tidy
