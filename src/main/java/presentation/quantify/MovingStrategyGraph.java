package presentation.quantify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import businessLogic.MeanReversion;
import businessLogic.MomentumStrategy;
import businessLogicService.MeanReversionService;
import businessLogicService.MomentumStrategyService;

public class MovingStrategyGraph {
	/**
	 * 获取收益曲线
	 * 
	 * @param section
	 *            股票模块
	 * @param stockPool
	 *            股票池
	 * @param holdPeriod
	 *            持有期
	 * @param formingPeriod
	 *            形成期
	 * @param begin
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public ChartPanel GetMeanReversionGraph(String section, ArrayList<String> stockPool, int holdPeriod,
			int formingPeriod, String begin, String end) {
		MomentumStrategyService service = new MomentumStrategy(stockPool, section);
		DefaultCategoryDataset dataset = null;
		try {
			dataset = service.getMStrategyComparedGraph(begin, end, formingPeriod, holdPeriod);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFreeChart jfreechart = ChartFactory.createLineChart("收益曲线", "日期", "收益率", dataset, PlotOrientation.VERTICAL,
				true, true, false);

		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);// 设置曲线图背景色
		plot.setDomainGridlinesVisible(false);// 不显示网格
		plot.setRangeGridlinePaint(Color.GRAY);// 设置间距格线颜色为灰色
		LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
		render.setBaseItemLabelsVisible(true);
		render.setSeriesShapesVisible(0, false);// 设置不显示数据点模型
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

		ChartPanel chartPanel = new ChartPanel(jfreechart, true);
		chartPanel.setPreferredSize(new Dimension(760, 546));

		return chartPanel;

	}

	/**
	 * 获得超额收益率曲线
	 * 
	 * @param section
	 *            股票模块
	 * @param stockPool
	 *            股票池
	 * @param Period
	 *            若isHold为真，为持有期；若为假，为形成期
	 * @param isHold
	 *            若isHold为真，持有期固定；若为假，形成期固定
	 * @param begin
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return
	 */
	public ChartPanel GetMeanReturnRateGraph(String section, ArrayList<String> stockPool, int Period, boolean isHold,
			String begin, String end) {
		MomentumStrategyService service = new MomentumStrategy(stockPool, section);
		DefaultCategoryDataset dataset = null;
		try {
			dataset = service.getMStrategyExtraProfitGraph(isHold, Period, begin, end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFreeChart jfreechart = ChartFactory.createLineChart("超额收益率", "计算周期", "超额收益", dataset, PlotOrientation.VERTICAL,
				true, true, false);

		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);// 设置曲线图背景色
		plot.setDomainGridlinesVisible(false);// 不显示网格
		plot.setRangeGridlinePaint(Color.GRAY);// 设置间距格线颜色为灰色
		LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
		render.setBaseItemLabelsVisible(true);
		render.setSeriesShapesVisible(0, false);// 设置不显示数据点模型
		render.setSeriesPaint(0, Color.BLUE);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));

		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));

		ChartPanel chartPanel = new ChartPanel(jfreechart, true);
		chartPanel.setPreferredSize(new Dimension(760, 546));

		return chartPanel;

	}

	/**
	 * 获取策略胜率曲线
	 * 
	 * @param section
	 *            股票模块
	 * @param stockPool
	 *            股票池
	 * @param Period
	 *            若isHold为真，为形成期；若为假，为持有期
	 * @param isHold
	 *            若isHold为真，持有期固定；若为假，形成期固定
	 * @param begin
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return
	 */
	public ChartPanel GetMeanWinningPercentageGraph(String section, ArrayList<String> stockPool, int Period,
			boolean isHold, String begin, String end) {
		MomentumStrategyService service = new MomentumStrategy(stockPool, section);
		DefaultCategoryDataset dataset = null;
		try {
			dataset = service.getMStrategyWinningGraph(isHold, Period, begin, end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFreeChart jfreechart = ChartFactory.createLineChart("策略胜率", "计算周期", "策略胜率", dataset, PlotOrientation.VERTICAL,
				true, true, false);

		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);// 设置曲线图背景色
		plot.setDomainGridlinesVisible(false);// 不显示网格
		plot.setRangeGridlinePaint(Color.GRAY);// 设置间距格线颜色为灰色
		LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
		render.setBaseItemLabelsVisible(true);
		render.setSeriesShapesVisible(0, false);// 设置不显示数据点模型
		render.setSeriesPaint(0, Color.BLUE);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));

		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));

		ChartPanel chartPanel = new ChartPanel(jfreechart, true);
		chartPanel.setPreferredSize(new Dimension(760, 546));

		return chartPanel;

	}

	/**
	 * 获得收益率分布直方图
	 * 
	 * @param section
	 * @param stockPool
	 * @param holdPeriod
	 * @param formingPeriod
	 * @param begin
	 * @param end
	 * @return
	 */
	public ChartPanel getDistributionHistogramGraph(String section, ArrayList<String> stockPool, int holdPeriod,
			int formingPeriod, String begin, String end) {

		MomentumStrategyService service = new MomentumStrategy(stockPool, section);
		DefaultCategoryDataset dataset = null;
		try {
			dataset = service.getMStrategyYieldGraph(begin, end, holdPeriod, formingPeriod);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFreeChart jfreechart = ChartFactory.createBarChart("相对指数收益分布图", "收益率", "频数", dataset, PlotOrientation.VERTICAL,
				true, true, false);

		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);// 设置曲线图背景色
		plot.setDomainGridlinesVisible(false);// 不显示网格
		plot.setRangeGridlinePaint(Color.GRAY);// 设置间距格线颜色为灰色
		BarRenderer render = (BarRenderer) plot.getRenderer();
		render.setBaseItemLabelsVisible(true);
		render.setSeriesPaint(0, Color.GREEN);
		render.setSeriesPaint(1, Color.RED);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));

		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
		ChartPanel chartPanel = new ChartPanel(jfreechart, true);
		chartPanel.setPreferredSize(new Dimension(760, 546));

		return chartPanel;
	}

	public static void main(String[] args) {
	String s = "深发展A, S ST华新, TCL 集团, 深物业A, 南 玻A, 沙河股份, 宜华地产, 中成股份, 丰原药业, 川化股份, 中联重科, 常山股份, 国际实业, 深康佳A, 宗申动力, *ST中华A, ST中冠A, *ST豫能, 深深宝A, 万 科A, 深华发A, 新 和 成, ST琼花, 伟星股份, 华邦制药, 德豪润达, 精功科技, 华兰生物, 大族激光, 天奇股份, 传化股份, 盾安环境, 凯恩股份, 中航精机, 永新股份, 霞客环保, 世荣兆业, 东信和平, 华星化工, 鑫富药业, 京新药业, 中捷股份, 科华生物, 海特高新, 苏宁电器, 航天电器, 山东威达, 七喜控股, 思源电气, 七 匹 狼, 达安基因, 巨轮股份, 苏 泊 尔, 丽江旅游, 美 欣 达, 华帝股份, 宜科科技, 久联发展, 双鹭药业, 黔源电力, 南 京 港, 登海种业, 华孚色纺, 兔 宝 宝, 江苏三友, 广州国光, 轴研科技, 成霖股份, 宁波华翔, 晶源电子, 三花股份, 中工国际, 同洲电子, 云南盐化, 德美化工, 得润电子, 横店东磁, 中钢天源, 威 尔 泰, 云南旅游, 粤 水 电, 江山化工, 宏润建设, 远光软件, 华峰氨纶, 东华软件, 瑞泰科技, 景兴纸业, 黑猫股份, 獐 子 岛, 众和股份, 江苏宏宝, *ST德棉, 软控股份, 东源电器, 雪 莱 特, 大港股份, 太阳纸业, 苏州固锝, 中材科技";
	ArrayList<String> stockPool = new ArrayList<String>();
	String[] array = s.split(", ");
	for (int i = 0; i < array.length; i++) {
		stockPool.add(array[i]);
	}
	MovingStrategyGraph ms = new MovingStrategyGraph();
//	ChartPanel chartPanel = ms.GetMeanReversionGraph(null, stockPool, 10, 10, "1/28/14", "4/29/14");
	ChartPanel chartPanel = ms.GetMeanWinningPercentageGraph("主板", stockPool, 6, true,"3/28/14", "4/29/14");
//	ChartPanel chartPanel = ms.GetMeanReturnRateGraph("主板", stockPool, 10, false,"3/28/14", "4/29/14");
//	ChartPanel chartPanel = ms.getDistributionHistogramGraph(null, stockPool, 10, 10, "4/28/13", "4/29/14");
	JFrame jFrame = new JFrame("");
	jFrame.setSize(800, 600);
	jFrame.add(chartPanel);
	jFrame.setVisible(true);
}
}
