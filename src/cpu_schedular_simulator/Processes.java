package cpu_schedular_simulator;

import java.util.ArrayList;

/**
 *
 * @author Mohamed Ashraf
 */
public class Processes {
    
    int numberOfProcesses ;
    int timeQuantum ;
    static int timeContext ;
    
    ArrayList<String> processesNames = new ArrayList<>();
    ArrayList<String> processesColors = new ArrayList<>();
    ArrayList<Integer> arrivalTimes = new ArrayList<>();
    ArrayList<Integer> burstTimes = new ArrayList<>();
    ArrayList<Integer> priorityNumbers = new ArrayList<>();
    
}
