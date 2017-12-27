package com.ruizton.main.service.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ruizton.main.Enum.QuestionStatusEnum;
import com.ruizton.main.Enum.QuestionTypeEnum;
import com.ruizton.main.dao.FquestionDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Fquestion;
import com.ruizton.main.model.Fwallet;
import com.ruizton.util.Utils;

@Service
public class QuestionService {
	@Autowired
	private FquestionDAO questionDAO;

	public Fquestion findById(int id) {
		return this.questionDAO.findById(id);
	}

	public void saveObj(Fquestion obj) {
		this.questionDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fquestion obj = this.questionDAO.findById(id);
		this.questionDAO.delete(obj);
	}

	public void updateObj(Fquestion obj) {
		this.questionDAO.attachDirty(obj);
	}

	public List<Fquestion> findByProperty(String name, Object value) {
		return this.questionDAO.findByProperty(name, value);
	}

	public List<Fquestion> findAll() {
		return this.questionDAO.findAll();
	}

	public List<Fquestion> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fquestion> all = this.questionDAO.list(firstResult, maxResults, filter,isFY);
		for (Fquestion fquestion : all) {
//			if(fquestion.getFuser() != null){
//				fquestion.getFuser().getFemail();
//			}
			if(fquestion.getFadmin() != null){
				fquestion.getFadmin().getFname();
			}		
		}
		return all;
	}
	
	/**
	 * 
	 * @param name			提问人
	 * @param phoneNum		提问人手机
	 * @param status		提问状态
	 * @param type			提问类型
	 * @param startDate		提问时间
	 * @param endDate		提问时间
	 * @return
	 */
	public int countForPage(String name, String phoneNum, Short status, Short type, String startDate, String endDate){
		return questionDAO.countForPage(name, phoneNum, status, type, startDate, endDate);
	}
	
	/**
	 * 回复提问
	 * 
	 * @param id
	 * @param fanswer
	 * @param fadmin
	 */
	public void updateReplyQuestion(int id, String fanswer, Fadmin fadmin){
		Fquestion question = questionDAO.findById(id);
		question.setfSolveTime(Utils.getTimestamp());
		question.setFanswer(fanswer);
		question.setFstatus(QuestionStatusEnum.SOLVED);
		question.setFadmin(fadmin);
		questionDAO.attachDirty(question);
	}
}

