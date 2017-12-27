package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fwallet;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.criterion.Example.create;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fwallet entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FwalletDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(FwalletDAO.class);
	// property constants
	public static final String FTOTAL_RMB = "ftotalRmb";
	public static final String FFROZEN_RMB = "ffrozenRmb";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(Fwallet transientInstance) {
		log.debug("saving Fwallet instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fwallet persistentInstance) {
		log.debug("deleting Fwallet instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fwallet findByIdNative(int id) {
		Fwallet wallet;
		try {
			wallet = jdbcTemplate.queryForObject("select fid, fTotalRMB, fFrozenRMB FROM fwallet WHERE fId = ?", new RowMapper<Fwallet>() {
				@Override
				public Fwallet mapRow(ResultSet rs, int rowNum) throws SQLException {
					Fwallet wallet = new Fwallet();
					wallet.setFid(rs.getInt("fid"));
					wallet.setFtotalRmb(rs.getDouble("fTotalRMB"));
					wallet.setFfrozenRmb(rs.getDouble("fFrozenRMB"));
					return wallet;
				}
			}, id);
//			SQLQuery query = getSession().createSQLQuery();
//			query.setParameter(0, id);
//			Object[] ret = (Object[]) query.uniqueResult();
//			if (ret != null) {
//				wallet.setFid(NumberUtils.toInt(ret[0] + ""));
//				wallet.setFtotalRmb(NumberUtils.toDouble(ret[1] + ""));
//				wallet.setFfrozenRmb(NumberUtils.toDouble(ret[2] + ""));
//			}
		} catch (Exception e) {
			wallet = new Fwallet();
		}
		return wallet;
	}

	public Fwallet findById(java.lang.Integer id) {
		log.debug("getting Fwallet instance with id: " + id);
		try {
			Fwallet instance = (Fwallet) getSession().get(
					"com.ruizton.main.model.Fwallet", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fwallet> findByExample(Fwallet instance) {
		log.debug("finding Fwallet instance by example");
		try {
			List<Fwallet> results = (List<Fwallet>) getSession()
					.createCriteria("com.ruizton.main.model.Fwallet")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fwallet instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fwallet as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fwallet> findByFtotalRmb(Object ftotalRmb) {
		return findByProperty(FTOTAL_RMB, ftotalRmb);
	}

	public List<Fwallet> findByFfrozenRmb(Object ffrozenRmb) {
		return findByProperty(FFROZEN_RMB, ffrozenRmb);
	}

	public List findAll() {
		log.debug("finding all Fwallet instances");
		try {
			String queryString = "from Fwallet";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fwallet merge(Fwallet detachedInstance) {
		log.debug("merging Fwallet instance");
		try {
			Fwallet result = (Fwallet) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fwallet instance) { 
		log.debug("attaching dirty Fwallet instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful"); 
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fwallet instance) {
		log.debug("attaching clean Fwallet instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List<Fwallet> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		List<Fwallet> list = null;
		log.debug("finding Fwallet instance with filter");
		try {
			String queryString = "from Fwallet " + filter;
			Query queryObject = getSession().createQuery(queryString);
			if (isFY) {
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
		return list;
	}

	public int updateRmb(final Fwallet fwallet) {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				// set 用户金额 = 用户金额 - 交易金额，冻结金额 = 冻结金额 - 交易金额，where 用户金额 >= 交易金额
				Query query = session.createQuery("update Fwallet set ftotalRmb = ftotalRmb - ?, ffrozenRmb = ffrozenRmb + ?, flastUpdateTime = ? where fid = ? and ftotalRmb >= ?");
				query.setDouble(0, fwallet.getFtotalRmb());
				query.setDouble(1, fwallet.getFtotalRmb());
				query.setTimestamp(2, fwallet.getFlastUpdateTime());
				query.setInteger(3, fwallet.getFid());
				query.setDouble(4, fwallet.getFtotalRmb());
				int updateRow = query.executeUpdate();
				return updateRow;
			}
		});
	}

	//更新钱包的冻结资金
	public int updateFwalletFrozen(final int fid, final double frozen){
		
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "update Fwallet set fFrozenRMB = ffrozenRmb - ?, flastUpdateTime = ? where fid = ? and ffrozenRmb + 0.2 >= ?";
				Query query = session.createQuery(sql);
				query.setDouble(0, frozen);
				query.setTimestamp(1, Utils.getTimestamp());
				query.setInteger(2, fid);
				query.setDouble(3, frozen);
				return query.executeUpdate();
			}
		});
	}
	//更新资金
	public int updateFwalletMoneyAndFrozen(final int fid, final double money){
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "update fwallet set fFrozenRMB = ffrozenRmb - ?, ftotalRMB = ftotalRMB + ?, flastUpdateTime= ?, version = version + 1 where fid = ? and ffrozenRmb + 0.2 >= ?";
				Query query = session.createSQLQuery(sql);
				query.setDouble(0, money);
				query.setDouble(1, money);
				query.setTimestamp(2, Utils.getTimestamp());
				query.setInteger(3, fid);
				query.setDouble(4, money);
				return query.executeUpdate();
			}
		});
	}
	//更新资金
	public int updateFwalletMoney(final int fid, final double money){
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "update fwallet set ftotalRMB = ftotalRMB + ?, flastUpdateTime = ? where fid = ?";
				Query query = session.createSQLQuery(sql);
				query.setDouble(0, money);
				query.setTimestamp(1, Utils.getTimestamp());
				query.setInteger(2, fid);
				return query.executeUpdate();
			}
		});
	}

	public boolean withdrawCNY(Fuser fuser,
			Fcapitaloperation fcapitaloperation, long time) {
		log.debug("finding all withdrawCNY instances");
		try {
			String queryString = "update Fwallet set ftotalRmb=ftotalRmb-"
					+ fcapitaloperation.getFamount()
					+ "-"
					+ fcapitaloperation.getFfees()
					+ " ,ffrozenRmb=ffrozenRmb+"
					+ fcapitaloperation.getFamount()
					+ "+"
					+ fcapitaloperation.getFfees()
					+ ",flastUpdateTime=?  where  flastUpdateTime=? and FUs_fId=?";
			SQLQuery queryObject = getSession().createSQLQuery(queryString);
			queryObject.setTimestamp(0, Utils.getTimestamp());
			queryObject.setTimestamp(1, new Timestamp(time));
			queryObject.setInteger(2, fuser.getFid());

			int count = queryObject.executeUpdate();
			if (count == 0) {
				return false;
			} else {
				return true;
			}
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Map getTotalMoney() {
		Map map = new HashMap();
		String sql = "select sum(IFNULL(ftotalRmb,0)+IFNULL(ffrozenRmb,0)) ftotalRmb,sum(IFNULL(ffrozenRmb,0)) ffrozenRmb from Fwallet";
		SQLQuery queryObject = getSession().createSQLQuery(sql);
		List all = queryObject.list();
		if (all.size() > 0) {
			Object[] o = (Object[]) all.get(0);
			map.put("totalRmb", o[0]);
			map.put("frozenRmb", o[1]);
		}
		return map;
	}

	/**
	 * 数据导出
	 * 
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> findForExport(String keyword, int firstResult, int maxResult, boolean toPage){
		StringBuilder sqlbuf = new StringBuilder();
		sqlbuf.append("select u.floginName, u.fNickName, u.fRealName, u.fEmail, u.fTelephone, w.fTotalRMB, w.fFrozenRMB, (w.fTotalRMB + w.fFrozenRMB) as total from fuser u");
		sqlbuf.append(" left join fwallet w on u.FWa_fId = w.fId where w.fTotalRMB + w.fFrozenRMB >= 0");
		if(StringUtils.hasText(keyword)){
			sqlbuf.append(" and (u.floginName like :keyword or u.fRealName like :keyword or u.fTelephone like :keyword or u.fEmail like :keyword)");
		}
		sqlbuf.append(" order by total desc");

		Query query = getSession().createSQLQuery(sqlbuf.toString());

		if(StringUtils.hasText(keyword)){
			query.setParameter("keyword", "%" + keyword.trim() + "%");
		}
		if(toPage){
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResult);
		}
		
		List list = query.list();
		List<Map<String, Object>> ret = new ArrayList<>(list.size());
		for(int i=0, len=list.size(); i<len; i++){
			Object[] obj = (Object[]) list.get(i);
			Map<String, Object> map = new HashMap<>(obj.length);
			map.put("floginName", null != obj[0] ? obj[0].toString() : "");
			map.put("fNickName", null != obj[1] ? obj[1].toString() : "");
			map.put("fRealName", null != obj[2] ? obj[2].toString() : "");
			map.put("fEmail", null != obj[3] ? obj[3].toString() : "");
			map.put("fTelephone", null != obj[4] ? obj[4].toString() : "");
			map.put("fTotalRMB", ((BigDecimal)obj[5]).doubleValue());
			map.put("fFrozenRMB", ((BigDecimal)obj[6]).doubleValue());
			map.put("total", ((BigDecimal)obj[7]).doubleValue());
			ret.add(map);
		}
		
		return ret;
	}

	/**
	 *	获取有冻结资金问题的记录
	 * @param symbol 0：rmb
	 * @param keyword
	 * @param orderField
	 * @param orderDirection
	 * @param offset
     * @param length
     * @return
     */
	public List<Map<String, Object>> findWalletErrorList(Integer symbol, String keyword, String orderField, String orderDirection, int offset, int length, boolean isPage){
		StringBuilder sqlBuf = new StringBuilder();
		if(null == symbol){
			sqlBuf.append("select * from (");
		}
		// 全部或人民币
		if(null == symbol || 0 == symbol){
			sqlBuf.append("select 0 as fvid,u.floginName,u.fRealName,u.fTelephone,u.fEmail,w.fTotalRMB fTotal,w.fFrozenRMB as fFrozen from fwallet w");
			sqlBuf.append(" left join fuser u on u.FWa_fId = w.fId where w.fFrozenRMB < 0");
			if(StringUtils.hasText(keyword)){
				sqlBuf.append(" and (u.floginName like :keyword or u.fTelephone like :keyword or u.fEmail like :keyword)");
			}
		}
		if(null == symbol){
			sqlBuf.append(" union all ");
		}
		if(null == symbol || symbol > 0){
			sqlBuf.append("select w.fVi_fId fvid,u.floginName,u.fRealName,u.fTelephone,u.fEmail,w.fTotal,w.fFrozen from fvirtualwallet w");
			sqlBuf.append(" left join fuser u on u.fid = w.fuid where w.fFrozen < 0");
			if(StringUtils.hasText(keyword)){
				sqlBuf.append(" and (u.floginName like :keyword or u.fTelephone like :keyword or u.fEmail like :keyword)");
			}
			if(null != symbol && symbol > 0){
				sqlBuf.append(" and w.fVi_fId =:symbol");
			}
		}
		if(null == symbol){
			sqlBuf.append(") as t");
		}
		if(StringUtils.hasText(orderDirection) && StringUtils.hasText(orderDirection)){
			sqlBuf.append(" order by ").append(orderField).append(" ").append(orderDirection);
		}else{
			sqlBuf.append(" order by fFrozen");
		}

		SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());
		if(null != symbol && symbol > 0){
			query.setParameter("symbol", symbol);
		}
		if(StringUtils.hasText(keyword)){
			query.setParameter("keyword", "%" + keyword + "%");
		}
		if(isPage){
			query.setFirstResult(offset);
			query.setMaxResults(length);
		}

		List list = query.list();
		List<Map<String, Object>> retList = new ArrayList<>(list.size());
		HashMap<String, Object> objMap = new HashMap(10);
		for(Object obj : list){
			Object[] objArr = (Object[]) obj;
			objMap.put("fvid", objArr[0]);
			objMap.put("floginName", objArr[1]);
			objMap.put("fRealName", objArr[2]);
			objMap.put("fTelephone", objArr[3]);
			objMap.put("fEmail", objArr[4]);
			objMap.put("fTotal", objArr[5]);
			objMap.put("fFrozen", objArr[6]);
			retList.add((HashMap)objMap.clone());
		}

		return retList;
	}

	public int countRMBWalletrError(String keyword){
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select count(1) from fwallet w");
		sqlBuf.append(" left join fuser u on u.FWa_fId = w.fId where w.fFrozenRMB < 0");
		if(StringUtils.hasText(keyword)){
			sqlBuf.append(" and (u.floginName like :keyword or u.fTelephone like :keyword or u.fEmail like :keyword or u.fRealName like :keyword)");
		}

		SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());
		if(StringUtils.hasText(keyword)){
			query.setParameter("keyword", "%" + keyword + "%");
		}
		return ((BigInteger)query.uniqueResult()).intValue();
	}

	public int countCoinWalletError(Integer symbol, String keyword){
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select count(1) from fvirtualwallet w");
		sqlBuf.append(" left join fuser u on u.fid = w.fuid where w.fTotal < 0");
		if(StringUtils.hasText(keyword)){
			sqlBuf.append(" and (u.floginName like :keyword or u.fTelephone like :keyword or u.fEmail like :keyword or u.fRealName like :keyword)");
		}
		if(null != symbol && symbol > 0){
			sqlBuf.append(" and w.fVi_fId =:symbol");
		}
		SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());
		if(null != symbol && symbol > 0){
			query.setParameter("symbol", symbol);
		}
		if(StringUtils.hasText(keyword)){
			query.setParameter("keyword", "%" + keyword + "%");
		}
		return ((BigInteger)query.uniqueResult()).intValue();
	}

}











