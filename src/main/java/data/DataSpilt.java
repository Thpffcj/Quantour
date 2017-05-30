package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.persistence.criteria.CriteriaBuilder.Case;

import data.BCConvert;

public class DataSpilt {
	String name = "";
	
	public DataSpilt() throws IOException{
		
		Scanner sc = new Scanner(new File("src/main/resources/CSV/股票历史数据ALL.csv"));
		sc.nextLine();
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(!line.split("\t")[6].equals("0")){
				line = BCConvert.qj2bj(line);//将全角符号转为半角符号
				WriteName(line);
				WriteFile(line);
			}
		}
		sc.close();
	}
	
	public void WriteFile(String line) throws IOException{
		FileWriter fileWriter = new FileWriter("src/main/resources/CSV/"+line.split("\t")[8]+".csv",true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(line);
		bufferedWriter.newLine();
		fileWriter.flush();
		bufferedWriter.close();
	}
	
	public void WriteName(String line) throws IOException {
		String[] message = line.split("\t");
		if(!name.equals(message[9])){
			FileWriter fileWriter = new FileWriter("src/main/resources/CSV/namelist.csv",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(message[8]+"\t"+message[9]);
			bufferedWriter.newLine();
			bufferedWriter.close();
			name = message[9];
		}
	}
	
	public void SplitPlate() throws IOException{
		Scanner sc = new Scanner(new File("src/main/resources/CSV/namelist.csv"));
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			
			String[] message = line.split("\t");
			WritePlate(message[0],message[1]);
		}
		sc.close();
	}
	
	public void WritePlate(String code,String name) throws IOException{
		String Code = "";
		switch (code.length()) {
		case 6:
			Code = code;
			break;
		case 5:
			Code = "0"+code;
			break;
		case 4:
			Code = "00"+code;
			break;
		case 3:
			Code = "000"+code;
			break;
		case 2:
			Code = "0000"+code;
			break;
		case 1:
			Code = "00000"+code;
			break;
		default:
			break;
		}
		String plate = "";
		if(Code.substring(0, 3).equals("000")||Code.substring(0, 3).equals("001")){
			plate = "主板";
		}else if(Code.substring(0, 3).equals("002")){
			plate = "中小板";
		}else if(Code.substring(0, 3).equals("300")){
			plate = "创业板";
		}
		FileWriter fileWriter = new FileWriter("src/main/resources/CSV/Name.csv",true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(code+"\t"+name+"\t"+plate);
		bufferedWriter.newLine();
		fileWriter.flush();
		bufferedWriter.close();
		
	}
	
	public static void downloaddata() throws IOException{
		Scanner sc = new Scanner(new File("F:/Chrome Downloads/399006.csv"),"UTF-8");
		int i=0;
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			String[] message = line.split(",");
			String data = dealdate(message[0])+"\t"+dealprice(message[3])+"\t"+dealprice(message[4]);
			writedata(data);
		}
		sc.close();
	}
	
	public static String dealdate(String date){
		String[] array = date.split("-");
		String result = Integer.valueOf(array[1])+"/"+Integer.valueOf(array[2])+"/"+array[0].substring(2, 4);
		return result;
	}
	
	public static String dealprice(String price){
		double d = Double.valueOf(price);
		DecimalFormat dFormat = new DecimalFormat("#.00");
		return dFormat.format(d);
	}
	
	public static void writedata(String data) throws IOException{
		FileWriter fileWriter = new FileWriter("src/main/resources/CSV/399006.csv",true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(data);
		bufferedWriter.newLine();
		fileWriter.flush();
		bufferedWriter.close();
	}
	
	public static void main(String[] args) throws IOException{
//		DataSpilt dataSpilt = new DataSpilt();
//		dataSpilt.SplitPlate();
		downloaddata();
		System.out.println("2014-04-29".length());
	}
}
