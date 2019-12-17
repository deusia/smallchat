import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ArrayList<PrintWriter> clientOutputStreams = new ArrayList<>();

    public static void main(String[] args) {
        Server server=new Server();
        server.go();
    }

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        //处理客户端发来的消息
        private ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                //接收来自客户端的流
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                //将流进行转化
                reader = new BufferedReader(isReader);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                //将转化的字符赋给message
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
//                    将消息返回给任意一个客户端
                    tellEveryone(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


        private void go() {
            try {
                ServerSocket serverSock = new ServerSocket(6000);
                while (true) {
                    Socket clientSocket = serverSock.accept();
                    System.out.println(clientSocket.getInetAddress());
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                    clientOutputStreams.add(writer);

                    Thread t = new Thread(new ClientHandler(clientSocket));
                    t.start();
                    System.out.println("got a connection");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void tellEveryone(String message){
            for (Object clientOutputStream : clientOutputStreams) {
                try {
                    PrintWriter writer = (PrintWriter) clientOutputStream;
                    writer.println(message);
                    writer.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
