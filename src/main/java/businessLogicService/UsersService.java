package businessLogicService;

import po.UserPO;

public interface UsersService {
	
	public String Login(String username, String password);
	
	public String Register(String username, String password1, String password2);
	
	public boolean ChangeName(String newname, String oldname);
	
	public String ChangePassword(String username,String oldpw,String newpw1,String newpw2);
	
//	public String GetPhoto(String username);
//	
//	public void UploadPhoto(String username,String imagename);
//	
	public boolean Logout(String username);

}
