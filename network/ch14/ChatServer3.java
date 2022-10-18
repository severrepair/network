package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer3 {
	
	ServerSocket server;
	int port = 8004;
	Vector<ClientThread3> vc;
	ChatMgr3 mgr;
	
	public ChatServer3() {
		try {
			server = new ServerSocket(port);
			vc = new Vector<ClientThread3>();
			mgr = new ChatMgr3();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in Server");
			System.exit(1);//���������� ����
		}
		System.out.println("****************************");
		System.out.println("Welcome Server 3.0...");
		System.out.println("Ŭ���̾�Ʈ�� ������ ��ٸ��� �ֽ��ϴ�.");
		System.out.println("****************************");
		try {
			while(true) {
				Socket sock = server.accept();
				ClientThread3 ct = new ClientThread3(sock);
				ct.start();//������ �����ٷ��� ��� -> run �޼ҵ� ȣ��
				vc.addElement(ct);//ClientThread ��ü�� ���Ϳ� ����
			}
		} catch (Exception e) {
			System.err.println("Error in sock");
			e.printStackTrace();
		}
	}
	
	//��� ���ӵ� Client���� �޼��� ����
	public void sendAllMessage(String msg) {
		for (int i = 0; i < vc.size(); i++) {
			//Vector�� ����� ClientThread�� ���������� ������
			ClientThread3 ct = vc.get(i);
			//ClientThread ������ �ִ� ������ �޼��� ������ �޼ҵ� ȣ��
			ct.sendMessage(msg);
		}
	}
	
	//������ ������ ClientThread�� ���Ϳ��� ����
	public void removeClient(ClientThread3 ct) {
		vc.remove(ct);
	}
	
	//���ӵ� ��� id ����Ʈ ex)aaa;bbb;ccc;��ȣ��;
	public String getIds() {
		String ids = "";
		for (int i = 0; i < vc.size(); i++) {
			ClientThread3 ct = vc.get(i);
			ids+=ct.id+";";
		}
		return ids;
	}
	
	//������ ClientThread2 �˻�
	public  ClientThread3 findClient(String id) {
		ClientThread3 ct = null;
		for (int i = 0; i < vc.size(); i++) {
			ct = vc.get(i);
			if(id.equals(ct.id)) {
				break;
			}//--if
		}//--for
		return ct;
	}
	
	class ClientThread3 extends Thread{
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id;
		
		public ClientThread3(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
				System.out.println(sock + " ���ӵ�...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					String line = in.readLine();
					if(line==null)
						break;
					else
						routine(line);
				}
				in.close();
				out.close();
				sock.close();
				removeClient(this);
			} catch (Exception e) {
				removeClient(this);
				System.err.println(sock + "[" + id +"] ������...");
			}
		}
		
		public  void routine(String line) {
			int idx = line.indexOf(ChatProtocol3.DM);//;
			String cmd = line.substring(0, idx);//CHATALL
			String data = line.substring(idx+1);//[aaa]������ �������Դϴ�.
			if(cmd.equals(ChatProtocol3.ID)) {
				idx = data.indexOf(';');//data -> aaa;1234
				cmd = data.substring(0, idx);//aaa
				data = data.substring(idx+1);//1234
				System.out.println(mgr.loginChk(cmd, data));
			}else if(cmd.equals(ChatProtocol3.CHAT)) {
				idx = data.indexOf(';');
				cmd = data.substring(0, idx);//bbb
				data = data.substring(idx+1);//data
				ClientThread3 ct = findClient(cmd);
				if(ct!=null) {//���� ������
					ct.sendMessage(ChatProtocol3.CHAT+ChatProtocol3.DM+"["
					+id+"(S)]" + data);//CHAT:[aaa(S)]�����
					sendMessage(ChatProtocol3.CHAT+ChatProtocol3.DM+"["
							+id+"(S)]" + data);//CHAT:[aaa(S)]�����
				}else {//bbb�� ������ �ȵ� ���
					sendMessage(ChatProtocol3.CHAT+ChatProtocol3.DM+"["+
							cmd+"]�� �����ڰ� �ƴմϴ�.");
				}
			}else if(cmd.equals(ChatProtocol3.MESSAGE)) {
				idx = data.indexOf(';');
				cmd = data.substring(0, idx);//bbb
				data = data.substring(idx+1);//data
				ClientThread3 ct = findClient(cmd);
				if(ct!=null) {//���� ������
					ct.sendMessage(ChatProtocol3.MESSAGE+ChatProtocol3.DM+
					id+";" + data);
				}else {//bbb�� ������ �ȵ� ���
					sendMessage(ChatProtocol3.CHAT+ChatProtocol3.DM+"["+
							cmd+"]�� �����ڰ� �ƴմϴ�.");
				}
			}else if(cmd.equals(ChatProtocol3.CHATALL)) {
				sendAllMessage(ChatProtocol3.CHATALL+ChatProtocol3.DM+"["+
						id+"]"+data);
			}
		}

		//Client���� �޼��� ���� �޼ҵ�
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		new ChatServer3();
	}

}





