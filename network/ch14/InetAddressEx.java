package ch14;

import java.net.InetAddress;

public class InetAddressEx {

	public static void main(String[] args) {
		try {
			InetAddress add=InetAddress.getLocalHost();
			System.out.println("Host Name:" + add.getHostName());
			System.out.println("Host Address:" + add.getHostAddress());
			add=InetAddress.getByName("auction.co.kr");
			System.out.println("¿Á¼Ç Address:" + add.getHostAddress());
			InetAddress adds[]=InetAddress.getAllByName("naver.com");
			System.out.println("-------------");
			for(int i=0; i<adds.length; i++) {
				System.out.println("naver:"+adds[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
	}//main
}
