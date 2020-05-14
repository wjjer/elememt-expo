package vip.ablog.bean;

import vip.ablog.excel.annotation.ExcelField;

/***
 * @author: jsp
 * @datetime: 2019/9/15 001520:05
 * @description: TODO
 * @version :1.0
 * 商品名称及规格型号
 * 数量及单位
 * 重量及单位
 * 单价/总价/币制
 * 原产国(地区)
 * 最终目的国(地区)
 * 境内货源地
 * 征免
 * 箱号
 ***/
public class ElementImportBean {
    @ExcelField(value = "num",title="项号",align=80,sort=1)
    private String num;
    @ExcelField(value = "codeName",title="要素品名",align=80,sort=2,type = 1)
    private String codeName;
    @ExcelField(value = "code",title="商品编号",align=80,sort=3)
    private String code;
    private String[] elArrays;

    @ExcelField(value = "name", title = "商品名称及规格型号", align=80,sort=4)
    private String name;

    /*@ExcelField(value = "name",title="商品名称及规格型号",align=80,sort=3)
    private String name;
    @ExcelField(value = "sl",title="数量及单位",align=80,sort=4)
    private String sl;
    @ExcelField(value = "weight",title="重量及单位",align=80,sort=5)
    private String weight;
    @ExcelField(value = "price",title="单价/总价/币制",align=80,sort=6)
    private String price;
    @ExcelField(value = "sArea",title="原产国(地区)",align=80,sort=7)
    private String sArea;
    @ExcelField(value = "eArea",title="最终目的国(地区)",align=80,sort=8)
    private String eArea;
    @ExcelField(value = "sourceArea",title="境内货源地",align=80,sort=9)
    private String sourceArea;
    @ExcelField(value = "zm",title="征免",align=80,sort=10)
    private String zm;
    @ExcelField(value = "boxNum",title="箱号",align=80,sort=11)
    private String boxNum;*/


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getElArrays() {
        return elArrays;
    }

    public void setElArrays(String[] elArrays) {
        this.elArrays = elArrays;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
