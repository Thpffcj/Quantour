package vo.quantify;

public class MeanReversionVO {
	
	private String yearRateOfReturn;
	private String benchmarkYearRateOfReturn;
	private String maximumRetracement;
	private String Alpha;
	private double beta;
	private double sharpeRatio;
	
	public MeanReversionVO() {
		
	}

	public MeanReversionVO(String yearRateOfReturn, String benchmarkYearRateOfReturn, String maximumRetracement,
			String alpha, double beta, double sharpeRatio) {
		super();
		this.yearRateOfReturn = yearRateOfReturn;
		this.benchmarkYearRateOfReturn = benchmarkYearRateOfReturn;
		this.maximumRetracement = maximumRetracement;
		Alpha = alpha;
		this.beta = beta;
		this.sharpeRatio = sharpeRatio;
	}

	public String getYearRateOfReturn() {
		return yearRateOfReturn;
	}

	public void setYearRateOfReturn(String yearRateOfReturn) {
		this.yearRateOfReturn = yearRateOfReturn;
	}

	public String getBenchmarkYearRateOfReturn() {
		return benchmarkYearRateOfReturn;
	}

	public void setBenchmarkYearRateOfReturn(String benchmarkYearRateOfReturn) {
		this.benchmarkYearRateOfReturn = benchmarkYearRateOfReturn;
	}

	public String getMaximumRetracement() {
		return maximumRetracement;
	}

	public void setMaximumRetracement(String maximumRetracement) {
		this.maximumRetracement = maximumRetracement;
	}

	public String getAlpha() {
		return Alpha;
	}

	public void setAlpha(String alpha) {
		Alpha = alpha;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public double getSharpeRatio() {
		return sharpeRatio;
	}

	public void setSharpeRatio(double sharpeRatio) {
		this.sharpeRatio = sharpeRatio;
	}

	
}
