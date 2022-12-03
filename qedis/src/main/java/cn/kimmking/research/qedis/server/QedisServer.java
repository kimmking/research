package cn.kimmking.research.qedis.server;

import cn.kimmking.research.qedis.QedisCache;
import cn.kimmking.research.qedis.QedisPlugin;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Qedis Server(refer camellia).
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 16:08
 */
@Component
@Order(100)
public class QedisServer implements QedisPlugin {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Channel serverChannel;

    @Autowired
    QedisCache cache;

    @Override
    public void init() {
        // initialize core properties
    }

    @Override
    public void startup() {
        this.bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("qedis-boss"));
        this.workGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("qedis-work"));

        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)  //默认128? -> 512/1024
                .childOption(ChannelOption.SO_SNDBUF, 16384) //默认4096？ -> 16384
                .childOption(ChannelOption.SO_RCVBUF, 16384)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true) // 其实没啥用
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(16386, 16384 << 1)) // 默认 32K, 64K
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new QedisDecoder());
                        //p.addLast(new RedisEncoder());
                        //p.addLast(new QedisChannelHolder());
                        p.addLast(new QedisStringHandler(cache));
                        //p.addLast(new StringEncoder());
                    }
                });

        int port = 6379;
        try {
            ChannelFuture future = server.bind(port).sync();
            this.serverChannel = future.channel();
        } catch (Exception ex) {
            // todo process ex
            ex.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        shutdownServerChanel();
        shutdownGracefully();
    }

    private void shutdownServerChanel() {
        if(this.serverChannel != null) {
            this.serverChannel.close();
            this.serverChannel = null;
        }
    }

    private void shutdownGracefully() {
        if(this.bossGroup != null) {
            this.bossGroup.shutdownGracefully(); // period 2s,timeout 15s
            this.bossGroup = null;
        }
        if(this.workGroup != null) {
            this.workGroup.shutdownGracefully(); // period 2s,timeout 15s
            this.workGroup = null;
        }
    }


}