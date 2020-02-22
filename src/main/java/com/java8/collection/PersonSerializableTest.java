package com.java8.collection;

import jdk.internal.util.xml.impl.Input;
import org.junit.jupiter.api.Test;

import java.io.*;

public class PersonSerializableTest {
	
	@Test
	public void test01() throws IOException, ClassNotFoundException {
		Person person = new Person();
		person.setFirstName("s");
		person.setLastName("gx");
		person.setAge(22);
		
		OutputStream op = new FileOutputStream("a.txt");
		ObjectOutput ops = new ObjectOutputStream(op);
		ops.writeObject(person);
		ops.flush();
		ops.close();
		
		InputStream is = new FileInputStream("a.txt");
		ObjectInput ois = new ObjectInputStream(is);
		Person p = (Person) ois.readObject();
		System.out.println(p);
		ois.close();
	}
}
