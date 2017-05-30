package vo;

import java.util.ArrayList;

public class WayVO {
	String waystyle;
	String option;
	boolean isHold;
	int formperiod;
	int holdPeriod;
	int ema;
	int shares;
	String begin;
	String end;
	ArrayList<String> codelist;

	public WayVO(String ws, String o, boolean is, int hp, int per, int e, int s, String b,
			String End, ArrayList<String> list) {
		waystyle = ws;
		option = o;
		isHold = is;
		holdPeriod = hp;
		formperiod = per;
		ema = e;
		shares = s;
		begin = b;
		end = End;
		codelist = list;
	}
	
	public String getwaystyle(){
		return waystyle;
	}
	
	public String getoption(){
		return option;
	}

	public boolean getisHold(){
		return isHold;
	}
	
	public int getformperiod(){
		return formperiod;
	}
	
	public int getholdperiod(){
		return holdPeriod;
	}
	
	public int getema(){
		return ema;
	}
	
	public int getshares(){
		return shares;
	}
	
	public String getBegin(){
		return begin;
	}
	
	public String getend(){
		return end;
	}
	
	public ArrayList<String> getcodelist(){
		return codelist;
	}
}
