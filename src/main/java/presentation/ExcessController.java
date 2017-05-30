package presentation;

import java.awt.Dimension;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;

import businessLogic.MeanReversion;
import businessLogic.MomentumStrategy;
import businessLogicService.MeanReversionService;
import businessLogicService.MomentumStrategyService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import presentation.quantify.MeanReturnRateGraph;
import presentation.quantify.MovingStrategyGraph;
import vo.WayVO;
import vo.quantify.MeanReturnRateVO;

public class ExcessController {
	@FXML
	private TableView<MeanReturnRateVO> tableView;
	@FXML
	private TableColumn<MeanReturnRateVO, Number> column1;
	@FXML
	private TableColumn<MeanReturnRateVO, String> column2;
	@FXML
	private TableColumn<MeanReturnRateVO, String> column3;
	@FXML
	private StackPane pane1;
	@FXML
	private StackPane pane2;

	@FXML
	/**
	 * 初始化
	 */
	private void initialize() {
		WayVO vo = StageRepertory.getwayVO();
		String option = "";
		if (vo.getoption().equals("全部") || vo.getoption().equals("自定义")) {
			option = null;
		} else {
			option = vo.getoption();
		}
		ChartPanel chartPanel1 = null;
		ChartPanel chartPanel2 = null;
		if (vo.getwaystyle().equals("均值回归")) {
			MeanReturnRateGraph graph = new MeanReturnRateGraph();
			chartPanel1 = graph.GetMeanReturnRateGraph(option, vo.getcodelist(), vo.getshares(), vo.getholdperiod(),
					vo.getema(), vo.getBegin(), vo.getend());
			chartPanel2 = graph.GetMeanWinningPercentageGraph(option, vo.getcodelist(), vo.getshares(),
					vo.getholdperiod(), vo.getema(), vo.getBegin(), vo.getend());
			MeanReversionService service = new MeanReversion();
			ArrayList<MeanReturnRateVO> VO = service.getCalculationCycle();
			ObservableList<MeanReturnRateVO> list = FXCollections.observableArrayList(VO);
			tableView.setItems(list);
			column1.setCellValueFactory(cellData -> cellData.getValue().getCalculationCycleProperty());
			column2.setCellValueFactory(cellData -> cellData.getValue().getExcessincomeProperty());
			column3.setCellValueFactory(cellData -> cellData.getValue().getWinningpercentageProperty());
		} else if (vo.getwaystyle().equals("动量策略")) {
			MovingStrategyGraph graph = new MovingStrategyGraph();
			if (vo.getisHold()) {
				chartPanel1 = graph.GetMeanReturnRateGraph(option, vo.getcodelist(), vo.getholdperiod(), vo.getisHold(),
						vo.getBegin(), vo.getend());
				chartPanel2 = graph.GetMeanWinningPercentageGraph(option, vo.getcodelist(), vo.getholdperiod(),
						vo.getisHold(), vo.getBegin(), vo.getend());
			} else {
				chartPanel1 = graph.GetMeanReturnRateGraph(option, vo.getcodelist(), vo.getformperiod(), vo.getisHold(),
						vo.getBegin(), vo.getend());
				chartPanel2 = graph.GetMeanWinningPercentageGraph(option, vo.getcodelist(), vo.getformperiod(),
						vo.getisHold(), vo.getBegin(), vo.getend());
			}
			MomentumStrategyService service = new MomentumStrategy(vo.getcodelist(), option);
			ArrayList<MeanReturnRateVO> VO = service.getCalculationCycle();
			ObservableList<MeanReturnRateVO> list = FXCollections.observableArrayList(VO);
			tableView.setItems(list);
			column1.setCellValueFactory(cellData -> cellData.getValue().getCalculationCycleProperty());
			column2.setCellValueFactory(cellData -> cellData.getValue().getExcessincomeProperty());
			column3.setCellValueFactory(cellData -> cellData.getValue().getWinningpercentageProperty());
		}
		chartPanel1.setPreferredSize(new Dimension(520, 325));
		chartPanel2.setPreferredSize(new Dimension(520, 325));
		SwingNode node1 = new SwingNode();
		node1.setContent(chartPanel1);
		pane1.getChildren().add(node1);

		SwingNode node2 = new SwingNode();
		node2.setContent(chartPanel2);
		pane2.getChildren().add(node2);
	}

}
