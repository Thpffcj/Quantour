package po;

public class AreaClassifiedPO {

	private int index;
	private String code;
	private String name;
	private String area;
	
	public AreaClassifiedPO() {
		super();
	}

	public AreaClassifiedPO(int index, String code, String name, String area) {
		super();
		this.index = index;
		this.code = code;
		this.name = name;
		this.area = area;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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
