package ClientSide;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import DCMSApp.DCMS;
import DCMSApp.DCMSHelper;




public class ManagerClient {
	
	public static String input;

	
	public static void main(String args[]){
		String managerID;
		Boolean validation;
		System.out.println("****Please input the manager ID****");
		Scanner keyboard = new Scanner(System.in);
		managerID = keyboard.next();
		ManagerSetup manager = new ManagerSetup(managerID);
		validation = manager.checkID(managerID);
	
		while(!validation){
			System.out.println("Manager ID is wrong !");
			System.out.println("****Please input the manager ID****");
			managerID = keyboard.next();
			validation = manager.checkID(managerID);
		}
		
		if(validation){
			System.out.println("Manager ID is valid !");
			manager.servant(managerID);
			manager.logManager(managerID);
			manager.loggerManager.info("ManagerID: "+ managerID + " logged in DCMS."+'\n');
		}
		
		//check menu
		do{
			System.out.println("****DSMS Manager Client System****");
			System.out.println("****Manager: "+managerID +"****");
			System.out.println("Please select an option by entering the corresponding number :");
			System.out.println("1.Create A Teacher Record");
			System.out.println("2.Create A Student Record");
			System.out.println("3.Get Record Counts");
			System.out.println("4.Edit Record");
			System.out.println("5.Transfer A Record");
			System.out.println("6.Exit");
			keyboard = new Scanner(System.in);
			input=keyboard.nextLine();
			
			switch(input){
			case "1":
				System.out.println("Please input first name(eg:Zac):");
				String firstName = keyboard.nextLine();
				System.out.println("Please input last name(eg:Chen):");
				String lastName = keyboard.nextLine();
				System.out.println("Please input address:");
				String address = keyboard.nextLine();
				System.out.println("Please input phone number:");
				String phone = keyboard.nextLine();
				System.out.println("Please input specialization:");
				String specialization = keyboard.nextLine();
				System.out.println("Please input location(mtl/lvl/ddo):");
				String location= keyboard.nextLine();
				//System.out.println("Before: "+CenterServerMTL.managerID);
				String feedback1 = manager.createTRecord(managerID, firstName, lastName, address, phone, specialization, location);
				//System.out.println("After: "+CenterServerMTL.managerID);
				System.out.println("Manager has tried to created a teacher record"+'\n'+feedback1+'\n');
				manager.loggerManager.info("Manager has tried to created a teacher record"+'\n'+feedback1+'\n');
				break;
	
			case "2":
				System.out.println("Please input first name(eg:Zac):");
				String firstname = keyboard.nextLine();
				System.out.println("Please input last name(eg:Chen):");
				String lastname = keyboard.nextLine();
				System.out.println("Please input the registered course:");
				String coursesRegistered = keyboard.nextLine();
				System.out.println("Please input status(active/inactive)");
				String status = keyboard.nextLine();
				System.out.println("Please input status date(yyyy/mm/dd/)");
				String statusDate = keyboard.nextLine();
				String feedback2 =  manager.createSRecord(managerID, firstname, lastname, coursesRegistered, status, statusDate);
				System.out.println("Manager has tried created a student record"+'\n'+feedback2+'\n');
				manager.loggerManager.info("Manager has tried created a student record"+'\n'+feedback2+'\n');
				break;
	
	
			case "3":
				System.out.println("****Counting records from all servers****");
				String feedback3 =  manager.getRecordCounts(managerID);
				System.out.println(feedback3);
				manager.loggerManager.info("Manager has requested to know the number of all the records from all the servers. The result is:"+'\n'+feedback3+'\n');
				break;
	
	
			case "4":
				System.out.println("Please input the recordID");
				String recordID = keyboard.nextLine();
				System.out.println("Please input the fieldName");
				String fieldName = keyboard.nextLine();
				System.out.println("Please input the newValue");
				String newValue = keyboard.nextLine();
				String feedback4 =  manager.editRecord(managerID, recordID, fieldName, newValue);
				System.out.println("Manager has editted a record"+'\n'+feedback4+'\n');
				manager.loggerManager.info("Manager has editted a record"+'\n'+feedback4+'\n');
				break;
			
			case "5":
				System.out.println("Please input the recordID");
				String ID = keyboard.nextLine();
				System.out.println("Please input the remote center server name");
				String serverName = keyboard.nextLine();
				String feedback5 =  manager.transferRecord(managerID, ID, serverName);
				System.out.println("Manager has tried to transfer a record"+'\n'+feedback5+'\n');
				manager.loggerManager.info("Manager has tried to transfer a record"+'\n'+feedback5+'\n');
				break;
				
			case "6":
				System.out.println("Thanks for using DSMS system!!");
				manager.loggerManager.info("Manager has logged out the DSMS"+'\n');
				keyboard.close();
				System.exit(0);
				break;
				
			
				
			default:
				System.out.println("Input option is invalided, please input the correct option number !!");
				manager.loggerManager.info("Manager's input option is invalid"+'\n');
			}
		
		
		
		}while(!input.equals("6"));
		
		System.out.println("Thanks for using DSMS system!!");
		manager.loggerManager.info("Manager has logged out the DSMS"+'\n');
		keyboard.close();
		System.exit(0);
		
	}
}
