package presentation.quantify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

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

import businessLogic.GetKDJStochasticData;
import businessLogic.GetRSIData;
import businessLogicService.GetKDJStochasticDataService;
import businessLogicService.GetRSIDataService;

public class KDJStochasticGraph {

	public ChartPanel GetKDJStochasticGraph(String condition, String begin, String end) {
		GetKDJStochasticDataService kdj = new GetKDJStochasticData();
		DefaultCategoryDataset dataset = null;
//		DefaultCategoryDataset dataset = kdj.getKDJStochasticData(condition, begin, end);
		JFreeChart jfreechart = ChartFactory.createLineChart("KDJ随机指标图", "日期", "最低值", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		
		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setBackgroundPaint(Color.BLACK);//设置曲线图背景色
        plot.setDomainGridlinesVisible(false);//不显示网格
        plot.setRangeGridlinePaint(Color.GRAY);//设置间距格线颜色为灰色
        LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
        render.setBaseItemLabelsVisible(true);
        render.setSeriesShapesVisible(0, false);//设置不显示数据点模型
        render.setSeriesShapesVisible(1, false);
        render.setSeriesShapesVisible(2, false);
		render.setSeriesPaint(0, Color.WHITE);
        render.setSeriesPaint(1, Color.YELLOW);
        render.setSeriesPaint(2, Color.MAGENTA);

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
		KDJStochasticGraph mGraph = new KDJStochasticGraph();
		ChartPanel chart = mGraph.GetKDJStochasticGraph("1", "1/20/14", "4/29/14");
//		ChartPanel chart = aGraph.GetKGraph("深发展A", "2/24/05", "5/16/05");
//		ChartPanel chart = aGraph.GetKGraph("宜华地产", "4/1/14", "4/29/14");
		JFrame jFrame = new JFrame("");
		jFrame.setSize(800, 600);
		jFrame.add(chart);
		jFrame.setVisible(true);
	}
}
