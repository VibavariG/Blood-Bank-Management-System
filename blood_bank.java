/*Roll No: 2325
 * Title : Blood Bank
 * Batch : A2
 */

import java.util.*;

class vertex{
	int number;
	String[] bloodgroups = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};			//8 types of blood groups
	int[] quantity = new int[8];
	
	vertex(int n) {
		number = n;
		for(int i=0;i<8;i++) {
			quantity[i] = 0;
		}
	}
};

class check{
	int[] pred;
	int[] visited;
	int[] dist;
	
	check(int n){
		pred = new int[n];
		dist = new int[n];
		visited = new int[n];
	}
}

class find_nearest{
	vertex[] v;
	//map of the city								//storing the connected hospitals in an adjacency matrix
	int adj[][]=new int[][] { {0,4,0,0,0,0,0,8,0},							
			{4,0,8,0,0,0,0,11,0},
			{0,8,0,7,0,4,0,0,2},
			{0,0,7,0,9,14,0,0,0},
			{0,0,0,9,0,10,0,0,0},
			{0,0,4,14,10,0,2,0,0},
			{0,0,0,0,0,2,0,1,6},
			{8,11,0,0,0,0,1,0,7},
			{0,0,2,0,0,0,6,7,0}
	};
	int n=9; 						//number of hospitals
	int e=14; 						//number of edges
	check c=new check(n);
	Scanner sc = new Scanner(System.in);

	
	
	void intialize() //allocate memory to the vertex array
	{
		v = new vertex[n];
		for(int i=0;i<n;i++) 
			v[i] = new vertex(i+1);
		
	}

	void have_blood()    //Accepting donated blood
	{
		int id, no, qty, bld;
		System.out.print("Enter your blood bank id: ");
		id = sc.nextInt();
		System.out.print("How many blood groups do you have the blood for?: ");
		no = sc.nextInt();
		
		System.out.println("==========================");
		System.out.println("1-> A+\t\t 2-> A-");
		System.out.println("3-> B+\t\t 4-> B-");
		System.out.println("5-> O+\t\t 6-> O-");
		System.out.println("7-> AB+\t\t 8-> AB-");
		System.out.println("==========================");


		for(int i=0;i<no;i++) {
			//update the qty of each blood grp the user has
			System.out.println();
			System.out.print("Choose from the Reference table above: ");
			bld = sc.nextInt();
			System.out.print("Enter number of bags of this blood: ");
			qty = sc.nextInt();
			System.out.println();
			v[id-1].quantity[bld-1]+=qty;
		}
	}
	
	void need_blood()       // Shows nearest available blood bank 
	{
		int id, bld, qty;
		
		System.out.print("Enter your blood bank id: ");
		id = sc.nextInt();
		System.out.println("1-> A+\t\t 2-> A-");
		System.out.println("3-> B+\t\t 4-> B-");
		System.out.println("5-> O+\t\t 6-> O-");
		System.out.println("7-> AB+\t\t8-> AB-");
		System.out.print("Choose the blood group that you need: ");
		bld = sc.nextInt();
		System.out.print("How many bags of that blood do you need?: ");
		qty = sc.nextInt();
		
		//find the nearest blood bank
		//find distance of all hospitals from source
		dijkstra(id);
		//sort all the hospitals according to their distances
		int[] ids = new int[n];
		int[] sorted_dist = new int[n];
		
		//copying all distances into sorted_dist
		for(int i=0;i<n;i++) {
			sorted_dist[i] = c.dist[i];
			ids[i] = i+1;
		}
		
		//sorting sorted_dist
		int t_dist, t_id;
		for(int j=0;j<n-1;j++) {
			for(int i=0;i<n-j-1;i++)
			{
				if(sorted_dist[i] > sorted_dist[i+1])			//checking each element with the next element
				{
					t_dist =sorted_dist[i];
					t_id=ids[i];

					ids[i]=ids[i+1];
					sorted_dist[i]=sorted_dist[i+1];

					ids[i+1]=t_id;
					sorted_dist[i+1]=t_dist;
				}
			}
		}
		
		//now we know hospitals that are nearest
		//find if they have the required blood group or not
		int flag=0;
		for(int i=0;i<n;i++) {
			if(sorted_dist[i] == 0) {
				continue;
			}
			if(v[ids[i]-1].quantity[bld-1] >= qty) {
				//found required nearest blood bank
				System.out.println("\n=========================================================================");
				System.out.println("Hospital id "+ids[i]+" is the nearest blood bank with "+v[ids[i]-1].bloodgroups[bld-1]+" blood.");
				System.out.println("Distance: "+sorted_dist[i]);
				System.out.println("=========================================================================\n");
				//updating the quantities
				v[ids[i]-1].quantity[bld-1]-=qty;
				v[id-1].quantity[bld-1]+=qty;
				flag=1;
				break;
			}
			else {
				continue;
			}
		}
		if(flag==0) {
			System.out.println("=================================================");
			System.out.println("Sorry, no blood bank found with required blood. ");
			System.out.println("=================================================");
		}
	}
	
