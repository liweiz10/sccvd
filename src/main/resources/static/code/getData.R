# 使用细胞类型差异基因做背景，进行基因富集分析。
enrichment<-function(x,adjust,threshold=0.05,bgfile) {
	load(bgfile)
	input<-c(x)
	n<-length(input)
	result<-matrix(NA,length(final_result$sampleid),20)
	ignore_case_intersect <- function(x, y) {
	  common <- intersect(tolower(x), tolower(y))
	  x[match(common, tolower(x))]
	}
	for (i in 1:length(final_result$sampleid)) {
		result[i,1]<-final_result$sampleid[[i]]
		result[i,2]<-final_result$cluster[[i]]
		result[i,3]<-final_result$gene_count[[i]]
		inter<-ignore_case_intersect(x,final_result$gene_set[[i]])
		#inter<-intersect(final_result$gene_set[[i]],x)
		result[i,4]<- paste(inter,collapse=", ")
		t<-length(final_result$gene_set[[i]])
		r<-length(inter)
		result[i,5]<-signif(phyper(r-1,t,20000-t,n,lower.tail=FALSE),3)
		result[i,7]<-r
		result[i,8]<-final_result$gse_id[[i]]
		result[i,9]<-final_result$species[[i]]
		result[i,10]<-final_result$tissue[[i]]
		result[i,11]<-final_result$age[[i]]
		result[i,12]<-final_result$disease[[i]]
		result[i,13]<-final_result$sample[[i]]
# 		result[i,14]<-final_result$genomic[[i]]
# 		result[i,15]<-final_result$technology[[i]]
# 		result[i,16]<-final_result$strain[[i]]
		result[i,17]<-final_result$pmid[[i]]
		result[i,18]<-final_result$article[[i]]
		result[i,19]<-final_result$journal[[i]]
		result[i,20]<-final_result$year[[i]]
	}
	result[,6]<-signif(p.adjust(result[,5],method="fdr",n=length(final_result$sampleid)),3)
	if (adjust==1) {
		result<-result[which(as.numeric(result[,6])<as.numeric(threshold)),]
		result<-result[sort.list(as.numeric(result[,6]),decreasing=F),]
	} else {
		result<-result[which(as.numeric(result[,5])<as.numeric(threshold)),]
		result<-result[sort.list(as.numeric(result[,5]),decreasing=F),]
	}
	result <- as.data.frame(result)
	result <- data.frame(lapply(result, as.character), stringsAsFactors = FALSE)
	return(result)
}

# 读取基因表达矩阵
get_gene_expression_fast <- function(h5_file, gene_index) {
  return(gene_expression)
}

getGeneExp <- function(file,gene){
    library(rhdf5)
    h5file <- H5Fopen(file)
    gene_names <- h5read(h5file, "gene_names")
    gene_index <- 1  # 目标基因名
    if(gene!="error"){
        gene_index <- which(gene_names == gene)
    }

    # 读取稀疏矩阵的组件
    i <- h5read(h5file, "expression_matrix/i")  # 行索引
    p <- h5read(h5file, "expression_matrix/p")  # 列指针
    x <- h5read(h5file, "expression_matrix/x")  # 非零值
    dim <- h5read(h5file, "expression_matrix/dim")  # 矩阵维度

    # 获取每列范围，并初始化结果
    gene_expression <- numeric(dim[2])
    # 遍历列指针的范围
    for (cell_index in 1:(length(p) - 1)) {
        start <- p[cell_index] + 1
        end <- p[cell_index + 1]
        # 检查目标基因是否在当前列中
        col_indices <- i[start:end]
        match_index <- which(col_indices == gene_index - 1)  # 基因索引从 0 开始
        if (length(match_index) > 0) {
            gene_expression[cell_index] <- x[start:end][match_index]
        }
    }
    umap_1 <- h5read(h5file, "umap_1")
    umap_2 <- h5read(h5file, "umap_2")
    exp <- cbind(umap_1, umap_2, gene_expression)
    max <- ceiling(max(gene_expression))
    result <- list(exp,max)
    h5closeAll()
    return(result)
}

getGeneExpValue <- function(file,gene){
    library(rhdf5)
    h5file <- H5Fopen(file)
    gene_names <- h5read(h5file, "gene_names")
    gene_index <- 1  # 目标基因名
    if(gene!="error"){
        gene_index <- which(gene_names == gene)
    }
    # 读取稀疏矩阵的组件
    i <- h5read(h5file, "expression_matrix/i")  # 行索引
    p <- h5read(h5file, "expression_matrix/p")  # 列指针
    x <- h5read(h5file, "expression_matrix/x")  # 非零值
    dim <- h5read(h5file, "expression_matrix/dim")  # 矩阵维度
    # 获取每列范围，并初始化结果
    gene_expression <- numeric(dim[2])
    # 遍历列指针的范围
    for (cell_index in 1:(length(p) - 1)) {
        start <- p[cell_index] + 1
        end <- p[cell_index + 1]
        # 检查目标基因是否在当前列中
        col_indices <- i[start:end]
        match_index <- which(col_indices == gene_index - 1)  # 基因索引从 0 开始
        if (length(match_index) > 0) {
            gene_expression[cell_index] <- x[start:end][match_index]
        }
    }
    max <- ceiling(max(gene_expression))
    result <- list(gene_expression,max)
    h5closeAll()
    return(result)
}

getGeneName <- function(file){
    library(rhdf5)
    gene_names <- h5read(file, "gene_names")
    return(gene_names)
}



########tf expression of pyscenic#########
getTFName <- function(file){
    library(rhdf5)
    tf_names <- h5read(file, "row_names")
    return(tf_names)
}

extract_tf_from_h5 <- function(file, tf_name) {
	tf_names <- h5read(file, "row_names")
	cell_names <- h5read(file, "col_names")
	if(!tf_name %in% tf_names){
		result <- rep(0, length(cell_names))
	}else{
		row_idx <- which(tf_names == tf_name) - 1
		indices <- h5read(file, "indices")
		indptr <- h5read(file, "indptr")
		data <- h5read(file, "data")
		result <- rep(0, length(cell_names))
		for (col in 1:(length(indptr)-1)) {
			start <- indptr[col] + 1
			end <- indptr[col + 1]
			if (start <= end) {
			  idx_in_range <- which(indices[start:end] == row_idx)
			  if (length(idx_in_range) > 0) {
				result[col] <- data[start + idx_in_range - 1]
			  }
			}
		}
	}
	h5closeAll()
	umap_1 <- h5read(file, "umap_1")
    umap_2 <- h5read(file, "umap_2")
    exp <- cbind(umap_1, umap_2, result*100)
	max <- max(result*100)
    result <- list(exp,max)
    return(result)
}