package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// @Component
@Component
@ConfigurationProperties(prefix = "spring.boot.demo")
public class PropertiesDemo {

	private String name;

	private String version;
	private int port;
	private String url;
	private String value;
	private int number;
	private long bignumber;
	private int test1;
	private int test2;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public long getBignumber() {
		return bignumber;
	}

	public void setBignumber(long bignumber) {
		this.bignumber = bignumber;
	}

	public int getTest1() {
		return test1;
	}

	public void setTest1(int test1) {
		this.test1 = test1;
	}

	public int getTest2() {
		return test2;
	}

	public void setTest2(int test2) {
		this.test2 = test2;
	}

	@Override
	public String toString() {
		return "PropertiesDemo [name=" + name + ", version=" + version + ", port=" + port + ", url=" + url + ", value="
				+ value + ", number=" + number + ", bignumber=" + bignumber + ", test1=" + test1 + ", test2=" + test2
				+ "]";
	}

	
}
