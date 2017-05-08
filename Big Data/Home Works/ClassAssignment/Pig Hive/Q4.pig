A = load '/HW3/join/input/NASA_HTTP.txt' using PigStorage('\t') as (IP,VALUE);
B = load '/HW3/join/input/HOST_COUNTRY.txt' using PigStorage('\t') as (IP, COUNTRY_ABBREVIATION);
C= load '/HW3/join/input/COUNTRY_NAME.txt' using PigStorage('\t') as (COUNTRY_ABBREVIATION, COUNTRY_NAME);

F = cogroup A by IP, B by IP;
G = FOREACH F GENERATE flatten(A), flatten(B);

I = cogroup G by COUNTRY_ABBREVIATION, C by COUNTRY_ABBREVIATION;
J = foreach I generate flatten(G), flatten(C);
store J into '/home/mxs121731/pig/output/q4'; 

