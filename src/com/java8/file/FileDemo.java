package com.java8.file;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileDemo {

	/**
	 * <p>{@link Files#list(Path)}
	 * 返回一个延迟填充的Stream，其中的元素是目录中的条目。列表不是递归的。
	 * 流的元素是Path对象，它们就像通过解析dir的目录条目名称一样获得。某些文件系统维护到目录本身和目录的父目录的特殊链接。表示这些链接的条目不包括在内。
	 * 流是弱一致的。它是线程安全的，但在迭代时不会冻结目录，因此它可能（或可能不）反映从此方法返回后发生的目录的更新。
	 * 返回的流封装了DirectoryStream。如果需要及时处理文件系统资源，则应使用try-with-resources构造来确保在流操作完成后调用流的close方法。
	 * 在封闭流上操作的行为就像已到达流的末尾一样。由于预读，在流关闭之后可以返回一个或多个元素。
	 * 如果在返回此方法后访问目录时抛出IOException，则会将其包装在UncheckedIOException中，该异常将从导致访问发生的方法抛出。
	 *
	 * <p>{@link Paths#get(URI)} 此类仅包含通过转换路径字符串或URI返回Path的静态方法。
	 */
	@Test
	public void testListPath() {
		// Files.list()返回一个指定路径下的所有的目录，如指定C盘，则返回C盘下的所的目录，但是不会返回子目录（第二级目录）
		// 列出了当前工作目录的所有文件 Stream<Path> list = Files.list(Paths.get(""))
		try (Stream<Path> list = Files.list(Paths.get("C:/"))) {
			list.map(String::valueOf) // 把Path转换成String字符串
					.filter(path -> !path.contains("$")) // 一次函数
					.sorted()
					.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * <p>{@link Files#find(Path, int, BiPredicate, FileVisitOption...)}
	 * Path start:目录路径初始起点，
	 * int maxDepth:定义要搜索的最大文件夹深度(起始目录从0开始算),
	 * BiPredicate<Path, BasicFileAttributes> matcher: 匹配谓词并定义搜索逻辑
	 *
	 * <p>我们搜索所有java文件（文件名以.java结尾）。
	 */
	@Test
	public void testFindFileWithFind() {
		// 1. 指定当前的工作目录
		Path start = Paths.get("");
		// 2. 指定文件层次的深度
		int maxDepth = 5;
		try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) -> String.valueOf(path) // 指定搜索的逻辑算法
				.endsWith(".java"))) {
			stream.sorted()
					.map(String::valueOf)
					.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * <p>{@link Files#walk(Path, int, FileVisitOption...)}和
	 * {@link Files#find(Path, int, BiPredicate, FileVisitOption...)}
	 * 实现相同的行为。只不过walk方法不是传递搜索谓词(逻辑)，而是遍历所有的文件。
	 * 这个测试例子和{@link FileDemo#testFindFileWithFind()} 的结果是一样的
	 */
	@Test
	public void testFindFileWithWalk() {
		// 1. 指定搜索的起始目录
		Path start = Paths.get("");
		// 2. 指定目录搜索的深度
		int maxDepth = 5;
		try (Stream<Path> stream = Files.walk(start, maxDepth)) {
			stream.map(String::valueOf)
					.filter(path -> path.endsWith(".java"))
					.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * java8读取文件是简单的
	 * <p>{@link Files#readAllLines(Path)} 将给定文件的所有行读入字符串列表。
	 * 这个方法的内存效率不高，因为整个文件将被读入内存。文件越大，将使用的堆大小越多。
	 * <p>作为一种节省内存的替代方案，您可以使用该方法{@link Files#lines(Path)}。
	 * 此方法不是一次将所有行读入内存，而是通过功能流逐个读取和流式传输每一行。参考
	 * {@link FileDemo#testFileRead02()}
	 *
	 * @throws IOException
	 */
	@Test
	public void testFileRead() throws IOException {
		// 将给定文件的所有行读入字符串列表
		// 这个方法的内存效率不高，因为整个文件将被读入内存。文件越大，将使用的堆大小越多
		List<String> lines = Files.readAllLines(Paths.get("README.md"));
		lines.forEach(System.out::println);
		//
		try (Stream<String> stream = Files.lines(Paths.get("README.md"))) {
			stream
					.filter(line -> line.contains("print"))
					.map(String::trim)
					.forEach(System.out::println);
		}

	}

	/**
	 * 按行读取文件，效率比{@link FileDemo#testFileRead()}高
	 * <p>作为一种节省内存的替代方案，您可以使用该方法{@link Files#lines(Path)}。
	 * 此方法不是一次将所有行读入内存，而是通过功能流逐个读取和流式传输每一行。
	 * 但是这种方式的效率还不好，你可以构造一个缓冲读取器，参考{@link FileDemo#testFileRead03()}
	 */
	@Test
	public void testFileRead02() {
		try (Stream<String> stream = Files.lines(Paths.get("README.md"))) {
			stream.map(String::trim)
					.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 如果您需要更细粒度的控制，效率更高的话，您可以构建一个新的缓冲读取器
	 */
	@Test
	public void testFileRead03() {
		Path path = Paths.get("D:/Spring Boot.md");
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			// reader.readLine()只读取第一行
			reader.lines()
					.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写入文件，效率太低，我们可以编写一个写入缓冲器，参考{@link FileDemo#testFileWrite02()}
	 * 但是有一个问题，就是java在读取换行的时候，默认是把换行符给去掉了，所以我们在写入文件的时候要注意这个问题
	 * @throws IOException
	 */
	@Test
	public void testFileWrite() throws IOException {
		// 将给定文件的所有行读入字符串列表
		List<String> lines = Files.readAllLines(Paths.get("README.md"));
		lines.forEach(System.out::println);
		lines.add("这一行是我自己增加的，哈哈");
		Files.write(Paths.get("d:/readme-my.md"), lines);
		System.out.println("over");
	}

	/**
	 * 写入文件，构造一个缓冲编写器
	 * @throws IOException
	 */
	@Test
	public void testFileWrite02() {
		Path pathRead = Paths.get("README.md");
		Path pathWrite = Paths.get("d:/readme-my.md");
		try (BufferedReader reader = Files.newBufferedReader(pathRead);
			 BufferedWriter writer = Files.newBufferedWriter(pathWrite)) {
			// reader.readLine()只读取第一行
			reader.lines().forEach(s -> {
				try {
					writer.write(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			writer.write("这是我自己写入的");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("over");
	}


}
