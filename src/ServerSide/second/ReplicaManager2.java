package ServerSide.second;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

import FrontEnd.FrontEnd;
import ServerSide.first.CenterServerDDO1;
import ServerSide.first.CenterServerLVL1;
import ServerSide.first.CenterServerMTL1;
import ServerSide.first.Replica1;
import ServerSide.first.ReplicaManager1;
import ServerSide.third.ReplicaManager3;





public class ReplicaManager2 {
	
	public static LinkedList <String> request_buffer =  new LinkedList<String>();
	private int top;
	private boolean flag_forward;
	private boolean flag_response;
	private String result1;
	private String result2;
	private String result3;
	public String feedback;
	public static boolean flag1;
	public static boolean flag2;
	public static boolean flag3;
	public static boolean flag_leader;
	public static boolean alive = true;
	public boolean restart = false;
	public static boolean RM1 = true;
	public static boolean RM3;
	
	public boolean getFlag(boolean flag)
	{
		return flag;
	}
	public void requestOut(){
		for(int i = 0; i < request_buffer.size(); i++){
			System.out.println(request_buffer.get(i).trim());
		}
	}

	public boolean analyse2(String a){
		if(a.contains("ServerMTL2 has receieved the request and processed it ...") || a.contains("ServerLVL2 has receieved the request and processed it ...") || a.contains("ServerDDO2 has receieved the request and processed it ...")){
			return true;
		}
		return false;
	}
	
	public boolean analyse3(String a){
		if(a.contains("ServerMTL3 has receieved the request and processed it ...") || a.contains("ServerLVL3 has receieved the request and processed it ...") || a.contains("ServerDDO3 has receieved the request and processed it ...")){
			return true;
		}
		return false;
	}
	
	
	public boolean analyse1(String a){
		if(a.contains("ServerMTL1 has receieved the request and processed it ...") || a.contains("ServerLVL1 has receieved the request and processed it ...") || a.contains("ServerDDO1 has receieved the request and processed it ...")){
			return true;
		}
		return false;
	}
	
	
	
	
	
/*class sendUDPRequest extends Thread{
		
		DatagramSocket forward = null;
		DatagramPacket packetToServer = null;
		String info = null;
		int portnumber = 0;
		public sendUDPRequest(DatagramSocket forward, String info, int portnumber) {
			this.forward = forward;
			this.info = info;
			this.portnumber = portnumber;
		}
		public void run() {
			try {		
				byte[] sendRequest = new byte[1024];
	            
				byte[] message = info.getBytes();
				InetAddress Address = InetAddress.getByName("localhost");
				 
				//send request message
	             sendRequest = "Hi, can I send a message to you?".getBytes();
	             DatagramPacket packet1 = new DatagramPacket(sendRequest,sendRequest.length, Address, portnumber);
	             forward.send(packet1);
	             
	             
	             //receive confirmation
	            byte[] receiveCon = new byte[1024];
	 			DatagramPacket packet2 = new DatagramPacket(receiveCon, receiveCon.length);
	 			forward.receive(packet2);
	 			String confirmation = new String(packet2.getData());
	 			System.out.println("receieved confirmation: "+confirmation.trim());
				
	 			
	 			//send the data
	 			if(confirmation.contains("Hi, you can send the message to me.")){
	 					packetToServer = new DatagramPacket(message, message.length, Address, portnumber);
	 					forward.send(packetToServer);
	 			}
	 			
	 			//get ACK
	 			byte[] receieveAck = new byte[1024];
				DatagramPacket response = new DatagramPacket(receieveAck, receieveAck.length);
				forward.receive(response);
				String resp = new String(response.getData());
				System.out.println("receieved acknowledgement: "+resp.trim());
	 			
			
				
			
			}
			catch(SocketException e){
				System.out.println("Soket: " + e.getMessage());
			}
			catch(IOException e){
				System.out.println("IO: " + e.getMessage());
			}
		}
		
	}
	
	class sendUDPReply extends Thread{
		DatagramSocket forward = null;
		DatagramPacket packetToFE = null;
		String info = null;
		int portnumber = 0;
		public sendUDPReply(DatagramSocket forward, String info, int portnumber) {
			this.forward = forward;
			this.info = info;
			this.portnumber = portnumber;
		}
		public void run() {
			try {
				byte[] message = info.getBytes();
				InetAddress Address = InetAddress.getByName("localhost");
				packetToFE = new DatagramPacket(message, message.length, Address, portnumber);
				forward.send(packetToFE);
				
				
				
			}
			catch(SocketException e){
				System.out.println("Soket: " + e.getMessage());
			}
			catch(IOException e){
				System.out.println("IO: " + e.getMessage());
			}
		}
	}*/
	public void broadcastLeader(DatagramSocket forward, String info, int portnumber){
		
		 try {	
			
			byte[] message = info.getBytes();
			InetAddress Address = InetAddress.getByName("localhost");	
			//send the data
			DatagramPacket packetToServer = new DatagramPacket(message, message.length, Address, portnumber);
			forward.send(packetToServer);
			
		  }
		
			
			catch(SocketException e){
				System.out.println("Soket: " + e.getMessage());
			}
			catch(IOException e){
				System.out.println("IO: " + e.getMessage());
			}	
					
			
		}
	
