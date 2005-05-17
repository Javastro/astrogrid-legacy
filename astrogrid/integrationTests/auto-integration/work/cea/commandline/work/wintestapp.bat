rem A windows TestApp
rem This will fail on Unix machines, but conversely the Unix scripts will fail on Windows machines
rem so the total test passes should be the same
rem echos Hello %1 to a file
echo Hello %1 > result.txt