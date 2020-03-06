package ServerSide.third;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import ClientSide.ManagerClient;
import DCMSApp.DCMS;
import DCMSApp.DCMSHelper;
import DCMSApp.DCMSPOA;

import Record.Record;
import Record.StudentRecord;
import Record.TeacherRecord;

import ServerSide.first.ServerInterface;

import java.io.Serializable;

public class CenterServerDDO3 implements ServerInterface{
	
	
	public static int mtlcount;
	public static int lvlcount;
	public static int ddocount;
	public int idNum = 30000;
	public static LinkedList <Record> new_transferRecord;
	public LinkedList <Record> records;
	public LinkedList <Record> new_records;
	static Logger logger;
	static FileHandler output;
	public static String managerID;
	public static boolean flag_reply;
	public static String serverName;


	

/*	public CenterServerDDO1() {
		String serverName = "Server_DDO1";
		log(serverName);
	}*/
	

	
	public void getserverName(){
		System.out.println("DDO");
	}
	
	//create the hashmap and input some records
	public static HashMap <Character, LinkedList<Record>> hashMapDDO = new HashMap<Character, LinkedList<Record>>(){
		{
			put('C', new LinkedList<Record>(Arrays.asList(new Record(new TeacherRecord("ZZZ", "CCCC", "5527 rue monsabre", "4389282222", "chinese", "ddo", "TR31111"),"TR31111"))));
			put('G', new LinkedList<Record>(Arrays.asList(new Record(new TeacherRecord("AlexDRAN", "GoR", "2001 rue monsabre", "4389282221", "french", "ddo", "TR31112"),"TR31112"))));
			put('H', new LinkedList<Record>(Arrays.asList(new Record(new StudentRecord("Hh", "Hh", "math", "active", "20170224", "SR31111"), "SR31111"))));
			put('W', new LinkedList<Record>(Arrays.asList(new Record(new StudentRecord("M", "W", "history", "active", "20170527", "SR31112"),"SR31112"))));
			put('T', new LinkedList<Record>(Arrays.asList(new Record(new StudentRecord("Jerome", "Targget", "frech", "active", "20170101", "SR31113"), "SR31113"))));
		}
	};
	
	//create manager ID list
	public static LinkedList <String> managers = new LinkedList<String>(){
		{
		add("DDO2111");
		add("DDO2112");
		add("DDO2113");
		add("DDO2114");
		add("DDO2115");
		}
	};
	
	//check if the current manager is in our database(if it is valid)
	public static boolean checkManager(String id){
		for(String list: CenterServerDDO3.managers){
			if(list.equals(id)){
				CenterServerDDO3.managerID = id;
				System.out.println("mtl manager id is: "+CenterServerDDO3.managerID);
				return true;
			}
		}
		return false;
	}
	
