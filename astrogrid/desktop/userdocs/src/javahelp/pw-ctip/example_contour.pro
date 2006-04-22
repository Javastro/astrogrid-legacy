pro example_contour

header = ''
time_header = ''
data_3d = fltarr(20,15,91)
data_2d = fltarr(20,91)
latitude = (findgen(91) - 45) * 2
longitude = findgen(20) * 18


openr,106,'mmm.txt'

  readf,106,header
  readf,106,time_header
  readf,106,data_3d

close,106

for ilon = 0 , 19 do begin
 data_2d(ilon,*) = data_3d(ilon,12,*)
endfor
zmin = min(data_2d)
zmax = max(data_2d)
diff=zmax-zmin
zmin=zmin - diff/10
zmax=zmax + diff/10

loadct,39
device, decomposed=0

contour,data_2d,longitude,latitude,/fill, $
levels=(findgen(60) * (zmax - zmin)/60) + zmin, $
xrange=[0,360],/xstyle,xticks=6, $
yrange=[-90,90],/ystyle,yticks=6, $
ytitle='Latitude / Degrees', xcharsize = 1.1, $
xtitle='Longitude / Degrees', ycharsize = 1.1, $
title='Example Contour Plot', charsize = 1.2


end