	void dijkstra(int source) //Finds shortest route from customer to bank
	{
		int s;
		s = source;
		//initialize pred, dist and visited everytime
		for(int i=0;i<n;i++) {
			c.visited[i] =0;
			c.pred[i] =0;
			c.dist[i]=0;
		}
		
		//start dijkstra algo
		c.visited[source-1] = 1;
		for(int i=0;i<n;i++) {
			if(i!=source-1) {
				c.dist[i] = 999;				//initialize all others to infinity
			}
		}
		int v=1;
		while(v<n) {
			for(int i=0;i<n;i++) {
				if(adj[s-1][i]!=0 && c.visited[i]!=1) {
					if(c.pred[i]!=0) {				//if pred exists
						if(adj[s-1][i] + c.dist[s-1] < c.dist[i]) {
							c.pred[i]=s;
							c.dist[i] = adj[s-1][i] + c.dist[s-1];
						}
					}
					else {							//if no pred
						if(adj[s-1][i] < c.dist[i]) {
							c.pred[i]=s;
							if(s==source) {
								c.dist[i] = adj[s-1][i];
							}
							else {
								c.dist[i] = adj[s-1][i] + c.dist[s-1];
							}
							
						}
					}
				}
			}
			
			int min=999;
			int pos =0;
			for(int i=0;i<n;i++) {
				if(c.visited[i]!=1) {
					if(c.dist[i] < min) {
						pos=i;
						min =c.dist[i];
					}
				}
			}
			c.visited[pos] = 1;
			v++;
			s = pos+1;
		}
		disp_result();
	}
	
	void disp_result() {
		System.out.println("Vertex\tDistance from Source");
		for(int i=0;i<n;i++) {
			System.out.println((i+1)+"\t\t\t"+c.dist[i]);
		}
	}
	
	void display_all_bloodbanks()  //displays all the blood banks' info
	{
		System.out.println("ID \t BLOOD \t QTY");
		for(int i=0;i<n;i++) {
			System.out.print(i+1+" \t ");
			for(int j=0; j<8;j++) {
				if(j==0)
					System.out.println(v[i].bloodgroups[j]+" \t "+ v[i].quantity[j]);
				else {
					System.out.println("  \t "+v[i].bloodgroups[j]+" \t "+ v[i].quantity[j]);
				}
			}
			System.out.println("---------------------------------------------------------------");
		}
	}
}

