import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nic on 15-08-06.
 * COMP 346 Operating Systems
 * Final Project
 * Prof. Todd Eavis
 * Nic Yantzi
 * summmer 15'
 */

public class MemoryManager {

    //DATA STRUCTURES

    public static Random rand = new Random();
    private static int tenPercentCounter;
    private static int ninetyPercentCounter;

    private static ArrayList<Integer> processCountList = new ArrayList<Integer>();
    private static int[] memory = new int[16000];

    private static int[][] memoryMapping = new int[1000][2];
    private static ArrayList<int[][]> pageTables = new ArrayList<int[][]>();
    private static ArrayList<Integer> freeFramesList = new ArrayList<Integer>();

    private static ArrayList<Integer> currentLineAccess = new ArrayList<Integer>();
    private static ArrayList<Integer> accessFlagList = new ArrayList<Integer>();

    private static int totalLines;
    private static ArrayList<Integer> leastRecentlyUsed = new ArrayList<Integer>();



    //MEMORY MANAGER MODULE

    public static void ProcessCreator (int process){
        for (int i = 0; i < process; i++) {
            try {
                PrintWriter writer = new PrintWriter("p" + i + ".process.txt", "UTF-8");
                PrintWriter writer1 = new PrintWriter("p"+i+".update.txt", "UTF-8");

                //from 60000 to 65535
                int lineCount = rand.nextInt((65535 - 60000)+1) + 60000;


                processCountList.add(lineCount);

                for (int n = 0; n < lineCount; n++) {

                    //from 0 to 999
                    int number = rand.nextInt(1000);
                    writer.println(number);
                    writer1.println(number);

                }
                writer.close();
                writer1.close();
            } catch (IOException ex){
                System.out.println("p" + i + ".process was not created.");
            }

        }
    }

    public static void AccessCreator (int process) {

        for (int i = 0; i < process; i++) {

            try {
                PrintWriter writer = new PrintWriter("p"+i+".access.txt", "UTF-8");

                int numAccesses = rand.nextInt((300-50)+1)+50;

                totalLines = totalLines + numAccesses;


                //System.out.println(i +": "+numAccesses);

                for (int n = 0; n < numAccesses; n++){

                    //generate numbers in range from 0 - 65535

                    //10% should be in 0-159 range, 90% in 160-65535 range.

                    int percentage = rand.nextInt(10)+1;

                    if (percentage == 1){  //0-159
                        int accNumber = rand.nextInt(160);
                        writer.println(accNumber);
                        tenPercentCounter++;

                    }
                    if (percentage > 1) {  //160-65535
                        int accNumber2 = rand.nextInt((65535 - 160)+1)+160;
                        writer.println(accNumber2);
                        ninetyPercentCounter++;
                    }
                }

                writer.close();

            } catch (IOException ex2){
                System.out.println("p"+i+".access was not created.");
            }
        }
    }

    public static void PercentageCheck(){
        double ten = (double)tenPercentCounter;
        double ninety = (double)ninetyPercentCounter;

        double total = ten + ninety;
        //System.out.println("Total = "+total);
        System.out.println("Ten Percent = "+ten/total*100);
        System.out.println("Ninety Percent = "+ ninety/total*100+"\n\n");
    }

    public static void SwapFileCreator(int process){

        int[][] tempProcessArrays = new int[65536][process];

        for(int i = 0; i < process; i++){
            String filename2 = ("p"+i+".process.txt");

            try{
                FileReader processFileReader = new FileReader(filename2);
                BufferedReader processReader = new BufferedReader(processFileReader);

                String line;
                int counter = 0;
                //intln("Process" + i);
                while((line = processReader.readLine()) != null){
                    int lineNum = Integer.parseInt(line);
                    //System.out.println("Process = "+i+ "Value = " +lineNum);
                    tempProcessArrays[counter][i] = lineNum;
                    counter++;
                }
                while(counter < 65536){
                    tempProcessArrays[counter][i] = 1000;
                    counter++;
                }

                processReader.close();
                processFileReader.close();

            } catch (IOException ex1){
                System.out.println("Error opening process file");
            }
        }

        String fileName = "swap.os";

        try{
            FileOutputStream fileOs = new FileOutputStream(fileName);
            DataOutputStream os = new DataOutputStream(fileOs);

            for (int i = 0; i < process; i++){

                for (int n = 0; n < 65536; n++) {

                    int temp = tempProcessArrays[n][i];
                    //System.out.println(temp);
                    os.writeInt(temp);
                }
            }

            os.close();
            fileOs.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error creating swap.os file.");
        } catch (IOException e){
            System.out.println("Error writing to swap.os file.");
        }
    }

