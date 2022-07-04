/* Name: Julius Czar Makiling
*  CSCC 32A: Operating Systems
*/

import java.util.*;

public class processSched {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        System.out.println("CHOOSE ALGORITHM ");
        System.out.println("Input 1 for First Come First Served (Non-preemptive)");
        System.out.println("Input 2 for Shortest Job First (Preemptive)");
        System.out.print("Choice: ");
        int choice = input.nextInt();

        System.out.print("Input number of processes: ");
        int num = input.nextInt();
        int totalBurst = 0;
        String[] processes = new String[num];           //array for process names
        int[] arrival = new int[num];                   //array for arrival times 
        int[] burst = new int[num];                     //array for burst time  
        int min = 100;                                  //initialize least arrival time
        
        if (choice==1){
            //input processes, arrival time, burst
            for(int y = 0; y<num; y++){
                processes[y] = input.next();
                arrival[y] = input.nextInt();
                burst[y] = input.nextInt();
                totalBurst = totalBurst + burst[y];
                if (arrival[y]<min){
                    min = arrival[y];
                }
            }
            System.out.println("----------");
            System.out.println("----------");
            System.out.println("----------");
            fcfs(num, arrival, processes, burst, totalBurst, min);
        } else if (choice==2){
            for(int y = 0; y<num; y++){
                processes[y] = input.next();
                arrival[y] = input.nextInt();
                burst[y] = input.nextInt();
                totalBurst = totalBurst + burst[y];
                if (arrival[y]<min){
                    min = arrival[y];
                }
            }
            System.out.println("----------");
            System.out.println("----------");
            System.out.println("----------");
            sjf(num, arrival, processes, burst, totalBurst, min);
        } else {
            System.out.println("Please input valid number.");
        }
        input.close();
        
    }

    //first come first served
    public static void fcfs(int num, int[] arrival, String[] processes, int[] burst, int totalBurst, int min){
        ArrayDeque<String> queue = new ArrayDeque<String>(); //array for current running processes
        System.out.print("T");
        System.out.printf("%8s", "RUNNING");
        System.out.printf("%8s", "READY");
        int ctr = min;
        int temp;
        String tempo;
        int total = totalBurst+min;

        //sort according to arrival time
        for(int x = 0; x < num; x++){
            for(int y = x; y < num; y++){
                if(arrival[y]<arrival[x]){
                    temp = arrival[x];
                    arrival[x] = arrival[y];
                    arrival[y] = temp;

                    temp = burst[x];
                    burst[x] = burst[y];
                    burst[y] = temp;

                    tempo = processes[x];
                    processes[x] = processes[y];
                    processes[y] = tempo;
                }
            }
        }

        int[] bClone = burst.clone();   //clone burst time to avoid conflict
        int[] tFinished = new int[num]; //time finished for each process
        while(ctr!=total){
            
            for(int x=min; x < total; x++){
                //ctr is arrival time and adds to queue as needed
                for(int i=0; i<num; i++){
                    if (arrival[i]==ctr){
                        queue.addLast(processes[i]); //arrives
                    }
                }
                //decrements burst time of the top of the queue
                if(queue.isEmpty()==false){
                    for(int y=0; y < num; y++){
                        if((queue.getFirst().equals(processes[y])) && (bClone[y]!=0)){ 
                            //if head of queue matches the process name then decrement burst time
                            bClone[y]--;
                            System.out.println();
                            System.out.print(ctr);
                            System.out.printf("%6s", processes[y]);
                            System.out.printf("%5s", " ");
                            Object[] arr = queue.toArray(); 
                            for(int i=1; i<arr.length ; i++){ 
                                System.out.print(" " + arr[i]);
                            } 
                            break;
                        } else if ((queue.getFirst().equals(processes[y])) && (bClone[y]==0)){
                            //if burst time is zero then process is done and removed from queue
                            queue.remove();
                            tFinished[y]=x-1;
                            bClone[y+1]--;
                            System.out.println();
                            System.out.print(ctr);
                            System.out.printf("%6s", processes[y+1]);
                            System.out.printf("%5s", " ");
                            Object[] arr = queue.toArray(); 
                            for(int i=1; i<arr.length ; i++){ 
                                System.out.print(" " + arr[i]);
                            } 
                            break;
                        }
                    }
                }
                ctr++;
            } 
            tFinished[num-1]=ctr-1;
        }
        printTAT(num, arrival, processes, burst, tFinished);
    }

    //shortest job first
    public static void sjf(int num, int[] arrival, String[] processes, int[] burst, int totalBurst, int min){ 
        System.out.print("T");
        System.out.printf("%8s", "RUNNING");
        System.out.printf("%8s", "READY");
        int ctr = min;
        int ready = 0;
        int temp = 0;
        String tempo;
        int total = totalBurst+min;
        int current = 0;

        //sort according to arrival time
        for(int x = 0; x < num; x++){
            for(int y = x; y < num; y++){
                if(arrival[y]<arrival[x]){
                    temp = arrival[x];
                    arrival[x] = arrival[y];
                    arrival[y] = temp;

                    temp = burst[x];
                    burst[x] = burst[y];
                    burst[y] = temp;

                    tempo = processes[x];
                    processes[x] = processes[y];
                    processes[y] = tempo;
                }
            }
        }

        int[] bClone = burst.clone(); //clone burst array to avoid conflict
        int[] tFinished = new int[num];
        //while not done
        while(ctr!=total){
            for(int x = ctr; x < total; x++){
                for(int y = 0; y < num; y++){
                    if (arrival[y]==ctr){
                        //count how many have arrived
                        ready++;
                    }
                }
                
                //find ready process with lowest burst time within the ready processes
                for(int z = current; z < ready; z++){
                    if(bClone[z]<1){
                        //if process is done, increment and set finish time
                        current++;
                        tFinished[z] = x-1;
                    } else if(bClone[z]<bClone[current]){ //if there is a process with shorter burst time, swap accordingly
                        //swap arrival times
                        temp = arrival[z];
                        arrival[z] = arrival[current];
                        arrival[current] = temp;
                        //swap burst times
                        temp = burst[z];
                        burst[z] = burst[current];
                        burst[current] = temp;
                        //swap process names
                        tempo = processes[z];
                        processes[z] = processes[current];
                        processes[current] = tempo;
                        //swap finish times
                        temp = tFinished[z];
                        tFinished[z] = tFinished[current];
                        tFinished[current] = temp;
                        //swap clone array
                        temp = bClone[z];
                        bClone[z] = bClone[current];
                        bClone[current] = temp;
                    }
                }

                bClone[current]--;  //decrement current lowest process
                System.out.println();
                System.out.print(ctr);
                System.out.printf("%6s", processes[current]);
                System.out.printf("%5s", " ");
                for(int i=0; i<num; i++){
                    if(arrival[i]<=ctr && bClone[i]!=0 && processes[i]!=processes[current]){
                        System.out.print(processes[i] + " ");
                    }
                }
                ctr++;
            }
            tFinished[num-1]=ctr-1;
        }
        printTAT(num, arrival, processes, burst, tFinished);
    }

    //print turnaround times
    public static void printTAT(int num, int[] arrival, String[] processes, int[] burst, int[] tFinished){
        System.out.println();
        System.out.println();
        System.out.println("Turn Around Time for");
        int aTAT = 0;
        double mean = 0;
        for(int x = 0; x < num; x++){
            aTAT = tFinished[x]-arrival[x]+1;
            System.out.println("Process "+processes[x]+": "+tFinished[x]+" - "+arrival[x]+" + (1) = "+ aTAT);
            mean += aTAT;
        }
        mean = mean / num;
        System.out.println("Average TAT: " + mean);
    }
}