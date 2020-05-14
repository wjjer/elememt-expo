package vip.ablog;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import vip.ablog.bean.ElementImportBean;
import vip.ablog.excel.ExportExcel;
import vip.ablog.excel.ImportExcel;
import vip.ablog.ui.inface.ConsoleInfoListener;
import vip.ablog.ui.inface.FileProcessListener;
import vip.ablog.utils.IntToSmallChineseNumber;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static String baseUrl = "http://hs.bianmachaxun.com/query/detail.php?word=";
    private static String word = "";   //bian ma
    private static StringBuilder builder = new StringBuilder();
    //private static LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
    private static LinkedList<ElementImportBean> dataList = new LinkedList<>();
    public static boolean startProcess(LinkedList<File> files, String outPath,
                                       FileProcessListener listener, ConsoleInfoListener consoleInfoListener) {

        //获取所有Excel Code\source
        //File file = new File("Code"+File.separator+"source");
        //String[] files = file.list();
        float currentSize = 0;
        float size = files.size();
        int index = 0;
        try {
            for ( int i=1;i<=files.size();i++) {
                final int[] count = {1};
                File file = files.get(i-1);
                currentSize++;
                double progress = (currentSize / size) * 100;
                String path = file.getPath();
                listener.updateProgress(index, (int) progress, file.getName());
                consoleInfoListener.printConsoleLog("当前进度：" + (int)progress + "%   " +
                        "正在处理的文件：" + path);
                index++;
                List<ElementImportBean> elementImportBeans = importDatas(path);
                elementImportBeans.forEach((data) -> {
                    try {
                        getData(count[0],data.getCode(),data.getName());
                        count[0]++;
                    } catch (IOException e) {
                        e.printStackTrace();
                        consoleInfoListener.printConsoleLog("获取数据异常：" + e.getMessage());
                    }
                });
                //进行文件拷贝，操作拷贝后的文件
                File outPathDir = new File(outPath);
                if (!outPathDir.exists()){
                    outPathDir.mkdirs();
                }
                String newFilePath = outPath+ File.separator+"【已处理】"+file.getName();
                //Files.copy(file.toPath(),new File(newFilePath).toPath());
                new ExportExcel(file.getName(),ElementImportBean.class)
                        .setIntellList(dataList).writeFile(newFilePath).dispose();

                dataList.clear();


            }

//            saveElToFile();
//            saveElToExcel();
            consoleInfoListener.printConsoleLog("数据处理完成，处理结果详见控制台信息！");
        } catch (Exception e) {
            consoleInfoListener.printConsoleLog("处理数据异常：" + e.getMessage());
            e.printStackTrace();
        }
        /*List<ElementBean> elList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ElementBean elementBean = new ElementBean();
            elementBean.setName("要素"+i);
            elList.add(elementBean);
        }
        try {
            new ExportExcel("标题", ElementBean.class).setDataList(elList).writeFile("生成要素.xlsx").dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            initWorkDir();
            String elCodeContent = getElCodeFromFile("Code\\code.txt");
            String[] codeArr = elCodeContent.split(",");
            for (String code : codeArr) {
                getData(code);
            }
            saveElToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return true;
    }


    public static void getData(int i,String code,String pm) throws IOException {
        Document doc = null;
        word = code;
        doc = Jsoup.connect(baseUrl + word).timeout(1000 * 60).get();
        //System.out.println(doc.html());
        Element table = doc.getElementsByClass("layui-tab-content").get(0);
        Element tr = table.getElementsByTag("tr").get(3);
        Element nameTr = table.getElementsByTag("tr").get(1);
        Element jgTr = table.getElementsByTag("tr").get(6);
        Element td = tr.getElementsByTag("td").get(1);
        Element nameTd = nameTr.getElementsByTag("td").get(1);
        Element jgTd = jgTr.getElementsByTag("a").get(0);
        //System.out.println(td.html());
        String name = nameTd.html();
        String jg = jgTd.html();
        String codeLineData = td.html();
        //dataMap.put(code, codeLineData);
        builder.append("编码：【" + code + "】" + " 品名：【" + name + "】" + " 要素：【" + codeLineData + "】" + " 监管：【" + jg + "】\r\n");
        //将数据放入待导出的集合中
        codeLineData = "商品编码,申报要素,"+StringUtils.substringBefore(codeLineData,"（以下要素仅上海海关要求）");

        String[] elNameArr = codeLineData.split(",");
        ElementImportBean bean = new ElementImportBean();
        bean.setCode(code);
        bean.setCodeName("商品编码");
        bean.setNum(IntToSmallChineseNumber.ToCH(i));
        bean.setElArrays(elNameArr);
        bean.setName(StringUtils.substringAfter(pm,"\n"));
        dataList.add(bean);
    }

    public void test(){
        String data = "申报要素；0.品牌类型；1.出口享惠情况；2.材质（钢铁）；3.种类（图钉、U形钉、平头钉、波纹钉等）（以下要素仅上海海关要求）4.GTIN；5.CAS";
        data = StringUtils.substringBefore(data,"（以下要素仅上海海关要求）");
        String[] elNameArr = data.split("；");
        for (int i = 0; i < elNameArr.length; i++) {
            System.out.println(elNameArr[i]);
        }
    }
    //init dir and file
    public static String initWorkDir() throws IOException {
        File dir = new File("Code");
        File codeFile = new File(dir.getPath() + "/" + "code.txt");
        File resultFile = new File(dir.getPath() + "/" + "result.txt");
        if (!dir.exists()) {
            dir.mkdir();
            codeFile.createNewFile();
            resultFile.createNewFile();
        } else {
            if (!codeFile.exists()) {
                codeFile.createNewFile();
            }
            if (!resultFile.exists()) {
                resultFile.createNewFile();
            }
        }
        return "";
    }

    //get element code from file
    public static String getElCodeFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();

        reader = new BufferedReader(new FileReader(file));
        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
            sbf.append(tempStr);
        }
        reader.close();
        return sbf.toString();
    }

    //save el info to file
    public static boolean saveElToFile() throws IOException {
        File file = new File("Code\\result.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(builder.toString());
        bw.close();
        System.out.println("Done");
        return true;
    }

    public static List<ElementImportBean> importDatas(String fileName) throws IOException, InvalidFormatException, IllegalAccessException, InstantiationException {
        List<ElementImportBean> newImpoElList = new ArrayList<>();
        ImportExcel importExcel = new ImportExcel(fileName, 17, 0);
        List<ElementImportBean> dataList = importExcel.getDataList(ElementImportBean.class);
        dataList.forEach((data) -> {
            if (StringUtils.isNotBlank(data.getNum())) {
                /*System.out.println(data.toString());*/
                newImpoElList.add(data);
            }
        });
        return newImpoElList;
    }


    //save el info to excel
    public static boolean saveElToExcel() throws IOException {
        File file = new File("Code\\result.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(builder.toString());
        bw.close();
        System.out.println("Done");
        return true;
    }


}
