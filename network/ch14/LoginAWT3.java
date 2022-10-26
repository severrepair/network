package ch14;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class LoginAWT3 extends MFrame implements ActionListener {

	TextField idTf, pwTf;
	Label logo, idl, pwl, msgl;
	Button logBtn;
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	String id;
	String host = "127.0.0.1";
	int port = 8004;
	String title = "MyChat 3.0";
	String label[] = {"ID와 PWD를 입력하세요.",
						"ID와 PWD를 확인하세요.",
						"이중 접속입니다."};

	public LoginAWT3() {
		super(450, 400, new Color(100, 200, 100));
		setLayout(null);
		setTitle(title);
		logo = new Label(title);
		logo.setFont(new Font("Dialog", Font.BOLD, 50));

		idl = new Label("ID");
		pwl = new Label("PWD");
		idTf = new TextField("aaa");
		pwTf = new TextField("1234");
		logBtn = new Button("로그인");
		msgl = new Label(label[0]);
		logo.setBounds(100, 70, 270, 100);
		idl.setBounds(150, 200, 50, 20);
		idTf.setBounds(200, 200, 100, 20);
		pwl.setBounds(150, 230, 50, 20);
		pwTf.setBounds(200, 230, 100, 20);
		logBtn.setBounds(150, 260, 150, 40);
		msgl.setBounds(150, 320, 150, 40);
		logBtn.addActionListener(this);
		add(logo);
		add(idl);
		add(idTf);
		add(pwl);
		add(pwTf);
		add(logBtn);
		add(msgl);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//서버로 id,pwd 전송 2022-10-24
		try {
		if(obj==logBtn) {
			connect();
			id = idTf.getText().trim();
			out.println(ChatProtocol3.ID+ChatProtocol3.DM+id+";"
			+pwTf.getText().trim());
			String line = in.readLine();
			int idx = line.indexOf(ChatProtocol3.DM);
			String cmd = line.substring(0, idx);
			String data = line.substring(idx+1);
			if(cmd.equals(ChatProtocol3.ID)) {
				if(data.equals("F")) {//로그인 실패
					msgl.setForeground(Color.RED);
					msgl.setText(label[1]);//ID,pwd확인하세요.
				}else if(data.equals("C")) {//이중접속
					msgl.setForeground(Color.BLUE);
					msgl.setText(label[2]);//ID,pwd확인하세요.
				}else if(data.equals("T")) {//로그인 성공
					//dispose();//창이 사라짐.
					new ChatClient3(in, out, id);//client3 연결
				}
			}
			}
		}catch (Exception e2) {
			e2.printStackTrace();
		}
//		2022-10-24
	}
	
	public void connect() {
		try {
			if(sock==null) {
			sock = new Socket(host, port);
			in = new BufferedReader(
					new InputStreamReader(
							sock.getInputStream()));
			out = new PrintWriter(
					sock.getOutputStream(),true/*auto flush*/);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new LoginAWT3();
	}
}
