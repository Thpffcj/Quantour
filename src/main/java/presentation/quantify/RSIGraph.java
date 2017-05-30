package presentation.quantify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import businessLogic.GetBollData;
import businessLogic.GetRSIData;
import businessLogic.GraphUtil;
import businessLogic.MeanReversion;
import businessLogicService.GetBollDataService;
import businessLogicService.GetRSIDataService;
import businessLogicService.MeanReversionService;

public class RSIGraph {

//	public ChartPanel GetRSIGraph(String condition, String begin, String end) {
//		GetRSIDataService rs = new GetRSIData();
//		DefaultCategoryDataset dataset = rs.getRSIGraphData(condition, begin, end);
//		JFreeChart jfreechart = ChartFactory.createLineChart("RSI图", "日期", "最低值", dataset,
//				PlotOrientation.VERTICAL, true, true, false);
//		
//		CategoryPlot plot = jfreechart.getCategoryPlot();
//		plot.setBackgroundPaint(Color.BLACK);//设置曲线图背景色
//        plot.setDomainGridlinesVisible(false);//不显示网格
//        plot.setRangeGridlinePaint(Color.GRAY);//设置间距格线颜色为灰色
//        LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
//        render.setBaseItemLabelsVisible(true);
//        render.setSeriesShapesVisible(0, false);//设置不显示数据点模型
//        render.setSeriesPaint(0, Color.BLUE);
//
//		CategoryAxis domainAxis = plot.getDomainAxis();
//		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
//		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
//
//		ValueAxis rangeAxis = plot.getRangeAxis();
//		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
//		
//		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
//		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
//		
//		ChartPanel chartPanel = new ChartPanel(jfreechart,true);
//		chartPanel.setPreferredSize(new Dimension(760,546));
//		
//		return chartPanel;

//	}

	
	public static void main(String[] args) {
//		RSIGraph mGraph = new RSIGraph();
//		ChartPanel chart = mGraph.GetRSIGraph("1", "10/8/13", "12/31/13");
////		ChartPanel chart = aGraph.GetKGraph("宜华地产", "4/1/14", "4/29/14");
//		JFrame jFrame = new JFrame("");
//		jFrame.setSize(800, 600);
//		jFrame.add(chart);
//		jFrame.setVisible(true);
	}
}
