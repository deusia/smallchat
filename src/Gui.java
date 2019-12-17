import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Gui {

    private JFrame frame=new JFrame();                                         //创建窗口
    private JButton sendButton=new JButton("Send");                      //创建发送按钮
    private JTextField textField=new JTextField(50);                 //创建发送框
    //创建显示框
    private JTextArea textArea=new JTextArea("Hello,welcome!!! :)",25,58);
    private PrintWriter writer;                                               //创建流对象
    private Socket socket;                                                    //创建socket连接对象
    private BufferedReader messageReader;                                     //用于转化传回来的流
    private Thread readThread;                                                //创建读取信息线程，从服务器来的信息
    public static  void  main(String[] args){
        Gui gui=new Gui();
        gui.go();

    }

    //SmallChat启动的过程
    private void go(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //创建面板
        JPanel jPanel=new JPanel();
        //将组件添加到面板
        jPanel.add(BorderLayout.NORTH,textArea);
        jPanel.add(BorderLayout.SOUTH,textField);
        jPanel.add(BorderLayout.SOUTH,sendButton);

        //添加监听对象
        sendButton.addActionListener(new sendButtonListener());

        setNetWorking();


        //创建任务对象
        FeedBackInfo mission=new FeedBackInfo();
        //给线程分配任务
        readThread=new Thread(mission);
        readThread.setName("Deusia");
        //执行任务
        readThread.start();

        frame.getContentPane().add(jPanel);            //将面板添加到窗口
        frame.setSize(600, 500);        //设置窗口大小
        frame.setVisible(true);


    }

    private void setNetWorking(){
        try {
            //配置socket主机的host与端口
            socket=new Socket("192.168.0.103",3489);
            //从现有的OutputStream创建一个新的PrintWriter，而不需要自动线路刷新
            //用于把消息框中的信息输出
            writer=new PrintWriter(socket.getOutputStream());

            //用于接收从服务器传来的流，
            InputStreamReader messageString=new InputStreamReader(socket.getInputStream());
            //将消息转换成字符
            messageReader =new BufferedReader(messageString);

            System.out.println("connecting successfully !!!");

        }catch (Exception ex){ ex.printStackTrace(); }
    }

    //该线程方法用于从服务器取回回馈信息
    class FeedBackInfo implements Runnable{
        public void run(){                       //此方法必须被定义
            //接收到的消息
            String message;

            try {
                while ((message= messageReader.readLine())!=null){
                    textArea.append("\n"+message);
                    System.out.println("read:"+message);
                }
            }catch (Exception ex){ ex.printStackTrace(); }
        }
    }

    //sendButton的监听
    class sendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            try {
                //让流对象获得发送的信息给服务器
                String sendMessage;
                sendMessage=readThread.getName()+" : "+textField.getText();
                writer.println(sendMessage);
                writer.flush();

            }catch (Exception ex){

                ex.printStackTrace();
            }
            textField.setText("");
            textField.requestFocus();
        }
    }
}
