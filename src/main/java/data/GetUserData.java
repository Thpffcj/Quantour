package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import dataService.GetUserDataService;

public class GetUserData implements GetUserDataService {

	/**
	 * 根据用户名得到密码
	 * 
	 * @param username
	 * @return
	 */
	public String GetUserpassword(String username) {
		try {
			Scanner sc = new Scanner(
					new File(Thread.currentThread().getContextClassLoader().getResource("User/User.txt").getPath()),
					"UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] message = line.split("\t");
				if (message[0].equals(username)) {
					return message[1];
				}
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到用户头像路径
	 * 
	 * @param username
	 * @return
	 */
	public String GetUserphoto(String username) {
		String fileload = "";
		try {
			Scanner sc = new Scanner(
					new File(Thread.currentThread().getContextClassLoader().getResource("User/User.txt").getPath()),
					"UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] message = line.split("\t");
				if (message[0].equals(username)) {
					fileload = message[2];
					break;
				}
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(fileload.equals("photo")){
			return "file:src/main/resources/Images/" + fileload + ".png";
		}else{
			return "file:"+getClass().getClassLoader().getResource("User/"+username+".png").getPath();
		}
	}

	/**
	 * 修改密码
	 */
	public void ChangePassword(String username, String password) {
		ArrayList<String> filecontent = new ArrayList<>();
		try {
			Scanner sc = new Scanner(
					new File(Thread.currentThread().getContextClassLoader().getResource("User/User.txt").getPath()),
					"UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] message = line.split("\t");
				if (message[0].equals(username)) {
					filecontent.add(message[0] + "\t" + password + "\t" + message[2]);
				} else {
					filecontent.add(line);
				}
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// 这里还要解决线程
			PrintStream output = new PrintStream(
					new File(Thread.currentThread().getContextClassLoader().getResource("User/User.txt").getPath()),
					"UTF-8");
			for (int i = 0; i < filecontent.size(); i++) {
				output.println(filecontent.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改用户头像
	 * 
	 * @param image
	 */
	public void ChangePhoto(String username, String imagename) {
		ArrayList<String> filecontent = new ArrayList<>();
		try {
			Scanner sc = new Scanner(
					new File(Thread.currentThread().getContextClassLoader().getResource("User/User.txt").getPath()),
					"UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] message = line.split("\t");
				if (message[0].equals(username)) {
					filecontent.add(message[0] + "\t" + message[1] + "\t" + imagename);
				} else {
					filecontent.add(line);
				}
			}
			sc.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			// 这里还要解决线程
			PrintStream output = new PrintStream(
					new File(Thread.currentThread().getContextClassLoader().getResource("User/User.txt").getPath()),
					"UTF-8");
			for (int i = 0; i < filecontent.size(); i++) {
				output.println(filecontent.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		GetUserData data = new GetUserData();
		System.out.println(data.GetUserphoto("151250035"));
	}
}
