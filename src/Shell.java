import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Shell extends Manager{
	public static void main(String args[]) {
		/*
		if(args.length == 0)
		{
			System.out.println("no path inputted");
			return;
		}*/
		String path = new String("/Users/alexhu/eclipse-workspace/CS143B_PROJ1/src/input.txt");
		
		int process_num = 16;
		int resource_num = 4;
		ArrayList<Process> PCB = new ArrayList<Process>(process_num);
		ArrayList<Resource> RCB = new ArrayList<Resource>(resource_num);
		Map<Integer,ArrayList<Integer>> RL = new HashMap<>();
		ArrayList<Integer> L = new ArrayList<Integer>();
		RL.put(0,L);
		ArrayList<Integer> L1 = new ArrayList<Integer>();
		RL.put(1,L1);
		ArrayList<Integer> L2 = new ArrayList<Integer>();
		RL.put(2,L2);
		ArrayList<String> s = new ArrayList<String>();
		
		AtomicInteger bool = new AtomicInteger(1);

		
		ArrayList<String> rs = new ArrayList<String>();
		File file = new File(path);
		try {
			readlines(file,rs);
		}catch(IOException e) {
			e.printStackTrace();
		}
		int len = rs.size();
		
		int bool_init = 0;
		for(int i = 0;i<len;i++)
		{
			String[] splited = rs.get(i).split("\\s+");
			if(splited.length == 1)
			{
				//System.out.println("here");
				if(splited[0].equals("in"))
				{
					if(bool_init == 1)
					{
						s.add("\n");
					}
					//System.out.println("here");
					PCB = new ArrayList<Process>(process_num);
					RCB = new ArrayList<Resource>(resource_num);
					RL = new HashMap<>();
					L = new ArrayList<Integer>();
					RL.put(0,L);
					L1 = new ArrayList<Integer>();
					RL.put(1,L1);
					L2 = new ArrayList<Integer>();
					RL.put(2,L2);
					bool = new AtomicInteger(1);
					init(PCB,RCB,RL);
					getvalue(bool,RL,s);
					bool_init = 1;
				}
				else if(splited[0].equals("to"))
				{
					bool = new AtomicInteger(1);
					timeout(RL);
					getvalue(bool,RL,s);
				}
			}
			else if(splited.length == 2) {
				if(splited[0].equals("cr"))
				{
					int num = Integer.parseInt(splited[1]);
					bool = new AtomicInteger(1);
					create(num,PCB,RL,bool);
					getvalue(bool,RL,s);
				}
				else if(splited[0].equals("de"))
				{
					int num = Integer.parseInt(splited[1]);
					//System.out.println("de " + num);
					bool = new AtomicInteger(1);
					destroy(num,PCB,RCB,RL,bool);
					getvalue(bool,RL,s);
				}
					
			}
			else if(splited.length == 3)
			{
				if(splited[0].equals("rq"))
				{
					int num1 = Integer.parseInt(splited[1]);
					int num2 = Integer.parseInt(splited[2]);
					bool = new AtomicInteger(1);
					request(num1,num2,PCB,RCB,RL,bool);
					getvalue(bool,RL,s);
				}
				else if(splited[0].equals("rl"))
				{
					int num1 = Integer.parseInt(splited[1]);
					int num2 = Integer.parseInt(splited[2]);
					bool = new AtomicInteger(1);
						release(num1,num2,PCB,RCB,RL,bool,1);
					getvalue(bool,RL,s);
				}
			}
		}
		write(s);
		
	}
	public static void readlines(File file, ArrayList<String> rs) throws IOException
	{
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while((line = br.readLine()) != null)
		{
			rs.add(line);
		}
		br.close();
		fr.close();
	}
}
