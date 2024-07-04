package com.example.java.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * GrpcClient4
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
public class GrpcClient4 {

    /**
     * 用于存储响应消息
     */
    static List<HelloProto.HelloResponse> responses = new ArrayList<>();

    public static void main(String[] args) {
        //1.创建通信的管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .build();
        try {
            //2.获得代理对象 stub
            HelloServiceGrpc.HelloServiceStub helloServiceStub = HelloServiceGrpc.newStub(managedChannel);
            //3.完成rpc调用
            StreamObserver<HelloProto.HelloRequest> helloRequestStreamObserver = helloServiceStub.cs2s(new StreamObserver<HelloProto.HelloResponse>() {
                @Override
                public void onNext(HelloProto.HelloResponse value) {
                    System.out.println("监听到服务端返回：" + value.getResult());
                }

                @Override
                public void onError(Throwable t) {
                }


                @Override
                public void onCompleted() {
                    System.out.println("服务端响应完毕");
                }

            });

            for (int i = 0; i < 10; i++) {
                HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
                builder.setName("Hello,world" + i);
                HelloProto.HelloRequest helloRequest = builder.build();
                //发送消息
                helloRequestStreamObserver.onNext(helloRequest);
                //睡眠
                Thread.sleep(1000);
            }
            //发送结束
            helloRequestStreamObserver.onCompleted();
            managedChannel.awaitTermination(60, TimeUnit.SECONDS);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
    }

}
