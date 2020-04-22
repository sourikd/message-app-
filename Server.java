import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
class ClientWorker implements Runnable 
{
   private Socket client;
   public static ArrayList<String>  known = new ArrayList <String> ();
   public static ArrayList<String>  current = new ArrayList <String> ();
   String  k = "";
   String recp = "";
   String msg = "";
   String to_be_retrieved = "";
   public static HashMap<String, ArrayList<String>> map = new HashMap <String, ArrayList<String>>();
   ArrayList<String>list;// = new ArrayList<String> ();
   
   ClientWorker(Socket client) 
   {
      this.client = client;
      //map = new HashMap<>(); 
      //known = new ArrayList<>();
      //current = new ArrayList<>();
//list = new ArrayList<>();
   }

   public void run()
   {
      String name;
      String line;
      String concurrent_check = "";
      String exit_signal=""; 
      BufferedReader in = null;
      PrintWriter out = null;
      int OPTION = 0;
      String  option_number = ""; 
      int i = 0; 
//list={};
      try 
      {
	 in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	 out = new PrintWriter(client.getOutputStream(), true);
      } 
      catch (IOException e) 
      {
	 System.out.println("in or out failed");
	 System.exit(-1);
      }

      try 
      {
	 
	 name = in.readLine();
	 if (current.contains(name))//to prevent concurrent connections
	 {
	 	System.out.println(name + "tried to connect concurrently" );
		concurrent_check = "1";//send 1 so that client displays the user that it is already connected
		out.println(concurrent_check);
		return;
	 }
	 else
	 {
		concurrent_check = "0";
		out.println(concurrent_check);
	
 	 }
	 DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");  // date in MM-DD-YYYY format
	 Date date = new Date();
	 if (known.contains(name))
	 {
	 	System.out.println(dateFormat.format(date) + "Connection by known user " + name);//to display log in server(known or nunknown)
  	 }
	 else 
	 {
		System.out.println(dateFormat.format(date) + "Connection by unknown user " + name);
	 }

	
         if (!known.contains(name))  //makes sure known users are not repeated
 	 {
	   known.add(name);//insert name in known name list 
	 }
	 if (!current.contains(name))
	 {
           current.add(name);
     	 }
	while(true)
	{
	 //list.clear();
	 list = new ArrayList<>();
  	 //System.out.println("name : " + name);
	 //System.out.println("At beginning of while : "  + map.get(name));
	 
	 option_number = in.readLine ();
	 OPTION = Integer.valueOf(option_number);
   	 //System.out.println(map.get(name));
       	 Iterator it = map.entrySet().iterator();
	 //System.out.println("Inside iterator while:........ ");
         while (it.hasNext()) 
	{
         Map.Entry pair = (Map.Entry)it.next();
        // System.out.println(pair.getKey() + " = " + pair.getValue());
         //it.remove();
	}
	 //System.out.println("After iterator while: ..........");

         switch (OPTION)	
 	 {
		case 1 :  System.out.println(dateFormat.format(date)  + " " + name + " displays all known users." );
			  k="";
			  k= k + known.get(0);
			  if (known.size()>1)//displays all known users from known Arraylist
			  {
			     for ( i = 1; i<known.size(); i++)
			     {
			  	k = k + " " + known.get (i);
			 						
			     }
			  }
			  out.println(k);
			  break;
			  
        	case 2:   System.out.println(dateFormat.format(date) + " " + name + " displays all currently connected users ") ;
			  k= "";
			  k= k + current.get(0);
			 // System.out.println("k in server is : "+ k);
			  if (known.size()>1)
	                  {
			    for (i = 1; i<current.size(); i++)//displays all cuurently connected users in the current ArrayList
			    {
				k = k + " " + current.get(i);
			 						
			    }
 			  }
			  System.out.println("Server sending k (current user list seperated by space) : "  + k);  
			  out.println(k);
			  break;

		case 3:   line = in.readLine();
                          String particular_msg[] =  line.split(";");
			  recp = particular_msg[0];
			  System.out.println(" recp is " + recp);
			  msg = particular_msg[1];
			  if (!known.contains(recp) )
			  {
				//System.out.println("Inside Server.java: if recipient not in known");
				known.add(recp);	
			  }
			  System.out.println(dateFormat.format(date) + " " + name + " posts a message for " + recp) ;
			  to_be_retrieved = "From " + name + ", " + dateFormat.format(date)+ ", " + msg ;
			 //In client,  we cannot split by space but by ;
			  if (map.get(recp)== null)
			  {
				map.put(recp, list);
				//System.out.println("inside map.get(recp) ==null");
			  }
			  //System.out.println("retrieval of recp from map : " + map.get(recp));
			  list = map.get(recp);
			  list.add(to_be_retrieved);
			 // System.out.println("Inside server.java: list is "+list);
			  map.put(recp, list);
			  //map.get(recp).add(to_be_retrieved);
                          //System.out.println (" after additon retrieval of " + recp +": " +   map.get (recp) );
       			  //System.out.println (" after additon retrieval of " + recp +": " +   map.get (recp) );
				
			  break;                   
			  			  
		case 4:   line = "";
			  line = in.readLine();
			  System.out.println(dateFormat.format(date) + " " + name + " sends message to all currently connected users ") ;
			  k="";
                          to_be_retrieved = "From " + name + ", " + dateFormat.format(date)+ ", " + line ;
			  for (i=0; i<current.size(); i++) 
			  { 
  				if (name.equals(current.get(i)))// as a person does not send the message to himself
				{
				  continue;
				}
				else
				{
					k= current.get(i);//gets the name, then checks map by this name toobtain and update corresponding list
			  		if (map.get(k)==null)
			  		{
						map.put(k, list);
					}
					//map.get(k).add(to_be_retrieved);
					list = map.get(k);
					list.add(to_be_retrieved);
					map.put(k,list);
				}
			//	System.out.println("I am in Server case 4 inside for loop " );
			  }
			  break;

		case 5:   line = "";
			  line = in.readLine();
			  System.out.println(dateFormat.format(date) + " " + name + " sends message to all known users ") ;
  			  k="";
                          to_be_retrieved = "From " + name + ", " + dateFormat.format(date)+ ", " + line ;
			  for (i=0; i<known.size(); i++)
			  { 
 				if (name.equals(known.get(i)))
				{
				  continue;
				}
				else
				{
					k = known.get(i);
					if (map.get(k)==null)
			  		{
						map.put(k, list);
					}
					//map.get(k).add(to_be_retrieved);
					list = map.get(k);
					list.add(to_be_retrieved);
					map.put(k,list);

				}
			 //	System.out.println("I am in Server case 5 inside for loop " );

			  }
			  break;

	        case 6:   System.out.println(dateFormat.format(date) + " " + name + " gets messages ") ;
			  //put if (map.get(name==null )
			  if (map.get(name) == null)
			  {
			
				map.put(name, list );	
			//	System.out.println("in case 6, inside map.get(name)==NULL");	
			  }
			  
			  list = map.get(name);
			 // System.out.println(name);
			  //System.out.println(map.get(name));
			  k= "";
                          //System.out.println("in case 6, before if");
			  if (list.isEmpty())
			  {
			     out.println("no messages;");
			    // System.out.println("in case 6, inside if");
			    
   	
			  }  
			  else
			  {
			    for (i=0; i < list.size(); i++)
		            {
			  	 k = k +  (list.get(i)) + ";";
			    }
			    out.println(k);
			    list.clear();
			    map.put(name, list);//deletes message after displaying to client
			  }		  	 
			  break;

		case 7:    System.out.println(dateFormat.format(date) + " " + name + " exits ") ;
			   //exit_signal = in.ReadLine();
			   
			   current.remove(name);//have to remove connected user
			
			  return;
			  //break;
	  	
		default : break;


	}
       }
      } 
      catch (IOException e) 
      {
	 System.out.println("Read failed");
	 System.exit(-1);
      }

      try 
      {
	 client.close();
      } 
      catch (IOException e) 
      {
	 System.out.println("Close failed");
	 System.exit(-1);
      }
   }
}

public class Server 
{
   ServerSocket server = null;
 //  private static HashMap<String, ArrayList<String>> map = new HashMap <String, ArrayList<String>>();

   public void listenSocket(int port)
   {
      try
      {
	 server = new ServerSocket(port); 
	 System.out.println("Server running on port " + port + 
	                     "," + " use ctrl-C to end");
      } 
      catch (IOException e) 
      {
	 System.out.println("Error creating socket");
	 System.exit(-1);
      }
      while(true)
      {
         ClientWorker w;
         try
         {
            w = new ClientWorker(server.accept());
            Thread t = new Thread(w);
            t.start();
	 } 
	 catch (IOException e) 
	 {
	    System.out.println("Accept failed");
	    System.exit(-1);
         }
      }
   }

   protected void finalize()
   {
      try
      {
         server.close();
      } 
      catch (IOException e) 
      {
         System.out.println("Could not close socket");
         System.exit(-1);
      }
   }

   public static void main(String[] args)
   {
      if (args.length != 1)
      {
         System.out.println("Usage: java SocketThrdServer port");
	 System.exit(1);
      }

      Server server = new Server();
      int port = Integer.valueOf(args[0]);
      server.listenSocket(port);
   }
}
