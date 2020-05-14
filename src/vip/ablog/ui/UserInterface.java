package vip.ablog.ui;//记事本界面

import vip.ablog.Main;
import vip.ablog.ui.inface.FileProcessListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UserInterface extends JFrame {

    //配置信息
    private String outPath = "D:"+File.separator+"ElementOutPath"+File.separator+"Excel";

    // 工具条
    private JToolBar toolBar;
    // 菜单条组件
    private JMenuBar menuBar;
    private JMenu menu_file,menu_set,menu_start,menu_about;
    private JMenuItem menuItem_open,menuItem_exit,menuItem_outPath,menuItem_start,menuItem_about,menuItem_update;
    private JTextArea textArea_console;
    private  JList jList_file;
    private  JList jList_finishFile;
    private  JLabel label_prepare,label_end,label_outPath,label_console;
    //打开文件所在路径标签
    private JButton btn_openOutPath,btn_clearOutFiles,btn_unfold,btn_selectFiles,btnUpdate;
    private LinkedList<File> waitProcessFiles = new LinkedList<>();

    //接口
    FileProcessListener listener;
    private final JProgressBar bar;
    private static final String VERSION = "1.2.1";
    private final JPanel panel;


    public static void main(String[] args) {
        // TODO Auto-generated method stub

        new UserInterface();

    }

    public UserInterface() {
        btn_selectFiles = new JButton("选择文件");
        btn_unfold = new JButton("<html>更<br>多<br>功<br>能</html>");

        //标签
        label_outPath = new JLabel("文件的保存位置【 "+outPath+"】");
        // 创建工具条
        toolBar = new JToolBar();
        menuBar = new JMenuBar();
        menu_file = new JMenu("文件(F)");menu_file.setMnemonic('F');// 设置助记符
        menuItem_open = new JMenuItem("●打开");
        menuItem_exit = new JMenuItem("●退出");

        menu_start = new JMenu("开始(D)");menu_start.setMnemonic('D');
        menuItem_start = new JMenuItem("●开始处理");

        menu_set = new JMenu("设置(S)");menu_set.setMnemonic('S');
        menuItem_outPath = new JMenuItem("●输出路径");

        menu_about = new JMenu("关于(A)");menu_about.setMnemonic('A');
        menuItem_about = new JMenuItem("●关于软件");
        menuItem_update = new JMenuItem("●检查更新");
        menu_about.add(menuItem_about);
        menu_about.add(menuItem_update);


        // 将菜单添加到菜单栏上
        menu_file.add(menuItem_open);
        menu_file.addSeparator();// 添加分割线
        menu_file.add(menuItem_exit);
        menu_set.add(menuItem_outPath);
        menu_start.add(menuItem_start);

        // 将菜单添加到菜单条上
        menuBar.add(menu_file);
        menuBar.add(menu_set);
        menuBar.add(menu_start);
        menuBar.add(menu_about);

        // 将菜单添加到窗体上
        this.setJMenuBar(menuBar);

        // 将工具条添加到窗体
        menuBar.setBounds(7,7,800,50);
        this.add(toolBar);
        //设置Lable
        label_outPath.setBounds(10,10,260,30);
        btn_openOutPath = new JButton("打开目录");
        btn_clearOutFiles = new JButton("清空目录");
        btn_openOutPath.setBounds(270,17,90,18);
        btn_clearOutFiles.setBounds(365,17,90,18);
        this.add(label_outPath);
        this.add(btn_openOutPath);
        this.add(btn_clearOutFiles);

        //进度条
        bar = new JProgressBar();
        bar.setBounds(3, 580, 650, 20);
        bar.setOrientation(JProgressBar.HORIZONTAL);
        bar.setPreferredSize(new Dimension(300,20));
        bar.setMinimum(0);
        bar.setMaximum(100);
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setBackground(Color.white);
        bar.setBorderPainted(true);



        JScrollPane jspConsole = new JScrollPane();
        jspConsole.setBounds(10,380,642,180);
        textArea_console = new JTextArea();
        textArea_console.setBounds(10,380,642,180);
        jspConsole.setViewportView(textArea_console);
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(10,20,700,700);
        label_prepare = new JLabel("已选择的文件");
        label_end = new JLabel("处理完成的文件");
        label_console = new JLabel("控制台信息");
        jList_file = new JList();
        jList_finishFile = new JList();
        jList_file.setSize(300,300);
        jList_finishFile.setSize(300,300);
        JScrollPane jsp = new JScrollPane(jList_file);
        JScrollPane jsp_end = new JScrollPane(jList_finishFile);
        //摆在左上角
        label_prepare.setBounds(10, 25, 100, 30);
        btn_selectFiles.setBounds(115, 33, 95, 18);
        btn_unfold.setBounds(660, 320, 20, 100);
        btn_unfold.setMargin(new Insets(1,1,1,1));
        label_end.setBounds(350, 25, 300, 30);
        label_console.setBounds(10,360,100,20);
        jsp.setBounds(10, 55, 300, 300);
        jsp_end.setBounds(350, 55, 300, 300);
        panel.add(label_console);
        panel.add(bar);
        panel.add(label_prepare);
        panel.add(btn_selectFiles);
        panel.add(label_end);
        panel.add(jsp);
        panel.add(jsp_end);
        panel.add(jspConsole);

        panel.add(btn_unfold);
        this.add(panel);

        // 展示
        this.setTitle("要素查询工具 V1.2.1");
       // ImageIcon icon = new ImageIcon("images\\jsb.png");
        //this.setIconImage(icon.getImage());
        //this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(730,700));
        this.setVisible(true);
        //设置事件
        initListener();
    }

    private void initListener() {
        ActionListener selectFileListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //添加已选择的文件列表
                LinkedList<File> files = parseLeagleFiles(chooseFilesDialog());
                LinkedList<File> newFiles = new LinkedList<>();
                for (int i = 0; i < files.size(); i++) {
                    String fileName = files.get(i).getName();
                    if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                        newFiles.add(files.get(i));
                    }
                }
                String[] showFileName = new String[newFiles.size()];
                for (int i = 0; i < newFiles.size(); i++) {
                    showFileName[i] = newFiles.get(i).getName();
                    waitProcessFiles.add(newFiles.get(i));
                }
                jList_file.setListData(showFileName);
                //jList_finishFile.setListData(showFileName);
            }
        };
        menuItem_open.addActionListener(selectFileListener);
        btn_selectFiles.addActionListener(selectFileListener);
        menuItem_outPath.addActionListener(e -> {
            JFileChooser chooser=new JFileChooser();
            chooser.setMultiSelectionEnabled(false);//这里是可以多选
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//这里是选择选择信息的模式
            chooser.showDialog(chooser, "确认");//打开文件选择框
            File file=chooser.getSelectedFile();
            System.out.println(file);
            outPath = file.getPath();
            label_outPath.setText("△文件保存路径："+file.getPath());
        });
        menuItem_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String []finishFiles = new String[waitProcessFiles.size()];
                /*ExecutorService executorService= Executors.newSingleThreadExecutor();
                Future f1 = executorService.submit(new ProcessThread());
                try {
                    boolean result = (Boolean)f1.get();
                    System.out.println("result:"+result);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }finally {
                    executorService.shutdown();
                }
                System.out.println("开始执行任务");*/
                new Thread(() -> Main.startProcess(waitProcessFiles, outPath, (index, progres, fileName) -> {
                    finishFiles[index] = fileName;
                    //更新进度
                    bar.setValue(progres);
                    jList_finishFile.setListData(finishFiles);
                    //System.out.println("当前进度：" + progres + "%");
                    return progres;
                }, log -> {
                    String dataTime = new Date().toString();
                    textArea_console.setText(textArea_console.getText()+ dataTime+"："+log+"\r\n");
                    return null;
                })).start();
            }

        });
        btn_openOutPath.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File(outPath));
            } catch (IOException ex) {
                ex.printStackTrace();
                textArea_console.setText(textArea_console.getText()+ex.getMessage()+"\r\n");
            }
        });
        btn_clearOutFiles.addActionListener( e -> {
            File generatedFiles = new File(outPath);
            for (File file : generatedFiles.listFiles()) {
                if (file.exists()){
                    file.delete();
                }
            }
            textArea_console.setText(textArea_console.getText()+"历史生成文件清理成功！\r\n");
        });
        menuItem_about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //关于页面
                JFrame mainWindow;
                mainWindow = new JFrame("关于");
                JLabel label1 = new JLabel("作者：          Allan");
                label1.setBounds(10,10,450,20);
                JLabel label2 = new JLabel("github：https://github.com/wjjer/elememt-expo");
                label2.setBounds(10,40,450,20);
                JLabel label3 = new JLabel("数据源：        新通关网");
                label3.setBounds(10,70,450,20);
                JLabel label4 = new JLabel("说明：本软件仅为技术交流与学术用途，如有侵权请告知！");
                label4.setBounds(10,90,450,20);
                mainWindow.setVisible(true);
                mainWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                mainWindow.add(label1);
                mainWindow.add(label2);
                mainWindow.add(label3);
                mainWindow.add(label4);
                mainWindow.pack();
                mainWindow.setBounds(500,400,400,400);
            }
        });
        menuItem_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 创建一个URI实例
                    java.net.URI uri = java.net.URI.create("https://github.com/wjjer/elememt-expo/releases");
                    // 获取当前系统桌面扩展
                    java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                    // 判断系统桌面是否支持要执行的功能
                    if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                        // 获取系统默认浏览器打开链接
                        dp.browse(uri);
                    }else{
                        System.out.println("暂不支持");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //检查更新页面
               /* JFrame mainWindow;
                mainWindow = new JFrame("检查更新");
                JLabel label1 = new JLabel("版本号："+VERSION);
                btnUpdate = new JButton("检查更新");
                btnUpdate.setBounds(10,10,100,20);
                JLabel lbUpdateLog = new JLabel("进入release页即可下载最新版本！");
                lbUpdateLog.setBounds(10,90,450,20);
                mainWindow.setVisible(true);
                mainWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                mainWindow.add(label1);
                mainWindow.add(btnUpdate);
                mainWindow.add(lbUpdateLog);
                mainWindow.pack();
                mainWindow.setBounds(500,400,400,400);*/
            }
        });
        btn_unfold.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //展开功能

            }
        });



    }


    class ProcessThread {
     /*   @Override
        public Boolean call() throws Exception {
            //进行文件处理
            Main.startProcess(waitProcessFiles, "", new FileProcessListener() {
                @Override
                public int updateProgress(int index,int progress,String fileName) {
                    //更新进度
                    bar.setValue(progress);
                    System.out.println("当前进度："+progress+"%");
                    return progress;
                }
            });
            return true;
        }*/
    }
