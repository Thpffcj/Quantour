package businessLogicService;

import po.UserPO;

public interface UsersService {
	
	public boolean Login(UserPO user);
	
	public boolean Register(UserPO user);
	
	public boolean ChangePassword(String username,String oldpw,String newpw);
	
	public String GetPhoto(String username);
	
	public void UploadPhoto(String username,String imagename);
	
	public boolean Logout(String username);

}
