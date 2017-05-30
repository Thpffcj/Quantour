package presentation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopFrameController {
	@FXML
	private Label word;
	
	private PopFrame popFrame;

	@FXML
	private void Close(){
		popFrame.getPrimaryStage().close();
	}
	
	@FXML
	private void initialize(){
		word.setText(StageRepertory.gettext());
	}
	
	public void setPopFrame(PopFrame popFrame) {
		this.popFrame = popFrame;
	}

}
