package Record;
import java.io.Serializable;

public class Record implements Serializable {
	public StudentRecord m_student;
	public TeacherRecord m_teacher;
	public String m_ID;
	
	public Record(StudentRecord student, String ID){
		m_student = student;
		m_ID = ID;
	}
	
	public Record(TeacherRecord teacher, String ID){
		m_teacher = teacher;
		m_ID = ID;
	}
	
	@Override
	public String toString() {
		String str = null;
		switch (m_ID.substring(0, 2)) {
		case "SR":
			str = m_student.getSRecord();
			return str;
		case "TR":
			str = m_teacher.getTRecord();
			return str;
		default:
			return null;
		}
	}
	
	
	
}
