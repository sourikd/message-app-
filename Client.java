import java.io.*;
import java.util.Scanner;
import java.net.*;
import java.lang.String;
public class Client
{
   Socket socket = null;
   PrintWriter out = null;//can write out anything
   BufferedReader in = null;//reads on lines (srings)"""Integer.parseInt(br.readLine())""" to retrive in server(strToInt conversion);
   
   public void communicate()
   {
      int option = 0;  
      int p = 0;
      String con_check = "";
      String msg= "";
      String recp_name= "";  
      String line = "";
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter your name: ");
      String name = sc.nextLine();

      //Send data over socket
      out.println(name); //for server to print connection by...
      try//to check concurret connection
      {
	con_check = in.readLine();
      }
       catch (IOException e)
       {
         System.out.println("No I/O transaction");
         System.exit(1);
       }
     

	
      if (con_check.equals("1"))
      {
		System.out.println("Cannot connect conccurently" );
	        System.exit(1);
      }

      while (true)
      {
	     line  =             " 1. Display the names of all known users.\n"+ 
				 " 2. Display the names of all currently connected users.\n"+
                                 " 3. Send a text message to a particular user.\n"+
				 " 4. Send a text message to all currently connected users.\n"+
				 " 5. Send a text message to all known users.\n"+
 				 " 6. Get my messages.\n"+
				 " 7. Exit.\n" ;
      
      System.out.println(line);
      option  = Integer.valueOf(sc.nextLine());  
      System.out.println("input check number "+ option); 
     if (option >= 1 && option <=7 )
     {
	out.println(String.valueOf(option)); // send option number to server 
     }
     else
     {
	System.out.println("Enter valid option");
     }

      //Receive text from server
      
      try
      {
        switch (option)
        {
     			/*There cannot be an empty array as case 1 and 2 will always have 1 (itself) component in array*/
 		case 1: line = in.readLine();
			
			String known_names[]= line.split(" ");//From servers send the names seperated by a whitespace
			System.out.println("Known users: \n" );
			p =0;
			for (int i =0 ; i< known_names.length; i++ )
			{
			  p = i + 1;
                          System.out.println(p +". " + known_names[i]+ "\n");
			}
			break;

		case 2: line = in.readLine();
		      //  System.out.println("in client after reading from server" + line);
			String current_names[]= line.split(" ");//From servers send the names seperated by a whitespace
			System.out.println("Currently connected  users: \n" );
			p =0;
			for (int i =0 ; i< current_names.length; i++ )
			{
			  p = i + 1;
                          System.out.println(p +". " + current_names[i]+ "\n");
			}
			break;

		case 3: System.out.println(" Enter  recepient's name :" );
			recp_name = sc.nextLine();			
			System.out.println(" Enter your message :" );
			msg = sc.nextLine();
			//put char limit check
                        int limit = msg.length();
            		int limit1;
             		while (limit > 80)
            		{ 
               		System.out.println("Input string should not exceed 80 characters");
                        System.out.println(" Enter your message again :" );
                        msg = sc.nextLine();
               	        limit1 = msg.length();
                        limit = limit1;                                      
            		}

			//System.out.println("The message sent is: " +  msg);
			out.println(recp_name+ ";"+ msg);//Recepient Name and message sent to server 
		        //System.out.println("Message posted to " + recp_name + "is" + recp_name+";"+msg);
			break;
	
		case 4:	System.out.println(" Enter your message : \n" );
			msg = sc.nextLine();
			out.println(msg);//message sent to server to broadcast to cuurently connected user
			break;
		       
		case 5:	System.out.println(" Enter your message : \n" );
			msg = sc.nextLine();
			out.println(msg);//REcepient Name and message sent to server  to broadcast to known users
			break;
		     
		case 6: line = in.readLine();//Since the clients thread will be created beforehand, we can retrieve messages using ththat from  server 
			System.out.println("line read from server  : " + line);
			String my_msg[]= line.split(";");//From servers send the names seperated by a whitespace
			System.out.println("my_msg[0]"  +  my_msg[0]);
			if (my_msg[0].equals("no messages"))//in case there are no messages for user
			  {
				System.out.println("no messages");
				break;
			  }
			else
			{
                          System.out.println("Your messages : \n" );
			  p =0;
		    	  for (int i =0 ; i< my_msg.length; i++ )
			  {
			    p = i + 1;//p is the serial number 
			    System.out.println(p +". " + my_msg[i]+ "\n");
			  }
			  break;
			}
		case 7: System.out.println("exit...");
			//out.println("1");
			System.exit(1);
			break;
	       default:System.out.println("Please enter correct option number ");
       
     }//switch 
    }//try
      catch (IOException e)
      {
         System.out.println("No I/O transaction");
         System.exit(1);
      }

   }//while
  }//communicate
   public void listenSocket(String host, int port)
   {
      //Create socket connection
      try
      {
	 System.out.println("Hello4");
	 socket = new Socket(host, port);
	 out = new PrintWriter(socket.getOutputStream(), true);
	 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	 System.out.println("Hello");
      } 
      catch (UnknownHostException e) 
      {
	 System.out.println("Unknown host");
	 System.exit(1);
      } 
      catch (IOException e) 
      {
	 System.out.println("No I/O");
	 System.exit(1);
      }
   }

   public static void main(String[] args)
   {
      if (args.length != 2)
      {
         System.out.println("Usage:  client hostname port");
	 System.exit(1);
      }

      Client client = new Client();


      String host = args[0];
      int port = Integer.valueOf(args[1]);
      System.out.println("Hello1");
      client.listenSocket(host, port);
    
       System.out.println("Hello2");
       client.communicate();
       System.out.println("Hello3");
   }
}
