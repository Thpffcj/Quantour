package presentation;

import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import vo.MarketQuotationVO;

/**
 * 
 * @author 费慧通
 *
 *         市场温度计的饼状图
 */
public class pieChart {
	public ChartPanel getPieChart(MarketQuotationVO vo) {
		DefaultPieDataset data = getDataset(vo);
		JFreeChart chart = ChartFactory.createPieChart("股票市场行情", data, true, false, false);
		// 设置百分比
		PiePlot pieplot = (PiePlot) chart.getPlot();
		DecimalFormat df = new DecimalFormat("0.00%");// 获得一个DecimalFormat对象，主要是设置小数问题
		NumberFormat nf = NumberFormat.getNumberInstance();// 获得一个NumberFormat对象
		StandardPieSectionLabelGenerator sp1 = new StandardPieSectionLabelGenerator("{0}  {2}", nf, df);// 获得StandardPieSectionLabelGenerator对象
		pieplot.setLabelGenerator(sp1);// 设置饼图显示百分比

		// 没有数据的时候显示的内容
		pieplot.setNoDataMessage("无数据显示");
		pieplot.setCircular(false);
		pieplot.setLabelGap(0.02D);

		pieplot.setIgnoreNullValues(true);// 设置不显示空值
		pieplot.setIgnoreZeroValues(true);// 设置不显示负值

		chart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体
		PiePlot piePlot = (PiePlot) chart.getPlot();// 获取图表区域对象
		piePlot.setLabelFont(new Font("宋体", Font.BOLD, 10));// 解决乱码
		chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 10));

		ChartPanel chartPanel = new ChartPanel(chart, true);
		chartPanel.setPreferredSize(new Dimension(510,402));
		
		return chartPanel;
	}

	public DefaultPieDataset getDataset(MarketQuotationVO vo) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("涨停股票数", vo.getNumOfTrading());
		dataset.setValue("跌停股票数", vo.getNumOfLimit());
		dataset.setValue("涨幅超过5%的股票数", vo.getIncreaseOver5());
		dataset.setValue("跌幅超过5%的股票数", vo.getDecreaseOver5());
		dataset.setValue("其他", vo.getOthers());
		return dataset;
	}

}