    public static void PageTableCreator(int process){
        for (int i = 0; i < process; i++){
            int size = processCountList.get(i);

            int pages;

             if (size % 16 == 0){
                 pages = size/16;

             } else {
                 pages = size/16 + 1;
             }
            int[][] tempPageTable = new int[4096][2];

            for(int n = 0; n < pages; n++){
                //invalid/valid bit initialized to invalid (0)
                tempPageTable[n][1] = 0;
            }

            //System.out.println("Process " + i + " page table created, size: " + pages);

            pageTables.add(tempPageTable);
        }
        System.out.println("Page Tables for All Process Created.\n\n");

    }

    public static void InitializeElements(int process){
        for(int i = 0; i < 1000; i++){
            freeFramesList.add(i);

        }

        for (int i = 0; i < process; i++){
            currentLineAccess.add(0);
            accessFlagList.add(1);
        }
        System.out.println("Free frames list initialized. All 1000 frames in memory are free.");

    }

    public static void AddLRU (int freeFrame){
        if (leastRecentlyUsed.contains(freeFrame)){
            leastRecentlyUsed.remove(freeFrame);
            //add freeFrame to end of list, (least recently used is at the beginning of the list...
            leastRecentlyUsed.add(freeFrame);
        }
        else {
            leastRecentlyUsed.add(freeFrame);
        }
    }

    public static void FrameCheck(int currentProcess){
        if(freeFramesList.size() == 0){
            int lru = leastRecentlyUsed.get(0);
            int process = memoryMapping[lru][0];
            int page = memoryMapping[lru][1];

            System.out.println("Process "+currentProcess+": SWAP. LRU frame: "+lru+", original mapping: process "+process+", page "+page);

            //Move data in memory back to swap.os file.

            SaveToDisk(process, lru, page);

            //Change page table to invalid bit for corresponding process/page...

            pageTables.get(process)[page][1] = 0;

            //add lru frame back to freeFramesList

            freeFramesList.add(lru);


        }



    }

    public static int[] DiskRetrieval (int process, int address){

        int positionToSeek;
        int page = address/16;

        positionToSeek = 65536*process + 16*page;

        //System.out.println("Position to Seek = "+positionToSeek + "Process = "+process);
        int tempFrameBuffer[] = new int[16];

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile("swap.os", "rw");

            raf.seek(positionToSeek*4);

            for(int i = 0; i < 16; i++){
                int temp = raf.readInt();
                //System.out.println(temp);
                tempFrameBuffer[i] = temp;
            }

            raf.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error finding swap.os file");
        } catch (IOException ex2){
            System.out.println("Error reading swap.os file");
        }

