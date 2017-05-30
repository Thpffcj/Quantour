package presentation;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartPanel;

import businessLogic.GetComparedData;
import businessLogic.GetKGraphData;
import businessLogic.MarketChanges;
import businessLogic.Users;
import businessLogicService.GetComparedDataService;
import businessLogicService.GetKGraphDataService;
import businessLogicService.MarketChangesService;
import businessLogicService.UsersService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import vo.MarketQuotationVO;
import vo.stockVO;

/**
 * 
 * @author 费慧通
 * 
 *         MainFrame的控制类
 */
public class MainFrameController {
	@FXML
	private ImageView imageView;
	@FXML
	private DatePicker begindate1;
	@FXML
	private DatePicker enddate1;
	@FXML
	private DatePicker begindate2;
	@FXML
	private DatePicker enddate2;
	@FXML
	private DatePicker date;
	@FXML
	private TextField sharesid;
	@FXML
	private TextField sharesid1;
	@FXML
	private TextField sharesid2;
	@FXML
	private StackPane chartpane;
	@FXML
	private Label volume;
	@FXML
	private Label limitup;
	@FXML
	private Label limitdown;
	@FXML
	private Label rose;
	@FXML
	private Label drop;
	@FXML
	private Label openpricerose;
	@FXML
	private Label openpricedrop;
	@FXML
	private StackPane VSChart;
	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private Label text;
	@FXML
	private Label variance;
	@FXML
	private TableView<stockVO> tableView;
	@FXML
	private TableColumn<stockVO, Number> code;
	@FXML
	private TableColumn<stockVO, String> name;
	@FXML
	private StackPane pie;
	@FXML
	private Menu menu;
	@FXML
	private MenuItem cpw;
	@FXML
	private MenuItem upload;
	@FXML
	private MenuItem logout;
	@FXML
	private ImageView photo;
	@FXML
	private AnchorPane backflow;

	private MainFrame mainframe;

	@FXML
	/**
	 * 根据搜索条件生成K线图和均线图
	 */
	private void SearchAction() {
		String beginDate = LocalDateToString(begindate1.getValue());
		String endDate = LocalDateToString(enddate1.getValue());
		String idORname = sharesid.getText();

//		KGraph kGraph = new KGraph();
//		ChartPanel panel = kGraph.GetKGraph(idORname, beginDate, endDate);
//		panel.setPreferredSize(new Dimension(927, 545));
//		final SwingNode swingNode = new SwingNode();
//		swingNode.setContent(panel);
//		chartpane.getChildren().add(swingNode);
	}

	@FXML
	/**
	 * 对比两个股票
	 */
	private void VSAction() {
		String beginDate = LocalDateToString(begindate2.getValue());
		String endDate = LocalDateToString(enddate2.getValue());
		String idOrname1 = sharesid1.getText();
		String idOrname2 = sharesid2.getText();

		chartpane.getChildren().removeAll();
		GetComparedDataService getComparedDataService = new GetComparedData();

		if (getComparedDataService.getLowestValue(idOrname1, idOrname2, beginDate, endDate) == null) {
			StageRepertory.settext("股票未在该时段内同时交易，无法比较");
			new PopFrame().start(new Stage());
		} else {
			comboBox.setVisible(true);
			comboBox.getSelectionModel().select(0);
			// VSChart添加最低值的比较图
			LineChart barChart = new LineChart();
			SwingNode swingNode = new SwingNode();
			swingNode.setContent(barChart.getLowestValueChart(idOrname1, idOrname2, beginDate, endDate));
			VSChart.getChildren().add(swingNode);

			text.setVisible(true);
			variance.setVisible(true);
			text.setText("最低值分别为：");
			variance.setText(getComparedDataService.GetLowest(idOrname1, idOrname2, beginDate, endDate));
		}
	}

