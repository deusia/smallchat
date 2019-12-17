import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Second {

    private JFrame frame=new JFrame();                                         //��������
    private JButton sendButton=new JButton("Send");                       //�������Ͱ�ť
    private JTextField textField=new JTextField(50);                 //�������Ϳ�
    //������ʾ��
    private JTextArea textArea=new JTextArea("Hello,welcome!!! :)",25,58);
    private PrintWriter writer;                                               //����������
    private Socket socket;                                                    //����socket���Ӷ���
    private BufferedReader messageReader;                                     //����ת������������
    private Thread readThread;                                                //������ȡ��Ϣ�̣߳��ӷ�����������Ϣ

    public static  void  main(String[] args){
        Second second=new Second();
        second.go();
    }

    //SmallChat�����Ĺ���
    private void go(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //�������
        JPanel jPanel=new JPanel();
        //�������ӵ����
        jPanel.add(BorderLayout.NORTH,textArea);
        jPanel.add(BorderLayout.SOUTH,textField);
        jPanel.add(BorderLayout.SOUTH,sendButton);

        //��Ӽ�������
        sendButton.addActionListener(new sendButtonListener());

        setNetWorking();

        //�����������
        FeedBackInfo mission=new FeedBackInfo();
        //���̷߳�������
        readThread=new Thread(mission);
        readThread.setName("Jason");
        //ִ������
        readThread.start();

        frame.getContentPane().add(jPanel);            //�������ӵ�����
        frame.setSize(600, 500);        //���ô��ڴ�С
        frame.setVisible(true);


    }

    private void setNetWorking(){
        try {
            //����socket������host��˿�
            socket=new Socket("47.102.133.86",3489);
//            socket=new Socket("192.168.0.1",44562);
            //�����е�OutputStream����һ���µ�PrintWriter��������Ҫ�Զ���·ˢ��
            //���ڰ���Ϣ���е���Ϣ���
            writer=new PrintWriter(socket.getOutputStream());

            //���ڽ��մӷ���������������
            InputStreamReader messageString=new InputStreamReader(socket.getInputStream());
            //����Ϣת�����ַ�
            messageReader =new BufferedReader(messageString);

            System.out.println("connecting successfully !!!");

        }catch (Exception ex){ ex.printStackTrace(); }
    }

    //���̷߳������ڴӷ�����ȡ�ػ�����Ϣ
    class FeedBackInfo implements Runnable{
        public void run(){                       //�˷������뱻����
            //���յ�����Ϣ
            String message;

            try {
                while ((message= messageReader.readLine())!=null){
                    textArea.append("\n"+message);
                    System.out.println("read:"+message);
                }
            }catch (Exception ex){ ex.printStackTrace(); }
        }
    }

    //sendButton�ļ���
    class sendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            try {
                //���������÷��͵���Ϣ��������
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
