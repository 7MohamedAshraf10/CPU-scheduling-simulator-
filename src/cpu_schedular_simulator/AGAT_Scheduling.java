/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu_schedular_simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Mohamed Ashraf
 */
public class AGAT_Scheduling {


    int arrivalTime;
    String name;
    int quantumTime;
    int burstTime;
    int priority;
    int AGFactor =0;
    int Worked = 0;
    Queue <AGAT_Scheduling> ReadyProcess = new LinkedList<>();
    String Color = "NON";
    ArrayList<AGAT_Scheduling>processes = new ArrayList<>();
    ArrayList<AGAT_Scheduling>processesAWT = new ArrayList<>();
    ArrayList<AGAT_Scheduling>Available = new ArrayList<>();
    ArrayList<save>outPut = new ArrayList<save>();
    ArrayList<AGAT_Scheduling> quantumHistory = new ArrayList<>();

    class save{
        String name , color;
        int AT , BT;        //AT = begin time , BT = End time
        int WT , TAT;       //Waiting time  , Turn around time
        public void setData(String a , String col, int b , int c){name = a; color = col ;AT = b ; BT = c;}
    }
    
    private void setAGFactor() {
        this.AGFactor = burstTime+arrivalTime+priority;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setQuantumTime(int quantumTime) {
        this.quantumTime = quantumTime;
    }

    private boolean isAlive(AGAT_Scheduling process){
        if (process.burstTime == 0) {processes.remove(process); ReadyProcess.remove(process); return false;}
        else return true;
    }

    private int run(int time){
        if (this.burstTime<time){
            int tmp = this.burstTime;
            this.burstTime = 0;
            return tmp;
        }
        else if (time<this.halfQuantum()){
            this.Worked+=time;
            this.burstTime -= time;
            return time;
        }
        else {
            this.Worked += this.halfQuantum();
            this.burstTime -= this.halfQuantum();
            return this.halfQuantum();
        }
    }

    private void dealQuantum(){
        //incase of switching
        if (this.Worked == this.quantumTime){
            this.quantumTime += Math.ceil(this.quantumTime*0.10);
            AGAT_Scheduling process = new AGAT_Scheduling();
            process.name = this.name;
            process.quantumTime = this.quantumTime;
            quantumHistory.add(process);
        }
        else if (this.Worked<this.quantumTime){
            this.quantumTime += (this.quantumTime-this.Worked);
            this.Worked = 0;
            AGAT_Scheduling process = new AGAT_Scheduling();
            process.name = this.name;
            process.quantumTime = this.quantumTime;
            quantumHistory.add(process);
        }
    }
    private void updateReady(int currentTime){
        for (int i=0; i<processes.size(); i++){
            if (!ReadyProcess.contains(processes.get(i)) && processes.get(i).arrivalTime<=currentTime)ReadyProcess.add(processes.get(i));
        }
    }

    private int halfQuantum(){
        return (int)Math.ceil(this.quantumTime/2.0);
    }

    private  void saveProcess(int AT, int BT,String name,String color){
        save tmp = new save();
        tmp.AT = AT;
        tmp.BT = BT;
        tmp.name = name;
        tmp.color = color;
        outPut.add(tmp);
    }


    private void updateNON(int currentTime){
        sort();
        Available = new ArrayList<AGAT_Scheduling>();
        for (int i=0; i<processes.size(); i++){
            if (processes.get(i).arrivalTime<=currentTime){Available.add(processes.get(i));}
        }
        sortAG();
    }

    private boolean fullQuantum(){
        if (this.Worked == this.quantumTime)return true;
        else return false;
    }

    public ArrayList<save> SolveAGAT(){

        int currentTime = 0;
        save tmp = new save();
        AGAT_Scheduling currentProcess = processes.get(0);
        currentTime = currentProcess.arrivalTime;
        updateReady(currentTime);
        updateNON(currentTime);
        tmp.AT = currentTime;
        int processStart = currentTime;

        while (processes.size()>0) {
            currentTime += currentProcess.run(currentProcess.halfQuantum());
            updateReady(currentTime);
            updateNON(currentTime);
            if(!isAlive(currentProcess)) {saveProcess(processStart,currentTime,currentProcess.name,currentProcess.Color); currentProcess = null;}
            ////non preemptive
            if (currentProcess!=null && Available.size()>1 && !Available.get(0).equals(currentProcess)){
                currentProcess.dealQuantum();
                AGAT_Scheduling test = new AGAT_Scheduling();
                test.quantumTime = currentProcess.quantumTime;
                test.name = currentProcess.name;
                quantumHistory.add(test);
                saveProcess(processStart,currentTime,currentProcess.name,currentProcess.Color);
                processStart = currentTime;
                currentProcess = Available.get(0);
                continue;
            }
            while (Available.size()>0 && Available.get(0).equals(currentProcess)){
                currentTime += currentProcess.run(1);
                updateReady(currentTime);
                updateNON(currentTime);

                if(!isAlive(currentProcess)) {
                    saveProcess(processStart,currentTime,currentProcess.name,currentProcess.Color);
                    currentProcess = null;
                    break;
                    }

                    else if (currentProcess.fullQuantum()) {
                    currentProcess.dealQuantum();
                    AGAT_Scheduling test = new AGAT_Scheduling();
                    test.quantumTime = currentProcess.quantumTime;
                    test.name = currentProcess.name;
                    quantumHistory.add(test);
                    quantumHistory.add(currentProcess);
                    saveProcess(processStart,currentTime,currentProcess.name,currentProcess.Color);
                    processStart =currentTime;
                    currentProcess = ReadyProcess.remove();
                    break;
                    }

                    else if (!Available.get(0).equals(currentProcess)){
                    saveProcess(processStart,currentTime,currentProcess.name,currentProcess.Color);
                    processStart = currentTime;
                    currentProcess.dealQuantum();
                    AGAT_Scheduling test = new AGAT_Scheduling();
                    test.quantumTime = currentProcess.quantumTime;
                    test.name = currentProcess.name;
                    quantumHistory.add(test);
                    quantumHistory.add(currentProcess);
                    currentProcess = Available.get(0);
                    break;
                    }
            }

            if (currentProcess == null && ReadyProcess.size()>0){
                processStart = currentTime;
                currentProcess = ReadyProcess.remove();
            }

            else if (currentProcess == null && processes.size()>0){
                for (int i=0; i<processes.size(); i++){
                    if (processes.get(i).arrivalTime>currentTime){currentTime=processes.get(i).arrivalTime;currentProcess=processes.get(i);processStart=currentTime; break;}
                }
            }

        }
        return outPut;
    }

    AGAT_Scheduling(){}////Default Constructor
    AGAT_Scheduling(int ArrivalTime, int BurstTime, String name){
        this.arrivalTime = ArrivalTime;
        this.burstTime = BurstTime;
        this.name = name;
    }
    AGAT_Scheduling(Processes process){
        AGAT_Scheduling tmp = new AGAT_Scheduling();
        for (int i=0; i<process.numberOfProcesses; i++){
            tmp.setBurstTime(process.burstTimes.get(i));
            tmp.setArrivalTime(process.arrivalTimes.get(i));
            tmp.setPriority(process.priorityNumbers.get(i));
            tmp.setQuantumTime(process.timeQuantum);
            tmp.name = process.processesNames.get(i);
            tmp.Color = process.processesColors.get(i);
            tmp.setAGFactor();
            ///tmp is ready
            processes.add(tmp);
            tmp = new AGAT_Scheduling();
            ///processes is ready
        }
        sort();
        for (int i=0; i<processes.size(); i++)
        processesAWT.add(new AGAT_Scheduling(processes.get(i).arrivalTime,processes.get(i).burstTime,processes.get(i).name));
        outPut = SolveAGAT();
    }
    public double getAwt(){
        double AWT =0;
        HashMap<String,Integer> at = new HashMap<String, Integer>();
        HashMap<String,Integer> et = new HashMap<String, Integer>();
        HashMap<String,Integer> wt = new HashMap<String, Integer>();
        for (int i=0; i<processesAWT.size(); i++){
            String name = processesAWT.get(i).name;
            int AT = processesAWT.get(i).arrivalTime;
            int WT =  processesAWT.get(i).burstTime;
            at.put(name,AT);
            wt.put(name,WT);
        }
        for (int i=0; i<outPut.size(); i++){
            String name = outPut.get(i).name;
            int BT = outPut.get(i).BT;
            et.put(name,BT);
        }
        for (String i : at.keySet()){
            AWT += ((et.get(i) - at.get(i))-wt.get(i));
        }
        return (AWT/processesAWT.size());
    }
    public double getATAT(){
        double ATAT =0;
        HashMap<String,Integer> at = new HashMap<String, Integer>();
        HashMap<String,Integer> et = new HashMap<String, Integer>();
        for (int i=0; i<processesAWT.size(); i++){
            String name = processesAWT.get(i).name;
            int AT = processesAWT.get(i).arrivalTime;
            at.put(name,AT);
        }
        for (int i=0; i<outPut.size(); i++){
            String name = outPut.get(i).name;
            int BT = outPut.get(i).BT;
            et.put(name,BT);
        }
        for (String i : at.keySet()){
            ATAT += (et.get(i) - at.get(i));
        }
        return ATAT/processesAWT.size();
    }
    private void sort(){
        for (int i=0; i<processes.size(); i++){
            AGAT_Scheduling smaller = processes.get(i);
            int index = i;
            for (int j=i+1; j<processes.size(); j++){
                if (processes.get(j).arrivalTime<smaller.arrivalTime){
                    smaller = processes.get(j);
                    index = j;
                }
            }
            AGAT_Scheduling tmp  = processes.get(i);
            processes.set(i,smaller);
            processes.set(index,tmp);
        }
    }
    private void sortAG(){
        for (int i=0; i<Available.size(); i++){
            AGAT_Scheduling smaller = Available.get(i);
            int index = i;
            for (int j=i+1; j<Available.size(); j++){
                if (Available.get(j).AGFactor<smaller.AGFactor){
                    smaller = Available.get(j);
                    index = j;
                }
            }
            AGAT_Scheduling tmp  = Available.get(i);
            Available.set(i,smaller);
            Available.set(index,tmp);
        }
    }

    public void getQuantumHistory() {
        for (int i=0; i<quantumHistory.size(); i++){
            System.out.println(quantumHistory.get(i).name +" "+quantumHistory.get(i).quantumTime);
        }
    }


}
