package com.zlw.stroke.getexpression;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;

/**
 * 基因表达值读取器 - 修复偏移量问题
 * 这个类是读取的h5_to_binary_converter.py代码生成的二进制文件，从而获取单个基因的表达。
 */
public class GeneExpressionReader2 {
    private RandomAccessFile file;
    private Map<String, GeneInfo> geneIndex;
    private int totalCells;
    private int totalGenes;
    private boolean initialized = false;

    public static class GeneInfo {
        public long offset; // 这个偏移量应该是相对于文件开头的
        public int count;

        public GeneInfo(long offset, int count) {
            this.offset = offset;
            this.count = count;
        }
    }

    /**
     * 初始化读取器
     */
    public void initialize(String binaryFilePath, String indexPath) throws IOException {
        // 读取索引文件
        readSimpleIndexFile(indexPath);

        // 打开二进制文件
        file = new RandomAccessFile(binaryFilePath, "r");

        // 读取文件头信息（小端序）
        byte[] headerBytes = new byte[12];
        file.readFully(headerBytes);

        ByteBuffer buffer = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);
        totalGenes = buffer.getInt();
        totalCells = buffer.getInt();
        int nonZeroValues = buffer.getInt();

        System.out.println("File header: genes=" + totalGenes + ", cells=" + totalCells + ", nonZero=" + nonZeroValues);
        System.out.println("File length: " + file.length() + " bytes");
        System.out.println("Index contains: " + geneIndex.size() + " genes");

