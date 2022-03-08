package logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
/**
 * Program for generating kanji component dependency order via topological sort.
 * 
 * @author Gates, Acuna
 * @version 1.0
 */
public class Gates {
	public static void main(String[] args) {
		
		HashMap<Integer, String> kanji_table = getKanjiTable(); 
		
		BetterDiGraph graph = new BetterDiGraph(900);  // arbitrary large value
		
		// load the keys as vertices into the graph 
		// Iterating HashMap through for loop
        
		System.out.println("Original:"); 
		
		for (HashMap.Entry<Integer, String> set : kanji_table.entrySet()) {
        	
        	System.out.print(set.getValue()); 
            graph.addVertex(set.getKey());
            System.out.print(" ");
        }
		System.out.println();
		
//		System.out.println("********data-components.txt***********");
		try(BufferedReader br = new BufferedReader(new FileReader("data-components.txt"))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();

		        if(line==null) {
		        	break;
		        }
		        
		        String str_1 = line.substring(0, 3); 
		        str_1 = str_1.replaceAll("\\s+",""); 
		        
		        String str_2 = line.substring(3, line.length()); 
		        str_2 = str_2.replaceAll("\\s+","");  
		        
		        
		        Integer val_1 = Integer.valueOf(str_1);
		        Integer val_2 = Integer.valueOf(str_2);
		        
		        graph.addEdge(val_1, val_2);
		        
		    }
		    
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}
		
		// now that everything has been added, let's sort this mamma jamma 
		IntuitiveTopological sort = new IntuitiveTopological(graph); 
		
		System.out.println("Sorted:"); 
		// loop over the sorted data 
		for(int k : sort.order()) {
			
			// look up in kanji table 
			System.out.print(kanji_table.get(k));
			
			
			// print space for readability
			System.out.print(" ");
		}
		

	}
	
	private static HashMap<Integer, String> getKanjiTable() {
		
		HashMap<Integer, String> kanji_table = new HashMap<Integer, String>(); 
		
//		System.out.println("********data-kanji.txt***********");
		try(BufferedReader br = new BufferedReader(new FileReader("data-kanji.txt"))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();

		        if(line==null) {
		        	break;
		        }
		        
		        // ignore comments 
		        if(line.charAt(0)!='#') {
	
		        	// NUMBER 
			        String key_str = line.replaceAll("\\D+","");
			        
			        // KANJI
		
			        String kanjiString = line.replaceAll("\\d","");
			        kanjiString = kanjiString.replaceAll("\\s","");

			        // convert the first value into an integer
			        Integer key = Integer.valueOf(key_str);
		
			        
			        kanji_table.put(key, kanjiString);
		        }

		    }
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println("********end-file***********");
		
		return kanji_table; 
	}
}
