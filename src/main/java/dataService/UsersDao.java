package dataService;

import po.UserPO;

public interface UsersDao{

	public boolean usersLogin(String username, String password);
	
	public boolean usersRegister(String username, String password);
}
