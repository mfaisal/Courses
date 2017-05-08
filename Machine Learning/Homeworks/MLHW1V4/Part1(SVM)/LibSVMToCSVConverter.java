import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Mustafa Amir Faisal
//Net Id: maf120030

public class LibSVMToCSVConverter {

	
	static String libSVMFile = "validation.new";
	static String csvFile = "validation.csv";	

	public static void main(String[] args){

		if(args.length>0){
			libSVMFile = args[0].trim();  // training file
			csvFile = args[1].trim(); // test file
		}
		
		try{
			BufferedReader bReader = new BufferedReader(new FileReader(libSVMFile));
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(csvFile));
			
			String line = "";
			int l_counter = 1;
			String cls="";
			String dataCont="";
			
			while((line=bReader.readLine())!=null){
				dataCont="";
				String[] fElements = line.split(" ");
				if(l_counter == 1){
					String hearder="";
					
					for(String str:fElements){
						str = str.trim();
						if(str.length()==1){
							cls = str;
						}else{
							String[] colSep = str.split(":");
							hearder = hearder  + colSep[0] + ",";
							dataCont = dataCont + colSep[1] + ",";
						}
					}
					
					hearder = hearder + "class\n";
					bWriter.write(hearder);
					dataCont = dataCont +cls+"\n";
					bWriter.write(dataCont);
				}
				else
				{
					for(String str:fElements){
						str = str.trim();
						if(str.length()==1){
							cls = str;
						}else{
							String[] colSep = str.split(":");
							
							dataCont = dataCont + colSep[1] + ",";
						}
					}
					
					
					dataCont = dataCont +cls+"\n";
					bWriter.write(dataCont);
				}
				
				
				l_counter++; //line counter
			}
			
			bReader.close();
			bWriter.close();
		}catch(IOException except){
			except.getStackTrace();
		}
	}
	
	
}
