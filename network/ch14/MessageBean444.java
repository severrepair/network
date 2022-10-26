package ch14;

import lombok.Getter;
import lombok.Setter;

// Bean 생성 시 lombok 이용하면 getter setter 편하게 만들 수 있음

//@Data
@Getter @Setter
public class MessageBean4 {
	
	private int no;
	private String fid;
	private String tid;
	private String msg;
	private String mdate;
	
}
