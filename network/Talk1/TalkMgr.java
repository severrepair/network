package Talk1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TalkMgr {
	
	private DBConnectionMgr pool;
	
	public TalkMgr() {
		pool=DBConnectionMgr.getInstance();
	}
	
	//�α���
	public boolean loginChk(String id, String pwd) {
		Connection con=null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql=null;
		boolean flag=false;
		try {
			con = pool.getConnection();//Connection ������
			sql="select id from tblRegister where id=? and pwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);//ù���� ?�� id = 'aaa'
			pstmt.setString(2, pwd);//�ι��� ?�� pwd = '1234'
			rs=pstmt.executeQuery();//���๮
			flag=rs.next();//���ǿ� �´� ������� ������ true ������ false
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//con�� �ݳ�, pstmt, rs �Ѵ� close
			pool.freeConnection(con,pstmt,rs);
		}
		return flag;
	}
}






