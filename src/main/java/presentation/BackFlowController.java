package presentation;

import java.awt.Dimension;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;

import businessLogic.GetKGraphData;
import businessLogicService.GetKGraphDataService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import presentation.quantify.BollBackTestGraph;
import presentation.quantify.BollGraph;
import presentation.quantify.KDJStochasticBackTestGraph;
import presentation.quantify.KDJStochasticGraph;
import presentation.quantify.RSIBackTestGraph;
import presentation.quantify.RSIGraph;
import vo.WayVO;
import vo.stockVO;

public class BackFlowController {
	@FXML
	private ComboBox<String> stocklist;
	@FXML
	private ComboBox<String> way;
	@FXML
	private DatePicker time1;
	@FXML
	private DatePicker time2;
	@FXML
	private Label word;
	@FXML
	private ComboBox<String> EMA;
	@FXML
	private Slider slider;
	@FXML
	private Label formation;
	@FXML
	private Slider slider1;
	@FXML
	private Label holding;
	@FXML
	private TextField number;
	@FXML
	private StackPane pane;
	@FXML
	private Label label1;
	@FXML
	private Label label2;
	@FXML
	private Label label3;
	@FXML
	private ProgressIndicator indicator;
	@FXML
	private Label text;
	@FXML
	private Hyperlink link;
	@FXML
	private Label xword;
	@FXML
	private Label yword;
	@FXML
	private CheckBox box;

	@FXML
	/**
	 * 根据不同的策略显示不同的输入框
	 */
	private void changeword() {
		String item = way.getSelectionModel().getSelectedItem();
		if (item != null && item.equals("动量策略")) {
			word.setText("形成期：");
			slider.setVisible(true);
			formation.setVisible(true);
			word.setVisible(true);
			xword.setVisible(true);
			slider1.setVisible(true);
			holding.setVisible(true);
			yword.setVisible(true);
			number.setVisible(true);
			EMA.setVisible(false);
			box.setVisible(true);
		} else if (item != null && item.equals("均值回归")) {
			word.setText("均线：");
			word.setVisible(true);
			xword.setVisible(true);
			slider1.setVisible(true);
			holding.setVisible(true);
			yword.setVisible(true);
			number.setVisible(true);
			slider.setVisible(false);
			formation.setVisible(false);
			EMA.setVisible(true);
			box.setVisible(false);
		} else if(item != null){
			word.setVisible(false);
			slider.setVisible(false);
			formation.setVisible(false);
			xword.setVisible(false);
			slider1.setVisible(false);
			holding.setVisible(false);
			yword.setVisible(false);
			number.setVisible(false);
			box.setVisible(false);
		}
	}

//	@FXML
//	/**
//	 * 自定义股票池
//	 */
//	private void Custom() {
//		String option = stocklist.getSelectionModel().getSelectedItem();
//		ArrayList<String> list = new ArrayList<>();
//		GetKGraphDataService service = new GetKGraphData();
//		if (option != null) {
//			if (option.equals("自定义")) {
//				new ChooseStockFrame().start(new Stage());
//			} else if (option.equals("全部")) {
//				ArrayList<stockVO> voList = service.getCodeAndName();
//				for (int i = 0; i < voList.size(); i++) {
//					list.add(voList.get(i).getName());
//				}
//			} else {
//				ArrayList<stockVO> voList = service.getCodeAndNameByPlate(option);
//				for (int i = 0; i < voList.size(); i++) {
//					list.add(voList.get(i).getName());
//				}
//			}
//		}
//		StageRepertory.setcodelist(list);
//	}

	@FXML
	/**
	 * 查看已选的股票
	 */
	private void lookcode() {
		new ListViewFrame().start(new Stage());
	}

	@FXML
	/**
	 * 得到形成期
	 */
	private void getFormation() {
		if (slider.getValue() == 0) {
			formation.setText("0天");
		} else {
			formation.setText((int) (slider.getValue()) + "天");
		}
	}

	@FXML
	/**
	 * 得到持有期
	 */
	private void getHolding() {
		holding.setText((int) (slider1.getValue()) + "天");
	}

