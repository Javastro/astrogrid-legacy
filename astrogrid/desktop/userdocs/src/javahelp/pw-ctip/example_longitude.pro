pro example_longitude

header = ''
time_header = ''
data_3d = fltarr(20,15,91)

openr,106,'mmm.txt'

  readf,106,header
  readf,106,time_header
  readf,106,data_3d

close,106


longitude = findgen(20) * 18
longitude_plot = data_3d(*,12,46)
ymin=min(longitude_plot)
ymax=max(longitude_plot)
diff=ymax-ymin
ymin=ymin - diff/10
ymax=ymax + diff/10

plot,longitude,longitude_plot, $
xrange=[0,360],/xstyle,xticks=6, $
yrange=[ymin,ymax],/ystyle, $
xtitle='Longitude / Degrees', xcharsize = 1.1, $
ytitle='Mean Molecular Mass', ycharsize = 1.1, $
title='Example Plot vs Longitude', charsize = 1.2

end
