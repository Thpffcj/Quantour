package presentation;

import javafx.fxml.FXML;

/**
 * 
 * @author 费慧通
 * WindowCloseFrame的控制类
 */
public class WindowCloseFrameController {
	
	private WindowCloseFrame windowCloseFrame;
	
	@FXML
	/**
	 * 按钮“是”的监听
	 */
	private void Confirm(){
		windowCloseFrame.getPrimaryStage().close();
		StageRepertory.getStage().close();
	}
	
	@FXML
	/**
	 * 按钮“否”的监听
	 */
	private void Cancel(){
		windowCloseFrame.getPrimaryStage().close();
	}

	public void WindowCloseFrame(WindowCloseFrame windowCloseFrame) {
		this.windowCloseFrame = windowCloseFrame;		
	}

}
