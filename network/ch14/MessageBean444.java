package ch14;

import lombok.Getter;
import lombok.Setter;

// Bean ���� �� lombok �̿��ϸ� getter setter ���ϰ� ���� �� ����

//@Data
@Getter @Setter
public class MessageBean4 {
	
	private int no;
	private String fid;
	private String tid;
	private String msg;
	private String mdate;
	
}
