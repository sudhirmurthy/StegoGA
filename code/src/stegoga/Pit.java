package stegoga;

/*PIT : Pixel Index Table  
 * PIT holds the x, y indices along 
 * with the modified pixel value.  
 * This is then used to decode the 
 * Stego Image.
 */
public class Pit {
	//the x, y of the pixel
		private int x;
		private int y;
		
		//the delta r, delta g, delta b component of the pixel
		private int[] deltaColorValues;
		
		//Constructor for PIT
		public Pit(){
			this.x = -1;
			this.y = -1;
			this.deltaColorValues = new int[3];
			for(int i=0;i<3;i++){
				this.deltaColorValues[i]=-1000;
			}
		}
		
		public Pit(int x, int y, int[] colorDeltas ){
			this.x = x;
			this.y = y;
			this.deltaColorValues = new int[3];
			for(int i = 0;i< colorDeltas.length;i++){
				this.deltaColorValues[i] = colorDeltas[i];
			}
		}
		
		public int getX(){
			return this.x;
		}
		
		public int getY(){
			return this.y;
		}
		
		public int[] getDeltas(){
			return this.deltaColorValues;
		}
		
		public int getDecodeValue(){
			int value =-1000;
			for(int i=0;i<this.deltaColorValues.length;i++){
				if(this.deltaColorValues[i]!=-1000){
					value = this.deltaColorValues[i];
				}
			}
			return value;
		}
		
		public int getDecodedValueIndex(){
			int index = -1;
			for(int i=0;i<this.deltaColorValues.length;i++){
				if(this.deltaColorValues[i]!=-1000){
					index = i;
				}
			}
			return index;
		}
		
		@Override
		public String toString(){
			//return "x : " + this.x + ", y : "+this.y+" dred : " + this.deltaColorValues[0]  + " dgreen : " + this.deltaColorValues[1] + " dblue : " + this.deltaColorValues[2];
			return this.x + ","+this.y+","+this.deltaColorValues[0]+","+this.deltaColorValues[1]+","+this.deltaColorValues[2];	
		}
		
}
