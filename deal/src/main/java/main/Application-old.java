package main;//package main;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.zhgtrade.deal.core.*;
//import com.zhgtrade.deal.data.JdbcTemplate;
//import com.zhgtrade.deal.listener.DealMarkingListener;
//import com.zhgtrade.deal.listener.DealMarkingListenerImpl;
//import com.zhgtrade.deal.listener.EntrustListener;
//import com.zhgtrade.deal.listener.EntrustListenerImpl;
//import com.zhgtrade.deal.market.*;
//import com.zhgtrade.deal.model.FentrustData;
//import com.zhgtrade.deal.model.FentrustlogData;
//import com.zhgtrade.deal.mq.MessageQueueService;
//import com.zhgtrade.deal.mq.RabbatMessageQueueServiceImpl;
//import com.zhgtrade.deal.task.AutoDealingOneDayData;
//import com.zhgtrade.deal.util.Config;
//import com.zhgtrade.deal.util.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//
///**
// * 招股金服
// * CopyRight : www.zhgtrade.com
// * Author : 林超（362228416@qq.com）
// * Date： 2016-05-10 14:32
// */
//public class Application {
//
//    private static final Logger log = LoggerFactory.getLogger(Application.class);
//
//    private Map<Class, Object> beans = new HashMap<>();
//
//    public Application() {
//        beans.put(ObjectMapper.class, objectMapper());
//        beans.put(DataSource.class, dataSource());
//        beans.put(JdbcTemplate.class, jdbcTemplate());
//        beans.put(JedisPool.class, jedisPool());
//        beans.put(ExecutorService.class, executorService());
//        beans.put(SyncTaskService.class, syncTaskService());
//        beans.put(MessageQueueService.class, messageQueueService());
//        beans.put(MarkService.class, markService());
//        beans.put(DepthEntrustService.class, depthEntrustService());
//        beans.put(DepthCalculateQueue.class, depthCalculateQueue());
//        beans.put(CacheDataService.class, fentrustlogDataService());
//        beans.put(DealMarkingListener.class, dealMarkingListener());
//        beans.put(EntrustListener.class, entrustListener());
//        beans.put(MatchingEngine.class, matchingEngine());
//        beans.put(TradingSystem.class, tradingSystem());
//        beans.put(MessageCenter.class, messageCenter());
//        beans.put(AutoDealingOneDayData.class, autoDealingOneDayData());
//    }
//
//    public <T> T getBean(Class<T> clazz) {
//        return (T) beans.get(clazz);
//    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper();
//    }
//
//    @Bean
//    public JedisPool jedisPool() {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        Config config = Config.getInstance();
//        String host = config.getProperty("redis.host");
//        Integer port = Integer.valueOf(config.getProperty("redis.port"));
//        Integer timeout = Integer.valueOf(config.getProperty("redis.timeout"));
//        Integer maxIdle = Integer.valueOf(config.getProperty("redis.maxIdle"));
//        Integer maxTotal = Integer.valueOf(config.getProperty("redis.maxTotal"));
//        Integer minIdle = Integer.valueOf(config.getProperty("redis.minIdle"));
//        Integer maxWaitMillis = Integer.valueOf(config.getProperty("redis.maxWaitMillis"));
//        Boolean testOnBorrow = Boolean.valueOf(config.getProperty("redis.testOnBorrow"));
//        String password = config.getProperty("redis.password");
//        poolConfig.setMaxIdle(maxIdle);
//        poolConfig.setMaxTotal(maxTotal);
//        poolConfig.setMinIdle(minIdle);
//        poolConfig.setMaxWaitMillis(maxWaitMillis);
//        poolConfig.setTestOnBorrow(testOnBorrow);
//        log.debug("init jedisPool host = {}, port = {}, password = {}", host, port, password.substring(0, 2));
//        JedisPool jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
//        return jedisPool;
//    }
//
//    @Bean
//    public DepthEntrustService depthEntrustService() {
////        return new DepthEntrustServiceImpl(getBean(JedisPool.class));
//        return new MemoryDepthEntrustServiceImpl();
//    }
//
//    @Bean
//    public ExecutorService executorService() {
//        return Executors.newFixedThreadPool(50);
//    }
//
//    @Bean
//    public DepthCalculateQueue depthCalculateQueue() {
//        DepthCalculateQueue depthCalculateQueue = new DepthCalculateQueue(getBean(DepthEntrustService.class), getBean(ExecutorService.class), getBean(MessageQueueService.class), getBean(JedisPool.class));
//        depthCalculateQueue.init();
//        return depthCalculateQueue;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        Config config = Config.getInstance();
//        DruidDataSource ds = new DruidDataSource();
//        String url = config.getProperty("jdbc.url");
//        String username = config.getProperty("jdbc.username");
//        String password = config.getProperty("jdbc.password");
//        ds.setUrl(url);
//        ds.setUsername(username);
//        ds.setPassword(password);
//        log.debug("init DataSource url = {}, username = {}", url, username);
//        ds.setInitialSize(1);
//        ds.setMinIdle(1);
//        ds.setMaxActive(50);
//        ds.setPoolPreparedStatements(true);
//        try {
//            ds.init();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(-1);
//        }
//        return ds;
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate(getBean(DataSource.class));
//    }
//
//    @Bean
//    public MessageQueueService messageQueueService() {
//        return new RabbatMessageQueueServiceImpl();
//    }
//
//    @Bean
//    public CacheDataService fentrustlogDataService() {
//        return new CacheDataServiceImpl(getBean(JedisPool.class));
//    }
//
//    @Bean
//    public MatchingEngine matchingEngine() {
//        return new MatchingEngine(getBean(DataSource.class), getBean(ObjectMapper.class), getBean(DealMarkingListener.class));
//    }
//
//    @Bean
//    public DealMarkingListener dealMarkingListener() {
//        return new DealMarkingListenerImpl(getBean(CacheDataService.class), getBean(MessageQueueService.class));
//    }
//
//    @Bean
//    public TradingSystem tradingSystem() {
//        return new TradingSystem(getBean(MatchingEngine.class), getBean(EntrustListener.class), getBean(DealMarkingListener.class));
//    }
//
//    @Bean
//    public EntrustListener entrustListener() {
//        return new EntrustListenerImpl(getBean(DepthCalculateQueue.class), getBean(SyncTaskService.class), getBean(JedisPool.class));
//    }
//
//    @Bean
//    public MessageCenter messageCenter() {
//        return new MessageCenter(getBean(EntrustListener.class), getBean(MessageQueueService.class), this);
//    }
//
//    @Bean
//    public MarkService markService() {
//        return new MarkServiceImpl(getBean(JdbcTemplate.class));
//    }
//
//    @Bean
//    public SyncTaskService syncTaskService() {
//        SyncTaskServiceImpl service = new SyncTaskServiceImpl();
//        service.init();
//        return service;
//    }
//
//    @Bean
//    public AutoDealingOneDayData autoDealingOneDayData() {
//        return new AutoDealingOneDayData(getBean(CacheDataService.class), getBean(MarkService.class));
//    }
//
//    @PostConstruct
//    public void loadEntrustFromDb() throws Exception {
//
//        MessageCenter messageCenter = getBean(MessageCenter.class);
//        JedisPool jedisPool = getBean(JedisPool.class);
//        DataSource dataSource = getBean(DataSource.class);
//        CacheDataService cacheDataService = getBean(CacheDataService.class);
//        ObjectMapper objectMapper = getBean(ObjectMapper.class);
//        DepthEntrustService depthEntrustService = getBean(DepthEntrustService.class);
//        TradingSystem tradingSystem = getBean(TradingSystem.class);
//
//        List<FentrustData> list = new ArrayList<>();
//
//        // clear depth data
//        depthEntrustService.clearDepthEntrust();
//
//        Jedis jedis = jedisPool.getResource();
//
//        try {
//            jedis.del("depth");
//        } finally {
//            jedis.close();
//        }
//
//        // get all fviFid
//        log.info("load fvirtualcointype");
//        List<Integer> fids = new ArrayList<>();
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement ps = conn.prepareStatement("select fid from fvirtualcointype where fstatus = 1")) {
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                fids.add(rs.getInt("fid"));
//            }
//            rs.close();
//        }
//
//        // get deal price
//        log.info("load deal price");
//        for (Integer fid : fids) {
//            double price = 0D;
//            try (Connection conn = dataSource.getConnection();
//                 PreparedStatement ps = conn.prepareStatement("select fPrize from fentrustlog WHERE FVI_type = ? ORDER BY fid desc LIMIT 1")) {
//                ps.setInt(1, fid);
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    price = rs.getDouble("fPrize");
//                }
//                rs.close();
//            }
//
//            tradingSystem.updateMarking(fid, price);
//        }
//
//        // load entrust from db
//        log.info("load entrust");
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement ps = conn.prepareStatement("select c.flevel, b.fneedfee, a.fid, a.FUs_fId, a.fVi_fId, a.fCreateTime, a.fEntrustType, a.fPrize, a.fAmount, a.fsuccessAmount, a.fCount, a.fleftCount, a.fStatus, a.flastUpdatTime, a.ffees, a.fleftfees, a.robotStatus \n" +
//                     "\tfrom fentrust a, fuser b, fscore c where (a.fstatus = 1 or a.fstatus = 2) and a.fisLimit = 0 AND a.FUs_fId = b.fid and b.fscoreid = c.fid")) {
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                FentrustData fentrustData = objectMapper.parse(rs, FentrustData.class);
//                list.add(fentrustData);
//            }
//            rs.close();
//        }
//
//        // load entrustlog from db
//
//        log.info("load Fentrustlog");
//        for (Integer fid : fids) {
//            try (Connection conn = dataSource.getConnection();
//                 PreparedStatement ps = conn.prepareStatement("SELECT fid,FVI_type,fEntrustType,fPrize,fAmount,fCount,isactive,fCreateTime from Fentrustlog where FVI_type = ? and isactive = 1 and fcreateTime > ? order by fid desc limit 25")) {
//                ps.setInt(1, fid);
//                ps.setObject(2, new Date(System.currentTimeMillis() - 24*60L*60*1000));
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    FentrustlogData log = objectMapper.parse(rs, FentrustlogData.class);
//                    cacheDataService.addFentrustLogData(log);
//                }
//                rs.close();
//            }
//        }
//
//        // TODO 调试K线
//
//        // add to trading system
//        log.info("send entrust to message center size " + list.size());
//        list.forEach(messageCenter::onMessage);
//    }
//
//    public static void main(String[] args) throws Exception {
//        long startTime = System.currentTimeMillis();
//        Application application = new Application();
//        application.loadEntrustFromDb();
//        MessageCenter messageCenter = application.getBean(MessageCenter.class);
//        ExecutorService executorService = application.getBean(ExecutorService.class);
//        AutoDealingOneDayData autoDealingOneDayData = application.getBean(AutoDealingOneDayData.class);
//
//        System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
//                "                   _ooOoo_\n" +
//                "                  o8888888o\n" +
//                "                  88\" . \"88\n" +
//                "                  (| -_- |)\n" +
//                "                  O\\  =  /O\n" +
//                "               ____/`---'\\____\n" +
//                "             .'  \\\\|     |//  `.\n" +
//                "            /  \\\\|||  :  |||//  \\\n" +
//                "           /  _||||| -:- |||||-  \\\n" +
//                "           |   | \\\\\\  -  /// |   |\n" +
//                "           | \\_|  ''\\---/''  |   |\n" +
//                "           \\  .-\\__  `-`  ___/-. /\n" +
//                "         ___`. .'  /--.--\\  `. . __\n" +
//                "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".\n" +
//                "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
//                "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n" +
//                "======`-.____`-.___\\_____/___.-`____.-'======\n" +
//                "                   `=---='\n" +
//                "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
//                "");
//        messageCenter.init();
//
//        executorService.execute(autoDealingOneDayData);
//
//        log.info("server started {} ms.", (System.currentTimeMillis() - startTime));
//
//    }
//
//}
