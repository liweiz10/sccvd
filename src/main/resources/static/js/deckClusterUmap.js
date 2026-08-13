class UMAPPlotter {
    // 构造函数，接收容器ID和初始配置
    constructor(containerId, options = {}) {
        // 私有属性，每个实例独立
        this.containerId = containerId;
        this.umapData = null;
        this.clusterValue = null;
        this.group = options.initialGroup || "celltype";
        this.id = options.id; // 用于AJAX请求的ID

        // 初始化DeckGL实例
        this.umapGL = new deck.DeckGL({
            container: document.getElementById(containerId),
            initialViewState: {
                longitude: -4,
                latitude: 3,
                zoom: 3.5,
                minZoom: 3,
                maxZoom: 6,
                ...options.initialViewState // 允许覆盖初始视图状态
            },
            controller: true
        });

        // 绑定事件处理函数的上下文
        this.labelChange = this.labelChange.bind(this);
        this.labelShowFun = this.labelShowFun.bind(this);
        this.redraw = this.redraw.bind(this);
        this.eventHandlerGroup = this.eventHandlerGroup.bind(this);

        // 初始化选择器
        this.initSelectGroup();
    }

    // 初始化分组选择下拉列表
    initSelectGroup() {
        const groupData = [{name:"celltype"}, {name: "clusters"}, {name: "origident"}];
        const $selectGroup = $(`#${this.containerId}-select-group`).selectize({
            valueField: 'name',
            labelField: 'name',
            searchField: 'name',
            options: groupData,
            create: false,
            onChange: this.eventHandlerGroup
        });

        this.selectControl = $selectGroup[0].selectize;
        this.selectControl.setValue(this.group);
    }

    // 当name="label"标签的checked发生变化时调用
    labelChange() {
        this.redraw("noUpdateLabel");
    }

    // 鼠标指向时的变化处理
    labelShowFun(param) {
        this.clusterValue = param;
        this.redraw("noUpdateLabel");
    }

    // 重新绘制图形
    redraw(updateLabel = "noUpdateLabel") {
        if (!this.umapData) return;

        const selectedCheckboxes = document.querySelectorAll(`#${this.containerId}-labelName input[name="${this.containerId}-label"]:checked`);
        let selectCluster = Array.from(selectedCheckboxes).map(checkbox => checkbox.value);

        let categoryText = null;
        if (this.group === "celltype") {
            categoryText = this.umapData.celltypeText;
        } else if (this.group === "clusters") {
            categoryText = this.umapData.clusterText;
        } else if (this.group === "origident") {
            categoryText = this.umapData.origText;
        }

        if (!categoryText) return;

        const categorys = [...new Set(categoryText.map(item => item.category))];
        const colorMap = this.colorMapFun(categorys);

        // 更新标签
        if (updateLabel !== "noUpdateLabel") {
            selectCluster = categorys;
            let labelNameHtml = "<div class='form-label'>Clusters and Cell Count</div>\n";

            categorys.forEach((value, index) => {
                // 使用容器ID作为前缀确保事件处理函数唯一性
                labelNameHtml += `
          <label class='form-check' onmouseover='${this.containerId}Plotter.labelShowFun("${value}")'>
            <input class='form-check-input' type='checkbox' 
                   onchange='${this.containerId}Plotter.labelChange()' 
                   name='${this.containerId}-label' 
                   value='${value}' 
                   style='background-color: rgb(${colorMap.get(value)})' 
                   checked />
            <span class='form-check-label'>
              ${value} 
              <span class='status status-blue'>${categoryText[index].count}</span>
            </span>
          </label>\n
        `;
            });

            $(`#${this.containerId}-labelName`).html(labelNameHtml);
        }

        // 创建散点图层
        const nodelayer = new deck.ScatterplotLayer({
            id: `${this.containerId}-scatter-plot`,
            data: this.umapData.data,
            getRadius: d => {
                if (this.group === "celltype") {
                    return d.celltype === this.clusterValue ? 1000 : 1;
                } else if (this.group === "clusters") {
                    return d.clusters === this.clusterValue ? 1000 : 1;
                } else if (this.group === "origident") {
                    return d.origident === this.clusterValue ? 1000 : 1;
                }
            },
            radiusMaxPixels: 100,
            radiusMinPixels: 1,
            radiusScale: 20,
            getPosition: d => [d.x, d.y],
            getFillColor: d => {
                if (this.group === "celltype") {
                    return colorMap.get(d.celltype) || [192, 192, 192];
                } else if (this.group === "clusters") {
                    return colorMap.get(d.clusters) || [192, 192, 192];
                } else if (this.group === "origident") {
                    return colorMap.get(d.origident) || [192, 192, 192];
                }
            },
            getFilterCategory: d => {
                if (this.group === "celltype") {
                    return d.celltype;
                } else if (this.group === "clusters") {
                    return d.clusters;
                } else if (this.group === "origident") {
                    return d.origident;
                }
            },
            updateTriggers: {
                getRadius: [this.clusterValue],
                getFillColor: [this.group],
                getFilterCategory: [this.group]
            },
            filterCategories: selectCluster,
            extensions: [new deck.DataFilterExtension({categorySize: 1})],
        });

        // 创建文本图层
        const textlayer = new deck.TextLayer({
            id: `${this.containerId}-text-layer`,
            data: categoryText,
            getPosition: d => [d.x, d.y],
            getText: d => d.category,
            getColor: [0, 0, 0],
            getSize: 15
        });

        // 更新图层
        this.umapGL.setProps({
            layers: [nodelayer, textlayer]
        });
    }

    // 颜色映射函数
    colorMapFun(categories) {
        const colorMap = new Map();
        const colors = [
            [255, 99, 132], [54, 162, 235], [255, 206, 86], [75, 192, 192],
            [153, 102, 255], [255, 159, 64], [231, 233, 237], [108, 117, 125],
            [220, 53, 69], [25, 135, 84], [13, 110, 253], [255, 193, 7]
        ];

        categories.forEach((category, index) => {
            // 循环使用颜色数组
            const color = colors[index % colors.length];
            colorMap.set(category, color);
        });

        return colorMap;
    }

    // 处理分组变化
    eventHandlerGroup(selectedGroup) {
        this.group = selectedGroup;

        if (this.umapData === null) {
            $.ajax({
                url: "getNodeDataGL",
                type: "GET",
                dataType: "json",
                async: true,
                data: {id: this.id},
                success: (result) => {
                    this.umapData = result;
                    this.redraw("updateLabel");
                }
            });
        } else {
            this.redraw("updateLabel");
        }
    }
}
