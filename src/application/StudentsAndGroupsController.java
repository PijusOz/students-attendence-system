package application;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import data.List;
import data.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentsAndGroupsController implements Window, Initializable {

	private static Workbook wb;
	private static Sheet sh;
	private static FileInputStream fis;
	private static FileOutputStream fos;
	private static Row row;
	private static Cell cell;
	
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private TextField nameField, surnameField, groupField;

	@FXML
	private ListView<String> myList;

	@Override
	public void switchToMenu(ActionEvent event) {

		try {
			root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	public void submitStudent(ActionEvent event) {

		if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || groupField.getText().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);

			if (nameField.getText().isEmpty()) {
				alert.setHeaderText("Trūksta studento vardo");
			} else if (surnameField.getText().isEmpty()) {
				alert.setHeaderText("Trūksta studento pavardės!");
			} else if (groupField.getText().isEmpty()) {
				alert.setHeaderText("Trūksta studento grupės!");
			}

			alert.show();
		} else {
			List.addStudentToTheList(new Student(nameField.getText(), surnameField.getText(), groupField.getText()));

			myList.getItems().add(nameField.getText() + " " + surnameField.getText() + " " + groupField.getText());
			nameField.clear();
			surnameField.clear();
			groupField.clear();
		}
	}

	public void updateStudent(ActionEvent event) {

		if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || groupField.getText().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);

			if (nameField.getText().isEmpty()) {
				alert.setHeaderText("Trūksta studento vardo");
			} else if (surnameField.getText().isEmpty()) {
				alert.setHeaderText("Trūksta studento pavardės!");
			} else if (groupField.getText().isEmpty()) {
				alert.setHeaderText("Trūksta studento grupės!");
			}

			alert.show();
		} else {
			List.students.get(myList.getSelectionModel().getSelectedIndex()).setName(nameField.getText());
			List.students.get(myList.getSelectionModel().getSelectedIndex()).setSurname(surnameField.getText());
			List.students.get(myList.getSelectionModel().getSelectedIndex()).setGroup(groupField.getText());

			nameField.clear();
			surnameField.clear();
			groupField.clear();

			this.reloadListView();
		}

	}

	public void importCSV(ActionEvent event) {

		BufferedReader reader = null;
		String line = "";

		try {
			reader = new BufferedReader(new FileReader("src\\Students.csv"));
			while ((line = reader.readLine()) != null) {

				String[] row = line.split(",");

				List.addStudentToTheList(new Student(row[0], row[1], row[2]));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.reloadListView();
		}
	}

	public void exportCSV(ActionEvent event) {

		try {
			FileWriter writer = new FileWriter("Students.csv");
			for (Student s : List.students) {
				writer.write(s.getName() + "," + s.getSurname() + "," + s.getGroup() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void importExcel(ActionEvent event) throws Exception {
	
		fis = new FileInputStream("src\\Students.xlsx");
		wb = WorkbookFactory.create(fis);
		sh = wb.getSheet("Sheet1");
		for(int i = 1; i <= sh.getLastRowNum(); ++i) {
			List.students.add(new Student(sh.getRow(i).getCell(0).toString(), sh.getRow(i).getCell(1).toString(),  Integer.toString(((int)Float.parseFloat(sh.getRow(1).getCell(2).toString())))));   
		}
		fis.close();
		
		this.reloadListView();
	}
	
	public void exportExcel(ActionEvent event) throws Exception {
		
		fis = new FileInputStream("Students.xlsx");
		
		int rowNumber = 1;
		
		wb = WorkbookFactory.create(fis);
		sh = wb.getSheet("Sheet1");
		
		for(int i = 1; i <= sh.getLastRowNum(); ++i) {
			
			row = sh.createRow(i);
			cell = row.createCell(0);
			cell.setBlank();
			cell = row.createCell(1);
			cell.setBlank();
			cell = row.createCell(2);
			cell.setBlank();
			
		}
		
		for(Student s: List.students) {
			
			row = sh.createRow(rowNumber);
			++rowNumber;
			cell = row.createCell(0);
			cell.setCellValue(s.getName());
			cell = row.createCell(1);
			cell.setCellValue(s.getSurname());
			cell = row.createCell(2);
			cell.setCellValue(s.getGroup());
		}
		
		fos = new FileOutputStream("Students.xlsx");
		wb.write(fos);
		fos.flush();
		fos.close();
		fis.close();
		
	}

	private void reloadListView() {

		myList.getItems().clear();

		for (Student s : List.students) {
			myList.getItems().add(s.getName() + " " + s.getSurname() + " " + s.getGroup());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		this.reloadListView();

		myList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

				try {
					nameField.setText(List.students.get(myList.getSelectionModel().getSelectedIndex()).getName());
					surnameField.setText(List.students.get(myList.getSelectionModel().getSelectedIndex()).getSurname());
					groupField.setText(List.students.get(myList.getSelectionModel().getSelectedIndex()).getGroup());
				} catch (Exception e) {
				}
				;
			}
		});
	}
}
