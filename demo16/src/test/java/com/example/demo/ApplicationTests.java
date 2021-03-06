package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTests {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CacheManager cacheManager;
	@Test
	public void test() throws Exception {
		userRepository.save(new User("AAA", 10));
		userRepository.findByName("AAA");
		userRepository.findByName("AAA");
		userRepository.findByName("AAA");
		User user1 = userRepository.findByName("AAA");
		System.out.println(user1.getName());
		System.out.println(user1.getAge());
		user1.setAge(222);
		userRepository.save(user1);
		user1 = userRepository.findByName("AAA");
		System.out.println(user1.getName());
		System.out.println(user1.getAge());
	}
}