        // 不需要跳过稀疏矩阵数据，因为Python写入的偏移量已经是绝对偏移量
        initialized = true;
        System.out.println("Reader initialized successfully");
    }

    /**
     * 读取索引文件
     */
    private void readSimpleIndexFile(String indexPath) throws IOException {
        geneIndex = new HashMap<>();
        int successCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(indexPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    try {
                        String geneName = parts[0].trim();
                        long offset = Long.parseLong(parts[1].trim());
                        int count = Integer.parseInt(parts[2].trim());

                        geneIndex.put(geneName, new GeneInfo(offset, count));
                        successCount++;
                    } catch (NumberFormatException e) {
                        // 忽略解析错误
                    }
                }
            }
        }

        System.out.println("Loaded index: " + successCount + " genes");
    }

    /**
     * 获取基因表达值 - 使用Python写入的绝对偏移量
     */
    public float[] getGeneExpression(String geneName) throws IOException {
        checkInitialized();

        GeneInfo geneInfo = geneIndex.get(geneName);
        if (geneInfo == null) {
            System.out.println("Gene '" + geneName + "' not found");
            return new float[totalCells];
        }

        System.out.println("Reading gene '" + geneName + "' at offset: " + geneInfo.offset +
                ", expected count: " + geneInfo.count);

        // 检查偏移量是否有效
        if (geneInfo.offset >= file.length()) {
            System.err.println("Error: Offset " + geneInfo.offset + " exceeds file length " + file.length());
            return new float[totalCells];
        }

        file.seek(geneInfo.offset);

        // 读取基因数据数量（小端序）
        int actualCount = readInt();

        System.out.println("Actual count read: " + actualCount);

        float[] expression = new float[totalCells];

        // 读取每个表达数据
        for (int i = 0; i < actualCount; i++) {
            int cellIndex = readInt(); // 读取细胞索引
            float value = readFloat(); // 读取表达值

            if (cellIndex >= 0 && cellIndex < totalCells) {
                expression[cellIndex] = value;
            }

            // 每1000个记录打印进度
            if ((i + 1) % 10000 == 0) {
                System.out.println("Processed " + (i + 1) + "/" + actualCount + " records");
            }
        }

        System.out.println("Successfully read " + actualCount + " expression values");
        return expression;
    }
    public float[] getGeneExpressionIgnor(String geneName) throws IOException {
        checkInitialized();

        // 查找不区分大小写的基因名称
        GeneInfo geneInfo = null;
        String actualGeneName = null;

        for (String key : geneIndex.keySet()) {
            if (key.equalsIgnoreCase(geneName)) {
                geneInfo = geneIndex.get(key);
                actualGeneName = key;
                break;
            }
        }

        if (geneInfo == null) {
            System.out.println("Gene '" + geneName + "' not found (case-insensitive search)");
            return new float[totalCells];
        }

        System.out.println("Reading gene '" + actualGeneName + "' (requested: '" + geneName +
                "') at offset: " + geneInfo.offset + ", expected count: " + geneInfo.count);

        // 检查偏移量是否有效
        if (geneInfo.offset >= file.length()) {
            System.err.println("Error: Offset " + geneInfo.offset + " exceeds file length " + file.length());
            return new float[totalCells];
        }

        file.seek(geneInfo.offset);

        // 读取基因数据数量（小端序）
        int actualCount = readInt();

        System.out.println("Actual count read: " + actualCount);

        float[] expression = new float[totalCells];

        // 读取每个表达数据
        for (int i = 0; i < actualCount; i++) {
            int cellIndex = readInt(); // 读取细胞索引
            float value = readFloat(); // 读取表达值

            if (cellIndex >= 0 && cellIndex < totalCells) {
                expression[cellIndex] = value;
            }

            // 每1000个记录打印进度
            if ((i + 1) % 10000 == 0) {
                System.out.println("Processed " + (i + 1) + "/" + actualCount + " records");
            }
        }

        System.out.println("Successfully read " + actualCount + " expression values");
        return expression;
    }
    /**
     * 新增功能：获取基因表达值的最大值
     */
    public float getGeneMaxValue(String geneName) throws IOException {
        checkInitialized();

        GeneInfo geneInfo = geneIndex.get(geneName);
        if (geneInfo == null) {
            System.out.println("Gene '" + geneName + "' not found");
            return 0f;
        }

        System.out.println("Reading max value for gene '" + geneName + "' at offset: " + geneInfo.offset);

        if (geneInfo.offset >= file.length()) {
            System.err.println("Error: Offset " + geneInfo.offset + " exceeds file length " + file.length());
            return 0f;
        }

        file.seek(geneInfo.offset);

        int actualCount = readInt();
        float maxValue = 0f;

        // 读取所有值并找出最大值
        for (int i = 0; i < actualCount; i++) {
            readInt(); // 跳过细胞索引
            float value = readFloat();
            if (value > maxValue) {
                maxValue = value;
            }
        }

        System.out.println("Gene '" + geneName + "' max value: " + maxValue);
        return maxValue;
    }

    /**
     * 简化的获取方法，用于调试
     */
    public float[] getGeneExpressionSimple(String geneName) throws IOException {
        checkInitialized();

        GeneInfo geneInfo = geneIndex.get(geneName);
        if (geneInfo == null) {
            System.out.println("Gene '" + geneName + "' not found");
            return new float[totalCells];
        }

        System.out.println("Reading gene '" + geneName + "' from offset: " + geneInfo.offset);

        // 检查偏移量范围
        if (geneInfo.offset < 0 || geneInfo.offset >= file.length()) {
            System.err.println("Invalid offset: " + geneInfo.offset + ", file length: " + file.length());
            return new float[totalCells];
        }

        file.seek(geneInfo.offset);

        // 读取基因数据数量
        int actualCount = readInt();
        System.out.println("Data count: " + actualCount);

        // 只读取前几个值进行测试
        int countToRead = Math.min(5, actualCount);
        System.out.println("Reading first " + countToRead + " values:");

        for (int i = 0; i < countToRead; i++) {
            int cellIndex = readInt(); // 读取细胞索引
            float value = readFloat(); // 读取表达值
            System.out.println("  " + i + ": cell=" + cellIndex + ", value=" + value);
        }

        return new float[totalCells];
    }

    /**
     * 调试方法：检查文件结构
     */
    public void debugFileStructure() throws IOException {
        System.out.println("\n=== File Structure Debug ===");
        System.out.println("File length: " + file.length() + " bytes");

        // 读取文件头
        file.seek(0);
        byte[] header = new byte[12];
        file.readFully(header);

        ByteBuffer buffer = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN);
        System.out.println("Header - Genes: " + buffer.getInt() +
                ", Cells: " + buffer.getInt() +
                ", NonZero: " + buffer.getInt());

        // 检查一些基因的偏移量
        String[] sampleGenes = {"Cd68", "Actb", "Gapdh"};
        for (String gene : sampleGenes) {
            GeneInfo info = geneIndex.get(gene);
            if (info != null) {
                System.out.println("Gene " + gene + ": offset=" + info.offset +
                        ", count=" + info.count +
                        ", valid=" + (info.offset < file.length()));
            }
        }
    }

    // 读取小端序int
    private int readInt() throws IOException {
        byte[] bytes = new byte[4];
        file.readFully(bytes);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    // 读取小端序float
    private float readFloat() throws IOException {
        byte[] bytes = new byte[4];
        file.readFully(bytes);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Reader not initialized");
        }
    }

    public String[] getAllGeneNames() {
        return geneIndex.keySet().toArray(new String[0]);
    }

    public boolean containsGene(String geneName) {
        return initialized && geneIndex.containsKey(geneName);
    }

    public int getTotalCells() {
        return totalCells;
    }

    public int getTotalGenes() {
        return initialized ? geneIndex.size() : 0;
    }

    public void close() throws IOException {
        if (file != null) file.close();
    }

    /**
     * 生成分隔线
     */
    private static String generateSeparator(int length) {
        return new String(new char[length]).replace('\0', '=');
    }

    /**
     * 主方法 - 先进行调试
     */
    public static void main(String[] args) {
        String binaryFile = "D:\\Workspace\\webdata\\geneh5\\Stroke_026_expression.bin";
        String indexFile = "D:\\Workspace\\webdata\\geneh5\\Stroke_026_gene_index.idx";

        GeneExpressionReader2 reader = new GeneExpressionReader2();

        try {
            System.out.println("Initializing reader...");
            reader.initialize(binaryFile, indexFile);

            // 先调试文件结构
            reader.debugFileStructure();

            // 测试基因
            String testGene = "Cd68";
            System.out.println("\n" + generateSeparator(50));
            System.out.println("Testing simple read for: " + testGene);

            if (reader.containsGene(testGene)) {
                float[] result = reader.getGeneExpression(testGene);
                System.out.println(result.length);
            } else {
                System.out.println("Gene not found");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}