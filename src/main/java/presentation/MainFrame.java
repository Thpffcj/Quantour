package presentation;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author 费慧通
 *Main的加载类
 */
public class MainFrame extends Application {

	//监听主界面关闭
	public class WindowsCloseEvent implements EventHandler<WindowEvent> {

		private Stage stage;

		public WindowsCloseEvent(Stage stage) {
			this.stage = stage;
		}

		public void handle(WindowEvent event) {
			event.consume();

			StageRepertory.setStage(stage);
			new WindowCloseFrame().start(new Stage());
		}
	}

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Stock King");
		primaryStage.setResizable(false);
		this.primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Images/Icon.jpg")));

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/FXML/Main.fxml"));
			AnchorPane frame = (AnchorPane) loader.load();

			Scene scene = new Scene(frame);
			primaryStage.setScene(scene);
			primaryStage.show();

			MainFrameController controller = loader.getController();
			controller.setMainFrame(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//添加监听
		primaryStage.setOnCloseRequest(new WindowsCloseEvent(primaryStage));
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
