package com.ruizton.util;

import com.ruizton.main.Enum.HedginglogStatusEnum;
import com.ruizton.main.Enum.HedginglogTypeEnum;
import com.ruizton.main.model.Fhedging;
import com.ruizton.main.model.Fhedginglog;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.HedgingService;
import com.ruizton.main.service.admin.HedginglogService;
import com.ruizton.main.cache.data.RealTimeDataService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HedgingUtils {
	@Autowired
	private HedgingService hedgingService;
	@Autowired
	private HedginglogService hedginglogService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private RealTimeDataService realTimeDataService;

	public void work() {
		synchronized (this) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
			String now = sdf.format(new Date());
			String filter = "where 1=1";
			List<Fhedging> fhedgings = this.hedgingService.list(0, 0, filter, false);
			for (Fhedging fhedging : fhedgings) {
				//查看有没有未结束的,没有新增
				try {
					String ss = "where fhedging.fid="+fhedging.getFid()+" and fstatus <>"+HedginglogStatusEnum.END;
					int cc = this.adminService.getAllCount("Fhedginglog", ss);
					if(cc == 0){
						Fhedginglog log = new Fhedginglog();
						log.setFcreatetime(Utils.getTimestamp());
						log.setFhedging(fhedging);
						log.setFtotal1(0d);
						log.setFtotal2(0d);
						log.setFtotal3(0d);
						log.setFstartprice(0d);
						log.setFendprice(0d);
						log.setFstatus(HedginglogStatusEnum.NOSTART);
						log.setFresult(HedginglogTypeEnum.NO);
						log.setFnumber(sdf.format(new Date()));
						log.setFstartpricetime(Timestamp.valueOf(now+" "+sdf1.format(fhedging.getFstartpricetime())));
						log.setFendpricetime(Timestamp.valueOf(now+" "+sdf1.format(fhedging.getFendpricetime())));
						log.setFstarttime(Timestamp.valueOf(now+" "+sdf1.format(fhedging.getFstarttime())));
						log.setFendtime(Timestamp.valueOf(now+" "+sdf1.format(fhedging.getFendtime())));
						if(log.getFendpricetime().getTime() < new Date().getTime()){
							continue;
						}
						if(log.getFstartpricetime().getTime() < new Date().getTime()){
							continue;
						}
						log.setFvirtualcointypeByFid(fhedging.getFvirtualcointypeByFid());
						log.setFvirtualcointypeByFvid2(fhedging.getFvirtualcointypeByFvid2());
						this.hedginglogService.saveObj(log);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		//取新的一期的价格
		String sql = "where fstatus="+HedginglogStatusEnum.NOSTART;
		List<Fhedginglog> fhedginglogs = this.hedginglogService.list(0, 0, sql, false);
		for (Fhedginglog fhedginglog : fhedginglogs) {
			if(fhedginglog.getFstatus() != HedginglogStatusEnum.NOSTART) continue;
			String url = fhedginglog.getFhedging().getFpriceurl();
			long priceTime = fhedginglog.getFstartpricetime().getTime();
			long startTime = fhedginglog.getFstarttime().getTime();
			long now = new Date().getTime();
			//取价格
			if(now >= priceTime && fhedginglog.getFstartprice() ==0d){
				if(url != null && url.trim().length() >0){
					try {
						trustAllHttpsCertificates();
						HttpsURLConnection.setDefaultHostnameVerifier(hv);
			    		URL xx = new URL(url) ;
			    		BufferedReader br = new BufferedReader(new InputStreamReader( xx.openStream()) ) ;
			    		StringBuffer sb = new StringBuffer() ;
			    		String tmp = null ;
			    		while((tmp=br.readLine())!=null){
			    			sb.append(tmp) ;
			    		}
		    		
		    			JSONObject s = (JSONObject)JSONObject.fromObject(sb.toString()).get("ticker");
		    			double last1 = Double.valueOf(s.get("last").toString());
		    			fhedginglog.setFstartprice(last1);
		    		} catch (Exception e1) {
		    			continue;
		    		}
				}else{
					double price = this.realTimeDataService.getLatestDealPrize(fhedginglog.getFvirtualcointypeByFid().getFid());
					fhedginglog.setFstartprice(price);
				}
			}
			//设置状态
            if(now >= startTime){
            	fhedginglog.setFstatus(HedginglogStatusEnum.TAKING);
			}
			this.hedginglogService.updateObj(fhedginglog);
		}
		
		//设置成开奖中
		String ss = "where fstatus="+HedginglogStatusEnum.TAKING;
		List<Fhedginglog> fhedginglogss = this.hedginglogService.list(0, 0, ss, false);
		for (Fhedginglog fhedginglog : fhedginglogss) {
			if(fhedginglog.getFstatus() != HedginglogStatusEnum.TAKING) continue;
			long endTime = fhedginglog.getFendtime().getTime();
			long now = new Date().getTime();
			//设置状态
            if(now >= endTime){
            	fhedginglog.setFstatus(HedginglogStatusEnum.DOING);
            	this.hedginglogService.updateObj(fhedginglog);
			}
		}
		
		//设置成结算
		String xx = "where fstatus="+HedginglogStatusEnum.DOING;
		List<Fhedginglog> fhedginglogsss = this.hedginglogService.list(0, 0, xx, false);
		for (Fhedginglog fhedginglog : fhedginglogsss) {
			if(fhedginglog.getFstatus() != HedginglogStatusEnum.DOING) continue;
			long endTime = fhedginglog.getFendpricetime().getTime();
			long now = new Date().getTime();
			String url = fhedginglog.getFhedging().getFpriceurl();
			//设置状态
            if(now >= endTime){
            	fhedginglog.setFstatus(HedginglogStatusEnum.END);
            	if(url != null && url.trim().length() >0){
					try {
			    		URL tt = new URL(url) ;
			    		BufferedReader br = new BufferedReader(new InputStreamReader( tt.openStream()) ) ;
			    		StringBuffer sb = new StringBuffer() ;
			    		String tmp = null ;
			    		while((tmp=br.readLine())!=null){
			    			sb.append(tmp) ;
			    		}
		    		
		    			JSONObject s = (JSONObject)JSONObject.fromObject(sb.toString()).get("ticker");
		    			double last1 = Double.valueOf(s.get("last").toString());
		    			fhedginglog.setFendprice(last1);
		    		} catch (Exception e1) {
		    			e1.printStackTrace();
		    			continue;
		    		}
				}else{
					double price = this.realTimeDataService.getLatestDealPrize(fhedginglog.getFvirtualcointypeByFid().getFid());
					fhedginglog.setFendprice(price);
				}
            	
            	
            	Double upthreshold = fhedginglog.getFhedging().getFupthreshold();
            	Double downthreshold = fhedginglog.getFhedging().getFdownthreshold();
            	if(fhedginglog.getFstartprice()*(1-downthreshold) >= fhedginglog.getFendprice().doubleValue()){
            		fhedginglog.setFresult(HedginglogTypeEnum.DOWN);
            	}else if(fhedginglog.getFstartprice()*(1+upthreshold) <= fhedginglog.getFendprice().doubleValue()){
            		fhedginglog.setFresult(HedginglogTypeEnum.UP);
            	}else{
            		fhedginglog.setFresult(HedginglogTypeEnum.PING);
            	}
            	this.hedginglogService.updateObj(fhedginglog);
			}
		}
	}
	
	HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            System.out.println("Warning: URL Host: " + urlHostName + " vs. "
                               + session.getPeerHost());
            return true;
        }
    };
    
	private void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}

	 class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
}