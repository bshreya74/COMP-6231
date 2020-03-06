package ServerSide.second;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import ServerSide.first.CenterServerDDO1;
import ServerSide.first.CenterServerLVL1;
import ServerSide.first.CenterServerMTL1;







public class Replica2 {
	
	public Replica2()
	{
		new MTL_thread().start();
		new LVL_thread().start();
		new DDO_thread().start();
	}
	
private String feedback; 
	
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
			System.out.println("Sending heartbeat to RM1");
		    sendPacket(PingSocket, 3002);
		    System.out.println("Sending heartbeat to RM3");
		    sendPacket(PingSocket, 5002);
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
		CenterServerMTL2 MTL2 = new CenterServerMTL2();
		MTL2.serverName = "Server_MTL2";
		MTL2.log(MTL2.serverName);
		System.out.println("serverMTL2 is ready and waiting ...");
		MTL2.openlistener();
		
	}
}

class LVL_thread extends Thread{
	public void run()
	{
		CenterServerLVL2 LVL2 = new CenterServerLVL2();
		LVL2.serverName = "Server_LVL2";
		LVL2.log(LVL2.serverName);
		System.out.println("serverLVL2 is ready and waiting ...");
		LVL2.openlistener();
		
	}
}

class DDO_thread extends Thread{
	public void run()
	{
		CenterServerDDO2 DDO2 = new CenterServerDDO2();
		DDO2.serverName = "Server_DDO2";
		DDO2.log(DDO2.serverName);
		System.out.println("serverDDO2 is ready and waiting ...");
		DDO2.openlistener();
		
	}
}

	
	public void listener(){
		DatagramSocket RP = null;
		try {
			RP = new DatagramSocket(1002);
			while(true){
				
				new heartbeat(RP).start();
				
				byte[] container = new byte[1024];
				DatagramPacket packet = new DatagramPacket(container, container.length);
				RP.receive(packet);
				String request = new String(packet.getData());
				int port = packet.getPort();
				InetAddress address = packet.getAddress();
				System.out.println("replica2 incoming: "+request);
				
				if(request.contains("Hi, can I send a message to you?")){
					System.out.println(request.trim());
					byte[] c = new byte[1024];
					c = "Hi, you can send the message to me.".getBytes();
		            DatagramPacket sendPacket1 =new DatagramPacket(c, c.length, address, port);
		            RP.send(sendPacket1);
				}
				
				
				//forward request to Replicas
				if(request.substring(5, 8).equals("MTL")){
					
					//send to mtl server 
					sendUDPRequest(RP, request, 6011);
					
				}		
				if(request.substring(5, 8).equals("LVL")){
					
					//send to lvl server		
					sendUDPRequest(RP, request, 6021);
				
					
				}
				if(request.substring(5, 8).equals("DDO")){
					
					//send to ddo server 
					sendUDPRequest(RP, request, 6031);
				}
				
				
				//receive a reply from server
				if(request.substring(0,2).equals("RP")){
					sendUDPReply(RP, request, 0002);
				}
				
				if(request.substring(0, 10).equals("ServerMTL2") || request.substring(0, 10).equals("ServerLVL2") || request.substring(0, 10).equals("ServerDDO2")){
					sendUDPReply(RP, request, 0002);
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
		Replica2 RP = new Replica2();
		System.out.println("Replica2 is ready and waiting ...");
		RP.listener();
	}
	private static void sendPacket(DatagramSocket to, int port) throws Exception {
        String ping = "replica2 is alive";
        byte[] data = ping.getBytes();
        InetAddress Address = InetAddress.getByName("localhost");

        DatagramPacket packet = new DatagramPacket(data, data.length, Address, port);
        to.send(packet);
    }
	
	
}

