package businessLogic;

import businessLogicService.UsersService;
import data.GetUserData;
import dataService.GetUserDataService;
import dataService.UsersDao;
import po.UserPO;

public class Users implements UsersService{
	GetUserDataService service = new GetUserData();
	
	//注入用户管理的Dao
	private UsersDao usersDao;
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	/**
	 * 登录
	 */
	public boolean Login(UserPO user){
//		System.out.println(666);
		if(usersDao.usersLogin(user.getUsername(), user.getPassword())){
			return true;
		}
		return false;
	}
	
	/**
	 * 注册
	 */
	public boolean Register(UserPO user) {
		if(usersDao.usersRegister(user.getUsername(), user.getPassword())){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改密码
	 */
	public boolean ChangePassword(String username,String oldpw,String newpw){
		String userpassword = service.GetUserpassword(username);
		if(oldpw.equals(userpassword)){
			service.ChangePassword(username, newpw);
			return true;
		}
		return false;
	}
	
	/**
	 * 得到头像路径
	 * @param username
	 * @return
	 */
	public String GetPhoto(String username){
		return service.GetUserphoto(username);
	}
	
	/**
	 * 上传头像
	 */
	public void UploadPhoto(String username,String imagename){
		service.ChangePhoto(username, imagename);
	}
	
	/**
	 * 登出
	 */
	public boolean Logout(String username){
		return true;
	}
}
