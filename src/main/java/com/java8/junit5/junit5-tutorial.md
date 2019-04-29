[IOC]

# junit5 指导

涉及到的注解

> * @BeforeAll
> * @BeforeEach
> * @AfterAll
> * @AfterEach
> * @DisplayName
> * @ParameterizedTest

被`@Test`、`@TestTemplate`、`@RepeatedTest`、`@BeforeAll`、`@AfterAll`、`@BeforeEach` 或 `@AfterEach` 注解标注的方法不可以有返回值。

## @BeforeAll

```java
@BeforeAll
static void setup() {
    log.info("@BeforeAll - 表示将在当前类中的所有测试方法之前执行带注释的方法（以前为@BeforeClass）");
}
```



##  @BeforeEach

```java
@BeforeEach
void init() {
    log.info("@BeforeEach - 表示在每个测试方法之前执行带注释的方法（之前为@Before）");
}
```



## @AfterAll

```java
@AfterAll
static void afterAll() {
    log.info("@AfterAll - 表示将在当前类中的所有测试方法之后执行带注释的方法（以前为@AfterClass）");
}
```



## @AfterEach

```java
@AfterEach
void afterEach() {
    log.info("@AfterEach - 表示在每个测试方法之后将执行带注释的方法（之前为@After）");
}
```



## @DisplayName

```java
// @DisplayName - 定义测试类或测试方法的自定义显示名称
@DisplayName("Single test successful")
@Test
void testSingleSuccessTest() {
    log.info("Success");
}
```



##  @Disabled

```java
@Test
// @Disable - 用于禁用测试类或方法（以前为@Ignore）
@Disabled("Not implemented yet")
void testShowSomething() {
}
```

