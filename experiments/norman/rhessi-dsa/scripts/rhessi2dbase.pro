; NAME:
;       rhessi2dbase
;
; PURPOSE:
;        To retrieve observational summary and quick look data and save
;        in text format
;
; CALLING SEQUENCE:
;       rhessi2dbase, time_interval
;
; INPUTS:
;       time_interval - 2 element array, [start_time, end_time]
;       outfn         - string, a filename to receive the output
;
; OUTPUTS:
;        text file where colomns are:
;        1) start time of 4 second time interval (time in ISO 8601 format)
;        2) Corrected count rate in 3-6 keV range (integer)
;        3)                        6-12 keV
;        4)                        12-25 keV
;        5)                        25-50 keV
;        6)                        50-100 keV
;        7)                        100-300 keV
;        8)                        300-800 keV
;        9)                        800-7000 keV
;        10)                       7000-20000 keV
;        11) URL to (not corrected) count rate picture (string)
;        12) URL to corrected count rate picture       (string)
;        13) Attenuator state (0= open,1=thin,3=thin+thick)
;        14) Eclipse flag (1=night)
;        15) Southern Atlantic Anomaly flag
;        16) Particle flag
;        17) Data gap flag
;        18) Flare flag
;        19) Front decimation weight
;        20) Rear decimation weight
;        21) Front segment decimation energy
;        22) Front segment decimation chanel/128
;
;
; KEYWORDS:
;
; EXAMPLE:
;       IDL> rhessi2dbase, time_interval = ['20-Jul-2002 01:31:00','25-Jul-2002 00:00:00']
;
;
; HISTORY:
;        written, eduard@astro.gla.ac.uk, 10-July-2006
;
; VERSION:
;        @VERSION_STRING@
;-

pro rhessi2dbase, time_interval=time_interval, outfn=outfn
;time_interval=['20-Jul-2002 01:31:00','25-Jul-2002 00:00:00']

; First, set up an error handler which reports any error to a file
; rhesse2dbase.err and then bombs out.  We exit with an error status,
; but in fact this is pointless, because stupid sswidl doesn't pass
; on the exit status.
catch, error_status
if error_status ne 0 then begin
    get_lun, errmsg
    openw, errmsg, 'rhessi2dbase.err'
    printf, errmsg, 'error message: ', !error_state.msg
    printf, errmsg, 'error code   : ', error_status
    printf, errmsg, 'exiting...'
    exit, status=1 ; not passed on to env through sswidl
endif

IF (N_elements(time_interval) EQ 0) THEN message, 'ERROR: time interval should be supplied'

IF (anytim(time_interval(0)) LT anytim('12-Feb-2002 01:32:00')) THEN $
print,'WARNING: start time set: 12-Feb-2002 01:32:00'

o= hsi_obs_summary(obs_time_interval=time_interval)
counts =o -> getdata(/corrected)

; check that we've got some data, and fail if not
counts_ok = size(counts,/type)
if size(counts, /type) eq 8 then begin
    thesize = size(counts)
    print, 'retrieved data: ', thesize[1], ' elements'
endif else begin
    message, 'can''t find data in range'
endelse


RATE0= counts.countrate(0,*)
RATE1= counts.countrate(1,*)
RATE2= counts.countrate(2,*)
RATE3= counts.countrate(3,*)
RATE4= counts.countrate(4,*)
RATE5= counts.countrate(5,*)
RATE6= counts.countrate(6,*)
RATE7= counts.countrate(7,*)
RATE8= counts.countrate(8,*)

time=o->getaxis(/ut)
time_iso = anytim(time,/ccsds)

;retrieving flags
flags     = o -> getdata(class='flag')
;help,flags.flags
;info_flag = o -> get(/info,class='flag')
;print, info_flag.flag_ids

SAA          = flags.flags(0,*)
Eclipse      = flags.flags(1,*)
Flare        = flags.flags(2,*)
Attenuator   = flags.flags(14,*)
NON_solar    = flags.flags(16,*)
Gap          = flags.flags(17,*)
FDEnergy     = flags.flags(18,*)
RDEnergy     = flags.flags(25,*)
FDWeight     = flags.flags(19,*)
Particles    = flags.flags(24,*)
RDWeight     = flags.flags(29,*)
BAD          = flags.flags(31,*)

hsi_filedb_read, filedb,filedb_trange=time_interval
;reading file database

URL_rate      =strarr(n_elements(time))
URL_corrected =strarr(n_elements(time))

; Fill in the url_rate and url_corrected arrays with the URLs to the
; appropriate quick-look images.  blockstart[] (blockend) is the array of
; times for the start (end) of each block with the same URL.
glasgow='http://www.astro.gla.ac.uk/rhessi/metadata/'
blockstart=filedb.start_time
blockend=filedb.end_time
For i=0,N_elements(blockstart)-1 do begin
    OK_range=where((time GE blockstart(i)) AND (time LE blockend(i)) AND (time GE time(0)))
    start_t=ANYTIM(blockstart(i),/UTC_EXT)
    Year =STRING(start_t.year,FORMAT='(I04)',/PRINT)
    Month=STRING(start_t.month,FORMAT='(I02)',/PRINT)
    Day  =STRING(start_t.month,FORMAT='(I02)',/PRINT)
    date=YEAR+'/'+MONTH+'/'+DAY+'/'
    if size(ok_range,/n_dimensions) gt 0 then begin
        URL_rate(min(OK_range):max(OK_range))      =glasgow+date+'hsi_'+time2file(blockstart(i),/seconds)+'_rate.png'
        URL_corrected(min(OK_range):max(OK_range)) =glasgow+date+'hsi_'+time2file(blockstart(i),/seconds)+'_corrected_rate.png'
    endif
end

if keyword_set(outfn) then $
    filename=outfn $
else $
    filename='out_'+time2file(time_interval(0))+'.txt'

;; Following traces output -- pretty, but unnecessary
;loginterval = n_elements(time)/10
;logcounter = 0

openw,1,filename
out_format='(A19,9("	",I6),"	", A82,"	",A92,6("	",I01),4("	",I03),"	")'

for i=0L, n_elements(time)-1 do begin
    printf,1, time_iso(i), rate0(i) , rate1(i), rate2(i), rate3(i), rate4(i),$
      rate5(i), rate6(i), rate7(i), rate8(i),URL_rate(i), URL_corrected(i),$
      Attenuator(i),Eclipse(i),SAA(i),Particles(i),Gap(i),Flare(i),$
      FDWeight(i), RDWeight(i), FDEnergy(i),RDEnergy(i),$
      format=out_format

    ;logcounter = logcounter+1
    ;if logcounter eq loginterval then begin
    ;    print, time_iso(i)
    ;    logcounter = 0
    ;endif
END
close,1

end