        return tempFrameBuffer;

    }

    public static void SaveToDisk(int process, int frame, int page) {

        int positionToSeek;
        positionToSeek = 65536*process + 16*page;
        int[] pageBuffer = new int[16];

        //System.out.println("Process "+process+" Address Start "+(16*page));

        for (int i = 0; i < 16; i++) {
            int temp = memory[frame*16 + i];
            //System.out.println("Index From Save To Disk = "+ (frame*16 + i));
            //System.out.println(temp);
            pageBuffer[i] = temp;
        }

        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile("swap.os", "rw");
            raf.seek(positionToSeek * 4);

            for(int i = 0; i < 16; i++){
                int temp = pageBuffer[i];
                raf.writeInt(temp);
            }
            raf.close();

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            System.out.println("Error Writing to File");
        }
    }

    public static void Start(int process){

        //for(int t = 0; t < totalLines; t++) {
        while(accessFlagList.contains(1)){

            for (int i = 0; i < process; i++) {

                BufferedReader accessTextReader;

                try {
                    String accessFile = "p" + i + ".access.txt";

                    //System.out.println("Access File = " + accessFile);

                    FileReader accessFileReader = new FileReader(accessFile);

                    accessTextReader = new BufferedReader(accessFileReader);

                    int currentLine = currentLineAccess.get(i);

                    for (int n = 0; n < currentLine; n++) {
                        //System.out.println("Here");
                        try {
                            accessTextReader.readLine();
                        } catch (IOException ex1) {
                            System.out.println("Error during finding current access line");
                        }

                    }

                    int counter = 0;



                    for (int p = 0; p < 5; p++) {

                        String line = accessTextReader.readLine();

                        if(line == null){
                            accessFlagList.set(i, 0);
                            break;
                        }
                        int lineNum = Integer.parseInt(line);
                        counter++;

                        if (lineNum == -1) {
                            accessFlagList.set(i, 0);
                            break;
                        }

                        int accessNum = Integer.parseInt(line);
                        int processSize = processCountList.get(i);


                        if (processSize < accessNum) {
                            p = p - 1;
                            break;
                        }

                        int pageNum = accessNum / 16;


                        //System.out.println("Page Number " + pageNum);
                        //System.out.println("Process Size " +processSize);
                        //System.out.println("Page Table Size: "+pageTables.get(i).length);

                        int offset = accessNum % 16;


                        String readOrWrite = "READ";

                        if (accessNum < 160) {

                            readOrWrite = "WRITE=";
                        }

                        //check page table to see if in memory...


                        //while validity = 0...
                        int freeFrame;

                        if (pageTables.get(i)[pageNum][1] == 0) {
                            //not in memory!
                            System.out.println((p + 1) + "] Process " + i + " (" + readOrWrite + "): address " + accessNum + " [page " + pageNum + ", offset " + offset + "], PAGE FAULT");


                            //find frame in memory to add page too
                            FrameCheck(process);
                            freeFrame = freeFramesList.get(0);
                            freeFramesList.remove(0);

                            //add to memory
                            int[] pageBuffer = DiskRetrieval(i, accessNum);
                            //System.out.println("Temp Values Retrieved...");

                            for (int q = 0; q < 16; q++) {
                                int tempValue = pageBuffer[q];
                                //System.out.println(tempValue);
                                memory[(freeFrame * 16) + q] = tempValue;
                            }


                            //update memoryMapping so you know which process/page the frame belongs too.
                            memoryMapping[freeFrame][0] = i;
                            memoryMapping[freeFrame][1] = pageNum;


                            //update pageTable so you know where the page is stored in memory
                            pageTables.get(i)[pageNum][0] = freeFrame;

                            //update the time stamp
                            AddLRU(freeFrame);


                            //make the bit valid.
                            pageTables.get(i)[pageNum][1] = 1;

                        }
                        //bit is valid already, means the page is in memory already
                        if (pageTables.get(i)[pageNum][1] == 1) {

                            int newWriteNum = rand.nextInt(1000);

                            int mapping = pageTables.get(i)[pageNum][0];

                            int value = memory[mapping*16 + offset];

                            if (readOrWrite == "READ") {
                                System.out.println((p + 1) + "] Process " + i + " (" + readOrWrite + "): address " + accessNum + " [page " + pageNum + ", offset " + offset + "], mapped to frame " + mapping + " , value = " + value);
                                //update timestamp to frame being used
                                AddLRU(mapping);
                            }
                            if (readOrWrite == "WRITE=") {
                                System.out.println((p + 1) + "] Process " + i + " (" + readOrWrite + newWriteNum + "): address " + accessNum + " [page " + pageNum + ", offset " + offset + "], mapped to frame " + mapping + " , value = " + value);
                                //update value in memory

                                memory[mapping*16 + offset] = newWriteNum;

//                                int written = memory[mapping*16 + offset];
//                                System.out.println("Index From Start Function = " + (pageNum*16 + offset));
//                                System.out.println("Original Value =" + value + "Written To Memory" + written);

                                //update timestamp to frame being used
                                AddLRU(mapping);
                            }
                        }
                    }
                    int tempCount = currentLineAccess.get(i);
                int newCount = tempCount + counter;

                    currentLineAccess.set(i, newCount);

                } catch (IOException ex2) {
                    System.out.println("Error Opening Files");
                }
                System.out.println("");
            }
        }
    }

    public static void WriteUpdates(int process){

        int[][] tempProcessArrays = new int[65536][process];
        String fileName = "swap.os";

        try{
            FileInputStream fileIs = new FileInputStream(fileName);
            DataInputStream Is = new DataInputStream(fileIs);

            for (int i = 0; i < process; i++){

                for (int n = 0; n < 65536; n++) {

                    int temp = Is.readInt();
                    //System.out.println("Process "+ i+" Value" + temp);
                    tempProcessArrays[n][i] = temp;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening swap.os file.");
        } catch (IOException e){
            System.out.println("Error reading swap.os file.");
        }

        for (int i = 0; i < process; i++) {
            try {
                PrintWriter writer = new PrintWriter("p" + i + ".update.txt", "UTF-8");

                for (int n = 0; n < 65536; n++) {
                    int temp = tempProcessArrays[n][i];

                    if (temp < 1000) {
                        writer.println(temp);
                    }

                }

                writer.close();

            } catch (IOException ex) {
                System.out.println("p" + i + ".process was not created.");
            }
        }
    }

    public static void FlushMemory(){
        for (int n = 0; n < 1000; n++){

            int process = memoryMapping[n][0];
            int page = memoryMapping[n][1];

            SaveToDisk(process, n, page);

        }
    }

    public static void main (String args[]) {
        System.out.println("\n\nWelcome to Nic's Virtual Memory Management Subsystem\n\n");

        if (args.length != 1){
            System.out.println("Correct Start of Program is MemoryManager x   -- Where x represents the number of processes you wish to run");
        }
        int processCount = Integer.parseInt(args[0]);

        ProcessCreator(processCount);
        AccessCreator(processCount);
        PercentageCheck();
        InitializeElements(processCount);
        PageTableCreator(processCount);
        SwapFileCreator(processCount);
        Start(processCount);
        FlushMemory();
        WriteUpdates(processCount);

    }
}
