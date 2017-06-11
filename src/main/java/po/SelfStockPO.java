package po;

import java.io.Serializable;

public class SelfStockPO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	
	private String code;
	
	public SelfStockPO(){
		
	}
	
	public SelfStockPO(String code, String name){
		super();
		this.code = code;
		this.username = name;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
