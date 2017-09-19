package com.example.demo;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private Task task;
	
	@Autowired
	private CallBackTask callBackTask;

	@Test
	public void doTask() throws Exception {

		task.doTaskOne();
		task.doTaskTwo();
		task.doTaskThree();

	}
	
	@Test
	public void doCallBackTask() throws Exception {
		Future<String> taskOneFuture = callBackTask.doTaskOne();
		Future<String> taskTwoFuture = callBackTask.doTaskTwo();
		Future<String> taskThreeFuture = callBackTask.doTaskThree();
		
		while (true) {
			if (taskOneFuture.isDone() && taskTwoFuture.isDone() && taskThreeFuture.isDone()) {
				System.out.println("所有任务已经完成");
				break;
			}
		}
		System.out.println(taskOneFuture.get());
		System.out.println(taskTwoFuture.get());
		System.out.println(taskThreeFuture.get());
	}
}
