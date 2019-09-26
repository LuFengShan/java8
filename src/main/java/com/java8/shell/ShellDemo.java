package com.java8.shell;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

public class ShellDemo {
	@Test
	public void test() {
//		System.getProperties().entrySet()
//				.forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
		boolean isWindows = System.getProperty("os.name")
				.toLowerCase().startsWith("windows");
		ProcessBuilder builder = new ProcessBuilder();
		if (isWindows) {
			builder.command("cmd.exe", "/c", "dir");
		} else {
			builder.command("sh", "-c", "ls");
		}
		// builder.directory(new File(System.getProperty("user.home")));
		builder.directory(new File("./"));
		Process process = null;
		try {
			process = builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StreamGobbler streamGobbler =
				new StreamGobbler(process.getInputStream(), System.out::println);
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		int exitCode = 0;
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(exitCode);
	}
}
