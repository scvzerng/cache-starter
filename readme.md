## cache starter

### redis 缓存拓展

- 提供注解式声明缓存的失效时间 `@Time`
- 使用`@EnableTimeCaching` 启用
- 提供 秒,分,时,天 四种时间类型选择
- 只对标注了 `Cacheable`和`CachePut`的注解生效
- 类级别 只对标注了 `RestController` `Component` `Service` `Repository` 的注解生效
- 使用fastjson序列化与反序列化
### 依赖

      <groupId>com.zero.scvzerng</groupId>
        <artifactId>cache-starter</artifactId>
        <version>{version}</version>

### example 
> 10秒后缓存过期
```
 @Cacheable( cacheNames = "names",key = "keys")
 @Time(value = 10)
 public returns method(params) 
```