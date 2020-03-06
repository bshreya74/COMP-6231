package ClientSide;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import DCMSApp.DCMS;
import DCMSApp.DCMSHelper;
import Record.Record;
import ServerSide.first.CenterServerDDO1;
import ServerSide.first.CenterServerLVL1;
import ServerSide.first.CenterServerMTL1;
import ServerSide.first.ReplicaManager1;

public class ManagerSetup {
	
	public String managerID;
	public Logger loggerManager;
	public FileHandler fileManager;
	public boolean validation;
	public DCMS serverImpl;

	
	public ManagerSetup(String id) {
		this.managerID = id;
	}
	
	
	public void logManager(String managerID){
		try {
			loggerManager = Logger.getLogger(ManagerClient.class.getName());
			loggerManager.setUseParentHandlers(false);
			fileManager = new FileHandler(managerID+".log");
			loggerManager.addHandler(fileManager);
			SimpleFormatter format = new SimpleFormatter();
			fileManager.setFormatter(format);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkID(String id){
		for(String list: CenterServerMTL1.managers){
			if(list.equals(id)){
				CenterServerMTL1.managerID = id;
				System.out.println("mtl manager id is: "+CenterServerMTL1.managerID);
				return true;
			}
		}
		for(String list: CenterServerLVL1.managers){
			if(list.equals(id)){
				CenterServerLVL1.managerID = id;
				System.out.println("lvl manager id is: "+CenterServerLVL1.managerID);
				return true;
			}
		}
		for(String list: CenterServerDDO1.managers){
			if(list.equals(id)){
				CenterServerDDO1.managerID = id;
				System.out.println("mtl manager id is: "+CenterServerDDO1.managerID);
				return true;
			}
		}
		
		return false;
	}
	
	public void servant(String id){
		
			try{
				
				ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "1050"}, null);	
				org.omg.CORBA.Object objRef = 
				orb.resolve_initial_references("NameService");	 
				NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
				String name = "FrontEnd";
				serverImpl = DCMSHelper.narrow(ncRef.resolve_str(name));
			} catch (Exception e) {
				System.out.println("ERROR : " + e) ;
				e.printStackTrace(System.out);
			}

	}
	
	
	
	
	public String createTRecord(String managerID, String firstName, String lastName,String address,String phone,String specialization,String location){
		String result = serverImpl.createTRecord(managerID, firstName, lastName, address, phone, specialization, location);
		return result;
	}
	
	public String createSRecord(String managerID, String firstName, String lastName, String coursesRegistered, String status, String statusDate) {
		String result = serverImpl.createSRecord(managerID, firstName, lastName, coursesRegistered, status, statusDate);
		return result;
	}

	public String getRecordCounts(String managerID) {
		String result = serverImpl.getRecordCounts(managerID);
		return result;
	}

	public String editRecord(String managerID, String recordID, String fieldName, String newValue) {
		String result = serverImpl.editRecord(managerID, recordID, fieldName, newValue);
		return result;
	}

	public String transferRecord(String managerID, String recordID, String remoteServerName) {
		String result = serverImpl.transferRecord(managerID, recordID, remoteServerName);
		return result;
	}
	
	
	
	
	
}
