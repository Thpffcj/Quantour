package vo;

public class WebCookieVO {
	static String searchcode = "";
	
	static int page = 0;
	
	static String platetype = "";
	
	static String platename = "";
	
	public static String getSearchCode(){
		return searchcode;
	}

	public static void setSearchCode(String code){
		searchcode = code;
	}
	
	public static int getpage(){
		return page;
	}
	
	public static void setpage(int p){
		page = p;
	}
	
	public static String getplatetype(){
		return platetype;
	}
	
	public static void setplatetype(String type){
		platetype = type;
	}
	
	public static String getplatename(){
		return platename;
	}
	
	public static void setplatename(String name){
		platename = name;
	}
}
