Client displays following to users. 
1. Display the names of all known users.
2. Display the names of all currently connected users.
3. Send a text message to a particular user.
4. Send a text message to all currently connected users.
5. Send a text message to all known users.
6. Get my messages.
7. Exit.


Any user which connects at any point of time that the server is running becomes known user. 
Current user is someone who is currently connected. 
Message sent by a user is stored in specific format along with recepient's name and sent to the server. 
The server maintains a hash table from where it can retrieve the message if the recepient wants to view it. 
Supports unicast and broadcast messaging. 


To execute : 
java Server 2008 (for server tab where 2008 is port number)
java Client <machine-name> 2008 (for client tabs)

