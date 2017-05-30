package presentation;

import java.awt.Dimension;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;

import businessLogic.MeanReversion;
import businessLogic.MomentumStrategy;
import businessLogicService.MeanReversionService;
import businessLogicService.MomentumStrategyService;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import presentation.quantify.MeanReversionGraph;
import presentation.quantify.MovingStrategyGraph;
import vo.WayVO;
import vo.quantify.MeanReversionVO;

public class CumulativeController {
	@FXML
	private Label label1;
	@FXML
	private Label label2;
	@FXML
	private Label label3;
	@FXML
	private Label label4;
	@FXML
	private Label label5;
	@FXML
	private Label label8;
	@FXML
	private StackPane pane;
	
	@FXML
	/**
	 * 初始化
	 */
	private void initialize() throws ParseException {
		WayVO vo = StageRepertory.getwayVO();
		String option = "";
		if(vo.getoption().equals("全部")||vo.getoption().equals("自定义")){
			option = null;
		}else{
			option = vo.getoption();
		}
		ChartPanel chartPanel = null;
		if(vo.getwaystyle().equals("均值回归")){
			MeanReversionGraph graph = new MeanReversionGraph();
			chartPanel = graph.GetMeanReversionGraph(option,vo.getcodelist(), vo.getshares(), vo.getholdperiod(), vo.getema(), vo.getBegin(), vo.getend());
			MeanReversionService service = new MeanReversion();
			MeanReversionVO VO = service.getParameter();
			label1.setText(VO.getYearRateOfReturn());
			label2.setText(VO.getBenchmarkYearRateOfReturn());
			label3.setText(VO.getAlpha());
			label4.setText(Double.toString(VO.getBeta()));
			label5.setText(Double.toString(VO.getSharpeRatio()));
			label8.setText(VO.getMaximumRetracement());
		}else if(vo.getwaystyle().equals("动量策略")){
			MovingStrategyGraph graph = new MovingStrategyGraph();
			chartPanel = graph.GetMeanReversionGraph(option, vo.getcodelist(), vo.getholdperiod(), vo.getformperiod(), vo.getBegin(), vo.getend());
			MomentumStrategyService service = new MomentumStrategy(vo.getcodelist(), option);
			MeanReversionVO VO = service.getParameter();
			label1.setText(VO.getYearRateOfReturn());
			label2.setText(VO.getBenchmarkYearRateOfReturn());
			label3.setText(VO.getAlpha());
			label4.setText(Double.toString(VO.getBeta()));
			label5.setText(Double.toString(VO.getSharpeRatio()));
			label8.setText(VO.getMaximumRetracement());
		}
		chartPanel.setPreferredSize(new Dimension(822, 542));
		SwingNode swingNode = new SwingNode();
		swingNode.setContent(chartPanel);
		pane.getChildren().add(swingNode);
	}

}
