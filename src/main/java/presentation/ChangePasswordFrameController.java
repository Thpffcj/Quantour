package presentation;

import businessLogic.Users;
import businessLogicService.UsersService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class ChangePasswordFrameController {
	@FXML
	private PasswordField old;
	@FXML
	private PasswordField new1;
	@FXML
	private PasswordField new2;
	@FXML
	private Label word;
	
	private ChangePasswordFrame frame;
	
	@FXML
	private void Change(){
		String oldpw = old.getText();
		String newpw1 = new1.getText();
		String newpw2 = new2.getText();
		
		if(!newpw1.equals(newpw2)){
			word.setVisible(true);
		}else{
			word.setVisible(false);
			UsersService service = new Users();
//			boolean result = service.ChangePassword(StageRepertory.getusername(), oldpw, newpw1);
//			if(result){
//				frame.getPrimaryStage().close();
//				StageRepertory.getStage().close();
//				new LogFrame().start(new Stage());
//			}else{
//				StageRepertory.settext("密码修改失败");
//				new PopFrame().start(new Stage());
//			}
			
		}
	}
	
	@FXML
	private void Cancel(){
		frame.getPrimaryStage().close();
	}

	public void setChangePasswordFrame(ChangePasswordFrame changePasswordFrame) {
		this.frame = changePasswordFrame;
	}

}
