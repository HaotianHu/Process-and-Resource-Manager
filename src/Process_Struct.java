import java.util.*;
import java.util.HashMap;
import java.util.Map;
public class Process_Struct {

}
class Process{
	
	public int pid;
	public int state;
	public int parent;
	public int priority;
	public ArrayList<Integer> children; //= new ArrayList<int>();
	public Map<Integer,Integer> resource; //= new ArrayList<int>();
	
}
class Resource{
	public int state;
	public int inventory;
	public Map<Integer,Integer> waitlist;
}