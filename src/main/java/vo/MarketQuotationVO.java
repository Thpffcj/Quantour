package vo;
/**
 * 存放市场温度计的相关数据
 * @author fc
 *
 */
public class MarketQuotationVO {

	private String Date;
	private long totalOfVolumn;
	private int numOfTrading;
	private int numOfLimit;
	private int increaseOver5;
	private int decreaseOver5;
	private int others;
	private int totalStock;
	private int Open_CloseIncreaseOver5;
	private int Open_CloseDecreaseOver5;
	
	public MarketQuotationVO(){
		super();
	}
	/**
	 * 
	 * @param Date 查询日期
	 * @param totalOfVolumn 总交易量
	 * @param numOfTrading 涨停股票数
	 * @param numOfLimit 跌停股票数
	 * @param increaseOver5 涨幅>5%股票数
	 * @param decreaseOver5 跌幅>5%股票数
	 * @param others 其它涨跌幅股票数
	 * @param Open_CloseIncreaseOver5 开盘-收盘大于5%*上一个交易日收盘价的股票个数
	 * @param Open_CloseDecreaseOver5 开盘‐收盘小于-5%*上一个交易日收盘价的股票个数
	 */
	public MarketQuotationVO(String Date,long totalOfVolumn,int numOfTrading,int numOfLimit,
			int increaseOver5,int decreaseOver5,int others,int totalStock,
			int Open_CloseIncreaseOver5,int Open_CloseDecreaseOver5){
		this.Date = Date;
		this.totalOfVolumn = totalOfVolumn;
		this.numOfTrading = numOfTrading;
		this.numOfLimit = numOfLimit;
		this.increaseOver5 = increaseOver5;
		this.decreaseOver5 = decreaseOver5;
		this.others = others;
		this.totalStock = totalStock;
	    this.Open_CloseIncreaseOver5 = Open_CloseIncreaseOver5;
	    this.Open_CloseDecreaseOver5 = Open_CloseDecreaseOver5;
	}

	public long getTotalOfVolumn() {
		return totalOfVolumn;
	}

	public int getNumOfTrading() {
		return numOfTrading;
	}

	public int getNumOfLimit() {
		return numOfLimit;
	}

	public int getIncreaseOver5() {
		return increaseOver5;
	}

	public int getDecreaseOver5() {
		return decreaseOver5;
	}

	public int getOpen_CloseIncreaseOver5() {
		return Open_CloseIncreaseOver5;
	}

	public int getOpen_CloseDecreaseOver5() {
		return Open_CloseDecreaseOver5;
	}

	public String getDate() {
		return Date;
	}

	public int getOthers() {
		return others;
	}
	public int getTotalStock() {
		return totalStock;
	}

}
