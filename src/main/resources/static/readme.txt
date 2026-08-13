####### 基因表达可视化的方法（先存为h5，再存为二进制文件会小一点。）
# 基因表达可视化通过Java二进制形式读取。此功能所有代码在“getexpression”包中。
# 需先把表达矩阵文件通过R语言存储为h5文件，再使用H5ToBinnaryConverter类，执行main函数，把h5文件转换为二进制文件（数据：id_expression.bin，基因索引：id_gene_index.idx）。然后通过GeneExpressionReader类中的main函数进行读取。
# 值得注意的是：此功能写入文件时会存储com.zlw.stroke.getexpression.GeneDataInfo类位置信息,如果此类位置或名字发生变化时需重新生成二进制文件才能读取到，否则在读取时会产生找不到com.zlw.stroke.getexpression.H5ToBinaryConverter$GeneDataInfo信息错误。
# 因为Sample_026数据集的文件过大，所以在读取基因表达时使用的方法与其他数据集有些差别，使用的是GeneExpressionReader2类（Stroke_026样本的二进制尽量不要重新生成，会报错，现在的刚好可以运行）。


####### TF活性可视化的方法
TF活性通过Java以二进制形式读写。此功能所有代码在“tfactivate”包中。
使用自定义的TfCsvToBinaryConverter工具把Stroke_001_scenic_regulon_auc.csv文件转存为二进制文件（数据：id_expression.bin，基因索引：id_gene_index.idx），执行一次即可。然后通过TfAvtivityReader工具进行读取。
*** 需要注意的是，TfCsvToBinaryConverter和TfAvtivityReader是配套使用的，即由1生成的二进制文件才能由2读取，因为读取转二进制文件时会存储com.zlw.stroke.tfactivate.TfCsvToBinaryConverte.TfDataInfo类的索引位置信息,在读取时会使用路径com.zlw.stroke.tfactivate.TfCsvToBinaryConverte$TfDataInfo信息。
*** 如果读取的文件中没有存储这个路径，就会发生报错找不到异常。所以当类名和方法名发生更改时，要重新生成文件，才能读取的到。（这里为了防止麻烦，就没有重新更改类名，所以gene和tf的表达，都是通过tfactivate包进行实现的。）


###### 发布时，更改application.properties文件中的数据库参数和静态资源路径即可。

###### 因为服务器部署在Linux上，所以MySQL查询时表名不分大小写，所以需要更改以下表名。
-- 首先查询所有以stroke开头的表
SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_NAME LIKE 'stroke%'
AND TABLE_SCHEMA = '你的数据库名';

-- 然后为每个表执行重命名操作
RENAME TABLE
    stroke_table1 TO Stroke_table1,
    stroke_table2 TO Stroke_table2,
    stroke_table3 TO Stroke_table3;
-- 继续添加所有需要重命名的表





# RNA-seq数据的样本
用物种求基因交集合并DEG值common表转换为长格式mus_musculus_deg_long，rattus_norvegicus_deg_long,homo_sapiens_deg_long
样本信息表：sample_table_rna
数据集信息及表名：rnaseq_group
数据集基因表达长格式：gseid_long
数据集差异分组信息表：rna_deg_analysis_summary
差异基因表：rna_deg_analysis_summary中的output_file列作为表名
