package com.example.java.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * GrpcClient
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
public class GrpcClient {

    public static void main(String[] args) {
        //1.创建通信的管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .build();
        try {
            //2.获得代理对象 stub
            HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
            //3.完成rpc调用
            //3.1 准备参数
            HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
            builder.setName("Hello,world");
            HelloProto.HelloRequest helloRequest = builder.build();
            //3.2 rpc请求
            HelloProto.HelloResponse helloResponse = helloServiceBlockingStub.hello(helloRequest);
            //3.3 获取返回值
            String result = helloResponse.getResult();
            System.out.println("rpc响应内容==》" + result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
    }

}
