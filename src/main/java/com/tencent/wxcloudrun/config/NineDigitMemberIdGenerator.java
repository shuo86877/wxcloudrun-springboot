package com.tencent.wxcloudrun.config;

/**
 * 分布式9位会员编号生成器
 * 原理：基于Snowflake算法，但仅取其低9位数字
 */
public class NineDigitMemberIdGenerator {

    // 机器ID（0~31）
    private final long workerId;
    // 数据中心ID（0~31）
    private final long datacenterId;
    // 序列号
    private long sequence = 0L;

    // 时间戳、机器、数据中心位偏移
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    private final long sequenceBits = 12L;

    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

    public NineDigitMemberIdGenerator(long workerId, long datacenterId) {
        if (workerId > 31 || workerId < 0)
            throw new IllegalArgumentException("workerId must be between 0 and 31");
        if (datacenterId > 31 || datacenterId < 0)
            throw new IllegalArgumentException("datacenterId must be between 0 and 31");
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = currentTime();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = nextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        long snowflakeId = ((timestamp - 1609459200000L) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;

        // 保证9位长度，取最后9位
        return snowflakeId % 1_000_000_000L;
    }

    private long nextMillis(long lastTimestamp) {
        long timestamp = currentTime();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTime();
        }
        return timestamp;
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }

    /***
     * 使用方式
     * // 初始化：不同节点用不同 workerId, datacenterId
     * NineDigitMemberIdGenerator generator = new NineDigitMemberIdGenerator(1, 1);
     *
     * // 注册用户时生成9位会员编号
     * WxUser user = new WxUser();
     * user.setId(UUID.randomUUID().toString());
     * user.setMemberId(generator.nextId());
     * user.setOpenid(openid);
     * wxUserMapper.insert(user);
     *
     * System.out.println("生成会员编号：" + user.getMemberId()); // e.g. 823641275
     */
}
