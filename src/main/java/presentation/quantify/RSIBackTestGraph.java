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

import businessLogic.GetBollData;
import businessLogic.GetRSIData;
import businessLogicService.GetBollDataService;
import businessLogicService.GetRSIDataService;

public class RSIBackTestGraph {

	public ChartPanel GetRSIBackTestGraph(String section, ArrayList<String> stockPool, String begin, String end) {
		
		GetRSIDataService rsids = new GetRSIData();
		DefaultCategoryDataset dataset = rsids.GetRSIBackTestGraphData(section, stockPool, begin, end);
		JFreeChart jfreechart = ChartFactory.createLineChart("收益曲线", "日期", "最低值", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		
		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);//设置曲线图背景色
        plot.setDomainGridlinesVisible(false);//不显示网格
        plot.setRangeGridlinePaint(Color.GRAY);//设置间距格线颜色为灰色
        LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
        render.setBaseItemLabelsVisible(true);
        render.setSeriesShapesVisible(0, false);//设置不显示数据点模型
        render.setSeriesShapesVisible(1, false);
        render.setSeriesPaint(0, Color.BLUE);
        render.setSeriesPaint(1, Color.RED);

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

	
	public static void main(String[] args) {
		RSIBackTestGraph mGraph = new RSIBackTestGraph();
		ArrayList<String> name = new ArrayList<>();
		name.add("1");
		name.add("2151");
		name.add("21");
		name.add("2229");
		name.add("2308");
		name.add("39");
		name.add("2249");
		name.add("2191");
		name.add("18");
		name.add("2502");
		name.add("2343");
		name.add("2536");
		name.add("27");	
		name.add("2066");	
		name.add("2067");	
		name.add("2068");	
		name.add("2069");	
		name.add("2070");	
		name.add("2071");	
		name.add("2072");	
		name.add("2073");	
		name.add("2074");		
		name.add("2208");
		ChartPanel chart = mGraph.GetRSIBackTestGraph(null, name,"10/8/13", "4/29/14");
//		ChartPanel chart = aGraph.GetKGraph("深发展A", "2/24/05", "5/16/05");
//		ChartPanel chart = aGraph.GetKGraph("宜华地产", "4/1/14", "4/29/14");
		JFrame jFrame = new JFrame("");
		jFrame.setSize(800, 600);
		jFrame.add(chart);
		jFrame.setVisible(true);
	}
}
