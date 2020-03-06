package ServerSide.first;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;



public class Replica1 {
	
	public Replica1()
	{	
		new MTL_thread().start();
		new LVL_thread().start();
		new DDO_thread().start();
	}
	
private String feedback; 


public void sendUDPRequest(DatagramSocket forward, String info, int portnumber){
	
	try {
		
		byte[] message = info.getBytes();
		InetAddress Address = InetAddress.getByName("localhost");
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
				
				byte[] message = info.getBytes();
				InetAddress Address = InetAddress.getByName("localhost");
				packetToServer = new DatagramPacket(message, message.length, Address, portnumber);
				forward.send(packetToServer);
				
			}
			catch(SocketException e){
				System.out.println("Soket: " + e.getMessage());
			}
			catch(IOException e){
				System.out.println("IO: " + e.getMessage());
			}
		}
		
	}*/

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

/*class sendUDPReply extends Thread{
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

class heartbeat extends Thread {
	DatagramSocket PingSocket = null;
	public heartbeat(DatagramSocket PingSocket)
	{
		this.PingSocket = PingSocket;
	}

	public void run(){
		try{
		while(true)
		{
			System.out.println("Sending heartbeat to RM2");
		    sendPacket(PingSocket, 4001);
		    System.out.println("Sending heartbeat to RM3");
		    sendPacket(PingSocket, 5001);
		    Thread.sleep(3000);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
	
class MTL_thread extends Thread{
	public void run()
	{
		CenterServerMTL1 MTL1 = new CenterServerMTL1();
		MTL1.serverName = "Server_MTL1";
		MTL1.log(MTL1.serverName);
		System.out.println("serverMTL1 is ready and waiting ...");
		MTL1.openlistener();
		
	}
}

class LVL_thread extends Thread{
	public void run()
	{
		CenterServerLVL1 LVL1 = new CenterServerLVL1();
		LVL1.serverName = "Server_LVL1";
		LVL1.log(LVL1.serverName);
		System.out.println("serverLVL1 is ready and waiting ...");
		LVL1.openlistener();
		
	}
}

class DDO_thread extends Thread{
	public void run()
	{
		CenterServerDDO1 DDO1 = new CenterServerDDO1();
		DDO1.serverName = "Server_DDO1";
		DDO1.log(DDO1.serverName);
		System.out.println("serverDDO1 is ready and waiting ...");
		DDO1.openlistener();
		
	}
}



	public void listener(){
		DatagramSocket RP = null;
		try {
			RP = new DatagramSocket(1001);
			while(true){
				
				new heartbeat(RP).start();
				
				byte[] container = new byte[1024];
				DatagramPacket packet = new DatagramPacket(container, container.length);
				RP.receive(packet);
				String request = new String(packet.getData());
				int port = packet.getPort();
				InetAddress address = packet.getAddress();
				System.out.println("replica1 incoming: "+request.trim());
				
				if(request.contains("Hi, can I send a message to you?")){
					
					byte[] c = new byte[1024];
					c = "Hi, you can send the message to me.".getBytes();
		            DatagramPacket sendPacket1 =new DatagramPacket(c, c.length, address, port);
		            RP.send(sendPacket1);
				}
				
				//forward request to servers
				if(request.substring(5, 8).equals("MTL")){	
					//send to mtl server 
					sendUDPRequest(RP, request, 6010);
					
				}		
				if(request.substring(5, 8).equals("LVL")){
					//send to lvl server		
					sendUDPRequest(RP, request, 6020);
				
					
				}
				if(request.substring(5, 8).equals("DDO")){
					//send to ddo server 
					sendUDPRequest(RP, request, 6030);
				}
				
				
				//receive a reply from server
				if(request.substring(0,2).equals("RP")){
					sendUDPReply(RP, request, 0001);
				}
				
				if(request.substring(0, 10).equals("ServerMTL1") || request.substring(0, 10).equals("ServerLVL1") || request.substring(0, 10).equals("ServerDDO1")){
					sendUDPReply(RP, request, 0001);
				}
			
			
			
			}
	
	
		}catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			if(RP != null)
				RP.close();
		}
	}
	
	
	public static void main(String[] args) {
		Replica1 RP = new Replica1();
		System.out.println("Replica1 is ready and waiting ...");
		RP.listener();
	}
	
	private static void sendPacket(DatagramSocket to, int port) throws Exception {
        String ping = "replica1 is alive";
        byte[] data = ping.getBytes();
        InetAddress Address = InetAddress.getByName("localhost");

        DatagramPacket packet = new DatagramPacket(data, data.length, Address, port);
        to.send(packet);
    }
	
	
	
	
}
