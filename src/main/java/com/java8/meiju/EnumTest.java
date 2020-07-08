package com.java8.meiju;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

public class EnumTest {
	
	@Test
	public void enumMapTest() {
		EnumMap<ColorEnum, String> enumMap = new EnumMap<>(ColorEnum.class);
		enumMap.put(ColorEnum.RED, "红色");
		enumMap.put(ColorEnum.GREEN, "绿色");
		enumMap.put(ColorEnum.BLANK, "白色");
		enumMap.put(ColorEnum.YELLOW, "黄色");
		System.out.println(ColorEnum.RED + ":" + enumMap.get(ColorEnum.RED));
	}
	
	@Test
	public void enumSetTest() {
		List<ColorEnum> list = new ArrayList<>();
		list.add(ColorEnum.RED);
		list.add(ColorEnum.RED);  // 重复元素
		list.add(ColorEnum.YELLOW);
		list.add(ColorEnum.GREEN);
		// 去掉重复数据
		EnumSet<ColorEnum> enumSet = EnumSet.copyOf(list);
		System.out.println("去重：" + enumSet);
		
		// 获取指定范围的枚举（获取所有的失败状态）
		EnumSet<ErrorCodeEnum> errorCodeEnums = EnumSet.range(ErrorCodeEnum.ERROR, ErrorCodeEnum.UNKNOWN_ERROR);
		System.out.println("所有失败状态：" + errorCodeEnums);
		errorCodeEnums = EnumSet.range(ErrorCodeEnum.SUCCESS, ErrorCodeEnum.UNKNOWN_ERROR);
		System.out.println("所有状态：" + errorCodeEnums);
	}
	
	// 枚举中增加方法
	@Test
	public void enumAddMethod() {
		ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.SUCCESS;
		System.out.println(errorCodeEnum.code());
		System.out.println(errorCodeEnum.msg());
	}
	
	// 覆盖枚举默认的方法
	@Test
	public void enumAddModifyMethod() {
		ColorEnum colorEnum = ColorEnum.YELLOW;
		System.out.println(colorEnum.toString());
	}
	
	/**
	 * 枚举实现接口
	 * 枚举不能继承类，因为默认已经继承了Enum类了
	 */
	@Test
	public void enumInterface() {
		ColorEnum colorEnum = ColorEnum.GREEN;
		colorEnum.print();
		System.out.println(colorEnum.getInfo());
	}
	
	/**
	 * 在接口中组织枚举
	 */
	@Test
	public void enumGroup() {
		ColorInterface colorInterface = ColorInterface.LiLiEnum.LILIA;
		System.out.println(colorInterface);
		colorInterface = ColorInterface.NewLiLiEnum.NEW_LILIA;
		System.out.println(colorInterface);
	}
	
	@Test
	public void single() {
		Singleton instance = Singleton.getInstance();
		Singleton instance1 = Singleton.getInstance();
		instance.sayHi();
		System.out.println(instance == instance1);
	}
	
}

interface ColorInterface {
	enum LiLiEnum implements ColorInterface {
		LILIA, LILIB, LILIC
	}
	
	enum NewLiLiEnum implements ColorInterface {
		NEW_LILIA, NEW_LILIB, NEW_LILIC
	}
}

interface aha {
	void print();
	
	String getInfo();
}

enum ColorEnum implements aha {
	RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLOW("黄色", 4);
	private String name;
	private int index;
	
	private ColorEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	@Override
	public String toString() {
		return name + ':' + index;
	}
	
	@Override
	public void print() {
		System.out.println(toString());
	}
	
	@Override
	public String getInfo() {
		return this.name;
	}
}

enum ErrorCodeEnum {
	SUCCESS(1000, "success"),
	ERROR(2001, "parameter error"),
	SYS_ERROR(2002, "system error"),
	NAMESPACE_NOT_FOUND(2003, "namespace not found"),
	NODE_NOT_EXIST(3002, "node not exist"),
	NODE_ALREADY_EXIST(3003, "node already exist"),
	UNKNOWN_ERROR(9999, "unknown error");
	
	private int code;
	private String msg;
	
	ErrorCodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int code() {
		return code;
	}
	
	public String msg() {
		return msg;
	}
	
	public static ErrorCodeEnum getErrorCode(int code) {
		for (ErrorCodeEnum item : ErrorCodeEnum.values()) {
			if (item.code == code) {
				return item;
			}
		}
		return UNKNOWN_ERROR;
	}
}

class Singleton {
	//  枚举类型是线程安全的，并且只会装载一次
	private enum SingletonEnum {
		INSTANCE;
		//  声明单例对象
		private final Singleton instance;
		
		//  实例化
		SingletonEnum() {
			instance = new Singleton();
		}
		
		private Singleton getInstance() {
			return instance;
		}
	}
	
	//  获取实例（单例对象）
	public static Singleton getInstance() {
		return SingletonEnum.INSTANCE.getInstance();
	}
	
	private Singleton() {
	}
	
	//  类方法
	public void sayHi() {
		System.out.println("Hi,Java.");
	}
}