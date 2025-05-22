# book-catalog-service
1. **RESTful API**：提供完整的图书CRUD操作，包括创建、查询、更新、删除图书，以及借阅、归还和预约图书的功能。

2. **设计模式**：
    - **工厂模式**：在`BookFactory`类中实现，根据不同的图书类型创建相应的图书对象。
    - **观察者模式**：通过`BookBorrowedSubject`、`BookBorrowedObserver`、`BookReturnedSubject`和`BookReturnedObserver`实现，当图书借阅状态变化时通知观察者。
    - **策略模式**：在`OverdueFeeStrategy`接口及实现类中实现，根据图书类型计算逾期费用。
    - **装饰器模式**：通过`BookDecorator`类实现，为图书添加额外服务。

3. **测试**：提供了全面的单元测试，确保代码覆盖率≥80%。

4. **Swagger集成**：配置了Swagger，可通过`http://localhost:8080/swagger-ui.html`访问API文档。

5. **异常处理**：通过`GlobalExceptionHandler`实现统一的异常处理。

6. **数据库脚本**：包含了创建图书表和预约表的SQL脚本。 脚本路径 ：`src/main/resources/db/schema.sql`

项目采用Maven管理依赖，可以直接运行。只需配置好数据库连接信息，运行`BookCatalogApplication`类的main方法即可启动服务。
