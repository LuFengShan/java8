package com.java8.suanfa;

import org.junit.jupiter.api.*;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@DisplayName("序列化二叉树")
public class XuLieHuaErChaShun {

    private final Logger log = LoggerFactory.getLogger(XuLieHuaErChaShun.class);

    static NodeDemo nodeDemoInit;

    @BeforeAll
    public static void before() {
        nodeDemoInit = NodeDemo.init();
    }

    @BeforeEach
    public void startTime() {
        log.info(() -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
    }

    @AfterEach
    public void endTime() {
        log.info(() -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
    }

    @DisplayName("打印数据实例")
    @Test
    public void printInit() {
        System.out.println(nodeDemoInit.toJson());
    }

    @DisplayName("前序遍历")
    @Test
    public void preOrder() {
        nodeDemoInit.preOrder();
    }

    @DisplayName("把数组按二叉树遍历")
    @Test
    public void testArrayToTree() {
        // 初始化一个整数数组
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        ArrBinaryTree arrBinaryTree = new ArrBinaryTree(arr);
        arrBinaryTree.preOrder(0);
        System.out.println();
        System.out.println("------------------------");
        arrBinaryTree.postOrder(0);
        System.out.println();
    }

    @DisplayName("线索化二叉树")
    @Test
    public void testArrayToTree2() {
    }


}
