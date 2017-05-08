


1. Log in cs6360.utdallas.edu with user: utd password: hadoop

2. Make sure that you have the hadoop-core-1.0.4.jar file in /users/utd.
   


Compile the .java file:

Step 1: javac -classpath <hadoop-core-1.0.4.jar file path>  <java file path>

Step 2: jar cvf <jar file> -C <manifest file directory> <class file directory>






3. Create a Folder with your name(e.g. Joe). 




HW1: Visitor count in White house

1. Create a folder HW1 inside folder Joe(folder created earlier).

2. Inside HW1 folder copy WhiteHouse.java

3. Run the following commands:

          javac -classpath ../../hadoop-core-1.0.4.jar  WhiteHouse.java

          jar cvf WhiteHouse.jar -C . .


4. You will see a jar file named WhiteHouse.jar is created in HW1 folder

5. Make sure that input files are inserted proper directory.

  hadoop fs -ls /home/kma041000/HW1/input

6. Remove the tmp and output directories if they were created before.
  
   hadoop fs -rmr /home/Joe/output
   hadoop fs -rmr /home/Joe/tmp


7. Run map reduce program

   hadoop jar WhiteHouse.jar WhiteHouse /home/kma041000/HW1/input /home/Joe/tmp /home/Joe/output
   

 
8. You will see the part-r-00000 file in /home/Joe/output directory. The following command will give you the output.
   
   hadoop fs -cat /home/Joe/output/part-r-00000



