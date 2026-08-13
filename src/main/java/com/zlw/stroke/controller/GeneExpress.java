package com.zlw.stroke.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zlw.stroke.mapper.Browse;
import com.zlw.stroke.mapper.DetailMapper;
import com.zlw.stroke.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GeneExpress {
    @Autowired(required = false)
    private DetailMapper detail;

    @RequestMapping("geneExpressData")
    @ResponseBody
    public Map geneExpressData(HttpServletRequest request, @RequestParam(required = false) String[] genes) throws InterruptedException {
        String sampleid = request.getParameter("id");
        Map map = new HashMap();
        List<GeneExpressData> dataList = new ArrayList<>();
        try {
            dataList = detail.getGeneExpressData(genes, sampleid.toLowerCase()+"_gene_celltype");
            scaleGeneExpression(dataList);
            String[][] result = dataList.stream()
                    .map(data -> new String[]{
                            data.getGene(), data.getCell_type(), data.getAvg_exp(), data.getPct_exp(), data.getN_cells(), data.getAvg_exp_scale()})
                    .toArray(String[][]::new);
            // 使用LinkedHashSet保持插入顺序
            Set<String> geneSet = new LinkedHashSet<>();
            // 使用LinkedHashMap保持插入顺序，并记录每个celltype第一次出现的n_cells
            Map<String, String> celltypeMap = new LinkedHashMap<>();
            double maxAvgExp = Double.MIN_VALUE;
            double maxPctExp = Double.MIN_VALUE;
            // 按原始顺序遍历，保证第一次出现的顺序被保留
            for (GeneExpressData data : dataList) {
                // gene去重：只有第一次出现的gene会被加入
                geneSet.add(data.getGene());
                // celltype去重：只有第一次出现的celltype会被记录
                celltypeMap.putIfAbsent(data.getCell_type(), data.getN_cells());
                maxAvgExp = Math.max(maxAvgExp, Double.parseDouble(data.getAvg_exp()));
                maxPctExp = Math.max(maxPctExp, Double.parseDouble(data.getPct_exp()));
            }
            // 转换为数组
            String[] geneArray = geneSet.toArray(new String[0]);
            Object[][] celltypeDataArray = new Object[celltypeMap.size()][2];
            int index = 0;
            for (Map.Entry<String, String> entry : celltypeMap.entrySet()) {
                celltypeDataArray[index][0] = entry.getKey();
                celltypeDataArray[index][1] = entry.getValue();
                index++;
            }
            map.put("data", result);
            map.put("gene", geneArray);
            map.put("maxAvgExp", maxAvgExp);
            map.put("maxPctExp", maxPctExp);
            map.put("celltype", celltypeDataArray);
            return map;
        } catch (Exception e) {
            String[][] result = new String[0][0];
            String[] geneArray = new String[0];
            double maxAvgExp = 0;
            double maxPctExp = 0;
            Object[][] celltypeDataArray = new String[0][0];
            map.put("data", result);
            map.put("gene", geneArray);
            map.put("maxAvgExp", maxAvgExp);
            map.put("maxPctExp", maxPctExp);
            map.put("celltype", celltypeDataArray);
            return map;
        }
    }

    // 可选的数字格式化，控制小数位数
    private static final DecimalFormat df = new DecimalFormat("0.######");

    /**
     * 对基因表达数据进行[0,1]标准化，结果存储为String
     */
    public static void scaleGeneExpression(List<GeneExpressData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // 1. 提取并转换avg_exp为Double类型，过滤无效数据
        List<Double> expressions = dataList.stream()
                .map(GeneExpressData::getAvg_exp)
                .map(avgExp -> {
                    try {
                        return Double.parseDouble(avgExp);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(value -> value != null)
                .collect(Collectors.toList());  // 修复：使用 collect(Collectors.toList())

        if (expressions.isEmpty()) {
            // 如果所有数据都无效，将所有avg_exp_scale设为null
            dataList.forEach(data -> data.setAvg_exp_scale(null));
            return;
        }

        // 2. 计算最小值和最大值
        double min = expressions.stream().min(Double::compare).orElse(0.0);
        double max = expressions.stream().max(Double::compare).orElse(1.0);

        // 3. 如果所有值都相同，避免除以0
        if (max == min) {
            dataList.forEach(data -> {
                try {
                    Double.parseDouble(data.getAvg_exp());
                    // 所有相同值设为"0.5"
                    data.setAvg_exp_scale("0.5");
                } catch (NumberFormatException e) {
                    data.setAvg_exp_scale(null); // 无效数据设为null
                }
            });
            return;
        }

        // 4. 对每个数据进行标准化并转换为String
        double range = max - min;
        dataList.forEach(data -> {
            try {
                double value = Double.parseDouble(data.getAvg_exp());
                double scaledValue = (value - min) / range;
                // 将double转换为String，使用格式化控制小数位数
                data.setAvg_exp_scale(df.format(scaledValue));
            } catch (NumberFormatException e) {
                data.setAvg_exp_scale(null); // 对于无法解析的数据设为null
            }
        });
    }

    /**
     * 重载方法：可以自定义小数位数
     */
    public static void scaleGeneExpression(List<GeneExpressData> dataList, int decimalPlaces) {
        // 动态创建DecimalFormat
        StringBuilder pattern = new StringBuilder("0.");
        for (int i = 0; i < decimalPlaces; i++) {
            pattern.append("#");
        }
        DecimalFormat customDf = new DecimalFormat(pattern.toString());

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // 1. 提取并转换avg_exp为Double类型，过滤无效数据
        List<Double> expressions = dataList.stream()
                .map(GeneExpressData::getAvg_exp)
                .map(avgExp -> {
                    try {
                        return Double.parseDouble(avgExp);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(value -> value != null)
                .collect(Collectors.toList());  // 修复：使用 collect(Collectors.toList())

        if (expressions.isEmpty()) {
            dataList.forEach(data -> data.setAvg_exp_scale(null));
            return;
        }

        // 2. 计算最小值和最大值
        double min = expressions.stream().min(Double::compare).orElse(0.0);
        double max = expressions.stream().max(Double::compare).orElse(1.0);

        // 3. 如果所有值都相同，避免除以0
        if (max == min) {
            dataList.forEach(data -> {
                try {
                    Double.parseDouble(data.getAvg_exp());
                    data.setAvg_exp_scale(customDf.format(0.5));
                } catch (NumberFormatException e) {
                    data.setAvg_exp_scale(null);
                }
            });
            return;
        }

        // 4. 对每个数据进行标准化并转换为String
        double range = max - min;
        dataList.forEach(data -> {
            try {
                double value = Double.parseDouble(data.getAvg_exp());
                double scaledValue = (value - min) / range;
                data.setAvg_exp_scale(customDf.format(scaledValue));
            } catch (NumberFormatException e) {
                data.setAvg_exp_scale(null);
            }
        });
    }

    /**
     * 兼容性更好的版本：不使用Stream API
     */
    public static void scaleGeneExpressionCompatible(List<GeneExpressData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // 1. 提取有效的数值并找到最小最大值
        List<Double> validValues = new ArrayList<>();
        for (GeneExpressData data : dataList) {
            try {
                double value = Double.parseDouble(data.getAvg_exp());
                validValues.add(value);
            } catch (NumberFormatException e) {
                // 忽略无效数据
            }
        }

        if (validValues.isEmpty()) {
            for (GeneExpressData data : dataList) {
                data.setAvg_exp_scale(null);
            }
            return;
        }

        // 2. 计算最小值和最大值
        double min = validValues.get(0);
        double max = validValues.get(0);
        for (double value : validValues) {
            if (value < min) min = value;
            if (value > max) max = value;
        }

        // 3. 如果所有值都相同
        if (max == min) {
            for (GeneExpressData data : dataList) {
                try {
                    Double.parseDouble(data.getAvg_exp());
                    data.setAvg_exp_scale("0.5");
                } catch (NumberFormatException e) {
                    data.setAvg_exp_scale(null);
                }
            }
            return;
        }

        // 4. 标准化处理
        double range = max - min;
        for (GeneExpressData data : dataList) {
            try {
                double value = Double.parseDouble(data.getAvg_exp());
                double scaledValue = (value - min) / range;
                data.setAvg_exp_scale(df.format(scaledValue));
            } catch (NumberFormatException e) {
                data.setAvg_exp_scale(null);
            }
        }
    }
}
