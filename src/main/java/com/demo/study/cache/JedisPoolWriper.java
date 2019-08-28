package com.demo.study.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * create cheng 2019/8/21
 */
public class JedisPoolWriper {
    private JedisPool jedisPool;

    public JedisPoolWriper(JedisPoolConfig poolConfig,String host,int port){
        jedisPool=new JedisPool(poolConfig,host,port);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
