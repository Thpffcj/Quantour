import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

import businessLogicService.GetComparedDataService;
import businessLogic.GetComparedData;

public class GetComparedDataTest {

	GetComparedDataService gcds = new GetComparedData();
	
	@Test
	public void testGetLowestValue(){
		gcds.getLowestValue("S ST华新", "深发展A", "1/20/14", "4/29/14");
	}
	
	@Test
	public void getLowestValuetest() {
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
		
		dataset1.addValue(10.59,"深发展A","4/4/14");
		dataset1.addValue(6.28,"S ST华新","4/4/14");
		dataset1.addValue(10.78,"深发展A","4/8/14");
		dataset1.addValue(6.33,"S ST华新","4/8/14");
		dataset1.addValue(11.16,"深发展A","4/9/14");
		dataset1.addValue(6.37,"S ST华新","4/9/14");
		dataset1.addValue(11.16,"深发展A","4/10/14");
		dataset1.addValue(6.4,"S ST华新","4/10/14");
		
		ArrayList<Number> data1 = new ArrayList<Number>();

		for(int i=0;i<dataset1.getRowCount();i++){
			for(int j=0;j<dataset1.getColumnCount();j++){
				data1.add(dataset1.getValue(i, j));
			}
		}
		
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		GetComparedDataService cds = new GetComparedData();
		dataset2=cds.getLowestValue("1", "10", "4/4/14", "4/10/14");
		
		ArrayList<Number> data2 = new ArrayList<Number>();

		for(int i=0;i<dataset2.getRowCount();i++){
			for(int j=0;j<dataset2.getColumnCount();j++){
				data2.add(dataset2.getValue(i, j));
			}
		}

		
		assertEquals(true,dataset1.getColumnKeys().equals(dataset2.getColumnKeys()));
		assertEquals(true,dataset1.getRowKeys().equals(dataset2.getRowKeys()));
		assertEquals(true,data1.equals(data2));
	}

	
	@Test
	public void getGetLowesttest() {
		String low1 = "10.59"+" "+"6.28";
		
		GetComparedDataService cds = new GetComparedData();
		String low2 = cds.GetLowest("1", "10", "4/4/14", "4/10/14");

		assertEquals(true,low1.equals(low2));
	}
	
	
	@Test
	public void getHighestValuetest(){
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
		
		dataset1.addValue(10.82,"深发展A","4/4/14");
		dataset1.addValue(6.42,"S ST华新","4/4/14");
		dataset1.addValue(11.49,"深发展A","4/8/14");
		dataset1.addValue(6.44,"S ST华新","4/8/14");
		dataset1.addValue(11.35,"深发展A","4/9/14");
		dataset1.addValue(6.47,"S ST华新","4/9/14");
		dataset1.addValue(11.6,"深发展A","4/10/14");
		dataset1.addValue(6.47,"S ST华新","4/10/14");
		
		ArrayList<Number> data1 = new ArrayList<Number>();

		for(int i=0;i<dataset1.getRowCount();i++){
			for(int j=0;j<dataset1.getColumnCount();j++){
				data1.add(dataset1.getValue(i, j));
			}
		}
		
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		GetComparedDataService cds = new GetComparedData();
		dataset2=cds.getHighestValue("深发展A", "S ST华新", "4/4/14", "4/10/14");
		
		ArrayList<Number> data2 = new ArrayList<Number>();

		for(int i=0;i<dataset2.getRowCount();i++){
			for(int j=0;j<dataset2.getColumnCount();j++){
				data2.add(dataset2.getValue(i, j));
			}
		}

		assertEquals(true,dataset1.getColumnKeys().equals(dataset2.getColumnKeys()));
		assertEquals(true,dataset1.getRowKeys().equals(dataset2.getRowKeys()));
		assertEquals(true,data1.equals(data2));
	}
	
	
	@Test
	public void getGetHighesttest() {
		String low1 = "11.6"+" "+"6.47";
		
		GetComparedDataService cds = new GetComparedData();
		String low2 = cds.GetHighest("1", "10", "4/4/14", "4/10/14");

		assertEquals(true,low1.equals(low2));
	}
	
	
	@Test
	public void getRoseAndDropValuetest(){
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
		
		dataset1.addValue(4.541242,"深发展A","4/8/14");
		dataset1.addValue(-0.468019,"S ST华新","4/8/14");
		dataset1.addValue(-0.797872,"深发展A","4/9/14");
		dataset1.addValue(1.097179,"S ST华新","4/9/14");
		dataset1.addValue(1.519214,"深发展A","4/10/14");
		dataset1.addValue(-0.155039,"S ST华新","4/10/14");
		dataset1.addValue(0.352113,"深发展A","4/11/14");
		dataset1.addValue(-1.242236,"S ST华新","4/11/14");
		dataset1.addValue(-1.052632,"深发展A","4/14/14");
		dataset1.addValue(1.100629,"S ST华新","4/14/14");
		
		ArrayList<Number> data1 = new ArrayList<Number>();

		for(int i=0;i<dataset1.getRowCount();i++){
			for(int j=0;j<dataset1.getColumnCount();j++){
				data1.add(dataset1.getValue(i, j));
			}
		}
		
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		GetComparedDataService cds = new GetComparedData();
		dataset2=cds.getRoseAndDropValue("深发展A", "S ST华新", "4/7/14", "4/14/14");
		
		ArrayList<Number> data2 = new ArrayList<Number>();

		for(int i=0;i<dataset2.getRowCount();i++){
			for(int j=0;j<dataset2.getColumnCount();j++){
				data2.add(dataset2.getValue(i, j));
			}
		}
		
		assertEquals(true,data1.equals(data2));
		assertEquals(true,dataset1.getColumnKeys().equals(dataset2.getColumnKeys()));
		assertEquals(true,dataset1.getRowKeys().equals(dataset2.getRowKeys()));
	}
	
	
	@Test
	public void getGetRoseAndDroptest() {
		String change1 = "4.541242"+" "+"0.312012";
		
		GetComparedDataService cds = new GetComparedData();
		String change2 = cds.getRoseAndDrop("1", "10", "4/7/14", "4/14/14");

		assertEquals(true,change1.equals(change2));
	}
	
	
	@Test
	public void getCloseValuetest() {
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
		
		dataset1.addValue(10.79,"深发展A","4/4/14");
		dataset1.addValue(6.41,"S ST华新","4/4/14");
		dataset1.addValue(11.28,"深发展A","4/8/14");
		dataset1.addValue(6.38,"S ST华新","4/8/14");
		dataset1.addValue(11.19,"深发展A","4/9/14");
		dataset1.addValue(6.45,"S ST华新","4/9/14");
		dataset1.addValue(11.36,"深发展A","4/10/14");
		dataset1.addValue(6.44,"S ST华新","4/10/14");
		
		ArrayList<Number> data1 = new ArrayList<Number>();

		for(int i=0;i<dataset1.getRowCount();i++){
			for(int j=0;j<dataset1.getColumnCount();j++){
				data1.add(dataset1.getValue(i, j));
			}
		}
		
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		GetComparedDataService cds = new GetComparedData();
		dataset2=cds.getCloseValue("1", "10", "4/4/14", "4/10/14");
		
		ArrayList<Number> data2 = new ArrayList<Number>();

		for(int i=0;i<dataset2.getRowCount();i++){
			for(int j=0;j<dataset2.getColumnCount();j++){
				data2.add(dataset2.getValue(i, j));
			}
		}
		
		assertEquals(true,data1.equals(data2));
		assertEquals(true,dataset1.getColumnKeys().equals(dataset2.getColumnKeys()));
		assertEquals(true,dataset1.getRowKeys().equals(dataset2.getRowKeys()));
		
	}
	
