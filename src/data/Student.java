package data;

import java.time.LocalDate;
import java.util.ArrayList;

public class Student {

	String name, surname, group;
	ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
	ArrayList<String> attendance = new ArrayList<String>();

	public Student(String name, String surname, String group) {
		this.name = name;
		this.surname = surname;
		this.group = group;
	}

	public ArrayList<LocalDate> getDates() {
		return dates;
	}

	public void addDate(LocalDate myLocalDate) {
		dates.add(myLocalDate);
	}

	public ArrayList<String> getAttendance() {
		return attendance;
	}

	public void addAttandance(String att) {
		attendance.add(att);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
