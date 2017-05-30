package data;

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
