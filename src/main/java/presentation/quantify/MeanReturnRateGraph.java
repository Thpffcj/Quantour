package presentation.quantify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import businessLogic.MeanReversion;
import businessLogicService.MeanReversionService;

public class MeanReturnRateGraph {

	public ChartPanel GetMeanReturnRateGraph(String section, ArrayList<String> stockPool, int shares, int holdPeriod, int formingPeriod, String begin, String end) {
		MeanReversionService ms = new MeanReversion();
		DefaultCategoryDataset dataset = null;
//		ArrayList<String> a = ms.getExcessIncome();
//		System.out.println(a.get(5));
		CategoryPlot plot = null;
		plot.setBackgroundPaint(Color.WHITE);//设置曲线图背景色
        plot.setDomainGridlinesVisible(false);//不显示网格
        plot.setRangeGridlinePaint(Color.GRAY);//设置间距格线颜色为灰色
        LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
        render.setBaseItemLabelsVisible(true);
        render.setSeriesShapesVisible(0, false);//设置不显示数据点模型
        render.setSeriesPaint(0, Color.BLUE);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		
//		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
//		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
//		
//		ChartPanel chartPanel = new ChartPanel(jfreechart,true);
//		chartPanel.setPreferredSize(new Dimension(760,546));
		
		return null;

	}
	
	public ChartPanel GetMeanWinningPercentageGraph(String section, ArrayList<String> stockPool, int shares, int holdPeriod, int formingPeriod, String begin, String end) {
		MeanReversionService ms = new MeanReversion();
		DefaultCategoryDataset dataset = null;
		
//		ArrayList<String> a = ms.getWinningPercentage();
//		System.out.println(a.get(5));
		CategoryPlot plot = null;
		plot.setBackgroundPaint(Color.WHITE);//设置曲线图背景色
        plot.setDomainGridlinesVisible(false);//不显示网格
        plot.setRangeGridlinePaint(Color.GRAY);//设置间距格线颜色为灰色
        LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
        render.setBaseItemLabelsVisible(true);
        render.setSeriesShapesVisible(0, false);//设置不显示数据点模型
        render.setSeriesPaint(0, Color.BLUE);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		
//		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
//		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
//		
//		ChartPanel chartPanel = new ChartPanel(jfreechart,true);
//		chartPanel.setPreferredSize(new Dimension(760,546));
		
		return null;

	}
	
	public static void main(String[] args) {
		MeanReturnRateGraph mGraph = new MeanReturnRateGraph();
		ArrayList<String> name = new ArrayList<>();
		name.add("1");
		name.add("2151");
		name.add("37");
		name.add("21");
		name.add("2229");
		name.add("2308");
		name.add("39");
		name.add("2249");
		name.add("2191");
		name.add("18");
		name.add("2208");
		ChartPanel chart = mGraph.GetMeanReturnRateGraph(null, name, 5, 4, 20, "1/20/14", "4/29/14");
//		ChartPanel chart = mGraph.GetMeanWinningPercentageGraph(null, name, 5, 4, 20, "1/20/14", "4/29/14");
//		ChartPanel chart = aGraph.GetKGraph("深发展A", "2/24/05", "5/16/05");
//		ChartPanel chart = aGraph.GetKGraph("宜华地产", "4/1/14", "4/29/14");
		JFrame jFrame = new JFrame("");
		jFrame.setSize(800, 600);
		jFrame.add(chart);
		jFrame.setVisible(true);
	}
}
