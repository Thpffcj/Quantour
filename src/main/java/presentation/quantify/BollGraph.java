package presentation.quantify;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import businessLogic.GetBollData;
import businessLogic.GraphUtil;
import businessLogicService.GetBollDataService;
import presentation.ToolTipChartPanel;

public class BollGraph {

	public ChartPanel GetBollGraph(String Code, String Begin, String End) {
		GraphUtil graphUtil = new GraphUtil();
		GetBollDataService gb = new GetBollData();
		OHLCSeriesCollection seriesCollection = null;
		TimeSeriesCollection timeSeriesCollection = null;
		
		// 设置日期格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		double highValue = Double.MIN_VALUE;// 设置K线数据当中的最大值
		double minValue = Double.MAX_VALUE;// 设置K线数据当中的最小值

		// 获取K线数据的最高值和最低值
		int seriesCount = seriesCollection.getSeriesCount();// 一共有多少个序列，目前为一个
//		System.out.println(seriesCount);
		for (int i = 0; i < seriesCount; i++) {
			int itemCount = seriesCollection.getItemCount(i);// 每一个序列有多少个数据项
			for (int j = 0; j < itemCount; j++) {
				if (highValue < seriesCollection.getHighValue(i, j)) {// 取第i个序列中的第j个数据项的最大值
					highValue = seriesCollection.getHighValue(i, j);
				}
				if (minValue > seriesCollection.getLowValue(i, j)) {// 取第i个序列中的第j个数据项的最小值
					minValue = seriesCollection.getLowValue(i, j);
				}
			}

		}

		// 设置K线图的画图器
		CandlestickRenderer candlestickRender = new CandlestickRenderer();
		// 设置是否使用自定义的边框线，程序自带的边框线的颜色不符合中国股票市场的习惯
		candlestickRender.setUseOutlinePaint(true);
		// 设置如何对K线图的宽度进行设定
		candlestickRender.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
		// 设置各个K线图之间的间隔
		candlestickRender.setAutoWidthGap(0.001);
		// 设置股票上涨的K线图颜色
		candlestickRender.setUpPaint(Color.RED);
		// 设置股票下跌的K线图颜色
		candlestickRender.setDownPaint(Color.GREEN);

		// 设置x轴，也就是时间轴
		DateAxis x1Axis = new DateAxis();
		x1Axis.setAutoRange(false);// 设置不采用自动设置时间范围
		try {
			// 设置时间范围，注意时间的最大值要比已有的时间最大值要多一天
			x1Axis.setRange(dateFormat.parse(graphUtil.dateTransform(Begin)), dateFormat.parse(graphUtil.dateTransform(End)));
			} catch (Exception e) {
			e.printStackTrace();
		}
		// 设置时间线显示的规则，用这个方法就摒除掉了周六和周日这些没有交易的日期(很多人都不知道有此方法)，使图形看上去连续
		SegmentedTimeline timeline = SegmentedTimeline.newMondayThroughFridayTimeline();// 设置时间线显示的规则，用这个方法摒除掉周六和周日这些没有交易的日期
		ArrayList<Date> dates = null;
		try {
			dates = graphUtil.GetDateOfSuspension(Code, Begin, End);
			for(Date illegal : dates){
				 timeline.addException(illegal);
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		}
		x1Axis.setTimeline(timeline);
		// 设置不采用自动选择刻度值
		x1Axis.setAutoTickUnitSelection(false);
		// 设置标记的位置
		x1Axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
		// 设置标准的时间刻度单位
		x1Axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());
		// 设置时间刻度的间隔，一般以周为单位
		x1Axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 10));
		// 设置显示时间的格式
		x1Axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
		// 设定y轴，就是数字轴
		NumberAxis y1Axis = new NumberAxis();
		// 不使用自动设定范围
		y1Axis.setAutoRange(false);
		// 设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
//		System.out.println(minValue);
//		System.out.println(highValue);
		y1Axis.setRange(minValue * 0.9, highValue * 1.1);
		// 设置刻度显示的密度
		y1Axis.setTickUnit(new NumberTickUnit((highValue * 1.1 - minValue * 0.9) / 10));
		
		XYLineAndShapeRenderer xyLine = new XYLineAndShapeRenderer();
		xyLine.setBaseItemLabelsVisible(true);
		xyLine.setSeriesShapesVisible(0, false);//设置不显示数据点模型
		xyLine.setSeriesShapesVisible(1, false);
		xyLine.setSeriesShapesVisible(2, false);
		xyLine.setSeriesPaint(0, Color.WHITE);//设置均线颜色
		xyLine.setSeriesPaint(1, Color.YELLOW);
		xyLine.setSeriesPaint(2, Color.MAGENTA);
		
		 XYPlot plot = new XYPlot(null,x1Axis,y1Axis,null);
         plot.setBackgroundPaint(Color.BLACK);//设置曲线图背景色
         plot.setDomainGridlinesVisible(false);//不显示网格
         plot.setRangeGridlinePaint(Color.RED);//设置间距格线颜色为红色

         //将设置好的数据集合和画图器放入画板
         plot.setDataset(0, seriesCollection);
         plot.setRenderer(0, candlestickRender);
         plot.setDataset(1, timeSeriesCollection);
         plot.setRenderer(1, xyLine);

		JFreeChart chart = new JFreeChart("Boll图", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
		ToolTipChartPanel ttp = new ToolTipChartPanel(chart, plot, null);

		return ttp;
	}
	
	public static void main(String[] args) {
		BollGraph aGraph = new BollGraph();
		ChartPanel chart = aGraph.GetBollGraph("深发展A", "1/20/14", "4/29/14");
//		ChartPanel chart = aGraph.GetKGraph("深发展A", "2/24/05", "5/16/05");
//		ChartPanel chart = aGraph.GetKGraph("宜华地产", "4/1/14", "4/29/14");
		JFrame jFrame = new JFrame("");
		jFrame.setSize(800, 600);
		jFrame.add(chart);
		jFrame.setVisible(true);
	}
}
