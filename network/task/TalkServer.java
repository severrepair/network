package task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TalkServer {

	ServerSocket server;
	Vector<TalkThread> vc;
	int port = 8006;
	TalkMgr mgr;
	
	public TalkServer() {
		try {
			server = new ServerSocket(port);
			vc = new Vector<TalkThread>();
			mgr = new TalkMgr();
		} catch (Exception e) {
			System.err.println("Error in Server");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("****************************");
		System.out.println("Welcome Talk Server");
		System.out.println("Ŭ���̾�Ʈ�� ������ ��ٸ��� �ֽ��ϴ�.");
		System.out.println("****************************");
		try {
			while(true) {
				Socket sock = server.accept();//Client ���� ��� ����
				TalkThread ct = new TalkThread(sock);
				ct.start();//������ �����ٷ����� ��� -> �����ٷ� ����
				vc.addElement(ct);//���Ϳ� �߰�
			}
		} catch (Exception e) {
			System.err.println("Error in Socket");
			e.printStackTrace();
		}
	}
	
	public void sendAllMessage(String msg) {
		for (int i = 0; i < vc.size(); i++) {
			TalkThread ct = vc.get(i);
			ct.sendMessage(msg);
		}
	}
	
	public void removeClient(TalkThread ct) {
		vc.remove(ct);
	}
	
	class TalkThread extends Thread{
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id = "�͸�";
		
		public TalkThread(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(
								sock.getInputStream()));
				out = new PrintWriter(
						sock.getOutputStream(),true/*auto flush*/);
				System.out.println(sock + " ���ӵ�...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				String data ="";
				while(true) {
					data = in.readLine();
					int idx = data.indexOf(';');
					String id = data.substring(0, idx);
					String pwd = data.substring(idx+1);
					if(mgr.loginChk(id, pwd)) {
						sendMessage("T");
						this.id = id;
						sendAllMessage("["+id+"]���� �����Ͽ����ϴ�.");
						break;
					}else {
						sendMessage("F");
					}
				}
				while(true) {
					data = in.readLine();//�޼��� ���ö����� ������
					if(data==null)
						break;
					sendAllMessage("["+id+"]"+data);
				}
				in.close();
				out.close();
				sock.close();
			} catch (Exception e) {
				removeClient(this);
				System.err.println(sock+" ������...");
			}
		}
		
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		new TalkServer();
	}
}






