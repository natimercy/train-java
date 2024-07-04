package com.example.java.grpc;

import io.grpc.stub.StreamObserver;

/**
 * HelloServiceImpl
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    /**
     * grpc 的返回值并不是通过Java中的返回值来返回。而是通过观察者设计模式通过参数返回的，后续会有详细的解释
     * 1. 接收客户端提交的参数
     * 2. 业务处理
     * 3. 返回处理结果
     *
     * @param request          req
     * @param responseObserver 响应观察者
     */
    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        String name = request.getName();
        System.out.println("service name===>" + name);
        HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
        builder.setResult("hello method invoke ok, request parameters: " + name);
        HelloProto.HelloResponse helloResponse = builder.build();
        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void c2ss(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        String name = request.getName();
        System.out.println("service name===>" + name);
        for (int i = 0; i < 10; i++) {
            try {
                HelloProto.HelloResponse helloResponse = HelloProto.HelloResponse.newBuilder()
                        .setResult("处理结果 ===》" + i)
                        .build();
                responseObserver.onNext(helloResponse);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloProto.HelloRequest> cs2s(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            @Override
            public void onNext(HelloProto.HelloRequest value) {
                System.out.println("接收到客户端消息：" + value.getName());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("异常消息：" + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("客户端消息全部接收成功");
                HelloProto.HelloResponse helloResponse = HelloProto.HelloResponse.newBuilder()
                        .setResult("all message is ok")
                        .build();
                responseObserver.onNext(helloResponse);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<HelloProto.HelloRequest> cs2ss(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            @Override
            public void onNext(HelloProto.HelloRequest value) {
                System.out.println("接收到客户端消息==：" + value.getName());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("异常消息：" + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("客户端消息全部接收成功==");
                HelloProto.HelloResponse helloResponse = HelloProto.HelloResponse.newBuilder()
                        .setResult("all message is ok")
                        .build();
                responseObserver.onNext(helloResponse);
                responseObserver.onCompleted();
            }
        };

    }
}
