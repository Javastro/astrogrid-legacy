pro example_latitude

header = ''
time_header = ''
data_3d = fltarr(20,15,91)

openr,106,'mmm.txt'

  readf,106,header
  readf,106,time_header
  readf,106,data_3d

close,106


latitude = (findgen(91) - 45) * 2
latitude_plot = data_3d(0,12,*)
ymin=min(latitude_plot)
ymax=max(latitude_plot)
diff=ymax-ymin
ymin=ymin - diff/10
ymax=ymax + diff/10

plot,latitude,latitude_plot, $
xrange=[-90,90],/xstyle,xticks=6, $
yrange=[ymin,ymax],/ystyle, $
xtitle='Latitude / Degrees', xcharsize = 1.1, $
ytitle='Mean Molecular Mass', ycharsize = 1.1, $
title='Example Plot vs Latitude', charsize = 1.2

end
