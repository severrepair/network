package ch13;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;

public class FileWriterEx2 extends MFrame implements ActionListener{
	
	TextArea ta;
	TextField tf;
	Button save;
		
	
	public FileWriterEx2() {
		
		super(320, 400);
		setTitle("FileWriter");
		add(ta=new TextArea());
		Panel p = new Panel();
		p.add(tf = new TextField(30));
		p.add(save = new Button("SAVE"));
		ta.setEditable(false);
		tf.addActionListener(this);
		save.addActionListener(this);
		add(p,BorderLayout.SOUTH);
		validate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();//이벤트를 발생시킨 객체를 리턴
		if(obj==tf) {
//			System.out.println("tf");
			ta.append(tf.getText()+"\n");
			tf.setText("");
			tf.requestFocus();
		}else if(obj==save) {
//			System.out.println("save");
			saveFile(ta.getText());
			ta.setText("");
			try {
				for(int i=5;i>0;i--) {
					ta.setText("저장 하였습니다.-" + i + "초후에 사라집니다.");
					Thread.sleep(1000);//1초
				}
			}catch (Exception e2) {
				e2.printStackTrace();
			}
			ta.setText("");
		}
	}
	
	public void saveFile(String str) {
		try {
			long name = System.currentTimeMillis();
			FileWriter fw = new FileWriter("ch13/"+name+".txt");
			fw.write(str);
			fw.flush();
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args) {
		new FileWriterEx2();
	}

}
