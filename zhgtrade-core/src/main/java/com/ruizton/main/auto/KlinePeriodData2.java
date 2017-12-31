package com.ruizton.main.auto;

import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.model.Fperiod;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.cache.data.KlineDataService;
import com.ruizton.main.cache.data.impl.KlineDataServiceImpl;
import com.ruizton.main.service.front.FrontOthersService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.*;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-21 00:59
 */
public class KlinePeriodData2 {

    @Autowired
    private FrontOthersService frontOthersService ;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService ;
    @Autowired
    private RealTimeData realTimeData;
    @Autowired
    private KlineDataService klineDataService;

    private Map<Integer, String[]> json = new HashMap<Integer, String[]>() ;
    private Map<Integer, String> indexJson = new HashMap<Integer, String>() ;
    private Map<Integer, String> indexJson2 = new HashMap<Integer, String>() ;

    private Map<Integer, TreeSet<Fperiod>> oneMiniteData = new HashMap<Integer, TreeSet<Fperiod>>() ;

    private Map<Integer, Map<Integer, TreeSet<Fperiod>>> periodMap = new HashMap<Integer, Map<Integer,TreeSet<Fperiod>>>() ;
    private Map<Integer, Map<Integer, TreeSet<Fperiod>>> containerMap = new HashMap<Integer, Map<Integer,TreeSet<Fperiod>>>() ;
    private long[] timeStep =
            new long[]{
                    1,//1h
                    3,//3d
                    5,//5d
                    15,
                    30,
                    1*60,//30d
                    2*60,
                    4*60,
                    6*60,
                    12*60,
                    1*24*60,
                    3*64*24,
                    7*64*24
            } ;

    Integer[] keys = null ;

    @PostConstruct
    private void readData(){
        keys = new Integer[13] ;
        for (int i = 0; i < 13; i++) {
            keys[i] = i ;
        }
//        long now = Utils.getTimestamp().getTime() ;

        List<Fvirtualcointype> fvirtualCoinTypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal) ;
        for (int i = 0; i < fvirtualCoinTypes.size(); i++) {
            Fvirtualcointype fvirtualCoinType = fvirtualCoinTypes.get(i) ;

            Map<Integer, TreeSet<Fperiod>> subMap = new HashMap<Integer, TreeSet<Fperiod>>() ;
            for (int j = 0; j < keys.length; j++) {
                Integer key = keys[j] ;
                TreeSet<Fperiod> fperiodSet = new TreeSet<Fperiod>(this.comparator) ;
                subMap.put(key, fperiodSet) ;
            }
            periodMap.put(fvirtualCoinType.getFid(), subMap) ;

            Map<Integer, TreeSet<Fperiod>> subMap2 = new HashMap<Integer, TreeSet<Fperiod>>() ;
            for (int j = 0; j < keys.length; j++) {
                Integer key = keys[j] ;
                TreeSet<Fperiod> fperiodSet = new TreeSet<Fperiod>(this.comparator) ;
                subMap2.put(key, fperiodSet) ;
            }
            containerMap.put(fvirtualCoinType.getFid(), subMap2) ;

            //oneMiniteData
            TreeSet<Fperiod> fperiodSets2 = new TreeSet<Fperiod>(this.comparator) ;
            this.oneMiniteData.put(fvirtualCoinType.getFid(), fperiodSets2) ;

            //json
            String[] jsonStrings = new String[keys.length] ;
            json.put(fvirtualCoinType.getFid(), jsonStrings) ;
            //index json
            String indexJsonStrings = new String("[]") ;
            indexJson.put(fvirtualCoinType.getFid(), indexJsonStrings) ;
            //index json
            String indexJsonStrings2 = new String("[]") ;
            indexJson2.put(fvirtualCoinType.getFid(), indexJsonStrings2) ;
        }

