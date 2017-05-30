package presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ProgressFrame extends Application{
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Stock King");
		primaryStage.setResizable(false);
		this.primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Images/Icon.jpg")));

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/FXML/Progress.fxml"));
			AnchorPane frame = (AnchorPane) loader.load();

			Scene scene = new Scene(frame);
			primaryStage.setScene(scene);
			primaryStage.show();

			ProgressFrameController controller = loader.getController();
			controller.setProgressFrame(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