//-----------------------------------------JUI--------------------------------------------//

    /***
     * 选择文件夹或文件
     * @return
     */
    public static LinkedList<File> chooseFilesDialog(){
        LinkedList filesList = new LinkedList();
        JFileChooser chooser=new JFileChooser(new File(("G:")));
        //new File("G:")      文件打开的默认目录
        chooser.setMultiSelectionEnabled(true);//这里是可以多选
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//这里是选择选择信息的模式
        /*
        int mod；
        mod=JFileChooser.FILES_ONLY   ；只选择文件
        mod=JJFileChooser.DIRECTORIES_ONLY  ；只选择目录
        mod=JJFileChooser.FILES_AND_DIRECTORIES ；文件和目录
        */
        chooser.showDialog(chooser, "确认");//打开文件选择框
        File[] file=chooser.getSelectedFiles();
        for(File filePath:file){
            filesList.add(filePath);
        }
        return filesList;
    }

    /**
     * 从选择的文件中解析所有文件
     * @return
     */
    public static LinkedList<File> parseLeagleFiles(List<File> fileList){
        LinkedList<File> filesList = new LinkedList<>();
        fileList.forEach(file->{
            if (file.isDirectory()){
                File[] files = file.listFiles();
                filesList.addAll(parseLeagleFiles(Arrays.asList(files)));
            }else{
                filesList.add(file);
            }
        });
        return filesList;
    }
}



