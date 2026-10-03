# -*- coding: utf-8 -*-
# =========================================================================
# scCVD 网站冒烟测试 v3（Q7: automated testing + performance measurement）
#
# 两层测试：
#   页面层 —— 主要路由返回 200 且含关键内容
#   接口层 —— 样本详情页的全部数据接口返回 200 且 JSON/文件非空
# v3: DataTables 服务端分页接口必须带完整 columns 定义，
#     否则后端返回空数据（v2 中 6 个 empty payload 均因此）
# 结果追加写入 smoke_test_log.csv，供每日定时运行留存日志
# =========================================================================

import time
import csv
import os
import datetime
import requests

# ------------------------- CONFIG -------------------------
BASE_URL  = "http://localhost:8080/sccvd"   # 网站服务器本地地址（已含 /sccvd 子路径）
SAMPLE_ID = "Sample_010"                    # 常规测试样本
BIG_SAMPLE = "Sample_042"                   # 大数据集（性能测试用）
TIMEOUT   = 60
OUT_CSV   = "smoke_test_log.csv"

def dt(cols, length=10):
    """按浏览器实际请求格式构造 DataTables 服务端分页参数"""
    p = "draw=1"
    for i, c in enumerate(cols):
        p += (f"&columns[{i}][data]={c}&columns[{i}][name]="
              f"&columns[{i}][searchable]=true&columns[{i}][orderable]=true"
              f"&columns[{i}][search][value]=&columns[{i}][search][regex]=false")
    p += (f"&order[0][column]=0&order[0][dir]=asc&order[0][name]="
          f"&start=0&length={length}&search[value]=&search[regex]=false")
    return p

DT_SAMPLE_INFO  = dt(["gsmId","sampleName","tissue","age","sex","treatment","times"], length=5)
DT_DEG          = dt(["gene","celltype","p_value","avg_log2fc","p_value_adj","pct1","pct2"])
DT_CELLCHAT     = dt(["source","target","ligand","receptor","prob","pval",
                      "interaction_name","interaction_name_2","pathway_name",
                      "annotation","evidence"])
DT_WGCNA        = dt(["umap1","umap1","gene","module","color","hub","kme"])
DT_RNA_SAMPLE   = dt(["gseId","gsmId","sampleName","species","tissue","age","sex",
                      "treatment","disease","times","genomic","strain","pmid",
                      "journal","year"])
DT_RNA_GROUP    = dt(["gse_id","case_group","ctrl_group","sample_n","sample_name","times"])

PAGE_ENDPOINTS = [
    ("page_home",      "/",              "scCVD"),
    ("page_browse",    "/browse",        "dataTable"),
    ("page_search",    "/search",        None),
    ("page_download",  "/download",      None),
    ("page_help",      "/help",          None),
    ("page_anaByGene", "/anaByGene",     None),
    ("page_geneEnrichment", "/geneEnrichment", None),
    ("page_geneExpressed",  "/geneExpressed",  None),
    ("page_comparison",     "/comparison",     None),
    ("page_detail",    f"/getDetailViewGL?id={SAMPLE_ID}", SAMPLE_ID),
]

def api(name, path, kind="json"):
    return (name, path, kind)

API_ENDPOINTS = [
    api("api_sample_descript",  f"/getSampleDescript?id={SAMPLE_ID}"),
    api("api_sample_info",      f"/getSampleInfo?id={SAMPLE_ID}&{DT_SAMPLE_INFO}"),
    api("api_cell_count",       f"/getCellCount?id={SAMPLE_ID}&type=celltype"),
    api("api_qc_info",          f"/getQcInfo?id={SAMPLE_ID}&type=nFeature_RNA"),
    api("api_umap_nodes",       f"/getNodeDataGL?id={SAMPLE_ID}"),          # deck.gl UMAP 数据
    api("api_dot_marker",       f"/getDotMarker?id={SAMPLE_ID}"),
    api("api_deg_table",        f"/getDegTable?id={SAMPLE_ID}&{DT_DEG}"),
    api("api_celltypes",        f"/getCelltypeBySample?id={SAMPLE_ID}&type=celltype"),
    api("api_go",               f"/getPathwayGo?id={SAMPLE_ID}&param1=Microglia&param2=BP&param3=0.005&type=celltype"),
    api("api_kegg",             f"/getPathwayKEGG?id={SAMPLE_ID}&param1=Microglia&param2=0.005&type=celltype"),
    api("api_cellchat_graph",   f"/getNodeCellInteraction?id={SAMPLE_ID}&type=count"),
    api("api_cellchat_table",   f"/toCellchatTab?id={SAMPLE_ID}&{DT_CELLCHAT}"),
    api("api_wgcna_table",      f"/getWgcnaTab?id={SAMPLE_ID}&{DT_WGCNA}"),
    api("api_gene_list",        f"/getGeneNameBinary?id={SAMPLE_ID}"),
    api("api_bulk_sample_tab",  f"/getRnaSampleTable?id={SAMPLE_ID}&{DT_RNA_SAMPLE}"),
    api("api_bulk_group_tab",   f"/getRnaSampleGroupTable?id={SAMPLE_ID}&{DT_RNA_GROUP}"),
    api("api_bulk_bubble",      f"/getRnaBubbleData?id={SAMPLE_ID}&gene=Adora2b"),
    api("api_bulk_group",       f"/getRnaGroup?id={SAMPLE_ID}&gene=Adora2b"),
    api("static_expr_file",     f"/{SAMPLE_ID}_logfc_expression.txt", "file"),
    api("static_monocle_png",   f"/{SAMPLE_ID}_monocle.replot.png", "file"),
    api("static_wgcna_png",     f"/{SAMPLE_ID}_wgcna_hubGeneNetwork.png", "file"),
    # 大数据集性能专项
    api("perf_big_umap",        f"/getNodeDataGL?id={BIG_SAMPLE}"),
    api("perf_big_detail_page", f"/getDetailViewGL?id={BIG_SAMPLE}", "html"),
]

