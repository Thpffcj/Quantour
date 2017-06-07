package businessLogic;

import java.sql.PseudoColumnUsage;
import java.util.ArrayList;

import org.apache.struts2.components.Password;

import businessLogicService.UsersService;
import data.GetUserData;
import dataService.GetUserDataService;
import dataService.UsersDao;
import po.UserPO;

public class Users implements UsersService{
	//注入用户管理的Dao
	private UsersDao usersDao;
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	/**
	 * 登录
	 */
	public String Login(String username, String password){
//		System.out.println(666);
		if(username.equals("")||password.equals("")){
			return "null";
		}
		if(usersDao.usersLogin(username, password)){
			return "success";
		}
		return "failure";
	}
	
	/**
	 * 用户注册
	 */
	public String Register(String username, String password1, String password2) {
		if(username.equals("")||password1.equals("")||password2.equals("")){
			return "null";
		}
		if(!password1.equals(password2)){
			return "notsame";
		}
		
		ArrayList<String> namelist = usersDao.getAllUsername();
		boolean NotSame = true;
		for(int i=0;i<namelist.size();i++){
			if(username.equals(namelist.get(i))){
				NotSame = false;
			}
		}
		if(NotSame==false){
			return "falsename";
		}
		
		if(usersDao.usersRegister(username, password1)){
			return "success";
		}
		return "failure";
	}
	
	/**
	 * 修改昵称
	 * @param newname
	 * @param oldname
	 * @return
	 */
	public boolean ChangeName(String newname, String oldname){
		return usersDao.ChangeName(newname, oldname);
	}
	
	/**
	 * 修改密码
	 * @param username
	 * @param oldpw
	 * @param newpw1
	 * @param newpw2
	 * @return
	 */
	public String ChangePassword(String username,String oldpw,String newpw1,String newpw2){
		if(oldpw.equals("")||newpw1.equals("")||newpw2.equals("")){
			return "null";
		}
		if(!newpw1.equals(newpw2)){
			return "notsame";
		}
		String realpw = usersDao.getPasswordByUsername(username);
		if(!realpw.equals(oldpw)){
			return "falseold";
		}
		boolean IsSuccess = usersDao.ChangePassword(username, newpw1);
		if(IsSuccess){
			return "success";
		}
		return "failure";
	}
	
	/**
	 * 得到头像路径
	 * @param username
	 * @return
	 */
//	public String GetPhoto(String username){
//		return service.GetUserphoto(username);
//	}
	
	/**
	 * 上传头像
	 */
//	public void UploadPhoto(String username,String imagename){
//		service.ChangePhoto(username, imagename);
//	}
	
	/**
	 * 登出
	 */
	public boolean Logout(String username){
		return true;
	}
}