	//initialize the logger for server
	public static void log(String serverName){
		try {
			logger = Logger.getLogger(ManagerClient.class.getName());
			logger.setUseParentHandlers(false);
			output = new FileHandler(serverName+".log", true);
			logger.addHandler(output);
			SimpleFormatter format = new SimpleFormatter();
			output.setFormatter(format);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized int assignID(){
		idNum++;
		return idNum;
	}
	
	//create teacher record
	public String ScreateTRecord(String managerID, String firstName, String lastName, String address, String phone, String specialization,
			String location) {
		
		if(!location.equals("mtl") && !location.equals("lvl") && !location.equals("ddo")){
			logger.info("Manager ID: "+managerID+". Manager tried to create a teacher record, but the location doesn't match with the expecting type, creating failed"+'\n');
			return "Creating a teacher record failed !! The location doesn't match with the expecting type !!";
		}
		
		char firstLetter = lastName.charAt(0);
		String ID = "TR" + assignID();
		TeacherRecord tr = new TeacherRecord(firstName, lastName, address, phone, specialization, location, ID);
		Record r = new Record(tr, ID);
		
		if(hashMapDDO.containsKey(firstLetter)){
			records = hashMapDDO.get(firstLetter);
			records.add(r);
		}else{
			records = new LinkedList<Record>();
			records.add(r);
		}
		synchronized(this){
			hashMapDDO.put(firstLetter, records);
		}
		for(HashMap.Entry entry : hashMapDDO.entrySet())
		{
			System.out.println("TR!!!+ "+entry.getKey() + ": " + entry.getValue().toString());
		}
		
		logger.info("Manager ID: "+managerID+". Manager has successfully created teacher record: "+'\n'+records.getLast().toString());
		return "Teacher record is created !! "+"The record is: "+'\n'+records.getLast().toString();		
	}
	//create student record
	public String ScreateSRecord(String managerID, String firstName, String lastName, String coursesRegistered, String status, String statusDate){
		
		if(!status.equals("active") && !status.equals("inactive")){
			logger.info("Manager ID: "+managerID+". Manager tried to create a student record, but the status doesn't match with the expecting type, creating failed"+'\n');
			return "Creating a student record failed !! The status doesn't match with the expecting type !!";
		}
			
		char firstLetter = lastName.charAt(0);
		String ID = "SR" + assignID();
		StudentRecord sr = new StudentRecord(firstName, lastName, coursesRegistered, status, statusDate, ID);
		Record r = new Record(sr, ID);
		
		
		
		if(hashMapDDO.containsKey(firstLetter)){
			records = hashMapDDO.get(firstLetter);
			records.add(r);
		}else{
			records = new LinkedList<Record>();
			records.add(r);
		}
		synchronized(this){
			hashMapDDO.put(firstLetter, records);
		}
		
		for(HashMap.Entry entry : hashMapDDO.entrySet())
		{
			System.out.println("SR!!!+ "+entry.getKey() + ": " + entry.getValue().toString());
		}
		logger.info("Manager ID: "+managerID+". Manager has successfully created student record: "+'\n'+records.getLast().toString());
		return "Student record is created !! "+"The record is: "+'\n'+records.getLast().toString();	
	
			
	}
 
	
	//request for the number of records 
	public String SgetRecordCounts(String managerID) {
		// TODO Auto-generated method stub
		lvlcount = requestlvl();
		mtlcount = requestmtl();
		ddocount = getHashSize();
		String count ="MTL: " + mtlcount + "\n" + "LVL: " + lvlcount + "\n" + "DDO: " + ddocount + "\n";
		logger.info("Manager ID: "+managerID+". Manager has requested to know the number of all the records from all the servers. The result is: "+'\n'+count);
		return count;
	}
	
	public int requestmtl()
	{
		int mtlcount;
		DatagramSocket MTLSocket = null;
		try
		{
			MTLSocket = new DatagramSocket();
			byte[] message = "GetCount".getBytes();
			InetAddress MTLHost = InetAddress.getByName("localhost");
			int MTLServerPort = 6012;
			DatagramPacket MTLrequest = new DatagramPacket(message, message.length, MTLHost, MTLServerPort);
			MTLSocket.send(MTLrequest);
			
			byte[] MTLbuffer = new byte[1000];
			DatagramPacket MTLreply = new DatagramPacket(MTLbuffer, MTLbuffer.length);
			MTLSocket.receive(MTLreply);
			String MTLCount = new String(MTLreply.getData());
			mtlcount = Integer.parseInt(MTLCount.trim());
			return mtlcount;
			
		}catch(SocketException e){
			System.out.println("Soket: " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}
		finally{
			
			if(MTLSocket != null)
				MTLSocket.close();
		}
		return 0;
	
	}

	public int requestlvl()
	{
		int lvlcount;
		DatagramSocket LVLSocket = null;
		try
		{
			LVLSocket = new DatagramSocket();
			byte[] message = "GetCount".getBytes();
			InetAddress LVLHost = InetAddress.getByName("localhost");
			int LVLServerPort = 6022;
			DatagramPacket LVLrequest = new DatagramPacket(message, message.length, LVLHost, LVLServerPort);
			LVLSocket.send(LVLrequest);
			
			byte[] LVLbuffer = new byte[1000];
			DatagramPacket LVLreply = new DatagramPacket(LVLbuffer, LVLbuffer.length);
			LVLSocket.receive(LVLreply);
			String LVLCount = new String(LVLreply.getData());
			lvlcount = Integer.parseInt(LVLCount.trim());
			return lvlcount;
			
		}catch(SocketException e){
			System.out.println("Soket: " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}
		finally{
			
			if(LVLSocket != null)
				LVLSocket.close();
		}
		return 0;
	
	}

	//edit record
	public String SeditRecord(String managerID, String recordID, String fieldName, String newValue){
		
		if(recordID.contains("TR")){
			for(Map.Entry<Character, LinkedList<Record>> entry : hashMapDDO.entrySet()){
				for(Record record : entry.getValue()){
					if(record.m_ID.equals(recordID)){
						if(fieldName.equals("Address") || fieldName.equals("address")){
							synchronized(this){
								record.m_teacher.setAddress(newValue); 
							}
							System.out.println("EditRecord is successful and the new record is: ");
							System.out.println(record.toString());
							logger.info("Manager ID: "+managerID+". Manager has successfully edited the address of teacher record : "+'\n'+record.toString());
							return "Editting the address of a teacher record is successful and the new record is: "+record.toString();
						}
						if(fieldName.equals("Phone") || fieldName.equals("phone")){
							synchronized(this){
								record.m_teacher.setPhone(newValue);	
							}
							System.out.println("EditRecord is successful and the new record is: ");
							System.out.println(record.toString());
							logger.info("Manager ID: "+managerID+". Manager has successfully edited the phone number of teacher record : "+'\n'+record.toString());
							return "Editting the phone munber of a teacher record is successful and the new record is: "+record.toString();
						}
						if(fieldName.equals("Specialization") || fieldName.equals("specialization")){
							synchronized(this){
								record.m_teacher.setSpecialization(newValue);
							}
							System.out.println("EditRecord is successful and the new record is: ");
							System.out.println(record.toString());
							logger.info("Manager ID: "+managerID+". Manager has successfully edited the sepcialization of teacher record : "+'\n'+record.toString());
							return "Editting the specialization of a teacher record is successful and the new record is: "+record.toString();
						}
						if(fieldName.equals("Location") || fieldName.equals("location")){
							if(newValue.equals("mtl") || newValue.equals("lvl") || newValue.equals("ddo")){
								synchronized(this){
									record.m_teacher.setLocation(newValue);
								}
								System.out.println("EditRecord is successful and the new record is: ");
								System.out.println(record.toString());
								logger.info("Manager ID: "+managerID+". Manager has successfully edited the location of teacher record : "+'\n'+record.toString());
								return "Editting the location of a teacher record is successful and the new record is: "+record.toString();
							}else{
								System.out.println("EditRecord failed !! The location doesn't match with the expecting type !!"+"\n");
								logger.info("Manager ID: "+managerID+". Manager has edited the location of teacher record, but the location doesn't match with the expecting type, editing failed"+'\n'+"The record before editting is: "+'\n'+records.toString());
								return "Editting the location of a teacher record failed !! The location doesn't match with the expecting type !!";
							}
						}
					}
				}
			}
		}
		
		if(recordID.contains("SR")){
			for(Map.Entry<Character, LinkedList<Record>> entry : hashMapDDO.entrySet()){
				for(Record record : entry.getValue()){
					if(record.m_ID.equals(recordID)){
						if(fieldName.equals("CoursesRegistered") || fieldName.equals("coursesregistered")){
							synchronized(this){
								record.m_student.setcourseRegistered(newValue); 
							}
							System.out.println("EditRecord is successful and the new record is: ");
							System.out.println(record.toString());
							logger.info("Manager ID: "+managerID+". Manager has successfully edited the courses registered of student record : "+'\n'+record.toString());
							return "Editting the courses registered of a student record is successful and the new record is: "+record.toString();
						}
						if(fieldName.equals("Status") || fieldName.equals("status")){
							if(newValue.equals("active") || newValue.equals("inactive")){
								synchronized(this){
									record.m_student.setStatus(newValue);	
								}
								System.out.println("EditRecord is successful and the new record is: ");
								System.out.println(record.toString());
								logger.info("Manager ID: "+managerID+". Manager has successfully edited the status of student record : "+'\n'+record.toString());
								return "Editting the status of a student record is successful and the new record is: "+record.toString();
							}else{
								logger.info("Manager ID: "+managerID+". Manager has edited the status of student record, but the status doesn't match with the expecting type, editting failed"+'\n'+"The record before editting is: "+'\n'+records.toString());
								System.out.println("EditRecord failed !! The status doesn't match with the expecting type !!"+"\n");
								return "Editting the status of a student record failed !! The status doesn't match with the expecting type !!";
							}
						}
						if(fieldName.equals("StatusDate") || fieldName.equals("statusdate")){
							synchronized(this){
								record.m_student.setStatusDate(newValue);
							}
							System.out.println("EditRecord is successful and the new record is: ");
							System.out.println(record.toString());
							logger.info("Manager ID: "+managerID+". Manager has successfully edited the status date of student record : "+'\n'+record.toString());
							return "Editting the status date of a student record is successful and the new record is: "+record.toString();
						} 
					}
					
				}
			}
		}
		logger.info("Manager ID: "+managerID+" has edited a record, but the editting failed "+'\n');
		return "Editting a record failed !!";
	}
	
	public static int getHashSize()
	{
		int count=0;
		for(HashMap.Entry<Character, LinkedList<Record>> entry: hashMapDDO.entrySet())
		{
			count = count + entry.getValue().size();
		}
		return count;
	}
	
/*	public static void main(String args[]) throws Exception{
		CenterServerDDO3 ddo = new CenterServerDDO3();
		serverName = "Server_DDO1";
		log(serverName);
		System.out.println("serverDDO1 is ready and waiting ...");
		ddo.openlistener();
		       
	}*/

	public void openlistener()
	{
		DatagramSocket ddosocket = null;
		Record rec;
		try{
			ddosocket = new DatagramSocket(6032);
			byte[] incomingData = new byte[10000]; 
			while(true){
				//get action
				byte[] buffer = new byte[100]; 
				DatagramPacket ddorequest = new DatagramPacket(buffer, buffer.length); 
				ddosocket.receive(ddorequest);
				String request = new String(ddorequest.getData());
				int port = ddorequest.getPort();
				InetAddress address = ddorequest.getAddress();
		
				if(request.contains("Hi, can I send a message to you?")){
					byte[] c = new byte[1024];
					c = "Hi, you can send the message to me.".getBytes();
		            DatagramPacket sendPacket1 =new DatagramPacket(c, c.length, address, port);
		            ddosocket.send(sendPacket1);
				}
				
				if(request.contains("GetCount"))
				{
					new thread(ddosocket, ddorequest);
				}
				
				else if(request.contains("Transfer"))
				{
					//send ack
					String rep = "Send Record";
					byte[] ack = rep.getBytes();
					DatagramPacket acknowledge = new DatagramPacket(ack, ack.length, ddorequest.getAddress(), ddorequest.getPort());
					ddosocket.send(acknowledge);
				
				//transfer record
					DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length); 
					ddosocket.receive(incomingPacket);
					byte[] data = incomingPacket.getData();
					ByteArrayInputStream bain = new ByteArrayInputStream(data);
					ObjectInputStream ois = new ObjectInputStream(bain);
				
					try{
						rec = (Record) ois.readObject();
						System.out.println("Record received");
						System.out.println(rec.toString());
						new threadrec(ddosocket, incomingPacket, rec);
					}catch(ClassNotFoundException e){
						e.printStackTrace();
					}
				}
				
				//create teacher record
				else if(request.substring(3,4).equals("1"))
				{	
					System.out.println("request to create a TR");
					new CreateTR(ddosocket, ddorequest, request).start();	
				}
				
				//create student record
				else if(request.substring(3,4).equals("2")){
					System.out.println("request to create a SR");
					new CreateSR(ddosocket, ddorequest, request).start();
				}
				
				//get count
				else if(request.substring(3,4).equals("3")){
					System.out.println("request to get count");
					new GetCount(ddosocket, ddorequest, request).start();
				}
				
				//edit record
				else if(request.substring(3,4).equals("4")){
					System.out.println("request to edit record");
					new EditRecord(ddosocket, ddorequest, request).start();

				}
				
				//transfer record
				else if(request.substring(3,4).equals("5")){
					System.out.println("request to transfer record");
					new TransferRecord(ddosocket, ddorequest, request).start();
				}
				
				else if(request.contains("Make RM2 leader")){
					flag_reply = false;
				}
				else if(request.contains("Make RM3 leader")){
					flag_reply = true;
				}
				else if(request.contains("Make RM1 leader")){
					flag_reply = false;
				}
				
				else{ System.out.println("unexisting request"); }
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(ddosocket != null) 
				ddosocket.close();
			//if(serverddosocket != null) 
				//serverddosocket.close();
		}
	}
	
	static class threadrec extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		Record newrecord;
		public threadrec(DatagramSocket n_socket, DatagramPacket n_request, Record rec) {
			this.socket = n_socket;
			this.request = n_request;
			this.newrecord = rec;
			this.start();
		}
		
	
		@Override
		public void run() {
			try {
				String reply ="Thank You";
				byte[] rep = reply.getBytes();
				DatagramPacket serverddoreply = new DatagramPacket(rep, rep.length, request.getAddress(), request.getPort()); 
				socket.send(serverddoreply);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(newrecord.m_ID.contains("TR")){
				char firstLetter = newrecord.m_teacher.m_lastName.charAt(0);
				if(CenterServerDDO3.hashMapDDO.containsKey(firstLetter)){
					new_transferRecord = CenterServerDDO3.hashMapDDO.get(firstLetter);
					new_transferRecord.add(newrecord);
				}else{
					new_transferRecord = new LinkedList<Record>();
					new_transferRecord.add(newrecord);
				}
				synchronized(this){
					CenterServerDDO3.hashMapDDO.put(firstLetter, new_transferRecord);
				}
			}
			
			if(newrecord.m_ID.contains("SR")){
				char firstLetter = newrecord.m_student.m_lastName.charAt(0);
				if(CenterServerDDO3.hashMapDDO.containsKey(firstLetter)){
					new_transferRecord = CenterServerDDO3.hashMapDDO.get(firstLetter);
					new_transferRecord.add(newrecord);
				}else{
					new_transferRecord = new LinkedList<Record>();
					new_transferRecord.add(newrecord);
				}
				synchronized(this){
					CenterServerDDO3.hashMapDDO.put(firstLetter, new_transferRecord);
				}
			}
		}
	}
	
	static class thread extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		byte[] result = new byte[1000];
		public thread(DatagramSocket n_socket, DatagramPacket n_request) {
			this.socket = n_socket;
			this.request = n_request;
			ddocount = getHashSize();
			String DDOcount = Integer.toString(ddocount);
			result= DDOcount.getBytes();
			this.start();
		}
		
		@Override
		public void run() {
			try {
				
				DatagramPacket ddoreply = new DatagramPacket(result, result.length, request.getAddress(), request.getPort()); 
				socket.send(ddoreply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class CreateTR extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		String info = null;
		public CreateTR(DatagramSocket socket, DatagramPacket request, String info) {
			this.socket = socket;
			this.request = request;
			this.info = info;
		}
		@Override
		public void run() {
			
			//process data
			String[] a = info.split(",");
			String prefix = "RP:"+a[9]+":";
			String result = ScreateTRecord(a[2], a[3], a[4], a[5], a[6], a[7], a[8]);
			String result1 = prefix + result;
			System.out.println("==========The request is :" + result1);
			String result2 = "ServerDDO3 has receieved the request and processed it ...";
			//send reply
			if(flag_reply){
				byte[] response = result1.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				byte[] response = result2.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	class CreateSR extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		String info = null;
		public CreateSR(DatagramSocket socket, DatagramPacket request, String info) {
			this.socket = socket;
			this.request = request;
			this.info = info;
		}
		@Override
		public void run() {
			
			//process data
			String[] a = info.split(",");
			String prefix = "RP:"+a[8]+":";
			String result = ScreateSRecord(a[2], a[3], a[4], a[5], a[6], a[7]);
			String result1 = prefix + result;
			System.out.println("==========The request is :" + result1);
			String result2 = "ServerDDO3 has receieved the request and processed it ...";
			//send reply
			if(flag_reply){
				byte[] response = result1.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				byte[] response = result2.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	class GetCount extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		String info = null;
		public GetCount(DatagramSocket socket, DatagramPacket request, String info) {
			this.socket = socket;
			this.request = request;
			this.info = info;
		}
		@Override
		public void run() {
			
			//process data
			String[] a = info.split(",");
			String prefix = "RP:"+a[3]+"::";
			String result = SgetRecordCounts(a[2]);
			String result1 = prefix + result;
			System.out.println("==========The request is :" + result1);
			String result2 = "ServerDDO3 has receieved the request and processed it ...";
			//send reply
			if(flag_reply){
				byte[] response = result1.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				byte[] response = result2.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	class EditRecord extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		String info = null;
		public EditRecord(DatagramSocket socket, DatagramPacket request, String info) {
			this.socket = socket;
			this.request = request;
			this.info = info;
		}
		@Override
		public void run() {
			
			//process data
			String[] a = info.split(",");
			String prefix = "RP:"+a[6]+":";
			String result = SeditRecord(a[2], a[3], a[4], a[5]);
			String result1 = prefix + result;
			System.out.println("==========The request is :" + result1);
			String result2 = "ServerDDO3 has receieved the request and processed it ...";
			//send reply
			if(flag_reply){
				byte[] response = result1.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				byte[] response = result2.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class TransferRecord extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		String info = null;
		public TransferRecord(DatagramSocket socket, DatagramPacket request, String info) {
			this.socket = socket;
			this.request = request;
			this.info = info;
		}
		@Override
		public void run() {
			
			//process data
			String[] a = info.split(",");
			String prefix = "RP:"+a[5]+":";
			String result = StransferRecord(a[2], a[3], a[4]);
			String result1 = prefix + result;
			System.out.println("==========The request is :" + result1);
			String result2 = "ServerDDO3 has receieved the request and processed it ...";
			//send reply
			if(flag_reply){
				byte[] response = result1.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				byte[] response = result2.getBytes();
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
				try {
					socket.send(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public String StransferRecord(String managerID, String recordID, String remoteCenterServerName) {
		if(remoteCenterServerName.equals("MTL") || remoteCenterServerName.equals("LVL") || remoteCenterServerName.equals("DDO")){
			//check if record exists
			for(Map.Entry<Character, LinkedList<Record>> entry : hashMapDDO.entrySet()){
				for(Record record : entry.getValue()){
					if(record.m_ID.equals(recordID)){
						// record exists and remote server exists, transfer record
						Record transfer = record;
						if(remoteCenterServerName.equals("MTL")){
							transferRecordMTL(transfer);
						}
						
						if(remoteCenterServerName.equals("LVL")){
							transferRecordLVL(transfer);
						}
						//if record is teacher record
						if(transfer.m_ID.contains("TR")){
							char firstLetter = transfer.m_teacher.m_lastName.charAt(0);
							
							//remove record from initial server
							new_records = hashMapDDO.get(firstLetter);
							new_records.remove(transfer);
							if(new_records != null){
								synchronized(this){
								hashMapDDO.put(firstLetter, new_records);
								}
							}else{
								hashMapDDO.remove(firstLetter);
							}
						}
						// if record is a student record
						if(transfer.m_ID.contains("SR")){
							char firstLetter = transfer.m_student.m_lastName.charAt(0);
							//remove
							new_records = hashMapDDO.get(firstLetter);
							new_records.remove(transfer);
							if(new_records != null){
								synchronized(this){
								hashMapDDO.put(firstLetter, new_records);
								}
							}else{
								hashMapDDO.remove(firstLetter);
							}
						}
						logger.info("Manager ID: "+managerID+" has successfully transfered record: "+recordID+" to server: "+remoteCenterServerName);
						return "record has been successfully transfered";
					}
				}
			}
			logger.info("Manager ID: "+managerID+" has tried to transfer record to a remote server, but record doesn't exist!");
			return "record doesn't exist";
			
		}else{
			logger.info("Manager ID: "+managerID+" has tried to transfer record to a remote server, but server doesn't exist!");
			return "remoteCenterServer doesn't exist";
		}
	}
	
	public void transferRecordMTL(Record transfer)
	{
		DatagramSocket TransferSocket = null;
		try
		{	//request transfer
			TransferSocket = new DatagramSocket();
			byte[] message = "Transfer".getBytes();
			InetAddress mtlHost = InetAddress.getByName("localhost");
			int MTLServerPort = 6012;
			DatagramPacket mtlrequest = new DatagramPacket(message, message.length, mtlHost, MTLServerPort);
			TransferSocket.send(mtlrequest);
			
			//receive confirmation
			byte[] MTLbuffer = new byte[1000];
			DatagramPacket MTLreply = new DatagramPacket(MTLbuffer, MTLbuffer.length);
			TransferSocket.receive(MTLreply);
			
			//send object
			System.out.println("Sending record to ddo");
			//TransferSocket = new DatagramSocket();
			//InetAddress inet = InetAddress.getByName("localhost");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(transfer);
			//int port = 1010;
			byte[] data = baos.toByteArray();
			DatagramPacket request = new DatagramPacket(data, data.length, mtlHost, MTLServerPort);
			TransferSocket.send(request);
			System.out.println("Record Transfered to MTL");
			
			//get ack
			byte[] indata = new byte[1000];
			DatagramPacket response = new DatagramPacket(indata, indata.length);
			TransferSocket.receive(response);
			String resp = new String(response.getData());
			System.out.println("The response is : " + resp.trim());
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(TransferSocket!=null)
				TransferSocket.close();
		}
		return;
	}
	
	public void transferRecordLVL(Record transfer)
	{
		DatagramSocket TransferSocket = null;
		try
		{
			//request transfer
			TransferSocket = new DatagramSocket();
			byte[] message = "Transfer".getBytes();
			InetAddress lvlHost = InetAddress.getByName("localhost");
			int LVLServerPort = 6022;
			DatagramPacket lvlrequest = new DatagramPacket(message, message.length, lvlHost, LVLServerPort);
			TransferSocket.send(lvlrequest);
			
			//receive confirmation
			byte[] LVLbuffer = new byte[1000];
			DatagramPacket LVLreply = new DatagramPacket(LVLbuffer, LVLbuffer.length);
			TransferSocket.receive(LVLreply);
			
			//send object
			System.out.println("Sending record to lvl");
			//TransferSocket = new DatagramSocket();
			//InetAddress inet = InetAddress.getByName("localhost");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(transfer);
			//int port = 1020;
			byte[] data = baos.toByteArray();
			DatagramPacket request = new DatagramPacket(data, data.length, lvlHost, LVLServerPort);
			TransferSocket.send(request);
			System.out.println("Record Transfered to LVL");
			
			//get ACK
			byte[] indata = new byte[1000];
			DatagramPacket response = new DatagramPacket(indata, indata.length);
			TransferSocket.receive(response);
			String resp = new String(response.getData());
			System.out.println("The response is : " + resp.trim());
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(TransferSocket!=null)
				TransferSocket.close();
		}
		return;
	}
	
	



	
	
	
	
	
}