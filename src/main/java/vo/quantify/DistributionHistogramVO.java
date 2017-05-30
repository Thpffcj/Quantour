package vo.quantify;

public class DistributionHistogramVO {

	private double winDays;
	private double loseDays;
	private double winPercentage;
	
	public DistributionHistogramVO() {
	
	}

	public DistributionHistogramVO(double winDays, double loseDays, double winPercentage) {
		super();
		this.winDays = winDays;
		this.loseDays = loseDays;
		this.winPercentage = winPercentage;
	}

	public double getWinDays() {
		return winDays;
	}

	public void setWinDays(double winDays) {
		this.winDays = winDays;
	}

	public double getLoseDays() {
		return loseDays;
	}

	public void setLoseDays(double loseDays) {
		this.loseDays = loseDays;
	}

	public double getWinPercentage() {
		return winPercentage;
	}

	public void setWinPercentage(double winPercentage) {
		this.winPercentage = winPercentage;
	}
	
}
