package presentation;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ListViewFrameController {
	@FXML
	private ListView<String> list;
	
	private ListViewFrame frame;

	@FXML
	private void OK(){
		frame.getPrimaryStage().close();
	}
	
	@FXML
	private void initialize(){
		ArrayList<String> codelist = StageRepertory.getcodelist();
		ObservableList<String> List = FXCollections.observableArrayList(codelist);
		list.setItems(List);
	}
	
	public void setListViewFrame(ListViewFrame listViewFrame) {
		this.frame = listViewFrame;
	}

}
