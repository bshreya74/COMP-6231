package Record;

import java.util.LinkedList;
import java.io.Serializable;

public class StudentRecord implements Serializable{
	
	public String m_firstName;
	public String m_lastName;
	public LinkedList<String> m_coursesRegistered = new LinkedList<String>();
	public String m_status;
	public String m_statusDate;
	public String m_ID;
	
	public StudentRecord(String firstName, String lastName, String coursesRegistered, String status, String statusDate, String ID){
		
		
		m_firstName = firstName;
		m_lastName = lastName;
		m_coursesRegistered.add(coursesRegistered);
		m_status = status;
		m_statusDate = statusDate;
		m_ID = ID;

		
		
	}
	

	public void setFirstName(String firstName) {
		m_firstName = firstName;
	}
	
	public String getFirstName() {
		return m_firstName;
	}
	
	public void setLastName(String lastName) {
		m_lastName = lastName;
	}
	
	public String getLastName() {
		return m_lastName;
	}
	
	public void setcourseRegistered(String coursesRegistered) {
		m_coursesRegistered.add(coursesRegistered);
	}
	
	public void setStatus(String status) {
		m_status = status;
	}
	public void setStatusDate(String statusDate) {
		m_statusDate = statusDate;
	}
	
	public void setID(String ID){
		m_ID = ID;
	}
	public String getID(){
		return m_ID;
	}
	
	public String getcoursesRegistered(){
		String a = ": ";
		for(int i = 0; i < m_coursesRegistered.size(); i++){
			a = a + m_coursesRegistered.get(i)+", ";
		}
		return a;
	}
	public String getStatusDate() {
		
		return m_statusDate;
	}


	public String getStatus() {
		
		return m_status;
	}

	
	public String getSRecord() {
		String str = "First Name: "+ getFirstName() + "\n" 
					+ "Last Name: " + getLastName() + "\n"
					+ "CoursesRegistered" + getcoursesRegistered() + "\n"
					+ "Status: " + getStatus() + "\n"
					+ "StatusDate: " + getStatusDate() + "\n" + "ID: "+ getID() + "\n";
		return str;
	}




	
	
	
	
}
