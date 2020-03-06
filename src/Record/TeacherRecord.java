package Record;
import java.io.Serializable;

public class TeacherRecord implements Serializable {
	
	public String m_firstName;
	public String m_lastName;
	public String m_address;
	public String m_phone;
	public String m_specialization;
	public String m_location;
	public String m_ID;
	
	public TeacherRecord(String firstName, String lastName, String address, String phone, String specialization, String location, String ID){
		m_firstName = firstName;
		m_lastName = lastName;
		m_address = address;
		m_phone = phone;
		m_specialization = specialization;
		m_location = location;
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
	
	public void setAddress(String address) {
		m_address = address;
	}
	
	public String getAddress() {
		return m_address;
	}
	
	public void setPhone(String phone) {
		m_phone = phone;
	}
	
	public String getPhone() {
		return m_phone;
	}
	
	public void setSpecialization(String specialization) {
		m_specialization = specialization;
	}
	
	public String getSpecialization() {
		return m_specialization;
	}
	
	public void setLocation(String location) {
		m_location = location;
	}
	
	public String getLocation() {
		return m_location;
	}
	
	
	public void setID(String ID){
		m_ID = ID;
	}
	public String getID(){
		return m_ID;
	}
	
	public String getTRecord() {
		String str = "First Name: "+ getFirstName() + "\n" 
					+ "Last Name: " + getLastName() + "\n"
					+ "Address: " + getAddress() + "\n"
					+ "Phone: " + getPhone() + "\n"
					+ "Specialization: " + getSpecialization() + "\n" 
					+ "Location: " + getLocation() + "\n" 
					+"ID: "+ getID() + "\n";
		return str;
	}

	
}
