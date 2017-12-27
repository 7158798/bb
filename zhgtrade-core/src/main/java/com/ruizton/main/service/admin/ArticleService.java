package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ruizton.main.model.Farticletype;
import com.ruizton.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FarticleDAO;
import com.ruizton.main.model.Farticle;

@Service
public class ArticleService {
	@Autowired
	private FarticleDAO farticleDao;

	public Farticle findById(int id) {
		return this.farticleDao.findById(id);
	}

	public void saveObj(Farticle obj) {
		this.farticleDao.save(obj);
	}

	public void deleteObj(int id) {
		Farticle obj = this.farticleDao.findById(id);
		this.farticleDao.delete(obj);
	}

	public void updateObj(Farticle obj) {
		this.farticleDao.attachDirty(obj);
	}

	public List<Farticle> findByProperty(String name, Object value) {
		return this.farticleDao.findByProperty(name, value);
	}

	public List<Farticle> findAll() {
		return this.farticleDao.findAll();
	}

	public List<Farticle> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Farticle> all = this.farticleDao.list(firstResult, maxResults, filter,isFY);
		for (Farticle farticle : all) {
			farticle.getFarticletype().getFname();
			if(farticle.getFadminByFcreateAdmin() != null){
				farticle.getFadminByFcreateAdmin().getFname();
			}
			if(farticle.getFadminByFmodifyAdmin() != null){
				farticle.getFadminByFmodifyAdmin().getFname();
			}
		}
		return all;
	}

	public Farticle findOne(Farticletype farticletype){
		List<Farticle> all = this.farticleDao.list(0, 1, "where farticletype.fid=" + farticletype.getFid() + " order by fid desc", true);
		if(CollectionUtils.isEmpty(all)){
			return null;
		}

		return all.get(0);
	}

}