	@FXML
	/**
	 * comboBox的监听
	 */
	private void ChangeChart() {
		String beginDate = LocalDateToString(begindate2.getValue());
		String endDate = LocalDateToString(enddate2.getValue());
		String idOrname1 = sharesid1.getText();
		String idOrname2 = sharesid2.getText();

		VSChart.getChildren().removeAll();
		LineChart lineChart = new LineChart();
		GetComparedDataService getComparedDataService = new GetComparedData();

		String option = comboBox.getSelectionModel().getSelectedItem();
		SwingNode swingNode = new SwingNode();

		switch (option) {
		case "最低值":
			swingNode.setContent(lineChart.getLowestValueChart(idOrname1, idOrname2, beginDate, endDate));
			VSChart.getChildren().add(swingNode);
			text.setVisible(true);
			variance.setVisible(true);
			text.setText("最低值分别为：");
			variance.setText(getComparedDataService.GetLowest(idOrname1, idOrname2, beginDate, endDate));
			break;
		case "最高值":
			swingNode.setContent(lineChart.getHighestValueChart(idOrname1, idOrname2, beginDate, endDate));
			VSChart.getChildren().add(swingNode);
			text.setVisible(true);
			variance.setVisible(true);
			text.setText("最高值分别为：");
			variance.setText(getComparedDataService.GetHighest(idOrname1, idOrname2, beginDate, endDate));
			break;
		case "涨幅/跌幅":
			swingNode.setContent(lineChart.getRoseAndDropValueChart(idOrname1, idOrname2, beginDate, endDate));
			VSChart.getChildren().add(swingNode);
			variance.setVisible(true);
			text.setVisible(true);
			text.setText("涨/跌幅分别为：");
			variance.setText(getComparedDataService.getRoseAndDrop(idOrname1, idOrname2, beginDate, endDate));
			break;
		case "收盘价":
			swingNode.setContent(lineChart.getCloseValueChart(idOrname1, idOrname2, beginDate, endDate));
			VSChart.getChildren().add(swingNode);
			variance.setVisible(false);
			text.setVisible(false);
			break;
		case "对数收益率":
			swingNode.setContent(lineChart.getRateOfReturnChart(idOrname1, idOrname2, beginDate, endDate));
			VSChart.getChildren().add(swingNode);
			variance.setVisible(true);
			text.setVisible(true);
			text.setText("对数收益率方差分别为：");
			variance.setText(getComparedDataService.getVariance(idOrname1, idOrname2, beginDate, endDate));
			break;
		}
	}

	@FXML
	/**
	 * 市场情况温度计
	 */
	private void MarketAction() {
		String Date = LocalDateToString(date.getValue());
		MarketChangesService marketChangesService = new MarketChanges(Date);
		MarketQuotationVO vo = marketChangesService.getMarketChanges();
		volume.setText(Long.toString(vo.getTotalOfVolumn()));
		limitup.setText(Integer.toString(vo.getNumOfTrading()));
		limitdown.setText(Integer.toString(vo.getNumOfLimit()));
		rose.setText(Integer.toString(vo.getIncreaseOver5()));
		drop.setText(Integer.toString(vo.getDecreaseOver5()));
		openpricerose.setText(Integer.toString(vo.getOpen_CloseIncreaseOver5()));
		openpricedrop.setText(Integer.toString(vo.getOpen_CloseDecreaseOver5()));

		pieChart pieChart = new pieChart();
		ChartPanel chartPanel = pieChart.getPieChart(vo);
		SwingNode swingNode = new SwingNode();
		swingNode.setContent(chartPanel);
		pie.getChildren().add(swingNode);
	}

	@FXML
	/**
	 * 当鼠标点击输入框时，输入框清空,且转化字体样式
	 * 
	 * @param event
	 */
	private void clearProjectName(MouseEvent event) {
		if (sharesid.getText().equals("股票号/股票名称")) {
			sharesid.clear();
			sharesid.setEffect(new Blend());
		}
	}

	@FXML
	/**
	 * 当第二次选择开始日期时，如果此时开始日期在之前输入的截止日期后，将截止日期置为null
	 */
	private void beginDate() {
		if (enddate1.getValue() != null && begindate1.getValue().isAfter(enddate1.getValue().plusDays(-1))) {
			enddate1.setValue(null);
		}

		if (enddate2.getValue() != null && begindate2.getValue().isAfter(enddate2.getValue().plusDays(-1))) {
			enddate2.setValue(null);
		}
	}