        // 读取半年的的数据，待优化
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, -180);
//        long fromTime = calendar.getTimeInMillis();
        long fromTime = 0;

        long begin = System.currentTimeMillis();
        System.out.println("初始化K线数据");
        //read from database
        for (int i = 0; i < fvirtualCoinTypes.size(); i++) {
            Fvirtualcointype fvirtualCoinType = fvirtualCoinTypes.get(i) ;
            List<Fperiod> fperiods = this.frontOthersService.findAllFperiod(fromTime, fvirtualCoinType.getFid()) ;

            for (int j=0;j<fperiods.size();j++) {
                Fperiod fperiod = fperiods.get(j) ;
                this.addFperiod(fvirtualCoinType.getFid(),keys[0], fperiod) ;
            }
            generateJson(fvirtualCoinType.getFid());
            generateIndexJson(fvirtualCoinType.getFid());
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - begin) + "ms.");
    }

    public synchronized void addFperiod(int id,int key,Fperiod fperiod){
        Map<Integer, TreeSet<Fperiod>> tmap = this.periodMap.get(id) ;
        TreeSet<Fperiod> fperiods = tmap.get(key) ;
        fperiods.add(fperiod) ;
        //每种数据的数量是60*24，超出部分剔除掉
        while(fperiods.size()>60*24*30){
            fperiods.remove(fperiods.first()) ;
        }

        tmap.put(key, fperiods) ;
        this.periodMap.put(id, tmap) ;

        //加入待计算容器
        if(key<keys.length-1){
            this.addFperiodContainer(id, key, fperiod) ;
        }
    }

    private synchronized void addFperiodContainer(int id,int key,Fperiod fperiod){
        Map<Integer, TreeSet<Fperiod>> tmap = this.containerMap.get(id) ;
        TreeSet<Fperiod> fperiods = tmap.get(key) ;
        fperiods.add(fperiod) ;
        if(key<keys.length-1){
            //不是最后一个
            long timestep1 = this.timeStep[key] ;
            long timestep2 = this.timeStep[key+1] ;
            long times = timestep2 / timestep1 ;
            if(fperiods.size()>=times){
                Fperiod calRet = this.calculate(fperiods) ;
                this.addFperiod(id, key+1, calRet) ;
                fperiods.clear() ;
            }

        }

        tmap.put(key, fperiods) ;
        this.containerMap.put(id, tmap) ;
    }

    private Fperiod calculate(TreeSet<Fperiod> fperiods){
        double fkai=0F;
        double fgao=0F;
        double fdi=0F;
        double fshou=0F;
        double fliang=0F;
        Timestamp ftime;
        Fperiod fperiod = new Fperiod() ;

        fkai = fperiods.first().getFkai() ;
        fshou = fperiods.last().getFshou() ;
        ftime = fperiods.first().getFtime() ;
        for (Fperiod f : fperiods) {
            fgao = fgao < f.getFgao()?f.getFgao():fgao ;
            if(fdi==0F){
                fdi = f.getFdi() ;
            }else{
                fdi = fdi > f.getFdi() ? f.getFdi() : fdi ;
            }
            fliang += f.getFliang() ;
        }

        fperiod.setFkai(fkai) ;
        fperiod.setFgao(fgao) ;
        fperiod.setFdi(fdi) ;
        fperiod.setFshou(fshou) ;
        fperiod.setFliang(fliang) ;
        fperiod.setFtime(ftime) ;

        return fperiod ;
    }

    private Comparator<Fperiod> comparator = new Comparator<Fperiod>() {

        public int compare(Fperiod o1, Fperiod o2) {
            return o1.getFtime().compareTo(o2.getFtime()) ;
        }
    };

    public void setOneMiniteData(int id,Fperiod fperiod){
        synchronized (oneMiniteData) {
            TreeSet<Fperiod> fperiods = this.oneMiniteData.get(id) ;
            fperiods.add(fperiod) ;
            this.oneMiniteData.put(id, fperiods) ;
        }
    }

    public Fperiod getOneMiniteData(Fvirtualcointype fvirtualcointype,long begin,long end){
        synchronized (oneMiniteData) {

            boolean needCreateNewPeriod = false ;
            int id = fvirtualcointype.getFid() ;
            Fperiod lastFperiod = this.frontOthersService.getLastFperiod(fvirtualcointype) ;

            needCreateNewPeriod = true ;

            if(needCreateNewPeriod){
                //这一分钟的行情begin
                TreeSet<Fperiod> fperiods = this.oneMiniteData.get(id) ;

                TreeSet<Fperiod> usefulFperiods = new TreeSet<Fperiod>(this.comparator) ;
                List<Fperiod> remove = new ArrayList<Fperiod>() ;
                for (Fperiod fperiod : fperiods) {
                    long time = fperiod.getFtime().getTime() ;
                    if(time>=begin && time<=end){
                        usefulFperiods.add(fperiod) ;
                        remove.add(fperiod) ;
                    }else{
                        if(time<begin){
                            remove.add(fperiod) ;
                        }
                    }
                }

                Fperiod fperiod = new Fperiod() ;
                double latest = 0D;
                if(lastFperiod==null){
                    latest = this.realTimeData.getLatestDealPrize(id) ;
                }else{
                    latest = lastFperiod.getFshou() ;
                }
                if(usefulFperiods.size()==0){
                    fperiod.setFgao(latest) ;
                    fperiod.setFkai(latest) ;
                    fperiod.setFdi(latest) ;
                    fperiod.setFshou(latest) ;
                    fperiod.setFliang(0F) ;
                    fperiod.setFtime(new Timestamp(begin)) ;
                }else{
                    fperiod = this.calculate(usefulFperiods) ;
                    fperiod.setFtime(new Timestamp(begin)) ;
                }

                for (Fperiod fperiod2 : remove) {
                    fperiods.remove(fperiod2) ;
                }
                this.oneMiniteData.put(id, fperiods);
                // 如果这分钟没有交易，则不返回数据，免得插入数据库，导致计算K线有问题
                if (usefulFperiods.size() == 0) {
                    return null;
                }
                return fperiod;
                //这一分钟的行情end
            }else{
                return null ;
            }

        }
    }

    public synchronized void setJsonString(int id,int key,String jsonString){
//        synchronized (this.json) {
//            String[] strings = this.json.get(id) ;
//            strings[key] = jsonString ;
//            json.put(id, strings) ;
//        }
        klineDataService.setJsonString(id, key, jsonString);

    }

    public synchronized void generateJson(int id){
        Map<Integer,TreeSet<Fperiod>> map = this.periodMap.get(id) ;
        for (Map.Entry<Integer, TreeSet<Fperiod>> entry : map.entrySet()) {
            TreeSet<Fperiod> fperiods = entry.getValue();
            StringBuffer stringBuffer = new StringBuffer() ;
            stringBuffer.append("[") ;
            int index = 0 ;
            for (Fperiod fperiod : fperiods) {
                index++ ;
                stringBuffer.append("["+(fperiod.getFtime().getTime()/1000)+",0,0,"+fperiod.getFkai()+","+fperiod.getFshou()+","+fperiod.getFgao()+","+fperiod.getFdi()+","+fperiod.getFliang()+"]") ;
                if(index!=fperiods.size()){
                    stringBuffer.append(",") ;
                }
            }
            stringBuffer.append("]") ;
            this.setJsonString(id, entry.getKey(), stringBuffer.toString()) ;
        }
    }

    public synchronized void setIndexJsonString(int id,String jsonString){
//        synchronized (this.indexJson) {
//            this.indexJson.put(id, jsonString) ;
//        }
        klineDataService.setJsonString(id, KlineDataServiceImpl.INDEX_KEY, jsonString);

    }
    public synchronized void setIndexJson2String(int id,String jsonString){
//        synchronized (this.indexJson2) {
//            this.indexJson2.put(id, jsonString) ;
//        }
        klineDataService.setJsonString(id, KlineDataServiceImpl.INDEX_KEY2, jsonString);
    }

    public synchronized void generateIndexJson(int id){
        TreeSet<Fperiod> fperiods = this.periodMap.get(id).get(keys[5]) ;
        StringBuffer stringBuffer = new StringBuffer() ;
        stringBuffer.append("[") ;
        int index = 0 ;
        for (Fperiod fperiod : fperiods) {
            index++ ;
            stringBuffer.append("["+(fperiod.getFtime().getTime())+","+fperiod.getFshou()+"]") ;
            if(index!=fperiods.size()){
                stringBuffer.append(",") ;
            }
        }
        stringBuffer.append("]") ;
        this.setIndexJsonString(id, stringBuffer.toString()) ;

        //交易中心K线图
        fperiods = this.periodMap.get(id).get(keys[5]) ;
        stringBuffer = new StringBuffer() ;
        stringBuffer.append("[") ;
        index = 0 ;
        for (Fperiod fperiod : fperiods) {
            index++ ;
            stringBuffer.append("["+(fperiod.getFtime().getTime())+","+fperiod.getFkai()+","+fperiod.getFgao()+","+fperiod.getFdi()+","+fperiod.getFshou()+","+fperiod.getFliang()+"]") ;
            if(index!=fperiods.size()){
                stringBuffer.append(",") ;
            }
        }
        stringBuffer.append("]") ;

        JSONObject jsonObject = new JSONObject() ;
        jsonObject.accumulate("result", stringBuffer.toString()) ;
        this.setIndexJson2String(id, jsonObject.toString()) ;

    }

}
