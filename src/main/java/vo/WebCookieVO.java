package vo;

public class WebCookieVO {
	static String searchcode = "";
	
	public static String getSearchCode(){
		return searchcode;
	}

	public static void setSearchCode(String code){
		searchcode = code;
	}
}
