FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app

VOLUME /data

COPY ./target/mini1.jar /app/target/mini1.jar
COPY ./src/main/java/com/example/data /data

ENV spring.data.userDataPath=/data/users.json
ENV spring.data.productDataPath=/data/products.json
ENV spring.data.orderDataPath=/data/orders.json
ENV spring.data.cartDataPath=/data/carts.json

EXPOSE 8080

CMD ["java", "-jar", "/app/target/mini1.jar"]