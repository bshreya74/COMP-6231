package ServerSide.first;

public interface ServerInterface {
	
	 //create teacher record
	  String ScreateTRecord (String managerID, String firstName, String lastName, String address, String phone, String specialization, String location);

	  //create student record
	  String ScreateSRecord (String managerID, String firstName, String lastName, String courseRegistered, String status, String statusDate);

	  //get record counts
	  String SgetRecordCounts (String managerID);

	  //edit record
	  String SeditRecord (String managerID, String recordID, String fieldName, String newValue);

	  //transfer record to another server
	  String StransferRecord (String managerID, String recordID, String remoteCenterServerName);
	
	
}
