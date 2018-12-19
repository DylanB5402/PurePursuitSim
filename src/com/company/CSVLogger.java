package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CSVLogger {

    boolean writeException;
    int counter = 0;
    File m_file;
    ArrayList<Double> dataList;
    ArrayList<String> headingList;
    String lineToLog = "";
    FileWriter writer;

    public CSVLogger(String filePath1, String filePath2, String fileName) {
        File logFolder1 = new File(filePath1);
        File logFolder2 = new File(filePath2);
        Path filePrefix = Paths.get("");
        if (logFolder1.exists() && logFolder1.isDirectory()) {
            filePrefix = Paths.get(filePath1 + fileName);
            System.out.println(1);
        } else if (logFolder2.exists() && logFolder2.isDirectory()) {
            filePrefix = Paths.get(filePath2 + fileName);
            System.out.println(2);
        } else {
            writeException = true;
            System.out.println(3);
        }
        if (!writeException) {
            int counter = 0;
            while (counter <= 99) {
                m_file = new File(filePrefix.toString() + String.format("%02d", counter) + ".csv");
                if (m_file.exists()) {
                    counter++;
                } else {
                    break;
                }
                if (counter == 99) {
                    System.out.println("file creation counter at 99!");
                }
            }
        }
        headingList = new ArrayList<>();
        dataList = new ArrayList<>();
    }

    public void addData(String dataName) {
        headingList.add(dataName);
    }

    public void updateData(double data) {
        dataList.add(data);
    }

    public void startLog(double timeStamp) {
        lineToLog = "";
        try {
            writer = new FileWriter(m_file);
            for (String dataHeader : headingList) {
                lineToLog = lineToLog + dataHeader + ",";
            }
            writer.append(lineToLog + "/n");
        } catch (IOException e) {
            e.printStackTrace();
            writeException = true;
        }
    }
    public void logToCSV() {
        lineToLog = "";
        if (!writeException) {
            try {
                for (double data : dataList) {
                    lineToLog = lineToLog + String.valueOf(data) + ",";
                    writer.append(lineToLog + "/n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                writeException = true;
            }

        }
        dataList.clear();
    }

    public void stopLogging() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            writeException = true;
        }
    }

}
