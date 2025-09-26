package com.mysite.sbb;

/*
import lombok.Getter;
import lombok.Setter;

@Getter // Getter, Setter 메서드 따로 작성하지 않아도 관련 메서드 사용할 수 있음
@Setter
public class HelloLombok {
	private String hello; // 클래스의 속성을 추가한다
	private int lombok;
	
	public static void main(String[] args) {
		HelloLombok helloLombok = new HelloLombok();
		helloLombok.setHello("헬로");
		helloLombok.setLombok(5);
		
		System.out.println(helloLombok.getHello());
		System.out.println(helloLombok.getLombok());
	}
}
*/

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HelloLombok{
	private final String hello;
	private final int lombok;
	
	public static void main(String[] args) {
		HelloLombok helloLombok = new HelloLombok("헬로",5);
		System.out.println(helloLombok.getHello());
		System.out.println(helloLombok.getLombok());
	}
}