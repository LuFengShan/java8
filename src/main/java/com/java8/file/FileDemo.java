package com.java8.file;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用Java列出目录中的文件
 */
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
		// Files.list()返回一个指定路径下的所有的文件夹及文件，如指定C盘，则返回C盘下的所的目录，但是不会返回子目录（第二级目录）
		// 列出了当前工作目录的所有文件 Stream<Path> list = Files.list(Paths.get(""))
		try (Stream<Path> list = Files.list(Paths.get("C:\\Users\\Administrator\\IdeaProjects\\java8\\src"))) {
			list.map(String::valueOf) // 把Path转换成String字符串
					.filter(path -> ! path.contains("$")) // 一次函数
					.sorted()
					.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Java 7引入了一个更快的替代方案列出目录中的文件夹及文件
	 * {@link DirectoryStream}
	 *
	 * @return
	 * @throws IOException
	 */
	@Test
	public void listFilesUsingDirectoryStream() throws IOException {
		String dir = "C:\\Users\\Administrator\\IdeaProjects\\java8\\src";
		Set<String> fileList = new HashSet<>();
		List<String> list = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : stream) {
				if (Files.isDirectory(path)) { // 判断这个文件是不是一个文件夹，如果是文件夹就存入fileList
					fileList.add(path.getFileName()
							.toString());
				}
				list.add(path.getFileName().toString());
			}
		}
		fileList.forEach(System.out::println);
		System.out.println("///////////");
		list.forEach(System.out::println);
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
		Path start = Paths.get("C:\\Users\\Administrator\\IdeaProjects\\java8\\src");
		// 2. 指定文件层次的深度
		int maxDepth = 7;
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
		Path start = Paths.get("d:/github/java8");
		// 2. 指定目录搜索的深度
		int maxDepth = 7;
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
		try (Stream<String> stream = Files.lines(Paths.get("d:/车辆静态信息表.txt"))) {
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
		//Path path = Paths.get("D:/车辆静态信息表.txt");
		Path path = Paths.get("C:\\Users\\Administrator\\Desktop\\经纬度\\h2data\\foshan\\part-00000-8615bada-3dc8-4aca-8f82-d4408b897266.csv");
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			//只读取第一行
			//System.out.println(reader.readLine());
			reader.lines()
					.forEach(System.out::println);
			//System.out.println(reader.lines().count());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入文件，效率太低，我们可以编写一个写入缓冲器，参考{@link FileDemo#testFileWrite02()}
	 * 但是有一个问题，就是java在读取换行的时候，默认是把换行符给去掉了，所以我们在写入文件的时候要注意这个问题
	 *
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
	 *
	 * @throws IOException
	 */
	@Test
	public void testFileWrite02() {
		Path pathRead = Paths.get("d:/上海市2018-11.txt");
		Path pathWrite = Paths.get("d:/上海市2018-11-2.txt");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("over");
	}
	
	
	@Test
	public void testFileWrite_yk() {
		Path pathRead = Paths.get("d:/上海市2018-11.txt");
		Path pathWrite = Paths.get("d:/上海市2018-11-3.txt");
		try (BufferedReader reader = Files.newBufferedReader(pathRead);
		     BufferedWriter writer = Files.newBufferedWriter(pathWrite)) {
			// reader.readLine()只读取第一行
			List<String> collect = reader.lines().collect(Collectors.toList());
			writer.write("[");
			int size = collect.size();
			for (int i = 0; i < size; i++) {
				if (Objects.equals(i, size - 1)) {
					String str = collect.get(i);
					str = str.substring(0, str.length() - 1);
					writer.write(str);
				} else {
					writer.write(collect.get(i));
				}
			}
			writer.write("]");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBigFileReadAndWrite() {
		String filePath = "D:/车辆静态信息表";
		try (BufferedReader reader = Files.newBufferedReader(Paths.get("D:/车辆静态信息表.txt"))) {
			// 读取所有的文件
			List<String> stringList = reader.lines().collect(Collectors.toList());
			long line = 1;
			int i = 1;
			Path pathWrite = Paths.get(filePath + "（" + i + "）.csv");
			BufferedWriter writer = Files.newBufferedWriter(pathWrite);
			for (String str : stringList) {
				if (line > 200001) {
					writer.flush();
					writer.close();
					line = 2;
					i++;
					// 如果是拆分csv就加上以下四行，目的是增加第一行数据，使excel格式不乱
					pathWrite = Paths.get(filePath + "（" + i + "）.csv");
					writer = Files.newBufferedWriter(pathWrite);
					writer.write(stringList.get(0));
					writer.newLine();
				}
				writer.write(str);
				writer.newLine();
				line++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test23() {
		String str = "{15613},";
		String substring = str.substring(0, str.length() - 1);
		System.out.println(substring);
	}
	
	/**
	 * 这个女人真讨厌，专业给我找活
	 *
	 * @return
	 * @Author sgx
	 * @Param
	 * @Date 2020/1/15
	 * @version V1.1.0
	 **/
	@Test
	public void zhpy() {
		// 1. 指定当前的工作目录
		Path start = Paths.get("D:\\BaiduNetdiskDownload\\Python数据分析与大数据处理从入门到精通\\1.案例源码");
		// 2. 指定文件层次的深度
		int maxDepth = 7;
		try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) -> String.valueOf(path) // 指定搜索的逻辑算法
				.endsWith(".txt"))) {
			stream.sorted()
					.map(String::valueOf)
					.forEach(item -> {
						File file = Paths.get(item).toFile();
						// 新文件的名称
						item = item.replace(".txt", ".py");
						file.renameTo(new File(item));
						System.out.println(item);
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
