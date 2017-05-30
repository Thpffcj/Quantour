package presentation;

import java.util.ArrayList;

import javafx.stage.Stage;
import vo.WayVO;

/**
 * 
 * @author 费慧通
 *静态保留主界面
 */
public class StageRepertory {
	
	//主界面
	private static javafx.stage.Stage mainstage;
	
	//用户名
	private static String username;
	
	//弹框显示内容
	private static String text;
	
	//股票池选择初始化
	private static int i;
	
	//股票池
	private static ArrayList<String> codelist;
	
	//WayVO
	private static WayVO wayVO;
	
	//进度条
	private static Stage progressframe;
	
	
	public static void setStage(javafx.stage.Stage stage){
		mainstage = stage;
	}
	
	public static javafx.stage.Stage getStage(){
		return mainstage;
	}
	
	public static void setusername(String s){
		username = s;
	}
	
	public static String getusername(){
		return username;
	}
	
	public static void settext(String t){
		text = t;
	}
	
	public static String gettext(){
		return text;
	}
	
	public static void seti(int x){
		i = x;
	}
	
	public static int geti(){
		return i;
	}
	
	public static void setcodelist(ArrayList<String> list){
		codelist = list;
	}
	
	public static ArrayList<String> getcodelist(){
		return codelist;
	}
	
	public static void setWayVO(WayVO vo){
		wayVO = vo;
	}
	
	public static WayVO getwayVO(){
		return wayVO;
	}
	
	public static void setProgressFrame(Stage stage){
		progressframe = stage;
	}
	
	public static Stage getProgressFrame(){
		return progressframe;
	}
}
