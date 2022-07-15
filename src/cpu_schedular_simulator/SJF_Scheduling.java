/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu_schedular_simulator;

import java.util.*;

/**
 *
 * @author Mohamed Ashraf
 */
public class SJF_Scheduling {
    
    ArrayList<Process> processesList = new ArrayList<>();
    double averageWaitingTime = 0 ;
    double averageTurnaroundTime = 0;

    public SJF_Scheduling(Processes processes) {
        for(int i = 0;i<processes.numberOfProcesses;i++)
        {
            processesList.add(new Process(processes.processesNames.get(i), processes.processesColors.get(i), processes.arrivalTimes.get(i), processes.burstTimes.get(i) ));
            run();
        }
        
    }

    private void run() {
        Comparator<Process> comparator = Comparator.comparing(Process -> Process.arrivalTime);
        comparator = comparator.thenComparing(Process -> Process.burstTime);
        processesList.sort(comparator);
        
        int lastProcess = 0;
        
        for(int i = 0; i<processesList.size() ; i++)
        {
            if(processesList.get(i).arrivalTime <= lastProcess)
            {
                processesList.get(i).beginTime = lastProcess;
            }else
            {
                processesList.get(i).beginTime = lastProcess + processesList.get(i).arrivalTime;
            }
            processesList.get(i).endTime = processesList.get(i).beginTime + processesList.get(i).burstTime;
            
            lastProcess = processesList.get(i).endTime;
            processesList.get(i).turnAroundTime = processesList.get(i).endTime - processesList.get(i).arrivalTime;
            processesList.get(i).waitingTime = processesList.get(i).turnAroundTime - processesList.get(i).burstTime;
        }
        
        //Calculate Average waiting and turnaround time
        for(int i =0;i<processesList.size();i++)
        {
            averageTurnaroundTime += processesList.get(i).turnAroundTime;
            averageWaitingTime += processesList.get(i).waitingTime;
        }
         averageTurnaroundTime /= processesList.size();
        averageWaitingTime /= processesList.size();
        
        
    }
    
    
    
    
     class Process{
        String name , color;
        int arrivalTime , burstTime;
        int waitingTime , turnAroundTime;
        int beginTime , endTime;

        public Process(String name, String color, int arrivalTime, int burstTime) {
            this.name = name;
            this.color = color;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            
        }
 
        
        
    }
}
