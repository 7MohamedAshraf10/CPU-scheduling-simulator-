/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu_schedular_simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author Mohamed Ashraf
 */
public class SRTF_Scheduling {
    
    
    
    int context,lastend =0;
    ArrayList<Integer> arrivalTimes = new ArrayList<>();
    HashMap<String,Integer> BurstTimes = new HashMap<>();       
    static ArrayList<save> processes = new ArrayList<save>();   
    ArrayList<Process> process = new ArrayList<Process>();       
    //ready queue
    ArrayList<Process> ready = new ArrayList<Process>();        
    //average waiting time
    double AWT = 0;       
    //average turn around time
    double ATAT = 0;       
    
    SRTF_Scheduling(Processes processes){
     
        context = processes.timeContext;
        for(int i=0 ; i<processes.numberOfProcesses ; i++)
        { 
            arrivalTimes.add(processes.arrivalTimes.get(i));
            BurstTimes.put(processes.processesNames.get(i), processes.burstTimes.get(i));
            process.add(new Process(processes.processesNames.get(i),processes.processesColors.get(i),processes.arrivalTimes.get(i),processes.burstTimes.get(i)));
        }
        
        
        
        Run();
    }
    
    class save{
        String name ;
        String color; 
        int arrival = 0 , burst = 0;
        int beginTime ;
        int endTime;     
        //waiting time 
        double WT=0;
        //turn around time
        double TAT=0;

        public save(String name, String color, int beginTime, int endTime, int arr, int burs) {
            this.name = name;
            this.color = color;
            this.beginTime = beginTime;
            this.endTime = endTime;
            arrival = arr;
            burst=burs;
        }
        
        public void setCon(int a , int b){beginTime = a ;endTime = b;}
    }
    class Process
    { 
        String name,color; // name 
        int bt,art; // Burst Time ,Arrival Time
        public Process(String n,String c, int a,int b ) {name = n; color = c; art = a; bt = b;}
        public double getcomp(){return  Double.parseDouble( Integer.toString(this.art) + "." + Integer.toString(this.bt) );}
    }
    public void Run(){
        int low = Collections.min(arrivalTimes);
        int high = Collections.max(arrivalTimes);
        Comparator<Process> comparator = Comparator.comparing(Process->Process.art);
        Comparator<Process> comparator2 = Comparator.comparing(Process->Process.bt);
        Comparator<save> comparator3 = Comparator.comparing(save->save.name);
        process.sort(comparator);
        for(int i = low; i <= high ;++i){
            boolean flag = true;
            while(flag){
                if(process.size()>0){
                    if(process.get(0).art == i ){
                        ready.add(process.get(0));
                        process.remove(0);
                    }
                    else flag = false;
                }
                else    flag = false;
            }
            ready.sort(comparator2);
            if(ready.size()>0){
                processes.add(new save( ready.get(0).name, ready.get(0).color, i, i+1 , ready.get(0).art ,ready.get(0).bt) );
                Process neew = ready.get(0);
                neew.bt-- ;
                if(neew.bt == 0){
                    ready.remove(0);
                }
                else    ready.set(0 ,neew);
            }
        }
        ready.sort(comparator2);
        for (int i=0 ;i < ready.size(); ++i){
            processes.add(new save( ready.get(i).name, ready.get(i).color, processes.get(processes.size()-1).endTime, ready.get(i).bt +processes.get(processes.size()-1).endTime , ready.get(i).art , ready.get(i).bt) );
        }
        for (int i=0 ; i < processes.size()-1 ; ++i){
            if( i != processes.size()-1 ){
                while(processes.get(i).name.matches(processes.get(i+1).name)){
                    processes.set(i, new save(processes.get(i).name, processes.get(i).color, processes.get(i).beginTime, processes.get(i+1).endTime , processes.get(i).arrival ,processes.get(i).burst ));
                    processes.remove(i+1);
                    if( i == processes.size()-1 )   break;
                }
            }
        }
        //printing
        for(int i=0 ; i < processes.size();++i){        
            processes.get(i).setCon(processes.get(i).beginTime + i*context, processes.get(i).endTime + i*context);
            processes.get(i).TAT = Double.parseDouble(Integer.toString(processes.get(i).endTime - processes.get(i).arrival)) ;
            processes.get(i).WT  = processes.get(i).TAT - BurstTimes.get(processes.get(i).name) ;
        }
        lastend =processes.get(processes.size()-1).endTime;
        processes.sort(comparator3);
        for (int i=0 ; i < processes.size() ; ++i){
            double valWT = processes.get(i).WT;
            double valTAT = processes.get(i).TAT;
            if( i!=processes.size()-1){
                while(processes.get(i).name.matches(processes.get(i+1).name)){
                    valWT = processes.get(i+1).WT;
                    valTAT = processes.get(i+1).TAT;
                    i++;
                    if (i == processes.size()-1 )break;
                }
            }
            AWT+=valWT;
            ATAT+=valTAT;
        }
        AWT/=arrivalTimes.size();
        ATAT/=arrivalTimes.size();
        
    }
    
    
}
