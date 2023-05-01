package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import data.List;
import data.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AttendanceController implements Window {
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private DatePicker since, until;

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

	public void update(ActionEvent event) {

		for (LocalDate date = since.getValue(); date.isBefore(until.getValue())
				|| date.isEqual(until.getValue()); date = date.plusDays(1)) {
			for (Student s : List.students) {
				for (int i = 0;  i < s.getDates().size(); ++i) {
					if (date.isEqual(s.getDates().get(i))) {
						myList.getItems().add(s.getName() + " " + s.getSurname() + " " + s.getGroup() + " " + date + " "
								+ s.getAttendance().get(i));
					}
				}
			}
		}
	}
	
	public void saveToPDF(ActionEvent event) throws Exception {
		
		FileOutputStream fos = new FileOutputStream("results.pdf");
		PdfWriter writer = new PdfWriter(fos);
		PdfDocument pdfDocument = new PdfDocument(writer); 
		Document document = new Document(pdfDocument);
		
		for(String s: myList.getItems()) {
			document.add(new Paragraph(s));
		}
		document.close();
	}
}







