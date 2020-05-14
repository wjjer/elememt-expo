package vip.ablog.bean;

import vip.ablog.excel.annotation.ExcelField;

/***
 * @author: jsp
 * @datetime: 2019/9/12 001222:54
 * @description: TODO
 * @version :1.0
 ***/
public class ElementBean {

    @ExcelField(value = "num",title="编码",align=80,sort=1)
    private String num;       //要素编号
    @ExcelField(value = "codeName",title="商品编码",align=80,sort=2)
    private String codeName;  //商品编码名
    @ExcelField(value = "code",title="编码值",align=80,sort=3)
    private String code;     //商品编码值
    @ExcelField(value = "boxNum",title="箱号",align=80,sort=4)
    private String boxNum;  //箱号

    private String proName;  //品名

    //实体类对应的实体加		title = 导出的列名	sort = 默认的样式
    @ExcelField(value = "name",title="要素名称",align=80,sort=2)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

}
