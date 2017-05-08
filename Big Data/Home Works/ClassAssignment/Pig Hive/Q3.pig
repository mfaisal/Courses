A = load '/HW3/join/input/NASA_HTTP.txt' using PigStorage('\t') as (IP,VALUE);
B = load '/HW3/join/input/HOST_COUNTRY.txt' using PigStorage('\t') as (IP, COUNTRY_ABBREVIATION);
C= load '/HW3/join/input/COUNTRY_NAME.txt' using PigStorage('\t') as (COUNTRY_ABBREVIATION, COUNTRY_NAME);

F = cogroup A by IP, B by IP;

store F into '/home/mxs121731/pig/output/q3'; 

