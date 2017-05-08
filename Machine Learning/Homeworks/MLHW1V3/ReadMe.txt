Name:  Mustafa Faisal
NetID: maf120030
--------------------


Part 1 (Perceptron)
=========================================
Compile
--------
javac Perceptron.java

Run
--------
java Perceptron
java Perceptron <training data set folder> <test data set folder> <file location of Stop words> <# of iteration> <learning rate> <initial weight> 

Example
------------
java Perceptron train test stopWords.txt 200 0.01 0

Note ham and spam forlder are named in lowercase.

Default values:

<training data set folder> ---> "Data/train/"
<test data set folder>  ----> "Data/test/"
<file location of Stop words> --> "Data/stopWords.txt"
<# of iteration>  --> 1000
<learning rate>  ---> 0.01
<initial weight> ---> 0.1

Part 2 (Neural Network)
=========================================

For creating .arff we did as follows:

1. Create the arff file with only attributes (as some attributes are making problem ), this part is done by Weka
2. Then save the list of words in a temporary file named, "wordList_.txt".
3. Use words in this temporary file and convert the whole dataset with ArffConverter.java
 
Compile with weka.jar // you need to keep weka.jar in the current directory or give a proper class path for it.  
javac -cp  weka.jar ArffConverter.java
Run with weka.jar
java -cp .:weka.jar ArffConverter // with default training and test file locations train and test 
or 
java -cp .:weka.jar ArffConverter trian/ test/

Note: All 3 datasets are converted with this converter. However, there need some preprocessing the data which includes removing some special strings as mentioned above.

Part 3 (Collaborating Filtering)
=========================================
Compile:
--------
javac ColaborativeFilter.java 

Run:
----
Option 1: java ColaborativeFilter.java 

	For this option please keep data set folder ("netflix") in the class directory.

Option 2: javac ColaborativeFilter.java <Path of training data file> <Path of testing data file>
	
Example:
-----------
javac ColaborativeFilter.java netflix/TrainingRatings.txt netflix/TestingRatings.txt

The output is provided in PredictedRating.txt file.