	@Test
	public void getRateOfReturntest() {
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
		
		dataset1.addValue(0.185529,"深发展A","4/4/14");
		dataset1.addValue(1.730964,"S ST华新","4/4/14");
		dataset1.addValue(4.441147,"深发展A","4/8/14");
		dataset1.addValue(-0.469117,"S ST华新","4/8/14");
		dataset1.addValue(-0.801072,"深发展A","4/9/14");
		dataset1.addValue(1.091203,"S ST华新","4/9/14");
		dataset1.addValue(1.507789,"深发展A","4/10/14");
		dataset1.addValue(-0.155159,"S ST华新","4/10/14");
		
		ArrayList<Number> data1 = new ArrayList<Number>();

		for(int i=0;i<dataset1.getRowCount();i++){
			for(int j=0;j<dataset1.getColumnCount();j++){
				data1.add(dataset1.getValue(i, j));
			}
		}
		
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		GetComparedDataService cds = new GetComparedData();
		dataset2=cds.getRateOfReturn("1", "10", "4/4/14", "4/10/14");
		
		ArrayList<Number> data2 = new ArrayList<Number>();

		for(int i=0;i<dataset2.getRowCount();i++){
			for(int j=0;j<dataset2.getColumnCount();j++){
				data2.add(dataset2.getValue(i, j));
			}
		}
		
		assertEquals(true,data1.equals(data2));
		assertEquals(true,dataset1.getColumnKeys().equals(dataset2.getColumnKeys()));
		assertEquals(true,dataset1.getRowKeys().equals(dataset2.getRowKeys()));
		
	}
	
	
	@Test
	public void getVariancetest() {
		String var1 = "5.450741"+"    "+"1.119204";
		
		GetComparedDataService cds = new GetComparedData();
		String var2 = cds.getVariance("1", "10", "4/4/14", "4/10/14");

		assertEquals(true,var1.equals(var2));
	}
	
}
