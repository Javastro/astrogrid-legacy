#!/bin/csh
if (-e $1) then

  idl_6.0 << EOD
  .run MakeMPEG.pro
  .run fitsToDataCube.pro
  cube = fitsToDataCube('$1')
  MakeMPEG, cube, '$2'
  if n_elements(cube) ne 0 then MakeMPEG, cube, '$2' else print, "There was an error converting the fits images to a datacube."
  exit
else 
  echo, "Filename containing fits filenames does not exist."
  exit
endif
EOD