	@FXML
	/**
	 * 截止日期不能位于开始日期之前
	 */
	private void endDate() {
		final Callback<DatePicker, DateCell> dayCellFactory1 = new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if ((begindate1.getValue() != null && item.isBefore(begindate1.getValue().plusDays(1)))
								|| item.isAfter(LocalDate.of(2014, 4, 29)) || item.getDayOfWeek() == DayOfWeek.SATURDAY
								|| item.getDayOfWeek() == DayOfWeek.SUNDAY) {
							setDisable(true);
							setStyle("-fx-background-color:  #d3d3d3;");
						}
					}
				};
			}
		};
		enddate1.setDayCellFactory(dayCellFactory1);

		final Callback<DatePicker, DateCell> dayCellFactory2 = new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if ((begindate2.getValue() != null && item.isBefore(begindate2.getValue().plusDays(1)))
								|| item.isAfter(LocalDate.of(2014, 4, 29)) || item.getDayOfWeek() == DayOfWeek.SATURDAY
								|| item.getDayOfWeek() == DayOfWeek.SUNDAY) {
							setDisable(true);
							setStyle("-fx-background-color:  #d3d3d3;");
						}
					}
				};
			}
		};
		enddate2.setDayCellFactory(dayCellFactory2);
	}

	@FXML
	/**
	 * 点击tableview中的某一行，显示相应的K线图
	 * 
	 * @param event
	 */
	private void ChooseTable(MouseEvent event) {
		stockVO vo = tableView.getSelectionModel().getSelectedItem();
		sharesid.setText(vo.getName());
		sharesid.setEffect(new Blend());
		String beginDate = LocalDateToString(begindate1.getValue());
		String endDate = LocalDateToString(enddate1.getValue());

//		KGraph kGraph = new KGraph();
//		ChartPanel panel = kGraph.GetKGraph(vo.getName(), beginDate, endDate);
//		panel.setPreferredSize(new Dimension(927, 545));
//		final SwingNode swingNode = new SwingNode();
//		swingNode.setContent(panel);
//		chartpane.getChildren().add(swingNode);
	}

	@FXML
	/**
	 * 修改密码的监听
	 */
	private void ChangePassword() {
		StageRepertory.setStage(mainframe.getPrimaryStage());
		new ChangePasswordFrame().start(new Stage());
	}

	@FXML
	private void UploadPhoto() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
		File file = fileChooser.showOpenDialog(mainframe.getPrimaryStage());
		if (file != null) {
			photo.setImage(new Image("file:" + file.getPath()));
			Image image = photo.getImage();
			File file2 = new File(getClass().getClassLoader().getResource("User").getPath() + "/"
					+ StageRepertory.getusername() + ".png");
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file2);
			} catch (IOException e) {
				e.printStackTrace();
			}

			UsersService service = new Users();
			service.UploadPhoto(StageRepertory.getusername(), StageRepertory.getusername());
		}
	}

	@FXML
	private void LogOut() {
		UsersService service = new Users();
		service.Logout(StageRepertory.getusername());
		mainframe.getPrimaryStage().close();
		new LogFrame().start(new Stage());
	}

	@FXML
	/**
	 * 初始化
	 */
	private void initialize() throws IOException {
		// 用户部分
		// UserService service1 = new User();
		// photo.setImage(new
		// Image(service1.GetPhoto(StageRepertory.getusername())));
		// menu.setText(StageRepertory.getusername());

		// 设置组件初始值
		begindate1.setValue(LocalDate.of(2014, 1, 28));
		enddate1.setValue(LocalDate.of(2014, 4, 29));
		begindate2.setValue(LocalDate.of(2014, 1, 28));
		enddate2.setValue(LocalDate.of(2014, 4, 29));
		date.setValue(LocalDate.of(2014, 4, 29));
		sharesid.setText("深发展A");
		sharesid1.setText("1");
		sharesid2.setText("10");

		ObservableList<String> options = FXCollections.observableArrayList("最低值", "最高值", "涨幅/跌幅", "收盘价", "对数收益率");
		comboBox.getItems().addAll(options);
		SearchAction();
		VSAction();
		MarketAction();

		// 日期选择器去除非工作日
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isAfter(LocalDate.of(2014, 4, 29)) || item.getDayOfWeek() == DayOfWeek.SATURDAY
								|| item.getDayOfWeek() == DayOfWeek.SUNDAY) {
							setDisable(true);
							setStyle("-fx-background-color:  #d3d3d3;");
						}
					}
				};
			}
		};
		begindate1.setDayCellFactory(dayCellFactory);
		enddate1.setDayCellFactory(dayCellFactory);
		begindate2.setDayCellFactory(dayCellFactory);
		enddate2.setDayCellFactory(dayCellFactory);
		date.setDayCellFactory(dayCellFactory);

		// table view 初始化
//		GetKGraphDataService service = new GetKGraphData();
//		ArrayList<stockVO> arrayList = service.getCodeAndName();
//		ObservableList<stockVO> list = FXCollections.observableArrayList(arrayList);
//		tableView.setItems(list);

		code.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
		name.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

		// Logo显示
		imageView.setImage(new Image("file:src/main/resources/Images/Icon.jpg"));

		// 股票回测
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/FXML/BackFlow.fxml"));
		AnchorPane pane = (AnchorPane) loader.load();
		backflow.getChildren().addAll(pane);
	}

	/**
	 * 将LocalDate转化为“m/dd/yy”形式
	 * 
	 * @param localDate
	 * @return
	 */
	public String LocalDateToString(LocalDate localDate) {
		int year = localDate.getYear() % 100;
		int mouth = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();
		if (year < 10) {
			return mouth + "/" + day + "/0" + year;
		} else {
			return mouth + "/" + day + "/" + year;
		}
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainframe = mainFrame;
	}

}
