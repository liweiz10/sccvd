package com.zlw.stroke.getexpression;

import java.io.*;
import java.util.Map;

public class GeneExpressionReader {
    private RandomAccessFile file;
    private Map<String, Object> geneIndex;
    private int totalCells;
    private boolean initialized = false;

    public void initialize(String binaryFilePath, String indexPath) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexPath))) {
            geneIndex = (Map<String, Object>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("index file format error", e);
        }

        file = new RandomAccessFile(binaryFilePath, "r");
        file.seek(0);

        // 读取文件头
        int totalGenes = file.readInt();
        totalCells = file.readInt();
        int nonZeroCount = file.readInt();

        // 跳过稀疏矩阵数据
        skipArray(); // rowIndices
        skipArray(); // colPointers
        skipArray(); // values

        initialized = true;
    }

    private void skipArray() throws IOException {
        int length = file.readInt();
        if (length < 0) {
            throw new IOException("Invalid array length: " + length);
        }
        file.seek(file.getFilePointer() + length * 4L);
    }

    public float[] getGeneExpression(String geneName) {
        if (!initialized) {
            throw new IllegalStateException("reader no init, please run initialize() method");
        }

        Object geneInfo = geneIndex.get(geneName);
        if (geneInfo == null) {
            return new float[totalCells];
        }

        try {
            java.lang.reflect.Field offsetField = geneInfo.getClass().getDeclaredField("offset");
            java.lang.reflect.Field countField = geneInfo.getClass().getDeclaredField("nonZeroCount");
            offsetField.setAccessible(true);
            countField.setAccessible(true);

            long offset = (Long) offsetField.get(geneInfo);
            int expectedCount = (Integer) countField.get(geneInfo);

            file.seek(offset);
            float[] expression = new float[totalCells];

            int actualCount = file.readInt();
            if (actualCount < 0 || actualCount > totalCells) {
                actualCount = Math.min(expectedCount, totalCells);
            }

            for (int i = 0; i < actualCount; i++) {
                int cellIndex = file.readInt();
                float value = file.readFloat();
                if (cellIndex >= 0 && cellIndex < totalCells) {
                    expression[cellIndex] = value;
                }
            }
            return expression;
        } catch (Exception e) {
            return new float[totalCells];
        }
    }

    public float[] getGeneExpressionIgnor(String geneName) {
        if (!initialized) {
            throw new IllegalStateException("reader no init, please run initialize() method");
        }

        // 查找不区分大小写的基因名称
        String actualGeneName = null;
        for (String key : geneIndex.keySet()) {
            if (key.equalsIgnoreCase(geneName)) {
                actualGeneName = key;
                break;
            }
        }

        if (actualGeneName == null) {
            return new float[totalCells];
        }

        return getGeneExpression(actualGeneName);
    }

    public GeneExpressionResult getGeneExpressionWithStats(String geneName) {
        float[] expression = getGeneExpression(geneName);

        int nonZeroCount = 0;
        float maxValue = 0;
        float sum = 0;

        for (float value : expression) {
            if (value > 0) {
                nonZeroCount++;
                sum += value;
                if (value > maxValue) {
                    maxValue = value;
                }
            }
        }

        float average = nonZeroCount > 0 ? sum / nonZeroCount : 0;
        return new GeneExpressionResult(expression, maxValue, average, nonZeroCount);
    }

    public String[] getAllGeneNames() {
        if (!initialized) {
            throw new IllegalStateException("reader no init");
        }
        return geneIndex.keySet().toArray(new String[0]);
    }

    public boolean containsGene(String geneName) {
        return initialized && geneIndex.containsKey(geneName);
    }

    public boolean containsGeneIgnoreCase(String geneName) {
        if (!initialized) return false;
        for (String key : geneIndex.keySet()) {
            if (key.equalsIgnoreCase(geneName)) {
                return true;
            }
        }
        return false;
    }

    public int getTotalCells() {
        return totalCells;
    }

    public int getTotalGenes() {
        return initialized ? geneIndex.size() : 0;
    }

    public void close() {
        try {
            if (file != null) {
                file.close();
            }
        } catch (IOException e) {
            // ignore
        }
        file = null;
        if (geneIndex != null) {
            geneIndex.clear();
        }
        initialized = false;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public static class GeneExpressionResult {
        private final float[] expression;
        private final float maxValue;
        private final float average;
        private final int nonZeroCount;

        public GeneExpressionResult(float[] expression, float maxValue, float average, int nonZeroCount) {
            this.expression = expression;
            this.maxValue = maxValue;
            this.average = average;
            this.nonZeroCount = nonZeroCount;
        }

        public float[] getExpression() { return expression; }
        public float getMaxValue() { return maxValue; }
        public float getAverage() { return average; }
        public int getNonZeroCount() { return nonZeroCount; }
        public int getTotalCells() { return expression.length; }
    }

    // 测试方法
    public static void main(String[] args) throws IOException {
        String binaryFile = "D:\\Workspace\\webdata\\Stroke_009_expression.bin";
        String indexFile = "D:\\Workspace\\webdata\\Stroke_009_gene_index.idx";

        GeneExpressionReader reader = new GeneExpressionReader();
        reader.initialize(binaryFile, indexFile);

        // 测试大小写敏感
        testGene(reader, "Cd68");
        testGene(reader, "CD68"); // 大写
        testGene(reader, "cd68"); // 小写

        reader.close();
    }

    private static void testGene(GeneExpressionReader reader, String geneName) {
        System.out.println("\nTesting gene: " + geneName);

        // 测试大小写敏感
        long start = System.currentTimeMillis();
        float[] expr1 = reader.getGeneExpression(geneName);
        long time1 = System.currentTimeMillis() - start;

        // 测试大小写不敏感
        start = System.currentTimeMillis();
        float[] expr2 = reader.getGeneExpressionIgnor(geneName);
        long time2 = System.currentTimeMillis() - start;

        // 统计结果
        int nonZero1 = countNonZero(expr1);
        int nonZero2 = countNonZero(expr2);

        System.out.println("Case-sensitive: " + nonZero1 + " non-zero, time: " + time1 + "ms");
        System.out.println("Case-insensitive: " + nonZero2 + " non-zero, time: " + time2 + "ms");
        System.out.println("Contains gene (case-sensitive): " + reader.containsGene(geneName));
        System.out.println("Contains gene (ignore case): " + reader.containsGeneIgnoreCase(geneName));
    }

    private static int countNonZero(float[] array) {
        int count = 0;
        for (float val : array) {
            if (val > 0) count++;
        }
        return count;
    }
}