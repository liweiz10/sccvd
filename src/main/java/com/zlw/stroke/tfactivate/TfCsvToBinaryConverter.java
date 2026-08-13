package com.zlw.stroke.tfactivate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TfCsvToBinaryConverter {

    public static void main(String[] args) {
//        String id = "Sample_045";
        String[] samples = {"Sample_046", "Sample_047", "Sample_048", "Sample_049", "Sample_050", "Sample_051", "Sample_052", "Sample_053", "Sample_054", "Sample_055", "Sample_056", "Sample_057", "Sample_058", "Sample_059", "Sample_060", "Sample_061", "Sample_062", "Sample_063", "Sample_064", "Sample_065", "Sample_066", "Sample_067", "Sample_068", "Sample_071", "Sample_072", "Sample_073", "Sample_074", "Sample_075", "Sample_076", "Sample_077", "Sample_078", "Sample_079", "Sample_080", "Sample_081", "Sample_082", "Sample_083", "Sample_084", "Sample_085"};
        String[] samplesatac1 = {"Sample_069"};
        String[] samplesatac2 = {"Sample_070","Sample_086","Sample_087","Sample_088","Sample_089"};
        String[] samplesatac = {"Sample_090"};
        for (String id : samplesatac) {
            System.out.println(id);
            String csvFilePath = "D:\\Workspace\\webdata\\" + id + "_chromVar_obj_zscores.csv";
//            String csvFilePath = "D:\\Workspace\\webdata\\" + id + "_scenic_regulon_auc.csv";
            String binaryFilePath = "D:\\Workspace\\webdata\\" + id + "_tf_activity.bin";
            String indexPath = "D:\\Workspace\\webdata\\" + id + "_tf_index.idx";

            try {
                System.out.println("Starting CSV to binary conversion: " + csvFilePath);
                convertCsvToBinary(csvFilePath, binaryFilePath, indexPath);
                System.out.println("Conversion completed successfully!");
                System.out.println("Binary file: " + binaryFilePath);
                System.out.println("Index file: " + indexPath);
            } catch (Exception e) {
                System.err.println("Conversion failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void convertCsvToBinary(String csvFilePath, String binaryFilePath, String indexPath) throws IOException {
        Map<String, TfDataInfo> tfIndexMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(binaryFilePath)))) {

            // 读取CSV文件头（细胞名称）
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("CSV file is null");
            }

            String[] cellNames = header.split(",");
            int numCells = cellNames.length - 1; // 减去第一列（TF名称）

            System.out.println("find " + numCells + " cells");

            // 写入文件头信息
            out.writeInt(numCells); // 细胞数量

            String line;
            int tfCount = 0;

            // 读取每一行（每个TF）
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != numCells + 1) {
                    System.out.println("warning: jump format error row: " + line);
                    continue;
                }

                String tfName = values[0];
                float[] activityValues = new float[numCells];

                // 解析活性值
                for (int i = 0; i < numCells; i++) {
                    try {
                        activityValues[i] = Float.parseFloat(values[i + 1]);
                    } catch (NumberFormatException e) {
                        activityValues[i] = 0f; // 解析失败设为0
                    }
                }

                // 写入TF数据
                long offset = out.size();
                out.writeInt(activityValues.length); // 值数量
                for (float value : activityValues) {
                    out.writeFloat(value);
                }

                tfIndexMap.put(tfName, new TfDataInfo(offset, activityValues.length));
                tfCount++;

                if (tfCount % 100 == 0) {
                    System.out.println("process " + tfCount + " TF/Gene");
                }
            }

            System.out.println("all process " + tfCount + " TF/Gene");
        }

        // 保存索引文件
        saveIndex(tfIndexMap, indexPath);
    }

    private static void saveIndex(Map<String, TfDataInfo> indexMap, String indexPath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(indexPath))) {
            oos.writeObject(indexMap);
        }
    }

    // TF数据信息类（使用通用Map格式，避免类路径问题）
    static class TfDataInfo implements Serializable {
        long offset;
        int valueCount;

        TfDataInfo(long offset, int valueCount) {
            this.offset = offset;
            this.valueCount = valueCount;
        }
    }
}