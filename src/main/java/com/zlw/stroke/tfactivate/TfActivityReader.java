package com.zlw.stroke.tfactivate;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * TF活性数据读取器
 * 用于从二进制文件中读取TF在所有细胞中的活性值
 */
public class TfActivityReader {
    private MappedByteBuffer buffer;
    private Map<String, Map<String, Object>> tfIndex; // 使用通用Map格式
    private int totalCells;
    private boolean initialized = false;

    /**
     * 初始化读取器
     */
    public void initialize(String binaryFilePath, String indexPath) throws IOException {
        tfIndex = loadIndexCompatible(indexPath);
        System.out.println("success load TF index, include " + tfIndex.size() + " TF");

        // 内存映射文件
        RandomAccessFile file = new RandomAccessFile(binaryFilePath, "r");
        buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());

        // 读取文件头信息
        totalCells = buffer.getInt(); // 细胞数量

        initialized = true;
        System.out.println("reader init success, cell number: " + totalCells);
    }

    // 兼容性索引加载方法
    private Map<String, Map<String, Object>> loadIndexCompatible(String indexPath) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexPath))) {
            Object rawIndex = ois.readObject();

            if (rawIndex instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> originalIndex = (Map<String, Object>) rawIndex;

                Map<String, Map<String, Object>> compatibleIndex = new HashMap<>();

                for (Map.Entry<String, Object> entry : originalIndex.entrySet()) {
                    Object tfInfo = entry.getValue();
                    Map<String, Object> tfData = new HashMap<>();

                    try {
                        // 使用反射获取字段值
                        java.lang.reflect.Field offsetField = tfInfo.getClass().getDeclaredField("offset");
                        java.lang.reflect.Field countField = tfInfo.getClass().getDeclaredField("valueCount");
                        offsetField.setAccessible(true);
                        countField.setAccessible(true);

                        tfData.put("offset", offsetField.get(tfInfo));
                        tfData.put("valueCount", countField.get(tfInfo));

                    } catch (Exception e) {
                        System.out.println("warning: no get TF info, use default value");
                        tfData.put("offset", 0L);
                        tfData.put("valueCount", 0);
                    }

                    compatibleIndex.put(entry.getKey(), tfData);
                }

                return compatibleIndex;
            }

            throw new IOException("index file format no support");

        } catch (ClassNotFoundException e) {
            throw new IOException("index file format error", e);
        }
    }

    /**
     * 获取TF在所有细胞中的活性值
     */
    public float[] getTfActivity(String tfName) {
        if (!initialized) {
            throw new IllegalStateException("reader no init");
        }

        Map<String, Object> tfInfo = tfIndex.get(tfName);
        if (tfInfo == null) {
            System.out.println("warning: TF '" + tfName + "' no exist");
            return new float[totalCells];
        }

        long offset = (Long) tfInfo.get("offset");
        int valueCount = (Integer) tfInfo.get("valueCount");

        // 定位到TF数据并读取
        ((java.nio.Buffer) buffer).position((int) offset);
        float[] activity = new float[valueCount];

        int actualCount = buffer.getInt();
        for (int i = 0; i < actualCount; i++) {
            activity[i] = buffer.getFloat();
        }

        return activity;
    }

    /**
     * 获取TF活性值并包含统计信息
     */
    public TfActivityResult getTfActivityWithStats(String tfName) {
        float[] activity = getTfActivity(tfName);

        float maxValue = Float.MIN_VALUE;
        float minValue = Float.MAX_VALUE;
        float sum = 0;
        int count = activity.length;

        for (float value : activity) {
            if (value > maxValue) maxValue = value;
            if (value < minValue) minValue = value;
            sum += value;
        }

        float average = count > 0 ? sum / count : 0;

        return new TfActivityResult(activity, maxValue, minValue, average, count);
    }

    /**
     * 获取所有TF名称
     */
    public String[] getAllTfNames() {
        return tfIndex.keySet().toArray(new String[0]);
    }

    /**
     * 检查TF是否存在
     */
    public boolean containsTf(String tfName) {
        return tfIndex.containsKey(tfName);
    }

    /**
     * 获取细胞总数
     */
    public int getTotalCells() {
        return totalCells;
    }

    /**
     * 获取TF总数
     */
    public int getTotalTfs() {
        return tfIndex.size();
    }

    /**
     * 关闭读取器
     */
    public void close() {
        buffer = null;
        tfIndex.clear();
        initialized = false;
    }

    /**
     * TF活性结果类
     */
    public static class TfActivityResult {
        private final float[] activity;
        private final float maxValue;
        private final float minValue;
        private final float average;
        private final int cellCount;

        public TfActivityResult(float[] activity, float maxValue, float minValue, float average, int cellCount) {
            this.activity = activity;
            this.maxValue = maxValue;
            this.minValue = minValue;
            this.average = average;
            this.cellCount = cellCount;
        }

        public float[] getActivity() { return activity; }
        public float getMaxValue() { return maxValue; }
        public float getMinValue() { return minValue; }
        public float getAverage() { return average; }
        public int getCellCount() { return cellCount; }
    }

    /**
     * 主方法示例
     */
    public static void main(String[] args) {

        String tfName = "Ahdc1(+)";
        String binaryFile = "D:\\Workspace\\webdata\\Stroke_001_tf_activity.bin";
        String indexFile = "D:\\Workspace\\webdata\\Stroke_001_tf_index.idx";

        TfActivityReader reader = new TfActivityReader();

        try {
            reader.initialize(binaryFile, indexFile);

            long startTime = System.currentTimeMillis();
            TfActivityResult result = reader.getTfActivityWithStats(tfName);
            long endTime = System.currentTimeMillis();

            System.out.println("=== TF activate result ===");
            System.out.println("TF name: " + tfName);
            System.out.println("cells number: " + result.getCellCount());
            System.out.println("max: " + result.getMaxValue());
            System.out.println("min: " + result.getMinValue());
            System.out.println("average: " + result.getAverage());
            System.out.println("read time: " + (endTime - startTime) + "ms");

            // 显示前几个值
            System.out.println("top 5 cells activate:");
            float[] activity = result.getActivity();
            for (int i = 0; i < Math.min(5, activity.length); i++) {
                System.out.printf("  cell%d: %.4f%n", i, activity[i]);
            }

        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }
}