Name: Mustafa Faisal
Net Id: maf120030
---------------------------

For both EM for GMM with unknown and known variance, we keep the same code. Only different parameter is used to differentiate them. Instead of running EM for one convergence, we run it for 100 convergences. And take the one for which log likelihood is maximum.

Log likelihood is calculate as:

http://www.ics.uci.edu/~smyth/courses/cs274/notes/EMnotes.pdf

Compile:
--------
javac EMGMM.java

Run:
---
Default:
java EMGMM > <output.txt> (optional)

Default the program's parameter values:
==========
dataFileName = "em_data.txt";
numClusters = 3;
In this case, please keep "em_data.txt" file in the same folder.
For convenience, please write the output in a text file.
So, though generating the output file is optional, this is encouraged for comparison. 
Example:
-------
java EMGMM > outputDefault.txt

EM for GMM with unknown variance (2 parameters)
------------------------------------------------
java EMGMM <data file.txt> <number of clusters> > <output.txt> (optional)

Parameters:
<data file.txt> : data file
<number of clusters> : number of clusters that we are interested.

Example:
--------
java EMGMM em_data.txt 3 > output1.txt

EM for GMM with known variance (3 parameters)
------------------------------------------------
java EMGMM <data file.txt> <number of clusters> <variance> > <output.txt> (optional)

Parameters:
<data file.txt> : data file
<number of clusters> : number of clusters that we are interested.
<variance> : known variance

Example:
--------
java EMGMM em_data.txt 3 1.0 > output2.txt
