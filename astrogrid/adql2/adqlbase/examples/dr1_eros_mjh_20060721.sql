SELECT distinct ls.sourceid,ls.ra,ls.dec,
 rtrim(substring
   (mfy.filename,charindex("w2",mfy.filename,1),32))
   as yfilename,
  rtrim(substring
   (mfj.filename,charindex("w2",mfj.filename,-1),32))
   as j_1filename,
  rtrim(substring
   (mfh.filename,charindex("w2",mfh.filename,-1),32))
   as hfilename,
  rtrim(substring
   (mfk.filename,charindex("w2",mfk.filename,-1),32))
    as kfilename,
  lml.yenum as yextnum,
  lml.j_1enum as j_1extnum,
  lml.henum as hextnum,
  lml.kenum as kextnum
FROM
/* get the neighbour count and nearest neigbbour using subquery */

 ( SELECT sourceID,T2.*
    FROM  ukidssdr1plus..lasSource
    LEFT JOIN (
       SELECT masterObjID,count(*) AS numNeighbs, MIN(distanceMins) AS minSep
       FROM   ukidssdr1plus..lasSourceNeighbours
       GROUP BY masterObjID
    ) AS T2 ON sourceID=T2.masterObjID

  ) AS T1 LEFT JOIN lasSourceNeighbours as X 
  ON T1.sourceID=X.masterObjID,
  ukidssdr1plus..LasSource as ls,
  ukidssdr1plus..Lasdetection as ldy,
  ukidssdr1plus..Lasdetection as ldj,
  ukidssdr1plus..Lasdetection as ldh,
  ukidssdr1plus..Lasdetection as ldk,
  ukidssdr1plus..Lasmergelog as lml,
  ukidssdr1plus..Multiframe as mfy,
  ukidssdr1plus..Multiframe as mfj,
  ukidssdr1plus..Multiframe as mfh,
  ukidssdr1plus..Multiframe as mfk
WHERE
  (T1.numneighbs is null or 
   T1.minSep >= 4.0/60.0) and
  T1.sourceid=ls.sourceid and
  ldy.objid=ls.yobjid and
  ldj.objid=ls.j_1objid and
  ldh.objid=ls.hobjid and
  ldk.objid=ls.kobjid and
  ls.framesetid=lml.framesetid and
  mfy.multiframeID=lml.ymfid and
  mfj.multiframeID=lml.j_1mfid and
  mfh.multiframeID=lml.hmfid and
  mfk.multiframeID=lml.kmfid and
  ((ls.j_1apermag3-ls.kapermag3) >= 2.5 or ls.j_1class < -500) and
  (ls.yclass < -500 or
  (ls.yclass > -500 and ls.j_1class > -500 and 
   ls.yapermag3 > ls.j_1apermag3)) and
  ls.kapermag3 >= 12.5 and
  ls.kapermag3 <= 17.0 and
  ls.kapermag3err <= 0.1 and
  ldk.x > 64 and
  ldk.y > 64 and
  ldk.x < 4060 and
  ldk.y < 4060 and
  ls.kclass = -1 and
  ls.hclass > -500 and
  (ls.priorsec = 0 or ls.priorsec = ls.framesetid)
  lml.yenum > 0 and lml.j_1enum > 0 and 
  lml.henum > 0 and lml.kenum > 0
order by ls.sourceid
