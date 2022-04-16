package fun.tans.seckill.redis;

public interface KeyPrefix {

    int expireSecond();

    String getPrefix();
}
