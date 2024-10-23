# douban-api



## 使用 Quarkus 和 jsoup 封装的 豆瓣书籍和电影api


### 运行环境
#### Environment
- jdk 21
- quarkus 3.15.1
- jsoup 1.18.1
- gradle 8.9

### 目前已经抓取的接口有
#### Interfaces that have been implemented

- 书籍详细信息 (/book/{id})
- 电影详细信息 (/movie/{id})
- 图片代理 (/imageProxy/view/cover)
- 演员详细信息 (/movie/{id}/cast)
- 书籍搜索 (/book/search)
- 电影搜索 (/movie/search)
- 定时扫描本地文件夹并分类书籍
- 书籍原信息修改

### 准备实现的接口
#### Planned interfaces

- 缓存接入优化
- 搜索书籍
- xml和json格式的文件生成


### 快速开始

打包成包含依赖的jar包
Building Uber-Jars
```shell
quarkus build -Dquarkus.package.jar.type=uber-jar
```
```shell
./gradlew build -Dquarkus.package.jar.type=uber-jar
```


> **_提示:_**  Quarkus 现在附带了一个开发 UI，该 UI 仅在开发模式下可用，网址为 <http://localhost:8080/q/dev/>.
