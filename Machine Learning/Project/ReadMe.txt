Name:   Mustafa Faisal
Net Id: maf120030
----------------------
Content:

ReadMe.txt:
------------
This file containing the content of the .zip file. And run the experiments.


Data (folder): 
-------
.csv file for continuous signals for particular PLC, we have used in this project. The first column provide timestamp, and successive columns provide time series for register in PLC.

Faisal_maf120030.pdf
--------------------
Report for this project.

Code (folder)
--------
1. ACFPACF.m: Calculates ACF and PACF for targeted signal and its diff.

2. Analysis_tau_delta.m: Help to analysis and detect TAU and Delta 

3. main1.m: Generate figure 6 in report (for tau=100 and delata=40). 

4. main2.m: Genarate figure 7 in report (tade off between memoryless and CUSUM)
5. correlationCoef.m: Generate correlated signal and plot two most correlated signals (S25 and S24)
6. csum.m: return the number of alarms or hits to the Tau.
7. residuals.m: generate absolute value of residules for particular signal
8. single_res.m: gain plot for memoryless + Single signal (with attack)
9. single_csum.m: gain plot for CUSUM + single signal (with attack)
10. corr_res.m: gain plot for memoryless + correlated signal (with attack)
11. corr_csum.m: gain plot for CUSUM + correlated signal (with attack)
12. gain_single_res.m: genarate expected times and gain for single signal + memoryless
13. gain_single_csum.m: genarate expected times and gain for single signal + CUSUM
14. gain_corr_res.m: genarate expected times and gain for correlated signals + memoryless
15. gain_corr_res.m: genarate expected times and gain for correlated signals + CUSUM

16. Data file
 
To run experimetns:
--------------------
Please place data file 172.16.11.229-cont-135.csv in the code directory. And change the current to the code directory.
We use the latest version of MatLAB R2014b (released this octobor)
However, we tested the code in CS1 machine where the version of MatLAB is R2014a.


