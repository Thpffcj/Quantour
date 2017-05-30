package vo.quantify;

import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MeanReturnRateVO {

	private Integer calculationCycle;
	private String excessIncome;
	private String winningPercentage;
	
	public MeanReturnRateVO() {
		
	}

	public MeanReturnRateVO(Integer calculationCycle, String excessIncome, String winningPercentage) {
		super();
		this.calculationCycle = calculationCycle;
		this.excessIncome = excessIncome;
		this.winningPercentage = winningPercentage;
	}

	public Integer getCalculationCycle() {
		return calculationCycle;
	}

	public void setCalculationCycle(Integer calculationCycle) {
		this.calculationCycle = calculationCycle;
	}

	public String getExcessIncome() {
		return excessIncome;
	}

	public void setExcessIncome(String excessIncome) {
		this.excessIncome = excessIncome;
	}

	public String getWinningPercentage() {
		return winningPercentage;
	}

	public void setWinningPercentage(String winningPercentage) {
		this.winningPercentage = winningPercentage;
	}
	
	public IntegerProperty getCalculationCycleProperty(){
		return new SimpleIntegerProperty(calculationCycle);
	}
	
	public StringProperty getExcessincomeProperty(){
		return new SimpleStringProperty(excessIncome);
	}
	
	public StringProperty getWinningpercentageProperty(){
		return new SimpleStringProperty(winningPercentage);
	}

}
