# AI议会系统

这是一个通过调用本地ollama部署的deepseek、通义千问、豆包大模型的Java应用。

## 功能

1. 用户输入问题
2. 问题分发给ollama部署的三个模型
3. 每个模型生成一份20字以内的简报
4. 将简报和用户问题一同交给三个模型投票选举出最合适的简报
5. 选出的模型根据用户问题和简报生成最终答复

## 系统要求

- Java 17+
- 本地运行ollama服务(默认端口11434)
- ollama已安装deepseek、qwen、doubao模型

## 使用方法

1. 确保ollama服务正在运行
2. 编译项目：`mvn clean package`
3. 运行程序：`java -jar target/ai-parliament-1.0-SNAPSHOT.jar`
4. 按照提示输入问题

## 模型配置

如果需要修改使用的模型，请编辑`src/main/java/com/aides/AIParliament.java`文件中的`MODELS`列表。