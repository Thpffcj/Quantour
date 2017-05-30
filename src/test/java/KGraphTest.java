import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.data.time.Day;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.junit.Test;

import po.GraphPO;

public class KGraphTest {

	@Test
	public void test() {
//		KGraph k = new KGraph();
		
		OHLCSeries series = new OHLCSeries("");
		
		String data1 = "0	4/29/14	11.02	11.25	10.92	11.16	41362100	11.16	1	深发展Ａ	SZ";
		String data2 = "1	4/28/14	11.25	11.28	10.96	11.03	52604500	11.03	1	深发展Ａ	SZ";
		String data3 = "2	4/25/14	11.23	11.52	11.11	11.25	71433500	11.25	1	深发展Ａ	SZ";
		String data4 = "3	4/24/14	11.42	11.45	11.12	11.23	63400400	11.23	1	深发展Ａ	SZ";
		String data5 = "4	4/23/14	11.08	11.45	11.08	11.3	119102800	11.3	1	深发展Ａ	SZ";
		String data6 = "5	4/22/14	10.71	11.18	10.68	11.06	52328100	11.06	1	深发展Ａ	SZ";
		String data7 = "6	4/21/14	10.75	10.9	10.67	10.69	29815600	10.69	1	深发展Ａ	SZ";
		String data8 = "7	4/18/14	10.85	10.88	10.72	10.8	32354400	10.8	1	深发展Ａ	SZ";
		String data9 = "8	4/17/14	11.02	11.08	10.83	10.9	30488500	10.9	1	深发展Ａ	SZ";
		String data10 = "9	4/16/14	10.93	11.13	10.86	10.99	37664600	10.99	1	深发展Ａ	SZ";
		ArrayList<String> datas = new ArrayList<String>();
		datas.add(data1);
		datas.add(data2);
		datas.add(data3);
		datas.add(data4);
		datas.add(data5);
		datas.add(data6);
		datas.add(data7);
		datas.add(data8);
		datas.add(data9);
		datas.add(data10);
		
		
		for(int i=0;i<datas.size();i++){
			int day = 0;
			int month = 0;
			int year = 2000;
			String[] data = datas.get(i).split("	");
	    	String[] time = data[1].split("/");
	    	month = Integer.parseInt(time[0]);
	    	day = Integer.parseInt(time[1]);
	    	year = year + Integer.parseInt(time[2]);
 			series.add(new Day(day,month,year),Double.parseDouble(data[2]),Double.parseDouble(data[3]),
					Double.parseDouble(data[4]),Double.parseDouble(data[5]));
	    }
		JFrame frame=new JFrame("Java数据统计图");  
//		frame.add(k.KGraph(series));
//		frame.setBounds(50, 50, 800, 600);  
		frame.setVisible(true);  
	}

}
