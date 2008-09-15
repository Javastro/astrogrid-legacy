#!/bin/sh
outfile=$1
shift

`dirname $0`/AGDFXCmultiTableMerger "$@" > $outfile
