package vip.ablog.ui.inface;

import org.apache.commons.lang3.StringUtils;

public interface ConsoleInfoListener {

    String printConsoleLog(String log);  //打印日志信息

}

class Test{
    public static void main(String[] args) {
        String a = "付款承诺函.pdf";
        a = StringUtils.substringBefore(a,".");
        System.out.println(a);
    }
}
