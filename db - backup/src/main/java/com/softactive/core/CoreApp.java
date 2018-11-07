package com.softactive.core;

import java.util.Calendar;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.softactive.updater.common.bean.RequesterBean;
@SpringBootApplication
@ComponentScan(basePackages="com.softactive")
public class CoreApp 
implements CommandLineRunner
{
	@Autowired
	private RequesterBean requester;


	private static final String UPDATE = "u";

	public static void main(String[] args) throws Exception {
		SpringApplication.run(CoreApp.class, args);
	}

	public static void test(String[] args) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		String date = year + "-" + month + "-" + day;
		System.out.println(date);

		String s = "Hello World's first baby!";
		System.out.println(escapeChars(s));
	}

	private static String escapeChars(String original) {
		return original.replaceAll("'", "\\\\'");
	}
	
	@Override
	public void run(String... args) throws Exception {
		if(args==null || args.length==0) {
			askMainCommand();
		} else if (args.length == 1){
			handleMainCommand(args[0]);
		} else {
			System.out.print("undefined command:");
			for(String s:args) {
				System.out.print(" -" + s);
			}
		}
	}

	public void askMainCommand() {
		System.out.println("enter u for update");
		Scanner sc = new Scanner(System.in);
		String command = sc.nextLine();
		sc.close();
		handleMainCommand(command);
	}
	
	public void handleMainCommand(String s) {
		switch(s) {
		case UPDATE:
			requester.update();
			break;
		default:
			System.out.println("undefined command -"+s);
			askMainCommand();
			break;
		}
	}
}
