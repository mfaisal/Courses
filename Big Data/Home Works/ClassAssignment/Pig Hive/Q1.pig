A = load '/HW3/top10/input' using PigStorage(',') as (NAMELAST,NAMEFIRST,NAMEMID,UIN,BDGNBR,ACCESS_TYPE,TOA,POA,TOD,POD,APPT_MADE_DATE);

B = group A by (NAMELAST,NAMEFIRST);

C = foreach B generate group, COUNT(A.(NAMELAST,NAMEFIRST)) as num_of_visits;

D = order C by num_of_visits desc;

E = limit D 10;


dump E; 
store E into '/home/mxs121731/pig/output/q1'; 