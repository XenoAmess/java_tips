# java tips

太多人说过的就不提了

## jdk篇

1. 慎用Path.toFile

在部分版本的jdk（主要是早期版本的11）上，Path.toFile函数对包含于utf8字符目录的文件会报错。这是jdk的bug。如无必要，对于文件路径中可能出现中文的文件，请不要使用这个函数。

2. 输入输出stream转reader包buffer时要包在最贴近使用的一侧

使用

`Reader reader = new BufferedReader(new InputStreamReader(inputStream))`
`Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream));`

而不是

`Reader reader = new InputStreamReader(new BufferedInputStream(inputStream))`
`Writer writer = new OutputStreamWriter(new BufferedOutputStream(outputStream));`

https://stackoverflow.com/questions/61500726/how-to-convert-inputstream-to-reader-in-java

## 常识篇

1. 抵制低质量组件

如无必要，不要在运行时使用任何阿里开源/闭源组件，因为绝大多数阿里开源项目代码质量低劣，且维护人员严重缺乏常识。

但是，如果是工具类的，而不是直接污染运行时的，则可以选择性使用。

举例，负面典型druid，正面典型arthas。

## jackson篇

1. Reader/InputStream不要包buffer

在使用jackson从Reader/InputStream解析的时候，不要特意包一层buffer，因为Jackson在处理过程中自己有一套buffer机制。

见com.fasterxml.jackson.core.json.UTF8StreamJsonParser及com.fasterxml.jackson.core.json.ReaderBasedJsonParser

## lombok篇

1. JsonProperty要手动同步到@Getter和@Setter

在@lombok.Data标识的类的field上，如果想要指定com.fasterxml.jackson.annotation.JsonProperty，那么请手动同步创建@Getter和@Setter，并写onMethod

见https://github.com/projectlombok/lombok/pull/2915
