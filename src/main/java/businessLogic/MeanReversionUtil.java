package businessLogic;

public class MeanReversionUtil {
	
	 public void heapSort(double[] array) {  
         if (array == null || array.length <= 1) {  
             return;  
         }  

         buildMaxHeap(array);  

         for (int i = array.length - 1; i >= 1; i--) {  
             double temp = array[0];  
             array[0] = array[i];  
             array[i] = temp;  
             maxHeap(array, i, 0);  
         }  
     }  

     private void buildMaxHeap(double[] array) {  
         if (array == null || array.length <= 1) {  
             return;  
         }  

         int half = array.length / 2;  
         for (int i = half; i >= 0; i--) {  
             maxHeap(array, array.length, i);  
         }  
     }  

     private void maxHeap(double[] array, int heapSize, int index) {  
         int left = index * 2 + 1;  
         int right = index * 2 + 2;  

         int largest = index;  
         if (left < heapSize && array[left] > array[index]) {  
             largest = left;  
         }  

         if (right < heapSize && array[right] > array[largest]) {  
             largest = right;  
         }  

         if (index != largest) {  
        	 double temp = array[index];  
             array[index] = array[largest];  
             array[largest] = temp;  
             maxHeap(array, heapSize, largest);  
         }  
     } 
}
