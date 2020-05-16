import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.lang.*;

public class Manager extends Process_Struct{

	public static void main(String args[])
	{	
		
		
	}
	public static void getvalue(AtomicInteger bool,Map<Integer,ArrayList<Integer>> RL, ArrayList<String> s) {
		if(bool.get() == 0)
		{
			s.add(Integer.toString(-1));
			s.add(" ");
			//s.add("\n");

		}
		else
		{
			s.add(Integer.toString(scheduler(RL).get(1)));
			s.add(" ");
		}

		
	}
	public static void write(ArrayList<String> s)
	  {
		  try {
				File file = new File("output");
				if (!file.exists())
				{
					file.createNewFile();
				}
				//PrintWriter pw = new PrintWriter(new FileWriter(file,true));
				PrintWriter pw = new PrintWriter(file);
				for(int i = 0;i<s.size();i++)
				{
					pw.print(s.get(i));
				}
				pw.close();
			}catch(IOException e) {
				System.out.println("create wrong");
			}

	  }
	public static void init(ArrayList PCB,ArrayList RCB, Map<Integer,ArrayList<Integer>> RL)//List PCB)
	{
		
		//process init;
		Process process = new Process();
		process.pid = 0;
		process.state = 1;
		process.parent = -1;
		process.children = new ArrayList<Integer>();
		process.resource = new HashMap<>();
		process.priority = 0;
		RL.get(0).add(0);
		PCB.add(process);
		
		//resource init
		Resource resource0 = new Resource();
		resource0.state = 1;
		resource0.inventory = 1;
		resource0.waitlist = new HashMap<>();
		RCB.add(resource0);
		
		Resource resource1 = new Resource();
		resource1.state = 1;
		resource1.inventory = 1;
		resource1.waitlist = new HashMap<>();
		RCB.add(resource1);
		
		Resource resource2 = new Resource();
		resource2.state = 2;
		resource2.inventory = 2;
		resource2.waitlist = new HashMap<>();
		RCB.add(resource2);
		
		Resource resource3 = new Resource();
		resource3.state = 3;
		resource3.inventory = 3;
		resource3.waitlist = new HashMap<>();
		RCB.add(resource3);
		
		
	}
	public static void create(int p, ArrayList PCB, Map<Integer,ArrayList<Integer>> RL, AtomicInteger bool)
	{
		Process process = new Process();
		ArrayList l_p = scheduler(RL);
		int parent = (Integer)l_p.get(1);
	
		process.state = 1;
		process.parent = parent;
		process.priority = p;
		process.children = new ArrayList<Integer>();
		process.resource = new HashMap<> ();
		
		int PCB_length = PCB.size();
		int add_bool = 0;
		for(int i = 0; i<PCB_length;i++)
		{
			if(((Process)PCB.get(i)).state == -1)
			{
				PCB.remove(i);
				PCB.add(i,process);
				process.pid = i;
				((Process)PCB.get(parent)).children.add(i);
				RL.get(p).add(i);
				add_bool = 1;
				break;
			}
		}
		if(add_bool == 0)
		{
			if(PCB_length < 16)
			{
				process.pid = PCB_length;
				((Process)PCB.get(parent)).children.add(PCB_length);
				PCB.add(PCB_length,process);
				RL.get(p).add(PCB_length);
			}
			else
			{
				bool.set(0);
				//System.out.println("create error");
			}
			
		}
		ArrayList<Integer> l = scheduler(RL);
		if(l.get(0) < p)
		{
			timeout(RL);
		}
	}
	public static void destroy(int pid, ArrayList PCB,ArrayList RCB, Map<Integer,ArrayList<Integer>> RL,AtomicInteger bool )
	{
		int num = destroy_num(pid, PCB, RCB,RL,bool);
		if(num == 0)
		{
			bool.set(0);
			//System.out.println("nothing destroyed");
		}
		else
		{
			//System.out.println(num + " processes destroyed");
		}
	}
	public static int destroy_num(int pid, ArrayList PCB,ArrayList RCB, Map<Integer,ArrayList<Integer>> RL,AtomicInteger bool)
	{

		int destroyTime = 0;
		//System.out.println("pid :"+pid);
		//System.out.println("PCB: "+ PCB);
		if(pid>=PCB.size())
		{
			bool.set(0);
			//System.out.println("destroy error");
			return 0;
		}
		int parent_pid = ((Process)PCB.get(pid)).parent;
		if(pid == 0)
		{
			bool.set(0);
			//System.out.println("destroy error");
			return 0;
		}
		if(((Process)PCB.get(parent_pid)).state != 1)
		{
			bool.set(0);
			//System.out.println("destroy error");
			return 0;
		}
		if(( (Process)PCB.get(pid)).children.isEmpty())
		{
			//System.out.println("here:" + pid);
			((Process)PCB.get(parent_pid)).children.remove(new Integer(pid));
			((Process)PCB.get(pid)).state = -1;
			RL.get(((Process)PCB.get(pid)).priority).remove(new Integer(pid));
			Map<Integer,Integer> l = ((Process)PCB.get(pid)).resource;
			int len_r = l.size();
			if(len_r!=0)
				for(int a = 0;a<len_r;a++)
				{
					int re = (int)l.keySet().toArray()[a];
					int re_num = l.get(re);
					AtomicInteger trivial = new AtomicInteger(1);
					release(re,re_num,PCB,RCB,RL,trivial,0);
				}
			return 1;
		}
		else
		{
			//System.out.println("here else:" + pid);
			int len = ((Process)PCB.get(pid)).children.size();
			int i =0;
			while(i<len)
			{
				int pid_rec = ((Process)PCB.get(pid)).children.get(0);
				((Process)PCB.get(parent_pid)).children.remove(new Integer(pid_rec));
				((Process)PCB.get(pid_rec)).state = -1;
				RL.get(((Process)PCB.get(pid_rec)).priority).remove(new Integer(pid));
				RL.get(((Process)PCB.get(pid_rec)).priority).remove(new Integer(pid_rec));
				Map<Integer,Integer> l_rec = ((Process)PCB.get(pid_rec)).resource;
				int len_recr = l_rec.size();
				if(len_recr!=0)
					for(int b = 0;b<len_recr;b++)
					{
						int re = (int)l_rec.keySet().toArray()[b];
						int re_num = l_rec.get(re);
						AtomicInteger trivial1 = new AtomicInteger(1);
						release(re,re_num,PCB,RCB,RL,trivial1,0);
					}
				destroyTime += destroy_num(pid_rec,PCB,RCB,RL,bool);
				i++;
			}
		}
		destroyTime += 1;
		return destroyTime;
	}
	