	public void sendUDPRequest(DatagramSocket forward, String info, int portnumber){
		
		  try {	
			byte[] sendRequest = new byte[1024];
	        
			byte[] message = info.getBytes();
			InetAddress Address = InetAddress.getByName("localhost");
			 
			//send request message
	         sendRequest = "Hi, can I send a message to you?".getBytes();
	         DatagramPacket packet1 = new DatagramPacket(sendRequest,sendRequest.length, Address, portnumber);
	       
				forward.send(packet1);
		
	         
	         
	         //receive confirmation
	        byte[] receiveCon = new byte[1024];
	        DatagramPacket packet2 = new DatagramPacket(receiveCon, receiveCon.length);
	        forward.receive(packet2);
		
	        String confirmation = new String(packet2.getData());
	        System.out.println("receieved confirmation: "+confirmation.trim());
			
				
					//send the data
			if(confirmation.contains("Hi, you can send the message to me.")){
				DatagramPacket packetToServer = new DatagramPacket(message, message.length, Address, portnumber);
				
					forward.send(packetToServer);
			}
		  }
		
			
			catch(SocketException e){
				System.out.println("Soket: " + e.getMessage());
			}
			catch(IOException e){
				System.out.println("IO: " + e.getMessage());
			}	
					
			
		}
	
public void sendUDPReply(DatagramSocket forward, String info, int portnumber){
		
		try {
			byte[] message = info.getBytes();
			InetAddress Address = InetAddress.getByName("localhost");
			DatagramPacket packetToFE = new DatagramPacket(message, message.length, Address, portnumber);
			forward.send(packetToFE);
			
			
			
		}
		catch(SocketException e){
			System.out.println("Soket: " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}
		
	}

	class getheartbeat extends Thread {
		DatagramSocket PingSocket = null;
		byte[] buffer = new byte[1000];
		DatagramPacket p = new DatagramPacket(buffer, buffer.length);
		int port, rep_num;
		public getheartbeat(DatagramSocket PingSocket, int port, int rep_num)
		{
			this.PingSocket = PingSocket;
			this.port = port;
			this.rep_num = rep_num;
			
			
		}

		public void run(){
			try{
				Thread.sleep(5000);
				PingSocket.setSoTimeout(5000);
			while(true)
			{
				try {
			        PingSocket.receive(p);
			        String replicaMessage = new String(p.getData());
			        System.out.println(replicaMessage.trim());
			        restart = false;
			    } catch (SocketTimeoutException ste) {
			    	if(restart == false )
			    	{
			    		System.out.println("### Replica-"+rep_num+" is dead");
			    		System.out.println("Inform RM-"+rep_num);
			    		String rest="RS replica";
			    		byte[] info = rest.getBytes();
			    		InetAddress Address = InetAddress.getByName("localhost");
			    		DatagramPacket rep2res = new DatagramPacket(info, info.length, Address ,port);
			    		PingSocket.send(rep2res);
			    		System.out.println("Sent replica restart message");
			    		restart = true ; 
			    	}
			    	else
			    	{
			    		System.out.println("Waiting for replica "+rep_num);
			    		continue;
			    	}
			        
			    }
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
/*	public void restore_replica(int last, DatagramSocket Rep){
		int new_top = 0;
		while(new_top<last){
			//forward first request
				if(new_top == 0){
					if(request_buffer.size()>0){
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("buffer length is :"+ request_buffer.size());
						String req_next = request_buffer.get(new_top);
						synchronized (this) {
							new_top ++;
							}
							sendUDPRequest(Rep, req_next, 1002);
						}
					}
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//replica server executed next request
				if(flag_forward == true){
					if(new_top < request_buffer.size()){
						System.out.println("*****buffer length is :"+ request_buffer.size());
						String req_next = request_buffer.get(new_top);
						synchronized (this) {
							new_top ++;
							flag_forward = false;
						}
						sendUDPRequest(Rep, req_next, 1002);	
						
					}
				}			
			}
	}*/
	
	class restore_replica extends Thread{
		int last;
		DatagramSocket Rep =null;
		int new_top = 0;
		public restore_replica(int last, DatagramSocket Rep)
		{
			this.last = last;
			this.Rep =Rep;
		}
		public void run(){
			while(new_top<last){
			//forward first request
				if(new_top == 0){
					if(request_buffer.size()>0){
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("buffer length is :"+ request_buffer.size());
						String req_next = request_buffer.get(new_top);
						synchronized (this) {
							new_top ++;
							}
							sendUDPRequest(Rep, req_next, 1002);
						}
					}
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//replica server executed next request
				if(flag_forward == true){
					if(new_top < request_buffer.size()){
						System.out.println("*****buffer length is :"+ request_buffer.size());
						String req_next = request_buffer.get(new_top);
						synchronized (this) {
							new_top ++;
							flag_forward = false;
						}
						sendUDPRequest(Rep, req_next, 1002);	
						
					}
				}			
			}
		}
	}
	
	class restart_replica extends Thread{
		
		
		
		public void run()
		{
			Replica2 RPnew = new Replica2();
			RPnew.listener();
		}
		
	}
	
	public void listen(){
		DatagramSocket RM = null;
		DatagramSocket RC = null;
		DatagramSocket replica1 = null;
		DatagramSocket replica3 = null;
		int last = 0;
		int last_req = 0;
		int restart_counter = 0;
		try {
			RM = new DatagramSocket(0002);
			RC = new DatagramSocket(0012);
			replica1 = new DatagramSocket(4001);
			replica3 = new DatagramSocket(4003);
			while(true){
				
				//receive ping from replica2
				new getheartbeat(replica1, 0001, 1).start();
				new getheartbeat(replica3, 0003, 3).start(); 
				
				byte[] container = new byte[1024];
				DatagramPacket packet = new DatagramPacket(container, container.length);
				RM.receive(packet);
				String request = new String(packet.getData());
				int port = packet.getPort();
				InetAddress address = packet.getAddress();
				
				System.out.println("M2 incoming: "+request.trim());
				
				if(request.contains("Hi, can I send a message to you?")){
					System.out.println(request.trim());
					byte[] c = new byte[1024];
					c = "Hi, you can send the message to me.".getBytes();
		            DatagramPacket sendPacket1 =new DatagramPacket(c, c.length, address, port);
		            RM.send(sendPacket1);
				}
				
				if(request.substring(0,2).equals("RS"))
				{	
					restart_counter++;
					System.out.println("Starting replica 1");
					alive = false;
					flag_forward = false;
					
					if(restart_counter == 2){
						new restart_replica().start();
						
						//changing leader
						if(flag_leader == true)
						{
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.out.println("Make RM3 leader");
							String msg = "Make RM3 leader";
							//inform RM3
							broadcastLeader(RC,msg,0003);
							//inform RM1
							broadcastLeader(RC,msg,0001);
							//inform Front End
							broadcastLeader(RC,msg,7777);
							//MTL
							broadcastLeader(RC,msg,6011);
							//LVL
							broadcastLeader(RC,msg,6021);
							//DDO
							broadcastLeader(RC,msg,6031);
							flag_leader = false;
							RM3 = true;
						}
						
						
						//execute buffer requests
						last_req = top;
						new restore_replica(last_req,RC).start();
						System.out.println("Replica 2 restored");
					}
					
				}
				
				//making itself leader
				if(request.contains("Make RM2 leader"))
				{	
					//MTL
					broadcastLeader(RC,request,6011);
					//LVL
					broadcastLeader(RC,request,6021);
					//DDO
					broadcastLeader(RC,request,6031);
					System.out.println("I am the new leader");
					flag_leader = true;
					RM1 = false;
					RM3 = false;
					
				}
				
				if(request.contains("Make RM1 leader")){
					System.out.println("RM1 is new Leader");
					RM1 = true;
					RM3 = false;
				}
				
				
				//received packet is request from FE
				if(request.substring(0,2).equals("RQ")){
					synchronized (this) {
					request_buffer.add(request);
					}
					System.out.println("buffer is: ");
					requestOut();
				}
				
				
				if(flag_leader){
					//forward first request
					if(top == 0 && alive){
						if(request_buffer.size()>0){
							System.out.println("buffer length is :"+ request_buffer.size());
							String req_next = request_buffer.get(top);
							synchronized (this) {
								top ++;
								}
							sendUDPRequest(RC, req_next, 1002);
							sendUDPRequest(RC, req_next, 0001);
							sendUDPRequest(RC, req_next, 0003);
							System.out.println("MTL top: "+ top+ " MTL FLAG: "+flag_forward);
						}
					}
					
					//receive a reply from server
					if(request.substring(0,2).equals("RP")){
						feedback = request;
						flag2 = true;
					}
					
					if(request.substring(0, 10).equals("ServerMTL1") || request.substring(0, 10).equals("ServerLVL1") || request.substring(0, 10).equals("ServerDDO1")){
						flag1 = analyse1(request);
					}
					
					if(request.substring(0, 10).equals("ServerMTL3") || request.substring(0, 10).equals("ServerLVL3") || request.substring(0, 10).equals("ServerDDO3")){
						flag3 = analyse3(request);
					}
					
					
					//reply back to FE and forward next request to replicas
					while(flag1 && flag2 && flag3){
						sendUDPReply(RM, feedback, 8888);
						flag1 = false;
						flag2 = false;
						flag3 = false;
						flag_forward = true;
						
						
					}
					
					//if replica is restarting
					if(flag2 == true && alive == false)
					{	last++;
						flag_forward = true;
						flag2 = false;
						if(last == last_req){
							alive = true;
							restart_counter = 0;
							last = 0;
						}
						System.out.println("****************i changed forward");
					}
					
					//forward next request
					if(flag_forward == true && alive){
						if(top < request_buffer.size()){
							System.out.println("*****buffer length is :"+ request_buffer.size());
							String req_next = request_buffer.get(top);
							synchronized (this) {
								top ++;
								flag_forward = false;
							}
							sendUDPRequest(RC, req_next, 1002);
							sendUDPRequest(RC, req_next, 0001);
							sendUDPRequest(RC, req_next, 0003);
							System.out.println("*****MTL top: "+ top+ " MTL FLAG: "+flag_forward);
						}
					}
				}
				
				//if RM is not the leader
				else{	
					
					
					
					
					
					//forward first request
					if(top == 0 && alive){
						if(request_buffer.size()>0){
							System.out.println("buffer length is :"+ request_buffer.size());
							String req_next = request_buffer.get(top);
							synchronized (this) {
								top ++;
							}
				            
							sendUDPRequest(RC, req_next, 1002);
							System.out.println("MTL top: "+ top+ " MTL FLAG: "+flag_forward);
						}
					}
					
					
					//receive a reply from server
					if(request.substring(0,2).equals("RP")){
						feedback = request;
						flag2 = true;
					}
				
					
					//receive a reply from server
					if(request.substring(0, 10).equals("ServerMTL2") || request.substring(0, 10).equals("ServerLVL2") || request.substring(0, 10).equals("ServerDDO2")){
						flag2 = analyse2(request);
						feedback = request;
					}
					
					//reply back to RM 
					while(flag2 && alive){
						if(RM1){
							sendUDPReply(RM, feedback, 0001);
						}
						
						if(RM3){
							sendUDPReply(RM, feedback, 0003);
						}
						
						flag2 = false;
						flag_forward = true;
					}
					
					//if replica is restarting
					if(flag2 == true && alive == false)
					{	last++;
						flag_forward = true;
						flag2 = false;
						if(last == last_req){
							alive = true;
							restart_counter = 0;
							last = 0;
						}
						System.out.println("****************i changed forward"+ flag_forward+" alive "+alive);
						
					}
					
		
					
					//forward next request
					if(flag_forward == true && alive){
						System.out.println("!!!!!!!!!!!!!!!!!alive again next request");
						if(top < request_buffer.size()){
							System.out.println("*****buffer length is :"+ request_buffer.size());
							String req_next = request_buffer.get(top);
							synchronized (this) {
								top ++;
								flag_forward = false;
							}
							sendUDPRequest(RC, req_next, 1002);
							System.out.println("*****MTL top: "+ top+ " MTL FLAG: "+flag_forward);
						}
					}
					
				}
				
				
				
				
				
			
				
			}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(RM != null )
					RM.close();
				
				if(replica1 != null)
					replica1.close();
				
				if(RC != null)
					RC.close();

			}
		
	}
	
/*	public void startReplica2(){
		Replica2 RP = new Replica2();
		System.out.println("Replica2 is ready and waiting ...");
		RP.listener();
	}
	
	public void startMTL(){
		CenterServerMTL2 mtl = new CenterServerMTL2();
		System.out.println("serverMTL1 is ready and waiting ...");
		mtl.openlistener();
	}
	
	public void startLVL(){
		CenterServerLVL2 lvl = new CenterServerLVL2();
		System.out.println("serverLVL1 is ready and waiting ...");
		lvl.openlistener();
	}

	public void startDDO(){
		CenterServerDDO2 ddo = new CenterServerDDO2();
		System.out.println("serverDDO1 is ready and waiting ...");
		ddo.openlistener();
	}
	*/
	
	public static void main(String[] args) {
		ReplicaManager2 RM = new ReplicaManager2();
		System.out.println("ReplicaManager2 is ready and waiting ...");
		RM.listen();
	}
}
