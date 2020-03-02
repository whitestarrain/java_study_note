package _0_old.net;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServerDemo {

    public static void main(String[] args) throws Exception {

        while (true) {

            ServerSocket ss = new ServerSocket(11000);
            Socket s = ss.accept();
            System.out.println(s.getInetAddress().getHostAddress());
            InputStream in = s.getInputStream();
            byte[] buf = new byte[1024];
            int len = in.read(buf);
            System.out.println(new String(buf, 0, len));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            StringBuffer result = new StringBuffer();
            result.append("HTTP/1.1 200 OK\n");
            result.append("Content-Type: text/html; charset=UTF-8\n\n");
            result.append("<html><head><title>test</title></head><body><font color='red'size='7'>客户端你好</font></body></html>");
            out.println(result.toString());
            s.close();
            ss.close();
        }
    }
}

// GET / HTTP/1.1
// Host: 192.168.20.1:11000
// Connection: keep-alive
// Upgrade-Insecure-Requests: 1
// User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36
// (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36
// Accept:
// text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
// Accept-Encoding: gzip, deflate
// Accept-Language: zh-CN,zh;q=0.9,ja;q=0.8
