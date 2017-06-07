package data;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dataService.UsersDao;
import po.UserPO;

public class UsersDaoImpl implements UsersDao{

	private	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public boolean usersLogin(String username, String password) {
		String hql = "";
		try{
			Session session = sessionFactory.openSession();
			hql = "from UserPO where username=? and password=? ";
			Query query = session.createQuery(hql);
			query.setParameter(0, username);
			query.setParameter(1, password);
			List list = query.list();
			if(list.size()>0){
				return true;
			}else{
				return false;
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	public boolean usersRegister(String username, String password) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		UserPO user = new UserPO();
		user.setUid(getNewUid());
		user.setUsername(username);
		user.setPassword(password);
		session.save(user);
		tx.commit();
		if(tx != null){
			tx = null;
		}
		return true;
	}
	
	/**
	 * 修改昵称
	 */
	public boolean ChangeName(String newname, String oldname){
		Session session = sessionFactory.openSession();
		String h = "select u.username from UserPO u";
		Query q = session.createQuery(h);
		ArrayList<String> namelist = new ArrayList<>();
		namelist = (ArrayList<String>) q.getResultList();
		for(int i=0;i<namelist.size();i++){
			if(newname.equals(namelist.get(i))){
				return false;
			}
		}
		
		Transaction tx = session.beginTransaction();
		String hql = "update UserPO u set u.username='"+newname+"'where u.username=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, oldname);
		int num = query.executeUpdate();
		System.out.println(num);
		tx.commit();
		if(num<=0){
			return false;
		}
		return true;
	}
	
	/**
	 * 根据用户名得到密码
	 * @param username
	 * @return
	 */
	public String getPasswordByUsername(String username){
		String hql = "";
		try{
			Session session = sessionFactory.openSession();
			hql = "select password from UserPO where username=?";
			Query query = session.createQuery(hql);
			query.setParameter(0, username);
			String password = (String) query.getSingleResult();
			return password;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 得到所有用户名
	 * @return
	 */
	public ArrayList<String> getAllUsername(){
		Session session = sessionFactory.openSession();
		String h = "select u.username from UserPO u";
		Query q = session.createQuery(h);
		ArrayList<String> namelist = new ArrayList<>();
		namelist = (ArrayList<String>) q.getResultList();
		return namelist;
	}
	
	/**
	 * 修改密码
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean ChangePassword(String username,String password){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "update UserPO u set u.password='"+password+"'where u.username=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, username);
		int num = query.executeUpdate();
		System.out.println(num);
		tx.commit();
		if(num<=0){
			return false;
		}
		return true;
	}
	
	private int getNewUid() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int sid = 0;
		String hql = "select max(uid) from UserPO";
		Query query = session.createQuery(hql);
		if(query.equals(null)){
			return 0;
		}
		sid = (int) query.uniqueResult();
		sid = sid + 1;
		tx.commit();
		return sid;
	}
	
	public static void main(String[] args) {
		UsersDaoImpl udi = new UsersDaoImpl();
		System.out.println(udi.usersLogin("1", "1"));
	}
}
