package FrontEnd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import DCMSApp.DCMS;
import DCMSApp.DCMSHelper;
import DCMSApp.DCMSPOA;

public class FrontEnd extends DCMSPOA {
	
	
	private ORB orb;
	static int requestID = -1;
	DatagramSocket FE = null;
	public static boolean leader1 = true;
	public static boolean leader2;
	public static boolean leader3;

	public FrontEnd() {
		try {
			FE = new DatagramSocket(9999);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void listen()
	
	{	DatagramSocket front = null;
		try
		{	front = new DatagramSocket(7777);
		
			while(true)
			{
				byte[] container = new byte[1024];
				DatagramPacket packet = new DatagramPacket(container, container.length);
				front.receive(packet);
				String request = new String(packet.getData());
				int port = packet.getPort();
				InetAddress address = packet.getAddress();
				
				
					if(request.contains("Make RM1 leader"))
					{
						leader1 = true;
						leader2 =false;
						leader3 = false;
						System.out.println("*********Leader is changed. Now leader is RM1**********");
					}
						
					if(request.contains("Make RM2 leader"))
					{
						leader2 = true;
						leader1 = false;
						leader3 = false;
						System.out.println("*********Leader is changed. Now leader is RM2**********");
					}
						
					if(request.contains("Make RM3 leader"))	
					{
						leader3 = true;
						leader1 = false;
						leader2 = false;
						System.out.println("*********Leader is changed. Now leader is RM3**********");
					}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setORB(ORB orb_val) {
		   orb = orb_val; 
	}
	
	
	
	
	
	
	
	@Override
	public String createTRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location) {
		requestID++;
		int a = requestID;
		String info = "RQ,"+"1,"+managerID+","+firstName+","+lastName+","+address+","+phone+","+specialization+","+location+","+a+",";
		String feedback = null;
		try {
			//Send the request
			byte[] message = info.getBytes();
			InetAddress FrontEndAddress = InetAddress.getByName("localhost");
			int RMPortNumber = 0;
			if(leader1){
				RMPortNumber = 0001; 
			}
			if(leader2){
				RMPortNumber = 0002; 
			}
			if(leader3){
				RMPortNumber = 0003; 
			}
			
			DatagramPacket client_request = new DatagramPacket(message, message.length, FrontEndAddress, RMPortNumber);
			FE.send(client_request);
			
			//receive feedback
			DatagramSocket waitingsocket = new DatagramSocket();
			String w = "WT:"+Integer.toString(a)+":";
			byte[] ww = w.getBytes();
			InetAddress FELaddress = InetAddress.getByName("localhost");
			int FELPortNumber = 8888; 
			DatagramPacket packet = new DatagramPacket(ww, ww.length, FELaddress, FELPortNumber);
			waitingsocket.send(packet);
			while(true){	
				byte[] container = new byte[1024];
				DatagramPacket reply = new DatagramPacket(container, container.length);
				waitingsocket.receive(reply);
				feedback = new String(reply.getData());
				if(!feedback.equals(null)){
					break;
				}
			}
			waitingsocket.close();
			
		}
		catch(SocketException e){
			System.out.println("Soket: " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}
		
		return feedback.trim();
	}


	@Override
	public String createSRecord(String managerID, String firstName, String lastName, String courseRegistered, String status, String statusDate) {
		requestID++;
		int a = requestID;
		String info = "RQ,"+"2," + managerID+","+ firstName+","+ lastName+","+ courseRegistered+"," +status+","+ statusDate+","+a+",";
//		DatagramSocket client_socket = null;
		String feedback = null;
		try {
//			client_socket = new DatagramSocket();
			byte[] message = info.getBytes();
			InetAddress FrontEndAddress = InetAddress.getByName("localhost");
			int RMPortNumber = 0;
			if(leader1){
				RMPortNumber = 0001; 
			}
			if(leader2){
				RMPortNumber = 0002; 
			}
			if(leader3){
				RMPortNumber = 0003; 
			}
			
			DatagramPacket client_request = new DatagramPacket(message, message.length, FrontEndAddress, RMPortNumber);
			FE.send(client_request);
			
			//receive feedback
			DatagramSocket waitingsocket = new DatagramSocket();
			String w = "WT:"+Integer.toString(a)+":";
			byte[] ww = w.getBytes();
			InetAddress FELaddress = InetAddress.getByName("localhost");
			int FELPortNumber = 8888; 
			DatagramPacket packet = new DatagramPacket(ww, ww.length, FELaddress, FELPortNumber);
			waitingsocket.send(packet);
			while(true){	
				byte[] container = new byte[1024];
				DatagramPacket reply = new DatagramPacket(container, container.length);
				waitingsocket.receive(reply);
				feedback = new String(reply.getData());
				if(!feedback.equals(null)){
					break;
				}
			}
			waitingsocket.close();
			
		
			
		}
		catch(SocketException e){
			System.out.println("Soket: " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}
		return feedback.trim();
	}


	@Override
	public String getRecordCounts(String managerID) {
		requestID++;
		int a = requestID;
		String info = "RQ,"+"3," +managerID+","+a+",";
//		DatagramSocket client_socket = null;
		String feedback = null;
		try {
			byte[] message = info.getBytes();
			InetAddress FrontEndAddress = InetAddress.getByName("localhost");
			int RMPortNumber = 0;
			if(leader1){
				RMPortNumber = 0001; 
			}
			if(leader2){
				RMPortNumber = 0002; 
			}
			if(leader3){
				RMPortNumber = 0003; 
			}
			
			DatagramPacket client_request = new DatagramPacket(message, message.length, FrontEndAddress, RMPortNumber);
			FE.send(client_request);
			
			
			//receive feedback
			DatagramSocket waitingsocket = new DatagramSocket();
			String w = "WT:"+Integer.toString(a)+":";
			byte[] ww = w.getBytes();
			InetAddress FELaddress = InetAddress.getByName("localhost");
			int FELPortNumber = 8888; 
			DatagramPacket packet = new DatagramPacket(ww, ww.length, FELaddress, FELPortNumber);
			waitingsocket.send(packet);
			while(true){	
				byte[] container = new byte[1024];
				DatagramPacket reply = new DatagramPacket(container, container.length);
				waitingsocket.receive(reply);
				feedback = new String(reply.getData());
				if(!feedback.equals(null)){
					break;
				}
			}
			waitingsocket.close();
			
	
		}
		catch(SocketException e){
			System.out.println("Soket: " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}
		return feedback.trim();
	}


	@Override
	public String editRecord(String managerID, String recordID, String fieldName, String newValue) {
		requestID++;
		int a = requestID;
		String info = "RQ,"+"4,"+ managerID+","+ recordID+","+ fieldName+","+ newValue+","+a+",";
//		DatagramSocket client_socket = null;
		String feedback = null;
		try {
			byte[] message = info.getBytes();
			InetAddress FrontEndAddress = InetAddress.getByName("localhost");
			int RMPortNumber = 0;
			if(leader1){
				RMPortNumber = 0001; 
			}
			if(leader2){
				RMPortNumber = 0002; 
			}
			if(leader3){
				RMPortNumber = 0003; 
			}
			
			DatagramPacket client_request = new DatagramPacket(message, message.length, FrontEndAddress, RMPortNumber);
			FE.send(client_request);
			
			//receive feedback
			DatagramSocket waitingsocket = new DatagramSocket();
			String w = "WT:"+Integer.toString(a)+":";
			byte[] ww = w.getBytes();
			InetAddress FELaddress = InetAddress.getByName("localhost");
			int FELPortNumber = 8888; 
			DatagramPacket packet = new DatagramPacket(ww, ww.length, FELaddress, FELPortNumber);
			waitingsocket.send(packet);
			while(true){	
				byte[] container = new byte[1024];
				DatagramPacket reply = new DatagramPacket(container, container.length);
				waitingsocket.receive(reply);
				feedback = new String(reply.getData());
				if(!feedback.equals(null)){
					break;
				}
			}
			waitingsocket.close();
			
			
		}
		catch(SocketException e){
			System.out.println("Soket: " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}
		return feedback.trim();
	}


	@Override
	public String transferRecord(String managerID, String recordID, String remoteCenterServerName) {
		requestID++;
		int a = requestID;
		String info ="RQ,"+ "5,"+ managerID+","+ recordID+","+ remoteCenterServerName+","+requestID+",";
//		DatagramSocket client_socket = null;
		String feedback = null;
		try {
			byte[] message = info.getBytes();
			InetAddress FrontEndAddress = InetAddress.getByName("localhost");
			int RMPortNumber = 0;
			if(leader1){
				RMPortNumber = 0001; 
			}
			if(leader2){
				RMPortNumber = 0002; 
			}
			if(leader3){
				RMPortNumber = 0003; 
			}
			
			DatagramPacket client_request = new DatagramPacket(message, message.length, FrontEndAddress, RMPortNumber);
			FE.send(client_request);
			
			//receive feedback
			DatagramSocket waitingsocket = new DatagramSocket();
			String w = "WT:"+Integer.toString(a)+":";
			byte[] ww = w.getBytes();
			InetAddress FELaddress = InetAddress.getByName("localhost");
			int FELPortNumber = 8888; 
			DatagramPacket packet = new DatagramPacket(ww, ww.length, FELaddress, FELPortNumber);
			waitingsocket.send(packet);
			while(true){	
				byte[] container = new byte[1024];
				DatagramPacket reply = new DatagramPacket(container, container.length);
				waitingsocket.receive(reply);
				feedback = new String(reply.getData());
				if(!feedback.equals(null)){
					break;
				}
			}
			waitingsocket.close();
		
			
		}
		catch(SocketException e){
			System.out.println("Soket: " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}
		return feedback.trim();
	}
	
	public static void main(String[] args) {
		try{
		      ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "1050"}, null);
		      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		      rootpoa.the_POAManager().activate();
		      FrontEnd frontImpl = new FrontEnd();
		      frontImpl.setORB(orb);
		      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(frontImpl);
		      DCMS href = DCMSHelper.narrow(ref);
		      org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		      String name = "FrontEnd";
		      NameComponent path[] = ncRef.to_name( name );
		      ncRef.rebind(path, href);
		      System.out.println("Front End is ready and waiting ...");
		      listen();
		      orb.run(); 
		 } catch (Exception e) {
			 System.err.println("ERROR: " + e);
			 e.printStackTrace(System.out);
		 }
	    
	}
}
