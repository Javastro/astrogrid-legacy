#!/bin/csh
# datacubeFile = $1
# arrayName = $2
if (-e $1) then
  idl_6.0 << EOD
  .run MakeMPEG.pro
  restore, '$1'
  if n_elements($2) ne 0 then MakeMPEG, $2, '$3' else print, "Array name does not exist in restored datacube." 
  exit
else 
  echo, "Datacube restore file does not exist."
  exit
endif
EOD
