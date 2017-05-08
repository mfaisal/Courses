import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 * 
 * @author Mustafa Faisal
 *
 */
public class ArffConverter {
	static ArrayList<String> wordList = new ArrayList<String>();
	
	static ArrayList<Integer> trainingclasslabel = new ArrayList<Integer>();
	static Map<Integer, Map<String,Integer>> trainingData = new HashMap<Integer, Map<String,Integer>>();
	
	static ArrayList<Integer> testclasslabel = new ArrayList<Integer>();
	static Map<Integer, Map<String,Integer>> testData = new HashMap<Integer, Map<String,Integer>>();
	
	static String trainingDir = "train/";
	static String testDir = "test/";
	
	public static void main(String[] args){
		
		if(args.length>0){
			 trainingDir = args[0].trim();
			 testDir = args[1].trim();
		}
		File dataFile = new File(trainingDir);
		gatherData(dataFile);
		
		
		wordList.remove("class");
		wordList.add("class");
		wordList.remove("");
		wordList.remove(",");
		
		
		/*try{
			FileWriter fw = new FileWriter("wordList_.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			
			
			for(String sr:wordList){
			  bw.write(sr + "\n");	
			}
			
			bw.close();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}*/
		try{
		 FileReader fr = new FileReader("wordList_.txt");
		 BufferedReader br = new BufferedReader(fr);
		 String line;
		 ArrayList<String> comp = new ArrayList<String>();
		 
		 while ((line = br.readLine()) != null) {
			 comp.add(line.trim());
		 }
		 
	     wordList = new ArrayList<String>();
	     
	     for(String sr:comp){
	    	 wordList.add(sr);
	     }
		 
		 br.close();
		 fr.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataFile = new File(testDir);
		gatherData(dataFile);
		
		//System.out.println(wordList.size());
		
		//System.out.println(trainingclasslabel.size());
		//System.out.println(trainingData.size());
		//System.out.println(testclasslabel.size());
		//System.out.println(testData.size());
		
		convertArff("train", trainingclasslabel, trainingData);
		convertArff("test", testclasslabel, testData);
	}

	private static void convertArff(String fileType, ArrayList<Integer> clss, Map<Integer, Map<String,Integer>> mainData) {
		try{
			File writeFile = new File(fileType+".csv");
			if(!writeFile.exists()){
				writeFile.createNewFile();
			}
			else{
				writeFile.delete();
			}
			
			
			FileWriter fw = new FileWriter(writeFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			String header="";
			
			for(String str:wordList){
				header += str+",";
			}
			
			
			header = header.substring(0,header.length()-1);
			bw.write(header +"\n");
			
			for(int flIndex:mainData.keySet()){
				
				String row="";
				
				Map<String,Integer> docContent = mainData.get(flIndex);
				
				int i;
				for(i=0;i<wordList.size();i++){
					
					if(i==(wordList.size()-1)){
						row = row + clss.get(flIndex-1)+"\n";
						
					}
					else
					{
						if(docContent.containsKey(wordList.get(i))){
							row = row + docContent.get(wordList.get(i))+ ",";
							
						}else{
							row = row + "0,";
							
						}
					}
					
				}
				//System.out.println(clss.get(flIndex-1));
				//row = row + clss.get(flIndex-1)+"\n";
				//System.out.println(row);
				
				bw.write(row);
			}
			
			bw.close();
			fw.close();
			
			
			
			
			 CSVLoader loader = new CSVLoader();
			 loader.setSource(writeFile);
			 Instances data = loader.getDataSet();
			   
			 String fnArrf = fileType+".arff";
			    
			 String tempFile = "temp.arff";
			    // save ARFF
			 ArffSaver saver = new ArffSaver();
			 saver.setInstances(data);
			 saver.setFile(new File(tempFile));
			 saver.setDestination(new File(tempFile));
			 saver.writeBatch();
			    
			 String line;
			 FileReader fr = new FileReader(tempFile);
			 BufferedReader br = new BufferedReader(fr);
	 		
	 		
	 		 writeFile = new File(fnArrf);
	 		if(!writeFile.exists()){
	 			writeFile.createNewFile();
	 		}
	 		else{
	 			writeFile.delete();
	 		}
	 		 fw = new FileWriter(writeFile.getAbsoluteFile());
	 		 bw = new BufferedWriter(fw);
	 		
			    while ((line = br.readLine()) != null) {
			    	if(line.equals("@attribute class numeric")){
			    		 bw.write("@attribute class {1,-1}\n");
			    	}
			    	else
			    	{
			    		bw.write(line+"\n");
			    	}
			    }
			   
			    
			    bw.close();
			    fw.close();
			    
			    br.close();
			    fr.close();
			    writeFile = new File(tempFile);
			    if(writeFile.exists()){
	 			writeFile.delete();
	 		}
		
		
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void gatherData(File dataFile){
		ArrayList<String> forbiddenStrings = new ArrayList<String>();
		forbiddenStrings.add(",");
		forbiddenStrings.add("'");
		forbiddenStrings.add("\"");
		forbiddenStrings.add("\\");
		forbiddenStrings.add("\n");
		String absPath = dataFile.getAbsolutePath();
		int fCounter = 0;
		for(String dir:dataFile.list()){
			String folderPath = absPath+"/"+dir+"/";
			File spamorhamDir = new File(folderPath);
			
			for(File file:spamorhamDir.listFiles()){
				
				BufferedReader br = null;
				FileReader fr = null;
				ArrayList<String> wList = new ArrayList<String>();
				Map<String,Integer> docContent = new HashMap<String,Integer>();
				
				try {
					fr = new FileReader(file.getAbsolutePath());
					br = new BufferedReader(fr);
					
					String line;
					while((line=br.readLine())!=null){
						String[] words = line.split(" ");
						for(String word:words){
							if(!wList.contains(word)){
								wList.add(word);
							}
							
							if(docContent.containsKey(word)){
								int db = docContent.get(word);
								docContent.put(word, db+1);
							}else{
								docContent.put(word, 1);
							}
						}
					}
					
					fCounter++;
					
					if(dataFile.getName().contains("train")){
						for(String token:docContent.keySet()){
							if(!wordList.contains(token.trim())&&!forbiddenStrings.contains(token.trim())){
								wordList.add(token);
							}
						}
						
						if(dir.equals("spam")){  // -1 for spam and 1 for ham
							trainingclasslabel.add(-1);
						}
						
						if(dir.equals("ham")){
							trainingclasslabel.add(1);
						}
						
						trainingData.put(fCounter, docContent);
						
					}
					if(dataFile.getName().contains("test")){
						if(dir.equals("spam")){  // -1 for spam and 1 for ham
							testclasslabel.add(-1);
						}
						
						if(dir.equals("ham")){
							testclasslabel.add(1);
						}
						
						testData.put(fCounter, docContent);
					}
					
					fr.close();
					br.close();
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}