	public static ArrayList<Integer> scheduler(Map<Integer,ArrayList<Integer>> RL)
	{
		ArrayList<Integer> L = new ArrayList<Integer>();
		for(int i = 2;i>=0;i--) {
			if(RL.get(i).size()!=0) {
				L.add(i);
				L.add(RL.get(i).get(0));
				//System.out.println("process "+ RL.get(i).get(0) +" is running");
				break;
			}
		}
		return L;
	}
	public static void timeout(Map<Integer,ArrayList<Integer>> RL)//, int pid)
	{
		System.out.println(""+RL);
		for(int i =2;i>=0;i--)
		{
			if(RL.get(i).size()!=0)
			{
				int s = RL.get(i).get(0);
				RL.get(i).remove(0);
				RL.get(i).add(s);
				return;
			}
		}
	}
	
	public static void request(int r_index, int request_num,ArrayList PCB,ArrayList RCB,Map<Integer,ArrayList<Integer>> RL,AtomicInteger bool )
	{	
		if(r_index<0 || r_index > 3)
		{
			
			bool.set(0);
			return;
		}
		
		Resource r = ((Resource)RCB.get(r_index));
		if(r.inventory < request_num)
		{
			bool.set(0);
			return;
		}
		
		ArrayList l_p = scheduler(RL);
		int run_index = (Integer)l_p.get(1);
		Process rp = (Process)PCB.get(run_index);
		//System.out.println("requst id: "+rp.pid);
		//System.out.println(""+rp.resource);
		//System.out.println("request r_index: "+r_index + " number: "+request_num);
		Map<Integer,Integer> re_rp = rp.resource;
		/*if(re_rp.containsKey(r_index))
		{
			System.out.println("here111");
			bool.set(0);
			return;
		}*/
		if(rp.pid == 0)
		{
			//System.out.println("here");
			bool.set(0);
			return;
		}
		//System.out.println("r state: "+r.state);
		if(r.state>=request_num)
		{
			((Resource)RCB.get(r_index)).state -= request_num;
			if(((Process)PCB.get(run_index)).resource.containsKey(r_index))
			{
				int num = ((Process)PCB.get(run_index)).resource.get(r_index) + request_num;
				((Process)PCB.get(run_index)).resource.put(r_index,num);
			}
			else {
				((Process)PCB.get(run_index)).resource.put(r_index,request_num);
			}
			
		}
		else
		{
			//((Resource)RCB.get(r_index)).state = 0;
			RL.get(rp.priority).remove(new Integer(rp.pid));
			((Resource)RCB.get(r_index)).waitlist.put(rp.pid,request_num);

		}
		return;
	
	}
	public static void release(int r_index, int r_num,ArrayList PCB,ArrayList RCB, Map<Integer,ArrayList<Integer>> RL,AtomicInteger bool, int self )
	{
		
		ArrayList l_p = scheduler(RL);
		int run_index = (Integer)l_p.get(1);
		Process rp = (Process)PCB.get(run_index);
		if(rp.resource.containsKey(r_index)) {
			if(r_num > rp.resource.get(r_index))
			{
				if(self == 1)
				{
					//System.out.println("here1");
					bool.set(0);
					return;
				}
			}}
		
		if(!rp.resource.containsKey(r_index) || rp.resource.get(r_index)==null)
		{
			if(self == 1)
			{
				//System.out.println("here1");
				bool.set(0);
				return;
			}
			
		}
		if(((Resource)RCB.get(r_index)).state == ((Resource)RCB.get(r_index)).inventory || ((Resource)RCB.get(r_index)).state+1 > ((Resource)RCB.get(r_index)).inventory)
		{
			if(self == 1)
			{
				//System.out.println("here2");
				bool.set(0);
			}
			
			return;
		}
		if(((Resource)RCB.get(r_index)).waitlist.isEmpty())
			{
			((Resource)RCB.get(r_index)).state += r_num;
			}
		else
		{
			//System.out.println("should be heere");
			int len_wait = ((Resource)RCB.get(r_index)).waitlist.size();
			int count = r_num;
			for(int i = 0 ; i<len_wait; i++)
			{
				
				System.out.println("waitlist: "+((Resource)RCB.get(r_index)).waitlist);
				
				 //System.out.println("i: "+i);
				 //System.out.println("size: "+((Resource)RCB.get(r_index)).waitlist.size());
				 if(((Resource)RCB.get(r_index)).waitlist.size() == 0)
				 {
					 //System.out.println("here?");
					 break;
				 }
				 int size = (int)((Resource)RCB.get(r_index)).waitlist.size();
				 int re = (int)((Resource)RCB.get(r_index)).waitlist.keySet().toArray()[size-1];
				 
				 //Process ph = (Process)PCB.get(re);
				 //int num_ph = ph.resource.get(r_index);
				 //System.out.println("now have: "+ num_ph);
				 //System.out.println("re: "+re);
				 //RL.get(((Process)PCB.get(re)).priority).add(re);
				 int req_num = ((Resource)RCB.get(r_index)).waitlist.get(re);
				 //System.out.println("req_num "+req_num);
				 //System.out.println("r_num "+r_num);
				 //System.out.println("heere: " + RL.get(((Process)PCB.get(re)).priority));
				 /*int sb = 0;
				 if(re == 3 && !(RL.get(((Process)PCB.get(re)).priority).contains(3)))
				 {
					 RL.get(((Process)PCB.get(re)).priority).add(re);
					 sb = 1;
				 }*/
				 System.out.println("r_num: "+r_num);
				 System.out.println("" + ((Resource)RCB.get(r_index)).state);
				 int i_want = r_num + ((Resource)RCB.get(r_index)).state;
				 System.out.println("req+r" + i_want);
				 //if(req_num+r_num >= ((Resource)RCB.get(r_index)).waitlist.get(re))
				 if(req_num<=count)// && req_num+r_num >= ((Resource)RCB.get(r_index)).waitlist.get(re))
				 {
					 //System.out.println("should be heere1");
					int j = (int)((Resource)RCB.get(r_index)).waitlist.keySet().toArray()[0];
					Process p_j = (Process)PCB.get(j);
					int j_pid = p_j.pid;
					int j_prio = p_j.priority;
					//System.out.println(""+RL);
					//System.out.println("j_pid: "+j_pid);
					//if(j_pid!=3)
					//{
					RL.get(j_prio).add(j_pid);
					//}
					//System.out.println(""+RL);
					((Resource)RCB.get(r_index)).waitlist.remove(new Integer(j_pid));
					((Process)PCB.get(j)).state = 1;
					((Process)PCB.get(j)).resource.put(r_index,r_num);
					
					ArrayList<Integer> l = scheduler(RL);
					if(l.get(0) > j_prio)
					{
						timeout(RL);
					}
					count -= req_num;
					
					
				 }
			}
		}
		return;
	}
}