public class blood_bank {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int ch1=0;
		find_nearest f = new find_nearest();
		f.intialize();
		do {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("1. Have Blood");
			System.out.println("2. Need Blood");
			System.out.println("3. Display all Blood Banks in the city");
			System.out.println("4.Exit");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.print("Enter your choice: ");
			ch1 = sc.nextInt();
			System.out.println();
			switch(ch1) {
			case 1:
				f.have_blood();
				break;
			case 2:
				f.need_blood();
				break;
			case 3:
				f.display_all_bloodbanks();
				break;
			case 4:
				System.out.println("Thank You!!");
				break;
			default:
				System.out.println("Wrong Input");	
			
			}
		}while(ch1!=4);
		sc.close();
	}
}
/*
 * ~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 1

Enter your blood bank id: 1
How many blood groups do you have the blood for?: 3
==========================
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		 8-> AB-
==========================

Choose from the Reference table above: 6
Enter number of bags of this blood: 2


Choose from the Reference table above: 7
Enter number of bags of this blood: 1


Choose from the Reference table above: 5
Enter number of bags of this blood: 4

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 1

Enter your blood bank id: 2
How many blood groups do you have the blood for?: 4
==========================
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		 8-> AB-
==========================

Choose from the Reference table above: 1
Enter number of bags of this blood: 4


Choose from the Reference table above: 4
Enter number of bags of this blood: 1


Choose from the Reference table above: 8
Enter number of bags of this blood: 10


Choose from the Reference table above: 5
Enter number of bags of this blood: 1

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 1

Enter your blood bank id: 3
How many blood groups do you have the blood for?: 2
==========================
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		 8-> AB-
==========================

Choose from the Reference table above: 7
Enter number of bags of this blood: 11


Choose from the Reference table above: 8
Enter number of bags of this blood: 13

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 1

Enter your blood bank id: 4
How many blood groups do you have the blood for?: 8
==========================
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		 8-> AB-
==========================

Choose from the Reference table above: 1
Enter number of bags of this blood: 6


Choose from the Reference table above: 2
Enter number of bags of this blood: 6


Choose from the Reference table above: 3
Enter number of bags of this blood: 6


Choose from the Reference table above: 4
Enter number of bags of this blood: 6


Choose from the Reference table above: 5
Enter number of bags of this blood: 6


Choose from the Reference table above: 6
Enter number of bags of this blood: 6


Choose from the Reference table above: 7
Enter number of bags of this blood: 6


Choose from the Reference table above: 8
Enter number of bags of this blood: 6

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 1

Enter your blood bank id: 5
How many blood groups do you have the blood for?: 1
==========================
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		 8-> AB-
==========================

Choose from the Reference table above: 4
Enter number of bags of this blood: 30

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 1

Enter your blood bank id: 6
How many blood groups do you have the blood for?: 5
==========================
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		 8-> AB-
==========================

Choose from the Reference table above: 1
Enter number of bags of this blood: 20


Choose from the Reference table above: 3
Enter number of bags of this blood: 15


Choose from the Reference table above: 4
Enter number of bags of this blood: 19


Choose from the Reference table above: 5
Enter number of bags of this blood: 2


Choose from the Reference table above: 6
Enter number of bags of this blood: 1

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 1

Enter your blood bank id: 7
How many blood groups do you have the blood for?: 6
==========================
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		 8-> AB-
==========================

Choose from the Reference table above: 3
Enter number of bags of this blood: 9


Choose from the Reference table above: 4
Enter number of bags of this blood: 9


Choose from the Reference table above: 5
Enter number of bags of this blood: 9


Choose from the Reference table above: 6
Enter number of bags of this blood: 9


Choose from the Reference table above: 7
Enter number of bags of this blood: 9


Choose from the Reference table above: 8
Enter number of bags of this blood: 9

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 1

Enter your blood bank id: 8
How many blood groups do you have the blood for?: 8
==========================
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		 8-> AB-
==========================

Choose from the Reference table above: 1
Enter number of bags of this blood: 10


Choose from the Reference table above: 2
Enter number of bags of this blood: 10


Choose from the Reference table above: 3
Enter number of bags of this blood: 10


Choose from the Reference table above: 4
Enter number of bags of this blood: 10


Choose from the Reference table above: 5
Enter number of bags of this blood: 10


Choose from the Reference table above: 6
Enter number of bags of this blood: 10


Choose from the Reference table above: 7
Enter number of bags of this blood: 10


Choose from the Reference table above: 8
Enter number of bags of this blood: 10

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 1

Enter your blood bank id: 9
How many blood groups do you have the blood for?: 7
==========================
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		 8-> AB-
==========================

Choose from the Reference table above: 1
Enter number of bags of this blood: 5


Choose from the Reference table above: 2
Enter number of bags of this blood: 6


Choose from the Reference table above: 3
Enter number of bags of this blood: 4


Choose from the Reference table above: 5
Enter number of bags of this blood: 9


Choose from the Reference table above: 4
Enter number of bags of this blood: 6


Choose from the Reference table above: 7
Enter number of bags of this blood: 11


Choose from the Reference table above: 6
Enter number of bags of this blood: 3

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 2

Enter your blood bank id: 3
1-> A+		 2-> A-
3-> B+		 4-> B-
5-> O+		 6-> O-
7-> AB+		8-> AB-
Choose the blood group that you need: 1
How many bags of that blood do you need?: 2
Vertex	Distance from Source
1			12
2			8
3			0
4			7
5			14
6			4
7			6
8			7
9			2

=========================================================================
Hospital id 9 is the nearest blood bank with A+ blood.
Distance: 2
=========================================================================

~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 3

ID 	 BLOOD 	 QTY
1 	 A+ 	 0
  	 A- 	 0
  	 B+ 	 0
  	 B- 	 0
  	 O+ 	 4
  	 O- 	 2
  	 AB+ 	 1
  	 AB- 	 0
---------------------------------------------------------------
2 	 A+ 	 4
  	 A- 	 0
  	 B+ 	 0
  	 B- 	 1
  	 O+ 	 1
  	 O- 	 0
  	 AB+ 	 0
  	 AB- 	 10
---------------------------------------------------------------
3 	 A+ 	 2
  	 A- 	 0
  	 B+ 	 0
  	 B- 	 0
  	 O+ 	 0
  	 O- 	 0
  	 AB+ 	 11
  	 AB- 	 13
---------------------------------------------------------------
4 	 A+ 	 6
  	 A- 	 6
  	 B+ 	 6
  	 B- 	 6
  	 O+ 	 6
  	 O- 	 6
  	 AB+ 	 6
  	 AB- 	 6
---------------------------------------------------------------
5 	 A+ 	 0
  	 A- 	 0
  	 B+ 	 0
  	 B- 	 30
  	 O+ 	 0
  	 O- 	 0
  	 AB+ 	 0
  	 AB- 	 0
---------------------------------------------------------------
6 	 A+ 	 20
  	 A- 	 0
  	 B+ 	 15
  	 B- 	 19
  	 O+ 	 2
  	 O- 	 1
  	 AB+ 	 0
  	 AB- 	 0
---------------------------------------------------------------
7 	 A+ 	 0
  	 A- 	 0
  	 B+ 	 9
  	 B- 	 9
  	 O+ 	 9
  	 O- 	 9
  	 AB+ 	 9
  	 AB- 	 9
---------------------------------------------------------------
8 	 A+ 	 10
  	 A- 	 10
  	 B+ 	 10
  	 B- 	 10
  	 O+ 	 10
  	 O- 	 10
  	 AB+ 	 10
  	 AB- 	 10
---------------------------------------------------------------
9 	 A+ 	 3
  	 A- 	 6
  	 B+ 	 4
  	 B- 	 6
  	 O+ 	 9
  	 O- 	 3
  	 AB+ 	 11
  	 AB- 	 0
---------------------------------------------------------------
~~~~~~~~~~~~~~~~~~~~~~~
1. Have Blood
2. Need Blood
3. Display all Blood Banks in the city
4.Exit
~~~~~~~~~~~~~~~~~~~~~~~
Enter your choice: 4

Thank You!!
*/
 
