A = load '/HW3/join/input/NASA_HTTP.txt' using PigStorage('\t') as (IP,VALUE);
B = load '/HW3/join/input/HOST_COUNTRY.txt' using PigStorage('\t') as (IP, COUNTRY_ABBREVIATION);
C= load '/HW3/join/input/COUNTRY_NAME.txt' using PigStorage('\t') as (COUNTRY_ABBREVIATION, COUNTRY_NAME);

D = join A by IP,  B by IP;
E = join D by COUNTRY_ABBREVIATION, C by COUNTRY_ABBREVIATION;

dump E; 
store E into '/home/mxs121731/pig/output/q2'; 



