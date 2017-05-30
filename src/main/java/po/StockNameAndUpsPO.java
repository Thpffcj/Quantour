package po;

public class StockNameAndUpsPO {
	private String name;
	private double ups;
	
	public StockNameAndUpsPO(String name,double ups){
		this.name=name;
		this.ups=ups;
	}

	public String getName() {
		return name;
	}

	public double getUps() {
		return ups;
	}
	
}
