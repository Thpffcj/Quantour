package po;

import java.util.ArrayList;

public class UpsAndDownsPO {
	private int[] upsAndDowns;
	private ArrayList<StockNameAndUpsPO> ups_Max;
	private ArrayList<StockNameAndUpsPO> downs_Max;
	public UpsAndDownsPO(int[] upsAndDowns,ArrayList<StockNameAndUpsPO> ups_Max,ArrayList<StockNameAndUpsPO> downs_Max){
		this.upsAndDowns = upsAndDowns;
		this.ups_Max = ups_Max;
		this.downs_Max = downs_Max;
	}
	public int[] getUpsAndDowns() {
		return upsAndDowns;
	}
	public ArrayList<StockNameAndUpsPO> getUps_Max() {
		return ups_Max;
	}
	public ArrayList<StockNameAndUpsPO> getDowns_Max() {
		return downs_Max;
	}
	
	
}
