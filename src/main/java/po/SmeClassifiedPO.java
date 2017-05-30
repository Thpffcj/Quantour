package po;

public class SmeClassifiedPO {

	private int index;
	private String code;
	private String name;
	
	public SmeClassifiedPO() {
		super();
	}

	public SmeClassifiedPO(int index, String code, String name) {
		super();
		this.index = index;
		this.code = code;
		this.name = name;
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
}
