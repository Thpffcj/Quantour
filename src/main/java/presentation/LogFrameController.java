package presentation;

import businessLogic.Users;
import businessLogicService.UsersService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LogFrameController {
	@FXML
	private ImageView imageView;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;

	private LogFrame logFrame;

	@FXML
	/**
	 * 实现登录
	 */
	private void Login() {
		String name = username.getText();
		String pw = password.getText();

		UsersService service = new Users();

//		boolean result = service.Login(name, pw);
		boolean result = true;
		if (result) {
			StageRepertory.setusername(name);
			new MainFrame().start(new Stage());
			logFrame.getPrimaryStage().close();
		}else{
			StageRepertory.settext("用户名或密码错误");
			new PopFrame().start(new Stage());
		}
	}

	@FXML
	/**
	 * 初始化
	 * @param logFrame
	 */
	private void initialize(){
		imageView.setImage(new Image("file:src/main/resources/Images/Icon.jpg"));
	}
	
	
	public void setLogFrame(LogFrame logFrame) {
		this.logFrame = logFrame;
	}

}
