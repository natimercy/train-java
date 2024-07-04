package com.example.java.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * GrpcServer
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        //1.设置端口
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(9000);
        //2.发布服务
        serverBuilder.addService(new HelloServiceImpl());
        //3.创建服务对象
        Server server = serverBuilder.build();
        System.out.println("grpc server start...");
        //4.启动服务器
        server.start();
        server.awaitTermination();
    }

}
