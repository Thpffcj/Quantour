package presentation;

import java.util.ArrayList;
import java.util.Iterator;

import businessLogic.GetKGraphData;
import businessLogicService.GetKGraphDataService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import vo.stockVO;

public class ChooseStockFrameController {

	public class ChooseCell extends TableCell<stockVO, Boolean> {

		final CheckBox cell = new CheckBox();

		ChooseCell() {
			{
				setAlignment(Pos.CENTER);
			}
//			cell.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent t) {
//					boolean ischosen = cell.isSelected();
//					stockVO vo = getTableView().getItems().get(getIndex());
//					if (ischosen) {
//						codelist.add(vo.getName());
//						updateListView();
//					} else {
//						String Name = vo.getName();
//						Iterator<String> iterator = codelist.iterator();
//						while (iterator.hasNext()) {
//							if (iterator.next().equals(Name)) {
//								iterator.remove();
//							}
//						}
//						updateListView();
//					}
//					number.setText("已选择：" + codelist.size() + "/100");
//					if (codelist.size() >= 100) {
//						button.setDisable(false);
//					} else {
//						button.setDisable(true);
//					}
//				}
//			});
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);

			if (!empty) {
				int i = StageRepertory.geti();
				if (i < 100) {
					cell.setSelected(true);
					i++;
					StageRepertory.seti(i);
				} else {
					cell.setSelected(false);
				}
				setGraphic(cell);
			}
		}
	}

	@FXML
	private TableView<stockVO> tableview;
	@FXML
	private TableColumn<stockVO, Boolean> chosen;
	@FXML
	private TableColumn<stockVO, Number> code;
	@FXML
	private TableColumn<stockVO, String> name;
	@FXML
	private Label number;
	@FXML
	private CheckBox checkBox;
	@FXML
	private Button button;
	@FXML
	private ListView<String> listView;

	private ChooseStockFrame frame;

	private ArrayList<String> codelist = new ArrayList<>();

	@FXML
	/**
	 * 复选框的监听
	 */
//	private void ChooseCheckBox() {
//		GetKGraphDataService service = new GetKGraphData();
//		ArrayList<stockVO> arrayList = service.getCodeAndName();
//		
//		boolean result = checkBox.isSelected();
//		if (result) {
//			StageRepertory.seti(0);
//
//			button.setDisable(false);
//			number.setText("已选择：100/100");
//			codelist.clear();
//			for (int i = 0; i < arrayList.size(); i++) {
//				if (i < 100) {
//					codelist.add(arrayList.get(i).getName());
//				}
//			}
//			updateListView();
//		} else {
//			StageRepertory.seti(100);
//			button.setDisable(true);
//			number.setText("已选择：0/100");
//			codelist.clear();
//			updateListView();
//		}
//		ObservableList<stockVO> list = FXCollections.observableArrayList(arrayList);
//		tableview.getItems().clear();
//		tableview.setItems(list);
//		code.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
//		name.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
//		chosen.setCellFactory(new Callback<TableColumn<stockVO, Boolean>, TableCell<stockVO, Boolean>>() {
//			@Override
//			public TableCell<stockVO, Boolean> call(TableColumn<stockVO, Boolean> p) {
//				return new ChooseCell();
//			}
//		});
//		
//	}

//	@FXML
	/**
	 * 取消的监听
	 */
	private void Cancel() {
		frame.getPrimaryStage().close();
	}

	@FXML
	/**
	 * 完成的监听
	 */
	private void Finish() {
		// 将选好的股票池传给逻辑层
		StageRepertory.setcodelist(codelist);
		frame.getPrimaryStage().close();
	}
	
	public void updateListView(){
		ObservableList<String> observableList = FXCollections.observableArrayList(codelist);
		listView.setItems(observableList);
	}

	@FXML
	/**
	 * 初始化
	 */
//	private void initialize() {
//		GetKGraphDataService service = new GetKGraphData();
//		ArrayList<stockVO> arrayList = service.getCodeAndName();
//		ObservableList<stockVO> list = FXCollections.observableArrayList(arrayList);
//		tableview.setItems(list);
//
//		code.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
//		name.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
//		StageRepertory.seti(100);
//		chosen.setCellFactory(new Callback<TableColumn<stockVO, Boolean>, TableCell<stockVO, Boolean>>() {
//			@Override
//			public TableCell<stockVO, Boolean> call(TableColumn<stockVO, Boolean> p) {
//				return new ChooseCell();
//			}
//		});
//	}

	public void setChooseStockFrame(ChooseStockFrame chooseStockFrame) {
		this.frame = chooseStockFrame;
	}

}
