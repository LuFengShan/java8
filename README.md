# java8 例子，学习，记录
> 部分测试引入JUnit5,请使用前引入

`java.util.function包`中的主要功能接口

| 功能接口名称   | 返回类型            | 功能接口说明                    |
| -------------- | ------------------- | ------------------------------- |
| Consumer       | Consumer< T >       | 接收T对象，不返回值             |
| Predicate      | Predicate< T >      | 接收T对象并返回boolean          |
| Function       | Function< T, R >    | 接收T对象，返回R对象            |
| Supplier       | Supplier< T >       | 提供T对象（例如工厂），不接收值 |
| UnaryOperator  | UnaryOperator< T >  | 接收T对象，返回T对象            |
| BiConsumer     | BiConsumer<T, U>    | 接收T对象和U对象，不返回值      |
| BiPredicate    | BiPredicate<T, U>   | 接收T对象和U对象，返回boolean   |
| BiFunction     | BiFunction<T, U, R> | 接收T对象和U对象，返回R对象     |
| BinaryOperator | BinaryOperator< T > | 接收两个T对象，返回T对象        |