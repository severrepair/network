package Talk1;

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

import Talk1.TalkClient.MDialog;
import ch14.MFrame;

public class TalkClient111 extends MFrame implements ActionListener, Runnable{
	
	Button btn1, btn2;
	TextField  tf2;
	TextArea ta;
	Panel p1, p2;
	BufferedReader in;
	PrintWriter out;
	int port = 8006;
	String id;
	String[] str1 = {"�ٺ�","������","����","�ڹ�","java"};
	
	
	public TalkClient111() {
		super(350,400);
		setTitle("Talk 1.0");
		p1 = new Panel();
		p1.setBackground(new Color(250,000,250));
	
		
		//p1.add(tf1 = new TextField("10.100.204.62",25));
		p1.add(btn1 = new Button("SAVE" ));
		
		p2 = new Panel();
		p2.setBackground(new Color(250,000,250));
		p2.add(new Label("CHAT ",Label.CENTER));
		p2.add(tf2 = new TextField("",25));
		p2.add(btn2 = new Button("SEND"));	
		
		
		tf2.addActionListener(this);
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		
		add(p1,BorderLayout.NORTH);
		add(ta=new TextArea());
		add(p2,BorderLayout.SOUTH);
		validate();//����
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==btn1/*save*/) {
			String content = ta.getText();
			//1970��1��1�� ~������� 1/1000�� ������ ���
			long fileName = System.currentTimeMillis();
			try {
				FileWriter fw = new FileWriter("ch14/"+fileName+".txt");
				fw.write(content);
				fw.close();
				ta.setText("");
//				new MDialog(this, "Save", "��ȭ������ �����Ͽ����ϴ�.");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}else if(obj==tf2||obj==btn2) {
			String str = tf2.getText().trim();
			if(str.length()==0)
				return;//�޼ҵ� ����
			//���͸� ���� ���
			if(filterStr(str)) {//������ ����
				ta.append("�Է��Ͻ� ���ڰ� ����� ���ԵǾ����ϴ�\n");
				tf2.setText(" ");
				tf2.requestFocus();
				return;
				
			}
			//���� ����� ���
			
			if(id==null){//���� ó������ ����
				id = str;
				setTitle(getTitle() + "[" + id + "]");
				ta.setText("ä���� �����մϴ�.\n");
			}
			//���͸� ����
//			String[] str1 = {"�ٺ�","������","����","�ڹ�","java"};
//			for(int i=0; i<str1.length; i++) {
//				if(str.contains(str1[i]))
//					
//				return;
			
			
//		}
			out.println(str);//������ �Է��� ���ڿ� ����.
			tf2.setText("");
			tf2.requestFocus();
		}
	
	}//--actionPerformed

	//������ ���͸�
	public boolean filterStr(String target) {
		boolean falg = false;
		for(int i=0; i<str1.length; i++) {
			if (target.contains(str1[i])) {
				falg= true;
				break;//������ ����
			}
		}
		return falg;
	}
	
	@Override
	//������ ���� �޼����� ������ �����ϴ� ���
	public void run() {
		try {
			while(true) {
				//�������� �޼��� ���޵Ǹ� ta�� append
				ta.append(in.readLine() + "\n");
				tf2.requestFocus();//ä�� �Է�â
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//--run
	

class MDialog extends Dialog implements ActionListener{
		
		Button ok;
		TalkClient ct2;
		
		public MDialog(TalkClient ct2,String title, String msg) {
			super(ct2, title, true);
			this.ct2 = ct2;
			 //////////////////////////////////////////////////////////////////////////////////////////
			   addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
			     dispose();
			    }
			   });
			   /////////////////////////////////////////////////////////////////////////////////////////
			   setLayout(new GridLayout(2,1));
			   Label label = new Label(msg, Label.CENTER);
			   add(label);
			   add(ok = new Button("Ȯ��"));
			   ok.addActionListener(this);
			   layset();
			   setVisible(true);
			   validate();
		}
		
		public void layset() {
			int x = ct2.getX();
			int y = ct2.getY();
			int w = ct2.getWidth();
			int h = ct2.getHeight();
			int w1 = 150;
			int h1 = 100;
			setBounds(x + w / 2 - w1 / 2, 
					y + h / 2 - h1 / 2, 200, 100);
		}

		public void actionPerformed(ActionEvent e) {
			tf2.setText("");
			dispose();
		}
	}
	
	
	
	public static void main(String[] args) {
		new TalkClient111();
	}
}






