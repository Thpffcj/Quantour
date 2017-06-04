package vo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import po.StockPO;

/**
 * 
 * @author 费慧通
 *保存stock的暂时对象
 */
public class stockVO {	
	private int index;
	private String date;
	private double open;
	private double high;
	private double low;
	private double close;
	private double adjClose;
	private String volume;
	private String code;
	
	public stockVO(StockPO po){
		date = po.getDate();
		open = po.getOpen();
		close = po.getClose();
		high = po.getHigh();
		low = po.getLow();
		volume = po.getVolume();
		adjClose = po.getAdjClose();
		code = po.getCode();
	}
	
	/**
	 * 获取股票的记录编号
	 * @return
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * 获得日期
	 * @return
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * 获得开盘指数
	 * @return
	 */
	public double getOpen() {
		return open;
	}
	
	/**
	 * 获得收盘指数
	 * @return
	 */
	public double getClose() {
		return close;
	}
	
	/**
	 * 获得最高指数
	 * @return
	 */
	public double getHigh() {
		return high;
	}
	
	/**
	 * 获得最低指数
	 * @return
	 */
	public double getLow() {
		return low;
	}
	
	/**
	 * 获得成交量
	 * @return
	 */
	public String getVolume() {
		return volume;
	}
	
	/**
	 * 获得复权后的收盘指数
	 * @return
	 */
	public double getAdjClose() {
		return adjClose;
	}
	
	/**
	 * 获得股票代码
	 * @return
	 */
	public String getCode() {
		return code;
	}
}
