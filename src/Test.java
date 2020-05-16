import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Test {
	public static void main(String args[]) {
	
		Map<Integer,ArrayList<Integer>> RL = new HashMap<>();
		ArrayList<Integer> L = new ArrayList<Integer>();
		//L.add(1);
		RL.put(0,L);
		ArrayList<Integer> L1 = new ArrayList<Integer>();
		RL.put(1,L1);
		ArrayList<Integer> L2 = new ArrayList<Integer>();
		RL.put(2,L2);
		add(RL,0,1);
		//add(RL);
		System.out.println(RL.get(1));
	}
		public static void add(Map<Integer,ArrayList<Integer>> RL, int key, int value) {
		    //List<Item> itemsList = items.get(mapKey);
		    ArrayList<Integer> L = RL.get(key);
		    
		    // if list does not exist create it
		    if(L == null) {
		         //itemsList = new ArrayList<Item>();
		    	ArrayList<Integer> L1 = new ArrayList<Integer>();
		    	L1.add(value);
		    	RL.put(key,L1);
		    } 
		    else 
		    {
		        // add if item is not already in list
		        if(!L.contains(value)) 
		        {
		        	L.add(value);
		        	//RL.put(key,L);
		        }
		    }
		}
		

}

