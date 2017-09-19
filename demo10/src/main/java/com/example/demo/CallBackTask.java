package com.example.demo;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Component
public class CallBackTask {
	@Async
	public Future<String> doTaskOne() throws Exception {
		System.out.println("doTaskOne");
		return new AsyncResult<>("doTaskOne");
	}

	@Async
	public  Future<String> doTaskTwo() throws Exception {
		System.out.println("doTaskTwo");
		return new AsyncResult<>("doTaskTwo");
	}

	@Async
	public  Future<String> doTaskThree() throws Exception {
		System.out.println("doTaskThree");
		return new AsyncResult<>("doTaskThree");
	}
}