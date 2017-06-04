package po;

public class AreaClassifiedPO {

	private String code;
	private String name;
	private String area;
	
	public AreaClassifiedPO() {
		super();
	}

	public AreaClassifiedPO(String code, String name, String area) {
		super();
		this.code = code;
		this.name = name;
		this.area = area;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
}
