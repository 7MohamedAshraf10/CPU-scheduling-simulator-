/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu_schedular_simulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author Mohamed Ashraf
 */
public class PriorityScheduling {
    


    class save {
        String name, color;
        int ST, CT, WT, TT;
        public void setData(String a, String col, int b, int c, int d, int e) {
            name = a; color = col; ST = b; CT = c; WT = d; TT = e;
        }
    }

    class Process {
        String name, color;
        int AT, BT, p;
        public void setData(String a, String col, int b, int c, int d) {
            name = a;  color = col; AT = b; BT = c; p = d;
        }
    }

    class MyComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Process p1 = (Process) o1;
            Process p2 = (Process) o2;
            if (p1.AT < p2.AT) {
                return (-1);
            } else if (p1.AT == p2.AT && p1.p < p2.p) {
                return (-1);
            } else if (p1.AT == p2.AT && p1.p == p2.p && p1.BT > p1.BT) {
                return (-1);
            } else {
                return (1);
            }
        }
    }

    class comp implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Process p1 = (Process) o1;
            Process p2 = (Process) o2;
            if (p1.p < p2.p) {
                return (-1);
            } else if (p1.p == p2.p && p1.BT <= p1.BT) {
                return (-1);
            } else {
                return 1;
            }
        }
    }
    Double AvgTAT = 0.0, AvgWT = 0.0;
    ArrayList<Process> process = new ArrayList<>();
    ArrayList<save> save = new ArrayList<save>();
    int n = 0;

    PriorityScheduling(Processes processes) {
        n = processes.numberOfProcesses;
        for (int i = 0; i < n; i++) {

            Process a = new Process();
            a.setData(processes.processesNames.get(i), processes.processesColors.get(i), processes.arrivalTimes.get(i), processes.burstTimes.get(i), processes.priorityNumbers.get(i));
            process.add(a);

        }
        run();
        for (int i = 0; i < n; i++) {
            AvgTAT += save.get(i).TT;
            AvgWT += save.get(i).WT;
        }

        AvgTAT /= n;
        AvgWT /= n;
    }

    public void run() {
        int time = 0;
        PriorityQueue<Process> readyqueue = new PriorityQueue<Process>(new MyComparator());
        PriorityQueue<Process> WorkQueue = new PriorityQueue<Process>(new comp());
        for (int i = 0; i < n; i++) {
            readyqueue.add(process.get(i));
        }
        time = readyqueue.peek().AT;
        while (!readyqueue.isEmpty()) {
            while (!readyqueue.isEmpty()) {
                Process a = readyqueue.peek();
                if (a.AT <= time) {
                    readyqueue.remove();
                    if (a.p > 5) {
                      //  a.p--;
                    }
                    WorkQueue.add(a);
                } else {
                    break;
                }
            }
            if (WorkQueue.isEmpty()) {
                Process a = readyqueue.poll();
                WorkQueue.add(a);
                time = a.AT;
            }
            Process c = WorkQueue.poll();
            save i = new save();
            i.setData(c.name, c.color, time, time + c.BT, time - c.AT, (time + c.BT) - c.AT);
            save.add(i);
            time = time + c.BT;
            while (!WorkQueue.isEmpty()) {
                Process a = WorkQueue.poll();
                readyqueue.add(a);
            }
        }
    }
}

