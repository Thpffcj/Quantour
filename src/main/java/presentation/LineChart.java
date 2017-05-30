package presentation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import businessLogic.GetComparedData;
import businessLogicService.GetComparedDataService;

/**
 * 
 * @author 费慧通
 *
 *股票对比柱状图
 */
public class LineChart {
	
	/**
	 * 最低值对比的折线图
	 * @param name1
	 * @param name2
	 * @param Begin
	 * @param End
	 * @return
	 */
	public ChartPanel getLowestValueChart(String name1,String name2,String Begin,String End){
		GetComparedDataService getComparedData = new GetComparedData();
		DefaultCategoryDataset dataset = getComparedData.getLowestValue(name1,name2,Begin,End);
		JFreeChart jfreechart = ChartFactory.createLineChart("每日最低值对比图", "日期", "最低值", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		
		CategoryPlot plot = jfreechart.getCategoryPlot();
		

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
		ChartPanel chartPanel = new ChartPanel(jfreechart,true);
		chartPanel.setPreferredSize(new Dimension(760,546));
		
		return chartPanel;
	}
	
	/**
	 * 最高值对比的柱状图
	 * @param name1
	 * @param name2
	 * @param Begin
	 * @param End
	 * @return
	 */
	public ChartPanel getHighestValueChart(String name1,String name2,String Begin,String End){
		GetComparedDataService getComparedData = new GetComparedData();
		DefaultCategoryDataset dataset = getComparedData.getHighestValue(name1,name2,Begin,End);
		JFreeChart jfreechart = ChartFactory.createLineChart("股票每日最高值对比图", "日期", "最高值", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		
		CategoryPlot plot = jfreechart.getCategoryPlot();

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
		ChartPanel chartPanel = new ChartPanel(jfreechart,true);
		chartPanel.setPreferredSize(new Dimension(760,546));
		
		return chartPanel;
	}
	
	/**
	 * 得到涨幅/跌幅的折线图
	 * @param name1
	 * @param name2
	 * @param Begin
	 * @param End
	 * @return
	 */
	public ChartPanel getRoseAndDropValueChart(String name1,String name2,String Begin,String End){
		GetComparedDataService getComparedData = new GetComparedData();
		DefaultCategoryDataset dataset = getComparedData.getRoseAndDropValue(name1, name2, Begin, End);
		JFreeChart jfreechart = ChartFactory.createLineChart("每日涨/跌幅对比图", "日期", "涨幅/跌幅", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		
		CategoryPlot plot = jfreechart.getCategoryPlot();

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
		ChartPanel chartPanel = new ChartPanel(jfreechart,true);
		chartPanel.setPreferredSize(new Dimension(760,546));
		
		return chartPanel;
	}
	
	/**
	 * 得到收盘价的对比折线图
	 * @param name1
	 * @param name2
	 * @param Begin
	 * @param End
	 * @return
	 */
	public ChartPanel getCloseValueChart(String name1,String name2,String Begin,String End){
		GetComparedDataService getComparedDataService = new GetComparedData();
		DefaultCategoryDataset dataset = getComparedDataService.getCloseValue(name1, name2, Begin, End);
		JFreeChart jfreechart = ChartFactory.createLineChart("收盘价对比图", "日期", "收盘价", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		
		CategoryPlot plot = jfreechart.getCategoryPlot();

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
		ChartPanel chartPanel = new ChartPanel(jfreechart,true);
		chartPanel.setPreferredSize(new Dimension(760,546));
		
		return chartPanel;
	}
	
	/**
	 * 得到对数收益率的对比折线图
	 * @param name1
	 * @param name2
	 * @param Begin
	 * @param End
	 * @return
	 */
	public ChartPanel getRateOfReturnChart(String name1,String name2,String Begin,String End){
		GetComparedDataService getComparedDataService = new GetComparedData();
		DefaultCategoryDataset dataset = getComparedDataService.getRateOfReturn(name1, name2, Begin, End);
		JFreeChart jfreechart = ChartFactory.createLineChart("对数收益率对比图", "日期", "对数收益率", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		
		CategoryPlot plot = jfreechart.getCategoryPlot();

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
		ChartPanel chartPanel = new ChartPanel(jfreechart,true);
		chartPanel.setPreferredSize(new Dimension(760,546));
		
		return chartPanel;
	}
	
	public static void main(String[] args){
		LineChart main = new LineChart();
		JFrame jFrame = new JFrame("");
		jFrame.add(main.getRoseAndDropValueChart("1", "151", "1/20/14", "1/29/14"));
		jFrame.setVisible(true);
		jFrame.setSize(900, 600);
	}
}