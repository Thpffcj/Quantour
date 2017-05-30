package presentation;

import java.awt.Dimension;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;

import businessLogic.MeanReversion;
import businessLogic.MomentumStrategy;
import businessLogicService.MeanReversionService;
import businessLogicService.MomentumStrategyService;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import presentation.quantify.MeanDistributionHistogram;
import presentation.quantify.MovingStrategyGraph;
import vo.WayVO;
import vo.quantify.DistributionHistogramVO;
import vo.quantify.MeanReturnRateVO;

public class DistributionController {
	@FXML
	private Label label1;
	@FXML
	private Label label2;
	@FXML
	private Label label3;
	@FXML
	private StackPane pane;
	
	@FXML
	/**
	 * 初始化
	 */
	private void initialize(){
		WayVO vo = StageRepertory.getwayVO();
		String option = "";
		if(vo.getoption().equals("全部")||vo.getoption().equals("自定义")){
			option = null;
		}else{
			option = vo.getoption();
		}
		ChartPanel chartPanel = null;
		if(vo.getwaystyle().equals("均值回归")){
			MeanDistributionHistogram graph = new MeanDistributionHistogram();
			chartPanel = graph.getDistributionHistogramGraph(option,vo.getcodelist(), vo.getshares(), vo.getholdperiod(), vo.getema(), vo.getBegin(), vo.getend());
			MeanReversionService service = new MeanReversion();
			DistributionHistogramVO VO = service.getDistributionHistogram();
			label1.setText(Double.toString(VO.getWinDays()));
			label2.setText(Double.toString(VO.getLoseDays()));
			label3.setText(Double.toString(VO.getWinPercentage()));
		}else if(vo.getwaystyle().equals("动量策略")){
			MovingStrategyGraph graph = new MovingStrategyGraph();
			chartPanel = graph.getDistributionHistogramGraph(option, vo.getcodelist(), vo.getholdperiod(), vo.getformperiod(), vo.getBegin(), vo.getend());
			MomentumStrategyService service = new MomentumStrategy(vo.getcodelist(), option);
			DistributionHistogramVO VO = service.getDistributionHistogram();
			label1.setText(Double.toString(VO.getWinDays()));
			label2.setText(Double.toString(VO.getLoseDays()));
			label3.setText(Double.toString(VO.getWinPercentage()));
		}
		chartPanel.setPreferredSize(new Dimension(890, 553));
		SwingNode swingNode = new SwingNode();
		swingNode.setContent(chartPanel);
		pane.getChildren().add(swingNode);
	}
}
