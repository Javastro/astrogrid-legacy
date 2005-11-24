pro example_height_profile

header = '                                                                                       '
time_header = '                                                   '
data_3d = fltarr(20,15,91)
height_3d = fltarr(20,15,91)

openr,106,'mmm.txt'

  readf,106,header
  readf,106,time_header
  readf,106,data_3d

close,106

openr,107,'ht.txt'

  readf,107,header
  readf,107,time_header
  readf,107,height_3d

close,107


Height_1d = height_3d(0,*,46)
height_plot = data_3d(0,*,46)
ymin=min(height_1d)
ymax=max(height_1d)
diff=ymax-ymin
ymin=ymin - diff/10
ymax=ymax + diff/10
xmin=min(height_plot)
xmax=max(height_plot)
diff=xmax-xmin
xmin=xmin - diff/10
xmax=xmax + diff/10

plot,height_plot,height_1d, $
xrange=[xmin,xmax],/xstyle, $
yrange=[ymin,ymax],/ystyle, $
ytitle='Height / Km', xcharsize = 1.1, $
xtitle='Mean Molecular Mass', ycharsize = 1.1, $
title='Example Height Profile', charsize = 1.2


end


