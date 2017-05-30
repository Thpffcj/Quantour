package po;

public class StockUpsAndDownsPO {

	private String name;
	private String amplitude;
	
	public StockUpsAndDownsPO() {
		
	}

	public StockUpsAndDownsPO(String name, String amplitude) {
		super();
		this.name = name;
		this.amplitude = amplitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(String amplitude) {
		this.amplitude = amplitude;
	}
}
