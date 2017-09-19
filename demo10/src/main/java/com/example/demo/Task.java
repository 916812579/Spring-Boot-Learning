package com.example.demo;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class Task {
	@Async
	public  void doTaskOne() throws Exception {
		System.out.println("doTaskOne");
	}

	@Async
	public  void doTaskTwo() throws Exception {
		System.out.println("doTaskTwo");
	}

	@Async
	public  void doTaskThree() throws Exception {
		System.out.println("doTaskThree");
	}
}