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

VOEXPLORER='~/voexplorer ; echo ; echo "Test done" ; read OK'
COMMAND1='~/python/dave/copymove.py --user dave --outer 200 --wait  5 --debug --exit ; echo ; echo "Test done" ; read OK'
COMMAND2='~/python/dave/copymove.py --user dave --outer 200 --wait 10 --debug --exit ; echo ; echo "Test done" ; read OK'
COMMAND3='~/python/dave/copymove.py --user dave --outer 200 --wait 15 --debug --exit ; echo ; echo "Test done" ; read OK'
COMMAND4='~/python/dave/copymove.py --user dave --outer 200 --wait 20 --debug --exit ; echo ; echo "Test done" ; read OK'

gnome-terminal --geometry=80x20+0+50    --tab -e "ssh agtester-000@alpha $VOEXPLORER" --tab -e "ssh agtester-000@alpha $COMMAND1"
gnome-terminal --geometry=80x20+200+150 --tab -e "ssh agtester-001@alpha $VOEXPLORER" --tab -e "ssh agtester-001@alpha $COMMAND2"
gnome-terminal --geometry=80x20+400+250 --tab -e "ssh agtester-002@alpha $VOEXPLORER" --tab -e "ssh agtester-002@alpha $COMMAND3"
gnome-terminal --geometry=80x20+600+350 --tab -e "ssh agtester-003@alpha $VOEXPLORER" --tab -e "ssh agtester-003@alpha $COMMAND4"

#
# Useful to tidy up after a failed test.
# gnome-terminal -e "ssh root@alpha"
# for pid in `ps -e | grep java | awk '{ print $1 }'`; do echo $pid ; kill $pid; done



~/python/dave/copymove.py --user dave --outer 200 --debug --exit
