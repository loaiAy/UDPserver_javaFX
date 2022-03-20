import java.io.*;
import java.net.*;
import java.util.Scanner;

public class UDPserver {
	public UDPserver() {
		try {
			while(true)
				sendAndRecieve();
		} catch(Exception e1) {}
	} 
	
	public static void main(String[] args) {
		new UDPserver();
	}

	public void sendAndRecieve()  {	
		
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(9876);
			// receive from client      
			byte[] receive = new byte[1024];
			DatagramPacket packet = new DatagramPacket(receive, receive.length);
			
			serverSocket.receive(packet);
			
			String city = new String(packet.getData()).substring(0, packet.getLength());
			try {
			      File myFile = new File("C:\\Users\\Admin\\eclipse-workspace\\maman16SectionB\\bin\\weather.txt");
			      Scanner myReader = new Scanner(myFile);
		    	  boolean flag = false;
		    	  
			      while (myReader.hasNextLine()) {
			    	  String data = myReader.nextLine();
			    	  //checking if there is an existing data of the city the client chooses
			    	  if(data.contains(city)) {
			        	flag = true;
			        	int i = 0 ;
			        	//get the relevant weather data
			        	while(i<data.length()) {
			        		if(data.charAt(i) == ':') {
			        			i++;
			        			city = new String(data.substring(i, data.length()));
			        			break;
			        		}
			        		i++;
			        	}
			        	if(flag) {
			        		break;
			        	}
			        }
			      }
			      
			      myReader.close();
			    } catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			  
			// send to client
			int p = packet.getPort(); 
			InetAddress add = packet.getAddress();
			packet = new DatagramPacket(city.getBytes(), city.getBytes().length, add, p); 	
			serverSocket.send(packet);
		} catch (IOException e1) { 
		} finally { serverSocket.close(); }
	}
}
