package task;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;


public class TalkClient extends MFrame implements ActionListener, Runnable {

	Button saveBtn, sendBtn;
	TextField sendTf;
	TextArea ta;
	Panel p1, p2;
	BufferedReader in;
	PrintWriter out;
	String id;
	String title = "Talk 1.0";

	public TalkClient(BufferedReader in, PrintWriter out, String id) {
		super(350, 400);
		this.in = in;
		this.out = out;
		this.id = id;
		setTitle(title + " - " + id + "님 반갑습니다.");
		p1 = new Panel();
		p1.setBackground(new Color(200, 100, 200));
		p1.add(saveBtn = new Button("SAVE"));

		p2 = new Panel();
		p2.setBackground(new Color(200, 100, 200));
		p2.add(new Label("CHAT ", Label.CENTER));
		p2.add(sendTf = new TextField("", 25));
		p2.add(sendBtn = new Button("SEND"));

		sendTf.addActionListener(this);
		saveBtn.addActionListener(this);
		sendBtn.addActionListener(this);

		add(p1, BorderLayout.NORTH);
		add(ta = new TextArea());
		add(p2, BorderLayout.SOUTH);
		new Thread(this).start();
		validate();// 갱신
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == saveBtn/* save */) {
			String content = ta.getText();
			long fileName = System.currentTimeMillis();
			try {
				FileWriter fw = new FileWriter("task/" + fileName + ".txt");
				fw.write(content);
				fw.close();
				ta.setText("");
				new MDialog(this, "Save", "대화내용을 저장하였습니다.");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else if (obj == sendTf || obj == sendBtn) {
			String str = sendTf.getText();
			if (filterMgr(str)) {
				new MDialog(this, "경고", "입력하신 글짜는 금지어입니다.");
				return;
			}
			sendMessage(str);
			sendTf.setText("");
			sendTf.requestFocus();
		}
	}// actionPerformed

	public void sendMessage(String msg) {
		out.println(msg);
	}

	@Override
	public void run() {
		try {
			while (true) {
				ta.append(in.readLine() + "\n");
				sendTf.requestFocus();
			}
		} catch (Exception e) {
			System.err.println("Error in run");
			e.printStackTrace();
			System.exit(1);
		}
	}// run

	boolean filterMgr(String msg) {
		boolean flag = false;
		String str[] = { "바보", "개새끼", "새끼", "자바", "java" };
		StringTokenizer st = new StringTokenizer(msg);
		String msgs[] = new String[st.countTokens()];
		for (int i = 0; i < msgs.length; i++) {
			msgs[i] = st.nextToken();
		}
		for (int i = 0; i < str.length; i++) {
			if (flag)
				break;
			for (int j = 0; j < msgs.length; j++) {
				if (str[i].equalsIgnoreCase(msgs[j])) {
					flag = true;
					break;
				} // if
			} // for2
		} // for1
		return flag;
	}

	class MDialog extends Dialog implements ActionListener {

		Button ok;
		TalkClient tc;

		public MDialog(TalkClient tc, String title, String msg) {
			super(tc, title, true);
			this.tc = tc;
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});
			setLayout(new GridLayout(2, 1));
			Label label = new Label(msg, Label.CENTER);
			add(label);
			add(ok = new Button("확인"));
			ok.addActionListener(this);
			layset();
			setVisible(true);
			validate();
		}

		public void layset() {
			int x = tc.getX();
			int y = tc.getY();
			int w = tc.getWidth();
			int h = tc.getHeight();
			int w1 = 150;
			int h1 = 100;
			setBounds(x + w / 2 - w1 / 2, y + h / 2 - h1 / 2, 200, 100);
		}

		public void actionPerformed(ActionEvent e) {
			sendTf.setText("");
			dispose();
		}
	}
}
