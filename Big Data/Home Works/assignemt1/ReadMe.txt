Make sure that you have the hadoop-core-1.0.4.jar file


Compile the .java file:

Step 1: javac -classpath <hadoop-core-1.0.4.jar file path>  <java file path>

Step 2: jar cvf <jar file> -C <manifest file directory> <class file directory>






Create a Folder name Assignment. Inside the folder copy hadoop-core-1.0.4.jar (This file can be downloaded from internet)




HW1: Visitor count in White house

1. Create a folder HW1 inside Assignment.
2. Inside HW1 folder copy WhiteHouse.java
3. Run the following commands:

          javac -classpath ../hadoop-core-1.0.4.jar  WhiteHouse.java

          jar cvf WhiteHouse.jar -C . .


4. You will see a jar file named WhiteHouse.jar is created in HW1 folder

5. Make sure that input files are inserted proper directory.

  hadoop fs -ls /home/kma041000/HW1/input

6. Remove the tmp and output directories if they were created befor.
  
   hadoop fs -rmr /home/mxs121731/output
   hadoop fs -rmr /home/mxs121731/tmp


7. Run map reduce program

   hadoop jar WhiteHouse.jar WhiteHouse /home/kma041000/HW1/input /home/mxs121731/tmp /home/mxs121731/output
   

 
8. You will see the part-r-00000 file in /home/mxs121731/output directory. The following command will give you the output.
   
   hadoop fs -cat /home/mxs121731/output/part-r-00000



HW2: Visitee count in White house

1. Create a folder HW2 inside Assignment.
2. Inside HW2 folder copy WhiteHouseVisitedPeople.java
3. Run the following commands:

          javac -classpath ../hadoop-core-1.0.4.jar  WhiteHouseVisitedPeople.java

          jar cvf WhiteHouseVisitedPeople.jar -C . .


4. You will see a jar file named WhiteHouseVisitedPeople.jar is created in HW2 folder

5. Make sure that input files are inserted proper directory.

  hadoop fs -ls /home/kma041000/HW1/input

6. Remove the tmp and output directories if they were created befor.
  
   hadoop fs -rmr /home/mxs121731/output
   hadoop fs -rmr /home/mxs121731/tmp


7. Run map reduce program

   hadoop jar WhiteHouseVisitedPeople.jar WhiteHouseVisitedPeople /home/kma041000/HW1/input /home/mxs121731/tmp /home/mxs121731/output
   

 
8. You will see the part-r-00000 file in /home/mxs121731/output directory. The following command will give you the output.
   
   hadoop fs -cat /home/mxs121731/output/part-r-00000


HW3: Visitor-Visitee count in White house

1. Create a folder HW3 inside Assignment.
2. Inside HW2 folder copy WhiteHouseVisitor_Visitee.java
3. Run the following commands:

          javac -classpath ../hadoop-core-1.0.4.jar  WhiteHouseVisitor_Visitee.java

          jar cvf WhiteHouseVisitor_Visitee.jar -C . .


4. You will see a jar file named WhiteHouseVisitor_Visitee.jar is created in HW3 folder

5. Make sure that input files are inserted proper directory.

  hadoop fs -ls /home/kma041000/HW1/input

6. Remove the tmp and output directories if they were created befor.
  
   hadoop fs -rmr /home/mxs121731/output
   hadoop fs -rmr /home/mxs121731/tmp


7. Run map reduce program

   hadoop jar WhiteHouseVisitor_Visitee.jar WhiteHouseVisitor_Visitee /home/kma041000/HW1/input /home/mxs121731/tmp /home/mxs121731/output
   

 
8. You will see the part-r-00000 file in /home/mxs121731/output directory. The following command will give you the output.
   
   hadoop fs -cat /home/mxs121731/output/part-r-00000


HW4: Monthwise average Visitor count for the last four years in White house

1. Create a folder HW4 inside Assignment.
2. Inside HW2 folder copy WhiteHouseAverage.java
3. Run the following commands:

          javac -classpath ../hadoop-core-1.0.4.jar  WhiteHouseAverage.java

          jar cvf WhiteHouseAverage.jar -C . .


4. You will see a jar file named WhiteHouseAverage.jar is created in HW4 folder

5. Make sure that input files are inserted proper directory.

  hadoop fs -ls /home/kma041000/HW1/input

6. Remove the tmp and output directories if they were created befor.
  
   hadoop fs -rmr /home/mxs121731/output
   hadoop fs -rmr /home/mxs121731/tmp


7. Run map reduce program

   hadoop jar WhiteHouseAverage.jar WhiteHouseAverage /home/kma041000/HW1/input /home/mxs121731/output
   

 
8. You will see the part-r-00000 file in /home/mxs121731/output directory. The following command will give you the output.
   
   hadoop fs -cat /home/mxs121731/output/part-r-00000


