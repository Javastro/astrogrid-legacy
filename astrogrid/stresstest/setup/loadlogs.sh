#!/bin/sh
#

AWK=/usr/bin/awk

# Loads into database - IP address, username, timestamp, actual log
# So match lines that have ACTION [(.*)] LOOP [(.*)] TIME [.*]
# and re-write them as INSERT INTO table (timestamp, IP, user, action, loop, time) VALUES ('xxxx', 'xxxx' ....)


# drop database and user ready for repopulating it
psql template1 postgres << EOF
DROP USER astrologger;
DROP DATABASE astrolog;
EOF

# after dropping, then creating new db with same name, previous data still present, needs to be fixed

# create fresh database, user, table and set privileges
psql template1 postgres << EOF
CREATE DATABASE astrolog;
CREATE USER astrologger;
CREATE TABLE logtime ( ident SERIAL, file varchar(100), date timestamp, host varchar(20), name varchar(20), time real );
GRANT SELECT, UPDATE, INSERT ON logtime TO astrologger;
GRANT SELECT, UPDATE ON logtime_ident_seq TO astrologger;
EOF



#`echo "\d logtime" |psql template1 postgres`
#`echo "INSERT INTO logtime (date, host, name, time) VALUES( '2007-06-18 04:27:42', '10.0.0.15', 'test1', '69.448219');" |psql template1 postgres`



LOG_DIR=/var/shared/logs
#LOG_DIR=/root/logtest
#LOG_DIR=/home/gary/logtest
LOGLIST=`ls -1 ${LOG_DIR}`

for LOGFILENAME in ${LOGLIST}
do
   echo ${LOGFILENAME}
   IP_ADDRESS=`echo ${LOGFILENAME} | awk -F- '{ print $1 }'`
   USERNAME=`echo ${LOGFILENAME} | awk -F- '{ print $2 "-" $3 }'`
   TIMESTAMP=`echo ${LOGFILENAME} | awk -F- '{ print $4 "-" $5}'`
   TIMESTAMP=`echo ${TIMESTAMP} | awk -F. '{ print $1}'`
   #echo ${LOG_DIR}/${LOGFILENAME}
   #echo ${IP_ADDRESS}
   #echo ${USERNAME}
   #echo ${TIMESTAMP}

   #echo "INSERT INTO table (username, ip, user, action, loop, time) VALUES (${USERNAME}, ${IP_ADDRESS}, )"
   # need to fix TIMESTAMP so format is '2007-06-18 04:27:42', not '20070618-042742'
   YEAR=`echo ${TIMESTAMP} |cut -b-4`
   MONTH=`echo ${TIMESTAMP} |cut -b5-6`
   DAY=`echo ${TIMESTAMP} |cut -b7-8`
   HOUR=`echo ${TIMESTAMP} |cut -b10-11`
   MINS=`echo ${TIMESTAMP} |cut -b12-13`
   SECS=`echo ${TIMESTAMP} |cut -b14-15`
   TIMESTAMP="${YEAR}-${MONTH}-${DAY} ${HOUR}:${MINS}:${SECS}"

    psql astrolog astrologger << EOF
    INSERT INTO logtime (file, 
                         date, 
                         host, 
                         name, 
                         time) 
    VALUES( '${LOG_DIR}/${LOGFILENAME}', 
                         '${TIMESTAMP}', 
                        '${IP_ADDRESS}', 
                          '${USERNAME}', 
                            '12.345678');


EOF
#   `echo "INSERT INTO logtime (file, date, host, name, time) VALUES( '${LOG_DIR}/${LOGFILENAME}', '${TIMESTAMP}', '${IP_ADDRESS}', '${USERNAME}', '69.448219');" |psql astrolog astrologger`


done


