import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.lang.*;

public class test2 {

	  public static void main(String[] args)
	  { 
		  ArrayList<String> s = new ArrayList<String>();
		  s.add("q ");
		  s.add("\n");
		  s.add("q ");
		  s.add("q ");
		  s.add("q ");
		  write(s);
	  }
	  public static void write(ArrayList<String> s)//throws Exception 
	  {
		  try {
				File file = new File("output");
				if (!file.exists())
				{
					file.createNewFile();
				}
				//PrintWriter out = new PrintWriter(new FileWriter(log, true));
				PrintWriter pw = new PrintWriter(new FileWriter(file,true));
				for(int i = 0;i<s.size();i++)
				{
					pw.print(s.get(i));
				}
				pw.close();
			}catch(IOException e) {
				System.out.println("create wrong");
			}

	  }

	
}
