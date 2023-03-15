import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Collections;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

// 사실은 서버 코드임


public class MultiServer {
    public static void main(String[] args) {
        MultiServer multiServer = new MultiServer();
        multiServer.start();

    }
    private void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(8000);
            while(true) {
                System.out.println("[클라이언트 연결 대기 중...]");
                socket = serverSocket.accept();
                ReceiveThread receiveThread = new ReceiveThread(socket);
                receiveThread.start();

            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(serverSocket != null) {
                try {
                    serverSocket.close();
                    System.out.println("[서버종료]");
                } catch(IOException e) {
                    e.printStackTrace();
                    System.out.println("[서버소켓통신에러]");
                }
            }
        }
    }

}

class ReceiveThread extends Thread {
    static List<PrintWriter> list =
            Collections.synchronizedList(new ArrayList<PrintWriter>());
    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;

    public ReceiveThread(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            list.add(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ClientNum() {
        int ClientNum = 0;

        if(조건식에 뭘 써야할까) {
            ClientNum++;
            sendAll("[ 현재 참여 인원 : " + ClientNum + "]");
        }
        else {
            ClientNum--;
            sendAll("[ 현재 참여 인원 : " + ClientNum + "]");
        }
    }

    public void run() {
        String name = " ";

        try {
            name = in.readLine();
            System.out.println("[" + name + " 새연결 생성]");
            sendAll("[" + name + "]님이 들어오셨습니다.");

            while (in != null) {

                String inputMsg = in.readLine();
                if("exit".equals(inputMsg)) break;
                sendAll(name + " >>> " + inputMsg);
            }
        } catch (IOException e) {
            System.out.println("[" + name + " 접속 끊김]");

        } finally {
            sendAll("[" + name + "]님이 나가셨습니다.");
            list.remove(out);

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + name + " 연결종료 ]");
    }
    private void sendAll (String s) {
        for (PrintWriter out: list) {
            out.println(s);
            out.flush();
        }
    }

}