package po;

import java.io.Serializable;

public class BasePO implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String code;
	private String date;
	private double adjOpen;
	private double adjClose;
	
	public BasePO() {
		super();
	}

	public BasePO(String code, String date, double adjOpen, double adjClose) {
		super();
		this.code = code;
		this.date = date;
		this.adjOpen = adjOpen;
		this.adjClose = adjClose;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getAdjOpen() {
		return adjOpen;
	}

	public void setAdjOpen(double adjOpen) {
		this.adjOpen = adjOpen;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}

}
