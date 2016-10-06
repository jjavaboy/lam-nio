#当前开发系统是在win10下，jdk1.7

#使用protobuf生成java文件说明

假设开发机器已经下载好protobuf，protobuf目录（假设为%PROTOBUF_HOME%）会有
可执行文件:protoc.exe

在%PROTOBUF_HOME%目录下创建目录
proto_file，用来存放proto文件，编辑好SubscribeResp.proto， SubscribeReq.proto放在该目录；
src，用来存放生成的java文件

在cmd命令模式下，cd到%PROTOBUF_HOME%目录下，执行以下命令:

protoc --java_out=./src ./proto_file/SubscribeResp.proto
#--java_out=./src，表示生成java文件，生成的源码存放在当前目录%PROTOBUF_HOME%的src目录下
#./proto_file/SubscribeResp.proto，表示proto文件是存入在当前目录%PROTOBUF_HOME%的proto_file目录下

protoc --java_out=./src ./proto_file/SubscribeReq.proto