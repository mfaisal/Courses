Name:   Mustafa Faisal
Net Id: maf120030
============================


1. Part One (SVM and Perceptron):
   
   LibSVM is integrated with Weka. So, we need to convert the provided data set.

   A java class (LibSVMToCSVConverter.java) is written for converting LibSVM format to .csv file.
   
   a) Compilation 
	javac LibSVMToCSVConverter.java
   b) Run
        java LibSVMToCSVConverter <input LibSVM format file> <output .csv file>

   Example:
        java LibSVMToCSVConverter training.new training.csv
  	java LibSVMToCSVConverter validation.new validation.csv

   .csv file is converted to .arff file. And these .arff files are used for Weka to train and test for various kerlen for LibSVM.

   .csv file is used for Perceptron (modified from Homework3)

    Perceptron:
    ===========

   a) Compilation:
      java Perceptron.java 
   b) Run
      option1: java Perceptron
               default parameter values:
                i) training file: training.csv
                ii) testing file: validation.csv
                iii) iteration number: 10000
                iv) learning rate: 0.01
                v) initial weight: -3.0
      option2: java Perceptron <training file> <testing file> <iteration number> <learning rate> <initial weight>
      example:
               java Perceptron training.csv validation.csv 10000 0.01 -3.0

2. Part two (Bagging and Boosting):
   
  Three datasets are selected from UCI Machine Learning repository. 

  Dataset 1 (Chess):
  https://archive.ics.uci.edu/ml/datasets/Chess+%28King-Rook+vs.+King%29

  Dataset 2 
  https://archive.ics.uci.edu/ml/datasets/seismic-bumps

  Dataset 3
  https://archive.ics.uci.edu/ml/datasets/banknote+authentication#  

  Dataset 2 is in .arff format. So, we need not convert into .arff format. The rest two are converted into .arff file formats.

  Mainly three classifiers are used for experiments: 
  
  a) Decision Tree (J48) (unstable classifier) b) Naive Bayes c) KNN (IBK -- with default parameter)

  All .arff files and output are included.

3. Part three (K-Means Clustering):

kmeans(int[] rgb, int k) method in KMeans.java is implemented.

a) Compile:
           javac KMeans.java
b) Run:    
           java KMeans <inputImage file> <K> <outputImage file>
  Example:
           java KMeans Koala.jpg 2 k2.jpg
	   java KMeans Penguins.jpg 20 P20.jpg

   
   Experiments are done in a Linux machine with OS: Ubuntu 14.04 LTS (64-bit) Memory: 16 GB Processor: Intel Core i5 @ 3.20GHz 

We found size of files are slightly different in Windows machine.



