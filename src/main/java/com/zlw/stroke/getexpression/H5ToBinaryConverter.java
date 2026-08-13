package com.zlw.stroke.getexpression;

import io.jhdf.HdfFile;
import io.jhdf.api.Dataset;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class H5ToBinaryConverter {

    public static void main(String[] args) throws Exception {
//        "Sample_048",矩阵太大
//        String stroke = "Sample_057";"Sample_045", "Sample_046","Sample_047",
        String[] samples1 = {"Sample_049", "Sample_050", "Sample_051", "Sample_052", "Sample_053", "Sample_054", "Sample_055", "Sample_056", "Sample_057", "Sample_058", "Sample_059", "Sample_060", "Sample_061", "Sample_062", "Sample_063", "Sample_064", "Sample_065", "Sample_066", "Sample_067", "Sample_068", "Sample_071", "Sample_072", "Sample_073", "Sample_074", "Sample_075", "Sample_076", "Sample_077", "Sample_078", "Sample_079", "Sample_080", "Sample_081", "Sample_082", "Sample_083", "Sample_084", "Sample_085"};
        String[] samples2 = {"Sample_048"};
        String[] sampleatacs1 = {"Sample_070","Sample_087","Sample_088","Sample_089"};
        String[] sampleatacs2 = {"Sample_069"};
        String[] sampleatacs3 = {"Sample_086"};
        String[] sampleatacs4 = {"Sample_086"};
        String[] sampleatacs = {"Sample_090"};
        for (String stroke : sampleatacs) {
            System.out.println(stroke);
            String h5FilePath = "D:\\Workspace\\webdata\\" + stroke + "_cicero_gene_activity_matrix.h5";
//            String h5FilePath = "D:\\Workspace\\webdata\\" + stroke + "_signac_gene_activity_matrix.h5";
//            String h5FilePath = "D:\\Workspace\\webdata\\" + stroke + "_expression_matrix.h5";
            String binaryFilePath = "D:\\Workspace\\webdata\\" + stroke + "_expression.bin";
//            String binaryFilePath = "D:\\Workspace\\webdata\\" + stroke + "_signac_expression.bin";
            String indexPath = "D:\\Workspace\\webdata\\" + stroke + "_gene_index.idx";
//            String indexPath = "D:\\Workspace\\webdata\\" + stroke + "_signac_gene_index.idx";

            System.out.println("Starting H5 file conversion: " + h5FilePath);
            convertH5ToBinary(h5FilePath, binaryFilePath, indexPath);
            System.out.println("Conversion completed successfully!");
        }

    }

    public static void convertH5ToBinary(String h5FilePath, String binaryFilePath, String indexPath) throws Exception {
        try (HdfFile hdfFile = new HdfFile(Paths.get(h5FilePath))) {

            // 1. 读取基因名称和维度
            Dataset geneNamesDataset = hdfFile.getDatasetByPath("gene_names");
            String[] geneNames = (String[]) geneNamesDataset.getData();

            Dataset dimDataset = hdfFile.getDatasetByPath("expression_matrix/dim");
            int[] dimensions = (int[]) dimDataset.getData();

            System.out.println("Found " + geneNames.length + " genes, " +
                    dimensions[1] + " cells");

            // 2. 读取稀疏矩阵数据
            Dataset rowIndicesDataset = hdfFile.getDatasetByPath("expression_matrix/i");
            Dataset colPointersDataset = hdfFile.getDatasetByPath("expression_matrix/p");
            Dataset valuesDataset = hdfFile.getDatasetByPath("expression_matrix/x");

            int[] rowIndices = readDatasetAsInt(rowIndicesDataset);
            int[] colPointers = readDatasetAsInt(colPointersDataset);
            float[] values = readDatasetAsFloat(valuesDataset);

            System.out.println("Total non-zero values: " + values.length);

            // 3. 创建二进制文件
            createBinaryFile(binaryFilePath, indexPath, geneNames,
                    rowIndices, colPointers, values, dimensions);
        }
    }

    private static int[] readDatasetAsInt(Dataset dataset) throws IOException {
        Object data = dataset.getData();
        if (data instanceof int[]) {
            return (int[]) data;
        } else if (data instanceof double[]) {
            double[] doubleData = (double[]) data;
            int[] intData = new int[doubleData.length];
            for (int i = 0; i < doubleData.length; i++) {
                intData[i] = (int) doubleData[i];
            }
            return intData;
        }
        throw new IllegalArgumentException("Unsupported data type for int array");
    }
    private static float[] readDatasetAsFloat(Dataset dataset) throws IOException {
        // 直接读取，但增加JVM内存到8GB以上
        Object data = dataset.getData();

        if (data instanceof float[]) {
            return (float[]) data;
        } else if (data instanceof double[]) {
            double[] doubleData = (double[]) data;
            float[] floatData = new float[doubleData.length];

            // 分批转换避免大数组操作
            int batchSize = 1_000_000;
            for (int i = 0; i < doubleData.length; i += batchSize) {
                int end = Math.min(i + batchSize, doubleData.length);
                for (int j = i; j < end; j++) {
                    floatData[j] = (float) doubleData[j];
                }
                if (i % (batchSize * 10) == 0) {
                    System.out.println("Converting: " + i + "/" + doubleData.length);
                }
            }
            return floatData;
        }
        throw new IllegalArgumentException("Unsupported data type");
    }

    private static void createBinaryFile(String binaryPath, String indexPath,
                                         String[] geneNames, int[] rowIndices,
                                         int[] colPointers, float[] values,
                                         int[] dimensions) throws IOException {

        Map<String, GeneDataInfo> geneIndexMap = new HashMap<>();

        // 1. 统计每个基因的非零值数量
        int[] geneCounts = new int[dimensions[0]];
        for (int cellIdx = 0; cellIdx < dimensions[1]; cellIdx++) {
            int start = colPointers[cellIdx];
            int end = (cellIdx < colPointers.length - 1) ? colPointers[cellIdx + 1] : values.length;

            for (int i = start; i < end; i++) {
                int geneIdx = rowIndices[i];
                geneCounts[geneIdx]++;
            }
        }

        // 2. 准备基因数据（按基因组织）
        List<float[]>[] geneTempData = new List[dimensions[0]];
        for (int i = 0; i < dimensions[0]; i++) {
            geneTempData[i] = new ArrayList<>(geneCounts[i]);
        }

        for (int cellIdx = 0; cellIdx < dimensions[1]; cellIdx++) {
            int start = colPointers[cellIdx];
            int end = (cellIdx < colPointers.length - 1) ? colPointers[cellIdx + 1] : values.length;

            for (int i = start; i < end; i++) {
                int geneIdx = rowIndices[i];
                float value = values[i];
                geneTempData[geneIdx].add(new float[]{cellIdx, value});
            }
        }

        // 3. 创建二进制文件（使用正确的偏移量计算）
        try (FileOutputStream fos = new FileOutputStream(binaryPath);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             DataOutputStream dos = new DataOutputStream(bos)) {

            // 3.1 写入文件头
            dos.writeInt(dimensions[0]);  // 基因数量
            dos.writeInt(dimensions[1]);  // 细胞数量
            dos.writeInt(values.length);  // 非零值数量

            // 3.2 写入稀疏矩阵数据
            writeIntArray(dos, rowIndices);
            writeIntArray(dos, colPointers);
            writeFloatArray(dos, values);

            // 3.3 计算基因数据的起始偏移量
            long headerSize = 12L;  // 3个int
            long rowIndicesSize = 4L + rowIndices.length * 4L;
            long colPointersSize = 4L + colPointers.length * 4L;
            long valuesSize = 4L + values.length * 4L;

            long geneDataStartOffset = headerSize + rowIndicesSize + colPointersSize + valuesSize;

            System.out.println("Gene data starts at offset: " + geneDataStartOffset);

            // 3.4 写入基因数据（使用正确的偏移量）
            long currentOffset = geneDataStartOffset;

            for (int geneIdx = 0; geneIdx < geneNames.length; geneIdx++) {
                String geneName = geneNames[geneIdx];
                if (geneName == null || geneName.trim().isEmpty()) {
                    // 对于空基因名，写入空数据但保留位置
                    dos.writeInt(0);
                    currentOffset += 4L;
                    continue;
                }

                List<float[]> exprs = geneTempData[geneIdx];
                int count = exprs.size();

                // 记录当前偏移量
                long geneOffset = currentOffset;

                // 写入基因数据
                dos.writeInt(count);
                for (float[] expr : exprs) {
                    dos.writeInt((int) expr[0]);  // cell index
                    dos.writeFloat(expr[1]);      // value
                }

                // 更新偏移量
                long geneDataSize = 4L + count * 8L;  // 4字节count + count*(4字节索引+4字节值)
                currentOffset += geneDataSize;

                // 保存到索引
                geneIndexMap.put(geneName, new GeneDataInfo(geneOffset, count));

                // 进度提示
                if (geneIdx % 1000 == 0) {
                    System.out.println("Processed " + geneIdx + "/" + geneNames.length +
                            " genes, current offset: " + geneOffset);
                }
            }

            dos.flush();
            System.out.println("Binary file created, total size: " + currentOffset + " bytes");
        }

        // 4. 保存索引文件
        saveIndex(geneIndexMap, indexPath);
        System.out.println("Index file saved with " + geneIndexMap.size() + " genes");

        // 5. 验证索引
        validateIndex(geneIndexMap, geneNames.length);
    }

    private static void writeIntArray(DataOutputStream dos, int[] array) throws IOException {
        dos.writeInt(array.length);
        for (int value : array) {
            dos.writeInt(value);
        }
    }

    private static void writeFloatArray(DataOutputStream dos, float[] array) throws IOException {
        dos.writeInt(array.length);
        for (float value : array) {
            dos.writeFloat(value);
        }
    }

    private static void saveIndex(Map<String, GeneDataInfo> indexMap, String indexPath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(indexPath))) {
            oos.writeObject(indexMap);
        }
    }

    private static void validateIndex(Map<String, GeneDataInfo> indexMap, int totalGenes) {
        System.out.println("\n=== Index Validation ===");
        System.out.println("Index contains " + indexMap.size() + " genes");
        System.out.println("Expected genes: " + totalGenes);

        int zeroCountGenes = 0;
        long minOffset = Long.MAX_VALUE;
        long maxOffset = Long.MIN_VALUE;

        for (Map.Entry<String, GeneDataInfo> entry : indexMap.entrySet()) {
            GeneDataInfo info = entry.getValue();

            if (info.nonZeroCount == 0) {
                zeroCountGenes++;
            }

            if (info.offset < minOffset) {
                minOffset = info.offset;
            }
            if (info.offset > maxOffset) {
                maxOffset = info.offset;
            }

            // 检查几个关键基因
            if (entry.getKey().equals("Cd68") ||
                    entry.getKey().equals("Gapdh") ||
                    entry.getKey().equals("Actb")) {
                System.out.println(String.format("Gene %-10s offset=%-12d count=%-6d",
                        entry.getKey(), info.offset, info.nonZeroCount));
            }
        }

        System.out.println("\nSummary:");
        System.out.println("Min offset: " + minOffset);
        System.out.println("Max offset: " + maxOffset);
        System.out.println("Genes with zero count: " + zeroCountGenes);
        System.out.println("=== Validation Complete ===\n");
    }

    // 内部类，与读取端保持一致
    public static class GeneDataInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        public long offset;
        public int nonZeroCount;

        public GeneDataInfo(long offset, int nonZeroCount) {
            this.offset = offset;
            this.nonZeroCount = nonZeroCount;
        }

        @Override
        public String toString() {
            return "GeneDataInfo{offset=" + offset + ", nonZeroCount=" + nonZeroCount + "}";
        }
    }
}