	@FXML
	/**
	 * 根据前后日期选择slider1的最大值
	 */
	private void changeSliderMax() {
		if (time2.getValue() != null && time1.getValue().isAfter(time2.getValue().plusDays(-1))) {
			time2.setValue(null);
		}

		final Callback<DatePicker, DateCell> dayCellFactory3 = new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if ((time1.getValue() != null && item.isBefore(time1.getValue().plusDays(1)))
								|| item.isAfter(LocalDate.of(2014, 4, 29)) || item.getDayOfWeek() == DayOfWeek.SATURDAY
								|| item.getDayOfWeek() == DayOfWeek.SUNDAY) {
							setDisable(true);
							setStyle("-fx-background-color:  #d3d3d3;");
						}
					}
				};
			}
		};
		time2.setDayCellFactory(dayCellFactory3);

		if (time1.getValue() != null && time2.getValue() != null) {
			int days = (int) Math.abs(time1.getValue().toEpochDay() - time2.getValue().toEpochDay());
			slider1.setMax(days);
		}
	}

//	@FXML
//	/**
//	 * 开始回测的监听
//	 */
//	private void BackFlow() throws IOException {
//		ArrayList<String> codelist = new ArrayList<>();
//		String option = stocklist.getSelectionModel().getSelectedItem();
//		GetKGraphDataService service = new GetKGraphData();
//		if (option != null) {
//			if (option.equals("全部")) {
//				ArrayList<stockVO> voList = service.getCodeAndName();
//				for (int i = 0; i < voList.size(); i++) {
//					codelist.add(voList.get(i).getName());
//				}
//			} else if (option.equals("自定义")) {
//				codelist = StageRepertory.getcodelist();
//			} else {
//				ArrayList<stockVO> voList = service.getCodeAndNameByPlate(option);
//				for (int i = 0; i < voList.size(); i++) {
//					codelist.add(voList.get(i).getName());
//				}
//			}
//		}
//		if (codelist.isEmpty()) {
//			StageRepertory.settext("股票池不能为空!");
//			new PopFrame().start(new Stage());
//			return ;
//		}
//		if(number.getText().equals("")){
//			StageRepertory.settext("必须输入持有股票数!");
//			new PopFrame().start(new Stage());
//			return ;
//		}
//		int shares = Integer.valueOf(number.getText());
//		String begin = LocalDateToString(time1.getValue());
//		String end = LocalDateToString(time2.getValue());
//		String emachoice = EMA.getSelectionModel().getSelectedItem();
//		int ema = 0;
//		if (emachoice != null) {
//			if (emachoice.equals("5日")) {
//				ema = 5;
//			} else if (emachoice.equals("10日")) {
//				ema = 10;
//			} else if (emachoice.equals("20日")) {
//				ema = 20;
//			}
//		}
//		int holdPeriod = (int) (slider1.getValue());
//		int formingPeriod = (int) (slider.getValue());
//		String waystyle = way.getSelectionModel().getSelectedItem();
//		boolean isHold = box.isSelected();
//		System.out.println(codelist);
//		Stage stage = new Stage();
//		new ProgressFrame().start(stage);
//		if (waystyle.equals("动量策略") || waystyle.equals("均值回归")) {
//			WayVO vo = new WayVO(waystyle, option, isHold, holdPeriod, formingPeriod, ema, shares, begin, end, codelist);
//			StageRepertory.setWayVO(vo);
//			label1.setVisible(true);
//			label2.setVisible(true);
//			label3.setVisible(true);
//			label1.setUnderline(true);
//			label2.setUnderline(false);
//			label3.setUnderline(false);
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(getClass().getResource("/FXML/Cumulative.fxml"));
//			AnchorPane anchorPane = (AnchorPane) loader.load();
//			pane.getChildren().addAll(anchorPane);
//		} else {
//			WayVO vo = new WayVO(waystyle, option,false, 0, 0, 0, 0, begin, end, codelist);
//			StageRepertory.setWayVO(vo);
//			label1.setVisible(true);
//			label3.setVisible(true);
//			label3.setText("股价趋势");
//			Cumulative();
//		}
////		indicator.setVisible(false);
////		text.setVisible(false);
//		stage.close();
//	}

	@FXML
	/**
	 * 累计收益
	 * 
	 * @throws IOException
	 */
	private void Cumulative() throws IOException {
		String waystyle = way.getSelectionModel().getSelectedItem();
		if (waystyle.equals("动量策略") || waystyle.equals("均值回归")) {
			Stage stage = new Stage();
			new ProgressFrame().start(stage);
			pane.getChildren().clear();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/FXML/Cumulative.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			pane.getChildren().addAll(anchorPane);
			label1.setUnderline(true);
			label2.setUnderline(false);
			label3.setUnderline(false);
			stage.close();
		} else {
			WayVO vo = StageRepertory.getwayVO();
			ChartPanel chartPanel = null;
			String plate = stocklist.getSelectionModel().getSelectedItem();
			if (plate.equals("全部") || plate.equals("自定义")) {
				plate = null;
			}
			if (vo.getwaystyle().equals("Boll策略")) {
				BollBackTestGraph graph = new BollBackTestGraph();
				chartPanel = graph.GetBollBackTestGraph(plate, vo.getcodelist(), vo.getBegin(), vo.getend());
			} else if (vo.getwaystyle().equals("KDJ随机指标策略")) {
				KDJStochasticBackTestGraph graph = new KDJStochasticBackTestGraph();
				chartPanel = graph.getKDJStochasticBackTestGraph(plate, vo.getcodelist(), vo.getBegin(), vo.getend());
			} else if (vo.getwaystyle().equals("RSI指标策略")) {
				RSIBackTestGraph graph = new RSIBackTestGraph();
				chartPanel = graph.GetRSIBackTestGraph(plate, vo.getcodelist(), vo.getBegin(), vo.getend());
			}
			chartPanel.setPreferredSize(new Dimension(944, 680));
			SwingNode swingNode = new SwingNode();
			swingNode.setContent(chartPanel);
			pane.getChildren().add(swingNode);
			label1.setUnderline(true);
			label3.setUnderline(false);
		}

	}

	@FXML
	/**
	 * 超额收益
	 * 
	 * @throws IOException
	 */
	private void Excess() throws IOException {
		Stage stage = new Stage();
		new ProgressFrame().start(stage);
		pane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/FXML/Excess.fxml"));
		AnchorPane anchorPane = (AnchorPane) loader.load();
		pane.getChildren().addAll(anchorPane);
		label1.setUnderline(false);
		label2.setUnderline(true);
		label3.setUnderline(false);
		stage.close();
	}

	@FXML
	/**
	 * 收益分布
	 * 
	 * @throws IOException
	 */
	private void distribution() throws IOException {
		String text = label3.getText();
		if (text.equals("收益分布")) {
			Stage stage = new Stage();
			new ProgressFrame().start(stage);
			pane.getChildren().clear();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/FXML/distribution.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			pane.getChildren().addAll(anchorPane);
			label1.setUnderline(false);
			label2.setUnderline(false);
			label3.setUnderline(true);
			stage.close();
		} else {
			WayVO vo = StageRepertory.getwayVO();
			ChartPanel chartPanel = null;
			if (vo.getwaystyle().equals("Boll策略")) {
				BollGraph graph = new BollGraph();
				chartPanel = graph.GetBollGraph(vo.getcodelist().get(0), vo.getBegin(), vo.getend());
			} else if (vo.getwaystyle().equals("KDJ随机指标策略")) {
				KDJStochasticGraph graph = new KDJStochasticGraph();
				chartPanel = graph.GetKDJStochasticGraph(vo.getcodelist().get(0), vo.getBegin(), vo.getend());
			} else if (vo.getwaystyle().equals("RSI指标策略")) {
//				RSIGraph graph = new RSIGraph();
//				chartPanel = graph.GetRSIGraph(vo.getcodelist().get(0), vo.getBegin(), vo.getend());
			}
			chartPanel.setPreferredSize(new Dimension(944, 680));
			SwingNode swingNode = new SwingNode();
			swingNode.setContent(chartPanel);
			pane.getChildren().add(swingNode);
			label1.setUnderline(false);
			label3.setUnderline(true);
		}
	}

	@FXML
	/**
	 * 初始化
	 */
	private void initialize() {
		time1.setValue(LocalDate.of(2014, 3, 28));
		time2.setValue(LocalDate.of(2014, 4, 29));
		slider1.setMax(32);
		slider.setMax(120);

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
		time1.setDayCellFactory(dayCellFactory);
		time2.setDayCellFactory(dayCellFactory);

		stocklist.getItems().addAll("全部", "主板", "中小板", "创业板", "自定义");
		way.getItems().addAll("动量策略", "均值回归");
		EMA.getItems().addAll("5日", "10日", "20日");

		// number输入框只能输入数字
		number.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					number.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		final Tooltip tooltip = new Tooltip();
		tooltip.setText("只允许输入数字");
		number.setTooltip(tooltip);
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

	// public static void main(String[] args){
	// String option = "全部";
	// ArrayList<String> list = new ArrayList<>();
	// GetKGraphDataService service = new GetKGraphData();
	// if (option != null) {
	// if (option.equals("全部")) {
	// ArrayList<stockVO> voList = service.getCodeAndName();
	// for (int i = 0; i < voList.size(); i++) {
	// System.out.println(voList.get(i).getName());
	// list.add(voList.get(i).getName());
	// }
	// }
	// }
	// }

}