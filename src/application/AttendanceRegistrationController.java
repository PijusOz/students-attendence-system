package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

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
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AttendanceRegistrationController implements Window, Initializable {

	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean isGroupEmpty, isDateFound;
	private LocalDate myLocalDate;

	@FXML
	private TextField groupField, myTextField;

	@FXML
	private DatePicker myDatePicker;

	@FXML
	private ListView<String> myListView;

	@FXML
	private RadioButton yes, no;

	@Override
	public void switchToMenu(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void searchForGroup(ActionEvent event) {

		try {
			myDatePicker.getValue().equals(myDatePicker.getValue());
			if (groupField.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Trūksta grupės!");
				alert.show();
			} else {

				myListView.getItems().clear();

				isGroupEmpty = true;

				for (Student s : List.students) {
					if (s.getGroup().equals(groupField.getText())) {
						isGroupEmpty = false;
						isDateFound = false;
						for (int i = 0; i < s.getDates().size(); ++i) {

							if (s.getDates().get(i).isEqual(myDatePicker.getValue())) {

								isDateFound = true;
								myListView.getItems().add(s.getName() + " " + s.getSurname() + " " + s.getGroup() + " "
										+ s.getAttendance().get(i));
								break;

							}
						}
						if (!isDateFound) {
							myListView.getItems().add(s.getName() + " " + s.getSurname() + " " + s.getGroup());
						}
					}
				}
			}
			if (isGroupEmpty) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Tokios grupės nėra!");
				alert.show();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Nepasirinkta data!");
			alert.show();
		}
	}

	public void update(ActionEvent event) {

		myLocalDate = myDatePicker.getValue();

		for (Student s : List.students) {

			if ((s.getName() + " " + s.getSurname() + " " + s.getGroup()).equals(myTextField.getText())) {

				s.addDate(myLocalDate);

				if (yes.isSelected()) {
					s.addAttandance("Dalyvavo");
				} else if (no.isSelected()) {
					s.addAttandance("Nedalyvavo");
				}
				break;
			}
		}

		myListView.getItems().clear();

		for (Student s : List.students) {
			if (s.getGroup().equals(groupField.getText())) {
				isDateFound = false;
				for (int i = 0; i < s.getDates().size(); ++i) {

					if (s.getDates().get(i).isEqual(myDatePicker.getValue())) {

						isDateFound = true;
						myListView.getItems().add(s.getName() + " " + s.getSurname() + " " + s.getGroup() + " "
								+ s.getAttendance().get(i));
						break;

					}
				}
				if (!isDateFound) {
					myListView.getItems().add(s.getName() + " " + s.getSurname() + " " + s.getGroup());
				}
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				myTextField.setText(myListView.getSelectionModel().getSelectedItem());
			}
		});
	}
}