NEGATIVE_TESTS = [
    ("neg_invalid_sample", f"/getDetailViewGL?id=Sample_9999"),
    ("neg_invalid_api",    f"/getNodeDataGL?id=Sample_9999"),
]
# -----------------------------------------------------------

def run_check(name, url, validate):
    t0 = time.time()
    try:
        r = requests.get(url, timeout=TIMEOUT)
        elapsed = round(time.time() - t0, 2)
        ok, note = validate(r)
        return {"name": name, "url": url, "status": r.status_code,
                "elapsed_s": elapsed, "pass": ok, "note": note}
    except requests.RequestException as e:
        return {"name": name, "url": url, "status": -1,
                "elapsed_s": round(time.time() - t0, 2), "pass": False,
                "note": f"request failed: {type(e).__name__}"}

def v_page(marker):
    def f(r):
        if r.status_code != 200:
            return False, "bad status"
        if marker and marker not in r.text:
            return False, f"marker '{marker}' missing"
        return True, ""
    return f

def v_json(r):
    if r.status_code != 200:
        return False, "bad status"
    try:
        j = r.json()
    except ValueError:
        return False, "not JSON"
    empty = (j is None) or (j == {}) or (j == []) or \
            (isinstance(j, dict) and "data" in j and not j["data"])
    return (not empty), ("empty payload" if empty else "")

def v_file(r):
    if r.status_code != 200:
        return False, "bad status"
    ok = len(r.content) > 100
    return ok, ("" if ok else "file too small")

def v_html(r):
    if r.status_code != 200:
        return False, "bad status"
    ok = len(r.text) > 500
    return ok, ("" if ok else "blank page")

def v_negative(r):
    # 优雅处理 = 非500崩溃，且返回了页面内容或JSON错误信息
    if r.status_code == 500:
        return False, "500 crash"
    try:
        r.json()
        return True, ""          # JSON 错误信息也算优雅
    except ValueError:
        pass
    ok = len(r.text) > 500
    return ok, ("" if ok else "blank page")

def main():
    ts = datetime.datetime.now().isoformat(timespec="seconds")
    results = []
    for name, path, marker in PAGE_ENDPOINTS:
        results.append(run_check(name, BASE_URL + path, v_page(marker)))
    for name, path, kind in API_ENDPOINTS:
        v = {"json": v_json, "file": v_file, "html": v_html}[kind]
        results.append(run_check(name, BASE_URL + path, v))
    for name, path in NEGATIVE_TESTS:
        results.append(run_check(name, BASE_URL + path, v_negative))

    for r in results:
        r["timestamp"] = ts
    n_pass = sum(r["pass"] for r in results)
    print(f"[{ts}] {n_pass}/{len(results)} passed")
    for r in results:
        flag = "OK  " if r["pass"] else "FAIL"
        print(f"  {flag} {r['name']:24s} {r['status']:>4}  {r['elapsed_s']:>7}s  {r['note']}")

    write_header = not os.path.exists(OUT_CSV)
    with open(OUT_CSV, "a", newline="", encoding="utf-8") as f:
        w = csv.DictWriter(f, fieldnames=["timestamp","name","url","status","elapsed_s","pass","note"])
        if write_header:
            w.writeheader()
        w.writerows(results)
    print(f"log appended -> {OUT_CSV}")

if __name__ == "__main__":
    main()
