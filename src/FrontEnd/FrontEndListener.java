package FrontEnd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import Record.Record;
import Record.StudentRecord;
import Record.TeacherRecord;

public class FrontEndListener {
	
	public static HashMap <Integer, DatagramPacket> packetMap;
	
	public FrontEndListener() {
		packetMap = new HashMap<Integer, DatagramPacket>();
	}
	
	class sendToFE extends Thread{
		DatagramSocket socket = null;
		DatagramPacket reponse = null;
		DatagramPacket request = null;
		String info = null;
		int number = 0;
		public sendToFE(DatagramSocket socket, DatagramPacket request, String info) {
			this.socket = socket;
			this.info = info;
			this.request = request;
		}
		public void run() {
			try {	
				byte[] message = info.getBytes();
				reponse = new DatagramPacket(message, message.length, request.getAddress(), request.getPort());
				socket.send(reponse);
			
			}
			catch(SocketException e){
				System.out.println("Soket: " + e.getMessage());
			}
			catch(IOException e){
				System.out.println("IO: " + e.getMessage());
			}
		}
	}
	
	public void listen(){
		DatagramSocket FEL;
		try {
			FEL = new DatagramSocket(8888);
			while(true){
				byte[] container = new byte[1024];
				DatagramPacket packet = new DatagramPacket(container, container.length);
				FEL.receive(packet);
				String request = new String(packet.getData());
				if(request.substring(0,2).equals("WT")){
					System.out.println("waiting is : "+request+" port number is: "+ packet.getPort());
					String[] b = request.split(":");
					packetMap.put(Integer.parseInt(b[1]), packet);
					int id = packetMap.get(Integer.parseInt(b[1])).getPort();
					System.out.println("port number is : "+id);
				}
				
				//receive a reply from server
				if(request.substring(0,2).equals("RP")){
					
					System.out.println("RP is "+request );
					String[] a = request.split(":");
					System.out.println("ID is "+a[1] );
					System.out.println("map size is "+packetMap.size());
					DatagramPacket p = packetMap.get(Integer.parseInt(a[1]));
					System.out.println("port number is: "+p.getPort());
					new sendToFE(FEL, p, request).start();
				}
				
			}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public static void main(String[] args) {
		FrontEndListener f = new FrontEndListener();
		System.out.println("Front End Listener is running");
		f.listen();
	}
	
	
}
