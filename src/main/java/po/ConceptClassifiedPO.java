package po;

public class ConceptClassifiedPO {

	private String code;
	private String name;
	private String cName;
	
	public ConceptClassifiedPO() {
		super();
	}

	public ConceptClassifiedPO(String code, String name, String cName) {
		super();
		this.code = code;
		this.name = name;
		this.cName = cName;
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

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}
}
