import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;


import po.StockPO;

public class StockTest {

	private int serial;
	private int code;
	private String date;
	private double open;
	private double close;
	private double high;
	private double low;
	private int volume;
	private double adjClose;
	private String name;
	private String market;
	
	@Test
	public void testSaveStocks() throws FileNotFoundException{
		//创建配置对象
		Configuration config = new Configuration().configure();
		//创建会话工厂对象
		SessionFactory sessionFactory = config.buildSessionFactory();
		//创建Session对象
		Session session = sessionFactory.getCurrentSession();
		//创建事务对象
		Transaction tx = session.beginTransaction();
		
		String[][] data = new String[1048576][];
		data = GetData();
		for(int i=0;i<100;i++){
			serial = Integer.parseInt(data[i][0]);
			date = data[i][1];
			open = Double.parseDouble(data[i][2]);
			close = Double.parseDouble(data[i][3]);
			high = Double.parseDouble(data[i][4]);
			low = Double.parseDouble(data[i][5]);
			volume = Integer.parseInt(data[i][6]);
			adjClose = Double.parseDouble(data[i][7]);
			code = Integer.parseInt(data[i][8]);
			name = data[i][9];
			market = data[i][10];
			StockPO spo = new StockPO(serial,date,open,close,high,low,volume,adjClose,code,name,market," ");
			
			session.save(spo);
			
		}
		tx.commit();
		sessionFactory.close();	
	}
	
	public String[][] GetData() throws FileNotFoundException{
		String[][] data = new String[10000][];
		Scanner sc = new Scanner(new File("src/main/resources/CSV/000002.csv"));
		int i = 0;
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if(!line.isEmpty()){
				data[i] = line.split("\t");
			}
			i++;
		}
		sc.close();
		return data;
	}

}
