# -*- coding: utf-8 -*-
# =========================================================================
# scCVD 全数据集自检（Q7: "increase the amount of self-testing data"）
#
# 对数据库中全部整合数据集逐一测试核心功能：
#   1. 详情页可打开
#   2. UMAP 节点数据接口返回非空
#   3. Marker dotplot 接口返回非空
#   4. DEG 表接口返回非空
# 用法：python3 smoke_test_all_samples.py
# 结果写入 smoke_test_all_log.csv
# =========================================================================

import time
import csv
import os
import datetime
import requests

BASE_URL = "http://localhost:8080/sccvd"
TIMEOUT  = 120
OUT_CSV  = "smoke_test_all_log.csv"

# 全部 90 个整合数据集 ID（83 scRNA-seq + 7 scATAC-seq）
# 如列表有变动，从数据库 sample 表重新导出覆盖即可
SAMPLE_IDS = [
    "Sample_001","Sample_002","Sample_003","Sample_004","Sample_005","Sample_006","Sample_007","Sample_008",
    "Sample_009","Sample_010","Sample_011","Sample_012","Sample_013","Sample_014","Sample_015","Sample_016",
    "Sample_017","Sample_018","Sample_019","Sample_020","Sample_021","Sample_022","Sample_023","Sample_024",
    "Sample_025","Sample_026","Sample_027","Sample_028","Sample_029","Sample_030","Sample_031","Sample_032",
    "Sample_033","Sample_034","Sample_035","Sample_036","Sample_037","Sample_038","Sample_039","Sample_040",
    "Sample_041","Sample_042","Sample_043","Sample_044","Sample_045","Sample_046","Sample_047","Sample_048",
    "Sample_049","Sample_050","Sample_051","Sample_052","Sample_053","Sample_054","Sample_055","Sample_056",
    "Sample_057","Sample_058","Sample_059","Sample_060","Sample_061","Sample_062","Sample_063","Sample_064",
    "Sample_065","Sample_066","Sample_067","Sample_068","Sample_069","Sample_070","Sample_071","Sample_072",
    "Sample_073","Sample_074","Sample_075","Sample_076","Sample_077","Sample_078","Sample_079","Sample_080",
    "Sample_081","Sample_082","Sample_083","Sample_084","Sample_085","Sample_086","Sample_087","Sample_088",
    "Sample_089","Sample_090",
]

def dt(cols, length=10):
    p = "draw=1"
    for i, c in enumerate(cols):
        p += (f"&columns[{i}][data]={c}&columns[{i}][name]="
              f"&columns[{i}][searchable]=true&columns[{i}][orderable]=true"
              f"&columns[{i}][search][value]=&columns[{i}][search][regex]=false")
    p += (f"&order[0][column]=0&order[0][dir]=asc&order[0][name]="
          f"&start=0&length={length}&search[value]=&search[regex]=false")
    return p

DT_DEG = dt(["gene","celltype","p_value","avg_log2fc","p_value_adj","pct1","pct2"])

# 已知合理空结果：Sample_040 仅一种细胞类型（无细胞类型间 DEG）；
# Sample_069/070/086-090 为 scATAC-seq 数据集（无 RNA marker dotplot）
EXPECTED_EMPTY = {("Sample_040", "deg_table")} | {
    (f"Sample_{i:03d}", "dot_marker") for i in [69, 70, 86, 87, 88, 89, 90]
}

CHECKS = [
    ("detail_page", "/getDetailViewGL?id={sid}", "html"),
    ("umap_nodes",  "/getNodeDataGL?id={sid}",   "json"),
    ("dot_marker",  "/getDotMarker?id={sid}",    "json"),
    ("deg_table",   "/getDegTable?id={sid}&" + DT_DEG, "json"),
]

def check(sid, cname, path, kind):
    url = BASE_URL + path.format(sid=sid)
    t0 = time.time()
    try:
        r = requests.get(url, timeout=TIMEOUT)
        elapsed = round(time.time() - t0, 2)
        if r.status_code != 200:
            ok, note = False, f"status {r.status_code}"
        elif kind == "html":
            ok = len(r.text) > 500
            note = "" if ok else "blank page"
        else:
            try:
                j = r.json()
                empty = (j is None) or (j == {}) or (j == []) or \
                        (isinstance(j, dict) and "data" in j and not j["data"])
                if empty and (sid, cname) in EXPECTED_EMPTY:
                    ok, note = True, "expected empty (by design)"
                else:
                    ok, note = (not empty), ("empty payload" if empty else "")
            except ValueError:
                ok, note = False, "not JSON"
        return {"sample": sid, "check": cname, "url": url, "status": r.status_code,
                "elapsed_s": elapsed, "pass": ok, "note": note}
    except requests.RequestException as e:
        return {"sample": sid, "check": cname, "url": url, "status": -1,
                "elapsed_s": round(time.time() - t0, 2), "pass": False,
                "note": f"request failed: {type(e).__name__}"}

def main():
    ts = datetime.datetime.now().isoformat(timespec="seconds")
    results = []
    for sid in SAMPLE_IDS:
        for cname, path, kind in CHECKS:
            results.append(check(sid, cname, path, kind))
        done = [r for r in results if r["sample"] == sid]
        n_ok = sum(r["pass"] for r in done)
        print(f"{sid}: {n_ok}/{len(CHECKS)}", flush=True)

    for r in results:
        r["timestamp"] = ts
    n_pass = sum(r["pass"] for r in results)
    slow = sorted(results, key=lambda r: -r["elapsed_s"])[:10]
    print(f"\n[{ts}] TOTAL {n_pass}/{len(results)} passed")
    print("slowest 10 requests:")
    for r in slow:
        print(f"  {r['sample']} {r['check']:14s} {r['elapsed_s']:>7}s")
    fails = [r for r in results if not r["pass"]]
    if fails:
        print("failures:")
        for r in fails:
            print(f"  {r['sample']} {r['check']:14s} {r['status']:>4} {r['note']}")

    write_header = not os.path.exists(OUT_CSV)
    with open(OUT_CSV, "a", newline="", encoding="utf-8") as f:
        w = csv.DictWriter(f, fieldnames=["timestamp","sample","check","url","status","elapsed_s","pass","note"])
        if write_header:
            w.writeheader()
        w.writerows(results)
    print(f"log appended -> {OUT_CSV}")

if __name__ == "__main__":
    main()
