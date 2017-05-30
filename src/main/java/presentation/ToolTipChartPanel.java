package presentation;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

public class ToolTipChartPanel extends ChartPanel {

	private DateAxis xAxis;
	private NumberAxis yAxis;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
	Map<String,Double> highValues;
	
	public ToolTipChartPanel(JFreeChart chart,XYPlot xyplot, Map<String,Double> highValues) {
		super(chart);
		this.highValues = highValues;
		this.xAxis = (DateAxis) xyplot.getDomainAxis();
		this.yAxis = (NumberAxis) xyplot.getRangeAxis();
	}

	public String getToolTipText(MouseEvent e) {
		Rectangle2D dataArea = getScreenDataArea();

		// 不在区域内的直接返回
		if (!dataArea.contains(e.getX(), e.getY())) {
			return null;
		}

		long x = getValueX(dataArea, e.getX());
//		double y = getValueY(dataArea, e.getY());
//		double y = getValeY();
//		Date day = new Date(x);
//		String sday = sdf.format(day);
//		double y = 0;
//		Date day = new Date(x);
//        long a = 24 * 60 * 60 * 1000;
//		if(x%a == 0L){
//			y = highValues.get(sdf.format(day));
//		}
		return sdf.format(new Date(x));
//		return sdf.format(new Date(x)) + "," + y ;
	}

	private long getValueX(Rectangle2D dataArea, int mouseX) {
		// 1 获得像素值
		long x = (long) (mouseX - dataArea.getMinX());

		// 2 转化成时间毫秒
		Date lowerBound = xAxis.getMinimumDate();
		Date upperBound = xAxis.getMaximumDate();
		x = (long) (x * (upperBound.getTime() - lowerBound.getTime()) / dataArea.getWidth() + lowerBound.getTime());

		return x;
	}

//	private double getValueY(Rectangle2D dataArea, int mouseY){
//		// 1 获得像素值
//		double y = (dataArea.getHeight() - (mouseY - dataArea.getMinY()));
//
//		// 2 转化成数值
//		double lowerBound = yAxis.getLowerBound();
//		double upperBound = yAxis.getUpperBound();
//		y = (y*(upperBound - lowerBound)/dataArea.getHeight() + lowerBound);
//
//		return y;
//	}
	
//	private double getValueY(Rectangle2D dataArea, int mouseX, int mouseY, Map<String,Double> highValues) {
//		// 1 获得像素值
//		long x = (long) (mouseX - dataArea.getMinX());
//
//		// 2 转化成时间毫秒
//		Date lowerBound = xAxis.getMinimumDate();
//		Date upperBound = xAxis.getMaximumDate();
//		x = (long) (x * (upperBound.getTime() - lowerBound.getTime()) / dataArea.getWidth() + lowerBound.getTime());
//		Date day = new Date(x);
//		String sday = sdf.format(day);
//		double y = highValues.get(sday);
//		return y;
//	}
}