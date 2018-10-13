package Inverted_index;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class query {
	private static HashMap<String, LinkedList<Integer>> map = new HashMap<>();

	public static void read_merged_index() throws IOException{
		File file = new File("/home/a/a_isht/Desktop/COMP_6791_P1/merge_sort_index/merged_index.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st="";
		while((st = br.readLine())!=null){
			if(st.contains("TERM = ")){
				String term = st.replaceAll("TERM = ","");
				st=st.replaceAll(" ","");
				st=br.readLine();
				String list=st.replaceAll("Posting List =", "");
				if(!list.contains("END")){
					st=br.readLine();
					while(!st.contains("END")){
						list=list+st;
						st=br.readLine();
					}
					list=list+st;
				}if(list.contains("END")){
					String []array = list.split(",");
					LinkedList<Integer> posting_list = new LinkedList<>();
					for(int i=0;!array[i].contains("END");i++){
						array[i]=array[i].replaceAll(" ","");
						int doc_id=Integer.parseInt(array[i]);
						posting_list.add(doc_id);	
					}
					map.put(term, posting_list);//add the values to the map
				}
				
			}
						
		}
		
		System.out.println("THE CODE HAS READ");
		br.close();
		
		
		
	}
	
	
	private static void intersection_search(String Term1, String Term2){
		LinkedList<Integer> list_1= map.get(Term1);
		LinkedList<Integer> list_2=map.get(Term2);
		LinkedList<Integer> result= new LinkedList<>();
		if(list_1==null|| list_2==null){
			if(list_1==null){
				System.out.println("The term " + Term1+ " doesn't exit");
			}else if(list_2==null){
				System.out.println("The term " + Term2+ " doesn't exit");
			}
		
		}else{
			int i=0,j=0;
			
			while(i<list_1.size() && j<list_2.size()){
				if(list_1.get(i).equals(list_2.get(j))){
					result.add(list_1.get(i));
					i++;
					j++;

				}else if(list_1.get(i).compareTo(list_2.get(j))<0){
					i++;
				}else if(list_1.get(i).compareTo(list_2.get(j))>0){
					j++;
				}
			}
		
			int k=0;
			
			if(result.isEmpty()){
				System.out.println(" there are no common search results ");
			}
			else{
				System.out.print(Term1+" AND "+Term2+" : ");
					while(k<result.size()){
						System.out.print(result.get(k)+" , ");
						k++;
					}
			}
		}
	}
	
	private static void union_search(String Term1, String Term2){
		LinkedList<Integer> list_1= map.get(Term1);
		LinkedList<Integer> list_2=map.get(Term2);
		LinkedList<Integer> result= new LinkedList<>();
		if(list_1==null|| list_2==null){
			if(list_1==null){
				System.out.println("The term " + Term1+ " doesn't exit");
			}else if(list_2==null){
				System.out.println("The term " + Term2+ " doesn't exit");
			}
		
		}else{
			
			int i=0,j=0;
			
			
			

			while(i<list_1.size() && j<list_2.size()){
				if(list_1.get(i).equals(list_2.get(j))){
					result.add(list_1.get(i));	
					i++;	
					j++;
		
				}else if(list_1.get(i).compareTo(list_2.get(j))<0){
					result.add(list_1.get(i));
					i++;
				}else if(list_1.get(i).compareTo(list_2.get(j))>0){
					result.add(list_2.get(j));
					j++;
				}
			}
			
			while(i<list_1.size()){
				result.add(list_1.get(i));
				i++;
			}
			while(j<list_2.size()){
				result.addFirst(list_2.get(j));
				j++;
			}
			int k=0;
			
			if(result.isEmpty()){
				System.out.println(" there are no common search results ");
			}
			else{
				System.out.print(Term1+" OR "+Term2+" : ");

					while(k<result.size()){
						System.out.print(result.get(k)+" , ");
						k++;
					}
			}
		}
	}
	
	
	
	public static void main(String []Args) throws IOException{
		read_merged_index();
		intersection_search("nil","france");
		System.out.println();
		union_search("toronto","berlin");

	}

	
}
